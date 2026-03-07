package com.study.systems.consensus;

// Simple quorum-based data store interface
public interface QuorumStore {
    // Write value with quorum
    boolean write(String key, String value);

    // Read value with quorum
    VersionedValue read(String key);

    // Configure quorum sizes
    void setQuorum(int readQuorum, int writeQuorum);

    // Check if value exists
    boolean exists(String key);
}
