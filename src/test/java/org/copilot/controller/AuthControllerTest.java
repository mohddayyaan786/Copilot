package org.copilot.controller;

import org.copilot.config.auth.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private AuthController authController;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        authController = new AuthController();
        // Use reflection to inject mocks into private fields
        Field jwtUtilField = AuthController.class.getDeclaredField("jwtUtil");
        jwtUtilField.setAccessible(true);
        jwtUtilField.set(authController, jwtUtil);
        Field udsField = AuthController.class.getDeclaredField("userDetailsService");
        udsField.setAccessible(true);
        udsField.set(authController, userDetailsService);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        String username = "user";
        String password = "password";
        UserDetails userDetails = new User(username, password, Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.generateToken(username)).thenReturn("mocked-token");
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);
        Map<String, String> response = authController.login(request);
        assertNotNull(response.get("token"));
        assertEquals("mocked-token", response.get("token"));
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        String username = "nouser";
        String password = "password";
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("User not found"));
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);
        assertThrows(RuntimeException.class, () -> authController.login(request));
    }

    @Test
    void login_shouldThrowException_whenPasswordIsIncorrect() {
        String username = "user";
        String password = "wrongpassword";
        UserDetails userDetails = new User(username, "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);
        assertThrows(RuntimeException.class, () -> authController.login(request));
    }

    @Test
    void login_shouldThrowException_whenUsernameOrPasswordIsMissing() {
        Map<String, String> request = new HashMap<>();
        request.put("username", "user");
        assertThrows(RuntimeException.class, () -> authController.login(request));
        request.clear();
        request.put("password", "password");
        assertThrows(RuntimeException.class, () -> authController.login(request));
    }

    @Test
    void login_shouldThrowException_whenRequestMapIsNull() {
        assertThrows(RuntimeException.class, () -> authController.login(null));
    }
}
