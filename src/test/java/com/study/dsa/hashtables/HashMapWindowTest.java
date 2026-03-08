package com.study.dsa.hashtables;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashMapWindowTest {

    // ---- minWindow ------------------------------------------------------

    @Test
    void testMinWindowTypical() {
        // s="ADOBECODEBANC", t="ABC"  →  "BANC"
        assertEquals("BANC", HashMapWindow.minWindow("ADOBECODEBANC", "ABC"));
    }

    @Test
    void testMinWindowExactMatch() {
        // s="a", t="a"  →  "a"
        assertEquals("a", HashMapWindow.minWindow("a", "a"));
    }

    @Test
    void testMinWindowImpossible() {
        // s="a", t="aa"  →  "" (can't cover two a's with one)
        assertEquals("", HashMapWindow.minWindow("a", "aa"));
    }

    // ---- checkInclusion -------------------------------------------------

    @Test
    void testCheckInclusionTrue() {
        // s1="ab", s2="eidbaooo"  →  "ba" is a permutation of "ab"
        assertTrue(HashMapWindow.checkInclusion("ab", "eidbaooo"));
    }

    @Test
    void testCheckInclusionFalse() {
        // s1="ab", s2="eidboaoo"  →  no permutation
        assertFalse(HashMapWindow.checkInclusion("ab", "eidboaoo"));
    }

    @Test
    void testCheckInclusionNoPermutation() {
        // s1="abc", s2="ccccbbbbaaaa"  →  no "abc" permutation as contiguous window
        assertFalse(HashMapWindow.checkInclusion("abc", "ccccbbbbaaaa"));
    }

    @Test
    void testCheckInclusionExactEqual() {
        assertTrue(HashMapWindow.checkInclusion("abc", "bca"));
    }
}
