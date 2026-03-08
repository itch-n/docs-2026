package com.study.dsa.slidingwindow;

import java.util.*;

public class HybridWindow {

    /**
     * Problem: Longest repeating character replacement
     * Time: O(n), Space: O(1) - only 26 letters
     *
     * TODO: Implement window with character replacement
     */
    public static int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int left = 0, maxCount = 0, maxLen = 0;

        // TODO: Implement iteration/conditional logic

        return 0; // Replace with implementation
    }

    /**
     * Problem: Max consecutive ones with k flips
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement window tracking flips
     */
    public static int longestOnes(int[] nums, int k) {
        int left = 0, zeros = 0, maxLen = 0;

        // TODO: Implement iteration/conditional logic

        return 0; // Replace with implementation
    }

    /**
     * Problem: Fruits into baskets (at most 2 types)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement window with at most 2 distinct elements
     */
    public static int totalFruit(int[] fruits) {
        Map<Integer, Integer> basket = new HashMap<>();
        int left = 0, maxFruits = 0;

        // TODO: Implement iteration/conditional logic

        return 0; // Replace with implementation
    }
}
