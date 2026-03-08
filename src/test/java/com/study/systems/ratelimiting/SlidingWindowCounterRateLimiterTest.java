package com.study.systems.ratelimiting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowCounterRateLimiterTest {

    @Test
    void testAllowsRequestsUpToLimit() {
        SlidingWindowCounterRateLimiter limiter = new SlidingWindowCounterRateLimiter(5, 10000);

        int allowed = 0;
        for (int i = 0; i < 5; i++) {
            if (limiter.tryAcquire()) allowed++;
        }

        assertEquals(5, allowed);
    }

    @Test
    void testRejectsRequestsOverLimit() {
        SlidingWindowCounterRateLimiter limiter = new SlidingWindowCounterRateLimiter(5, 10000);

        for (int i = 0; i < 5; i++) {
            limiter.tryAcquire();
        }

        assertFalse(limiter.tryAcquire());
    }

    @Test
    void testEstimatedCountAfterRequests() {
        SlidingWindowCounterRateLimiter limiter = new SlidingWindowCounterRateLimiter(10, 10000);

        limiter.tryAcquire();
        limiter.tryAcquire();
        limiter.tryAcquire();

        // Estimated count should be >= 3 (no time has passed, previous window empty)
        assertTrue(limiter.getEstimatedCount() >= 3.0);
    }

    @Test
    void testWindowRotation() throws InterruptedException {
        // 5 requests per 300ms window
        SlidingWindowCounterRateLimiter limiter = new SlidingWindowCounterRateLimiter(5, 300);

        // Fill window
        for (int i = 0; i < 5; i++) {
            limiter.tryAcquire();
        }

        // Wait for window to rotate
        Thread.sleep(350);

        // New window — should be allowed
        assertTrue(limiter.tryAcquire());
    }
}
