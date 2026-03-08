package com.study.systems.ratelimiting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowLogRateLimiterTest {

    @Test
    void testAllowsRequestsUpToLimit() {
        SlidingWindowLogRateLimiter limiter = new SlidingWindowLogRateLimiter(5, 10000);

        int allowed = 0;
        for (int i = 0; i < 5; i++) {
            if (limiter.tryAcquire()) allowed++;
        }

        assertEquals(5, allowed);
    }

    @Test
    void testRejectsRequestsOverLimit() {
        SlidingWindowLogRateLimiter limiter = new SlidingWindowLogRateLimiter(5, 10000);

        for (int i = 0; i < 5; i++) {
            limiter.tryAcquire();
        }

        assertFalse(limiter.tryAcquire());
    }

    @Test
    void testCurrentCountMatchesRequests() {
        SlidingWindowLogRateLimiter limiter = new SlidingWindowLogRateLimiter(5, 10000);

        limiter.tryAcquire();
        limiter.tryAcquire();
        limiter.tryAcquire();

        assertEquals(3, limiter.getCurrentCount());
    }

    @Test
    void testOldRequestsSlideOutOfWindow() throws InterruptedException {
        // 3 requests per 300ms window
        SlidingWindowLogRateLimiter limiter = new SlidingWindowLogRateLimiter(3, 300);

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire()); // Over limit

        // Wait for window to expire
        Thread.sleep(400);

        // Old requests are outside the window — should be allowed
        assertTrue(limiter.tryAcquire());
    }
}
