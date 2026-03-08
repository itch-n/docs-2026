package com.study.systems.ratelimiting;

import com.study.util.MutableClock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenBucketRateLimiterTest {

    @Test
    void testAllowsRequestsUpToCapacity() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 0.0);

        int allowed = 0;
        for (int i = 0; i < 15; i++) {
            if (limiter.tryAcquire()) allowed++;
        }

        assertEquals(10, allowed);
    }

    @Test
    void testRefillsTokensOverTime() {
        MutableClock clock = new MutableClock();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 5.0, clock);

        for (int i = 0; i < 5; i++) limiter.tryAcquire(); // drain
        assertFalse(limiter.tryAcquire());

        clock.advanceBySeconds(1); // 5 tokens refilled
        assertTrue(limiter.tryAcquire());
    }

    @Test
    void testRefillCapsAtCapacity() {
        MutableClock clock = new MutableClock();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 5.0, clock);

        for (int i = 0; i < 5; i++) limiter.tryAcquire(); // drain

        clock.advanceBySeconds(10); // would add 50, but capped at 5

        int allowed = 0;
        for (int i = 0; i < 10; i++) {
            if (limiter.tryAcquire()) allowed++;
        }
        assertEquals(5, allowed);
    }

    @Test
    void testPartialRefill() {
        MutableClock clock = new MutableClock();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 10.0, clock);

        for (int i = 0; i < 10; i++) limiter.tryAcquire(); // drain
        assertFalse(limiter.tryAcquire());

        clock.advanceByMillis(200); // 200ms × 10 tokens/s = 2 tokens
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire()); // 3rd should fail
    }

    @Test
    void testWeightedAcquisition() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 0.0);

        assertTrue(limiter.tryAcquire(7));
        assertFalse(limiter.tryAcquire(5)); // only 3 remain
        assertTrue(limiter.tryAcquire(3));
    }

    @Test
    void testInitialBucketIsFull() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 0.0);
        assertTrue(limiter.getTokens() >= 10.0 - 0.001);
    }

    @Test
    void testRateLimitAfterCapacityExhausted() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(3, 0.0);

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
    }
}
