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

**Next Topic:** [15. Tries →](15-tries.md)

**Back to:** [13. Dynamic Programming (1D) ←](13-dynamic-programming-1d.md)
