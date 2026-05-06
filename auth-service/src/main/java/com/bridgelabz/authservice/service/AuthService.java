package com.bridgelabz.authservice.service;

import com.bridgelabz.authservice.dto.AuthResponse;
import com.bridgelabz.authservice.dto.LoginRequest;
import com.bridgelabz.authservice.dto.RegisterRequest;
import com.bridgelabz.authservice.entity.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    User getCurrentUserProfile(String email);
    java.util.List<User> getAllUsers();
    User updateUser(Long id, java.util.Map<String, Object> userData);
    void deleteUser(Long id);
}

