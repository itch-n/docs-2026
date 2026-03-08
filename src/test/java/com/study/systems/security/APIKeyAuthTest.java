package com.study.systems.security;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class APIKeyAuthTest {

    @Test
    void testGenerateKeyReturnsNonNull() {
        APIKeyAuth auth = new APIKeyAuth();
        Set<String> scopes = new HashSet<>(Arrays.asList("read", "write"));

        String key = auth.generateKey("service1", scopes);

        assertNotNull(key);
    }

    @Test
    void testValidateKeyWithAllowedScopeReturnsUserId() {
        APIKeyAuth auth = new APIKeyAuth();
        Set<String> scopes = new HashSet<>(Arrays.asList("read", "write"));
        String key = auth.generateKey("service1", scopes);

        String userId = auth.validateKey(key, "write");

        assertEquals("service1", userId);
    }

    @Test
    void testValidateKeyWithReadScopeOnReadOnlyKey() {
        APIKeyAuth auth = new APIKeyAuth();
        Set<String> scopes = new HashSet<>(Arrays.asList("read"));
        String key = auth.generateKey("service2", scopes);

        String userId = auth.validateKey(key, "read");

        assertEquals("service2", userId);
    }

    @Test
    void testValidateKeyWithDisallowedScopeReturnsNull() {
        APIKeyAuth auth = new APIKeyAuth();
        // key2 only has "read" scope
        Set<String> scopes = new HashSet<>(Arrays.asList("read"));
        String key = auth.generateKey("service2", scopes);

        String userId = auth.validateKey(key, "write");

        assertNull(userId);
    }

    @Test
    void testRevokeKeyReturnsTrue() {
        APIKeyAuth auth = new APIKeyAuth();
        String key = auth.generateKey("service1", new HashSet<>(Arrays.asList("read")));

        boolean revoked = auth.revokeKey(key);

        assertTrue(revoked);
    }

    @Test
    void testRevokeNonExistentKeyReturnsFalse() {
        APIKeyAuth auth = new APIKeyAuth();

        boolean revoked = auth.revokeKey("nonexistent-key");

        assertFalse(revoked);
    }

    @Test
    void testValidateRevokedKeyReturnsNull() {
        APIKeyAuth auth = new APIKeyAuth();
        Set<String> scopes = new HashSet<>(Arrays.asList("read"));
        String key = auth.generateKey("service1", scopes);

        auth.revokeKey(key);
        String userId = auth.validateKey(key, "read");

        assertNull(userId);
    }

    @Test
    void testTwoDistinctKeysDontInterfere() {
        APIKeyAuth auth = new APIKeyAuth();
        String key1 = auth.generateKey("service1", new HashSet<>(Arrays.asList("read", "write")));
        String key2 = auth.generateKey("service2", new HashSet<>(Arrays.asList("read")));

        assertNotEquals(key1, key2);
        assertEquals("service1", auth.validateKey(key1, "write"));
        assertNull(auth.validateKey(key2, "write"));
    }
}
