# 08. Binary Search

> Reduce O(n) to O(log n) by eliminating half the search space each step

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is binary search in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why is O(log n) so fast?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "Binary search is like finding a word in a dictionary by opening to the middle..."
   - Your analogy: _[Fill in]_

4. **When does binary search work?**
   - Your answer: _[Fill in after solving problems]_

5. **What breaks binary search?**
   - Your answer: _[Fill in after testing]_

---

## Core Implementation

### Pattern 1: Classic Binary Search

**Concept:** Search in sorted array by halving search space.

**Use case:** Find target, find insert position, find boundaries.

```java
public class ClassicBinarySearch {

    /**
     * Problem: Find target in sorted array
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement classic binary search
     */
    public static int binarySearch(int[] nums, int target) {
        // TODO: left = 0, right = nums.length - 1

        // TODO: While left <= right:
        //   mid = left + (right - left) / 2
        //   If nums[mid] == target, return mid
        //   If nums[mid] < target, left = mid + 1
        //   If nums[mid] > target, right = mid - 1

        // TODO: Return -1 if not found

        return -1; // Replace with implementation
    }

    /**
     * Problem: Find insert position for target
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement search insert position
     */
    public static int searchInsert(int[] nums, int target) {
        // TODO: Similar to binary search
        // TODO: Return left pointer when not found

        return 0; // Replace with implementation
    }

    /**
     * Problem: Find first and last position of target
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement find range
     */
    public static int[] searchRange(int[] nums, int target) {
        // TODO: Find leftmost occurrence
        // TODO: Find rightmost occurrence
        // TODO: Return [-1, -1] if not found

        return new int[]{-1, -1}; // Replace with implementation
    }

    private static int findFirst(int[] nums, int target) {
        // TODO: Binary search to find first occurrence
        // TODO: When found, continue searching left half

        return -1; // Replace with implementation
    }

    private static int findLast(int[] nums, int target) {
        // TODO: Binary search to find last occurrence
        // TODO: When found, continue searching right half

        return -1; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class ClassicBinarySearchClient {

    public static void main(String[] args) {
        System.out.println("=== Classic Binary Search ===\n");

        // Test 1: Find target
        System.out.println("--- Test 1: Find Target ---");
        int[] arr = {1, 3, 5, 7, 9, 11, 13};
        int[] targets = {5, 8, 1, 13};

        System.out.println("Array: " + Arrays.toString(arr));
        for (int target : targets) {
            int index = ClassicBinarySearch.binarySearch(arr, target);
            System.out.printf("Search %d: index = %d%n", target, index);
        }

        // Test 2: Search insert position
        System.out.println("\n--- Test 2: Search Insert Position ---");
        int[] arr2 = {1, 3, 5, 6};
        int[] insertTargets = {5, 2, 7, 0};

        System.out.println("Array: " + Arrays.toString(arr2));
        for (int target : insertTargets) {
            int pos = ClassicBinarySearch.searchInsert(arr2, target);
            System.out.printf("Insert position for %d: %d%n", target, pos);
        }

        // Test 3: Find range
        System.out.println("\n--- Test 3: Find First and Last Position ---");
        int[] arr3 = {5, 7, 7, 8, 8, 8, 10};
        int rangeTarget = 8;

        System.out.println("Array: " + Arrays.toString(arr3));
        int[] range = ClassicBinarySearch.searchRange(arr3, rangeTarget);
        System.out.printf("Range for %d: [%d, %d]%n", rangeTarget, range[0], range[1]);
    }
}
```

---

### Pattern 2: Rotated Array Search

**Concept:** Search in rotated sorted array.

**Use case:** Find target in rotated array, find minimum, find rotation point.

```java
public class RotatedArraySearch {

    /**
     * Problem: Search in rotated sorted array
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement rotated array search
     */
    public static int search(int[] nums, int target) {
        // TODO: left = 0, right = nums.length - 1

        // TODO: While left <= right:
        //   mid = left + (right - left) / 2
        //   If nums[mid] == target, return mid
        //
        //   Determine which half is sorted:
        //   If nums[left] <= nums[mid]: left half sorted
        //     Check if target in left half
        //   Else: right half sorted
        //     Check if target in right half

        return -1; // Replace with implementation
    }

    /**
     * Problem: Find minimum in rotated sorted array
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement find minimum
     */
    public static int findMin(int[] nums) {
        // TODO: left = 0, right = nums.length - 1

        // TODO: While left < right:
        //   mid = left + (right - left) / 2
        //   If nums[mid] > nums[right]:
        //     Minimum is in right half: left = mid + 1
        //   Else:
        //     Minimum is in left half or mid: right = mid

        // TODO: Return nums[left]

        return 0; // Replace with implementation
    }

    /**
     * Problem: Find rotation count (how many rotations)
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement rotation count
     */
    public static int findRotationCount(int[] nums) {
        // TODO: Find index of minimum element
        // TODO: That index is the rotation count

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class RotatedArraySearchClient {

    public static void main(String[] args) {
        System.out.println("=== Rotated Array Search ===\n");

        // Test 1: Search in rotated array
        System.out.println("--- Test 1: Search in Rotated Array ---");
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        int[] searchTargets = {0, 3, 4, 7};

        System.out.println("Rotated array: " + Arrays.toString(rotated));
        for (int target : searchTargets) {
            int index = RotatedArraySearch.search(rotated, target);
            System.out.printf("Search %d: index = %d%n", target, index);
        }

        // Test 2: Find minimum
        System.out.println("\n--- Test 2: Find Minimum ---");
        int[][] rotatedArrays = {
            {3, 4, 5, 1, 2},
            {4, 5, 6, 7, 0, 1, 2},
            {11, 13, 15, 17}
        };

        for (int[] arr : rotatedArrays) {
            int min = RotatedArraySearch.findMin(arr);
            System.out.printf("Array: %s -> Min: %d%n", Arrays.toString(arr), min);
        }

        // Test 3: Find rotation count
        System.out.println("\n--- Test 3: Find Rotation Count ---");
        for (int[] arr : rotatedArrays) {
            int count = RotatedArraySearch.findRotationCount(arr);
            System.out.printf("Array: %s -> Rotations: %d%n", Arrays.toString(arr), count);
        }
    }
}
```

---

### Pattern 3: Search in 2D Matrix

**Concept:** Binary search in matrix with sorted properties.

**Use case:** Search in row-sorted matrix, search in fully sorted matrix.

```java
public class Search2DMatrix {

    /**
     * Problem: Search in matrix where each row is sorted
     * Time: O(m log n), Space: O(1)
     *
     * TODO: Implement 2D matrix search (rows sorted)
     */
    public static boolean searchMatrix1(int[][] matrix, int target) {
        // TODO: Binary search on each row
        // TODO: Or: binary search to find row, then binary search in row

        return false; // Replace with implementation
    }

    /**
     * Problem: Search in matrix sorted like a flat array
     * Time: O(log(m*n)), Space: O(1)
     *
     * TODO: Implement 2D matrix search (fully sorted)
     */
    public static boolean searchMatrix2(int[][] matrix, int target) {
        // TODO: Treat matrix as 1D array
        // TODO: left = 0, right = rows * cols - 1
        // TODO: Convert mid to [row, col]: row = mid / cols, col = mid % cols

        return false; // Replace with implementation
    }

    /**
     * Problem: Search in matrix sorted row-wise and column-wise
     * Time: O(m + n), Space: O(1)
     *
     * TODO: Implement staircase search
     */
    public static boolean searchMatrixStaircase(int[][] matrix, int target) {
        // TODO: Start from top-right corner
        // TODO: If target < current, move left
        // TODO: If target > current, move down
        // TODO: If target == current, found

        return false; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class Search2DMatrixClient {

    public static void main(String[] args) {
        System.out.println("=== Search in 2D Matrix ===\n");

        // Test 1: Search in row-sorted matrix
        System.out.println("--- Test 1: Row-Sorted Matrix ---");
        int[][] matrix1 = {
            {1, 3, 5, 7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };

        int[] targets1 = {3, 13, 60};
        for (int target : targets1) {
            boolean found = Search2DMatrix.searchMatrix1(matrix1, target);
            System.out.printf("Search %d: %s%n", target, found ? "FOUND" : "NOT FOUND");
        }

        // Test 2: Search in fully sorted matrix
        System.out.println("\n--- Test 2: Fully Sorted Matrix ---");
        int[][] matrix2 = {
            {1, 3, 5, 7},
            {10, 11, 16, 20},
            {23, 30, 34, 50}
        };

        int[] targets2 = {3, 13, 50};
        for (int target : targets2) {
            boolean found = Search2DMatrix.searchMatrix2(matrix2, target);
            System.out.printf("Search %d: %s%n", target, found ? "FOUND" : "NOT FOUND");
        }

        // Test 3: Staircase search
        System.out.println("\n--- Test 3: Staircase Search ---");
        int[][] matrix3 = {
            {1, 4, 7, 11},
            {2, 5, 8, 12},
            {3, 6, 9, 16},
            {10, 13, 14, 17}
        };

        int[] targets3 = {5, 20, 14};
        for (int target : targets3) {
            boolean found = Search2DMatrix.searchMatrixStaircase(matrix3, target);
            System.out.printf("Search %d: %s%n", target, found ? "FOUND" : "NOT FOUND");
        }
    }
}
```

---

### Pattern 4: Binary Search on Answer

**Concept:** Binary search on solution space, not array index.

**Use case:** Find square root, find kth smallest, capacity problems.

```java
public class BinarySearchOnAnswer {

    /**
     * Problem: Find square root (integer part)
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement integer square root
     */
    public static int mySqrt(int x) {
        // TODO: Binary search from 0 to x
        // TODO: Check if mid * mid <= x
        // TODO: Return largest mid where mid * mid <= x

        return 0; // Replace with implementation
    }

    /**
     * Problem: Find minimum capacity to ship packages in D days
     * Time: O(n log(sum)), Space: O(1)
     *
     * TODO: Implement capacity to ship
     */
    public static int shipWithinDays(int[] weights, int days) {
        // TODO: Binary search on capacity
        // TODO: left = max(weights), right = sum(weights)
        // TODO: Check if capacity allows shipping in D days

        return 0; // Replace with implementation
    }

    private static boolean canShip(int[] weights, int days, int capacity) {
        // TODO: Simulate shipping with given capacity
        // TODO: Return true if possible in D days

        return false; // Replace with implementation
    }

    /**
     * Problem: Find kth missing positive number
     * Time: O(log n), Space: O(1)
     *
     * TODO: Implement kth missing
     */
    public static int findKthPositive(int[] arr, int k) {
        // TODO: Binary search on array
        // TODO: Count missing numbers at each position

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class BinarySearchOnAnswerClient {

    public static void main(String[] args) {
        System.out.println("=== Binary Search on Answer ===\n");

        // Test 1: Square root
        System.out.println("--- Test 1: Integer Square Root ---");
        int[] sqrtInputs = {4, 8, 16, 27};

        for (int x : sqrtInputs) {
            int sqrt = BinarySearchOnAnswer.mySqrt(x);
            System.out.printf("sqrt(%d) = %d%n", x, sqrt);
        }

        // Test 2: Ship within days
        System.out.println("\n--- Test 2: Ship Within Days ---");
        int[] weights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int days = 5;

        System.out.println("Weights: " + Arrays.toString(weights));
        System.out.println("Days: " + days);
        int capacity = BinarySearchOnAnswer.shipWithinDays(weights, days);
        System.out.println("Minimum capacity: " + capacity);

        // Test 3: Kth missing positive
        System.out.println("\n--- Test 3: Kth Missing Positive ---");
        int[] arr = {2, 3, 4, 7, 11};
        int k = 5;

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("k = " + k);
        int kthMissing = BinarySearchOnAnswer.findKthPositive(arr, k);
        System.out.println("Kth missing: " + kthMissing);
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for binary search problems.

### Question 1: Is the data sorted?

Answer after solving problems:
- **Already sorted?** _[Classic binary search]_
- **Rotated?** _[Find sorted half, adjust search]_
- **Partially sorted?** _[Modified binary search]_
- **Not sorted?** _[Can't use binary search]_

### Question 2: What are you searching for?

**Search for value:**
- Direct search: _[Classic binary search]_
- First/last occurrence: _[Modified binary search]_

**Search for position:**
- Insert position: _[Binary search with left pointer]_
- Peak element: _[Binary search on local property]_

**Search on answer space:**
- Square root, capacity: _[Binary search on range]_
- Minimize/maximize: _[Binary search with validation]_

### Your Decision Tree

```
Binary Search Pattern Selection
│
├─ Standard sorted array?
│   └─ Use: Classic binary search ✓
│
├─ Rotated sorted array?
│   └─ Use: Modified binary search ✓
│
├─ 2D matrix?
│   ├─ Fully sorted → Treat as 1D ✓
│   ├─ Row-sorted → Search each row ✓
│   └─ Row & col sorted → Staircase search ✓
│
└─ Not searching in array?
    └─ Use: Binary search on answer ✓
```

### The "Kill Switch" - When NOT to use Binary Search

**Don't use when:**
1. _[Data is unsorted and can't be sorted]_
2. _[Need all occurrences, not just one]_
3. _[Small dataset where linear search is simpler]_
4. _[No monotonic property to exploit]_

### The Rule of Three: Alternatives

**Option 1: Binary Search**
- Pros: _[O(log n), very fast]_
- Cons: _[Requires sorted data]_
- Use when: _[Large sorted dataset]_

**Option 2: Linear Search**
- Pros: _[Works on unsorted, simple]_
- Cons: _[O(n), slow for large data]_
- Use when: _[Small dataset or unsorted]_

**Option 3: Hash Table**
- Pros: _[O(1) lookup, no sorting needed]_
- Cons: _[Extra space, can't find ranges]_
- Use when: _[Single lookups, space available]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**
- [ ] [704. Binary Search](https://leetcode.com/problems/binary-search/)
  - Pattern: _[Classic binary search]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [35. Search Insert Position](https://leetcode.com/problems/search-insert-position/)
  - Pattern: _[Binary search with insert]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [69. Sqrt(x)](https://leetcode.com/problems/sqrtx/)
  - Pattern: _[Binary search on answer]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [278. First Bad Version](https://leetcode.com/problems/first-bad-version/)
  - Pattern: _[Find first occurrence]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 3-4):**
- [ ] [33. Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/)
  - Pattern: _[Rotated array]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [34. Find First and Last Position of Element in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)
  - Pattern: _[Find range]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [74. Search a 2D Matrix](https://leetcode.com/problems/search-a-2d-matrix/)
  - Pattern: _[2D binary search]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [153. Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/)
  - Pattern: _[Find minimum]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

**Hard (Optional):**
- [ ] [4. Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/)
  - Pattern: _[Binary search on two arrays]_
  - Key insight: _[Fill in after solving]_

- [ ] [410. Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/)
  - Pattern: _[Binary search on answer]_
  - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Classic: search, insert, find range all work
  - [ ] Rotated: search and find min both work
  - [ ] 2D: all three matrix patterns work
  - [ ] On answer: sqrt, capacity, kth missing work
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify when to use binary search
  - [ ] Understand sorted vs rotated arrays
  - [ ] Know when to search on answer space
  - [ ] Recognize 2D matrix patterns

- [ ] **Problem Solving**
  - [ ] Solved 4 easy problems
  - [ ] Solved 3-4 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Handled edge cases (empty, single element)

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use binary search
  - [ ] Can explain why O(log n) is fast

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand off-by-one errors and how to avoid them

---

**Next Topic:** [09. Union-Find →](09-union-find.md)

**Back to:** [07. Trees - Recursion ←](07-trees-recursion.md)
