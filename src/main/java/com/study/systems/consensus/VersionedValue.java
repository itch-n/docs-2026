package com.study.systems.consensus;

/** Stub: represents a versioned value in a quorum store. */
public class VersionedValue {
    public final String data;
    public final long version;
    public final long timestamp;

    public VersionedValue(String data, long version, long timestamp) {
        this.data = data;
        this.version = version;
        this.timestamp = timestamp;
    }
}
