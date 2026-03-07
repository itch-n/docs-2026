package com.study.systems.ratelimiting;

import java.util.*;
/**
 * Leaky Bucket: Requests leak out at constant rate
 *
 * Key principles:
 * - Bucket holds pending requests (queue)
 * - Requests leak out at fixed rate
 * - Smooths burst traffic
 * - Rejects requests if bucket full
 */

public class LeakyBucketRateLimiter {

    private final int capacity;           // Max queue size
    private final double leakRate;        // Requests per second
    private final Queue<Long> bucket;     // Request timestamps
    private long lastLeakTime;

    /**
     * Initialize leaky bucket
     *
     * @param capacity Maximum pending requests
     * @param leakRate Requests processed per second
     *
     * TODO: Initialize bucket
     * - Create queue with capacity
     * - Set leak rate
     * - Record current time
     */
    public LeakyBucketRateLimiter(int capacity, double leakRate) {
        // TODO: Track state

        // TODO: Initialize queue (LinkedList)

        // TODO: Record current time

        this.capacity = 0; // Replace
        this.leakRate = 0; // Replace
        this.bucket = null; // Replace
    }

    /**
     * Try to add request to bucket
     *
     * @return true if accepted, false if bucket full
     *
     * TODO: Implement request acceptance
     * 1. Leak out old requests
     * 2. Check if space available
     * 3. Add current request
     *
     * Hint: Remove requests older than (current_time - 1/leak_rate)
     */
    public synchronized boolean tryAcquire() {
        // TODO: Leak out processed requests
        // requests_to_leak = elapsed_seconds * leakRate

        // TODO: Remove that many requests from queue

        // TODO: Implement iteration/conditional logic

        // TODO: Otherwise return false (bucket full)

        return false; // Replace
    }

    /**
     * Leak out processed requests
     *
     * TODO: Remove requests based on elapsed time
     * - Calculate requests that should leak
     * - Remove from queue
     * - Update lastLeakTime
     */
    private void leak() {
        // TODO: Calculate elapsed time

        // TODO: Calculate requests to leak
        // requestsToLeak = elapsed_seconds * leakRate

        // TODO: Poll requestsToLeak items from queue

        // TODO: Update lastLeakTime
    }

    /**
     * Get current bucket size (for monitoring)
     */
    public synchronized int getQueueSize() {
        leak();
        return bucket.size();
    }
}
