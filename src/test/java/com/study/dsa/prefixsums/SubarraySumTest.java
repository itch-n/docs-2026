package com.study.dsa.prefixsums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubarraySumTest {

    // ---- subarraySum (count subarrays summing to k) ---------------------

    @Test
    void testSubarraySumAllOnesK2() {
        // [1,1,1], k=2 -> [1,1] starting at 0 and [1,1] starting at 1 -> 2
        assertEquals(2, SubarraySum.subarraySum(new int[]{1, 1, 1}, 2));
    }

    @Test
    void testSubarraySumK3() {
        // [1,2,3], k=3 -> [3] and [1,2] -> 2
        assertEquals(2, SubarraySum.subarraySum(new int[]{1, 2, 3}, 3));
    }

    @Test
    void testSubarraySumNegativesK0() {
        // [-1,-1,1], k=0 -> [-1,-1+1]=[0] starting at 0 -> 1
        // prefix sums: 0,-1,-2,-1
        // -1 appears twice: difference = indices 0 and 3 -> length-3 subarray sums to 0
        // Also check: no subarray of single elements sums to 0
        // subarrays: [-1],[-1],[1],[-1,-1],[-1,1],[-1,-1,1]
        // sums:      -1,  -1,  1,   -2,    0,     -1
        // k=0 matches: [-1,1] -> count=1
        assertEquals(1, SubarraySum.subarraySum(new int[]{-1, -1, 1}, 0));
    }

    @Test
    void testSubarraySumSingleElementMatch() {
        assertEquals(1, SubarraySum.subarraySum(new int[]{5}, 5));
    }

    @Test
    void testSubarraySumNoMatch() {
        assertEquals(0, SubarraySum.subarraySum(new int[]{1, 2, 3}, 10));
    }

    @Test
    void testSubarraySumK0AllZeros() {
        // [0,0,0], k=0: every subarray sums to 0
        // subarrays: [0],[0],[0],[0,0],[0,0],[0,0,0] -> 6
        assertEquals(6, SubarraySum.subarraySum(new int[]{0, 0, 0}, 0));
    }

    // ---- findMaxLength (contiguous array equal 0s and 1s) ---------------

    @Test
    void testFindMaxLengthSimple() {
        // [0,1] -> entire array: length 2
        assertEquals(2, SubarraySum.findMaxLength(new int[]{0, 1}));
    }

    @Test
    void testFindMaxLength3Elements() {
        // [0,1,0] -> [0,1] is the longest equal subarray: length 2
        assertEquals(2, SubarraySum.findMaxLength(new int[]{0, 1, 0}));
    }

    @Test
    void testFindMaxLength6Elements() {
        // [0,1,0,1,1,0] -> [0,1,0,1,1,0] has equal 0s and 1s (3 each): length 6
        assertEquals(6, SubarraySum.findMaxLength(new int[]{0, 1, 0, 1, 1, 0}));
    }

    @Test
    void testFindMaxLengthAllZeros() {
        // [0,0,0] -> no equal-length subarray -> 0
        assertEquals(0, SubarraySum.findMaxLength(new int[]{0, 0, 0}));
    }

    @Test
    void testFindMaxLengthAllOnes() {
        // [1,1,1] -> no equal-length subarray -> 0
        assertEquals(0, SubarraySum.findMaxLength(new int[]{1, 1, 1}));
    }
}
