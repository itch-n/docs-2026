package com.study.dsa.slidingwindow;

import java.util.*;

public class StringWindow {

    /**
     * Problem: Find all anagrams of pattern in string
     * Time: O(n), Space: O(1) - only 26 letters
     *
     * TODO: Implement using frequency arrays
     */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s.length() < p.length()) return result;

        // TODO: Create frequency array for pattern p

        // TODO: Create frequency array for current window

        // TODO: Fixed window of size p.length()

        return result; // Replace with implementation
    }

    /**
     * Problem: Permutation in string (s2 contains permutation of s1)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using sliding window comparison
     */
    public static boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        // TODO: Similar to findAnagrams but return true on first match

        return false; // Replace with implementation
    }

    /**
     * Problem: Minimum window substring containing all chars of t
     * Time: O(n + m), Space: O(k) where k = unique chars
     *
     * TODO: Implement using two frequency maps
     */
    public static String minWindow(String s, String t) {
        if (s.isEmpty() || t.isEmpty()) return "";

        // TODO: Create frequency map for t

        // TODO: Track matched characters

        // TODO: Expand right, shrink left when valid

        return ""; // Replace with implementation
    }
}
