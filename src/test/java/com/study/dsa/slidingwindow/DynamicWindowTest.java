package com.study.dsa.slidingwindow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DynamicWindowTest {

    // ---- lengthOfLongestSubstring ----------------------------------------

    @Test
    void testLongestSubstringAbcabcbb() {
        // "abcabcbb"  →  "abc" = 3
        assertEquals(3, DynamicWindow.lengthOfLongestSubstring("abcabcbb"));
    }

    @Test
    void testLongestSubstringBbbbb() {
        // "bbbbb"  →  "b" = 1
        assertEquals(1, DynamicWindow.lengthOfLongestSubstring("bbbbb"));
    }

    @Test
    void testLongestSubstringPwwkew() {
        // "pwwkew"  →  "wke" = 3
        assertEquals(3, DynamicWindow.lengthOfLongestSubstring("pwwkew"));
    }

    @Test
    void testLongestSubstringEmpty() {
        assertEquals(0, DynamicWindow.lengthOfLongestSubstring(""));
    }

    // ---- lengthOfLongestSubstringKDistinct ------------------------------

    @Test
    void testKDistinctEceba() {
        // "eceba", k=2  →  "ece" = 3
        assertEquals(3, DynamicWindow.lengthOfLongestSubstringKDistinct("eceba", 2));
    }

    @Test
    void testKDistinctAa() {
        // "aa", k=1  →  "aa" = 2
        assertEquals(2, DynamicWindow.lengthOfLongestSubstringKDistinct("aa", 1));
    }

    @Test
    void testKDistinctAaabbccd() {
        // "aaabbccd", k=2  →  "aaabb" = 5
        assertEquals(5, DynamicWindow.lengthOfLongestSubstringKDistinct("aaabbccd", 2));
    }

    @Test
    void testKDistinctZero() {
        assertEquals(0, DynamicWindow.lengthOfLongestSubstringKDistinct("abc", 0));
    }

    // ---- minSubArrayLen --------------------------------------------------

    @Test
    void testMinSubArrayLenTypical() {
        // {2,3,1,2,4,3}, target=7  →  [4,3] = 2
        assertEquals(2, DynamicWindow.minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}));
    }

    @Test
    void testMinSubArrayLenSingleElement() {
        // {1,4,4}, target=4  →  [4] = 1
        assertEquals(1, DynamicWindow.minSubArrayLen(4, new int[]{1, 4, 4}));
    }

    @Test
    void testMinSubArrayLenNoSolution() {
        // {1,1,1,1,1,1,1,1}, target=11  →  0 (impossible)
        assertEquals(0, DynamicWindow.minSubArrayLen(11, new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
    }

    @Test
    void testMinSubArrayLenWholeArray() {
        // {1,2,3,4,5}, target=15  →  entire array = 5
        assertEquals(5, DynamicWindow.minSubArrayLen(15, new int[]{1, 2, 3, 4, 5}));
    }
}
