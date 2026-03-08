package com.study.util;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * A controllable {@link Clock} for use in tests.
 *
 * <p>Starts at {@link Instant#EPOCH} and advances only when explicitly told to.
 * Pass an instance to any class that accepts a {@code Clock} constructor argument,
 * then call {@code advanceBy} / {@code advanceBySeconds} / {@code advanceByMillis}
 * to simulate the passage of time without sleeping.
 *
 * <pre>{@code
 * MutableClock clock = new MutableClock();
 * TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 5.0, clock);
 *
 * for (int i = 0; i < 5; i++) limiter.tryAcquire(); // drain
 * assertFalse(limiter.tryAcquire());
 *
 * clock.advanceBySeconds(1);                         // 5 tokens refilled
 * assertTrue(limiter.tryAcquire());
 * }</pre>
 */
public class MutableClock extends Clock {

    private Instant instant;
    private final ZoneId zone;

    public MutableClock() {
        this(Instant.EPOCH, ZoneOffset.UTC);
    }

    public MutableClock(Instant initial, ZoneId zone) {
        this.instant = initial;
        this.zone = zone;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new MutableClock(instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public void advanceBy(Duration duration) {
        instant = instant.plus(duration);
    }

    public void advanceBySeconds(long seconds) {
        instant = instant.plusSeconds(seconds);
    }

    public void advanceByMillis(long millis) {
        instant = instant.plusMillis(millis);
    }

    public void set(Instant newInstant) {
        this.instant = newInstant;
    }
}
