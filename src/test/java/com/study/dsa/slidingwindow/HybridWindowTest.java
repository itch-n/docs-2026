package com.study.dsa.slidingwindow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HybridWindowTest {

    // ---- characterReplacement -------------------------------------------

    @Test
    void testCharacterReplacementABAB() {
        // s="ABAB", k=2  →  entire string, length=4
        assertEquals(4, HybridWindow.characterReplacement("ABAB", 2));
    }

    @Test
    void testCharacterReplacementAABABBA() {
        // s="AABABBA", k=1  →  4 (e.g., "AABA" or "ABBA")
        assertEquals(4, HybridWindow.characterReplacement("AABABBA", 1));
    }

    @Test
    void testCharacterReplacementAllSame() {
        // s="AAAA", k=2  →  4 (no replacements needed)
        assertEquals(4, HybridWindow.characterReplacement("AAAA", 2));
    }

    // ---- longestOnes ----------------------------------------------------

    @Test
    void testLongestOnesK2() {
        // {1,1,1,0,0,0,1,1,1,1,0}, k=2  →  6
        assertEquals(6, HybridWindow.longestOnes(new int[]{1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0}, 2));
    }

    @Test
    void testLongestOnesK3() {
        // {0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1}, k=3  →  10
        assertEquals(10, HybridWindow.longestOnes(
            new int[]{0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1}, 3));
    }

    @Test
    void testLongestOnesK0() {
        // {1,1,0,1}, k=0  →  longest run of 1s without any flip = 2
        assertEquals(2, HybridWindow.longestOnes(new int[]{1, 1, 0, 1}, 0));
    }

    // ---- totalFruit -----------------------------------------------------

    @Test
    void testTotalFruit121() {
        // {1,2,1}  →  3 (all 3, two types: 1 and 2)
        assertEquals(3, HybridWindow.totalFruit(new int[]{1, 2, 1}));
    }

    @Test
    void testTotalFruit0122() {
        // {0,1,2,2}  →  3 (types 1 and 2: [1,2,2])
        assertEquals(3, HybridWindow.totalFruit(new int[]{0, 1, 2, 2}));
    }

    @Test
    void testTotalFruit12322() {
        // {1,2,3,2,2}  →  4 (types 2 and 3: [3,2,2] = 3, or [2,3,2,2]=4)
        assertEquals(4, HybridWindow.totalFruit(new int[]{1, 2, 3, 2, 2}));
    }
}
