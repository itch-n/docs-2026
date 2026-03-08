package com.study.dsa.backtracking;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CombinationsPatternTest {

    // ---- subsets --------------------------------------------------------

    @Test
    void testSubsetsSize() {
        // [1,2,3] has 2^3 = 8 subsets
        List<List<Integer>> result = CombinationsPattern.subsets(new int[]{1, 2, 3});
        assertEquals(8, result.size());
    }

    @Test
    void testSubsetsContainsEmptySet() {
        List<List<Integer>> result = CombinationsPattern.subsets(new int[]{1, 2, 3});
        assertTrue(result.contains(Collections.emptyList()));
    }

    @Test
    void testSubsetsContainsFullSet() {
        List<List<Integer>> result = CombinationsPattern.subsets(new int[]{1, 2, 3});
        assertTrue(result.contains(Arrays.asList(1, 2, 3)));
    }

    @Test
    void testSubsetsContainsAllSingleElements() {
        List<List<Integer>> result = CombinationsPattern.subsets(new int[]{1, 2, 3});
        assertTrue(result.contains(Arrays.asList(1)));
        assertTrue(result.contains(Arrays.asList(2)));
        assertTrue(result.contains(Arrays.asList(3)));
    }

    @Test
    void testSubsetsEmptyInput() {
        // Empty array has one subset: the empty set
        List<List<Integer>> result = CombinationsPattern.subsets(new int[]{});
        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.emptyList()));
    }

    // ---- combine --------------------------------------------------------

    @Test
    void testCombineSize() {
        // C(4,2) = 6
        List<List<Integer>> result = CombinationsPattern.combine(4, 2);
        assertEquals(6, result.size());
    }

    @Test
    void testCombineContainsExpectedPairs() {
        List<List<Integer>> result = CombinationsPattern.combine(4, 2);
        assertTrue(result.contains(Arrays.asList(1, 2)));
        assertTrue(result.contains(Arrays.asList(1, 3)));
        assertTrue(result.contains(Arrays.asList(1, 4)));
        assertTrue(result.contains(Arrays.asList(2, 3)));
        assertTrue(result.contains(Arrays.asList(2, 4)));
        assertTrue(result.contains(Arrays.asList(3, 4)));
    }

    @Test
    void testCombineNEqualsK() {
        // C(3,3) = 1
        List<List<Integer>> result = CombinationsPattern.combine(3, 3);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(1, 2, 3), result.get(0));
    }

    // ---- combinationSum -------------------------------------------------

    @Test
    void testCombinationSumTarget7() {
        // candidates=[2,3,6,7], target=7
        // Expected: [2,2,3] and [7]
        List<List<Integer>> result = CombinationsPattern.combinationSum(new int[]{2, 3, 6, 7}, 7);
        assertEquals(2, result.size());
        assertTrue(result.contains(Arrays.asList(2, 2, 3)));
        assertTrue(result.contains(Arrays.asList(7)));
    }

    @Test
    void testCombinationSumTarget3() {
        // candidates=[2,3,6,7], target=3
        // Expected: [3]
        List<List<Integer>> result = CombinationsPattern.combinationSum(new int[]{2, 3, 6, 7}, 3);
        assertEquals(1, result.size());
        assertTrue(result.contains(Arrays.asList(3)));
    }

    @Test
    void testCombinationSumNoSolution() {
        // candidates=[3,5], target=1 — impossible
        List<List<Integer>> result = CombinationsPattern.combinationSum(new int[]{3, 5}, 1);
        assertEquals(0, result.size());
    }

    // ---- subsetsWithDup -------------------------------------------------

    @Test
    void testSubsetsWithDupSize() {
        // [1,2,2] has 6 unique subsets: [], [1], [2], [1,2], [2,2], [1,2,2]
        List<List<Integer>> result = CombinationsPattern.subsetsWithDup(new int[]{1, 2, 2});
        assertEquals(6, result.size());
    }

    @Test
    void testSubsetsWithDupContents() {
        List<List<Integer>> result = CombinationsPattern.subsetsWithDup(new int[]{1, 2, 2});
        assertTrue(result.contains(Collections.emptyList()));
        assertTrue(result.contains(Arrays.asList(1)));
        assertTrue(result.contains(Arrays.asList(2)));
        assertTrue(result.contains(Arrays.asList(1, 2)));
        assertTrue(result.contains(Arrays.asList(2, 2)));
        assertTrue(result.contains(Arrays.asList(1, 2, 2)));
    }

    @Test
    void testSubsetsWithDupAllSame() {
        // [1,1] -> [], [1], [1,1] = 3 subsets
        List<List<Integer>> result = CombinationsPattern.subsetsWithDup(new int[]{1, 1});
        assertEquals(3, result.size());
        assertTrue(result.contains(Collections.emptyList()));
        assertTrue(result.contains(Arrays.asList(1)));
        assertTrue(result.contains(Arrays.asList(1, 1)));
    }
}
