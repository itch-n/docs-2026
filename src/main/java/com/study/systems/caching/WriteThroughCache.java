package com.study.systems.caching;

import java.util.*;

/**
 * Write-Through Cache - Writes go to cache AND database synchronously
 *
 * Pros: Data consistency, simple
 * Cons: Higher write latency
 */
public class WriteThroughCache<K, V> {

    private final LRUCache<K, V> cache;
    private final Database<K, V> database;

    interface Database<K, V> {
        V read(K key);
        void write(K key, V value);
    }

    public WriteThroughCache(int capacity, Database<K, V> database) {
        this.cache = new LRUCache<>(capacity);
        this.database = database;
    }

    /**
     * Get value
     *
     * TODO: Implement cache-aside pattern
     */
    public V get(K key) {
        // TODO: Check cache first, then fallback to database

        return null; // Replace
    }

    /**
     * Put value
     *
     * TODO: Implement write-through
     */
    public void put(K key, V value) {
        // TODO: Update both cache and database synchronously
    }
}
