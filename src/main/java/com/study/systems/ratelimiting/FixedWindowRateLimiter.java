package com.study.systems.ratelimiting;

/**
 * Fixed Window: Count requests in fixed time windows
 *
 * Key principles:
 * - Reset counter at window boundaries
 * - Simple and memory efficient
 * - Can allow 2x rate at window boundaries
 */

public class FixedWindowRateLimiter {

    private final int maxRequests;        // Max requests per window
    private final long windowSizeMs;      // Window size in milliseconds
    private int counter;                  // Requests in current window
    private long windowStart;             // Current window start time

    /**
     * Initialize fixed window rate limiter
     *
     * @param maxRequests Maximum requests per window
     * @param windowSizeMs Window size in milliseconds
     *
     * TODO: Initialize window
     * - Set max requests and window size
     * - Start counter at 0
     * - Record window start time
     */
    public FixedWindowRateLimiter(int maxRequests, long windowSizeMs) {
        // TODO: Track state

        // TODO: Initialize counter to 0

        // TODO: Track state

        this.maxRequests = 0; // Replace
        this.windowSizeMs = 0; // Replace
    }

    /**
     * Try to acquire permission
     *
     * @return true if allowed, false if rate limited
     *
     * TODO: Implement fixed window logic
     * 1. Check if window expired (reset if needed)
     * 2. Check if under limit
     * 3. Increment counter
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // TODO: Implement iteration/conditional logic

        // TODO: Implement iteration/conditional logic

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get current window stats (for monitoring)
     */
    public synchronized WindowStats getStats() {
        return new WindowStats(counter, maxRequests, windowStart);
    }

    static class WindowStats {
        int current;
        int max;
        long windowStart;

        WindowStats(int current, int max, long windowStart) {
            this.current = current;
            this.max = max;
            this.windowStart = windowStart;
        }
    }
}
