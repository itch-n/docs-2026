package com.study.dsa.dynamicprogramming;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecisionProblemsTest {

    // ---- coinChange (minimum coins) ------------------------------------

    @Test
    void testCoinChangeAmount11() {
        // coins=[1,2,5], amount=11 -> 5+5+1 = 3 coins
        assertEquals(3, DecisionProblems.coinChange(new int[]{1, 2, 5}, 11));
    }

    @Test
    void testCoinChangeAmount3() {
        // coins=[1,2,5], amount=3 -> 2+1 = 2 coins
        assertEquals(2, DecisionProblems.coinChange(new int[]{1, 2, 5}, 3));
    }

    @Test
    void testCoinChangeAmountZero() {
        // amount=0 always needs 0 coins
        assertEquals(0, DecisionProblems.coinChange(new int[]{1, 2, 5}, 0));
    }

    @Test
    void testCoinChangeAmount7() {
        // coins=[1,2,5], amount=7 -> 5+2 = 2 coins
        assertEquals(2, DecisionProblems.coinChange(new int[]{1, 2, 5}, 7));
    }

    @Test
    void testCoinChangeImpossible() {
        // coins=[2], amount=3 -> impossible
        assertEquals(-1, DecisionProblems.coinChange(new int[]{2}, 3));
    }

    // ---- change (count ways) -------------------------------------------

    @Test
    void testChangeAmount5() {
        // amount=5, coins=[1,2,5] -> [5],[1,2,2],[1,1,1,2,2 no],[2,2,1],[1,1,2 no]
        // Correct count = 4: [5],[1,4 no],[2,3 no],[1+1+1+1+1],[1+2+2],[1+1+1+2]
        // = [5], [1,1,1,1,1], [1,1,1,2], [1,2,2] = 4
        assertEquals(4, DecisionProblems.change(5, new int[]{1, 2, 5}));
    }

    @Test
    void testChangeAmountZero() {
        // amount=0 -> 1 way (use no coins)
        assertEquals(1, DecisionProblems.change(0, new int[]{1, 2, 5}));
    }

    @Test
    void testChangeImpossible() {
        // amount=3, coins=[2] -> impossible = 0 ways
        assertEquals(0, DecisionProblems.change(3, new int[]{2}));
    }

    @Test
    void testChangeAmount3WithCoins123() {
        // amount=3, coins=[1,2,3] -> [1,1,1],[1,2],[3] = 3 ways
        assertEquals(3, DecisionProblems.change(3, new int[]{1, 2, 3}));
    }

    // ---- numSquares ----------------------------------------------------

    @Test
    void testNumSquares12() {
        // 12 = 4+4+4 -> 3 squares
        assertEquals(3, DecisionProblems.numSquares(12));
    }

    @Test
    void testNumSquares13() {
        // 13 = 9+4 -> 2 squares
        assertEquals(2, DecisionProblems.numSquares(13));
    }

    @Test
    void testNumSquares1() {
        // 1 = 1 -> 1 square
        assertEquals(1, DecisionProblems.numSquares(1));
    }

    @Test
    void testNumSquares4() {
        // 4 = 4 -> 1 square
        assertEquals(1, DecisionProblems.numSquares(4));
    }

    @Test
    void testNumSquares9() {
        // 9 = 9 -> 1 square
        assertEquals(1, DecisionProblems.numSquares(9));
    }

    @Test
    void testNumSquares16() {
        // 16 = 16 -> 1 square
        assertEquals(1, DecisionProblems.numSquares(16));
    }

    // ---- canPartition --------------------------------------------------

    @Test
    void testCanPartitionTrue() {
        // [1,5,11,5] -> sum=22, half=11, can make 11 with [11] or [5+5+1] -> true
        assertTrue(DecisionProblems.canPartition(new int[]{1, 5, 11, 5}));
    }

    @Test
    void testCanPartitionFalse() {
        // [1,2,3,5] -> sum=11 (odd) -> false
        assertFalse(DecisionProblems.canPartition(new int[]{1, 2, 3, 5}));
    }

    @Test
    void testCanPartitionEvenSplit() {
        // [1,2,3,4] -> sum=10, half=5, [1,4] or [2,3] -> true
        assertTrue(DecisionProblems.canPartition(new int[]{1, 2, 3, 4}));
    }

    @Test
    void testCanPartitionTwoEqualElements() {
        // [2,2] -> sum=4, half=2 -> true
        assertTrue(DecisionProblems.canPartition(new int[]{2, 2}));
    }

    @Test
    void testCanPartitionOddSum() {
        // [1,1,1] -> sum=3 (odd) -> false
        assertFalse(DecisionProblems.canPartition(new int[]{1, 1, 1}));
    }
}
