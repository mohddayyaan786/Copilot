package org.copilot.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void userDetailsService_shouldReturnInMemoryUserDetailsManager() {
        UserDetailsService uds = securityConfig.userDetailsService();
        assertNotNull(uds);
        assertNotNull(uds.loadUserByUsername("user"));
    }

    @Test
    void unauthenticatedRequestToProtectedEndpoint_shouldReturnUnauthorizedOrNotFoundOrForbidden() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/some-protected-endpoint"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 403 || status == 404,
                        "Should be Unauthorized (401), Forbidden (403), or NotFound (404)");
                });
    }

    @Test
    void unauthenticatedRequestToLogin_shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/login"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertEquals(405, status, "Should be Method Not Allowed (405) since /auth/login only allows POST");
                });
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "user", roles = {"USER"})
    void authenticatedRequestToProtectedEndpoint_shouldBeAllowedOrNotFound() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/some-protected-endpoint"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || status == 404, "Should be allowed (200) or NotFound (404), but not Unauthorized");
                });
    }

    @Test
    void h2ConsoleShouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/h2-console/"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || status == 404, "Should be accessible or NotFound, but not Unauthorized");
                });
    }
}
