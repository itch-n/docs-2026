package com.study.dsa.prefixsums;

public class PrefixSum {

    /**
     * Problem: Range sum query (immutable array)
     * Time: O(1) query after O(n) preprocessing, Space: O(n)
     *
     * TODO: Implement range sum query
     */
    static class NumArray {
        private int[] prefixSum;

        public NumArray(int[] nums) {
            // TODO: Build prefix sum array of length nums.length + 1
            // TODO: prefixSum[0] = 0
            // TODO: prefixSum[i] = prefixSum[i-1] + nums[i-1]
        }

        public int sumRange(int left, int right) {
            // TODO: Return prefixSum[right+1] - prefixSum[left]
            return 0; // Replace with implementation
        }
    }
}
