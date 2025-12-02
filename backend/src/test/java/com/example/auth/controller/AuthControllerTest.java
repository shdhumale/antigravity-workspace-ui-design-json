package com.example.auth.controller;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.ResetPasswordRequest;
import com.example.auth.dto.SignupRequest;
import com.example.auth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private ResetPasswordRequest resetPasswordRequest;

    @BeforeEach
    void setUp() {
        // Initialize test data
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password123");

        resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("testuser@example.com");
        resetPasswordRequest.setNewPassword("newpassword123");
    }

    // ========== Login Tests ==========

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        when(userService.login(any(LoginRequest.class))).thenReturn("Login successful");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Arrange
        when(userService.login(any(LoginRequest.class))).thenReturn("Invalid username or password");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void testLogin_WithEmptyUsername() throws Exception {
        // Arrange
        loginRequest.setUsername("");
        when(userService.login(any(LoginRequest.class))).thenReturn("Invalid username or password");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_WithEmptyPassword() throws Exception {
        // Arrange
        loginRequest.setPassword("");
        when(userService.login(any(LoginRequest.class))).thenReturn("Invalid username or password");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    // ========== Signup Tests ==========

    @Test
    void testSignup_Success() throws Exception {
        // Arrange
        when(userService.signup(any(SignupRequest.class))).thenReturn("User registered successfully!");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    void testSignup_UsernameTaken() throws Exception {
        // Arrange
        when(userService.signup(any(SignupRequest.class))).thenReturn("Error: Username is already taken!");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username is already taken!"));
    }

    @Test
    void testSignup_EmailAlreadyInUse() throws Exception {
        // Arrange
        when(userService.signup(any(SignupRequest.class))).thenReturn("Error: Email is already in use!");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Email is already in use!"));
    }

    @Test
    void testSignup_WithEmptyUsername() throws Exception {
        // Arrange
        signupRequest.setUsername("");
        when(userService.signup(any(SignupRequest.class))).thenReturn("Error: Username is already taken!");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignup_WithInvalidEmail() throws Exception {
        // Arrange
        signupRequest.setEmail("invalidemail");
        when(userService.signup(any(SignupRequest.class))).thenReturn("Error: Email is already in use!");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignup_WithEmptyPassword() throws Exception {
        // Arrange
        signupRequest.setPassword("");
        when(userService.signup(any(SignupRequest.class))).thenReturn("Error: Username is already taken!");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    // ========== Reset Password Tests ==========

    @Test
    void testResetPassword_Success() throws Exception {
        // Arrange
        when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn("Password reset successfully");

        // Act & Assert
        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }

    @Test
    void testResetPassword_EmailNotFound() throws Exception {
        // Arrange
        when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn("Error: Email not found");

        // Act & Assert
        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Email not found"));
    }

    @Test
    void testResetPassword_WithEmptyEmail() throws Exception {
        // Arrange
        resetPasswordRequest.setEmail("");
        when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn("Error: Email not found");

        // Act & Assert
        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testResetPassword_WithEmptyNewPassword() throws Exception {
        // Arrange
        resetPasswordRequest.setNewPassword("");
        when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn("Error: Email not found");

        // Act & Assert
        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testResetPassword_WithInvalidEmail() throws Exception {
        // Arrange
        resetPasswordRequest.setEmail("invalidemail");
        when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn("Error: Email not found");

        // Act & Assert
        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isBadRequest());
    }
}
