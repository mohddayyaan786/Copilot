package org.copilot.config.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateToken_shouldContainUsernameAndBeValid() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(username, jwtUtil.getUsernameFromToken(token));
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String username = "anotheruser";
        String token = jwtUtil.generateToken(username);
        String extracted = jwtUtil.getUsernameFromToken(token);
        assertEquals(username, extracted);
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        // Create a token with a very short expiration for test
        JwtUtil shortLivedJwtUtil = new JwtUtil() {
            @Override
            public String generateToken(String username) {
                return io.jsonwebtoken.Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(new java.util.Date())
                        .setExpiration(new java.util.Date(System.currentTimeMillis() + 1)) // 1 ms expiry
                        .signWith(this.key, io.jsonwebtoken.SignatureAlgorithm.HS256)
                        .compact();
            }
        };
        String token = shortLivedJwtUtil.generateToken("expireduser");
        Thread.sleep(20); // Wait longer to ensure token is expired
        assertFalse(shortLivedJwtUtil.validateToken(token));
    }
}
