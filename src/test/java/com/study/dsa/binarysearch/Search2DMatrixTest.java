package com.study.dsa.binarysearch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Search2DMatrixTest {

    @Test
    void testSearchMatrix1Found() {
        int[][] matrix = {
            {1,  3,  5,  7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };
        assertTrue(Search2DMatrix.searchMatrix1(matrix, 3));
        assertTrue(Search2DMatrix.searchMatrix1(matrix, 60));
        assertTrue(Search2DMatrix.searchMatrix1(matrix, 1));
    }

    @Test
    void testSearchMatrix1NotFound() {
        int[][] matrix = {
            {1,  3,  5,  7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };
        assertFalse(Search2DMatrix.searchMatrix1(matrix, 13));
        assertFalse(Search2DMatrix.searchMatrix1(matrix, 0));
        assertFalse(Search2DMatrix.searchMatrix1(matrix, 100));
    }

    @Test
    void testSearchMatrix2Found() {
        // Rows are sorted and last element of row i < first element of row i+1
        int[][] matrix = {
            {1,  3,  5,  7},
            {10, 11, 16, 20},
            {23, 30, 34, 50}
        };
        assertTrue(Search2DMatrix.searchMatrix2(matrix, 3));
        assertTrue(Search2DMatrix.searchMatrix2(matrix, 50));
        assertTrue(Search2DMatrix.searchMatrix2(matrix, 1));
    }

    @Test
    void testSearchMatrix2NotFound() {
        int[][] matrix = {
            {1,  3,  5,  7},
            {10, 11, 16, 20},
            {23, 30, 34, 50}
        };
        assertFalse(Search2DMatrix.searchMatrix2(matrix, 13));
        assertFalse(Search2DMatrix.searchMatrix2(matrix, 0));
    }

    @Test
    void testSearchMatrixStaircaseFound() {
        int[][] matrix = {
            {1,  4,  7,  11},
            {2,  5,  8,  12},
            {3,  6,  9,  16},
            {10, 13, 14, 17}
        };
        assertTrue(Search2DMatrix.searchMatrixStaircase(matrix, 5));
        assertTrue(Search2DMatrix.searchMatrixStaircase(matrix, 14));
        assertTrue(Search2DMatrix.searchMatrixStaircase(matrix, 1));
        assertTrue(Search2DMatrix.searchMatrixStaircase(matrix, 17));
    }

    @Test
    void testSearchMatrixStaircaseNotFound() {
        int[][] matrix = {
            {1,  4,  7,  11},
            {2,  5,  8,  12},
            {3,  6,  9,  16},
            {10, 13, 14, 17}
        };
        assertFalse(Search2DMatrix.searchMatrixStaircase(matrix, 20));
        assertFalse(Search2DMatrix.searchMatrixStaircase(matrix, 0));
    }
}
