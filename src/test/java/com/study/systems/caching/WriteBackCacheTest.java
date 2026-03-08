package com.study.systems.caching;

import org.junit.jupiter.api.Test;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
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

        assertEquals("Alice", cache.get("user:1"));
        assertNull(db.storage.get("user:1")); // Not flushed yet
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
    void testShutdownFlushesDataToDatabase() {
        MockDb db = new MockDb();
        WriteBackCache<String, String> cache = new WriteBackCache<>(5, db, 60000);

        cache.put("user:1", "Alice");
        cache.shutdown();

        assertEquals("Alice", db.storage.get("user:1"));
    }

    @Test
    void testBackgroundFlushWritesToDatabase() {
        MockDb db = new MockDb();
        WriteBackCache<String, String> cache = new WriteBackCache<>(5, db, 100);

        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");

        await().atMost(500, MILLISECONDS)
               .until(() -> db.storage.containsKey("user:1") && db.storage.containsKey("user:2"));

        assertEquals("Alice", db.storage.get("user:1"));
        assertEquals("Bob", db.storage.get("user:2"));
        cache.shutdown();
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        MockDb db = new MockDb();
        WriteBackCache<String, String> cache = new WriteBackCache<>(5, db, 5000);
        assertNull(cache.get("nonexistent"));
        cache.shutdown();
    }
}
