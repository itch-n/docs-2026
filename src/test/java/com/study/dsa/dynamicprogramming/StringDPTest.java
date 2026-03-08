package com.study.dsa.dynamicprogramming;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class StringDPTest {

    // ---- numDecodings ---------------------------------------------------

    @Test
    void testNumDecodings12() {
        // "12" -> "AB" or "L" -> 2 ways
        assertEquals(2, StringDP.numDecodings("12"));
    }

    @Test
    void testNumDecodings226() {
        // "226" -> "BBF","BZ","VF" -> 3 ways
        assertEquals(3, StringDP.numDecodings("226"));
    }

    @Test
    void testNumDecodings06() {
        // "06" -> leading zero: invalid -> 0 ways
        assertEquals(0, StringDP.numDecodings("06"));
    }

    @Test
    void testNumDecodings10() {
        // "10" -> "J" -> 1 way
        assertEquals(1, StringDP.numDecodings("10"));
    }

    @Test
    void testNumDecodings1() {
        // "1" -> "A" -> 1 way
        assertEquals(1, StringDP.numDecodings("1"));
    }

    @Test
    void testNumDecodings11106() {
        // "11106" -> "AAJF","KJF" -> 2 ways
        assertEquals(2, StringDP.numDecodings("11106"));
    }

    // ---- wordBreak ------------------------------------------------------

    @Test
    void testWordBreakLeetcode() {
        List<String> dict = Arrays.asList("leet", "code");
        assertTrue(StringDP.wordBreak("leetcode", dict));
    }

    @Test
    void testWordBreakCatsand() {
        List<String> dict = Arrays.asList("leet", "code", "sand", "and", "cat");
        assertTrue(StringDP.wordBreak("catsand", dict));
    }

    @Test
    void testWordBreakCatsandog() {
        List<String> dict = Arrays.asList("leet", "code", "sand", "and", "cat");
        assertFalse(StringDP.wordBreak("catsandog", dict));
    }

    @Test
    void testWordBreakSingleWordMatch() {
        List<String> dict = Arrays.asList("apple");
        assertTrue(StringDP.wordBreak("apple", dict));
    }

    @Test
    void testWordBreakNoMatch() {
        List<String> dict = Arrays.asList("dog", "cat");
        assertFalse(StringDP.wordBreak("fish", dict));
    }

    // ---- lengthOfLIS ----------------------------------------------------

    @Test
    void testLengthOfLIS8Elements() {
        // [10,9,2,5,3,7,101,18] -> [2,3,7,101] or [2,5,7,101] or [2,3,7,18] = length 4
        assertEquals(4, StringDP.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
    }

    @Test
    void testLengthOfLIS6Elements() {
        // [0,1,0,3,2,3] -> [0,1,2,3] = length 4
        assertEquals(4, StringDP.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));
    }

    @Test
    void testLengthOfLISAllSame() {
        // [7,7,7,7] -> LIS length = 1
        assertEquals(1, StringDP.lengthOfLIS(new int[]{7, 7, 7, 7}));
    }

    @Test
    void testLengthOfLISSingleElement() {
        assertEquals(1, StringDP.lengthOfLIS(new int[]{5}));
    }

    @Test
    void testLengthOfLISStrictlyDecreasing() {
        // [5,4,3,2,1] -> LIS length = 1
        assertEquals(1, StringDP.lengthOfLIS(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void testLengthOfLISStrictlyIncreasing() {
        // [1,2,3,4,5] -> LIS length = 5
        assertEquals(5, StringDP.lengthOfLIS(new int[]{1, 2, 3, 4, 5}));
    }

    // ---- longestPalindrome ----------------------------------------------

    @Test
    void testLongestPalindromeBabad() {
        // "babad" -> "bab" or "aba" (both length 3)
        String result = StringDP.longestPalindrome("babad");
        assertTrue(result.equals("bab") || result.equals("aba"),
            "Expected 'bab' or 'aba', got: " + result);
    }

    @Test
    void testLongestPalindromeCbbd() {
        // "cbbd" -> "bb" (length 2)
        assertEquals("bb", StringDP.longestPalindrome("cbbd"));
    }

    @Test
    void testLongestPalindromeRacecar() {
        // "racecar" is itself a palindrome
        assertEquals("racecar", StringDP.longestPalindrome("racecar"));
    }

    @Test
    void testLongestPalindromeNoon() {
        // "noon" is itself a palindrome
        assertEquals("noon", StringDP.longestPalindrome("noon"));
    }

    @Test
    void testLongestPalindromeSingleChar() {
        // Single character is always a palindrome
        assertEquals("a", StringDP.longestPalindrome("a"));
    }
}
