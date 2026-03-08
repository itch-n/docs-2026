package com.study.dsa.prefixsums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumMatrixTest {

    private static int[][] sampleMatrix() {
        return new int[][]{
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
    }

    // ---- sumRegion (from demo expected values) ---------------------------

    @Test
    void testSumRegion2_1_4_3() {
        // sumRegion(2,1,4,3) = 2+0+1 + 1+0+1 + 0+3+0 = 8
        NumMatrix nm = new NumMatrix(sampleMatrix());
        assertEquals(8, nm.sumRegion(2, 1, 4, 3));
    }

    @Test
    void testSumRegion1_1_2_2() {
        // sumRegion(1,1,2,2) = 6+3 + 2+0 = 11
        NumMatrix nm = new NumMatrix(sampleMatrix());
        assertEquals(11, nm.sumRegion(1, 1, 2, 2));
    }

    @Test
    void testSumRegion1_2_2_4() {
        // sumRegion(1,2,2,4) = 3+2+1 + 0+1+5 = 12
        NumMatrix nm = new NumMatrix(sampleMatrix());
        assertEquals(12, nm.sumRegion(1, 2, 2, 4));
    }

    @Test
    void testSumRegionSingleCell() {
        NumMatrix nm = new NumMatrix(sampleMatrix());
        // sumRegion(0,0,0,0) = 3
        assertEquals(3, nm.sumRegion(0, 0, 0, 0));
        // sumRegion(1,1,1,1) = 6
        assertEquals(6, nm.sumRegion(1, 1, 1, 1));
    }

    @Test
    void testSumRegionEntireMatrix() {
        // Sum of all elements in sampleMatrix
        // Row sums: 10, 17, 9, 13, 9 -> total = 58
        NumMatrix nm = new NumMatrix(sampleMatrix());
        assertEquals(58, nm.sumRegion(0, 0, 4, 4));
    }

    @Test
    void testSumRegionFirstRow() {
        // sumRegion(0,0,0,4) = 3+0+1+4+2 = 10
        NumMatrix nm = new NumMatrix(sampleMatrix());
        assertEquals(10, nm.sumRegion(0, 0, 0, 4));
    }

    @Test
    void testSumRegionFirstColumn() {
        // sumRegion(0,0,4,0) = 3+5+1+4+1 = 14
        NumMatrix nm = new NumMatrix(sampleMatrix());
        assertEquals(14, nm.sumRegion(0, 0, 4, 0));
    }
}
