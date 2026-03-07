package com.study.systems.resilience;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Circuit Breaker — wraps calls to a remote dependency and trips open
 * when failures exceed a threshold.
 *
 * States:
 *   CLOSED    — normal operation; all calls forwarded
 *   OPEN      — fast-fail; all calls rejected immediately
 *   HALF_OPEN — one probe request allowed through to test recovery
 */
public class CircuitBreaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    private final int failureThreshold;        // failures before opening
    private final long recoveryTimeoutMs;      // ms to wait before probing

    private final AtomicReference<State> state;
    private final AtomicInteger failureCount;
    private final AtomicLong lastFailureTime;  // epoch ms of most recent failure

    /**
     * @param failureThreshold  consecutive failures that trip the breaker open
     * @param recoveryTimeoutMs milliseconds to wait in OPEN before entering HALF_OPEN
     */
    public CircuitBreaker(int failureThreshold, long recoveryTimeoutMs) {
        this.failureThreshold = failureThreshold;
        this.recoveryTimeoutMs = recoveryTimeoutMs;
        // TODO: initialise state to CLOSED
        // TODO: initialise failureCount to 0
        // TODO: initialise lastFailureTime to 0
        this.state = null; // replace
        this.failureCount = null; // replace
        this.lastFailureTime = null; // replace
    }

    /**
     * Execute the given supplier if the circuit allows it.
     *
     * @param action the remote call to attempt
     * @param <T>    return type
     * @return result of action
     * @throws CircuitOpenException if the circuit is OPEN and the recovery timeout has not elapsed
     *
     * TODO: implement the execution guard
     * 1. Call getState() to get the current (possibly auto-transitioning) state
     * 2. If OPEN, throw CircuitOpenException
     * 3. If HALF_OPEN, allow the call through; on success call recordSuccess();
     *    on failure call recordFailure() and re-throw
     * 4. If CLOSED, allow the call through; on success call recordSuccess();
     *    on failure call recordFailure() and re-throw
     */
    public <T> T execute(Supplier<T> action) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Record a successful downstream call.
     *
     * TODO: implement
     * - If state is HALF_OPEN, transition to CLOSED and reset failureCount
     * - If state is CLOSED, reset failureCount (success clears the failure streak)
     */
    public void recordSuccess() {
        // TODO: implement
    }

    /**
     * Record a failed downstream call.
     *
     * TODO: implement
     * - Increment failureCount
     * - Record lastFailureTime = System.currentTimeMillis()
     * - If failureCount >= failureThreshold, transition state to OPEN
     */
    public void recordFailure() {
        // TODO: implement
    }

    /**
     * Return the current logical state, auto-transitioning OPEN → HALF_OPEN
     * when the recovery timeout has elapsed.
     *
     * TODO: implement
     * - If current state is OPEN and (now - lastFailureTime) >= recoveryTimeoutMs,
     *   transition to HALF_OPEN (use compareAndSet to avoid races)
     * - Return the current state
     */
    public State getState() {
        // TODO: implement
        return state.get(); // replace with correct logic
    }

    /** Thrown when a call is rejected because the circuit is open. */
    public static class CircuitOpenException extends RuntimeException {
        public CircuitOpenException(String message) {
            super(message);
        }
    }
}
