package com.study.dsa.graphs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DFSTest {

    @Test
    void testNumIslands() {
        // 3 islands: top-left block, center cell, bottom-right pair
        char[][] grid = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
        };
        assertEquals(3, DFS.numIslands(grid));
    }

    @Test
    void testNumIslandsSingleIsland() {
        char[][] grid = {
            {'1', '1'},
            {'1', '0'}
        };
        assertEquals(1, DFS.numIslands(grid));
    }

    @Test
    void testNumIslandsAllWater() {
        char[][] grid = {
            {'0', '0'},
            {'0', '0'}
        };
        assertEquals(0, DFS.numIslands(grid));
    }

    @Test
    void testNumIslandsNull() {
        assertEquals(0, DFS.numIslands(null));
    }

    @Test
    void testHasPathTrue() {
        // Graph: 0->{1,2}, 1->{3}, 2->{3}, 3->{}
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(3));
        graph.put(2, Arrays.asList(3));
        graph.put(3, Arrays.asList());
        assertTrue(DFS.hasPath(graph, 0, 3));
    }

    @Test
    void testHasPathFalse() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(3));
        graph.put(2, Arrays.asList(3));
        graph.put(3, Arrays.asList());
        assertFalse(DFS.hasPath(graph, 0, 4));
    }

    @Test
    void testHasPathDirectEdge() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList());
        assertTrue(DFS.hasPath(graph, 0, 1));
    }

    @Test
    void testHasPathSameNode() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList());
        assertTrue(DFS.hasPath(graph, 0, 0));
    }
}
