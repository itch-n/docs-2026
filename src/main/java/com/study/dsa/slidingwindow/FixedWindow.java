package com.study.dsa.slidingwindow;

import java.util.*;
public class FixedWindow {

    /**
     * Problem: Maximum sum of K consecutive elements
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement fixed window
     */
    public static double maxAverageSubarray(int[] nums, int k) {
        if (nums.length < k) return 0.0;

        // TODO: Calculate initial window sum
        // Slide the window and update max as you go

        return 0.0; // Replace with implementation
    }

    /**
     * Problem: Contains nearby duplicate within k distance
     * Time: O(n), Space: O(k)
     *
     * TODO: Implement using HashSet as fixed window
     */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        // TODO: Use a set to track elements in current window

        return false; // Replace with implementation
    }

    /**
     * Problem: Maximum of all subarrays of size k
     * Time: O(n), Space: O(k) using deque
     *
     * TODO: Implement using deque for efficient max tracking
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        // TODO: Use a deque to maintain useful elements in window
        // Keep elements in decreasing order for easy max access

        return new int[0]; // Replace with implementation
    }
}
