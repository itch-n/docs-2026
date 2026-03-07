package com.study.dsa.dynamicprogramming;

public class FibonacciStyle {

    /**
     * Problem: Climbing stairs (1 or 2 steps at a time)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using DP
     */
    public static int climbStairs(int n) {
        // TODO: Base cases: n=1 -> 1, n=2 -> 2

        // TODO: DP approach (bottom-up):

        return 0; // Replace with implementation
    }

    /**
     * Problem: House robber (can't rob adjacent houses)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using DP
     */
    public static int rob(int[] nums) {
        // TODO: dp[i] = max money robbing up to house i
        // TODO: dp[i] = max(dp[i-1], dp[i-2] + nums[i])
        // TODO: Optimize space: only need last 2 values

        return 0; // Replace with implementation
    }

    /**
     * Problem: House robber II (houses in a circle)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement circular version
     */
    public static int robCircular(int[] nums) {
        // TODO: Can't rob both first and last house
        // TODO: Try two scenarios:
        // TODO: Return max of both

        return 0; // Replace with implementation
    }

    /**
     * Problem: Min cost climbing stairs
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement min cost DP
     */
    public static int minCostClimbingStairs(int[] cost) {
        // TODO: dp[i] = min cost to reach step i
        // TODO: dp[i] = cost[i] + min(dp[i-1], dp[i-2])
        // TODO: Can start from step 0 or 1

        return 0; // Replace with implementation
    }
}
