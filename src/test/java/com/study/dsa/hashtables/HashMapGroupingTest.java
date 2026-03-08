package com.study.dsa.hashtables;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class HashMapGroupingTest {

    // ---- groupAnagrams --------------------------------------------------

    @Test
    void testGroupAnagramsSize() {
        // {"eat","tea","tan","ate","nat","bat"}  →  3 groups
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> groups = HashMapGrouping.groupAnagrams(words);
        assertNotNull(groups);
        assertEquals(3, groups.size());
    }

    @Test
    void testGroupAnagramsContents() {
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> groups = HashMapGrouping.groupAnagrams(words);

        // Sort each group and collect sorted groups for comparison
        Set<List<String>> actual = new HashSet<>();
        for (List<String> group : groups) {
            List<String> sorted = new ArrayList<>(group);
            Collections.sort(sorted);
            actual.add(sorted);
        }

        Set<List<String>> expected = new HashSet<>(Arrays.asList(
            Arrays.asList("ate", "eat", "tea"),
            Arrays.asList("nat", "tan"),
            Arrays.asList("bat")
        ));
        assertEquals(expected, actual);
    }

    @Test
    void testGroupAnagramsSingleWord() {
        List<List<String>> groups = HashMapGrouping.groupAnagrams(new String[]{"abc"});
        assertEquals(1, groups.size());
        assertEquals(1, groups.get(0).size());
    }

    // ---- groupByDigitSum ------------------------------------------------

    @Test
    void testGroupByDigitSumContents() {
        // {12,21,13,31,100,10,1,23,32}
        // digitSum: 12->3, 21->3, 13->4, 31->4, 100->1, 10->1, 1->1, 23->5, 32->5
        int[] numbers = {12, 21, 13, 31, 100, 10, 1, 23, 32};
        Map<Integer, List<Integer>> result = HashMapGrouping.groupByDigitSum(numbers);
        assertNotNull(result);

        // Sum 3: [12, 21]
        List<Integer> sum3 = new ArrayList<>(result.get(3));
        Collections.sort(sum3);
        assertEquals(Arrays.asList(12, 21), sum3);

        // Sum 4: [13, 31]
        List<Integer> sum4 = new ArrayList<>(result.get(4));
        Collections.sort(sum4);
        assertEquals(Arrays.asList(13, 31), sum4);

        // Sum 1: [100, 10, 1]
        List<Integer> sum1 = new ArrayList<>(result.get(1));
        Collections.sort(sum1);
        assertEquals(Arrays.asList(1, 10, 100), sum1);
    }

    // ---- groupByFirstChar -----------------------------------------------

    @Test
    void testGroupByFirstCharContents() {
        // {"apple","ant","ball","bear","cat","car","dog"}
        String[] dictionary = {"apple", "ant", "ball", "bear", "cat", "car", "dog"};
        Map<Character, List<String>> result = HashMapGrouping.groupByFirstChar(dictionary);
        assertNotNull(result);

        List<String> aWords = new ArrayList<>(result.get('a'));
        Collections.sort(aWords);
        assertEquals(Arrays.asList("ant", "apple"), aWords);

        List<String> bWords = new ArrayList<>(result.get('b'));
        Collections.sort(bWords);
        assertEquals(Arrays.asList("ball", "bear"), bWords);

        List<String> cWords = new ArrayList<>(result.get('c'));
        Collections.sort(cWords);
        assertEquals(Arrays.asList("car", "cat"), cWords);

        assertEquals(Arrays.asList("dog"), result.get('d'));
    }
}
