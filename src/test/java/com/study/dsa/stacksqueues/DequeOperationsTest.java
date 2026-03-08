package com.study.dsa.stacksqueues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DequeOperationsTest {

    // ---- maxSlidingWindow -----------------------------------------------

    @Test
    void testMaxSlidingWindowTypical() {
        // {1,3,-1,-3,5,3,6,7}, k=3  →  {3,3,5,5,6,7}
        int[] result = DequeOperations.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, result);
    }

    @Test
    void testMaxSlidingWindowSingleElement() {
        int[] result = DequeOperations.maxSlidingWindow(new int[]{5}, 1);
        assertArrayEquals(new int[]{5}, result);
    }

    @Test
    void testMaxSlidingWindowKEqualsLength() {
        // Only one window, max of entire array
        int[] result = DequeOperations.maxSlidingWindow(new int[]{3, 1, 2}, 3);
        assertArrayEquals(new int[]{3}, result);
    }

    @Test
    void testMaxSlidingWindowAllSame() {
        int[] result = DequeOperations.maxSlidingWindow(new int[]{4, 4, 4, 4}, 2);
        assertArrayEquals(new int[]{4, 4, 4}, result);
    }

    // ---- canFormPalindrome ----------------------------------------------

    @Test
    void testCanFormPalindromeAab() {
        // "aab"  →  a(2), b(1)  →  one odd count  →  true (e.g., "aba")
        assertTrue(DequeOperations.canFormPalindrome("aab"));
    }

    @Test
    void testCanFormPalindromeAbc() {
        // "abc"  →  a(1), b(1), c(1)  →  three odd counts  →  false
        assertFalse(DequeOperations.canFormPalindrome("abc"));
    }

    @Test
    void testCanFormPalindromeRacecar() {
        // "racecar"  →  all counts even (r:2,a:2,c:2,e:1)  →  one odd  →  true
        assertTrue(DequeOperations.canFormPalindrome("racecar"));
    }

    @Test
    void testCanFormPalindromeHello() {
        // "hello"  →  h(1),e(1),l(2),o(1)  →  three odd counts  →  false
        assertFalse(DequeOperations.canFormPalindrome("hello"));
    }

    @Test
    void testCanFormPalindromeSingleChar() {
        assertTrue(DequeOperations.canFormPalindrome("a"));
    }

    @Test
    void testCanFormPalindromeEvenCounts() {
        // "aabb"  →  all even  →  true
        assertTrue(DequeOperations.canFormPalindrome("aabb"));
    }
}
