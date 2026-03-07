package com.study.dsa.dynamicprogramming;

public class StockProblems {

    /**
     * Problem: Best time to buy and sell stock (one transaction)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement single transaction
     */
    public static int maxProfit(int[] prices) {
        // TODO: Track minimum price seen so far
        // TODO: Track maximum profit (price - minPrice)
        // TODO: One pass solution

        return 0; // Replace with implementation
    }

    /**
     * Problem: Best time to buy and sell stock II (unlimited transactions)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement unlimited transactions
     */
    public static int maxProfitUnlimited(int[] prices) {
        // TODO: Sum all positive differences
        // TODO: Buy before every increase, sell at peak

        return 0; // Replace with implementation
    }

    /**
     * Problem: Best time to buy and sell stock with cooldown
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement with cooldown
     */
    public static int maxProfitCooldown(int[] prices) {
        // TODO: Track three states:
        // TODO: Transitions: hold -> sold -> rest -> hold

        return 0; // Replace with implementation
    }

    /**
     * Problem: Best time to buy and sell stock with fee
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement with transaction fee
     */
    public static int maxProfitWithFee(int[] prices, int fee) {
        // TODO: Similar to cooldown
        // TODO: Subtract fee when selling

        return 0; // Replace with implementation
    }
}
