package com.study.systems.resilience;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CircuitBreakerTest {

    @Test
    void testInitialStateIsClosed() {
        CircuitBreaker cb = new CircuitBreaker(3, 1000L);

        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }

    @Test
    void testSuccessfulCallReturnsValue() {
        CircuitBreaker cb = new CircuitBreaker(3, 1000L);

        String result = cb.execute(() -> "hello");

        assertEquals("hello", result);
    }

    @Test
    void testRemainsClosedBelowFailureThreshold() {
        CircuitBreaker cb = new CircuitBreaker(3, 1000L);

        cb.recordFailure();
        cb.recordFailure();

        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }

    @Test
    void testTripsOpenAfterReachingFailureThreshold() {
        CircuitBreaker cb = new CircuitBreaker(3, 1000L);

        cb.recordFailure();
        cb.recordFailure();
        cb.recordFailure();

        assertEquals(CircuitBreaker.State.OPEN, cb.getState());
    }

    @Test
    void testOpenCircuitThrowsCircuitOpenException() {
        CircuitBreaker cb = new CircuitBreaker(2, 60_000L); // long timeout — won't auto-recover

        cb.recordFailure();
        cb.recordFailure(); // circuit opens

        assertThrows(CircuitBreaker.CircuitOpenException.class, () -> cb.execute(() -> "blocked"));
    }

    @Test
    void testSuccessAfterHalfOpenTransitionsToClosed() throws InterruptedException {
        CircuitBreaker cb = new CircuitBreaker(2, 50L); // 50ms recovery timeout

        cb.recordFailure();
        cb.recordFailure(); // circuit opens
        assertEquals(CircuitBreaker.State.OPEN, cb.getState());

        Thread.sleep(60); // wait for recovery timeout

        // Now state should auto-transition to HALF_OPEN
        assertEquals(CircuitBreaker.State.HALF_OPEN, cb.getState());

        // A success in HALF_OPEN should close the circuit
        cb.recordSuccess();
        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }

    @Test
    void testSuccessResetsClosed() {
        CircuitBreaker cb = new CircuitBreaker(3, 1000L);

        cb.recordFailure();
        cb.recordFailure();
        cb.recordSuccess(); // streak broken

        // should still be closed; a single additional failure shouldn't trip it
        cb.recordFailure();
        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }

    @Test
    void testExecuteCallsActionWhenClosed() {
        CircuitBreaker cb = new CircuitBreaker(3, 1000L);

        int result = cb.execute(() -> 42);

        assertEquals(42, result);
    }
}
