package com.study.systems.eventsourcing;

import java.util.*;
import java.util.List;

public interface EventStore {

    /**
     * Append events to a stream.
     *
     * @param aggregateId  the stream identifier
     * @param events       ordered list of events to append
     * @param expectedVersion  the version the caller believes the stream is at;
     *                         throws OptimisticConcurrencyException if the
     *                         actual version differs (another writer raced ahead)
     *
     * TODO: Implement append
     * - Verify that the current stream version matches expectedVersion
     * - Assign sequential version numbers to each event
     * - Persist all events atomically (all succeed or all fail)
     * - Publish events to any registered subscribers after commit
     */
    void append(String aggregateId, List<DomainEvent> events, long expectedVersion);

    /**
     * Load all events for a stream, in order.
     *
     * @param aggregateId  the stream identifier
     * @return ordered list of all events; empty list if stream does not exist
     *
     * TODO: Implement load
     * - Return events in append order (by version, ascending)
     * - Return an empty list (not null) if the aggregate has no events
     */
    List<DomainEvent> load(String aggregateId);

    /**
     * Load events starting from a given version (inclusive).
     * Used when loading after a snapshot.
     *
     * @param aggregateId   the stream identifier
     * @param fromVersion   the first version to include in the result
     * @return ordered list of events at version >= fromVersion
     *
     * TODO: Implement partial load
     * - Return only events where version >= fromVersion
     * - Return in ascending version order
     */
    List<DomainEvent> loadFrom(String aggregateId, long fromVersion);
}
