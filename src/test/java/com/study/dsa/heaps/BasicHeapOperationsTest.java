package com.study.dsa.heaps;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BasicHeapOperationsTest {

    @Test
    void testFindKthLargest() {
        // [3, 2, 1, 5, 6, 4], k=2 -> 2nd largest = 5
        int[] arr = {3, 2, 1, 5, 6, 4};
        assertEquals(5, BasicHeapOperations.findKthLargest(arr, 2));
    }

    @Test
    void testFindKthLargestFirst() {
        int[] arr = {3, 2, 1, 5, 6, 4};
        assertEquals(6, BasicHeapOperations.findKthLargest(arr, 1));
    }

    @Test
    void testFindKthLargestLast() {
        int[] arr = {3, 2, 1, 5, 6, 4};
        assertEquals(1, BasicHeapOperations.findKthLargest(arr, 6));
    }

    @Test
    void testKLargestSize() {
        // [3, 2, 3, 1, 2, 4, 5, 5, 6], k=4 -> 4 largest elements
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        List<Integer> result = BasicHeapOperations.kLargest(arr, 4);
        assertEquals(4, result.size());
        // All returned elements should be >= 4 (the 4 largest are 4,5,5,6)
        for (int v : result) {
            assertTrue(v >= 4, "Expected element >= 4, got: " + v);
        }
    }

    @Test
    void testHeapSort() {
        int[] arr = {5, 2, 8, 1, 9, 3};
        int[] sorted = BasicHeapOperations.heapSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 5, 8, 9}, sorted);
    }

    @Test
    void testHeapSortSingleElement() {
        int[] arr = {7};
        assertArrayEquals(new int[]{7}, BasicHeapOperations.heapSort(arr));
    }

    @Test
    void testHeapSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, BasicHeapOperations.heapSort(arr));
    }

    @Test
    void testHeapSortReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, BasicHeapOperations.heapSort(arr));
    }
}
