package com.study.dsa.hashtables;

import java.util.*;

public class HashMapGrouping {

    /**
     * Problem: Group anagrams together
     * Time: O(n * k log k) where k = max word length, Space: O(n * k)
     *
     * TODO: Implement using HashMap with sorted string as key
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        // TODO: What makes anagrams have the same key?
        // How can you transform each string into a unique key?

        return new ArrayList<>(); // Replace with implementation
    }

    /**
     * Problem: Group numbers by digit sum
     * Time: O(n * d) where d = digits, Space: O(n)
     *
     * TODO: Implement custom grouping
     */
    public static Map<Integer, List<Integer>> groupByDigitSum(int[] nums) {
        // TODO: Compute a key for each number based on its digits
        // Group numbers with the same key together

        return new HashMap<>(); // Replace with implementation
    }

    /**
     * Problem: Find all strings that start with same character
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement grouping by first character
     */
    public static Map<Character, List<String>> groupByFirstChar(String[] words) {
        // TODO: Extract the grouping criterion from each word
        // Store words with the same criterion together

        return new HashMap<>(); // Replace with implementation
    }

    // Helper: Calculate digit sum
    private static int digitSum(int n) {
        int sum = 0;
        n = Math.abs(n);
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }
}
