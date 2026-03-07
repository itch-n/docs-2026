package com.study.systems.eventsourcing;

import java.util.*;
import java.util.List;

public abstract class Aggregate {

    private String id;
    private long version = 0;

    // Events raised by the most recent handleCommand call, not yet persisted.
    // TODO: initialise this field to an empty mutable list
    private List<DomainEvent> pendingEvents;

    public String getId() { return id; }
    public long getVersion() { return version; }
    public List<DomainEvent> getPendingEvents() { return pendingEvents; }

    /**
     * Replay a single event to rebuild aggregate state.
     * Must NOT throw for unknown event types — be tolerant of future event versions.
     *
     * TODO: Implement in each concrete aggregate subclass
     * - Switch on event type and update internal state fields
     * - Increment the version counter
     * - Do NOT enforce invariants here — apply is called during both
     *   initial load and command handling; only handleCommand enforces rules
     */
    protected abstract void apply(DomainEvent event);

    /**
     * Replay a sequence of events (used when loading from the event store).
     *
     * TODO: Implement replayAll
     * - Call apply(event) for each event in order
     * - After replay, version should equal the version of the last event
     */
    public void replayAll(List<DomainEvent> events) {
        // TODO: iterate events and call apply on each
    }

    /**
     * Handle an inbound command and return the events it produces.
     * Returns an empty list if the command is a no-op.
     * Throws a domain exception if the command violates an invariant.
     *
     * TODO: Implement in each concrete aggregate subclass
     * - Validate the command against current state
     * - Construct new events (do not persist them — that is the caller's job)
     * - Call apply() on each new event to update in-memory state
     * - Add new events to pendingEvents
     * - Return the list of new events
     */
    public abstract List<DomainEvent> handleCommand(Command command);

    /**
     * Raise a new event: apply it locally and queue it for persistence.
     * Concrete subclasses call this inside handleCommand.
     *
     * TODO: Implement raiseEvent
     * - Call apply(event) to update in-memory state immediately
     * - Add event to pendingEvents
     */
    protected void raiseEvent(DomainEvent event) {
        // TODO: apply event to self
        // TODO: add to pendingEvents
    }
}
