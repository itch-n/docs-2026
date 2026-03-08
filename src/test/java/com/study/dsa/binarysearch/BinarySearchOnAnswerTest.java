package com.study.dsa.binarysearch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchOnAnswerTest {

    @Test
    void testMySqrtPerfectSquare() {
        assertEquals(2, BinarySearchOnAnswer.mySqrt(4));
        assertEquals(4, BinarySearchOnAnswer.mySqrt(16));
    }

    @Test
    void testMySqrtFloor() {
        // sqrt(8) = 2.828... -> floor = 2
        assertEquals(2, BinarySearchOnAnswer.mySqrt(8));
        // sqrt(27) = 5.196... -> floor = 5
        assertEquals(5, BinarySearchOnAnswer.mySqrt(27));
    }

    @Test
    void testMySqrtZeroAndOne() {
        assertEquals(0, BinarySearchOnAnswer.mySqrt(0));
        assertEquals(1, BinarySearchOnAnswer.mySqrt(1));
    }

    @Test
    void testShipWithinDays() {
        // weights = [1..10], days = 5 -> minimum capacity = 15
        int[] weights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(15, BinarySearchOnAnswer.shipWithinDays(weights, 5));
    }

    @Test
    void testShipWithinDaysSmall() {
        // weights = [1, 2, 3, 1, 1], days = 4 -> capacity = 3
        int[] weights = {1, 2, 3, 1, 1};
        assertEquals(3, BinarySearchOnAnswer.shipWithinDays(weights, 4));
    }

    @Test
    void testShipWithinDaysOneDayShipsAll() {
        // With 1 day, capacity must be sum of all weights
        int[] weights = {3, 2, 2, 4, 1, 4};
        assertEquals(16, BinarySearchOnAnswer.shipWithinDays(weights, 1));
    }

    @Test
    void testFindKthPositive() {
        // arr = [2, 3, 4, 7, 11], k = 5
        // Missing positives: 1, 5, 6, 8, 9 -> 5th is 9
        int[] arr = {2, 3, 4, 7, 11};
        assertEquals(9, BinarySearchOnAnswer.findKthPositive(arr, 5));
    }

    @Test
    void testFindKthPositiveFirst() {
        // arr = [2, 3, 4, 7, 11], k = 1 -> first missing is 1
        int[] arr = {2, 3, 4, 7, 11};
        assertEquals(1, BinarySearchOnAnswer.findKthPositive(arr, 1));
    }

    @Test
    void testFindKthPositiveBeyondArray() {
        // arr = [1, 2, 3], k = 2 -> missing: 4, 5 -> 2nd missing = 5
        int[] arr = {1, 2, 3};
        assertEquals(5, BinarySearchOnAnswer.findKthPositive(arr, 2));
    }
}
