package com.study.dsa.graphs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CycleDetectionTest {

    @Test
    void testHasCycleDirectedNoCycle() {
        // 0->1->2: no cycle
        int[][] edges = {{0, 1}, {1, 2}};
        assertFalse(CycleDetection.hasCycleDirected(3, edges));
    }

    @Test
    void testHasCycleDirectedWithCycle() {
        // 0->1->2->0: cycle
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        assertTrue(CycleDetection.hasCycleDirected(3, edges));
    }

    @Test
    void testHasCycleDirectedSelfLoop() {
        int[][] edges = {{0, 0}};
        assertTrue(CycleDetection.hasCycleDirected(1, edges));
    }

    @Test
    void testHasCycleDirectedNoEdges() {
        assertFalse(CycleDetection.hasCycleDirected(4, new int[0][]));
    }

    @Test
    void testHasCycleUndirectedNoCycle() {
        // 0-1-2: no cycle (line graph)
        int[][] edges = {{0, 1}, {1, 2}};
        assertFalse(CycleDetection.hasCycleUndirected(3, edges));
    }

    @Test
    void testHasCycleUndirectedWithCycle() {
        // 0-1-2-0: triangle
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        assertTrue(CycleDetection.hasCycleUndirected(3, edges));
    }

    @Test
    void testHasCycleUndirectedNoEdges() {
        assertFalse(CycleDetection.hasCycleUndirected(3, new int[0][]));
    }
}
