# 16. Advanced Topics

> Master bit manipulation, intervals, prefix sums, and monotonic stacks

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What are these advanced techniques in one sentence each?**
   - Bit manipulation: _[Fill in after implementation]_
   - Intervals: _[Fill in after implementation]_
   - Prefix sum: _[Fill in after implementation]_
   - Monotonic stack: _[Fill in after implementation]_

2. **Real-world analogies:**
   - Bit manipulation: "Like using switches that are either on or off..."
   - Intervals: "Like managing calendar appointments..."
   - Prefix sum: "Like keeping a running total..."
   - Monotonic stack: "Like organizing plates by size..."
   - Your analogies: _[Fill in]_

3. **When does each pattern work?**
   - Your answers: _[Fill in after solving problems]_

---

## Core Implementation

### Pattern 1: Bit Manipulation

**Concept:** Use bitwise operations for efficient computations.

**Use case:** XOR tricks, bit masks, counting bits, power of two.

```java
public class BitManipulation {

    /**
     * Problem: Single number (all appear twice except one)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using XOR
     */
    public static int singleNumber(int[] nums) {
        // TODO: XOR all numbers
        // TODO: a XOR a = 0, a XOR 0 = a
        // TODO: Duplicates cancel out, single remains

        return 0; // Replace with implementation
    }

    /**
     * Problem: Number of 1 bits (Hamming weight)
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement bit counting
     */
    public static int hammingWeight(int n) {
        // TODO: Count set bits
        // TODO: Method 1: Loop and check each bit
        // TODO: Method 2: n & (n-1) removes rightmost 1

        return 0; // Replace with implementation
    }

    /**
     * Problem: Reverse bits
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement bit reversal
     */
    public static int reverseBits(int n) {
        // TODO: Process bit by bit
        // TODO: Extract bit: (n >> i) & 1
        // TODO: Place bit: result |= (bit << (31 - i))

        return 0; // Replace with implementation
    }

    /**
     * Problem: Missing number (0 to n with one missing)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using XOR or math
     */
    public static int missingNumber(int[] nums) {
        // TODO: Method 1: XOR all indices and values
        // TODO: Method 2: Sum formula - sum(0..n) - sum(nums)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Power of two
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement power of two check
     */
    public static boolean isPowerOfTwo(int n) {
        // TODO: Power of 2 has exactly one bit set
        // TODO: Check: n > 0 && (n & (n-1)) == 0

        return false; // Replace with implementation
    }

    /**
     * Problem: Counting bits (0 to n)
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using DP with bit manipulation
     */
    public static int[] countBits(int n) {
        // TODO: dp[i] = dp[i >> 1] + (i & 1)
        // TODO: Bits in i = bits in i/2 + (1 if i is odd)

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: Sum of two integers without + operator
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement using bit operations
     */
    public static int getSum(int a, int b) {
        // TODO: XOR for sum without carry
        // TODO: AND and shift for carry
        // TODO: Repeat until no carry

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class BitManipulationClient {

    public static void main(String[] args) {
        System.out.println("=== Bit Manipulation ===\n");

        // Test 1: Single number
        System.out.println("--- Test 1: Single Number ---");
        int[] arr1 = {4, 1, 2, 1, 2};
        System.out.println("Array: " + Arrays.toString(arr1));
        System.out.println("Single number: " + BitManipulation.singleNumber(arr1));

        // Test 2: Hamming weight
        System.out.println("\n--- Test 2: Hamming Weight ---");
        int[] numbers = {11, 128, 255};
        for (int n : numbers) {
            int weight = BitManipulation.hammingWeight(n);
            System.out.printf("%d (binary: %s): %d bits%n",
                n, Integer.toBinaryString(n), weight);
        }

        // Test 3: Reverse bits
        System.out.println("\n--- Test 3: Reverse Bits ---");
        int n = 43261596;
        System.out.printf("Original: %d (binary: %s)%n",
            n, Integer.toBinaryString(n));
        int reversed = BitManipulation.reverseBits(n);
        System.out.printf("Reversed: %d (binary: %s)%n",
            reversed, Integer.toBinaryString(reversed));

        // Test 4: Missing number
        System.out.println("\n--- Test 4: Missing Number ---");
        int[] arr2 = {3, 0, 1};
        System.out.println("Array: " + Arrays.toString(arr2));
        System.out.println("Missing: " + BitManipulation.missingNumber(arr2));

        // Test 5: Power of two
        System.out.println("\n--- Test 5: Power of Two ---");
        int[] testPowers = {1, 2, 3, 4, 16, 18};
        for (int num : testPowers) {
            boolean isPower = BitManipulation.isPowerOfTwo(num);
            System.out.printf("%d: %s%n", num, isPower ? "YES" : "NO");
        }

        // Test 6: Counting bits
        System.out.println("\n--- Test 6: Counting Bits ---");
        int num = 5;
        int[] bitCounts = BitManipulation.countBits(num);
        System.out.printf("Bit counts from 0 to %d: %s%n", num, Arrays.toString(bitCounts));

        // Test 7: Sum without + operator
        System.out.println("\n--- Test 7: Sum Without + ---");
        int a = 15, b = 27;
        int sum = BitManipulation.getSum(a, b);
        System.out.printf("%d + %d = %d%n", a, b, sum);
    }
}
```

---

### Pattern 2: Intervals

**Concept:** Merge, insert, or manipulate intervals efficiently.

**Use case:** Meeting rooms, merge intervals, interval intersection.

```java
import java.util.*;

public class Intervals {

    /**
     * Problem: Merge overlapping intervals
     * Time: O(n log n), Space: O(n)
     *
     * TODO: Implement merge intervals
     */
    public static int[][] merge(int[][] intervals) {
        // TODO: Sort by start time
        // TODO: Iterate and merge overlapping intervals
        // TODO: If current.start <= previous.end, merge
        // TODO: Otherwise, add previous to result

        return new int[0][0]; // Replace with implementation
    }

    /**
     * Problem: Insert interval into sorted intervals
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement insert interval
     */
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        // TODO: Add all intervals before newInterval
        // TODO: Merge all overlapping intervals
        // TODO: Add all intervals after newInterval

        return new int[0][0]; // Replace with implementation
    }

    /**
     * Problem: Interval intersection
     * Time: O(m + n), Space: O(min(m,n))
     *
     * TODO: Implement interval intersection
     */
    public static int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        // TODO: Two pointers on both lists
        // TODO: Find intersection: max(start1, start2) to min(end1, end2)
        // TODO: Move pointer of interval that ends first

        return new int[0][0]; // Replace with implementation
    }

    /**
     * Problem: Minimum number of meeting rooms
     * Time: O(n log n), Space: O(n)
     *
     * TODO: Implement meeting rooms II
     */
    public static int minMeetingRooms(int[][] intervals) {
        // TODO: Method 1: Sort start and end times separately
        // TODO: Method 2: Use min-heap for end times

        return 0; // Replace with implementation
    }

    /**
     * Problem: Remove covered intervals
     * Time: O(n log n), Space: O(1)
     *
     * TODO: Implement remove covered
     */
    public static int removeCoveredIntervals(int[][] intervals) {
        // TODO: Sort by start (ascending), then end (descending)
        // TODO: Track current max end
        // TODO: If current.end <= maxEnd, it's covered

        return 0; // Replace with implementation
    }

    /**
     * Problem: Non-overlapping intervals (min removals)
     * Time: O(n log n), Space: O(1)
     *
     * TODO: Implement min removals
     */
    public static int eraseOverlapIntervals(int[][] intervals) {
        // TODO: Sort by end time (greedy)
        // TODO: Keep track of last end time
        // TODO: If overlap, increment removal count

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class IntervalsClient {

    public static void main(String[] args) {
        System.out.println("=== Intervals ===\n");

        // Test 1: Merge intervals
        System.out.println("--- Test 1: Merge Intervals ---");
        int[][] intervals1 = {{1,3}, {2,6}, {8,10}, {15,18}};
        System.out.println("Input: " + Arrays.deepToString(intervals1));
        int[][] merged = Intervals.merge(intervals1);
        System.out.println("Merged: " + Arrays.deepToString(merged));

        // Test 2: Insert interval
        System.out.println("\n--- Test 2: Insert Interval ---");
        int[][] intervals2 = {{1,3}, {6,9}};
        int[] newInterval = {2, 5};
        System.out.println("Intervals: " + Arrays.deepToString(intervals2));
        System.out.println("New: " + Arrays.toString(newInterval));
        int[][] inserted = Intervals.insert(intervals2, newInterval);
        System.out.println("Result: " + Arrays.deepToString(inserted));

        // Test 3: Interval intersection
        System.out.println("\n--- Test 3: Interval Intersection ---");
        int[][] first = {{0,2}, {5,10}, {13,23}, {24,25}};
        int[][] second = {{1,5}, {8,12}, {15,24}, {25,26}};
        System.out.println("First: " + Arrays.deepToString(first));
        System.out.println("Second: " + Arrays.deepToString(second));
        int[][] intersection = Intervals.intervalIntersection(first, second);
        System.out.println("Intersection: " + Arrays.deepToString(intersection));

        // Test 4: Meeting rooms
        System.out.println("\n--- Test 4: Meeting Rooms ---");
        int[][] meetings = {{0,30}, {5,10}, {15,20}};
        System.out.println("Meetings: " + Arrays.deepToString(meetings));
        int rooms = Intervals.minMeetingRooms(meetings);
        System.out.println("Min rooms needed: " + rooms);

        // Test 5: Remove covered intervals
        System.out.println("\n--- Test 5: Remove Covered Intervals ---");
        int[][] intervals3 = {{1,4}, {3,6}, {2,8}};
        System.out.println("Intervals: " + Arrays.deepToString(intervals3));
        int remaining = Intervals.removeCoveredIntervals(intervals3);
        System.out.println("Remaining after removing covered: " + remaining);

        // Test 6: Erase overlap intervals
        System.out.println("\n--- Test 6: Erase Overlap Intervals ---");
        int[][] intervals4 = {{1,2}, {2,3}, {3,4}, {1,3}};
        System.out.println("Intervals: " + Arrays.deepToString(intervals4));
        int removals = Intervals.eraseOverlapIntervals(intervals4);
        System.out.println("Min removals to make non-overlapping: " + removals);
    }
}
```

---

### Pattern 3: Prefix Sum

**Concept:** Precompute cumulative sums for fast range queries.

**Use case:** Subarray sum, range sum query, contiguous array.

```java
import java.util.*;

public class PrefixSum {

    /**
     * Problem: Range sum query (immutable array)
     * Time: O(1) query after O(n) preprocessing, Space: O(n)
     *
     * TODO: Implement range sum query
     */
    static class NumArray {
        private int[] prefixSum;

        public NumArray(int[] nums) {
            // TODO: Build prefix sum array
            // TODO: prefixSum[i] = sum of nums[0..i-1]
        }

        public int sumRange(int left, int right) {
            // TODO: Return prefixSum[right+1] - prefixSum[left]
            return 0; // Replace with implementation
        }
    }

    /**
     * Problem: Subarray sum equals K
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using prefix sum + hashmap
     */
    public static int subarraySum(int[] nums, int k) {
        // TODO: Use HashMap<prefixSum, frequency>
        // TODO: For each prefix sum:
        //   Check if (currentSum - k) exists in map
        //   Add count to result
        //   Update map with currentSum

        return 0; // Replace with implementation
    }

    /**
     * Problem: Contiguous array (equal 0s and 1s)
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using prefix sum
     */
    public static int findMaxLength(int[] nums) {
        // TODO: Convert 0s to -1s
        // TODO: Problem becomes: longest subarray with sum 0
        // TODO: Use HashMap<prefixSum, firstIndex>
        // TODO: If same prefix sum seen before, found zero-sum subarray

        return 0; // Replace with implementation
    }

    /**
     * Problem: Product of array except self
     * Time: O(n), Space: O(1) excluding output
     *
     * TODO: Implement using prefix/suffix products
     */
    public static int[] productExceptSelf(int[] nums) {
        // TODO: First pass: compute prefix products
        // TODO: Second pass: compute suffix products and multiply

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: Range sum query 2D (matrix)
     * Time: O(1) query after O(m*n) preprocessing, Space: O(m*n)
     *
     * TODO: Implement 2D prefix sum
     */
    static class NumMatrix {
        private int[][] prefixSum;

        public NumMatrix(int[][] matrix) {
            // TODO: Build 2D prefix sum
            // TODO: prefixSum[i][j] = sum of submatrix (0,0) to (i-1,j-1)
            // TODO: Use inclusion-exclusion principle
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            // TODO: Use inclusion-exclusion:
            //   sum = total - top - left + top-left
            return 0; // Replace with implementation
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class PrefixSumClient {

    public static void main(String[] args) {
        System.out.println("=== Prefix Sum ===\n");

        // Test 1: Range sum query
        System.out.println("--- Test 1: Range Sum Query ---");
        int[] arr = {-2, 0, 3, -5, 2, -1};
        PrefixSum.NumArray numArray = new PrefixSum.NumArray(arr);
        System.out.println("Array: " + Arrays.toString(arr));

        int[][] queries = {{0, 2}, {2, 5}, {0, 5}};
        for (int[] query : queries) {
            int sum = numArray.sumRange(query[0], query[1]);
            System.out.printf("sumRange(%d, %d) = %d%n", query[0], query[1], sum);
        }

        // Test 2: Subarray sum equals K
        System.out.println("\n--- Test 2: Subarray Sum Equals K ---");
        int[] arr2 = {1, 1, 1};
        int k = 2;
        System.out.println("Array: " + Arrays.toString(arr2));
        System.out.println("k = " + k);
        int count = PrefixSum.subarraySum(arr2, k);
        System.out.println("Count of subarrays: " + count);

        // Test 3: Contiguous array
        System.out.println("\n--- Test 3: Contiguous Array ---");
        int[] arr3 = {0, 1, 0, 1, 1, 0};
        System.out.println("Array: " + Arrays.toString(arr3));
        int maxLen = PrefixSum.findMaxLength(arr3);
        System.out.println("Max length with equal 0s and 1s: " + maxLen);

        // Test 4: Product except self
        System.out.println("\n--- Test 4: Product Except Self ---");
        int[] arr4 = {1, 2, 3, 4};
        System.out.println("Array: " + Arrays.toString(arr4));
        int[] products = PrefixSum.productExceptSelf(arr4);
        System.out.println("Products: " + Arrays.toString(products));

        // Test 5: 2D range sum query
        System.out.println("\n--- Test 5: 2D Range Sum Query ---");
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        PrefixSum.NumMatrix numMatrix = new PrefixSum.NumMatrix(matrix);

        System.out.println("Matrix:");
        for (int[] row : matrix) {
            System.out.println("  " + Arrays.toString(row));
        }

        int sum = numMatrix.sumRegion(2, 1, 4, 3);
        System.out.printf("sumRegion(2, 1, 4, 3) = %d%n", sum);
    }
}
```

---

### Pattern 4: Monotonic Stack

**Concept:** Maintain stack in monotonic (increasing/decreasing) order.

**Use case:** Next greater element, largest rectangle, trap rain water.

```java
import java.util.*;

public class MonotonicStack {

    /**
     * Problem: Next greater element
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using monotonic decreasing stack
     */
    public static int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        // TODO: Stack stores indices
        // TODO: Maintain decreasing stack
        // TODO: When nums[i] > stack.top(), found next greater

        return result; // Replace with implementation
    }

    /**
     * Problem: Daily temperatures (days until warmer)
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using monotonic stack
     */
    public static int[] dailyTemperatures(int[] temperatures) {
        // TODO: Similar to next greater element
        // TODO: Store index difference instead of value

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: Largest rectangle in histogram
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using monotonic increasing stack
     */
    public static int largestRectangleArea(int[] heights) {
        // TODO: Stack stores indices
        // TODO: Maintain increasing stack
        // TODO: When heights[i] < stack.top(), compute area
        // TODO: Width = current_index - left_boundary - 1
        // TODO: Height = heights[stack.top()]

        return 0; // Replace with implementation
    }

    /**
     * Problem: Maximal rectangle in binary matrix
     * Time: O(m*n), Space: O(n)
     *
     * TODO: Implement using largest rectangle for each row
     */
    public static int maximalRectangle(char[][] matrix) {
        // TODO: Convert each row to histogram
        // TODO: Apply largest rectangle algorithm

        return 0; // Replace with implementation
    }

    /**
     * Problem: Trap rain water
     * Time: O(n), Space: O(1) with two pointers
     *
     * TODO: Implement using two pointers or monotonic stack
     */
    public static int trap(int[] height) {
        // TODO: Method 1: Two pointers
        //   Track leftMax and rightMax
        // TODO: Method 2: Monotonic stack
        //   Stack stores indices of decreasing heights

        return 0; // Replace with implementation
    }

    /**
     * Problem: Remove K digits to make smallest number
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using monotonic increasing stack
     */
    public static String removeKdigits(String num, int k) {
        // TODO: Maintain increasing stack of digits
        // TODO: Remove k larger digits
        // TODO: Handle leading zeros

        return ""; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class MonotonicStackClient {

    public static void main(String[] args) {
        System.out.println("=== Monotonic Stack ===\n");

        // Test 1: Next greater element
        System.out.println("--- Test 1: Next Greater Element ---");
        int[] arr1 = {2, 1, 2, 4, 3};
        System.out.println("Array: " + Arrays.toString(arr1));
        int[] nextGreater = MonotonicStack.nextGreaterElement(arr1);
        System.out.println("Next greater: " + Arrays.toString(nextGreater));

        // Test 2: Daily temperatures
        System.out.println("\n--- Test 2: Daily Temperatures ---");
        int[] temps = {73, 74, 75, 71, 69, 72, 76, 73};
        System.out.println("Temperatures: " + Arrays.toString(temps));
        int[] days = MonotonicStack.dailyTemperatures(temps);
        System.out.println("Days to wait: " + Arrays.toString(days));

        // Test 3: Largest rectangle
        System.out.println("\n--- Test 3: Largest Rectangle in Histogram ---");
        int[] heights = {2, 1, 5, 6, 2, 3};
        System.out.println("Heights: " + Arrays.toString(heights));
        int maxArea = MonotonicStack.largestRectangleArea(heights);
        System.out.println("Largest rectangle area: " + maxArea);

        // Test 4: Maximal rectangle
        System.out.println("\n--- Test 4: Maximal Rectangle ---");
        char[][] matrix = {
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}
        };
        System.out.println("Matrix:");
        for (char[] row : matrix) {
            System.out.println("  " + Arrays.toString(row));
        }
        int maxRect = MonotonicStack.maximalRectangle(matrix);
        System.out.println("Maximal rectangle: " + maxRect);

        // Test 5: Trap rain water
        System.out.println("\n--- Test 5: Trap Rain Water ---");
        int[] elevation = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("Elevation: " + Arrays.toString(elevation));
        int water = MonotonicStack.trap(elevation);
        System.out.println("Water trapped: " + water);

        // Test 6: Remove K digits
        System.out.println("\n--- Test 6: Remove K Digits ---");
        String num = "1432219";
        int k = 3;
        System.out.println("Number: " + num);
        System.out.println("k = " + k);
        String smallest = MonotonicStack.removeKdigits(num, k);
        System.out.println("Smallest number: " + smallest);
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for advanced patterns.

### Question 1: Which pattern to use?

**Bit manipulation when:**
- Need: _[Constant space for flags/subsets]_
- Operations: _[XOR, AND, OR, shifts]_
- Examples: _[Single number, power of 2]_

**Intervals when:**
- Need: _[Merge, insert, find overlaps]_
- Input: _[Array of [start, end] pairs]_
- Examples: _[Meeting rooms, merge intervals]_

**Prefix sum when:**
- Need: _[Fast range sum queries]_
- Trade-off: _[O(n) space for O(1) queries]_
- Examples: _[Subarray sum, range query]_

**Monotonic stack when:**
- Need: _[Next greater/smaller element]_
- Pattern: _[Looking for boundary values]_
- Examples: _[Histogram, temperatures]_

### Your Decision Trees

```
Advanced Pattern Selection

Bit Manipulation:
├─ XOR all → Find single/missing element ✓
├─ Check bits → Count 1s, power of 2 ✓
├─ Bit masks → Subset operations ✓
└─ No +/- → Use XOR and AND ✓

Intervals:
├─ Overlapping? → Merge intervals ✓
├─ Insert new? → Insert interval ✓
├─ Intersection? → Two pointers ✓
└─ Min rooms? → Sweep line or heap ✓

Prefix Sum:
├─ Range queries → 1D/2D prefix sum ✓
├─ Subarray sum → HashMap + prefix ✓
├─ Equal 0s/1s → Convert to sum=0 ✓
└─ Product except self → Prefix/suffix ✓

Monotonic Stack:
├─ Next greater/smaller → Decreasing/increasing stack ✓
├─ Rectangle area → Increasing stack ✓
├─ Trap water → Stack or two pointers ✓
└─ Remove digits → Monotonic stack ✓
```

### The "Kill Switch" - When NOT to use

**Bit manipulation:**
- Don't use when: _[Logic complex, readability suffers]_

**Intervals:**
- Don't use when: _[Not dealing with ranges]_

**Prefix sum:**
- Don't use when: _[Array is mutable, single query]_

**Monotonic stack:**
- Don't use when: _[Not finding next/previous boundary]_

---

## Practice

### LeetCode Problems

**Easy (Complete 4):**
- [ ] [136. Single Number](https://leetcode.com/problems/single-number/)
  - Pattern: _[Bit manipulation - XOR]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [191. Number of 1 Bits](https://leetcode.com/problems/number-of-1-bits/)
  - Pattern: _[Bit counting]_
  - Your solution time: ___

- [ ] [303. Range Sum Query](https://leetcode.com/problems/range-sum-query-immutable/)
  - Pattern: _[Prefix sum]_
  - Your solution time: ___

- [ ] [496. Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/)
  - Pattern: _[Monotonic stack]_
  - Your solution time: ___

**Medium (Complete 6-8):**
- [ ] [56. Merge Intervals](https://leetcode.com/problems/merge-intervals/)
  - Pattern: _[Intervals]_
  - Difficulty: _[Rate 1-10]_

- [ ] [57. Insert Interval](https://leetcode.com/problems/insert-interval/)
  - Pattern: _[Intervals]_
  - Difficulty: _[Rate 1-10]_

- [ ] [560. Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/)
  - Pattern: _[Prefix sum + hashmap]_
  - Difficulty: _[Rate 1-10]_

- [ ] [739. Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)
  - Pattern: _[Monotonic stack]_
  - Difficulty: _[Rate 1-10]_

- [ ] [84. Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/)
  - Pattern: _[Monotonic stack]_
  - Difficulty: _[Rate 1-10]_

- [ ] [238. Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/)
  - Pattern: _[Prefix/suffix products]_
  - Difficulty: _[Rate 1-10]_

**Hard (Optional):**
- [ ] [42. Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)
  - Pattern: _[Monotonic stack or two pointers]_
  - Key insight: _[Fill in after solving]_

- [ ] [85. Maximal Rectangle](https://leetcode.com/problems/maximal-rectangle/)
  - Pattern: _[Histogram + monotonic stack]_
  - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving on:

- [ ] **Implementation**
  - [ ] Bit manipulation: XOR, counting bits, power of 2 all work
  - [ ] Intervals: merge, insert, intersection all work
  - [ ] Prefix sum: range query, subarray sum, 2D all work
  - [ ] Monotonic stack: next greater, histogram, temperatures all work
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify when to use bit manipulation
  - [ ] Recognize interval problems
  - [ ] Know when prefix sum helps
  - [ ] Identify monotonic stack opportunities

- [ ] **Problem Solving**
  - [ ] Solved 4 easy problems
  - [ ] Solved 6-8 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Understood when each pattern is optimal

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision trees for each pattern
  - [ ] Identified when NOT to use each pattern
  - [ ] Can explain trade-offs

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand why these techniques are "advanced"

---

**Congratulations!** You've completed all 16 DSA topics.

**Back to:** [15. Tries ←](15-tries.md)

**Home:** [DSA Overview](index.md)
