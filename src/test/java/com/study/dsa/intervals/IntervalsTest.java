package com.study.dsa.intervals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntervalsTest {

    // helper for deep equality of int[][]
    private static void assertIntervals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length, "result length mismatch");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "interval[" + i + "] mismatch");
        }
    }

    // ---- merge ----------------------------------------------------------

    @Test
    void testMergeBasicOverlap() {
        // [[1,3],[2,6],[8,10],[15,18]] -> [[1,6],[8,10],[15,18]]
        int[][] input    = {{1,3},{2,6},{8,10},{15,18}};
        int[][] expected = {{1,6},{8,10},{15,18}};
        assertIntervals(expected, Intervals.merge(input));
    }

    @Test
    void testMergeAdjacentEndpoints() {
        // [[1,4],[4,5]] -> [[1,5]]
        int[][] input    = {{1,4},{4,5}};
        int[][] expected = {{1,5}};
        assertIntervals(expected, Intervals.merge(input));
    }

    @Test
    void testMergeFullyContained() {
        // [[1,4],[2,3]] -> [[1,4]]
        int[][] input    = {{1,4},{2,3}};
        int[][] expected = {{1,4}};
        assertIntervals(expected, Intervals.merge(input));
    }

    @Test
    void testMergeStartsBefore() {
        // [[1,4],[0,4]] -> [[0,4]]
        int[][] input    = {{1,4},{0,4}};
        int[][] expected = {{0,4}};
        assertIntervals(expected, Intervals.merge(input));
    }

    @Test
    void testMergeNoOverlap() {
        // [[1,2],[3,4],[5,6]] -> [[1,2],[3,4],[5,6]]
        int[][] input    = {{1,2},{3,4},{5,6}};
        int[][] expected = {{1,2},{3,4},{5,6}};
        assertIntervals(expected, Intervals.merge(input));
    }

    @Test
    void testMergeSingleInterval() {
        int[][] input    = {{1,5}};
        int[][] expected = {{1,5}};
        assertIntervals(expected, Intervals.merge(input));
    }

    @Test
    void testMergeAllMergeIntoOne() {
        // [[1,10],[2,8],[3,7]] -> [[1,10]]
        int[][] input    = {{1,10},{2,8},{3,7}};
        int[][] expected = {{1,10}};
        assertIntervals(expected, Intervals.merge(input));
    }
}
