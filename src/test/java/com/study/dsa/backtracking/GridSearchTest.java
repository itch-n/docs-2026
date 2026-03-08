package com.study.dsa.backtracking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GridSearchTest {

    // ---- exist (word search) --------------------------------------------

    private static char[][] sampleBoard() {
        return new char[][]{
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
    }

    @Test
    void testExistWordFound_ABCCED() {
        assertTrue(GridSearch.exist(sampleBoard(), "ABCCED"));
    }

    @Test
    void testExistWordFound_SEE() {
        assertTrue(GridSearch.exist(sampleBoard(), "SEE"));
    }

    @Test
    void testExistWordNotFound_ABCB() {
        // Can't reuse cell [0][1] for both B's
        assertFalse(GridSearch.exist(sampleBoard(), "ABCB"));
    }

    @Test
    void testExistSingleCharPresent() {
        assertTrue(GridSearch.exist(sampleBoard(), "A"));
    }

    @Test
    void testExistSingleCharAbsent() {
        assertFalse(GridSearch.exist(sampleBoard(), "Z"));
    }

    @Test
    void testExistEntireGridAsWord() {
        // "ABCE" can be found along the top row
        assertTrue(GridSearch.exist(sampleBoard(), "ABCE"));
    }

    // ---- countPaths -----------------------------------------------------

    @Test
    void testCountPathsWithObstacle() {
        // 3x3 grid with centre blocked
        int[][] grid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        // From (0,0) to (2,2): two paths go around the obstacle
        assertEquals(2, GridSearch.countPaths(grid));
    }

    @Test
    void testCountPathsNoObstacle2x2() {
        // 2x2 with no obstacles: right-down and down-right = 2 paths
        int[][] grid = {
            {0, 0},
            {0, 0}
        };
        assertEquals(2, GridSearch.countPaths(grid));
    }

    @Test
    void testCountPathsBlockedAtStart() {
        int[][] grid = {
            {1, 0},
            {0, 0}
        };
        assertEquals(0, GridSearch.countPaths(grid));
    }

    @Test
    void testCountPathsBlockedAtEnd() {
        int[][] grid = {
            {0, 0},
            {0, 1}
        };
        assertEquals(0, GridSearch.countPaths(grid));
    }

    @Test
    void testCountPathsSingleCell() {
        int[][] grid = {{0}};
        assertEquals(1, GridSearch.countPaths(grid));
    }

    // ---- longestIncreasingPath ------------------------------------------

    @Test
    void testLongestIncreasingPathMatrix3x3() {
        // [[9,9,4],[6,6,8],[2,1,1]] -> longest path is 4: 1->2->6->9
        int[][] matrix = {
            {9, 9, 4},
            {6, 6, 8},
            {2, 1, 1}
        };
        assertEquals(4, GridSearch.longestIncreasingPath(matrix));
    }

    @Test
    void testLongestIncreasingPathAllSame() {
        // All cells equal: no strictly increasing move possible, path length = 1
        int[][] matrix = {
            {3, 3},
            {3, 3}
        };
        assertEquals(1, GridSearch.longestIncreasingPath(matrix));
    }

    @Test
    void testLongestIncreasingPathSingleCell() {
        int[][] matrix = {{7}};
        assertEquals(1, GridSearch.longestIncreasingPath(matrix));
    }

    @Test
    void testLongestIncreasingPathStrictlyIncreasing1x4() {
        // [1,2,3,4]: path 1->2->3->4 = length 4
        int[][] matrix = {{1, 2, 3, 4}};
        assertEquals(4, GridSearch.longestIncreasingPath(matrix));
    }
}
