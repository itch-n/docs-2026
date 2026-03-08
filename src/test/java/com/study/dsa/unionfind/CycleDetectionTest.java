package com.study.dsa.unionfind;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CycleDetectionTest {

    @Test
    void testHasCycleNoCycle() {
        // 0->1->2: no cycle
        int[][] edges = {{0, 1}, {1, 2}};
        assertFalse(CycleDetection.hasCycle(3, edges));
    }

    @Test
    void testHasCycleWithCycle() {
        // 0-1-2-0: triangle = cycle
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        assertTrue(CycleDetection.hasCycle(3, edges));
    }

    @Test
    void testHasCycleSharedEdge() {
        // 0-1, 0-2, 1-2: cycle through 0-1-2-0
        int[][] edges = {{0, 1}, {0, 2}, {1, 2}};
        assertTrue(CycleDetection.hasCycle(3, edges));
    }

    @Test
    void testFindRedundantConnection() {
        // [[1,2],[1,3],[2,3]]: edge [2,3] creates cycle
        int[] redundant = CycleDetection.findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}});
        assertArrayEquals(new int[]{2, 3}, redundant);
    }

    @Test
    void testFindRedundantConnectionLarger() {
        // [[1,2],[2,3],[3,4],[1,4],[1,5]]: edge [1,4] creates cycle
        int[] redundant = CycleDetection.findRedundantConnection(
            new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}});
        assertArrayEquals(new int[]{1, 4}, redundant);
    }

    @Test
    void testValidTreeTrue() {
        // 5 nodes, 4 edges, connected, no cycle -> valid tree
        int[][] edges = {{0, 1}, {0, 2}, {0, 3}, {1, 4}};
        assertTrue(CycleDetection.validTree(5, edges));
    }

    @Test
    void testValidTreeFalseWithCycle() {
        // Has a cycle -> not a tree
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {1, 3}, {1, 4}};
        assertFalse(CycleDetection.validTree(5, edges));
    }

    @Test
    void testValidTreeFalseTooFewEdges() {
        // 5 nodes, 3 edges -> disconnected -> not a tree
        int[][] edges = {{0, 1}, {1, 2}, {3, 4}};
        assertFalse(CycleDetection.validTree(5, edges));
    }
}
