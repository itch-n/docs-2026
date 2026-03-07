package com.study.dsa.dynamicprogramming;

import java.util.*;

public class DecisionProblems {

    /**
     * Problem: Coin change - minimum coins to make amount
     * Time: O(amount * n), Space: O(amount)
     *
     * TODO: Implement using DP
     */
    public static int coinChange(int[] coins, int amount) {
        // TODO: dp[i] = min coins to make amount i
        // TODO: dp[i] = min(dp[i], dp[i - coin] + 1) for each coin
        // TODO: Initialize dp[0] = 0, rest = infinity
        // TODO: Return dp[amount] or -1 if impossible

        return -1; // Replace with implementation
    }

    /**
     * Problem: Coin change II - count ways to make amount
     * Time: O(amount * n), Space: O(amount)
     *
     * TODO: Implement counting ways
     */
    public static int change(int amount, int[] coins) {
        // TODO: dp[i] = ways to make amount i
        // TODO: Implement iteration/conditional logic
        // TODO: dp[i] += dp[i - coin]

        return 0; // Replace with implementation
    }

    /**
     * Problem: Perfect squares - min perfect squares to sum to n
     * Time: O(n * sqrt(n)), Space: O(n)
     *
     * TODO: Implement using DP
     */
    public static int numSquares(int n) {
        // TODO: Similar to coin change
        // TODO: "Coins" are perfect squares: 1, 4, 9, 16, ...
        // TODO: dp[i] = min perfect squares to sum to i

        return 0; // Replace with implementation
    }

    /**
     * Problem: Partition equal subset sum
     * Time: O(n * sum), Space: O(sum)
     *
     * TODO: Implement subset sum DP
     */
    public static boolean canPartition(int[] nums) {
        // TODO: Implement iteration/conditional logic
        // TODO: Problem becomes: can we make sum/2?
        // TODO: dp[i] = can we make sum i?
        // TODO: Implement iteration/conditional logic

        return false; // Replace with implementation
    }
}
