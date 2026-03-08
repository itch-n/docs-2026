package com.study.systems.caching;

import java.util.*;
import java.util.concurrent.*;

/**
 * Write-Back Cache - Writes go to cache immediately, database asynchronously
 *
 * Pros: Lower write latency
 * Cons: Risk of data loss, more complex
 */
public class WriteBackCache<K, V> {

    private final LRUCache<K, V> cache;
    private final Database<K, V> database;
    private final Map<K, V> dirtyEntries;
    private final ScheduledExecutorService flusher;

    interface Database<K, V> {
        V read(K key);
        void write(K key, V value);
    }

    public WriteBackCache(int capacity, Database<K, V> database, long flushIntervalMs) {
        this.cache = new LRUCache<>(capacity);
        this.database = database;
        this.dirtyEntries = new ConcurrentHashMap<>();
        this.flusher = Executors.newSingleThreadScheduledExecutor();

        // TODO: Schedule background flush task
    }

    /**
     * Get value
     *
     * TODO: Implement get
     */
    public V get(K key) {
        // TODO: Check cache, dirty entries, then database

        return null; // Replace
    }

    /**
     * Put value
     *
     * TODO: Implement write-back
     */
    public void put(K key, V value) {
        // TODO: Update cache immediately
        // Mark for later database flush
    }

    /**
     * Flush dirty entries to database
     *
     * TODO: Implement flush
     */
    private void flushDirtyEntries() {
        // TODO: Write all dirty entries to database
        // Handle failures appropriately
    }

    public void shutdown() {
        // TODO: Ensure all data is flushed before shutdown
    }
}
