package com.bridgelabz.authservice.controller;

import com.bridgelabz.authservice.dto.AuthResponse;
import com.bridgelabz.authservice.dto.LoginRequest;
import com.bridgelabz.authservice.dto.RegisterRequest;
import com.bridgelabz.authservice.entity.User.Role;

import com.bridgelabz.authservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import com.bridgelabz.authservice.security.JwtFilter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private UserDetailsService userDetailsService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFullName("Test User");
        request.setRole(Role.ADMIN);

        AuthResponse response = AuthResponse.builder()
                .token("testToken")
                .email("test@example.com")
                .role("ADMIN")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        AuthResponse response = AuthResponse.builder()
                .token("testToken")
                .email("test@example.com")
                .role("ADMIN")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"));
    }
}
