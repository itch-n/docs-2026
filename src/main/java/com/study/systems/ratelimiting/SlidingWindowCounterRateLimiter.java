package com.study.systems.ratelimiting;

/**
 * Sliding Window Counter: Hybrid of fixed window and sliding window
 *
 * Key principles:
 * - Two counters: current and previous window
 * - Weighted average based on time in window
 * - More accurate than fixed window
 * - Less memory than sliding log
 */

public class SlidingWindowCounterRateLimiter {

    private final int maxRequests;
    private final long windowSizeMs;
    private int currentWindowCount;
    private int previousWindowCount;
    private long currentWindowStart;

    /**
     * Initialize sliding window counter
     *
     * @param maxRequests Maximum requests per window
     * @param windowSizeMs Window size in milliseconds
     *
     * TODO: Initialize counters
     * - Set max requests and window size
     * - Initialize both counters
     * - Record window start
     */
    public SlidingWindowCounterRateLimiter(int maxRequests, long windowSizeMs) {
        // TODO: Track state

        // TODO: Initialize currentWindowCount to 0

        // TODO: Initialize previousWindowCount to 0

        // TODO: Track state

        this.maxRequests = 0; // Replace
        this.windowSizeMs = 0; // Replace
    }

    /**
     * Try to acquire permission
     *
     * @return true if allowed, false if rate limited
     *
     * TODO: Implement sliding window counter
     * 1. Rotate windows if needed
     * 2. Calculate weighted count
     * 3. Check against limit
     *
     * Formula:
     * weighted_count = previous_count * (1 - elapsed_ratio) + current_count
     * where elapsed_ratio = time_in_window / window_size
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // TODO: Check if window expired

        // TODO: Calculate time elapsed in current window
        // elapsedRatio = (now - currentWindowStart) / windowSizeMs

        // TODO: Calculate weighted count
        // weightedCount = previousWindowCount * (1 - elapsedRatio) + currentWindowCount

        // TODO: Implement iteration/conditional logic

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get estimated current count (for monitoring)
     */
    public synchronized double getEstimatedCount() {
        long now = System.currentTimeMillis();
        double elapsedRatio = (double)(now - currentWindowStart) / windowSizeMs;
        return previousWindowCount * (1 - elapsedRatio) + currentWindowCount;
    }
}
