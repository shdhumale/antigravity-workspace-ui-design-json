package com.example.auth.service;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.ResetPasswordRequest;
import com.example.auth.dto.SignupRequest;
import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return "Login successful";
        }
        return "Invalid username or password";
    }

    public String signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return "Error: Username is already taken!";
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return "Error: Email is already in use!";
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        userRepository.save(user);
        return "User registered successfully!";
    }

    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Optional<User> user = userRepository.findByEmail(resetPasswordRequest.getEmail());
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
            userRepository.save(existingUser);
            return "Password reset successfully";
        }
        return "Error: Email not found";
    }
}
