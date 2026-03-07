package com.study.systems.consensus;

import java.util.*;
// High-level Raft interface
public interface RaftConsensus {
    // Start election (becomes candidate)
    void startElection(int nodeId);

    // Append command to replicated log
    boolean appendEntry(int leaderId, String command);

    // Get current leader
    int getLeader();

    // Get committed log entries
    List<LogEntry> getCommittedEntries(int nodeId);
}
