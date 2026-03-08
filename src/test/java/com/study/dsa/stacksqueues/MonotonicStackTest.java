package com.study.dsa.stacksqueues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MonotonicStackTest {

    // ---- nextGreaterElement ---------------------------------------------

    @Test
    void testNextGreaterElementTypical() {
        // {2,1,2,4,3}  →  {4,2,4,-1,-1}
        // index 0 (val=2): next greater is 4
        // index 1 (val=1): next greater is 2
        // index 2 (val=2): next greater is 4
        // index 3 (val=4): no greater  →  -1
        // index 4 (val=3): no greater  →  -1
        int[] result = MonotonicStack.nextGreaterElement(new int[]{2, 1, 2, 4, 3});
        assertArrayEquals(new int[]{4, 2, 4, -1, -1}, result);
    }

    @Test
    void testNextGreaterElementAllDescending() {
        // {5,4,3,2,1}  →  {-1,-1,-1,-1,-1}
        int[] result = MonotonicStack.nextGreaterElement(new int[]{5, 4, 3, 2, 1});
        assertArrayEquals(new int[]{-1, -1, -1, -1, -1}, result);
    }

    @Test
    void testNextGreaterElementAllAscending() {
        // {1,2,3,4}  →  {2,3,4,-1}
        int[] result = MonotonicStack.nextGreaterElement(new int[]{1, 2, 3, 4});
        assertArrayEquals(new int[]{2, 3, 4, -1}, result);
    }

    // ---- dailyTemperatures ----------------------------------------------

    @Test
    void testDailyTemperaturesTypical() {
        // {73,74,75,71,69,72,76,73}  →  {1,1,4,2,1,1,0,0}
        int[] result = MonotonicStack.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, result);
    }

    @Test
    void testDailyTemperaturesDecreasing() {
        // {5,4,3,2,1}  →  {0,0,0,0,0}
        int[] result = MonotonicStack.dailyTemperatures(new int[]{5, 4, 3, 2, 1});
        assertArrayEquals(new int[]{0, 0, 0, 0, 0}, result);
    }

    @Test
    void testDailyTemperaturesIncreasing() {
        // {1,2,3}  →  {1,1,0}
        int[] result = MonotonicStack.dailyTemperatures(new int[]{1, 2, 3});
        assertArrayEquals(new int[]{1, 1, 0}, result);
    }

    // ---- largestRectangleArea -------------------------------------------

    @Test
    void testLargestRectangleAreaTypical() {
        // {2,1,5,6,2,3}  →  10 (bars 5 and 6, height 5 width 2)
        assertEquals(10, MonotonicStack.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
    }

    @Test
    void testLargestRectangleAreaAllSame() {
        // {3,3,3}  →  9
        assertEquals(9, MonotonicStack.largestRectangleArea(new int[]{3, 3, 3}));
    }

    @Test
    void testLargestRectangleAreaSingleBar() {
        assertEquals(5, MonotonicStack.largestRectangleArea(new int[]{5}));
    }

    @Test
    void testLargestRectangleAreaTwoBars() {
        // {2,4}  →  max(2*2, 4*1) = 4
        assertEquals(4, MonotonicStack.largestRectangleArea(new int[]{2, 4}));
    }
}
