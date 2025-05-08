package com.hacka.user.domain;

import com.hacka.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void registerUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.registerUser(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isActivo());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_DuplicateUsername() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.registerUser(user));
    }

    @Test
    void getUserById_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getUsersByCompany_Success() {
        // Arrange
        Long companyId = 1L;
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByCompanyId(companyId)).thenReturn(users);

        // Act
        List<User> result = userService.getUsersByCompany(companyId);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void updateUser_Success() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");

        User updatedUser = new User();
        updatedUser.setEmail("new@example.com");
        updatedUser.setPassword("newpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        Optional<User> result = userService.updateUser(userId, updatedUser);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("new@example.com", result.get().getEmail());
        verify(passwordEncoder).encode("newpassword");
    }
}