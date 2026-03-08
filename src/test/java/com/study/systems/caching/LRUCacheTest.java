package com.study.systems.caching;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void testPutAndGet() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");

        assertEquals("Alice", cache.get("user:1"));
        assertEquals("Bob", cache.get("user:2"));
        assertEquals("Charlie", cache.get("user:3"));
    }

    @Test
    void testSizeAfterInserts() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");

        assertEquals(3, cache.size());
    }

    @Test
    void testEvictsLeastRecentlyUsed() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");

        // Access user:1 to make it most recently used
        cache.get("user:1");

        // Adding user:4 should evict user:2 (LRU)
        cache.put("user:4", "David");

        assertEquals("Alice", cache.get("user:1"));
        assertNull(cache.get("user:2")); // Should be evicted
        assertEquals("Charlie", cache.get("user:3"));
        assertEquals("David", cache.get("user:4"));
    }

    @Test
    void testUpdateExistingKey() {
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("key", "original");
        cache.put("key", "updated");

        assertEquals("updated", cache.get("key"));
        assertEquals(1, cache.size());
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        assertNull(cache.get("nonexistent"));
    }

    @Test
    void testEvictionOrder() {
        // After filling cache and accessing in specific order,
        // confirm the LRU item is evicted
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");

        // Access 1 and 2, making 3 the LRU
        cache.get(1);
        cache.get(2);

        // Insert 4 — should evict 3 (LRU)
        cache.put(4, "four");

        assertNotNull(cache.get(1));
        assertNotNull(cache.get(2));
        assertNull(cache.get(3));
        assertNotNull(cache.get(4));
    }
}
