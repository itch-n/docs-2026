package com.study.systems.multiregion;

import java.util.*;
import java.util.concurrent.*;

public class VersionedValue {

    public final String value;
    public final VectorClock clock;
    public final long timestampMs; // wall-clock time at write, used by LWW

    public VersionedValue(String value, VectorClock clock, long timestampMs) {
        // TODO: assign fields
        this.value = null;       // replace
        this.clock = null;       // replace
        this.timestampMs = 0L;   // replace
    }
}
