package com.study.dsa.intervals;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntervalIntersectionTest {

    private static void assertIntervals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length, "result length mismatch");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "interval[" + i + "] mismatch");
        }
    }

    // ---- intervalIntersection -------------------------------------------

    @Test
    void testIntersectionFromDemo1() {
        // firstList=[[0,2],[5,10],[13,23],[24,25]]
        // secondList=[[1,5],[8,12],[15,24],[25,26]]
        // Intersections: [1,2],[5,5],[8,10],[15,23],[24,24],[25,25]
        int[][] first    = {{0,2},{5,10},{13,23},{24,25}};
        int[][] second   = {{1,5},{8,12},{15,24},{25,26}};
        int[][] expected = {{1,2},{5,5},{8,10},{15,23},{24,24},{25,25}};
        assertIntervals(expected, IntervalIntersection.intervalIntersection(first, second));
    }

    @Test
    void testIntersectionEmptySecondList() {
        // firstList=[[1,3],[5,9]], secondList=[] -> []
        int[][] first    = {{1,3},{5,9}};
        int[][] second   = {};
        int[][] expected = {};
        assertIntervals(expected, IntervalIntersection.intervalIntersection(first, second));
    }

    @Test
    void testIntersectionEmptyFirstList() {
        int[][] first    = {};
        int[][] second   = {{1,5}};
        int[][] expected = {};
        assertIntervals(expected, IntervalIntersection.intervalIntersection(first, second));
    }

    @Test
    void testIntersectionNoOverlap() {
        // [[1,2]] vs [[3,4]] -> no intersection
        int[][] first    = {{1,2}};
        int[][] second   = {{3,4}};
        int[][] expected = {};
        assertIntervals(expected, IntervalIntersection.intervalIntersection(first, second));
    }

    @Test
    void testIntersectionSinglePointOverlap() {
        // [[1,3]] vs [[3,5]] -> [3,3]
        int[][] first    = {{1,3}};
        int[][] second   = {{3,5}};
        int[][] expected = {{3,3}};
        assertIntervals(expected, IntervalIntersection.intervalIntersection(first, second));
    }

    @Test
    void testIntersectionFullOverlap() {
        // [[2,6]] vs [[1,8]] -> [2,6]
        int[][] first    = {{2,6}};
        int[][] second   = {{1,8}};
        int[][] expected = {{2,6}};
        assertIntervals(expected, IntervalIntersection.intervalIntersection(first, second));
    }

    @Test
    void testIntersectionIdenticalIntervals() {
        // [[1,5]] vs [[1,5]] -> [1,5]
        int[][] first    = {{1,5}};
        int[][] second   = {{1,5}};
        int[][] expected = {{1,5}};
        assertIntervals(expected, IntervalIntersection.intervalIntersection(first, second));
    }
}
