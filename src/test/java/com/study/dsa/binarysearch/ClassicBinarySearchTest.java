package com.study.dsa.binarysearch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClassicBinarySearchTest {

    @Test
    void testBinarySearchFound() {
        int[] arr = {1, 3, 5, 7, 9, 11, 13};
        assertEquals(2, ClassicBinarySearch.binarySearch(arr, 5));
        assertEquals(0, ClassicBinarySearch.binarySearch(arr, 1));
        assertEquals(6, ClassicBinarySearch.binarySearch(arr, 13));
        assertEquals(3, ClassicBinarySearch.binarySearch(arr, 7));
    }

    @Test
    void testBinarySearchNotFound() {
        int[] arr = {1, 3, 5, 7, 9, 11, 13};
        assertEquals(-1, ClassicBinarySearch.binarySearch(arr, 8));
        assertEquals(-1, ClassicBinarySearch.binarySearch(arr, 0));
        assertEquals(-1, ClassicBinarySearch.binarySearch(arr, 14));
    }

    @Test
    void testBinarySearchSingleElement() {
        int[] arr = {42};
        assertEquals(0, ClassicBinarySearch.binarySearch(arr, 42));
        assertEquals(-1, ClassicBinarySearch.binarySearch(arr, 1));
    }

    @Test
    void testSearchInsertExact() {
        // [1, 3, 5, 6]: target 5 is at index 2
        int[] arr = {1, 3, 5, 6};
        assertEquals(2, ClassicBinarySearch.searchInsert(arr, 5));
    }

    @Test
    void testSearchInsertBetween() {
        // [1, 3, 5, 6]: target 2 goes at index 1 (between 1 and 3)
        int[] arr = {1, 3, 5, 6};
        assertEquals(1, ClassicBinarySearch.searchInsert(arr, 2));
    }

    @Test
    void testSearchInsertAfterEnd() {
        // [1, 3, 5, 6]: target 7 goes at index 4
        int[] arr = {1, 3, 5, 6};
        assertEquals(4, ClassicBinarySearch.searchInsert(arr, 7));
    }

    @Test
    void testSearchInsertBeforeStart() {
        // [1, 3, 5, 6]: target 0 goes at index 0
        int[] arr = {1, 3, 5, 6};
        assertEquals(0, ClassicBinarySearch.searchInsert(arr, 0));
    }

    @Test
    void testSearchRangeFound() {
        // [5, 7, 7, 8, 8, 8, 10]: target 8 -> [3, 5]
        int[] arr = {5, 7, 7, 8, 8, 8, 10};
        assertArrayEquals(new int[]{3, 5}, ClassicBinarySearch.searchRange(arr, 8));
    }

    @Test
    void testSearchRangeNotFound() {
        int[] arr = {5, 7, 7, 8, 8, 8, 10};
        assertArrayEquals(new int[]{-1, -1}, ClassicBinarySearch.searchRange(arr, 6));
    }

    @Test
    void testSearchRangeSingleOccurrence() {
        int[] arr = {5, 7, 7, 8, 8, 8, 10};
        assertArrayEquals(new int[]{0, 0}, ClassicBinarySearch.searchRange(arr, 5));
        assertArrayEquals(new int[]{6, 6}, ClassicBinarySearch.searchRange(arr, 10));
    }

    @Test
    void testSearchRangeAllSame() {
        int[] arr = {3, 3, 3, 3};
        assertArrayEquals(new int[]{0, 3}, ClassicBinarySearch.searchRange(arr, 3));
    }
}
