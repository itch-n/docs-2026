package com.study.dsa.prefixsums;

public class ProductExceptSelf {

    /**
     * Problem: Product of array except self
     * Time: O(n), Space: O(1) excluding output array
     *
     * TODO: Implement using prefix and suffix products
     * Pass 1: result[i] = product of all elements to the LEFT of i
     * Pass 2: multiply result[i] by product of all elements to the RIGHT of i
     */
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // TODO: Pass 1 — fill result with prefix products
        // result[0] = 1 (no elements to the left of index 0)
        // result[i] = result[i-1] * nums[i-1]

        // TODO: Pass 2 — multiply by suffix products in-place
        // Track running suffix product (start at 1)
        // Traverse right to left: result[i] *= suffix; suffix *= nums[i]

        return result; // Replace with implementation
    }
}
