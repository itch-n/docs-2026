package com.study.systems.consensus;

// High-level API - implementation details abstracted
public interface LeaderElection {
    // Start election process
    void startElection(int nodeId);

    // Get current leader (or -1 if none)
    int getLeader();

    // Check if this node is the leader
    boolean isLeader(int nodeId);

    // Detect leader failure via heartbeat timeout
    void checkLeaderHealth(int nodeId);
}
