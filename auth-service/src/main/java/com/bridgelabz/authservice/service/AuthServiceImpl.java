package com.bridgelabz.authservice.service;

import com.bridgelabz.authservice.dto.AuthResponse;
import com.bridgelabz.authservice.dto.LoginRequest;
import com.bridgelabz.authservice.dto.RegisterRequest;
import com.bridgelabz.authservice.entity.User;
import com.bridgelabz.authservice.repository.UserRepository;
import com.bridgelabz.authservice.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final com.bridgelabz.authservice.messaging.UserEventPublisher eventPublisher;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, com.bridgelabz.authservice.messaging.UserEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .isActive(true)
                .build();

        user = userRepository.save(user);

        // Publish registration event in background thread (non-blocking)
        // This ensures registration succeeds even if RabbitMQ is offline
        final Long savedUserId = user.getId();
        final String savedEmail = user.getEmail();
        final String savedFullName = user.getFullName();
        final String savedRole = user.getRole().name();
        Thread eventThread = new Thread(() -> {
            try {
                eventPublisher.publishUserRegistered(new com.bridgelabz.authservice.messaging.UserEventPublisher.UserRegisteredEvent(
                        savedUserId, savedEmail, savedFullName, savedRole
                ));
            } catch (Exception e) {
                System.err.println("Failed to publish registration event (non-critical): " + e.getMessage());
            }
        });
        eventThread.setDaemon(true);
        eventThread.start();

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtProvider.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtProvider.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public User getCurrentUserProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (userData.getFullName() != null) user.setFullName(userData.getFullName());
        if (userData.getRole() != null) user.setRole(userData.getRole());
        if (userData.getPhone() != null) user.setPhone(userData.getPhone());
        if (userData.getDepartment() != null) user.setDepartment(userData.getDepartment());
        user.setActive(userData.isActive());
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void logout(String email) {
        // In a stateless JWT system, logout is usually handled by the client by deleting the token.
        // For server-side logout, we could implement a token blacklist in Redis.
        System.out.println("User logged out: " + email);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String username = jwtProvider.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtProvider.validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public AuthResponse refreshToken(String token) {
        String username = jwtProvider.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtProvider.validateToken(token, userDetails)) {
            String newToken = jwtProvider.generateToken(userDetails);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return AuthResponse.builder()
                    .token(newToken)
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
        }
        throw new RuntimeException("Invalid token for refresh");
    }

    @Override
    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Incorrect old password");
        }
        
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }
}

