package com.bridgelabz.website.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    // ThreadLocal to store the token for the current request
    public static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();

    @Override
    public void apply(RequestTemplate template) {
        // First try ThreadLocal (most reliable)
        String token = TOKEN_HOLDER.get();

        // Fallback: try RequestContextHolder
        if (token == null) {
            try {
                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String authHeader = request.getHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        token = authHeader;
                    }
                }
            } catch (Exception ignored) {}
        }

        if (token != null) {
            template.header("Authorization", token);
        }
    }
}
