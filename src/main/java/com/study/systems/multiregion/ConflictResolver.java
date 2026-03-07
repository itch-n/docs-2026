package com.study.systems.multiregion;

import java.util.*;
import java.util.concurrent.*;

public interface ConflictResolver {

    /**
     * Given two concurrent VersionedValues, return the one to keep
     * (or a merged value that subsumes both).
     *
     * @param v1 first concurrent version
     * @param v2 second concurrent version
     * @return the resolved VersionedValue to store
     *
     * TODO: Implement LastWriteWinsResolver as a concrete example:
     *  - Compare v1.timestampMs and v2.timestampMs
     *  - Return the version with the larger timestamp
     *  - Note the clock-skew risk: if clocks drift, LWW can discard newer writes
     */
    VersionedValue resolve(VersionedValue v1, VersionedValue v2);
}
