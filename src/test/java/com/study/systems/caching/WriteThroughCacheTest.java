package com.study.systems.caching;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class WriteThroughCacheTest {

    // Local mock database implementing WriteThroughCache.Database
    static class MockDb implements WriteThroughCache.Database<String, String> {
        final Map<String, String> storage = new HashMap<>();

        @Override
        public String read(String key) {
            return storage.get(key);
        }

        @Override
        public void write(String key, String value) {
            storage.put(key, value);
        }
    }

    @Test
    void testPutWritesToBothCacheAndDatabase() {
        MockDb db = new MockDb();
        WriteThroughCache<String, String> cache = new WriteThroughCache<>(5, db);

        cache.put("user:1", "Alice");

        // Database should have been written synchronously
        assertEquals("Alice", db.storage.get("user:1"));
    }

    @Test
    void testGetReturnsCachedValue() {
        MockDb db = new MockDb();
        WriteThroughCache<String, String> cache = new WriteThroughCache<>(5, db);

        cache.put("user:1", "Alice");

        assertEquals("Alice", cache.get("user:1"));
    }

    @Test
    void testGetFallsBackToDatabase() {
        MockDb db = new MockDb();
        db.storage.put("user:2", "Bob"); // Added directly to DB
        WriteThroughCache<String, String> cache = new WriteThroughCache<>(5, db);

        // Cache miss — should fall back to DB
        assertEquals("Bob", cache.get("user:2"));
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        MockDb db = new MockDb();
        WriteThroughCache<String, String> cache = new WriteThroughCache<>(5, db);

        assertNull(cache.get("nonexistent"));
    }
}
