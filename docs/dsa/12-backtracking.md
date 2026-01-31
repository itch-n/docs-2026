# 11. Backtracking

> Explore all possible solutions by building candidates and abandoning them when they fail

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is backtracking in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **How is backtracking different from brute force?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "Backtracking is like solving a maze by trying each path and going back when you hit a dead end..."
   - Your analogy: _[Fill in]_

4. **When does this pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **What makes a problem suitable for backtracking?**
   - Your answer: _[Fill in after learning the pattern]_

---

## Core Implementation

### Pattern 1: Permutations

**Concept:** Generate all possible orderings of elements.

**Use case:** Permutations, permutations with duplicates.

```java
import java.util.*;

public class PermutationsPattern {

    /**
     * Problem: Generate all permutations of distinct integers
     * Time: O(n! * n), Space: O(n!)
     *
     * TODO: Implement using backtracking
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // TODO: Call backtrack with empty list

        return result; // Replace with implementation
    }

    private static void backtrack(int[] nums, List<Integer> current,
                                  boolean[] used, List<List<Integer>> result) {
        // TODO: Base case: if current.size() == nums.length
        //   Add copy of current to result
        //   Return

        // TODO: For each index i in nums:
        //   If used[i], skip (already in current path)
        //   Mark used[i] = true
        //   Add nums[i] to current
        //   Recursively backtrack
        //   Remove nums[i] from current (backtrack)
        //   Mark used[i] = false (backtrack)
    }

    /**
     * Problem: Permutations with duplicates
     * Time: O(n! * n), Space: O(n!)
     *
     * TODO: Implement with duplicate handling
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // TODO: Sort array first to handle duplicates
        // TODO: Use backtracking with duplicate checking

        return result; // Replace with implementation
    }

    /**
     * Problem: Next permutation
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement next permutation in-place
     */
    public static void nextPermutation(int[] nums) {
        // TODO: Find rightmost ascending pair (i < j where nums[i] < nums[j])
        // TODO: Find smallest element greater than nums[i] to the right
        // TODO: Swap them
        // TODO: Reverse suffix after i
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class PermutationsPatternClient {

    public static void main(String[] args) {
        System.out.println("=== Permutations ===\n");

        // Test 1: Basic permutations
        System.out.println("--- Test 1: Permutations ---");
        int[] nums1 = {1, 2, 3};
        System.out.println("Input: " + Arrays.toString(nums1));
        List<List<Integer>> perms = PermutationsPattern.permute(nums1);
        System.out.println("Permutations (" + perms.size() + " total):");
        for (List<Integer> perm : perms) {
            System.out.println("  " + perm);
        }

        // Test 2: Permutations with duplicates
        System.out.println("\n--- Test 2: Permutations with Duplicates ---");
        int[] nums2 = {1, 1, 2};
        System.out.println("Input: " + Arrays.toString(nums2));
        List<List<Integer>> uniquePerms = PermutationsPattern.permuteUnique(nums2);
        System.out.println("Unique permutations (" + uniquePerms.size() + " total):");
        for (List<Integer> perm : uniquePerms) {
            System.out.println("  " + perm);
        }

        // Test 3: Next permutation
        System.out.println("\n--- Test 3: Next Permutation ---");
        int[] nums3 = {1, 2, 3};
        System.out.println("Start: " + Arrays.toString(nums3));
        for (int i = 0; i < 5; i++) {
            PermutationsPattern.nextPermutation(nums3);
            System.out.println("Next:  " + Arrays.toString(nums3));
        }
    }
}
```

---

### Pattern 2: Combinations and Subsets

**Concept:** Generate all possible selections of elements.

**Use case:** Combinations, subsets, subset sum.

```java
import java.util.*;

public class CombinationsPattern {

    /**
     * Problem: Generate all subsets (power set)
     * Time: O(2^n * n), Space: O(2^n)
     *
     * TODO: Implement using backtracking
     */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // TODO: Start with empty subset
        // TODO: Backtrack to generate all subsets

        return result; // Replace with implementation
    }

    private static void backtrackSubsets(int[] nums, int start,
                                        List<Integer> current,
                                        List<List<Integer>> result) {
        // TODO: Add current subset to result (valid at every step)

        // TODO: For i from start to nums.length:
        //   Add nums[i] to current
        //   Recursively backtrack from i+1
        //   Remove nums[i] from current (backtrack)
    }

    /**
     * Problem: Generate combinations of k elements
     * Time: O(C(n,k) * k), Space: O(C(n,k))
     *
     * TODO: Implement combinations
     */
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        // TODO: Backtrack with size constraint

        return result; // Replace with implementation
    }

    /**
     * Problem: Combination sum (elements can be reused)
     * Time: O(2^n), Space: O(n)
     *
     * TODO: Implement combination sum
     */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        // TODO: Backtrack with sum tracking
        // TODO: Can reuse same element

        return result; // Replace with implementation
    }

    /**
     * Problem: Subsets with duplicates
     * Time: O(2^n * n), Space: O(2^n)
     *
     * TODO: Implement with duplicate handling
     */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // TODO: Sort first
        // TODO: Skip duplicate elements in same level

        return result; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class CombinationsPatternClient {

    public static void main(String[] args) {
        System.out.println("=== Combinations and Subsets ===\n");

        // Test 1: Subsets
        System.out.println("--- Test 1: Subsets ---");
        int[] nums1 = {1, 2, 3};
        System.out.println("Input: " + Arrays.toString(nums1));
        List<List<Integer>> subsets = CombinationsPattern.subsets(nums1);
        System.out.println("Subsets (" + subsets.size() + " total):");
        for (List<Integer> subset : subsets) {
            System.out.println("  " + subset);
        }

        // Test 2: Combinations
        System.out.println("\n--- Test 2: Combinations ---");
        int n = 4, k = 2;
        System.out.println("n = " + n + ", k = " + k);
        List<List<Integer>> combinations = CombinationsPattern.combine(n, k);
        System.out.println("Combinations (" + combinations.size() + " total):");
        for (List<Integer> comb : combinations) {
            System.out.println("  " + comb);
        }

        // Test 3: Combination sum
        System.out.println("\n--- Test 3: Combination Sum ---");
        int[] candidates = {2, 3, 6, 7};
        int target = 7;
        System.out.println("Candidates: " + Arrays.toString(candidates));
        System.out.println("Target: " + target);
        List<List<Integer>> combSums = CombinationsPattern.combinationSum(candidates, target);
        System.out.println("Combinations:");
        for (List<Integer> comb : combSums) {
            System.out.println("  " + comb);
        }

        // Test 4: Subsets with duplicates
        System.out.println("\n--- Test 4: Subsets with Duplicates ---");
        int[] nums2 = {1, 2, 2};
        System.out.println("Input: " + Arrays.toString(nums2));
        List<List<Integer>> uniqueSubsets = CombinationsPattern.subsetsWithDup(nums2);
        System.out.println("Unique subsets (" + uniqueSubsets.size() + " total):");
        for (List<Integer> subset : uniqueSubsets) {
            System.out.println("  " + subset);
        }
    }
}
```

---

### Pattern 3: N-Queens and Constraint Satisfaction

**Concept:** Place elements with constraints, backtrack on violations.

**Use case:** N-Queens, Sudoku solver.

```java
import java.util.*;

public class ConstraintSatisfaction {

    /**
     * Problem: N-Queens - place N queens on N×N board
     * Time: O(N!), Space: O(N^2)
     *
     * TODO: Implement N-Queens using backtracking
     */
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        // TODO: Initialize board
        // TODO: Track columns, diagonals under attack
        // TODO: Backtrack row by row

        return result; // Replace with implementation
    }

    private static void backtrackQueens(int row, int n, char[][] board,
                                       Set<Integer> cols, Set<Integer> diag1,
                                       Set<Integer> diag2, List<List<String>> result) {
        // TODO: Base case: if row == n, found solution
        //   Convert board to list of strings
        //   Add to result
        //   Return

        // TODO: For each column in current row:
        //   Check if column, diag1, diag2 are safe
        //   If safe:
        //     Place queen
        //     Add to cols, diag1 (row-col), diag2 (row+col)
        //     Recursively solve next row
        //     Remove queen (backtrack)
        //     Remove from cols, diag1, diag2 (backtrack)
    }

    /**
     * Problem: Sudoku solver
     * Time: O(9^m) where m = empty cells, Space: O(1)
     *
     * TODO: Implement Sudoku solver
     */
    public static void solveSudoku(char[][] board) {
        // TODO: Find empty cell
        // TODO: Try digits 1-9
        // TODO: Check row, column, 3×3 box constraints
        // TODO: Backtrack if no valid digit
    }

    private static boolean isValidSudoku(char[][] board, int row, int col, char c) {
        // TODO: Check row constraint
        // TODO: Check column constraint
        // TODO: Check 3×3 box constraint
        return false; // Replace with implementation
    }

    /**
     * Problem: Count total N-Queens solutions
     * Time: O(N!), Space: O(N)
     *
     * TODO: Implement optimized N-Queens counter
     */
    public static int totalNQueens(int n) {
        // TODO: Similar to solveNQueens but just count

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class ConstraintSatisfactionClient {

    public static void main(String[] args) {
        System.out.println("=== Constraint Satisfaction ===\n");

        // Test 1: N-Queens
        System.out.println("--- Test 1: N-Queens (n=4) ---");
        List<List<String>> solutions = ConstraintSatisfaction.solveNQueens(4);
        System.out.println("Found " + solutions.size() + " solutions:");
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println("Solution " + (i + 1) + ":");
            for (String row : solutions.get(i)) {
                System.out.println("  " + row);
            }
            System.out.println();
        }

        // Test 2: Count N-Queens
        System.out.println("--- Test 2: Count N-Queens Solutions ---");
        for (int n = 1; n <= 8; n++) {
            int count = ConstraintSatisfaction.totalNQueens(n);
            System.out.printf("n=%d: %d solutions%n", n, count);
        }

        // Test 3: Sudoku solver
        System.out.println("\n--- Test 3: Sudoku Solver ---");
        char[][] sudoku = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };

        System.out.println("Before:");
        printSudoku(sudoku);

        ConstraintSatisfaction.solveSudoku(sudoku);

        System.out.println("\nAfter:");
        printSudoku(sudoku);
    }

    private static void printSudoku(char[][] board) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
```

---

### Pattern 4: Grid Search (Word Search)

**Concept:** Explore grid paths with backtracking.

**Use case:** Word search, path finding with constraints.

```java
public class GridSearch {

    /**
     * Problem: Word search in 2D grid
     * Time: O(m * n * 4^L) where L = word length, Space: O(L)
     *
     * TODO: Implement word search using backtracking
     */
    public static boolean exist(char[][] board, String word) {
        // TODO: Try starting from each cell
        // TODO: Use DFS with backtracking

        return false; // Replace with implementation
    }

    private static boolean dfs(char[][] board, String word, int index,
                              int row, int col, boolean[][] visited) {
        // TODO: Base case: if index == word.length(), found word

        // TODO: Check bounds and visited
        // TODO: Check if board[row][col] == word.charAt(index)

        // TODO: Mark visited
        // TODO: Explore 4 directions (up, down, left, right)
        // TODO: If any direction succeeds, return true
        // TODO: Unmark visited (backtrack)

        return false; // Replace with implementation
    }

    /**
     * Problem: Count paths from top-left to bottom-right
     * Time: O(2^(m+n)), Space: O(m+n)
     *
     * TODO: Implement path counter with obstacles
     */
    public static int countPaths(int[][] grid) {
        // TODO: Backtrack with path counting
        // TODO: Handle obstacles (grid[i][j] == 1)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Longest increasing path in matrix
     * Time: O(m * n), Space: O(m * n) with memoization
     *
     * TODO: Implement using DFS with memoization
     */
    public static int longestIncreasingPath(int[][] matrix) {
        // TODO: DFS from each cell
        // TODO: Use memo to cache results
        // TODO: Can only move to strictly greater neighbors

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class GridSearchClient {

    public static void main(String[] args) {
        System.out.println("=== Grid Search ===\n");

        // Test 1: Word search
        System.out.println("--- Test 1: Word Search ---");
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };

        String[] words = {"ABCCED", "SEE", "ABCB"};

        System.out.println("Board:");
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println();

        for (String word : words) {
            boolean found = GridSearch.exist(board, word);
            System.out.printf("Word \"%s\": %s%n", word, found ? "FOUND" : "NOT FOUND");
        }

        // Test 2: Count paths
        System.out.println("\n--- Test 2: Count Paths ---");
        int[][] grid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };

        System.out.println("Grid (0=path, 1=obstacle):");
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        int paths = GridSearch.countPaths(grid);
        System.out.println("Total paths: " + paths);

        // Test 3: Longest increasing path
        System.out.println("\n--- Test 3: Longest Increasing Path ---");
        int[][] matrix = {
            {9, 9, 4},
            {6, 6, 8},
            {2, 1, 1}
        };

        System.out.println("Matrix:");
        for (int[] row : matrix) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        int longest = GridSearch.longestIncreasingPath(matrix);
        System.out.println("Longest increasing path: " + longest);
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for backtracking problems.

### Question 1: What are you generating?

Answer after solving problems:
- **All permutations?** _[Use permutation backtracking]_
- **All combinations/subsets?** _[Use combination backtracking]_
- **Single valid solution?** _[Return early when found]_
- **Count solutions?** _[Track count, don't store paths]_

### Question 2: What are the constraints?

**No duplicates in input:**
- Approach: _[Simple backtracking]_

**Duplicates in input:**
- Approach: _[Sort first, skip duplicates at same level]_

**Size constraint (k elements):**
- Approach: _[Add base case for size]_

**Sum/product constraint:**
- Approach: _[Track running sum/product, prune early]_

**Grid constraints:**
- Approach: _[Mark visited, unmark on backtrack]_

### Your Decision Tree

```
Backtracking Pattern Selection
│
├─ Generating sequences?
│   ├─ All orderings → Permutations ✓
│   ├─ All selections → Combinations/Subsets ✓
│   └─ With size K → Combinations with constraint ✓
│
├─ Constraint satisfaction?
│   ├─ Board placement → N-Queens pattern ✓
│   ├─ Sudoku-like → Try digits with validation ✓
│   └─ Grid search → DFS with visited tracking ✓
│
└─ Optimization?
    ├─ Prune invalid branches early ✓
    ├─ Use memoization if overlapping ✓
    └─ Sort input if helpful for pruning ✓
```

### The "Kill Switch" - When NOT to use Backtracking

**Don't use backtracking when:**
1. _[Solution space too large without pruning]_
2. _[Can solve with DP or greedy]_
3. _[Only need one solution and can find it directly]_
4. _[No way to validate partial solutions]_

### The Rule of Three: Alternatives

**Option 1: Backtracking**
- Pros: _[Explores all solutions, can prune]_
- Cons: _[Exponential time complexity]_
- Use when: _[Need all solutions or constraint satisfaction]_

**Option 2: Dynamic Programming**
- Pros: _[Polynomial time if applicable]_
- Cons: _[Need optimal substructure]_
- Use when: _[Counting or optimization, not enumeration]_

**Option 3: Greedy**
- Pros: _[Fast, simple]_
- Cons: _[Doesn't work for all problems]_
- Use when: _[Greedy choice property holds]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 2):**
- [ ] [257. Binary Tree Paths](https://leetcode.com/problems/binary-tree-paths/)
  - Pattern: _[Backtracking with path tracking]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [401. Binary Watch](https://leetcode.com/problems/binary-watch/)
  - Pattern: _[Generate all combinations]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 4-5):**
- [ ] [46. Permutations](https://leetcode.com/problems/permutations/)
  - Pattern: _[Classic permutations]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [78. Subsets](https://leetcode.com/problems/subsets/)
  - Pattern: _[Classic subsets]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [39. Combination Sum](https://leetcode.com/problems/combination-sum/)
  - Pattern: _[Combinations with reuse]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [79. Word Search](https://leetcode.com/problems/word-search/)
  - Pattern: _[Grid DFS with backtracking]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [22. Generate Parentheses](https://leetcode.com/problems/generate-parentheses/)
  - Pattern: _[Generate valid sequences]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

**Hard (Optional):**
- [ ] [51. N-Queens](https://leetcode.com/problems/n-queens/)
  - Pattern: _[Constraint satisfaction]_
  - Key insight: _[Fill in after solving]_

- [ ] [37. Sudoku Solver](https://leetcode.com/problems/sudoku-solver/)
  - Pattern: _[Grid constraint satisfaction]_
  - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Permutations: basic and with duplicates work
  - [ ] Combinations: subsets, combinations, combination sum work
  - [ ] N-Queens: placement and counting work
  - [ ] Word search: grid DFS with backtracking works
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify permutation vs combination problems
  - [ ] Understand when to use visited array vs set
  - [ ] Know when to sort input for duplicates
  - [ ] Recognize constraint satisfaction problems

- [ ] **Problem Solving**
  - [ ] Solved 2 easy problems
  - [ ] Solved 4-5 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Understood pruning strategies

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use backtracking
  - [ ] Can explain backtrack step clearly

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand when to prune branches

---

**Next Topic:** [13. Dynamic Programming (1D) →](13-dynamic-programming-1d.md)

**Back to:** [11. Heaps ←](11-heaps.md)
