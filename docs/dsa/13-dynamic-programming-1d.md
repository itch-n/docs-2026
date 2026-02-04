# 12. Dynamic Programming (1D)

> Turn exponential recursion into polynomial iteration by caching subproblem results

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is dynamic programming in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

2. **How is DP different from regular recursion?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

3. **Real-world analogy:**
    - Example: "DP is like saving your homework answers so you don't have to recalculate the same problems..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **When does this pattern work?**
    - Your answer: <span class="fill-in">[Fill in after solving problems]</span>

5. **What's the difference between top-down and bottom-up?**
    - Your answer: <span class="fill-in">[Fill in after learning both approaches]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

<div class="learner-section" markdown>

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Pure recursive Fibonacci (no memoization):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified after learning: <span class="fill-in">[Actual: O(?)]</span>

2. **Fibonacci with memoization (top-down DP):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Fibonacci with bottom-up DP:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity with array: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity optimized: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

4. **Speedup calculation for Fibonacci(40):**
    - Recursive (no memo) = 2^40 = <span class="fill-in">_____</span> operations
    - With memoization = 40 = <span class="fill-in">_____</span> operations
    - Speedup factor: <span class="fill-in">_____</span> times faster

### Scenario Predictions

**Scenario 1:** Climbing stairs - 1 or 2 steps at a time to reach step 5

- **How many ways without computing?** <span class="fill-in">[Your guess: <span class="fill-in">___</span>_]</span>
- **Can you see the Fibonacci pattern?** <span class="fill-in">[Yes/No - Why?]</span>
- **Recurrence relation:** ways(n) = <span class="fill-in">[Fill in formula]</span>
- **Why does memoization help here?** <span class="fill-in">[Fill in]</span>

**Scenario 2:** Coin change - coins [1, 2, 5], amount = 11

- **Minimum coins needed:** <span class="fill-in">[Your guess: <span class="fill-in">___</span>_]</span>
- **What makes this a DP problem?** <span class="fill-in">[Fill in]</span>
- **What are the overlapping subproblems?** <span class="fill-in">[Fill in]</span>

**Scenario 3:** House robber - houses [2, 7, 9, 3, 1]

- **Maximum money without adjacents:** <span class="fill-in">[Your guess: <span class="fill-in">___</span>_]</span>
- **Which pattern applies?** <span class="fill-in">[Fibonacci-style/Decision/String/Stock]</span>
- **Recurrence relation:** rob(i) = <span class="fill-in">[Fill in formula]</span>

### Trade-off Quiz

**Question:** When would recursive with memoization be BETTER than bottom-up DP?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question:** What's the MAIN requirement for dynamic programming to work?

- [ ] Problem must involve arrays
- [ ] Must have optimal substructure
- [ ] Must have overlapping subproblems
- [ ] Both optimal substructure AND overlapping subproblems
- [ ] Must be solvable recursively

Verify after implementation: <span class="fill-in">[Which one(s)?]</span>

**Question:** Space optimization - when can you reduce O(n) to O(1)?

- Your answer: <span class="fill-in">[Fill in - what's the pattern?]</span>
- Verified: <span class="fill-in">[Fill in after learning Fibonacci-style problems]</span>

</div>

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand exponential to polynomial transformation.

### Example: Fibonacci Number

**Problem:** Calculate the nth Fibonacci number where F(n) = F(n-1) + F(n-2).

#### Approach 1: Pure Recursion (Exponential)

```java
// Naive approach - Recalculates same values many times
public static int fib_Recursive(int n) {
    if (n <= 1) return n;
    return fib_Recursive(n - 1) + fib_Recursive(n - 2);
}
```

**Analysis:**

- Time: O(2^n) - Each call branches into two more calls
- Space: O(n) - Recursion stack depth
- For n = 40: ~2,000,000,000 operations (takes several seconds)
- For n = 50: Would take hours or days

**Why so slow?** Tree of recursive calls:

```
fib(5)
├── fib(4)
│   ├── fib(3)
│   │   ├── fib(2)
│   │   │   ├── fib(1)
│   │   │   └── fib(0)
│   │   └── fib(1)
│   └── fib(2)
│       ├── fib(1)
│       └── fib(0)
└── fib(3)  ← RECALCULATING same subtree!
    ├── fib(2)
    │   ├── fib(1)
    │   └── fib(0)
    └── fib(1)
```

fib(3) is calculated TWICE, fib(2) THREE times, fib(1) FIVE times!

#### Approach 2: Memoization - Top-Down DP (Polynomial)

```java
// Optimized - Cache results to avoid recalculation
public static int fib_Memoization(int n, int[] memo) {
    if (n <= 1) return n;
    if (memo[n] != 0) return memo[n];  // Already calculated!

    memo[n] = fib_Memoization(n - 1, memo) + fib_Memoization(n - 2, memo);
    return memo[n];
}

// Wrapper
public static int fib(int n) {
    return fib_Memoization(n, new int[n + 1]);
}
```

**Analysis:**

- Time: O(n) - Each fib(i) computed exactly once
- Space: O(n) - Memoization array + recursion stack
- For n = 40: ~40 operations (instant)
- For n = 50: ~50 operations (instant)

#### Approach 3: Bottom-Up DP (Best Space Optimization)

```java
// Iterative - Build from bottom, optimize space
public static int fib_BottomUp(int n) {
    if (n <= 1) return n;

    int prev2 = 0;  // F(0)
    int prev1 = 1;  // F(1)

    for (int i = 2; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

**Analysis:**

- Time: O(n) - Single pass
- Space: O(1) - Only 3 variables (optimized from O(n) array)
- For n = 40: ~40 operations, minimal memory
- No recursion stack overhead

#### Performance Comparison

| Approach    | n=10    | n=20    | n=30    | n=40    | Space      |
|-------------|---------|---------|---------|---------|------------|
| Recursive   | 0.001ms | 2ms     | 200ms   | 2000ms  | O(n) stack |
| Memoization | 0.001ms | 0.001ms | 0.001ms | 0.001ms | O(n) array |
| Bottom-Up   | 0.001ms | 0.001ms | 0.001ms | 0.001ms | O(1)       |

**Speedup for n=40:** Memoization is ~2,000,000x faster than pure recursion!

#### Why Does DP Work Here?

**Key properties that enable DP:**

1. **Optimal Substructure:** Solution to F(n) can be built from solutions to F(n-1) and F(n-2)

2. **Overlapping Subproblems:** Same values calculated repeatedly in naive recursion

**Visualization of overlapping subproblems:**

```
Computing fib(6):
fib(5): needs fib(4) + fib(3)
fib(4): needs fib(3) + fib(2)
fib(3): needs fib(2) + fib(1)
              ↑         ↑
      These repeat!   These repeat!
```

**Without DP:** Recalculate everything (exponential waste)
**With DP:** Calculate once, reuse (polynomial efficiency)

---

### Example 2: Coin Change (Decision Problem)

**Problem:** Minimum coins needed to make amount = 11 with coins [1, 2, 5].

#### Approach 1: Brute Force Recursion

```java
// Try all combinations - exponential time
public static int coinChange_Recursive(int[] coins, int amount) {
    if (amount == 0) return 0;
    if (amount < 0) return -1;

    int min = Integer.MAX_VALUE;
    for (int coin : coins) {
        int result = coinChange_Recursive(coins, amount - coin);
        if (result >= 0 && result < min) {
            min = result + 1;
        }
    }

    return min == Integer.MAX_VALUE ? -1 : min;
}
```

**Analysis:**

- Time: O(amount^coins) - Exponential branching
- Space: O(amount) - Recursion depth
- For amount = 100, coins = [1, 2, 5]: billions of operations

#### Approach 2: Bottom-Up DP

```java
// Build table of minimum coins for each amount
public static int coinChange_DP(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);  // Initialize with "infinity"
    dp[0] = 0;  // Base case

    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }

    return dp[amount] > amount ? -1 : dp[amount];
}
```

**Analysis:**

- Time: O(amount × coins) - Polynomial
- Space: O(amount) - DP array
- For amount = 100, coins = [1, 2, 5]: ~300 operations

**Speedup:** From exponential to polynomial!

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- Why does caching subproblem results help? <span class="fill-in">[Your answer]</span>
- What's the difference between top-down and bottom-up? <span class="fill-in">[Your answer]</span>
- When would you choose one approach over the other? <span class="fill-in">[Your answer]</span>

</div>

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

## Debugging Challenges

**Your task:** Find and fix bugs in broken DP implementations. This tests your understanding of recurrence relations,
base cases, and iteration order.

### Challenge 1: Broken Climbing Stairs

```java
/**
 * This code is supposed to count ways to climb n stairs.
 * It has 2 BUGS. Find them!
 */
public static int climbStairs_Buggy(int n) {
    if (n == 1) return 1;
    if (n == 2) return 2;

    int[] dp = new int[n];    dp[0] = 1;
    dp[1] = 2;

    for (int i = 2; i <= n; i++) {        dp[i] = dp[i - 1] + dp[i - 2];
    }

    return dp[n];
}
```

**Your debugging:**

- Bug 1: <span class="fill-in">[What\'s the bug?]</span>

- Bug 2: <span class="fill-in">[What\'s the bug?]</span>

**Test case to expose:**

- Input: `n = 5`
- Expected: `8` ways
- What happens: <span class="fill-in">[Trace through - where does it crash?]</span>

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 6):** Array size should be `n + 1`, not `n`. We need indices from 0 to n inclusive, or adjust indexing.

**Bug 2 (Line 9):** Loop condition `i <= n` will cause ArrayIndexOutOfBoundsException. Either use `i < n` or fix array
size to `n + 1`.

**Correct version:**

```java
public static int climbStairs(int n) {
    if (n == 1) return 1;
    if (n == 2) return 2;

    int[] dp = new int[n + 1];  // Fix 1: n + 1 size
    dp[1] = 1;
    dp[2] = 2;

    for (int i = 3; i <= n; i++) {  // Fix 2: start at 3, or use i < n
        dp[i] = dp[i - 1] + dp[i - 2];
    }

    return dp[n];
}
```

**Alternative fix with O(1) space:**

```java
public static int climbStairs(int n) {
    if (n <= 2) return n;

    int prev2 = 1, prev1 = 2;
    for (int i = 3; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }
    return prev1;
}
```

</details>

---

### Challenge 2: Broken Coin Change

```java
/**
 * Minimum coins to make amount.
 * This has 2 CRITICAL BUGS - wrong recurrence and wrong base case.
 */
public static int coinChange_Buggy(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;  // Base case: 0 coins for amount 0

    for (int i = 0; i < amount; i++) {        for (int coin : coins) {
            if (i + coin <= amount) {
                dp[i + coin] = Math.min(dp[i + coin], dp[i] + 1);            }
        }
    }

    return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
}
```

**Your debugging:**

- **Bug 1:** <span class="fill-in">[Loop should be `i < amount` or `i <= amount`? Why?]</span>
- **Bug 2:** _[Is the recurrence relation correct? What if dp[i] is MAX_VALUE?]_

**Test case:**

- coins = `[2]`, amount = `3`
- Expected: `-1` (impossible)
- With buggy code: <span class="fill-in">[What do you get?]</span>

**Trace through manually:**

- i=0: dp[0]=0, trying coin=2 → dp[2] = min(MAX, 0+1) = 1
- i=1: dp[1]=MAX, trying coin=2 → <span class="fill-in">[What happens here?]</span>

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** Loop is actually okay but can be clearer. Better approach is `for (int i = 1; i <= amount; i++)` and check
`i - coin >= 0`.

**Bug 2:** The logic has a subtle bug: if `dp[i]` is MAX_VALUE, then `dp[i] + 1` overflows to negative! Need to check
before adding.

**Correct version:**

```java
public static int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);  // Use amount + 1 as "infinity" (safer)
    dp[0] = 0;

    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }

    return dp[amount] > amount ? -1 : dp[amount];
}
```

**Key insight:** Use `amount + 1` instead of `Integer.MAX_VALUE` as infinity to avoid overflow. It's impossible to need
more than `amount` coins if you have coin value 1.
</details>

---

### Challenge 3: Broken House Robber

```java
/**
 * Maximum money robbing houses without adjacent.
 * This has 1 SUBTLE BUG in recurrence relation.
 */
public static int rob_Buggy(int[] nums) {
    if (nums.length == 0) return 0;
    if (nums.length == 1) return nums[0];

    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    dp[1] = nums[1];
    for (int i = 2; i < nums.length; i++) {
        dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
    }

    return dp[nums.length - 1];
}
```

**Your debugging:**

- **Bug location:** <span class="fill-in">[Which line has the wrong base case?]</span>
- **Bug explanation:** _[Why is dp[1] = nums[1] wrong?]_
- **Bug fix:** _[What should dp[1] be?]_

**Test case to expose the bug:**

- Input: `[2, 1, 1, 2]`
- Expected: `4` (rob houses 0 and 3)
- Buggy output: <span class="fill-in">[Trace through - what do you get?]</span>

**Manual trace with buggy code:**

- dp[0] = 2
- dp[1] = 1 (BUG: should this compare something?)
- dp[2] = max(dp[1], dp[0] + nums[2]) = max(1, 2+1) = 3
- dp[3] = max(dp[2], dp[1] + nums[3]) = max(3, 1+2) = 3
- Returns 3, but correct answer is 4!

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 7):** `dp[1]` should be `Math.max(nums[0], nums[1])`, not just `nums[1]`.

**Why:** At house 1, you have a choice: rob house 0 OR rob house 1. You should take the maximum of the two, not
automatically rob house 1.

**Correct version:**

```java
public static int rob(int[] nums) {
    if (nums.length == 0) return 0;
    if (nums.length == 1) return nums[0];

    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    dp[1] = Math.max(nums[0], nums[1]);  // Fix: choose better of first two

    for (int i = 2; i < nums.length; i++) {
        dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
    }

    return dp[nums.length - 1];
}
```

**Space-optimized version:**

```java
public static int rob(int[] nums) {
    if (nums.length == 0) return 0;
    if (nums.length == 1) return nums[0];

    int prev2 = nums[0];
    int prev1 = Math.max(nums[0], nums[1]);

    for (int i = 2; i < nums.length; i++) {
        int current = Math.max(prev1, prev2 + nums[i]);
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

</details>

---

### Challenge 4: Broken Word Break

```java
/**
 * Check if string can be segmented into dictionary words.
 * This has 2 BUGS - wrong iteration order and wrong base case check.
 */
public static boolean wordBreak_Buggy(String s, List<String> wordDict) {
    Set<String> dict = new HashSet<>(wordDict);
    boolean[] dp = new boolean[s.length() + 1];
    dp[0] = true;

    for (int i = 1; i <= s.length(); i++) {
        for (int j = i; j >= 0; j--) {            if (dp[j] && dict.contains(s.substring(j, i))) {
                dp[i] = true;
                break;            }
        }
    }

    return dp[s.length()];
}
```

**Your debugging:**

- **Bug 1:** <span class="fill-in">[Is the inner loop direction correct?]</span>
- **Bug 2:** <span class="fill-in">[Is the break statement causing missed solutions?]</span>

**Test case:**

- s = `"leetcode"`, dict = `["leet", "code"]`
- Expected: `true`
- Trace manually: <span class="fill-in">[Does it find the solution?]</span>

**Actually... is this code correct?**

- <span class="fill-in">[Carefully trace through the logic]</span>
- Check: does it explore all possible segmentations? <span class="fill-in">[Your answer]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Surprise:** The code is actually CORRECT! This was a trick question to test your careful analysis.

**Why it works:**

- Inner loop `j` from `i` down to `0` checks all possible last words ending at position `i`
- If `dp[j]` is true (substring 0..j can be segmented) AND substring j..i is in dictionary, then substring 0..i can be
  segmented
- The `break` is an optimization - once we find one valid segmentation, we don't need to find more

**Common misconception:** Students often think the break causes problems, but we only need to know IF segmentation is
possible, not find ALL ways.

**The real learning:** Not all complex-looking DP code has bugs! Sometimes the challenge is understanding WHY it's
correct.

**Alternative version without break (slightly less efficient):**

```java
public static boolean wordBreak(String s, List<String> wordDict) {
    Set<String> dict = new HashSet<>(wordDict);
    boolean[] dp = new boolean[s.length() + 1];
    dp[0] = true;

    for (int i = 1; i <= s.length(); i++) {
        for (int j = 0; j < i; j++) {
            if (dp[j] && dict.contains(s.substring(j, i))) {
                dp[i] = true;
                // No break - checks all j values
            }
        }
    }

    return dp[s.length()];
}
```

Both versions are correct; the first is slightly faster due to early exit.
</details>

---

### Challenge 5: Wrong Iteration Order

```java
/**
 * Coin change II - count ways to make amount.
 * This has 1 CRITICAL BUG that causes wrong answer.
 */
public static int change_Buggy(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;  // One way to make 0: use no coins

    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] += dp[i - coin];
            }
        }
    }

    return dp[amount];
}
```

**Your debugging:**

- **Bug:** <span class="fill-in">[Why does loop order matter here?]</span>
- **What does this code actually count?** <span class="fill-in">[Permutations or combinations?]</span>
- **How to fix:** <span class="fill-in">[Swap loop order? Why?]</span>

**Test case:**

- amount = `4`, coins = `[1, 2]`
- Expected: `3` ways (1+1+1+1, 1+1+2, 2+2)
- Buggy output: <span class="fill-in">[What do you get? Why?]</span>

**Think:** This buggy version counts `[1,1,2]` and `[1,2,1]` and `[2,1,1]` as different. Should they be?

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Loop order causes counting permutations instead of combinations!

**Buggy version counts:** {1,1,2}, {1,2,1}, {2,1,1}, {2,2}, {1,1,1,1} = 5 ways
**Correct should count:** {1,1,2}, {2,2}, {1,1,1,1} = 3 ways

**Fix - swap loop order:**

```java
public static int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;

    // Correct: iterate coins in outer loop
    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            dp[i] += dp[i - coin];
        }
    }

    return dp[amount];
}
```

**Why this works:**

- Outer loop on coins ensures we consider coins in order
- For each coin, we update all amounts that can use it
- This prevents counting different orders of the same coin set

**Key insight:** In combination problems (Coin Change II, Subset Sum), loop over items in outer loop. In minimum/maximum
problems (Coin Change I), loop order doesn't matter.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 7+ bugs across 5 challenges
- [ ] Understood WHY each bug causes incorrect behavior
- [ ] Could explain the difference between combinations and permutations
- [ ] Learned to check: array bounds, base cases, recurrence relations, loop order

**Common DP mistakes you discovered:**

1. **Off-by-one errors:** <span class="fill-in">[Array size, loop bounds]</span>
2. **Wrong base cases:** <span class="fill-in">[Initial dp values affect everything]</span>
3. **Overflow issues:** <span class="fill-in">[Using Integer.MAX_VALUE in arithmetic]</span>
4. **Wrong recurrence relation:** <span class="fill-in">[Not considering all choices correctly]</span>
5. **Wrong iteration order:** <span class="fill-in">[Matters for combination vs permutation counting]</span>

**Your key learnings:**

- What was the most surprising bug? <span class="fill-in">[Your answer]</span>
- Which bug was hardest to spot? <span class="fill-in">[Your answer]</span>
- What will you always check now when writing DP code? <span class="fill-in">[Your answer]</span>

---

## Decision Framework

**Your task:** Build decision trees for 1D DP problems.

### Question 1: What defines a state?

Answer after solving problems:

- **Single index?** <span class="fill-in">[1D DP array]</span>
- **Two indices?** <span class="fill-in">[2D DP - next topic]</span>
- **Additional state (holding/not)?** <span class="fill-in">[Multiple DP arrays or states]</span>
- **Your observation:** <span class="fill-in">[Fill in]</span>

### Question 2: Top-down or bottom-up?

**Top-down (Memoization):**

- Pros: <span class="fill-in">[Natural recursion, only compute needed states]</span>
- Cons: <span class="fill-in">[Stack space, slightly slower]</span>
- Use when: <span class="fill-in">[Complex recurrence, not all states needed]</span>

**Bottom-up (Tabulation):**

- Pros: <span class="fill-in">[No stack, often faster, space optimization]</span>
- Cons: <span class="fill-in">[Must compute all states]</span>
- Use when: <span class="fill-in">[Simple iteration order, need all states]</span>

### Your Decision Tree
```mermaid
flowchart LR
    Start[""]

    Q1{"Depends on previous 1-2 states?"}
    Start --> Q1
    Q2{"Take/skip decision at each element?"}
    Start --> Q2
    Q3{"String problem?"}
    Start --> Q3
    N4(["String DP ✓"])
    Q3 -->|"Build character by character"| N4
    N5["2D DP<br/>(next topic)"]
    Q3 -->|"Subsequence"| N5
    N6["O(1) space"]
    Q3 -->|"Expand from center"| N6
    Q7{"State machine<br/>(multiple states)?"}
    Start --> Q7
```


---

## Practice

### LeetCode Problems

**Easy (Complete all 3):**

- [ ] [70. Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)
    - Pattern: <span class="fill-in">[Fibonacci-style]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [746. Min Cost Climbing Stairs](https://leetcode.com/problems/min-cost-climbing-stairs/)
    - Pattern: <span class="fill-in">[Fibonacci with cost]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [121. Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)
    - Pattern: <span class="fill-in">[Single transaction]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Medium (Complete 4-5):**

- [ ] [198. House Robber](https://leetcode.com/problems/house-robber/)
    - Pattern: <span class="fill-in">[Fibonacci-style decision]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [322. Coin Change](https://leetcode.com/problems/coin-change/)
    - Pattern: <span class="fill-in">[Decision DP]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [139. Word Break](https://leetcode.com/problems/word-break/)
    - Pattern: <span class="fill-in">[String DP]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [300. Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)
    - Pattern: <span class="fill-in">[Sequence DP]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [91. Decode Ways](https://leetcode.com/problems/decode-ways/)
    - Pattern: <span class="fill-in">[String DP]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Hard (Optional):**

- [ ] [152. Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/)
    - Pattern: <span class="fill-in">[Track min and max]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [132. Palindrome Partitioning II](https://leetcode.com/problems/palindrome-partitioning-ii/)
    - Pattern: <span class="fill-in">[String DP with cut optimization]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

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

### Mastery Certification

**I certify that I can:**

- [ ] Identify when a problem needs DP (optimal substructure + overlapping subproblems)
- [ ] Write correct recurrence relations for all 4 pattern types
- [ ] Implement both top-down (memoization) and bottom-up (tabulation) approaches
- [ ] Optimize space complexity when only previous states are needed
- [ ] Debug common DP bugs (base cases, array bounds, recurrence errors, loop order)
- [ ] Analyze time and space complexity accurately
- [ ] Choose between DP approaches based on problem constraints
- [ ] Explain DP concepts clearly to others

**Self-assessment score:** ___/10

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered 1D Dynamic Programming. Proceed to 2D DP.

---

**Final Reflection Questions:**

1. **What was your biggest "aha!" moment with DP?**
    - <span class="fill-in">[Fill in]</span>

2. **Which problem was hardest and why?**
    - <span class="fill-in">[Fill in]</span>

3. **What's the #1 thing you'll remember about DP?**
    - <span class="fill-in">[Fill in]</span>

4. **How would you explain DP in ONE sentence?**
    - <span class="fill-in">[Fill in - this is your elevator pitch]</span>
