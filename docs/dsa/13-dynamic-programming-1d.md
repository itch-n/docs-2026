# 12. Dynamic Programming (1D)

> Turn exponential recursion into polynomial iteration by caching subproblem results

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is dynamic programming in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **How is DP different from regular recursion?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "DP is like saving your homework answers so you don't have to recalculate the same problems..."
   - Your analogy: _[Fill in]_

4. **When does this pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **What's the difference between top-down and bottom-up?**
   - Your answer: _[Fill in after learning both approaches]_

---

## Core Implementation

### Pattern 1: Fibonacci-Style Problems

**Concept:** Each state depends on previous 1-2 states.

**Use case:** Climbing stairs, house robber, decode ways.

```java
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
        //   dp[i] = ways to reach step i
        //   dp[i] = dp[i-1] + dp[i-2]
        //   Optimize space: only need last 2 values

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
        //   Either skip house i or rob it (can't rob i-1)
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
        //   1. Rob houses 0 to n-2
        //   2. Rob houses 1 to n-1
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
```

**Runnable Client Code:**

```java
import java.util.*;

public class FibonacciStyleClient {

    public static void main(String[] args) {
        System.out.println("=== Fibonacci-Style DP ===\n");

        // Test 1: Climbing stairs
        System.out.println("--- Test 1: Climbing Stairs ---");
        int[] stairs = {1, 2, 3, 4, 5, 10};
        for (int n : stairs) {
            int ways = FibonacciStyle.climbStairs(n);
            System.out.printf("n=%d: %d ways%n", n, ways);
        }

        // Test 2: House robber
        System.out.println("\n--- Test 2: House Robber ---");
        int[][] houses = {
            {1, 2, 3, 1},
            {2, 7, 9, 3, 1},
            {2, 1, 1, 2}
        };

        for (int[] house : houses) {
            int maxMoney = FibonacciStyle.rob(house);
            System.out.printf("Houses: %s -> Max: %d%n",
                Arrays.toString(house), maxMoney);
        }

        // Test 3: House robber II (circular)
        System.out.println("\n--- Test 3: House Robber II ---");
        int[][] circularHouses = {
            {2, 3, 2},
            {1, 2, 3, 1},
            {1, 2, 3}
        };

        for (int[] house : circularHouses) {
            int maxMoney = FibonacciStyle.robCircular(house);
            System.out.printf("Houses: %s -> Max: %d%n",
                Arrays.toString(house), maxMoney);
        }

        // Test 4: Min cost climbing
        System.out.println("\n--- Test 4: Min Cost Climbing Stairs ---");
        int[][] costs = {
            {10, 15, 20},
            {1, 100, 1, 1, 1, 100, 1, 1, 100, 1}
        };

        for (int[] cost : costs) {
            int minCost = FibonacciStyle.minCostClimbingStairs(cost);
            System.out.printf("Cost: %s -> Min: %d%n",
                Arrays.toString(cost), minCost);
        }
    }
}
```

---

### Pattern 2: Decision Problems (Take/Skip)

**Concept:** At each position, decide to take or skip element.

**Use case:** Coin change, subset sum, partition equal subset.

```java
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
        // TODO: For each coin, update all amounts >= coin
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
        // TODO: If total sum is odd, return false
        // TODO: Problem becomes: can we make sum/2?
        // TODO: dp[i] = can we make sum i?
        // TODO: For each num, update dp array backwards

        return false; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class DecisionProblemsClient {

    public static void main(String[] args) {
        System.out.println("=== Decision Problems ===\n");

        // Test 1: Coin change
        System.out.println("--- Test 1: Coin Change ---");
        int[] coins = {1, 2, 5};
        int[] amounts = {11, 3, 0, 7};

        System.out.println("Coins: " + Arrays.toString(coins));
        for (int amount : amounts) {
            int result = DecisionProblems.coinChange(coins, amount);
            System.out.printf("Amount %d: %d coins%n", amount, result);
        }

        // Test 2: Coin change II (count ways)
        System.out.println("\n--- Test 2: Coin Change II ---");
        int amount = 5;
        int[] coins2 = {1, 2, 5};

        System.out.println("Amount: " + amount);
        System.out.println("Coins: " + Arrays.toString(coins2));
        int ways = DecisionProblems.change(amount, coins2);
        System.out.println("Ways: " + ways);

        // Test 3: Perfect squares
        System.out.println("\n--- Test 3: Perfect Squares ---");
        int[] numbers = {12, 13, 1, 4, 9, 16};

        for (int n : numbers) {
            int count = DecisionProblems.numSquares(n);
            System.out.printf("n=%d: %d perfect squares%n", n, count);
        }

        // Test 4: Partition equal subset
        System.out.println("\n--- Test 4: Partition Equal Subset ---");
        int[][] arrays = {
            {1, 5, 11, 5},
            {1, 2, 3, 5},
            {1, 2, 3, 4}
        };

        for (int[] arr : arrays) {
            boolean canPartition = DecisionProblems.canPartition(arr);
            System.out.printf("Array: %s -> %s%n",
                Arrays.toString(arr), canPartition ? "YES" : "NO");
        }
    }
}
```

---

### Pattern 3: String DP

**Concept:** Build solution character by character.

**Use case:** Decode ways, word break, palindrome partitioning.

```java
import java.util.*;

public class StringDP {

    /**
     * Problem: Decode ways (1=A, 2=B, ..., 26=Z)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement decode ways
     */
    public static int numDecodings(String s) {
        // TODO: dp[i] = ways to decode substring 0..i
        // TODO: Single digit: if s[i] != '0', dp[i] += dp[i-1]
        // TODO: Two digits: if 10 <= s[i-1:i] <= 26, dp[i] += dp[i-2]
        // TODO: Optimize space: only need last 2 values

        return 0; // Replace with implementation
    }

    /**
     * Problem: Word break - can string be segmented into words
     * Time: O(n^2 * m) where m = avg word length, Space: O(n)
     *
     * TODO: Implement word break
     */
    public static boolean wordBreak(String s, List<String> wordDict) {
        // TODO: dp[i] = can substring 0..i be segmented?
        // TODO: dp[i] = true if any dp[j] && s.substring(j,i) in dict
        // TODO: Use HashSet for O(1) word lookup

        return false; // Replace with implementation
    }

    /**
     * Problem: Longest increasing subsequence
     * Time: O(n^2), Space: O(n)
     *
     * TODO: Implement LIS using DP
     */
    public static int lengthOfLIS(int[] nums) {
        // TODO: dp[i] = length of LIS ending at i
        // TODO: dp[i] = max(dp[j] + 1) for all j < i where nums[j] < nums[i]
        // TODO: Return max value in dp array

        return 0; // Replace with implementation
    }

    /**
     * Problem: Longest palindromic substring
     * Time: O(n^2), Space: O(n^2) or O(1) with expand from center
     *
     * TODO: Implement LPS
     */
    public static String longestPalindrome(String s) {
        // TODO: Expand around center approach (space O(1))
        // TODO: Try each position as center (odd and even length)
        // TODO: Or use DP: dp[i][j] = is substring i..j palindrome?

        return ""; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class StringDPClient {

    public static void main(String[] args) {
        System.out.println("=== String DP ===\n");

        // Test 1: Decode ways
        System.out.println("--- Test 1: Decode Ways ---");
        String[] codes = {"12", "226", "06", "10"};

        for (String code : codes) {
            int ways = StringDP.numDecodings(code);
            System.out.printf("Code \"%s\": %d ways%n", code, ways);
        }

        // Test 2: Word break
        System.out.println("\n--- Test 2: Word Break ---");
        List<String> dict = Arrays.asList("leet", "code", "sand", "and", "cat");
        String[] testStrings = {"leetcode", "catsand", "catsandog"};

        System.out.println("Dictionary: " + dict);
        for (String s : testStrings) {
            boolean canBreak = StringDP.wordBreak(s, dict);
            System.out.printf("String \"%s\": %s%n", s, canBreak ? "YES" : "NO");
        }

        // Test 3: Longest increasing subsequence
        System.out.println("\n--- Test 3: Longest Increasing Subsequence ---");
        int[][] sequences = {
            {10, 9, 2, 5, 3, 7, 101, 18},
            {0, 1, 0, 3, 2, 3},
            {7, 7, 7, 7}
        };

        for (int[] seq : sequences) {
            int length = StringDP.lengthOfLIS(seq);
            System.out.printf("Array: %s -> LIS length: %d%n",
                Arrays.toString(seq), length);
        }

        // Test 4: Longest palindromic substring
        System.out.println("\n--- Test 4: Longest Palindromic Substring ---");
        String[] palindromeTests = {"babad", "cbbd", "racecar", "noon"};

        for (String s : palindromeTests) {
            String lps = StringDP.longestPalindrome(s);
            System.out.printf("String \"%s\" -> LPS: \"%s\"%n", s, lps);
        }
    }
}
```

---

### Pattern 4: Buy/Sell Stock Problems

**Concept:** Track states (holding/not holding stock) through array.

**Use case:** Stock trading with various constraints.

```java
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
        //   hold: max profit with stock
        //   sold: max profit just sold
        //   rest: max profit resting (not holding)
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
```

**Runnable Client Code:**

```java
import java.util.*;

public class StockProblemsClient {

    public static void main(String[] args) {
        System.out.println("=== Stock Problems ===\n");

        // Test 1: Single transaction
        System.out.println("--- Test 1: Single Transaction ---");
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        System.out.println("Prices: " + Arrays.toString(prices1));
        int profit1 = StockProblems.maxProfit(prices1);
        System.out.println("Max profit: " + profit1);

        // Test 2: Unlimited transactions
        System.out.println("\n--- Test 2: Unlimited Transactions ---");
        int[] prices2 = {7, 1, 5, 3, 6, 4};
        System.out.println("Prices: " + Arrays.toString(prices2));
        int profit2 = StockProblems.maxProfitUnlimited(prices2);
        System.out.println("Max profit: " + profit2);

        // Test 3: With cooldown
        System.out.println("\n--- Test 3: With Cooldown ---");
        int[] prices3 = {1, 2, 3, 0, 2};
        System.out.println("Prices: " + Arrays.toString(prices3));
        int profit3 = StockProblems.maxProfitCooldown(prices3);
        System.out.println("Max profit: " + profit3);

        // Test 4: With fee
        System.out.println("\n--- Test 4: With Transaction Fee ---");
        int[] prices4 = {1, 3, 2, 8, 4, 9};
        int fee = 2;
        System.out.println("Prices: " + Arrays.toString(prices4));
        System.out.println("Fee: " + fee);
        int profit4 = StockProblems.maxProfitWithFee(prices4, fee);
        System.out.println("Max profit: " + profit4);
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for 1D DP problems.

### Question 1: What defines a state?

Answer after solving problems:
- **Single index?** _[1D DP array]_
- **Two indices?** _[2D DP - next topic]_
- **Additional state (holding/not)?** _[Multiple DP arrays or states]_
- **Your observation:** _[Fill in]_

### Question 2: Top-down or bottom-up?

**Top-down (Memoization):**
- Pros: _[Natural recursion, only compute needed states]_
- Cons: _[Stack space, slightly slower]_
- Use when: _[Complex recurrence, not all states needed]_

**Bottom-up (Tabulation):**
- Pros: _[No stack, often faster, space optimization]_
- Cons: _[Must compute all states]_
- Use when: _[Simple iteration order, need all states]_

### Your Decision Tree

```
1D DP Pattern Selection
│
├─ Depends on previous 1-2 states?
│   └─ Use: Fibonacci-style DP ✓
│         Space optimize to O(1)
│
├─ Take/skip decision at each element?
│   └─ Use: Decision DP ✓
│         Often need sum constraint
│
├─ String problem?
│   ├─ Build character by character → String DP ✓
│   ├─ Subsequence → 2D DP (next topic)
│   └─ Expand from center → O(1) space
│
└─ State machine (multiple states)?
    └─ Use: Multiple DP arrays ✓
          Track each state separately
```

### The "Kill Switch" - When NOT to use 1D DP

**Don't use when:**
1. _[Need 2D state - use 2D DP]_
2. _[Greedy works - simpler and faster]_
3. _[Can use math formula - O(1)]_
4. _[State space too large - need optimization]_

### The Rule of Three: Alternatives

**Option 1: Dynamic Programming**
- Pros: _[Optimal solution, polynomial time]_
- Cons: _[Need optimal substructure]_
- Use when: _[Overlapping subproblems]_

**Option 2: Greedy**
- Pros: _[Simpler, faster]_
- Cons: _[Doesn't always work]_
- Use when: _[Greedy choice property holds]_

**Option 3: Recursion with Memoization**
- Pros: _[More intuitive]_
- Cons: _[Stack overhead]_
- Use when: _[Complex recurrence]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 3):**
- [ ] [70. Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)
  - Pattern: _[Fibonacci-style]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [746. Min Cost Climbing Stairs](https://leetcode.com/problems/min-cost-climbing-stairs/)
  - Pattern: _[Fibonacci with cost]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [121. Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)
  - Pattern: _[Single transaction]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 4-5):**
- [ ] [198. House Robber](https://leetcode.com/problems/house-robber/)
  - Pattern: _[Fibonacci-style decision]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [322. Coin Change](https://leetcode.com/problems/coin-change/)
  - Pattern: _[Decision DP]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [139. Word Break](https://leetcode.com/problems/word-break/)
  - Pattern: _[String DP]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [300. Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)
  - Pattern: _[Sequence DP]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [91. Decode Ways](https://leetcode.com/problems/decode-ways/)
  - Pattern: _[String DP]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

**Hard (Optional):**
- [ ] [152. Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/)
  - Pattern: _[Track min and max]_
  - Key insight: _[Fill in after solving]_

- [ ] [132. Palindrome Partitioning II](https://leetcode.com/problems/palindrome-partitioning-ii/)
  - Pattern: _[String DP with cut optimization]_
  - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Fibonacci-style: stairs, robber, min cost all work
  - [ ] Decision: coin change, partition all work
  - [ ] String: decode ways, word break, LIS all work
  - [ ] Stock: single, unlimited, cooldown, fee all work
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify overlapping subproblems
  - [ ] Understand recurrence relations
  - [ ] Know when to use top-down vs bottom-up
  - [ ] Can optimize space complexity

- [ ] **Problem Solving**
  - [ ] Solved 3 easy problems
  - [ ] Solved 4-5 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Understood state transitions

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use DP
  - [ ] Can explain memoization vs tabulation

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand how to derive recurrence

---

**Next Topic:** [14. Dynamic Programming (2D) →](14-dynamic-programming-2d.md)

**Back to:** [12. Backtracking ←](12-backtracking.md)
