package com.study.systems.ratelimiting;

import java.util.*;
/**
 * Sliding Window Log: Track individual request timestamps
 *
 * Key principles:
 * - Store timestamp of each request
 * - Count requests in sliding window
 * - More accurate than fixed window
 * - Higher memory usage
 */

public class SlidingWindowLogRateLimiter {

    private final int maxRequests;        // Max requests per window
    private final long windowSizeMs;      // Window size in milliseconds
    private final Queue<Long> requestLog; // Request timestamps

    /**
     * Initialize sliding window log
     *
     * @param maxRequests Maximum requests per window
     * @param windowSizeMs Window size in milliseconds
     *
     * TODO: Initialize log
     * - Set max requests and window size
     * - Create queue for timestamps
     */
    public SlidingWindowLogRateLimiter(int maxRequests, long windowSizeMs) {
        // TODO: Track state

        // TODO: Initialize LinkedList for request log

        this.maxRequests = 0; // Replace
        this.windowSizeMs = 0; // Replace
        this.requestLog = null; // Replace
    }

    /**
     * Try to acquire permission
     *
     * @return true if allowed, false if rate limited
     *
     * TODO: Implement sliding window logic
     * 1. Remove old requests outside window
     * 2. Check if under limit
     * 3. Add current timestamp
     *
     * Hint: Remove requests older than (now - windowSizeMs)
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // TODO: Remove timestamps older than (now - windowSizeMs)

        // TODO: Implement iteration/conditional logic

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get current request count (for monitoring)
     */
    public synchronized int getCurrentCount() {
        long now = System.currentTimeMillis();
        while (!requestLog.isEmpty() && requestLog.peek() <= now - windowSizeMs) {
            requestLog.poll();
        }
        return requestLog.size();
    }
}
