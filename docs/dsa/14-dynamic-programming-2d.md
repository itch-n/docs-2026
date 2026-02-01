# 13. Dynamic Programming (2D)

> Solve problems with two-dimensional state space using tabulation or memoization

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is 2D DP in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **How is 2D DP different from 1D DP?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
    - Example: "2D DP is like filling out a grid where each cell depends on cells above and to the left..."
    - Your analogy: _[Fill in]_

4. **When does this pattern work?**
    - Your answer: _[Fill in after solving problems]_

5. **How do you know when you need 2D instead of 1D?**
    - Your answer: _[Fill in after learning the pattern]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Recursive solution for LCS (no memoization):**
    - Time complexity: _[Your guess: O(?)]_
    - Verified after learning: _[Actual: O(?)]_

2. **2D DP table for LCS:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified: _[Actual]_

3. **Speedup calculation:**
    - If m = 100, n = 100, naive recursion ≈ _____ operations
    - 2D DP table = m × n = _____ operations
    - Speedup factor: _____ times faster

### Scenario Predictions

**Scenario 1:** Longest Common Subsequence of "abc" and "abc"

- **What's the answer?** _[Fill in]_
- **Size of DP table?** _[m+1 × n+1 or m × n?]_
- **Why do we need +1 for dimensions?** _[Fill in - think about base cases]_

**Scenario 2:** Edit distance from "cat" to "dog"

- **Your guess for minimum edits:** _[Fill in]_
- **What operations are allowed?** _[Fill in - insert, delete, replace?]_
- **If characters match, what happens in DP?** _[Fill in]_

**Scenario 3:** Unique paths in 3×3 grid (can only move right or down)

- **Manual count:** _[Try to draw and count all paths]_
- **DP recurrence:** dp[i][j] = _[Fill in formula]_
- **Starting position value:** dp[0][0] = _[0 or 1?]_

### Pattern Recognition Quiz

**Question 1:** Which 2D DP pattern applies?

Match each problem to its pattern:
- [ ] Longest Common Subsequence → _[String/Grid/Knapsack/Interval?]_
- [ ] Unique Paths → _[String/Grid/Knapsack/Interval?]_
- [ ] 0/1 Knapsack → _[String/Grid/Knapsack/Interval?]_
- [ ] Burst Balloons → _[String/Grid/Knapsack/Interval?]_

**Question 2:** When do you need 2D instead of 1D DP?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

### State Design Quiz

**For LCS of strings "abcde" and "ace":**

What does dp[3][2] represent?
- [ ] LCS length of "abc" and "ac"
- [ ] LCS length of first 3 chars of s1 and first 2 chars of s2
- [ ] LCS length including index 3 and 2
- [ ] Something else: _[Fill in]_

Verify after implementation: _[Which one is correct?]_

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand the impact.

### Example 1: Longest Common Subsequence

**Problem:** Find the length of the longest common subsequence of two strings.

#### Approach 1: Brute Force Recursion (No Memoization)

```java
// Naive recursive approach - Try all possibilities
public static int lcs_Recursive(String s1, String s2, int i, int j) {
    // Base case: reached end of either string
    if (i == s1.length() || j == s2.length()) {
        return 0;
    }

    // If characters match, include and move both
    if (s1.charAt(i) == s2.charAt(j)) {
        return 1 + lcs_Recursive(s1, s2, i + 1, j + 1);
    }

    // Characters don't match - try both options
    int skipS1 = lcs_Recursive(s1, s2, i + 1, j);
    int skipS2 = lcs_Recursive(s1, s2, i, j + 1);

    return Math.max(skipS1, skipS2);
}
```

**Analysis:**

- Time: O(2^(m+n)) - Exponential! Each call branches into 2 recursive calls
- Space: O(m+n) - Recursion stack depth
- For m = n = 20: ~1,000,000,000 operations (over 1 billion!)

**Why so slow?** Recalculates the same subproblems repeatedly.

#### Approach 2: 2D DP Table (Bottom-Up)

```java
// Optimized 2D DP - Build table from base cases
public static int lcs_DP(String s1, String s2) {
    int m = s1.length();
    int n = s2.length();
    int[][] dp = new int[m + 1][n + 1];

    // Base case: dp[0][j] = 0, dp[i][0] = 0 (already initialized)

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1] + 1;  // Match: take both
            } else {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);  // No match: try both
            }
        }
    }

    return dp[m][n];
}
```

**Analysis:**

- Time: O(m × n) - Fill each cell once
- Space: O(m × n) - Store entire table
- For m = n = 20: 400 operations (just 400!)

#### Performance Comparison

| String Lengths | Recursive (2^(m+n)) | 2D DP (m×n) | Speedup |
|----------------|---------------------|-------------|---------|
| m=10, n=10     | ~1,000,000         | 100         | 10,000x |
| m=15, n=15     | ~1,000,000,000     | 225         | 4.4M x  |
| m=20, n=20     | ~1,000,000,000,000 | 400         | 2.5B x  |

**Your calculation:** For m = 25, n = 25, the speedup is approximately _____ times faster.

#### Why Does 2D DP Work?

**Key insight to understand:**

For strings "ace" and "abcde":

```
    ""  a  b  c  d  e
""   0  0  0  0  0  0
a    0  1  1  1  1  1    ← 'a' matches: dp[1][1] = dp[0][0] + 1
c    0  1  1  2  2  2    ← 'c' matches: dp[2][3] = dp[1][2] + 1
e    0  1  1  2  2  3    ← 'e' matches: dp[3][5] = dp[2][4] + 1

Answer: dp[3][5] = 3 (LCS = "ace")
```

**Why do we need the extra row/column of zeros?**

- _[Fill in after understanding - what do they represent?]_

**Why can we skip recomputation?**

- Each cell depends only on: _[which cells?]_
- We compute in order: _[top-to-bottom, left-to-right]_
- So dependencies are always ready when needed!

---

### Example 2: 0/1 Knapsack

**Problem:** Given items with weights and values, maximize value without exceeding capacity.

#### Approach 1: Recursive Exploration

```java
// Try all combinations - exponential time
public static int knapsack_Recursive(int[] weights, int[] values,
                                     int capacity, int index) {
    // Base case: no items left or no capacity
    if (index == weights.length || capacity == 0) {
        return 0;
    }

    // Can't take current item - too heavy
    if (weights[index] > capacity) {
        return knapsack_Recursive(weights, values, capacity, index + 1);
    }

    // Try both: take it or skip it
    int take = values[index] +
               knapsack_Recursive(weights, values,
                                 capacity - weights[index], index + 1);
    int skip = knapsack_Recursive(weights, values, capacity, index + 1);

    return Math.max(take, skip);
}
```

**Analysis:**

- Time: O(2^n) - For each item, branch into take/skip
- For n = 30 items: Over 1 billion recursive calls!

#### Approach 2: 2D DP Table

```java
// Build table: dp[item][capacity]
public static int knapsack_DP(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] dp = new int[n + 1][capacity + 1];

    for (int i = 1; i <= n; i++) {
        for (int w = 0; w <= capacity; w++) {
            // Skip current item
            dp[i][w] = dp[i - 1][w];

            // Take current item (if it fits)
            if (weights[i - 1] <= w) {
                int takeValue = values[i - 1] + dp[i - 1][w - weights[i - 1]];
                dp[i][w] = Math.max(dp[i][w], takeValue);
            }
        }
    }

    return dp[n][capacity];
}
```

**Analysis:**

- Time: O(n × capacity)
- For n = 30, capacity = 1000: 30,000 operations vs 1 billion!

#### Visualization: Knapsack Table

Items: weights=[1, 2, 3], values=[10, 5, 15], capacity=5

```
       Capacity: 0  1  2  3  4  5
No items (i=0):  0  0  0  0  0  0
Item 1 (w=1,v=10): 0 10 10 10 10 10  ← Take item 1
Item 2 (w=2,v=5):  0 10 10 15 15 15  ← Take items 1+2 or just 1
Item 3 (w=3,v=15): 0 10 10 15 25 25  ← Take items 1+3

Answer: dp[3][5] = 25 (take items 1 and 3)
```

**After implementing, explain in your own words:**

- _[Why does each cell represent "best value using first i items with capacity w"?]_
- _[Why do we need to check both "take" and "skip" options?]_

---

## Core Implementation

### Pattern 1: Grid Path Problems

**Concept:** Count paths or find optimal path in 2D grid.

**Use case:** Unique paths, minimum path sum, maximal square.

```java
public class GridPathProblems {

    /**
     * Problem: Unique paths in m×n grid (can only move right or down)
     * Time: O(m*n), Space: O(n) optimized
     *
     * TODO: Implement using 2D DP
     */
    public static int uniquePaths(int m, int n) {
        // TODO: dp[i][j] = number of ways to reach cell (i,j)
        // TODO: dp[i][j] = dp[i-1][j] + dp[i][j-1]
        // TODO: Base: dp[0][j] = 1, dp[i][0] = 1
        // TODO: Optimize to 1D: only need previous row

        return 0; // Replace with implementation
    }

    /**
     * Problem: Unique paths with obstacles
     * Time: O(m*n), Space: O(n)
     *
     * TODO: Implement with obstacles
     */
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        // TODO: Similar to uniquePaths
        // TODO: If obstacleGrid[i][j] == 1, dp[i][j] = 0
        // TODO: Handle obstacles in first row/column

        return 0; // Replace with implementation
    }

    /**
     * Problem: Minimum path sum (sum of cell values)
     * Time: O(m*n), Space: O(n)
     *
     * TODO: Implement minimum path sum
     */
    public static int minPathSum(int[][] grid) {
        // TODO: dp[i][j] = minimum sum to reach (i,j)
        // TODO: dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])
        // TODO: Can modify grid in-place to save space

        return 0; // Replace with implementation
    }

    /**
     * Problem: Maximum sum path (can move in all 4 directions)
     * Time: O(m*n), Space: O(m*n)
     *
     * TODO: Implement maximum path sum
     */
    public static int maxPathSum(int[][] grid) {
        // TODO: Use DFS with memoization
        // TODO: Or: DP with careful ordering

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class GridPathProblemsClient {

    public static void main(String[] args) {
        System.out.println("=== Grid Path Problems ===\n");

        // Test 1: Unique paths
        System.out.println("--- Test 1: Unique Paths ---");
        int[][] grids = {{3, 2}, {3, 7}, {7, 3}};
        for (int[] grid : grids) {
            int paths = GridPathProblems.uniquePaths(grid[0], grid[1]);
            System.out.printf("Grid %d×%d: %d paths%n", grid[0], grid[1], paths);
        }

        // Test 2: Unique paths with obstacles
        System.out.println("\n--- Test 2: Unique Paths with Obstacles ---");
        int[][] obstacleGrid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        System.out.println("Grid (0=path, 1=obstacle):");
        printGrid(obstacleGrid);
        int pathsWithObstacles = GridPathProblems.uniquePathsWithObstacles(obstacleGrid);
        System.out.println("Unique paths: " + pathsWithObstacles);

        // Test 3: Minimum path sum
        System.out.println("\n--- Test 3: Minimum Path Sum ---");
        int[][] grid = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        System.out.println("Grid:");
        printGrid(grid);
        int minSum = GridPathProblems.minPathSum(grid);
        System.out.println("Minimum path sum: " + minSum);

        // Test 4: Maximum path sum
        System.out.println("\n--- Test 4: Maximum Path Sum ---");
        int[][] grid2 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        System.out.println("Grid:");
        printGrid(grid2);
        int maxSum = GridPathProblems.maxPathSum(grid2);
        System.out.println("Maximum path sum: " + maxSum);
    }

    private static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
```

---

### Pattern 2: String Matching (LCS, Edit Distance)

**Concept:** Compare two strings character by character.

**Use case:** Longest common subsequence, edit distance, wildcard matching.

```java
public class StringMatching {

    /**
     * Problem: Longest common subsequence
     * Time: O(m*n), Space: O(m*n)
     *
     * TODO: Implement LCS using 2D DP
     */
    public static int longestCommonSubsequence(String text1, String text2) {
        // TODO: dp[i][j] = LCS of text1[0..i] and text2[0..j]
        // TODO: If text1[i] == text2[j]: dp[i][j] = dp[i-1][j-1] + 1
        // TODO: Else: dp[i][j] = max(dp[i-1][j], dp[i][j-1])

        return 0; // Replace with implementation
    }

    /**
     * Problem: Edit distance (insert, delete, replace)
     * Time: O(m*n), Space: O(m*n)
     *
     * TODO: Implement edit distance
     */
    public static int minDistance(String word1, String word2) {
        // TODO: dp[i][j] = min edits to transform word1[0..i] to word2[0..j]
        // TODO: If word1[i] == word2[j]: dp[i][j] = dp[i-1][j-1]
        // TODO: Else: dp[i][j] = 1 + min(insert, delete, replace)
        //   Insert: dp[i][j-1]
        //   Delete: dp[i-1][j]
        //   Replace: dp[i-1][j-1]

        return 0; // Replace with implementation
    }

    /**
     * Problem: Longest palindromic subsequence
     * Time: O(n^2), Space: O(n^2)
     *
     * TODO: Implement LPS using 2D DP
     */
    public static int longestPalindromeSubseq(String s) {
        // TODO: dp[i][j] = LPS length in s[i..j]
        // TODO: If s[i] == s[j]: dp[i][j] = dp[i+1][j-1] + 2
        // TODO: Else: dp[i][j] = max(dp[i+1][j], dp[i][j-1])
        // TODO: Fill diagonal first, then expand

        return 0; // Replace with implementation
    }

    /**
     * Problem: Wildcard matching (* and ?)
     * Time: O(m*n), Space: O(m*n)
     *
     * TODO: Implement wildcard matching
     */
    public static boolean isMatch(String s, String p) {
        // TODO: dp[i][j] = does s[0..i] match p[0..j]?
        // TODO: Handle * (matches any sequence)
        // TODO: Handle ? (matches single char)

        return false; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class StringMatchingClient {

    public static void main(String[] args) {
        System.out.println("=== String Matching ===\n");

        // Test 1: LCS
        System.out.println("--- Test 1: Longest Common Subsequence ---");
        String[][] lcsTests = {
            {"abcde", "ace"},
            {"abc", "abc"},
            {"abc", "def"}
        };

        for (String[] test : lcsTests) {
            int lcs = StringMatching.longestCommonSubsequence(test[0], test[1]);
            System.out.printf("\"%s\" and \"%s\": LCS = %d%n", test[0], test[1], lcs);
        }

        // Test 2: Edit distance
        System.out.println("\n--- Test 2: Edit Distance ---");
        String[][] editTests = {
            {"horse", "ros"},
            {"intention", "execution"},
            {"abc", "abc"}
        };

        for (String[] test : editTests) {
            int dist = StringMatching.minDistance(test[0], test[1]);
            System.out.printf("\"%s\" -> \"%s\": %d edits%n", test[0], test[1], dist);
        }

        // Test 3: Longest palindromic subsequence
        System.out.println("\n--- Test 3: Longest Palindromic Subsequence ---");
        String[] lpsTests = {"bbbab", "cbbd", "racecar"};

        for (String s : lpsTests) {
            int lps = StringMatching.longestPalindromeSubseq(s);
            System.out.printf("\"%s\": LPS length = %d%n", s, lps);
        }

        // Test 4: Wildcard matching
        System.out.println("\n--- Test 4: Wildcard Matching ---");
        String[][] matchTests = {
            {"aa", "a"},
            {"aa", "*"},
            {"cb", "?a"},
            {"adceb", "*a*b"}
        };

        for (String[] test : matchTests) {
            boolean matches = StringMatching.isMatch(test[0], test[1]);
            System.out.printf("s=\"%s\", p=\"%s\": %s%n",
                test[0], test[1], matches ? "MATCH" : "NO MATCH");
        }
    }
}
```

---

### Pattern 3: Knapsack Problems

**Concept:** Select items with constraints to maximize/minimize value.

**Use case:** 0/1 knapsack, unbounded knapsack, target sum.

```java
public class KnapsackProblems {

    /**
     * Problem: 0/1 Knapsack
     * Time: O(n * capacity), Space: O(n * capacity)
     *
     * TODO: Implement 0/1 knapsack
     */
    public static int knapsack(int[] weights, int[] values, int capacity) {
        // TODO: dp[i][w] = max value using first i items with capacity w
        // TODO: dp[i][w] = max(
        //   dp[i-1][w],  // don't take item i
        //   dp[i-1][w-weight[i]] + value[i]  // take item i
        // )
        // TODO: Can optimize to 1D by iterating backwards

        return 0; // Replace with implementation
    }

    /**
     * Problem: Partition into K equal sum subsets
     * Time: O(k * n * sum), Space: O(n * sum)
     *
     * TODO: Implement partition check
     */
    public static boolean canPartitionKSubsets(int[] nums, int k) {
        // TODO: If sum % k != 0, return false
        // TODO: Target = sum / k
        // TODO: Use backtracking or DP to check if k subsets possible

        return false; // Replace with implementation
    }

    /**
     * Problem: Target sum (assign + or - to make target)
     * Time: O(n * sum), Space: O(sum)
     *
     * TODO: Implement target sum
     */
    public static int findTargetSumWays(int[] nums, int target) {
        // TODO: Transform to subset sum problem
        // TODO: sum(P) - sum(N) = target where P=positive, N=negative
        // TODO: sum(P) + sum(N) = sum(all)
        // TODO: Therefore: sum(P) = (target + sum) / 2
        // TODO: Count subsets that sum to (target + sum) / 2

        return 0; // Replace with implementation
    }

    /**
     * Problem: Ones and Zeroes (2D knapsack)
     * Time: O(l * m * n), Space: O(m * n)
     *
     * TODO: Implement 2D knapsack
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        // TODO: dp[i][j] = max strings with i zeros and j ones
        // TODO: For each string, count zeros and ones
        // TODO: Update DP backwards (0/1 knapsack style)

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class KnapsackProblemsClient {

    public static void main(String[] args) {
        System.out.println("=== Knapsack Problems ===\n");

        // Test 1: 0/1 Knapsack
        System.out.println("--- Test 1: 0/1 Knapsack ---");
        int[] weights = {1, 2, 3, 5};
        int[] values = {10, 5, 15, 7};
        int capacity = 7;

        System.out.println("Weights: " + Arrays.toString(weights));
        System.out.println("Values:  " + Arrays.toString(values));
        System.out.println("Capacity: " + capacity);

        int maxValue = KnapsackProblems.knapsack(weights, values, capacity);
        System.out.println("Max value: " + maxValue);

        // Test 2: Partition K subsets
        System.out.println("\n--- Test 2: Partition K Subsets ---");
        int[] nums = {4, 3, 2, 3, 5, 2, 1};
        int k = 4;

        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("k = " + k);
        boolean canPartition = KnapsackProblems.canPartitionKSubsets(nums, k);
        System.out.println("Can partition: " + (canPartition ? "YES" : "NO"));

        // Test 3: Target sum
        System.out.println("\n--- Test 3: Target Sum ---");
        int[] nums2 = {1, 1, 1, 1, 1};
        int target = 3;

        System.out.println("Array: " + Arrays.toString(nums2));
        System.out.println("Target: " + target);
        int ways = KnapsackProblems.findTargetSumWays(nums2, target);
        System.out.println("Ways: " + ways);

        // Test 4: Ones and Zeroes
        System.out.println("\n--- Test 4: Ones and Zeroes ---");
        String[] strs = {"10", "0001", "111001", "1", "0"};
        int m = 5; // max zeros
        int n = 3; // max ones

        System.out.println("Strings: " + Arrays.toString(strs));
        System.out.println("Max 0s: " + m + ", Max 1s: " + n);
        int maxStrings = KnapsackProblems.findMaxForm(strs, m, n);
        System.out.println("Max strings: " + maxStrings);
    }
}
```

---

### Pattern 4: Game Theory / Min-Max

**Concept:** Two players making optimal moves.

**Use case:** Stone game, predict winner, burst balloons.

```java
public class GameTheory {

    /**
     * Problem: Stone game (take from ends, maximize score)
     * Time: O(n^2), Space: O(n^2)
     *
     * TODO: Implement stone game
     */
    public static boolean stoneGame(int[] piles) {
        // TODO: dp[i][j] = max stones first player gets from piles[i..j]
        // TODO: Player chooses max of:
        //   piles[i] + min(dp[i+2][j], dp[i+1][j-1])  // take left
        //   piles[j] + min(dp[i+1][j-1], dp[i][j-2])  // take right
        // TODO: First player wins if dp[0][n-1] > sum/2

        return false; // Replace with implementation
    }

    /**
     * Problem: Predict the winner
     * Time: O(n^2), Space: O(n^2)
     *
     * TODO: Implement predict winner
     */
    public static boolean predictWinner(int[] nums) {
        // TODO: dp[i][j] = max advantage first player has in nums[i..j]
        // TODO: Advantage = player1 score - player2 score
        // TODO: Return dp[0][n-1] >= 0

        return false; // Replace with implementation
    }

    /**
     * Problem: Burst balloons (maximize coins)
     * Time: O(n^3), Space: O(n^2)
     *
     * TODO: Implement burst balloons
     */
    public static int maxCoins(int[] nums) {
        // TODO: Add virtual balloons with value 1 at both ends
        // TODO: dp[i][j] = max coins from bursting balloons (i..j)
        // TODO: Try each balloon k as last to burst in range [i,j]
        // TODO: dp[i][j] = max(dp[i][k] + dp[k][j] + nums[i]*nums[k]*nums[j])

        return 0; // Replace with implementation
    }

    /**
     * Problem: Minimum score triangulation
     * Time: O(n^3), Space: O(n^2)
     *
     * TODO: Implement triangulation
     */
    public static int minScoreTriangulation(int[] values) {
        // TODO: Similar to burst balloons
        // TODO: dp[i][j] = min score triangulating polygon from i to j

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class GameTheoryClient {

    public static void main(String[] args) {
        System.out.println("=== Game Theory ===\n");

        // Test 1: Stone game
        System.out.println("--- Test 1: Stone Game ---");
        int[] piles = {5, 3, 4, 5};
        System.out.println("Piles: " + Arrays.toString(piles));
        boolean firstWins = GameTheory.stoneGame(piles);
        System.out.println("First player wins: " + (firstWins ? "YES" : "NO"));

        // Test 2: Predict winner
        System.out.println("\n--- Test 2: Predict Winner ---");
        int[][] testArrays = {
            {1, 5, 2},
            {1, 5, 233, 7}
        };

        for (int[] arr : testArrays) {
            boolean player1Wins = GameTheory.predictWinner(arr);
            System.out.printf("Array: %s -> Player 1 wins: %s%n",
                Arrays.toString(arr), player1Wins ? "YES" : "NO");
        }

        // Test 3: Burst balloons
        System.out.println("\n--- Test 3: Burst Balloons ---");
        int[] balloons = {3, 1, 5, 8};
        System.out.println("Balloons: " + Arrays.toString(balloons));
        int maxCoins = GameTheory.maxCoins(balloons);
        System.out.println("Max coins: " + maxCoins);

        // Test 4: Triangulation
        System.out.println("\n--- Test 4: Minimum Score Triangulation ---");
        int[] polygon = {1, 2, 3};
        System.out.println("Polygon: " + Arrays.toString(polygon));
        int minScore = GameTheory.minScoreTriangulation(polygon);
        System.out.println("Min score: " + minScore);
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken 2D DP implementations. This tests your understanding of state transitions and edge cases.

### Challenge 1: Broken LCS Implementation

```java
/**
 * Longest Common Subsequence - has 3 BUGS!
 * Find them all.
 */
public static int lcs_Buggy(String s1, String s2) {
    int m = s1.length();
    int n = s2.length();
    int[][] dp = new int[m][n];  // BUG 1: What's wrong with dimensions?

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i) == s2.charAt(j)) {  // BUG 2: Index issue?
                dp[i][j] = dp[i-1][j-1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }
    }

    return dp[m][n];  // BUG 3: Out of bounds?
}
```

**Your debugging:**

- **Bug 1 location:** _[Which line?]_
- **Bug 1 explanation:** _[Why will this fail?]_
- **Bug 1 fix:** _[What should it be?]_

- **Bug 2 location:** _[Which line?]_
- **Bug 2 explanation:** _[What error occurs?]_
- **Bug 2 fix:** _[How to correct the index?]_

- **Bug 3 location:** _[Which line?]_
- **Bug 3 explanation:** _[Why is this wrong?]_
- **Bug 3 fix:** _[What should it be?]_

**Test case to expose bugs:**

- Input: s1 = "abc", s2 = "abc"
- Expected: 3
- Actual with buggy code: _[What happens? Crash or wrong answer?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 4):** Should be `int[][] dp = new int[m + 1][n + 1]`. We need extra row/column for the empty string base case.

**Bug 2 (Line 8):** Should be `s1.charAt(i - 1) == s2.charAt(j - 1)`. The DP indices are 1-based but string indices are 0-based.

**Bug 3 (Line 15):** With Bug 1 unfixed, `dp[m][n]` is out of bounds. After fixing Bug 1, this is correct.

**All three bugs are interconnected!** The root cause is not allocating space for the base case (empty string).
</details>

---

### Challenge 2: Broken Edit Distance

```java
/**
 * Edit Distance - has 2 SUBTLE BUGS
 * One is an off-by-one error, one is a logic error
 */
public static int editDistance_Buggy(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();
    int[][] dp = new int[m + 1][n + 1];

    // Initialize base cases
    for (int i = 0; i <= m; i++) dp[i][0] = i;
    for (int j = 0; j <= n; j++) dp[0][j] = j;

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i) == word2.charAt(j)) {  // BUG 1: Index issue
                dp[i][j] = dp[i-1][j-1];
            } else {
                int insert = dp[i][j-1];
                int delete = dp[i-1][j];
                int replace = dp[i-1][j-1];
                dp[i][j] = Math.min(insert, Math.min(delete, replace));  // BUG 2: Missing something?
            }
        }
    }

    return dp[m][n];
}
```

**Your debugging:**

- **Bug 1:** _[What's wrong with charAt(i)?]_
- **Bug 1 fix:** _[Correct index?]_

- **Bug 2:** _[What's missing in the min calculation?]_
- **Bug 2 fix:** _[Fill in the correct formula]_

**Test case:**

- Input: "horse" → "ros"
- Expected: 3 (delete 'h', delete 'r', replace 'e' with 's')
- Actual: _[Trace through manually - what do you get?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 15):** Should be `word1.charAt(i - 1) == word2.charAt(j - 1)`. DP uses 1-based indexing, strings use 0-based.

**Bug 2 (Line 21):** Each operation (insert, delete, replace) costs 1, so should be:
```java
dp[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
```

Without the `+ 1`, we're not counting the operation cost!
</details>

---

### Challenge 3: Broken Unique Paths

```java
/**
 * Unique Paths in m×n grid
 * Has 2 BUGS: one initialization, one recurrence
 */
public static int uniquePaths_Buggy(int m, int n) {
    int[][] dp = new int[m][n];

    // Initialize first row and column
    for (int i = 0; i < m; i++) dp[i][0] = 0;  // BUG 1: What should this be?
    for (int j = 0; j < n; j++) dp[0][j] = 1;

    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = dp[i-1][j] * dp[i][j-1];  // BUG 2: Wrong operation?
        }
    }

    return dp[m-1][n-1];
}
```

**Your debugging:**

- **Bug 1:** _[Why is dp[i][0] = 0 wrong?]_
- **Bug 1 explanation:** _[What should the first column represent?]_
- **Bug 1 fix:** _[Correct value?]_

- **Bug 2:** _[Why is multiplication wrong?]_
- **Bug 2 explanation:** _[What's the correct recurrence?]_
- **Bug 2 fix:** _[Should be...?]_

**Test case:**

- Input: m = 3, n = 3
- Expected: 6 paths
- Actual with buggy code: _[Calculate it]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 8):** Should be `dp[i][0] = 1`. There's exactly ONE way to reach any cell in the first column (move down only).

**Bug 2 (Line 13):** Should be `dp[i][j] = dp[i-1][j] + dp[i][j-1]` (addition, not multiplication). We're COUNTING paths, not multiplying them.

**Why addition?** The number of ways to reach cell (i,j) is the sum of:
- Ways to reach (i-1,j) [coming from above]
- Ways to reach (i,j-1) [coming from left]
</details>

---

### Challenge 4: Broken Knapsack

```java
/**
 * 0/1 Knapsack - has 3 BUGS
 * Focus on state transition and boundary conditions
 */
public static int knapsack_Buggy(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] dp = new int[n][capacity + 1];  // BUG 1: Array size issue?

    for (int i = 1; i < n; i++) {  // BUG 2: Should start at 1?
        for (int w = 1; w <= capacity; w++) {
            // Don't take item i
            dp[i][w] = dp[i-1][w];

            // Take item i (if it fits)
            if (weights[i] <= w) {  // BUG 3: Correct index?
                int takeValue = values[i] + dp[i-1][w - weights[i]];
                dp[i][w] = Math.max(dp[i][w], takeValue);
            }
        }
    }

    return dp[n-1][capacity];
}
```

**Your debugging:**

- **Bug 1:** _[Why might this cause issues?]_
- **Bug 1 fix:** _[Correct dimensions?]_

- **Bug 2:** _[What gets skipped?]_
- **Bug 2 fix:** _[Should loop start at...?]_

- **Bug 3:** _[Index mismatch between DP and arrays?]_
- **Bug 3 explanation:** _[Fill in]_
- **Bug 3 fix:** _[Correct index?]_

**Test case:**

- weights = [1, 2, 3], values = [10, 5, 15], capacity = 5
- Expected: 25 (items 1 and 3)
- Actual: _[Trace through - what happens?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 3):** Should be `int[][] dp = new int[n + 1][capacity + 1]`. Need extra row for "no items" base case.

**Bug 2 (Line 5):** Should start at `i = 1`, which is actually correct! But with Bug 1 fixed, we need the +1 dimension.

**Bug 3 (Line 11):** Should be `weights[i - 1]` and `values[i - 1]`. The DP table has n+1 rows but arrays have n elements, so there's an offset.

**The complete fix:**
```java
for (int i = 1; i <= n; i++) {  // Note: <= n
    for (int w = 1; w <= capacity; w++) {
        dp[i][w] = dp[i-1][w];
        if (weights[i - 1] <= w) {  // i-1 for array index
            int takeValue = values[i - 1] + dp[i-1][w - weights[i - 1]];
            dp[i][w] = Math.max(dp[i][w], takeValue);
        }
    }
}
return dp[n][capacity];  // Not n-1
```
</details>

---

### Challenge 5: Missing Edge Case Initialization

```java
/**
 * Unique Paths with Obstacles
 * Has 1 CRITICAL EDGE CASE BUG
 */
public static int uniquePathsWithObstacles_Buggy(int[][] obstacleGrid) {
    int m = obstacleGrid.length;
    int n = obstacleGrid[0].length;
    int[][] dp = new int[m][n];

    // Initialize first row
    for (int j = 0; j < n; j++) {
        dp[0][j] = 1;  // BUG: What if there's an obstacle?
    }

    // Initialize first column
    for (int i = 0; i < m; i++) {
        dp[i][0] = 1;  // BUG: Same problem
    }

    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (obstacleGrid[i][j] == 1) {
                dp[i][j] = 0;
            } else {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
    }

    return dp[m-1][n-1];
}
```

**Your debugging:**

- **Bug location:** _[Lines 10 and 15]_
- **Bug explanation:** _[What happens if first row/column has obstacle?]_
- **Why is this critical?** _[Once blocked, all cells after it are unreachable]_
- **Fix:** _[How to handle obstacles in initialization?]_

**Test case to expose:**
```
Grid:
0 0 0
0 1 0  ← Obstacle in first column
0 0 0

Expected: 2 paths
Buggy result: _[Fill in]_
```

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Must check for obstacles during initialization. If there's an obstacle in the first row/column, all cells AFTER it are unreachable (can't be reached).

**Fix:**
```java
// Initialize first row - stop at first obstacle
for (int j = 0; j < n; j++) {
    if (obstacleGrid[0][j] == 1) {
        break;  // All cells after obstacle are unreachable
    }
    dp[0][j] = 1;
}

// Initialize first column - stop at first obstacle
for (int i = 0; i < m; i++) {
    if (obstacleGrid[i][0] == 1) {
        break;  // All cells after obstacle are unreachable
    }
    dp[i][0] = 1;
}

// Also need to check starting cell!
if (obstacleGrid[0][0] == 1) return 0;
```

**Key insight:** In first row/column, once blocked, everything after is blocked (only one direction to reach them).
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 11+ bugs across 5 challenges
- [ ] Understood WHY each bug causes incorrect behavior
- [ ] Could explain the difference between DP indices and array indices
- [ ] Learned importance of base case initialization

**Common 2D DP mistakes you discovered:**

1. **Off-by-one errors:** _[DP table size vs array size]_
2. **Index mismatches:** _[1-based DP vs 0-based strings/arrays]_
3. **Missing base cases:** _[Edge initialization forgotten]_
4. **Wrong recurrence:** _[Addition vs multiplication, missing +1 cost]_
5. **Edge case bugs:** _[Obstacles, empty strings, zero capacity]_

**Your reflection:** Which bug was hardest to find? _[Fill in]_

---

## Decision Framework

**Your task:** Build decision trees for 2D DP problems.

### Question 1: What are your two dimensions?

Answer after solving problems:
- **Two strings?** _[String matching DP]_
- **Grid coordinates?** _[Path DP]_
- **Range [i,j]?** _[Interval DP]_
- **Items and capacity?** _[Knapsack DP]_

### Question 2: What's the recurrence?

**String matching:**

- Match: _[Use both characters]_
- Mismatch: _[Try alternatives]_

**Grid paths:**

- Current cell: _[Function of neighbors]_
- Direction: _[Usually top/left]_

**Interval DP:**

- Try each split point: _[Combine subproblems]_

**Knapsack:**

- Take or skip: _[Compare options]_

### Your Decision Tree

```
2D DP Pattern Selection
│
├─ Two strings?
│   ├─ Common subsequence → LCS ✓
│   ├─ Transform one to other → Edit distance ✓
│   └─ Pattern matching → Wildcard/regex DP ✓
│
├─ Grid problem?
│   ├─ Count paths → Path counting DP ✓
│   ├─ Minimize cost → Min path sum ✓
│   └─ Maximize value → Max path DP ✓
│
├─ Interval/range [i,j]?
│   ├─ Palindrome → Palindrome DP ✓
│   ├─ Burst/merge → Interval DP ✓
│   └─ Game theory → Min-max DP ✓
│
└─ Items with capacity?
    └─ Knapsack variants ✓
```

### The "Kill Switch" - When NOT to use 2D DP

**Don't use when:**

1. _[Can reduce to 1D with tricks]_
2. _[Greedy works - simpler]_
3. _[Space is O(n²) and n is huge]_
4. _[No clear recurrence relation]_

### The Rule of Three: Alternatives

**Option 1: 2D DP**

- Pros: _[Handles complex state]_
- Cons: _[O(n²) space, slower]_
- Use when: _[Two dimensions needed]_

**Option 2: 1D DP**

- Pros: _[Less space, faster]_
- Cons: _[Not always possible]_
- Use when: _[Can optimize space]_

**Option 3: DFS with Memo**

- Pros: _[More intuitive]_
- Cons: _[Stack overhead]_
- Use when: _[Complex recurrence]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 2):**

- [ ] [62. Unique Paths](https://leetcode.com/problems/unique-paths/)
    - Pattern: _[Grid path counting]_
    - Your solution time: ___
    - Key insight: _[Fill in after solving]_

- [ ] [64. Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/)
    - Pattern: _[Grid min path]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

**Medium (Complete 4-5):**

- [ ] [1143. Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/)
    - Pattern: _[String matching]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [72. Edit Distance](https://leetcode.com/problems/edit-distance/)
    - Pattern: _[String transformation]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [63. Unique Paths II](https://leetcode.com/problems/unique-paths-ii/)
    - Pattern: _[Grid with obstacles]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [516. Longest Palindromic Subsequence](https://leetcode.com/problems/longest-palindromic-subsequence/)
    - Pattern: _[Interval DP]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [494. Target Sum](https://leetcode.com/problems/target-sum/)
    - Pattern: _[Knapsack variant]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

**Hard (Optional):**

- [ ] [312. Burst Balloons](https://leetcode.com/problems/burst-balloons/)
    - Pattern: _[Interval DP]_
    - Key insight: _[Fill in after solving]_

- [ ] [44. Wildcard Matching](https://leetcode.com/problems/wildcard-matching/)
    - Pattern: _[String matching]_
    - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] Grid paths: unique, with obstacles, min sum all work
    - [ ] String matching: LCS, edit distance, palindrome all work
    - [ ] Knapsack: 0/1, target sum, ones-zeroes all work
    - [ ] Game theory: stone game, burst balloons work
    - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
    - [ ] Can identify when 2D state is needed
    - [ ] Understand recurrence in each pattern
    - [ ] Know when to optimize space to 1D
    - [ ] Recognize interval DP problems

- [ ] **Problem Solving**
    - [ ] Solved 2 easy problems
    - [ ] Solved 4-5 medium problems
    - [ ] Analyzed time/space complexity
    - [ ] Understood state transitions

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Built decision tree
    - [ ] Identified when NOT to use 2D DP
    - [ ] Can explain how to derive 2D recurrence

- [ ] **Mastery Check**
    - [ ] Could implement all patterns from memory
    - [ ] Could recognize pattern in new problem
    - [ ] Could explain to someone else
    - [ ] Understand space optimization techniques

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about 2D DP and how it's different from 1D DP.

**Your explanation (write it out):**

> "2D Dynamic Programming is..."
>
> _[Fill in your explanation in plain English - 4-5 sentences max]_

**Follow-up: Explain when you need 2D instead of 1D:**

> "You need 2D DP when..."
>
> _[Fill in - what makes a problem require two dimensions?]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by someone who knows 1D DP? _[Yes/No]_
- Did you use concrete examples? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: State Design Exercise

**Task:** Design the DP state for a new problem without implementing it.

**Problem:** Minimum number of coins to make amount N, using coins of denominations in array `coins[]`. Each coin can be used unlimited times.

**Your state design:**

1. **What are the dimensions?**
    - Dimension 1: _[What does it represent?]_
    - Dimension 2: _[What does it represent? Or is this 1D?]_

2. **What does dp[i][j] represent?** (Or dp[i] if 1D)
    - Your answer: _[Fill in]_

3. **What's the recurrence relation?**
    - Your formula: _[Fill in - how to compute dp[i][j] from previous states?]_

4. **What are the base cases?**
    - Base case 1: _[Fill in]_
    - Base case 2: _[Fill in]_

5. **Is this 1D or 2D DP?**
    - Your answer: _[1D or 2D?]_
    - Why: _[Explain your reasoning]_

<details markdown>
<summary>Click to verify your design</summary>

**This is actually 1D DP!** You only need one dimension:
- `dp[amount]` = minimum coins to make that amount
- Recurrence: `dp[i] = min(dp[i - coin] + 1)` for each coin
- Base: `dp[0] = 0`

**Why 1D?** We're building up amounts from 0 to N. We don't need to track which coin we're considering separately—we try all coins for each amount.

**Compare to 0/1 Knapsack (which IS 2D):** In knapsack, each item can only be used ONCE, so we need to track "using first i items" as a separate dimension. Here, coins are unlimited, so 1D suffices.
</details>

---

### Gate 3: Whiteboard LCS Table

**Task:** Fill in the LCS table for strings "ABCD" and "ACBD" without looking at code.

**Draw and complete the table:**

```
       ""  A  C  B  D
    ""  0  0  0  0  0
    A   0  ?  ?  ?  ?
    B   0  ?  ?  ?  ?
    C   0  ?  ?  ?  ?
    D   0  ?  ?  ?  ?
```

**Your completed table:**
```
[Fill in all the ? cells]
```

**Questions to answer:**

1. **What's the final LCS length?** _[Fill in: dp[4][4] = ?]_
2. **Which cells used the "match" rule (both chars equal)?** _[List positions]_
3. **How do you trace back to find the actual LCS string?** _[Describe the process]_
4. **What is the actual LCS?** _[The string itself: "___"]_

**Verification:**

- [ ] All cells filled correctly
- [ ] Identified match positions
- [ ] Correctly traced back the LCS
- [ ] Explained the recurrence for each cell type

<details markdown>
<summary>Click to verify your work</summary>

**Completed table:**
```
       ""  A  C  B  D
    ""  0  0  0  0  0
    A   0  1  1  1  1
    B   0  1  1  2  2
    C   0  1  2  2  2
    D   0  1  2  2  3
```

**LCS length:** 3

**Match positions:** (A,A) at [1,1], (C,C) at [3,2], (B,B) at [2,3], (D,D) at [4,4]

**Actual LCS:** "ACD" or "ABD" (both have length 3)
</details>

---

### Gate 4: Pattern Recognition Test

**Without looking at your notes, classify these problems:**

| Problem | 1D or 2D? | Pattern Type | Why? |
|---------|-----------|--------------|------|
| Longest Increasing Subsequence | _[1D/2D?]_ | _[Grid/String/Knapsack/Interval?]_ | _[Explain]_ |
| Edit Distance | _[1D/2D?]_ | _[Pattern?]_ | _[Explain]_ |
| House Robber | _[1D/2D?]_ | _[Pattern?]_ | _[Explain]_ |
| Unique Paths in Grid | _[1D/2D?]_ | _[Pattern?]_ | _[Explain]_ |
| 0/1 Knapsack | _[1D/2D?]_ | _[Pattern?]_ | _[Explain]_ |
| Coin Change (unlimited) | _[1D/2D?]_ | _[Pattern?]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the patterns and try again.

---

### Gate 5: Complexity Analysis Challenge

**Complete this table from memory:**

| Problem | Dimensions | Time | Space | Can optimize space? |
|---------|-----------|------|-------|---------------------|
| LCS (m, n) | _[Fill]_ | O(?) | O(?) | _[Yes/No - How?]_ |
| Edit Distance | _[Fill]_ | O(?) | O(?) | _[Yes/No - How?]_ |
| Unique Paths (m, n) | _[Fill]_ | O(?) | O(?) | _[Yes/No - How?]_ |
| 0/1 Knapsack (n, capacity) | _[Fill]_ | O(?) | O(?) | _[Yes/No - How?]_ |

**Deep question 1:** Why can many 2D DP problems be optimized to O(n) space?

Your answer: _[Fill in - explain the key insight about dependencies]_

**Deep question 2:** When CAN'T you optimize 2D → 1D space?

Your answer: _[Fill in - what prevents space optimization?]_

---

### Gate 6: Recurrence Design (The Hardest Test)

**Problem:** You're given a triangle (array of arrays). Find the minimum path sum from top to bottom. You can move to adjacent numbers on the row below.

```
Example:
     2
    3 4
    6 5 7
  4 1 8 3
```

**Your task: Design the DP solution**

1. **What are the dimensions?**
    - Your answer: _[1D or 2D? What do they represent?]_

2. **State definition: What does dp[i][j] mean?**
    - Your answer: _[Fill in precisely]_

3. **Recurrence relation:**
    - Your formula: _[How to compute dp[i][j]?]_
    - Why this works: _[Explain the logic]_

4. **Base case:**
    - Your answer: _[What to initialize?]_

5. **Final answer location:**
    - Your answer: _[Where in the DP table?]_

6. **Complexity:**
    - Time: _[Fill in]_
    - Space: _[Fill in]_
    - Can optimize space? _[Yes/No - How?]_

<details markdown>
<summary>Click to verify your design</summary>

**Correct design:**

1. **2D DP**: `dp[row][col]` where row = triangle row, col = position in that row

2. **State:** `dp[i][j]` = minimum path sum from top to position (i,j)

3. **Recurrence:**
    ```
    dp[i][j] = triangle[i][j] + min(dp[i-1][j-1], dp[i-1][j])
    ```
    (But watch bounds: j-1 and j must exist in row i-1)

4. **Base case:** `dp[0][0] = triangle[0][0]` (the top)

5. **Final answer:** `min(dp[lastRow][0], dp[lastRow][1], ..., dp[lastRow][n])`

6. **Complexity:**
    - Time: O(n²) where n = number of rows
    - Space: O(n²) but can optimize to O(n) by only keeping previous row

**Alternative: Bottom-up approach** (even cleaner!)
- Start from bottom row
- Work upward: `dp[i][j] = triangle[i][j] + min(dp[i+1][j], dp[i+1][j+1])`
- Answer at `dp[0][0]`
</details>

---

### Gate 7: Bug Detection Speed Test

**Set a 5-minute timer. Find all bugs in this code:**

```java
/**
 * Find minimum path sum in grid (can only move right or down)
 * This code has 4 BUGS
 */
public static int minPathSum(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;
    int[][] dp = new int[m][n];

    dp[0][0] = 0;  // BUG 1

    for (int i = 1; i < m; i++) {
        dp[i][0] = dp[i-1][0] + grid[i][0];
    }
    for (int j = 1; j < n; j++) {
        dp[0][j] = dp[0][j-1] + grid[j][0];  // BUG 2
    }

    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = grid[i][j] + Math.max(dp[i-1][j], dp[i][j-1]);  // BUG 3
        }
    }

    return dp[m][n];  // BUG 4
}
```

**Your findings:**

1. Bug 1: _[What's wrong?]_ → Fix: _[What should it be?]_
2. Bug 2: _[What's wrong?]_ → Fix: _[What should it be?]_
3. Bug 3: _[What's wrong?]_ → Fix: _[What should it be?]_
4. Bug 4: _[What's wrong?]_ → Fix: _[What should it be?]_

**Time taken:** ___ minutes

<details markdown>
<summary>Click to verify your findings</summary>

**Bug 1 (Line 5):** Should be `dp[0][0] = grid[0][0]`. We need to include the starting cell's value!

**Bug 2 (Line 13):** Should be `dp[0][j] = dp[0][j-1] + grid[0][j]`. We're initializing row 0, not column 0, so second index should be `[0][j]`, not `[j][0]`.

**Bug 3 (Line 18):** Should use `Math.min`, not `Math.max`. We want the MINIMUM path sum.

**Bug 4 (Line 22):** Should be `dp[m-1][n-1]`, not `dp[m][n]` (out of bounds).

**Score:**

- Found all 4 in < 5 min: Expert level
- Found 3-4 in < 5 min: Good understanding
- Found 2 or took > 5 min: Review bug patterns
</details>

---

### Gate 8: Teaching Check

**The ultimate test of understanding is teaching.**

**Task 1:** Explain why LCS needs 2D but Longest Increasing Subsequence (LIS) only needs 1D.

Your explanation:

> _[Fill in - what's fundamentally different between these problems?]_

**Task 2:** Explain to someone the "+1" dimension trick (why dp[m+1][n+1] instead of dp[m][n]).

Your explanation:

> "We add an extra row and column because..."
>
> _[Fill in - what does the extra dimension represent? Why is it helpful?]_

**Task 3:** When would you choose recursion + memoization over bottom-up tabulation for 2D DP?

Your answer:

> _[List 2-3 scenarios where top-down is better]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Design 2D DP state from problem description
- [ ] Write correct recurrence relations
- [ ] Handle base cases and edge initialization properly
- [ ] Identify and fix off-by-one errors
- [ ] Optimize 2D → 1D space when possible
- [ ] Explain why a problem needs 2D vs 1D
- [ ] Trace through a DP table by hand
- [ ] Debug incorrect implementations quickly
- [ ] Teach 2D DP concepts to someone else

**Self-assessment score:** ___/10

**Pattern mastery check - Can you implement from memory?**

- [ ] LCS (Longest Common Subsequence)
- [ ] Edit Distance
- [ ] Unique Paths
- [ ] 0/1 Knapsack

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered 2D Dynamic Programming. Proceed to the next topic.
