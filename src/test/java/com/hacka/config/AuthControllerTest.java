package com.hacka.config;

import com.hacka.user.domain.Role;
import com.hacka.security.JwtUtil;
import com.hacka.user.domain.User;
import com.hacka.user.domain.UserService;
import com.hacka.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {



    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(userService, userRepository, jwtUtil, passwordEncoder);
    }

    @Test
    void register_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
        when(userService.registerUser(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<?> response = authController.register(user);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(userService).registerUser(any(User.class));
    }

    @Test
    void register_InvalidEmail() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("invalid-email");
        user.setPassword("Password123!");
        when(userService.registerUser(any(User.class)))
            .thenThrow(new RuntimeException("Invalid email format"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.register(user));
    }

    @Test
    void register_WeakPassword() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("weak");
        when(userService.registerUser(any(User.class)))
            .thenThrow(new RuntimeException("Password must be at least 8 characters long and contain at least one number, one uppercase letter, and one special character"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.register(user));
    }

    @Test
    void register_DuplicateUsername() {
        // Arrange
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
        when(userService.registerUser(any(User.class)))
            .thenThrow(new RuntimeException("Username already exists"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.register(user));
    }

    @Test
    void login_Success() {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(Role.ROLE_USER);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "USER")).thenReturn("testToken");

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthController.AuthResponse);
        assertEquals("testToken", ((AuthController.AuthResponse) response.getBody()).getToken());
    }

    @Test
    void login_InvalidCredentials() {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciales inválidas", response.getBody());
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.login(request));
    }

    @Test
    void login_InactiveUser() {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.setUsername("inactiveuser");
        request.setPassword("password");

        User user = new User();
        user.setUsername("inactiveuser");
        user.setPassword("encodedPassword");
        user.setActivo(false);

        when(userRepository.findByUsername("inactiveuser")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.login(request));
    }

    @Test
    void login_AdminUser() {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.setUsername("admin");
        request.setPassword("adminpass");

        User user = new User();
        user.setUsername("admin");
        user.setPassword("encodedPassword");
        user.setRole(Role.ROLE_COMPANY_ADMIN);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("adminpass", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin", "ADMIN")).thenReturn("adminToken");

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthController.AuthResponse);
        assertEquals("adminToken", ((AuthController.AuthResponse) response.getBody()).getToken());
    }

    @Test
    void login_EmptyCredentials() {
        // Arrange
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.setUsername("");
        request.setPassword("");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.login(request));
    }
}