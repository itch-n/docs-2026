package com.study.dsa.dynamicprogramming;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StockProblemsTest {

    // ---- maxProfit (single transaction) ---------------------------------

    @Test
    void testMaxProfitBuyLowSellHigh() {
        // [7,1,5,3,6,4]: buy at 1, sell at 6 -> profit 5
        assertEquals(5, StockProblems.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }

    @Test
    void testMaxProfitNoProfit() {
        // [7,6,4,3,1]: always decreasing -> profit 0
        assertEquals(0, StockProblems.maxProfit(new int[]{7, 6, 4, 3, 1}));
    }

    @Test
    void testMaxProfitSingleDay() {
        assertEquals(0, StockProblems.maxProfit(new int[]{5}));
    }

    @Test
    void testMaxProfitTwoDays() {
        // [2,4]: profit = 2
        assertEquals(2, StockProblems.maxProfit(new int[]{2, 4}));
    }

    // ---- maxProfitUnlimited (unlimited transactions) --------------------

    @Test
    void testMaxProfitUnlimitedMultipleTransactions() {
        // [7,1,5,3,6,4]: buy 1 sell 5 (+4), buy 3 sell 6 (+3) -> 7
        assertEquals(7, StockProblems.maxProfitUnlimited(new int[]{7, 1, 5, 3, 6, 4}));
    }

    @Test
    void testMaxProfitUnlimitedAlwaysIncreasing() {
        // [1,2,3,4,5]: sum all positive diffs = 4
        assertEquals(4, StockProblems.maxProfitUnlimited(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void testMaxProfitUnlimitedAlwaysDecreasing() {
        // [7,6,5,4,3]: no profitable transaction = 0
        assertEquals(0, StockProblems.maxProfitUnlimited(new int[]{7, 6, 5, 4, 3}));
    }

    @Test
    void testMaxProfitUnlimitedSingleDay() {
        assertEquals(0, StockProblems.maxProfitUnlimited(new int[]{5}));
    }

    // ---- maxProfitCooldown ----------------------------------------------

    @Test
    void testMaxProfitCooldownBasic() {
        // [1,2,3,0,2]: buy@1 sell@3 (cooldown@0) buy@0 sell@2 -> 3
        assertEquals(3, StockProblems.maxProfitCooldown(new int[]{1, 2, 3, 0, 2}));
    }

    @Test
    void testMaxProfitCooldownSingleDay() {
        assertEquals(0, StockProblems.maxProfitCooldown(new int[]{1}));
    }

    @Test
    void testMaxProfitCooldownAlwaysDecreasing() {
        // [5,4,3,2,1]: no profit = 0
        assertEquals(0, StockProblems.maxProfitCooldown(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void testMaxProfitCooldownTwoDays() {
        // [1,2]: buy@1 sell@2 = 1
        assertEquals(1, StockProblems.maxProfitCooldown(new int[]{1, 2}));
    }

    // ---- maxProfitWithFee -----------------------------------------------

    @Test
    void testMaxProfitWithFeeBasic() {
        // [1,3,2,8,4,9], fee=2: buy@1 sell@8 profit=7-2=5, buy@4 sell@9 profit=5-2=3 -> 8
        assertEquals(8, StockProblems.maxProfitWithFee(new int[]{1, 3, 2, 8, 4, 9}, 2));
    }

    @Test
    void testMaxProfitWithFeeNoProfit() {
        // [1,2], fee=2: buy@1 sell@2 profit=1-2=-1 -> 0
        assertEquals(0, StockProblems.maxProfitWithFee(new int[]{1, 2}, 2));
    }

    @Test
    void testMaxProfitWithFeeSingleDay() {
        assertEquals(0, StockProblems.maxProfitWithFee(new int[]{5}, 1));
    }

    @Test
    void testMaxProfitWithFeeSmallFee() {
        // [1,3,2,8,4,9], fee=0: same as unlimited = 13 (1->3=2, 2->8=6, 4->9=5 -> 13)
        assertEquals(13, StockProblems.maxProfitWithFee(new int[]{1, 3, 2, 8, 4, 9}, 0));
    }
}
