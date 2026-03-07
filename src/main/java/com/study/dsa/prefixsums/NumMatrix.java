package com.study.dsa.prefixsums;

public class NumMatrix {

    private int[][] prefixSum;

    /**
     * Problem: Range sum query 2D
     * Time: O(1) query after O(m*n) preprocessing, Space: O(m*n)
     *
     * TODO: Implement 2D prefix sum
     * prefixSum[i][j] = sum of submatrix from (0,0) to (i-1,j-1)
     * Build using: prefixSum[i][j] = matrix[i-1][j-1]
     *                              + prefixSum[i-1][j]
     *                              + prefixSum[i][j-1]
     *                              - prefixSum[i-1][j-1]
     */
    public NumMatrix(int[][] matrix) {
        // TODO: Build 2D prefix sum with (m+1) x (n+1) dimensions
    }

    /**
     * TODO: Implement sumRegion using inclusion-exclusion:
     * sum = prefixSum[row2+1][col2+1]
     *     - prefixSum[row1][col2+1]
     *     - prefixSum[row2+1][col1]
     *     + prefixSum[row1][col1]
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        return 0; // Replace with implementation
    }
}
