package com.study.systems.multiregion;

import java.util.*;
import java.util.concurrent.*;

public class ReplicationManager {

    private final String localRegion;
    private final List<String> remoteRegions;
    private final ConflictResolver conflictResolver;

    // Local store: key → versioned value
    private final ConcurrentHashMap<String, VersionedValue> store;

    /**
     * @param localRegion     identifier for this region (e.g. "us-east-1")
     * @param remoteRegions   list of peer region identifiers
     * @param conflictResolver strategy for resolving concurrent writes
     *
     * TODO: Initialise the local store and stash the constructor parameters
     */
    public ReplicationManager(String localRegion,
                               List<String> remoteRegions,
                               ConflictResolver conflictResolver) {
        // TODO: assign localRegion, remoteRegions, conflictResolver
        // TODO: initialise store as a new ConcurrentHashMap
        this.localRegion = null;      // replace
        this.remoteRegions = null;    // replace
        this.conflictResolver = null; // replace
        this.store = null;            // replace
    }

    /**
     * Accept a write from the local application and propagate it
     * to all remote regions asynchronously.
     *
     * @param key    the record key
     * @param value  the new value
     * @param region the region originating this write (usually localRegion)
     *
     * TODO:
     *  1. Create a VersionedValue with the current VectorClock for this key,
     *     incremented for localRegion
     *  2. Store it locally (putIfAbsent or compare-and-swap to detect conflicts)
     *  3. Enqueue an async replication task for each remote region
     *  4. On replication, call receiveRemoteWrite on the remote manager
     */
    public void write(String key, String value, String region) {
        // TODO: retrieve or initialise the VectorClock for this key
        // TODO: increment the clock for the originating region
        // TODO: wrap value + clock into a VersionedValue
        // TODO: store locally, checking for concurrent writes
        // TODO: for each remote region, submit async replication
    }

    /**
     * Handle an inbound replicated write from another region.
     * Detects conflicts using VectorClock comparison and delegates
     * to the ConflictResolver when writes are concurrent.
     *
     * @param key     the record key
     * @param incoming the versioned value received from a remote region
     *
     * TODO:
     *  1. Look up the current local version for this key (may be absent)
     *  2. If absent, store incoming directly
     *  3. If incoming.clock happensBefore local.clock, discard (we're ahead)
     *  4. If local.clock happensBefore incoming.clock, replace with incoming
     *  5. Otherwise the writes are concurrent — call resolveConflict
     */
    public void receiveRemoteWrite(String key, VersionedValue incoming) {
        // TODO: look up existing value
        // TODO: handle absent case
        // TODO: compare vector clocks and branch accordingly
        // TODO: on concurrent writes, delegate to resolveConflict
    }

    /**
     * Resolve a conflict between two concurrent VersionedValues.
     * Stores the winner returned by the ConflictResolver.
     *
     * @param key    the record key
     * @param v1     one concurrent version
     * @param v2     another concurrent version
     *
     * TODO:
     *  1. Call conflictResolver.resolve(v1, v2)
     *  2. Store the returned winner in the local store
     *  3. Propagate the resolved value to remote regions so they converge
     */
    public void resolveConflict(String key, VersionedValue v1, VersionedValue v2) {
        // TODO: invoke conflictResolver.resolve(v1, v2)
        // TODO: store winner locally
        // TODO: propagate winner to remote regions (async)
    }

    /**
     * Read a value from the local store.
     * Note: caller may observe stale data if replication is lagging.
     *
     * @param key the record key
     * @return the current local VersionedValue, or empty if not present
     */
    public Optional<VersionedValue> read(String key) {
        // TODO: return value from local store
        return Optional.empty(); // replace
    }
}
