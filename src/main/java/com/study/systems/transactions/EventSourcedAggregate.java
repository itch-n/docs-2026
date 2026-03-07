package com.study.systems.transactions;

import java.util.*;
/**
 * Event Sourcing: Store events instead of current state
 *
 * Key principles:
 * - All changes stored as events
 * - Current state derived from events
 * - Complete audit trail
 * - Time travel (replay to any point)
 */

public class EventSourcedAggregate {

    private final String aggregateId;
    private final List<DomainEvent> events;
    private int version;

    /**
     * Initialize event sourced aggregate
     *
     * @param aggregateId Unique identifier
     *
     * TODO: Initialize aggregate
     * - Store aggregate ID
     * - Create events list
     * - Set version to 0
     */
    public EventSourcedAggregate(String aggregateId) {
        // TODO: Store aggregateId

        // TODO: Initialize events list

        // TODO: Track state

        this.aggregateId = null; // Replace
        this.events = null; // Replace
        this.version = 0;
    }

    /**
     * Apply and record event
     *
     * @param event Event to apply
     *
     * TODO: Apply event
     * - Add event to list
     * - Increment version
     * - Apply state change
     */
    public void applyEvent(DomainEvent event) {
        // TODO: Set event version

        // TODO: Add to events list

        // TODO: Increment version

        // TODO: Apply state change (handled by subclass)

        System.out.println("Event applied: " + event);
    }

    /**
     * Replay events to reconstruct state
     *
     * @param events Historical events
     *
     * TODO: Replay all events
     * - Clear current state
     * - Apply each event in order
     * - Reconstruct current state
     */
    public void replayEvents(List<DomainEvent> events) {
        System.out.println("Replaying " + events.size() + " events");

        // TODO: Implement iteration/conditional logic
    }

    /**
     * Get events after specific version
     *
     * TODO: Filter events by version
     */
    public List<DomainEvent> getEventsSince(int version) {
        // TODO: Filter events where event.version > version
        return null; // Replace
    }

    /**
     * Get all events
     */
    public List<DomainEvent> getAllEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Get current version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Domain event
     */
    static class DomainEvent {
        String aggregateId;
        String eventType;
        int version;
        long timestamp;
        Map<String, Object> data;

        public DomainEvent(String aggregateId, String eventType) {
            this.aggregateId = aggregateId;
            this.eventType = eventType;
            this.timestamp = System.currentTimeMillis();
            this.data = new HashMap<>();
        }

        public void put(String key, Object value) {
            data.put(key, value);
        }

        public Object get(String key) {
            return data.get(key);
        }

        @Override
        public String toString() {
            return "Event{type='" + eventType + "', version=" + version + "}";
        }
    }
}
