package com.study.systems.consensus;

// Simple distributed lock interface
public interface DistributedLock {
    // Try to acquire lock immediately
    Lock tryAcquire(String resourceId, String ownerId);

    // Try with custom TTL
    Lock tryAcquire(String resourceId, String ownerId, long ttlMs);

    // Blocking acquire with timeout
    Lock acquire(String resourceId, String ownerId, long timeoutMs);

    // Release lock
    boolean release(String resourceId, String ownerId, long fencingToken);

    // Extend lease
    boolean renew(String resourceId, String ownerId, long fencingToken);

    // Check if locked
    boolean isLocked(String resourceId);
}
