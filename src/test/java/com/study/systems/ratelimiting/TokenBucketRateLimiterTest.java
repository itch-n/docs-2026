package com.study.systems.ratelimiting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenBucketRateLimiterTest {

    @Test
    void testAllowsRequestsUpToCapacity() {
        // 10 token capacity, 0 refill so tokens drain immediately
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 0.0);

        int allowed = 0;
        for (int i = 0; i < 15; i++) {
            if (limiter.tryAcquire()) allowed++;
        }

        // Should allow exactly 10 (capacity), reject 5
        assertEquals(10, allowed);
    }

    @Test
    void testRefillsTokensOverTime() throws InterruptedException {
        // 5 capacity, 5 tokens/second refill
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 5.0);

        // Drain the bucket
        for (int i = 0; i < 5; i++) {
            limiter.tryAcquire();
        }

        // Should be rate-limited now
        assertFalse(limiter.tryAcquire());

        // Wait 1 second for ~5 tokens to refill
        Thread.sleep(1100);

        // Should be allowed again
        assertTrue(limiter.tryAcquire());
    }

    @Test
    void testWeightedAcquisition() {
        // 10 tokens, no refill
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 0.0);

        // Use 7 tokens in one weighted call
        assertTrue(limiter.tryAcquire(7));

        // Only 3 remain — requesting 5 should fail
        assertFalse(limiter.tryAcquire(5));

        // But requesting 3 should succeed
        assertTrue(limiter.tryAcquire(3));
    }

    @Test
    void testInitialBucketIsFull() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 0.0);

        // Should start with a full bucket
        assertTrue(limiter.getTokens() >= 10.0 - 0.001);
    }

    @Test
    void testRateLimitAfterCapacityExhausted() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(3, 0.0);

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire()); // Bucket empty
    }
}
