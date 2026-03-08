package com.study.dsa.twopointers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class SameDirectionPointersTest {

    // ---- removeDuplicates -----------------------------------------------

    @Test
    void testRemoveDuplicatesTypical() {
        // {1,1,2,2,2,3,4,4,5}  →  unique: {1,2,3,4,5}, length = 5
        int[] arr = {1, 1, 2, 2, 2, 3, 4, 4, 5};
        int newLength = SameDirectionPointers.removeDuplicates(arr);
        assertEquals(5, newLength);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Arrays.copyOf(arr, newLength));
    }

    @Test
    void testRemoveDuplicatesNoDuplicates() {
        int[] arr = {1, 2, 3};
        int newLength = SameDirectionPointers.removeDuplicates(arr);
        assertEquals(3, newLength);
        assertArrayEquals(new int[]{1, 2, 3}, Arrays.copyOf(arr, newLength));
    }

    @Test
    void testRemoveDuplicatesAllSame() {
        int[] arr = {5, 5, 5, 5};
        int newLength = SameDirectionPointers.removeDuplicates(arr);
        assertEquals(1, newLength);
        assertEquals(5, arr[0]);
    }

    @Test
    void testRemoveDuplicatesEmpty() {
        int[] arr = {};
        int newLength = SameDirectionPointers.removeDuplicates(arr);
        assertEquals(0, newLength);
    }

    // ---- moveZeroes -----------------------------------------------------

    @Test
    void testMoveZeroesTypical() {
        // {0,1,0,3,12,0,5}  →  {1,3,12,5,0,0,0}
        int[] arr = {0, 1, 0, 3, 12, 0, 5};
        SameDirectionPointers.moveZeroes(arr);
        // All non-zero elements appear first in order
        assertArrayEquals(new int[]{1, 3, 12, 5, 0, 0, 0}, arr);
    }

    @Test
    void testMoveZeroesNoZeroes() {
        int[] arr = {1, 2, 3};
        SameDirectionPointers.moveZeroes(arr);
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }

    @Test
    void testMoveZeroesAllZeroes() {
        int[] arr = {0, 0, 0};
        SameDirectionPointers.moveZeroes(arr);
        assertArrayEquals(new int[]{0, 0, 0}, arr);
    }

    // ---- partition -------------------------------------------------------

    @Test
    void testPartitionTypical() {
        // {7,2,9,1,5,3,8}, pivot=5
        // All elements before partitionIdx should be < 5
        int[] arr = {7, 2, 9, 1, 5, 3, 8};
        int pivot = 5;
        int idx = SameDirectionPointers.partition(arr, pivot);
        // Verify all elements before idx are < pivot
        for (int i = 0; i < idx; i++) {
            assertTrue(arr[i] < pivot,
                "Element at index " + i + " (" + arr[i] + ") should be < " + pivot);
        }
        // Verify count of elements < pivot equals idx
        long countLess = 0;
        for (int v : new int[]{7, 2, 9, 1, 5, 3, 8}) {
            if (v < pivot) countLess++;
        }
        assertEquals(countLess, idx);
    }

    @Test
    void testPartitionAllLess() {
        // {1, 2, 3}, pivot=5  →  all elements < pivot, idx=3
        int[] arr = {1, 2, 3};
        int idx = SameDirectionPointers.partition(arr, 5);
        assertEquals(3, idx);
    }

    @Test
    void testPartitionNoneLess() {
        // {6, 7, 8}, pivot=5  →  no elements < pivot, idx=0
        int[] arr = {6, 7, 8};
        int idx = SameDirectionPointers.partition(arr, 5);
        assertEquals(0, idx);
    }
}
