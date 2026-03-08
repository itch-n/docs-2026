package com.study.systems.ratelimiting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FixedWindowRateLimiterTest {

    @Test
    void testAllowsRequestsUpToLimit() {
        // 5 requests per 10-second window
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5, 10000);

        int allowed = 0;
        for (int i = 0; i < 5; i++) {
            if (limiter.tryAcquire()) allowed++;
        }

        assertEquals(5, allowed);
    }

    @Test
    void testRejectsRequestsOverLimit() {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5, 10000);

        for (int i = 0; i < 5; i++) {
            limiter.tryAcquire();
        }

        // 6th request should be rejected
        assertFalse(limiter.tryAcquire());
    }

    @Test
    void testResetsAfterWindowExpires() throws InterruptedException {
        // Very short 200ms window
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(2, 200);

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire()); // Over limit

        // Wait for window reset
        Thread.sleep(300);

        assertTrue(limiter.tryAcquire()); // New window
    }

    @Test
    void testStatsReflectCurrentWindow() {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5, 10000);

        limiter.tryAcquire();
        limiter.tryAcquire();

        FixedWindowRateLimiter.WindowStats stats = limiter.getStats();
        assertEquals(2, stats.current);
        assertEquals(5, stats.max);
    }
}
