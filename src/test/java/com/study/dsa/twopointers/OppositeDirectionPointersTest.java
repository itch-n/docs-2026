package com.study.dsa.twopointers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OppositeDirectionPointersTest {

    // ---- isPalindrome ----------------------------------------------------

    @Test
    void testIsPalindromeRacecar() {
        assertTrue(OppositeDirectionPointers.isPalindrome("racecar"));
    }

    @Test
    void testIsPalindromeNoon() {
        assertTrue(OppositeDirectionPointers.isPalindrome("noon"));
    }

    @Test
    void testIsPalindromeSingleChar() {
        assertTrue(OppositeDirectionPointers.isPalindrome("a"));
    }

    @Test
    void testIsPalindromeEmpty() {
        assertTrue(OppositeDirectionPointers.isPalindrome(""));
    }

    @Test
    void testIsPalindromeHelloFalse() {
        assertFalse(OppositeDirectionPointers.isPalindrome("hello"));
    }

    // ---- twoSum ---------------------------------------------------------

    @Test
    void testTwoSumFound() {
        // {1, 3, 5, 7, 9, 11}, target=12  →  indices 1 and 4 (3+9) or 2 and 3 (5+7)
        // 5 + 7 = 12: indices [2, 3]
        int[] sortedArray = {1, 3, 5, 7, 9, 11};
        int[] result = OppositeDirectionPointers.twoSum(sortedArray, 12);
        assertNotNull(result);
        assertEquals(2, result.length);
        // verify the values at those indices actually sum to target
        assertEquals(12, sortedArray[result[0]] + sortedArray[result[1]]);
    }

    @Test
    void testTwoSumSimple() {
        // {1, 2, 3}, target=3  →  indices [0, 2] (1+2) or [0,2] depending on pointers
        int[] arr = {1, 2, 3};
        int[] result = OppositeDirectionPointers.twoSum(arr, 3);
        assertEquals(3, arr[result[0]] + arr[result[1]]);
    }

    @Test
    void testTwoSumNotFound() {
        // No pair sums to 100
        int[] arr = {1, 2, 3, 4};
        int[] result = OppositeDirectionPointers.twoSum(arr, 100);
        assertEquals(-1, result[0]);
        assertEquals(-1, result[1]);
    }

    // ---- reverseArray ---------------------------------------------------

    @Test
    void testReverseArray() {
        int[] arr = {1, 2, 3, 4, 5};
        OppositeDirectionPointers.reverseArray(arr);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, arr);
    }

    @Test
    void testReverseArrayEvenLength() {
        int[] arr = {1, 2, 3, 4};
        OppositeDirectionPointers.reverseArray(arr);
        assertArrayEquals(new int[]{4, 3, 2, 1}, arr);
    }

    @Test
    void testReverseArraySingleElement() {
        int[] arr = {42};
        OppositeDirectionPointers.reverseArray(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testReverseArrayEmpty() {
        int[] arr = {};
        OppositeDirectionPointers.reverseArray(arr);
        assertArrayEquals(new int[]{}, arr);
    }
}
