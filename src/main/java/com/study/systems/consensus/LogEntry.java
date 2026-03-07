package com.study.systems.consensus;

/** Stub: represents a replicated log entry in Raft. */
public class LogEntry {
    public final int term;
    public final int index;
    public final String command;

    public LogEntry(int term, int index, String command) {
        this.term = term;
        this.index = index;
        this.command = command;
    }
}
