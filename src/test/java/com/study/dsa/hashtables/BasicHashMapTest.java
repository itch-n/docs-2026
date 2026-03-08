package com.study.dsa.hashtables;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class BasicHashMapTest {

    // ---- twoSum ---------------------------------------------------------

    @Test
    void testTwoSumFound() {
        // {2,7,11,15}, target=9  →  2+7=9, indices [0,1]
        int[] result = BasicHashMap.twoSum(new int[]{2, 7, 11, 15}, 9);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    void testTwoSumNotFound() {
        int[] result = BasicHashMap.twoSum(new int[]{1, 2, 3}, 100);
        assertEquals(-1, result[0]);
        assertEquals(-1, result[1]);
    }

    @Test
    void testTwoSumValues() {
        // Verify values at returned indices sum to target
        int[] nums = {3, 2, 4};
        int target = 6;
        int[] result = BasicHashMap.twoSum(nums, target);
        assertNotEquals(-1, result[0]);
        assertEquals(target, nums[result[0]] + nums[result[1]]);
    }

    // ---- countCharacters ------------------------------------------------

    @Test
    void testCountCharactersHello() {
        Map<Character, Integer> freq = BasicHashMap.countCharacters("hello");
        assertNotNull(freq);
        assertEquals(1, freq.get('h'));
        assertEquals(1, freq.get('e'));
        assertEquals(2, freq.get('l'));
        assertEquals(1, freq.get('o'));
        assertEquals(4, freq.size());
    }

    @Test
    void testCountCharactersMississippi() {
        Map<Character, Integer> freq = BasicHashMap.countCharacters("mississippi");
        assertNotNull(freq);
        assertEquals(1, freq.get('m'));
        assertEquals(4, freq.get('i'));
        assertEquals(4, freq.get('s'));
        assertEquals(2, freq.get('p'));
    }

    @Test
    void testCountCharactersAabbcc() {
        Map<Character, Integer> freq = BasicHashMap.countCharacters("aabbcc");
        assertNotNull(freq);
        assertEquals(2, freq.get('a'));
        assertEquals(2, freq.get('b'));
        assertEquals(2, freq.get('c'));
    }

    // ---- containsDuplicate ----------------------------------------------

    @Test
    void testContainsDuplicateNone() {
        // {1,2,3,4,5}  →  false
        assertFalse(BasicHashMap.containsDuplicate(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void testContainsDuplicateExists() {
        // {1,2,3,1}  →  true
        assertTrue(BasicHashMap.containsDuplicate(new int[]{1, 2, 3, 1}));
    }

    @Test
    void testContainsDuplicateMultiple() {
        // {1,1,1,3,3,4,3,2,4,2}  →  true
        assertTrue(BasicHashMap.containsDuplicate(new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2}));
    }
}
