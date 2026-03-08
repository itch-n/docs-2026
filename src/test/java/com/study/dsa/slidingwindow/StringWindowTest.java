package com.study.dsa.slidingwindow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class StringWindowTest {

    // ---- findAnagrams ---------------------------------------------------

    @Test
    void testFindAnagramsCbaebabacd() {
        // s="cbaebabacd", p="abc"  →  [0, 6]
        List<Integer> result = StringWindow.findAnagrams("cbaebabacd", "abc");
        assertEquals(List.of(0, 6), result);
    }

    @Test
    void testFindAnagramsAbab() {
        // s="abab", p="ab"  →  [0, 1, 2]
        List<Integer> result = StringWindow.findAnagrams("abab", "ab");
        assertEquals(List.of(0, 1, 2), result);
    }

    @Test
    void testFindAnagramsNoMatch() {
        List<Integer> result = StringWindow.findAnagrams("hello", "xyz");
        assertTrue(result.isEmpty());
    }

    // ---- checkInclusion -------------------------------------------------

    @Test
    void testCheckInclusionTrue() {
        // s1="ab", s2="eidbaooo"  →  "ba" at index 3 is a permutation of "ab"
        assertTrue(StringWindow.checkInclusion("ab", "eidbaooo"));
    }

    @Test
    void testCheckInclusionFalse() {
        // s1="ab", s2="eidboaoo"  →  no permutation
        assertFalse(StringWindow.checkInclusion("ab", "eidboaoo"));
    }

    @Test
    void testCheckInclusionFalseAbc() {
        // s1="abc", s2="bbbca"  →  no permutation of "abc" exists
        assertFalse(StringWindow.checkInclusion("abc", "bbbca"));
    }

    @Test
    void testCheckInclusionExactMatch() {
        assertTrue(StringWindow.checkInclusion("abc", "abc"));
    }

    // ---- minWindow ------------------------------------------------------

    @Test
    void testMinWindowTypical() {
        // s="ADOBECODEBANC", t="ABC"  →  "BANC"
        assertEquals("BANC", StringWindow.minWindow("ADOBECODEBANC", "ABC"));
    }

    @Test
    void testMinWindowExactMatch() {
        // s="a", t="a"  →  "a"
        assertEquals("a", StringWindow.minWindow("a", "a"));
    }

    @Test
    void testMinWindowImpossible() {
        // s="a", t="aa"  →  "" (can't cover two a's)
        assertEquals("", StringWindow.minWindow("a", "aa"));
    }
}
