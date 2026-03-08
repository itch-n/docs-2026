package com.study.dsa.hashtables;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class HashSetOperationsTest {

    // ---- intersection ---------------------------------------------------

    @Test
    void testIntersectionSimple() {
        // {1,2,2,1} ∩ {2,2}  →  {2}
        int[] result = HashSetOperations.intersection(new int[]{1, 2, 2, 1}, new int[]{2, 2});
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(2, result[0]);
    }

    @Test
    void testIntersectionMultipleCommon() {
        // {4,9,5} ∩ {9,4,9,8,4}  →  {4,9} (order may vary)
        int[] result = HashSetOperations.intersection(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4});
        assertNotNull(result);
        assertEquals(2, result.length);
        // Sort both to compare regardless of order
        Arrays.sort(result);
        assertArrayEquals(new int[]{4, 9}, result);
    }

    @Test
    void testIntersectionEmpty() {
        // {1,2} ∩ {3,4}  →  {}
        int[] result = HashSetOperations.intersection(new int[]{1, 2}, new int[]{3, 4});
        assertEquals(0, result.length);
    }

    // ---- missingNumber --------------------------------------------------

    @Test
    void testMissingNumber2() {
        // {3,0,1}  →  missing 2
        assertEquals(2, HashSetOperations.missingNumber(new int[]{3, 0, 1}));
    }

    @Test
    void testMissingNumber2_v2() {
        // {0,1}  →  missing 2
        assertEquals(2, HashSetOperations.missingNumber(new int[]{0, 1}));
    }

    @Test
    void testMissingNumber8() {
        // {9,6,4,2,3,5,7,0,1}  →  missing 8
        assertEquals(8, HashSetOperations.missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
    }

    // ---- longestConsecutive ---------------------------------------------

    @Test
    void testLongestConsecutive4() {
        // {100,4,200,1,3,2}  →  1,2,3,4 = length 4
        assertEquals(4, HashSetOperations.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
    }

    @Test
    void testLongestConsecutive9() {
        // {0,3,7,2,5,8,4,6,0,1}  →  0,1,2,3,4,5,6,7,8 = length 9
        assertEquals(9, HashSetOperations.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
    }

    @Test
    void testLongestConsecutiveNegatives() {
        // {9,1,-3,2,4,8,3,-1,6,-2,-4,7}  →  -3,-2,-1 = 3, or 1,2,3,4 = 4... let's check:
        // Negatives: -4,-3,-2,-1 = 4; positives: 1,2,3,4 = 4, 6,7,8,9 = 4  →  max = 4
        assertEquals(4, HashSetOperations.longestConsecutive(
            new int[]{9, 1, -3, 2, 4, 8, 3, -1, 6, -2, -4, 7}));
    }
}
