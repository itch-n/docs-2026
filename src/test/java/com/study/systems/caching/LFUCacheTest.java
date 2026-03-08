package com.study.systems.caching;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LFUCacheTest {

    @Test
    void testPutAndGet() {
        LFUCache<String, String> cache = new LFUCache<>(2);
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
    }

    @Test
    void testEvictsLeastFrequentlyUsed() {
        LFUCache<String, String> cache = new LFUCache<>(2);

        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // Access key1 once more — freq: key1=2, key2=1
        cache.get("key1");

        // Adding key3 should evict key2 (LFU, freq=1)
        cache.put("key3", "value3");

        assertNull(cache.get("key2"));   // Should be evicted
        assertEquals("value3", cache.get("key3"));
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        LFUCache<String, String> cache = new LFUCache<>(2);
        assertNull(cache.get("missing"));
    }

    @Test
    void testUpdateExistingKey() {
        LFUCache<String, String> cache = new LFUCache<>(2);
        cache.put("key1", "value1");
        cache.put("key1", "updated");

        assertEquals("updated", cache.get("key1"));
    }

    @Test
    void testZeroCapacityDoesNothing() {
        LFUCache<String, String> cache = new LFUCache<>(0);
        cache.put("key", "value");
        assertNull(cache.get("key"));
    }
}
