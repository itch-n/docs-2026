package com.study.dsa.slidingwindow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FixedWindowTest {

    // ---- maxAverageSubarray ---------------------------------------------

    @Test
    void testMaxAverageSubarrayTypical() {
        // {1,12,-5,-6,50,3}, k=4
        // Windows: [1,12,-5,-6]=2/4=0.5, [12,-5,-6,50]=51/4=12.75, [-5,-6,50,3]=42/4=10.5
        // Max average = 12.75
        double result = FixedWindow.maxAverageSubarray(new int[]{1, 12, -5, -6, 50, 3}, 4);
        assertEquals(12.75, result, 0.001);
    }

    @Test
    void testMaxAverageSubarraySingleElement() {
        double result = FixedWindow.maxAverageSubarray(new int[]{5}, 1);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    void testMaxAverageSubarrayKEqualsLength() {
        // k equals array length: only one window
        double result = FixedWindow.maxAverageSubarray(new int[]{1, 2, 3, 4}, 4);
        assertEquals(2.5, result, 0.001);
    }

    @Test
    void testMaxAverageSubarrayKLargerThanArray() {
        // Should return 0.0 per the guard at the top
        double result = FixedWindow.maxAverageSubarray(new int[]{1, 2}, 5);
        assertEquals(0.0, result, 0.001);
    }

    // ---- containsNearbyDuplicate ----------------------------------------

    @Test
    void testContainsNearbyDuplicateTrue1() {
        // {1,2,3,1}, k=3  →  true (1 at indices 0 and 3, diff = 3 <= k)
        assertTrue(FixedWindow.containsNearbyDuplicate(new int[]{1, 2, 3, 1}, 3));
    }

    @Test
    void testContainsNearbyDuplicateTrue2() {
        // {1,0,1,1}, k=1  →  true (1 at indices 2 and 3, diff = 1 <= k)
        assertTrue(FixedWindow.containsNearbyDuplicate(new int[]{1, 0, 1, 1}, 1));
    }

    @Test
    void testContainsNearbyDuplicateFalse() {
        // {1,2,3,1,2,3}, k=2  →  false (nearest duplicates are 3 apart)
        assertFalse(FixedWindow.containsNearbyDuplicate(new int[]{1, 2, 3, 1, 2, 3}, 2));
    }

    // ---- maxSlidingWindow -----------------------------------------------

    @Test
    void testMaxSlidingWindowTypical() {
        // {1,3,-1,-3,5,3,6,7}, k=3  →  {3,3,5,5,6,7}
        int[] result = FixedWindow.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, result);
    }

    @Test
    void testMaxSlidingWindowSingleElement() {
        int[] result = FixedWindow.maxSlidingWindow(new int[]{5}, 1);
        assertArrayEquals(new int[]{5}, result);
    }

    @Test
    void testMaxSlidingWindowKEqualsLength() {
        // Only one window, max of all elements
        int[] result = FixedWindow.maxSlidingWindow(new int[]{3, 1, 2}, 3);
        assertArrayEquals(new int[]{3}, result);
    }
}
