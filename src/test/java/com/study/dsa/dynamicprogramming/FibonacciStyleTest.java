package com.study.dsa.dynamicprogramming;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FibonacciStyleTest {

    // ---- climbStairs ----------------------------------------------------

    @Test
    void testClimbStairs1() {
        assertEquals(1, FibonacciStyle.climbStairs(1));
    }

    @Test
    void testClimbStairs2() {
        assertEquals(2, FibonacciStyle.climbStairs(2));
    }

    @Test
    void testClimbStairs3() {
        assertEquals(3, FibonacciStyle.climbStairs(3));
    }

    @Test
    void testClimbStairs4() {
        assertEquals(5, FibonacciStyle.climbStairs(4));
    }

    @Test
    void testClimbStairs5() {
        assertEquals(8, FibonacciStyle.climbStairs(5));
    }

    @Test
    void testClimbStairs10() {
        assertEquals(89, FibonacciStyle.climbStairs(10));
    }

    // ---- rob (house robber linear) --------------------------------------

    @Test
    void testRobFourHouses() {
        // [1,2,3,1] -> rob houses 0 and 2: 1+3=4
        assertEquals(4, FibonacciStyle.rob(new int[]{1, 2, 3, 1}));
    }

    @Test
    void testRobFiveHouses() {
        // [2,7,9,3,1] -> rob houses 0,2,4: 2+9+1=12
        assertEquals(12, FibonacciStyle.rob(new int[]{2, 7, 9, 3, 1}));
    }

    @Test
    void testRobTieBreaker() {
        // [2,1,1,2] -> rob houses 0 and 3: 2+2=4
        assertEquals(4, FibonacciStyle.rob(new int[]{2, 1, 1, 2}));
    }

    @Test
    void testRobSingleHouse() {
        assertEquals(5, FibonacciStyle.rob(new int[]{5}));
    }

    @Test
    void testRobTwoHouses() {
        // [3,10] -> rob house 1: 10
        assertEquals(10, FibonacciStyle.rob(new int[]{3, 10}));
    }

    // ---- robCircular (house robber II) ----------------------------------

    @Test
    void testRobCircularThreeHouses() {
        // [2,3,2] -> can't rob 0 and 2 (adjacent in circle); best is 3
        assertEquals(3, FibonacciStyle.robCircular(new int[]{2, 3, 2}));
    }

    @Test
    void testRobCircularFourHouses() {
        // [1,2,3,1] -> rob 0 and 2 (linear sub-problem) = 4; or 1 and 3 = 3 -> 4
        assertEquals(4, FibonacciStyle.robCircular(new int[]{1, 2, 3, 1}));
    }

    @Test
    void testRobCircularThreeAscending() {
        // [1,2,3] -> can't take 1 and 3; best is take 3 alone = 3
        assertEquals(3, FibonacciStyle.robCircular(new int[]{1, 2, 3}));
    }

    @Test
    void testRobCircularSingleHouse() {
        assertEquals(5, FibonacciStyle.robCircular(new int[]{5}));
    }

    // ---- minCostClimbingStairs ------------------------------------------

    @Test
    void testMinCostThreeSteps() {
        // cost=[10,15,20]: start from step 0 (cost 10, then jump 2) = 10+15? No.
        // Step 0 costs 10 (can reach top in 2 jumps from 0): 10+15=25? Or start at 1: 15+20=35?
        // Optimal: take step 0 (cost 10) jump to step 2 (cost 20) jump to top = 10+20=30? No.
        // dp[0]=10, dp[1]=15, dp[2]=20+min(10,15)=30; answer = min(dp[1],dp[2]) = min(15,30)=15? No.
        // Canonical answer for [10,15,20] = 15 (start at step 1, cost 15, jump 2 to top).
        assertEquals(15, FibonacciStyle.minCostClimbingStairs(new int[]{10, 15, 20}));
    }

    @Test
    void testMinCostTenSteps() {
        // [1,100,1,1,1,100,1,1,100,1] -> expected 6
        assertEquals(6, FibonacciStyle.minCostClimbingStairs(
            new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
    }

    @Test
    void testMinCostTwoSteps() {
        // [1,2]: can start at step 0 (cost 1, jump 1 to step 1, cost 2, jump 1 to top = 3)
        // or start at step 0 (cost 1, jump 2 to top = 1), or step 1 (cost 2, jump 1 = 2)
        // min cost = 1
        assertEquals(1, FibonacciStyle.minCostClimbingStairs(new int[]{1, 2}));
    }
}
