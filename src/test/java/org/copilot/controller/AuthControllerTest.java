package org.copilot.controller;

import org.copilot.config.auth.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthControllerTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        Map<String, String> request = new HashMap<>();
        request.put("username", "user");
        request.put("password", "password");
        UserDetails userDetails = User.withUsername("user").password("password").roles("USER").build();
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtil.generateToken("user")).thenReturn("dummy-token");
        Map<String, String> response = authController.login(request);
        assertEquals("dummy-token", response.get("token"));
    }

    @Test
    void testLoginInvalidCredentials() {
        Map<String, String> request = new HashMap<>();
        request.put("username", "user");
        request.put("password", "wrong");
        UserDetails userDetails = User.withUsername("user").password("password").roles("USER").build();
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        assertThrows(RuntimeException.class, () -> authController.login(request));
    }
}

