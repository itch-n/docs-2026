package com.study.dsa.binarysearch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RotatedArraySearchTest {

    @Test
    void testSearchFoundAfterRotation() {
        // [4, 5, 6, 7, 0, 1, 2]: target 0 is at index 4
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(4, RotatedArraySearch.search(rotated, 0));
    }

    @Test
    void testSearchFoundInLeftHalf() {
        // [4, 5, 6, 7, 0, 1, 2]: target 4 is at index 0
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(0, RotatedArraySearch.search(rotated, 4));
    }

    @Test
    void testSearchFoundMid() {
        // [4, 5, 6, 7, 0, 1, 2]: target 7 is at index 3
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(3, RotatedArraySearch.search(rotated, 7));
    }

    @Test
    void testSearchNotFound() {
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(-1, RotatedArraySearch.search(rotated, 3));
    }

    @Test
    void testSearchNotRotated() {
        int[] arr = {1, 2, 3, 4, 5};
        assertEquals(2, RotatedArraySearch.search(arr, 3));
    }

    @Test
    void testFindMinRotated() {
        // [3, 4, 5, 1, 2] -> min = 1
        assertEquals(1, RotatedArraySearch.findMin(new int[]{3, 4, 5, 1, 2}));
        // [4, 5, 6, 7, 0, 1, 2] -> min = 0
        assertEquals(0, RotatedArraySearch.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
        // [11, 13, 15, 17] (no rotation) -> min = 11
        assertEquals(11, RotatedArraySearch.findMin(new int[]{11, 13, 15, 17}));
    }

    @Test
    void testFindRotationCount() {
        // [3, 4, 5, 1, 2]: min is at index 3 -> 3 rotations
        assertEquals(3, RotatedArraySearch.findRotationCount(new int[]{3, 4, 5, 1, 2}));
        // [4, 5, 6, 7, 0, 1, 2]: min is at index 4 -> 4 rotations
        assertEquals(4, RotatedArraySearch.findRotationCount(new int[]{4, 5, 6, 7, 0, 1, 2}));
        // [11, 13, 15, 17]: no rotation, min at index 0 -> 0 rotations
        assertEquals(0, RotatedArraySearch.findRotationCount(new int[]{11, 13, 15, 17}));
    }
}
