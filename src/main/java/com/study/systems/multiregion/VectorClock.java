package com.study.systems.multiregion;

import java.util.*;
import java.util.concurrent.*;

public class VectorClock {

    // nodeId → logical counter
    private final Map<String, Integer> clock;

    public VectorClock() {
        // TODO: initialise clock as an empty HashMap
        this.clock = null; // replace
    }

    /** Deep-copy constructor — needed when forking a clock for a new write. */
    public VectorClock(VectorClock other) {
        // TODO: copy other.clock into a new HashMap
        this.clock = null; // replace
    }

    /**
     * Increment the counter for the given nodeId.
     * Call this once per write on the originating node.
     *
     * @param nodeId the region or node performing the write
     *
     * TODO: getOrDefault(nodeId, 0) + 1, then put back
     */
    public void increment(String nodeId) {
        // TODO: increment clock entry for nodeId
    }

    /**
     * Determine whether this clock causally precedes another.
     *
     * this.happensBefore(other) is true when:
     *  - For every nodeId, this[nodeId] <= other[nodeId]
     *  - AND at least one nodeId has this[nodeId] < other[nodeId]
     *
     * @param other the clock to compare against
     * @return true if this happened-before other (strict causal precedence)
     *
     * TODO:
     *  1. Collect all nodeIds from both clocks
     *  2. For each nodeId, compare entries (missing entry = 0)
     *  3. If any this[nodeId] > other[nodeId], return false
     *  4. Return true only if at least one entry is strictly less
     */
    public boolean happensBefore(VectorClock other) {
        // TODO: iterate over all keys in both clocks
        // TODO: return false if any entry in this exceeds other
        // TODO: return true if strictly less in at least one position
        return false; // replace
    }

    /**
     * Merge two clocks by taking the element-wise maximum.
     * Used when synthesising a resolved version after conflict resolution.
     *
     * @param other the clock to merge with
     * @return a new VectorClock representing the merged causal history
     *
     * TODO: for each nodeId in either clock, result[nodeId] = max(this[nodeId], other[nodeId])
     */
    public VectorClock merge(VectorClock other) {
        // TODO: build merged clock
        return null; // replace
    }

    /** Returns an unmodifiable view for inspection / serialisation. */
    public Map<String, Integer> entries() {
        return Collections.unmodifiableMap(clock);
    }
}
