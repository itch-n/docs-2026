package com.study.dsa.intervals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsertIntervalTest {

    private static void assertIntervals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length, "result length mismatch");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "interval[" + i + "] mismatch");
        }
    }

    // ---- insert ---------------------------------------------------------

    @Test
    void testInsertIntoMiddle() {
        // [[1,3],[6,9]], new=[2,5] -> [[1,5],[6,9]]
        int[][] intervals    = {{1,3},{6,9}};
        int[]   newInterval  = {2,5};
        int[][] expected     = {{1,5},{6,9}};
        assertIntervals(expected, InsertInterval.insert(intervals, newInterval));
    }

    @Test
    void testInsertMergesMultiple() {
        // [[1,2],[3,5],[6,7],[8,10],[12,16]], new=[4,8] -> [[1,2],[3,10],[12,16]]
        int[][] intervals    = {{1,2},{3,5},{6,7},{8,10},{12,16}};
        int[]   newInterval  = {4,8};
        int[][] expected     = {{1,2},{3,10},{12,16}};
        assertIntervals(expected, InsertInterval.insert(intervals, newInterval));
    }

    @Test
    void testInsertIntoEmptyList() {
        // [], new=[5,7] -> [[5,7]]
        int[][] intervals    = {};
        int[]   newInterval  = {5,7};
        int[][] expected     = {{5,7}};
        assertIntervals(expected, InsertInterval.insert(intervals, newInterval));
    }

    @Test
    void testInsertNewInsideExisting() {
        // [[1,5]], new=[2,3] -> [[1,5]]
        int[][] intervals    = {{1,5}};
        int[]   newInterval  = {2,3};
        int[][] expected     = {{1,5}};
        assertIntervals(expected, InsertInterval.insert(intervals, newInterval));
    }

    @Test
    void testInsertBeforeAll() {
        // [[3,5],[6,9]], new=[1,2] -> [[1,2],[3,5],[6,9]]
        int[][] intervals    = {{3,5},{6,9}};
        int[]   newInterval  = {1,2};
        int[][] expected     = {{1,2},{3,5},{6,9}};
        assertIntervals(expected, InsertInterval.insert(intervals, newInterval));
    }

    @Test
    void testInsertAfterAll() {
        // [[1,2],[3,5]], new=[7,9] -> [[1,2],[3,5],[7,9]]
        int[][] intervals    = {{1,2},{3,5}};
        int[]   newInterval  = {7,9};
        int[][] expected     = {{1,2},{3,5},{7,9}};
        assertIntervals(expected, InsertInterval.insert(intervals, newInterval));
    }

    @Test
    void testInsertMergesAll() {
        // [[1,3],[4,6]], new=[2,5] -> [[1,6]]
        int[][] intervals    = {{1,3},{4,6}};
        int[]   newInterval  = {2,5};
        int[][] expected     = {{1,6}};
        assertIntervals(expected, InsertInterval.insert(intervals, newInterval));
    }
}
