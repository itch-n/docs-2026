package com.study.systems.ratelimiting;

import java.util.*;
import java.util.concurrent.*;
import java.time.Clock;

/**
 * Token Bucket: Tokens refill at constant rate, burst traffic allowed
 *
 * Key principles:
 * - Bucket holds tokens (capacity)
 * - Tokens refill at fixed rate
 * - Request consumes token(s)
 * - Allows burst traffic up to capacity
 */

public class TokenBucketRateLimiter {

    private final int capacity;          // Max tokens in bucket
    private final double refillRate;     // Tokens per second
    private double tokens;               // Current tokens
    private long lastRefillTime;         // Last refill timestamp (ms)
    private final Clock clock;           // Time source (injectable for testing)

    public TokenBucketRateLimiter(int capacity, double refillRate) {
        this(capacity, refillRate, Clock.systemDefaultZone());
    }

    /** Package-private constructor for testing with a controllable clock. */
    TokenBucketRateLimiter(int capacity, double refillRate, Clock clock) {
        this.clock = clock;
        this.lastRefillTime = clock.millis();

        // TODO: Store capacity and refill rate
        this.capacity = 0; // Replace
        this.refillRate = 0; // Replace

        // TODO: Initialize tokens to capacity (bucket starts full)
    }

    /**
     * Attempt to acquire a token
     *
     * @return true if token acquired, false if rate limited
     *
     * TODO: Implement token acquisition
     * 1. Refill tokens based on time elapsed
     * 2. Check if token available
     * 3. Consume token if available
     *
     * Hint: tokens_to_add = time_elapsed * refill_rate
     */
    public synchronized boolean tryAcquire() {
        // TODO: Calculate time elapsed since last refill

        // TODO: Calculate new tokens to add
        // tokens_to_add = elapsed_seconds * refillRate

        // TODO: Add tokens but cap at capacity
        // tokens = Math.min(tokens + tokens_to_add, capacity)

        // TODO: Update lastRefillTime to now

        // TODO: Implement iteration/conditional logic

        // TODO: Otherwise return false (rate limited)

        return false; // Replace
    }

    /**
     * Try to acquire multiple tokens (for weighted rate limiting)
     *
     * @param tokensNeeded Number of tokens to acquire
     * @return true if acquired, false if insufficient tokens
     *
     * TODO: Implement multi-token acquisition
     * - Refill tokens first
     * - Check if enough tokens available
     * - Consume requested tokens
     */
    public synchronized boolean tryAcquire(int tokensNeeded) {
        // TODO: Refill tokens (same as tryAcquire())

        // TODO: Check if tokens >= tokensNeeded

        // TODO: Implement iteration/conditional logic

        // TODO: Otherwise return false

        return false; // Replace
    }

    /**
     * Get current token count (for monitoring)
     */
    public synchronized double getTokens() {
        refill();
        return tokens;
    }

    /**
     * Refill tokens based on elapsed time
     *
     * TODO: Extract refill logic
     * - Calculate elapsed time
     * - Add tokens
     * - Cap at capacity
     */
    private void refill() {
        // TODO: Implement refill logic
        // Use clock.millis() (not System.currentTimeMillis()) to get current time
        // - Calculate elapsed ms since lastRefillTime
        // - Add (elapsed / 1000.0 * refillRate) tokens, capped at capacity
        // - Update lastRefillTime = clock.millis()
    }
}
