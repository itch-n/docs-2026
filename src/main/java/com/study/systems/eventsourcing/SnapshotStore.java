package com.study.systems.eventsourcing;

import java.util.List;

public interface SnapshotStore {

    /**
     * Save a snapshot of aggregate state at a specific version.
     *
     * @param aggregateId  stream identifier
     * @param version      the event version this snapshot was taken after
     * @param state        serialised aggregate state (JSON, Protobuf, etc.)
     *
     * TODO: Implement save
     * - Persist aggregateId, version, and serialised state atomically
     * - Overwrite any existing snapshot for this aggregate
     */
    void save(String aggregateId, long version, byte[] state);

    /**
     * Load the most recent snapshot for an aggregate.
     * Returns null if no snapshot exists.
     *
     * TODO: Implement load
     * - Return the snapshot with the highest version for this aggregateId
     * - Return null (not an exception) if no snapshot exists
     */
    Snapshot load(String aggregateId);

    /**
     * Container for a snapshot read result.
     */
    record Snapshot(String aggregateId, long version, byte[] state) {}
}
