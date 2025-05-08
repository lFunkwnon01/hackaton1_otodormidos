package com.hacka.config;

import com.hacka.security.JwtAuthenticationFilter;
import com.hacka.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private UserRepository userRepository;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig(jwtAuthenticationFilter, userRepository);
    }

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Act
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder);
        assertTrue(encoder.getClass().getName().contains("BCryptPasswordEncoder"));
    }

    @Test
    void filterChain_ShouldConfigureSecurityFilters() throws Exception {
        // Arrange
        HttpSecurity http = mock(HttpSecurity.class);

        // Act
        SecurityFilterChain filterChain = securityConfig.filterChain(http);

        // Assert
        assertNotNull(filterChain);
    }
}