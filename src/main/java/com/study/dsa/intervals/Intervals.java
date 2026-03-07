package com.study.dsa.intervals;

import java.util.*;

public class Intervals {

    /**
     * Problem: Merge overlapping intervals
     * Time: O(n log n), Space: O(n)
     *
     * TODO: Implement merge intervals
     * Step 1: Sort by start time
     * Step 2: Walk the list; if next.start <= current.end, merge
     *         using current.end = Math.max(current.end, next.end)
     * Step 3: Otherwise, push current to result and move on
     * Step 4: Don't forget to add the final current to result
     */
    public static int[][] merge(int[][] intervals) {
        // TODO: Sort by start time
        // TODO: Iterate and merge overlapping intervals

        return new int[0][0]; // Replace with implementation
    }
}
