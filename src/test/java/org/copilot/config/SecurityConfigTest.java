package org.copilot.config;

import org.copilot.config.auth.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {
    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void testUserDetailsService() {
        UserDetailsService uds = securityConfig.userDetailsService();
        assertNotNull(uds);
        assertInstanceOf(InMemoryUserDetailsManager.class, uds);
    }

    @Test
    void testFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        assertDoesNotThrow(() -> securityConfig.filterChain(http, jwtAuthFilter));
    }
}

