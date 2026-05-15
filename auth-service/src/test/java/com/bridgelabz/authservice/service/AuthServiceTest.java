package com.bridgelabz.authservice.service;

import com.bridgelabz.authservice.dto.AuthResponse;
import com.bridgelabz.authservice.dto.LoginRequest;
import com.bridgelabz.authservice.dto.RegisterRequest;
import com.bridgelabz.authservice.entity.User;
import com.bridgelabz.authservice.entity.User.Role;

import com.bridgelabz.authservice.repository.UserRepository;
import com.bridgelabz.authservice.security.JwtProvider;
import com.bridgelabz.authservice.messaging.UserEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserEventPublisher eventPublisher;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFullName("Test User");
        registerRequest.setRole(Role.ADMIN);

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .fullName("Test User")
                .role(Role.ADMIN)
                .isActive(true)
                .build();
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
        when(jwtProvider.generateToken(any(UserDetails.class))).thenReturn("testToken");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("testToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
        when(jwtProvider.generateToken(any(UserDetails.class))).thenReturn("testToken");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testGetCurrentUserProfile() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        User result = authService.getCurrentUserProfile("test@example.com");

        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedData = new User();
        updatedData.setFullName("Updated Name");

        User result = authService.updateUser(1L, updatedData);

        assertEquals("Updated Name", result.getFullName());
    }
}
