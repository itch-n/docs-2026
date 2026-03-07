package com.study.dsa.slidingwindow;

import java.util.*;

public class DynamicWindow {

    /**
     * Problem: Longest substring without repeating characters
     * Time: O(n), Space: O(k) where k = unique chars
     *
     * TODO: Implement dynamic window with HashSet
     */
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> window = new HashSet<>();
        int left = 0, maxLen = 0;

        // TODO: Implement iteration/conditional logic

        return 0; // Replace with implementation
    }

    /**
     * Problem: Longest substring with at most K distinct characters
     * Time: O(n), Space: O(k)
     *
     * TODO: Implement with HashMap for frequency counting
     */
    public static int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (k == 0) return 0;

        Map<Character, Integer> window = new HashMap<>();
        int left = 0, maxLen = 0;

        // TODO: Implement iteration/conditional logic

        return 0; // Replace with implementation
    }

    /**
     * Problem: Minimum size subarray sum >= target
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement shrinking window
     */
    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;

        // TODO: Implement iteration/conditional logic

        // TODO: Return minLen == Integer.MAX_VALUE ? 0 : minLen

        return 0; // Replace with implementation
    }
}
