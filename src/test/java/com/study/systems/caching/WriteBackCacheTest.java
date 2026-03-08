package com.study.systems.caching;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.jupiter.api.Assertions.*;

class WriteBackCacheTest {

    static class MockDb implements WriteBackCache.Database<String, String> {
        final Map<String, String> storage = new ConcurrentHashMap<>();

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
    void testPutWritesToCacheImmediately() {
        MockDb db = new MockDb();
        WriteBackCache<String, String> cache = new WriteBackCache<>(5, db, 5000);

        cache.put("user:1", "Alice");

        // Cache should return value immediately without waiting for flush
        assertEquals("Alice", cache.get("user:1"));
        cache.shutdown();
    }

    @Test
    void testGetReturnsValueFromCache() {
        MockDb db = new MockDb();
        WriteBackCache<String, String> cache = new WriteBackCache<>(5, db, 5000);

        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");

        assertEquals("Alice", cache.get("user:1"));
        assertEquals("Bob", cache.get("user:2"));
        cache.shutdown();
    }

    @Test
    void testShutdownFlushesDataToDatabase() throws InterruptedException {
        MockDb db = new MockDb();
        WriteBackCache<String, String> cache = new WriteBackCache<>(5, db, 60000);

        cache.put("user:1", "Alice");

        // Shutdown should flush all dirty entries before returning
        cache.shutdown();

        assertEquals("Alice", db.storage.get("user:1"));
    }

    @Test
    void testBackgroundFlushWritesToDatabase() throws InterruptedException {
        MockDb db = new MockDb();
        // Very short flush interval
        WriteBackCache<String, String> cache = new WriteBackCache<>(5, db, 200);

        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");

        // Wait for background flush
        Thread.sleep(600);

        assertEquals("Alice", db.storage.get("user:1"));
        assertEquals("Bob", db.storage.get("user:2"));
        cache.shutdown();
    }
}
