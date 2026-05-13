package com.bridgelabz.website.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatewayJwtFilter extends OncePerRequestFilter {

    @Value("${app.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7);
            
            byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(jwtSecret);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            String username = claims.getSubject();
            
            // Store token for Feign interceptor BEFORE processing
            FeignAuthInterceptor.TOKEN_HOLDER.set(authHeader);

            List<String> roles = claims.get("roles", List.class);
            List<SimpleGrantedAuthority> authorities = roles == null ? List.of() :
                roles.stream()
                     .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("DEBUG [Hub]: Authenticated " + username + " roles=" + authorities);
            }
        } catch (Throwable t) {
            System.err.println("CRITICAL [Hub]: JWT Filter error: " + t.getMessage());
        }

        try {
            // doFilter INSIDE try so ThreadLocal is available during entire request processing
            filterChain.doFilter(request, response);
        } finally {
            // Clean up AFTER request is fully processed
            FeignAuthInterceptor.TOKEN_HOLDER.remove();
        }
    }
}
