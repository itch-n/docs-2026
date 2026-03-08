package com.study.dsa.backtracking;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PermutationsPatternTest {

    // ---- permute (distinct integers) ------------------------------------

    @Test
    void testPermuteSizeThreeElements() {
        // [1,2,3] has 3! = 6 permutations
        List<List<Integer>> result = PermutationsPattern.permute(new int[]{1, 2, 3});
        assertEquals(6, result.size());
    }

    @Test
    void testPermuteContainsExpectedPermutations() {
        List<List<Integer>> result = PermutationsPattern.permute(new int[]{1, 2, 3});
        // Sort each permutation and sort the list for comparison
        List<List<Integer>> sorted = new ArrayList<>(result);
        for (List<Integer> p : sorted) Collections.sort(p);
        // All 6 distinct permutations must be present
        assertTrue(result.contains(Arrays.asList(1, 2, 3)));
        assertTrue(result.contains(Arrays.asList(1, 3, 2)));
        assertTrue(result.contains(Arrays.asList(2, 1, 3)));
        assertTrue(result.contains(Arrays.asList(2, 3, 1)));
        assertTrue(result.contains(Arrays.asList(3, 1, 2)));
        assertTrue(result.contains(Arrays.asList(3, 2, 1)));
    }

    @Test
    void testPermuteSingleElement() {
        List<List<Integer>> result = PermutationsPattern.permute(new int[]{5});
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(5), result.get(0));
    }

    @Test
    void testPermuteTwoElements() {
        List<List<Integer>> result = PermutationsPattern.permute(new int[]{1, 2});
        assertEquals(2, result.size());
        assertTrue(result.contains(Arrays.asList(1, 2)));
        assertTrue(result.contains(Arrays.asList(2, 1)));
    }

    // ---- permuteUnique (with duplicates) --------------------------------

    @Test
    void testPermuteUniqueSize() {
        // [1,1,2] has 3!/2! = 3 unique permutations
        List<List<Integer>> result = PermutationsPattern.permuteUnique(new int[]{1, 1, 2});
        assertEquals(3, result.size());
    }

    @Test
    void testPermuteUniqueContents() {
        List<List<Integer>> result = PermutationsPattern.permuteUnique(new int[]{1, 1, 2});
        assertTrue(result.contains(Arrays.asList(1, 1, 2)));
        assertTrue(result.contains(Arrays.asList(1, 2, 1)));
        assertTrue(result.contains(Arrays.asList(2, 1, 1)));
    }

    @Test
    void testPermuteUniqueAllSame() {
        // [2,2,2] has only 1 unique permutation
        List<List<Integer>> result = PermutationsPattern.permuteUnique(new int[]{2, 2, 2});
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(2, 2, 2), result.get(0));
    }

    // ---- nextPermutation ------------------------------------------------

    @Test
    void testNextPermutationBasic() {
        // [1,2,3] -> [1,3,2]
        int[] nums = {1, 2, 3};
        PermutationsPattern.nextPermutation(nums);
        assertArrayEquals(new int[]{1, 3, 2}, nums);
    }

    @Test
    void testNextPermutationFromMiddle() {
        // [1,3,2] -> [2,1,3]
        int[] nums = {1, 3, 2};
        PermutationsPattern.nextPermutation(nums);
        assertArrayEquals(new int[]{2, 1, 3}, nums);
    }

    @Test
    void testNextPermutationLastWrapsToFirst() {
        // [3,2,1] is last permutation -> wraps to [1,2,3]
        int[] nums = {3, 2, 1};
        PermutationsPattern.nextPermutation(nums);
        assertArrayEquals(new int[]{1, 2, 3}, nums);
    }

    @Test
    void testNextPermutationSingleElement() {
        int[] nums = {1};
        PermutationsPattern.nextPermutation(nums);
        assertArrayEquals(new int[]{1}, nums);
    }

    @Test
    void testNextPermutationSequenceOf5Steps() {
        // Start [1,2,3], advance 5 times
        int[] nums = {1, 2, 3};
        int[][] expected = {
            {1, 3, 2},
            {2, 1, 3},
            {2, 3, 1},
            {3, 1, 2},
            {3, 2, 1}
        };
        for (int[] exp : expected) {
            PermutationsPattern.nextPermutation(nums);
            assertArrayEquals(exp, nums);
        }
    }
}
