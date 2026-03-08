package com.study.systems.ratelimiting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LeakyBucketRateLimiterTest {

    @Test
    void testAllowsRequestsUpToCapacity() {
        // 5 capacity, very slow leak (0.001 per second) so bucket barely drains
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(5, 0.001);

        int allowed = 0;
        for (int i = 0; i < 5; i++) {
            if (limiter.tryAcquire()) allowed++;
        }

        assertEquals(5, allowed);
    }

    @Test
    void testRejectsRequestsWhenBucketFull() {
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(5, 0.001);

        for (int i = 0; i < 5; i++) {
            limiter.tryAcquire();
        }

        // Bucket is full — overflow
        assertFalse(limiter.tryAcquire());
    }

    @Test
    void testQueueSizeAfterRequests() {
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(5, 0.001);

        limiter.tryAcquire();
        limiter.tryAcquire();
        limiter.tryAcquire();

        // Queue should contain 3 pending requests
        assertEquals(3, limiter.getQueueSize());
    }

    @Test
    void testLeaksRequestsOverTime() throws InterruptedException {
        // 3 capacity, 3 requests/second leak
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(3, 3.0);

        // Fill bucket
        for (int i = 0; i < 3; i++) {
            limiter.tryAcquire();
        }

        assertEquals(3, limiter.getQueueSize());

        // Wait 1 second — 3 requests should have leaked
        Thread.sleep(1100);

        assertTrue(limiter.getQueueSize() < 3);
    }
}
