package com.study.systems.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JWTAuthenticatorTest {

    @Test
    void testGenerateTokenReturnsNonNull() {
        JWTAuthenticator auth = new JWTAuthenticator("my-secret", 3600000L);
        String token = auth.generateToken("user123");

        assertNotNull(token);
    }

    @Test
    void testGenerateTokenHasThreeParts() {
        JWTAuthenticator auth = new JWTAuthenticator("my-secret", 3600000L);
        String token = auth.generateToken("user123");

        assertNotNull(token);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    void testValidateTokenExtractsUserId() {
        JWTAuthenticator auth = new JWTAuthenticator("my-secret", 3600000L);
        String token = auth.generateToken("user123");

        String userId = auth.validateToken(token);

        assertEquals("user123", userId);
    }

    @Test
    void testValidateInvalidTokenReturnsNull() {
        JWTAuthenticator auth = new JWTAuthenticator("my-secret", 3600000L);

        String result = auth.validateToken("invalid.token.here");

        assertNull(result);
    }

    @Test
    void testValidateExpiredTokenReturnsNull() throws InterruptedException {
        // Token expires in 100ms
        JWTAuthenticator auth = new JWTAuthenticator("my-secret", 100L);
        String token = auth.generateToken("user123");

        Thread.sleep(200);

        String result = auth.validateToken(token);
        assertNull(result);
    }

    @Test
    void testTokenFromDifferentSecretIsInvalid() {
        JWTAuthenticator auth1 = new JWTAuthenticator("secret-1", 3600000L);
        JWTAuthenticator auth2 = new JWTAuthenticator("secret-2", 3600000L);

        String token = auth1.generateToken("user123");
        String result = auth2.validateToken(token);

        assertNull(result);
    }
}
