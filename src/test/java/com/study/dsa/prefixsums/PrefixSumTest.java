package com.study.dsa.prefixsums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrefixSumTest {

    // ---- NumArray (range sum query) -------------------------------------

    @Test
    void testSumRangeFromDemo() {
        // Array: [-2, 0, 3, -5, 2, -1]
        int[] arr = {-2, 0, 3, -5, 2, -1};
        PrefixSum.NumArray numArray = new PrefixSum.NumArray(arr);

        // sumRange(0,2) = -2+0+3 = 1
        assertEquals(1, numArray.sumRange(0, 2));

        // sumRange(2,5) = 3+(-5)+2+(-1) = -1
        assertEquals(-1, numArray.sumRange(2, 5));

        // sumRange(0,5) = -2+0+3+(-5)+2+(-1) = -3
        assertEquals(-3, numArray.sumRange(0, 5));
    }

    @Test
    void testSumRangeSingleElement() {
        int[] arr = {7, 2, 9};
        PrefixSum.NumArray numArray = new PrefixSum.NumArray(arr);
        assertEquals(7, numArray.sumRange(0, 0));
        assertEquals(2, numArray.sumRange(1, 1));
        assertEquals(9, numArray.sumRange(2, 2));
    }

    @Test
    void testSumRangeEntireArray() {
        int[] arr = {1, 2, 3, 4, 5};
        PrefixSum.NumArray numArray = new PrefixSum.NumArray(arr);
        assertEquals(15, numArray.sumRange(0, 4));
    }

    @Test
    void testSumRangeAllNegative() {
        int[] arr = {-3, -2, -1};
        PrefixSum.NumArray numArray = new PrefixSum.NumArray(arr);
        assertEquals(-6, numArray.sumRange(0, 2));
    }

    @Test
    void testSumRangeMiddleSegment() {
        int[] arr = {1, 2, 3, 4, 5};
        PrefixSum.NumArray numArray = new PrefixSum.NumArray(arr);
        // sumRange(1,3) = 2+3+4 = 9
        assertEquals(9, numArray.sumRange(1, 3));
    }
}
