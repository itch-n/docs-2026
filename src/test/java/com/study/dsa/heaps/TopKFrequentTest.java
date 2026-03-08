package com.study.dsa.heaps;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TopKFrequentTest {

    @Test
    void testTopKFrequent() {
        // [1,1,1,2,2,3], k=2 -> top 2 most frequent: [1, 2]
        int[] arr = {1, 1, 1, 2, 2, 3};
        List<Integer> result = TopKFrequent.topKFrequent(arr, 2);
        assertEquals(2, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
    }

    @Test
    void testTopKFrequentSingleElement() {
        int[] arr = {5};
        List<Integer> result = TopKFrequent.topKFrequent(arr, 1);
        assertEquals(1, result.size());
        assertTrue(result.contains(5));
    }

    @Test
    void testFrequencySort() {
        // [1,1,2,2,2,3] -> sorted by freq desc: 2 appears 3x, 1 appears 2x, 3 appears 1x
        // Expected: [2,2,2,1,1,3] (or equivalent ordering within same freq)
        int[] arr = {1, 1, 2, 2, 2, 3};
        int[] sorted = TopKFrequent.frequencySort(arr);
        assertEquals(6, sorted.length);
        // First three should all be 2 (highest frequency)
        assertEquals(2, sorted[0]);
        assertEquals(2, sorted[1]);
        assertEquals(2, sorted[2]);
        // Next two should both be 1
        assertEquals(1, sorted[3]);
        assertEquals(1, sorted[4]);
        // Last should be 3
        assertEquals(3, sorted[5]);
    }

    @Test
    void testKClosest() {
        // points = [[1,3],[-2,2],[5,8],[0,1]], k=2
        // Distances^2: [1,3]=10, [-2,2]=8, [5,8]=89, [0,1]=1
        // 2 closest: [0,1] (dist^2=1) and [-2,2] (dist^2=8)
        int[][] points = {{1, 3}, {-2, 2}, {5, 8}, {0, 1}};
        int[][] closest = TopKFrequent.kClosest(points, 2);
        assertEquals(2, closest.length);
        Set<String> resultSet = new HashSet<>();
        for (int[] p : closest) {
            resultSet.add(p[0] + "," + p[1]);
        }
        assertTrue(resultSet.contains("0,1"));
        assertTrue(resultSet.contains("-2,2"));
    }

    @Test
    void testKClosestAllPoints() {
        int[][] points = {{3, 4}};
        int[][] closest = TopKFrequent.kClosest(points, 1);
        assertEquals(1, closest.length);
    }
}
