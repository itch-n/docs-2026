package com.study.dsa.graphs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BFSTest {

    @Test
    void testShortestPath() {
        // Graph: 0->{1,2}, 1->{3}, 2->{3,4}, 3->{4}, 4->{}
        // Shortest path 0->4: 0->2->4 = 2 steps
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(3));
        graph.put(2, Arrays.asList(3, 4));
        graph.put(3, Arrays.asList(4));
        graph.put(4, Arrays.asList());
        assertEquals(2, BFS.shortestPath(graph, 0, 4));
    }

    @Test
    void testShortestPathDirectEdge() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList());
        assertEquals(1, BFS.shortestPath(graph, 0, 1));
    }

    @Test
    void testShortestPathSameNode() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList());
        assertEquals(0, BFS.shortestPath(graph, 0, 0));
    }

    @Test
    void testShortestPathNotFound() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList());
        assertEquals(-1, BFS.shortestPath(graph, 0, 5));
    }

    @Test
    void testMinKnightMovesSimple() {
        // (2,1): one knight move away from (0,0) -> 1 move
        assertEquals(1, BFS.minKnightMoves(2, 1));
    }

    @Test
    void testMinKnightMovesOrigin() {
        // Already at origin
        assertEquals(0, BFS.minKnightMoves(0, 0));
    }

    @Test
    void testMinKnightMovesFarther() {
        // (5,5): known answer is 4 moves
        assertEquals(4, BFS.minKnightMoves(5, 5));
    }

    @Test
    void testOrangesRottingAllRot() {
        // Standard 3x3 grid from main()
        int[][] grid = {
            {2, 1, 1},
            {1, 1, 0},
            {0, 1, 1}
        };
        assertEquals(4, BFS.orangesRotting(grid));
    }

    @Test
    void testOrangesRottingNoFresh() {
        // All rotten already
        int[][] grid = {{2, 2}, {2, 2}};
        assertEquals(0, BFS.orangesRotting(grid));
    }

    @Test
    void testOrangesRottingImpossible() {
        // Fresh orange isolated from rotten ones
        int[][] grid = {{2, 0, 1}};
        assertEquals(-1, BFS.orangesRotting(grid));
    }

    @Test
    void testOrangesRottingEmpty() {
        int[][] grid = {{0, 2}};
        assertEquals(0, BFS.orangesRotting(grid));
    }
}
