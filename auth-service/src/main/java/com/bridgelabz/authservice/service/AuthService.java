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
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    void logout(String email);
    boolean validateToken(String token);
    AuthResponse refreshToken(String token);
    void changePassword(String email, String oldPassword, String newPassword);
    void deactivateUser(Long id);
}

