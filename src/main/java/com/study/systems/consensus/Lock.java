package com.study.systems.consensus;

/** Stub: represents an acquired distributed lock. */
public class Lock {
    public final String resourceId;
    public final String ownerId;
    public final long fencingToken;
    public final long expiresAt;

    public Lock(String resourceId, String ownerId, long fencingToken, long expiresAt) {
        this.resourceId = resourceId;
        this.ownerId = ownerId;
        this.fencingToken = fencingToken;
        this.expiresAt = expiresAt;
    }
}
