# 02. Sliding Window

> Optimize subarray/substring problems from O(n²) to O(n)

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is the sliding window pattern in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **How is it different from two pointers?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "Sliding window is like a camera viewfinder moving across a scene..."
   - Your analogy: _[Fill in]_

4. **When does this pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **When does this pattern fail?**
   - Your answer: _[Fill in after trying non-contiguous problems]_

---

## Core Implementation

### Pattern 1: Fixed Window Size

**Concept:** Window size is constant, slide one position at a time.

**Use case:** Maximum/minimum of k consecutive elements, average of subarrays.

```java
public class FixedWindow {

    /**
     * Problem: Maximum sum of K consecutive elements
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement fixed window
     */
    public static double maxAverageSubarray(int[] nums, int k) {
        if (nums.length < k) return 0.0;

        // TODO: Calculate sum of first k elements
        //   int windowSum = 0;
        //   for (int i = 0; i < k; i++) windowSum += nums[i];
        //   double maxAvg = windowSum / (double) k;

        // TODO: Slide window:
        //   for (int i = k; i < nums.length; i++)
        //     Remove nums[i-k] from windowSum
        //     Add nums[i] to windowSum
        //     Update maxAvg if needed

        return 0.0; // Replace with implementation
    }

    /**
     * Problem: Contains nearby duplicate within k distance
     * Time: O(n), Space: O(k)
     *
     * TODO: Implement using HashSet as fixed window
     */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        // TODO: Create HashSet for window
        //   Set<Integer> window = new HashSet<>();

        // TODO: For each index i:
        //   If window contains nums[i], return true
        //   Add nums[i] to window
        //   If window.size() > k, remove nums[i-k]

        return false; // Replace with implementation
    }

    /**
     * Problem: Maximum of all subarrays of size k
     * Time: O(n), Space: O(k) using deque
     *
     * TODO: Implement using deque for efficient max tracking
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        // TODO: Use Deque<Integer> to store indices
        //   Deque stores indices in decreasing order of values
        //   Front of deque always has index of maximum in current window

        return new int[0]; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class FixedWindowClient {

    public static void main(String[] args) {
        System.out.println("=== Fixed Window Size ===\n");

        // Test 1: Max average subarray
        System.out.println("--- Test 1: Max Average ---");
        int[] nums1 = {1, 12, -5, -6, 50, 3};
        int k1 = 4;

        double maxAvg = FixedWindow.maxAverageSubarray(nums1, k1);
        System.out.printf("Array: %s%n", Arrays.toString(nums1));
        System.out.printf("k = %d%n", k1);
        System.out.printf("Max average: %.2f%n", maxAvg);

        // Test 2: Contains nearby duplicate
        System.out.println("\n--- Test 2: Nearby Duplicate ---");
        int[][] dupTests = {
            {1, 2, 3, 1},     // k=3, should be true
            {1, 0, 1, 1},     // k=1, should be true
            {1, 2, 3, 1, 2, 3} // k=2, should be false
        };
        int[] kValues = {3, 1, 2};

        for (int i = 0; i < dupTests.length; i++) {
            boolean hasDup = FixedWindow.containsNearbyDuplicate(dupTests[i], kValues[i]);
            System.out.printf("Array: %s, k=%d -> %b%n",
                Arrays.toString(dupTests[i]), kValues[i], hasDup);
        }

        // Test 3: Max sliding window
        System.out.println("\n--- Test 3: Max Sliding Window ---");
        int[] nums3 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k3 = 3;

        int[] maxes = FixedWindow.maxSlidingWindow(nums3, k3);
        System.out.printf("Array: %s%n", Arrays.toString(nums3));
        System.out.printf("k = %d%n", k3);
        System.out.printf("Maximums: %s%n", Arrays.toString(maxes));
    }
}
```

---

### Pattern 2: Dynamic Window Size

**Concept:** Window expands and contracts based on condition.

**Use case:** Longest/shortest substring with constraint, subarray sum.

```java
import java.util.*;

public class DynamicWindow {

    /**
     * Problem: Longest substring without repeating characters
     * Time: O(n), Space: O(k) where k = unique chars
     *
     * TODO: Implement dynamic window with HashSet
     */
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> window = new HashSet<>();
        int left = 0, maxLen = 0;

        // TODO: For each right pointer (0 to s.length()):
        //   char c = s.charAt(right)
        //   While window contains c:
        //     Remove s.charAt(left) from window
        //     left++
        //   Add c to window
        //   Update maxLen = Math.max(maxLen, right - left + 1)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Longest substring with at most K distinct characters
     * Time: O(n), Space: O(k)
     *
     * TODO: Implement with HashMap for frequency counting
     */
    public static int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (k == 0) return 0;

        Map<Character, Integer> window = new HashMap<>();
        int left = 0, maxLen = 0;

        // TODO: For each right pointer:
        //   Add s.charAt(right) to window (increment frequency)
        //   While window.size() > k:
        //     Decrease frequency of s.charAt(left)
        //     If frequency becomes 0, remove from window
        //     left++
        //   Update maxLen

        return 0; // Replace with implementation
    }

    /**
     * Problem: Minimum size subarray sum >= target
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement shrinking window
     */
    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;

        // TODO: For each right pointer:
        //   Add nums[right] to sum
        //   While sum >= target:
        //     Update minLen = Math.min(minLen, right - left + 1)
        //     Subtract nums[left] from sum
        //     left++

        // TODO: Return minLen == Integer.MAX_VALUE ? 0 : minLen

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class DynamicWindowClient {

    public static void main(String[] args) {
        System.out.println("=== Dynamic Window Size ===\n");

        // Test 1: Longest substring without repeating
        System.out.println("--- Test 1: Longest Substring (No Repeats) ---");
        String[] test1 = {"abcabcbb", "bbbbb", "pwwkew", ""};

        for (String s : test1) {
            int len = DynamicWindow.lengthOfLongestSubstring(s);
            System.out.printf("\"%s\" -> %d%n", s, len);
        }

        // Test 2: Longest with K distinct
        System.out.println("\n--- Test 2: K Distinct Characters ---");
        String[] test2 = {"eceba", "aa", "aaabbccd"};
        int[] kValues = {2, 1, 2};

        for (int i = 0; i < test2.length; i++) {
            int len = DynamicWindow.lengthOfLongestSubstringKDistinct(test2[i], kValues[i]);
            System.out.printf("\"%s\", k=%d -> %d%n", test2[i], kValues[i], len);
        }

        // Test 3: Minimum subarray sum
        System.out.println("\n--- Test 3: Min Subarray Sum >= Target ---");
        int[][] arrays = {
            {2, 3, 1, 2, 4, 3},
            {1, 4, 4},
            {1, 1, 1, 1, 1, 1, 1, 1}
        };
        int[] targets = {7, 4, 11};

        for (int i = 0; i < arrays.length; i++) {
            int minLen = DynamicWindow.minSubArrayLen(targets[i], arrays[i]);
            System.out.printf("Array: %s, target=%d -> %d%n",
                Arrays.toString(arrays[i]), targets[i], minLen);
        }
    }
}
```

---

### Pattern 3: String Problems with Window

**Concept:** Track character frequencies in window for pattern matching.

**Use case:** Anagram problems, substring with all characters.

```java
import java.util.*;

public class StringWindow {

    /**
     * Problem: Find all anagrams of pattern in string
     * Time: O(n), Space: O(1) - only 26 letters
     *
     * TODO: Implement using frequency arrays
     */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s.length() < p.length()) return result;

        // TODO: Create frequency array for pattern p
        //   int[] pCount = new int[26];
        //   for (char c : p.toCharArray()) pCount[c - 'a']++;

        // TODO: Create frequency array for current window
        //   int[] sCount = new int[26];

        // TODO: Fixed window of size p.length()
        //   Build first window
        //   Compare counts, if match add index 0
        //   Slide window: remove left, add right, compare

        return result; // Replace with implementation
    }

    /**
     * Problem: Permutation in string (s2 contains permutation of s1)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using sliding window comparison
     */
    public static boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        // TODO: Similar to findAnagrams but return true on first match

        return false; // Replace with implementation
    }

    /**
     * Problem: Minimum window substring containing all chars of t
     * Time: O(n + m), Space: O(k) where k = unique chars
     *
     * TODO: Implement using two frequency maps
     */
    public static String minWindow(String s, String t) {
        if (s.isEmpty() || t.isEmpty()) return "";

        // TODO: Create frequency map for t
        //   Map<Character, Integer> required = new HashMap<>();

        // TODO: Track matched characters
        //   int matched = 0, required_size = required.size()

        // TODO: Expand right, shrink left when valid
        //   Track minimum window start and length

        return ""; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class StringWindowClient {

    public static void main(String[] args) {
        System.out.println("=== String Window Problems ===\n");

        // Test 1: Find anagrams
        System.out.println("--- Test 1: Find Anagrams ---");
        String[][] anagramTests = {
            {"cbaebabacd", "abc"},
            {"abab", "ab"}
        };

        for (String[] test : anagramTests) {
            List<Integer> indices = StringWindow.findAnagrams(test[0], test[1]);
            System.out.printf("s=\"%s\", p=\"%s\" -> %s%n",
                test[0], test[1], indices);
        }

        // Test 2: Check inclusion
        System.out.println("\n--- Test 2: Permutation In String ---");
        String[][] inclusionTests = {
            {"ab", "eidbaooo"},
            {"ab", "eidboaoo"},
            {"abc", "bbbca"}
        };

        for (String[] test : inclusionTests) {
            boolean found = StringWindow.checkInclusion(test[0], test[1]);
            System.out.printf("s1=\"%s\", s2=\"%s\" -> %b%n",
                test[0], test[1], found);
        }

        // Test 3: Minimum window
        System.out.println("\n--- Test 3: Minimum Window Substring ---");
        String[][] windowTests = {
            {"ADOBECODEBANC", "ABC"},
            {"a", "a"},
            {"a", "aa"}
        };

        for (String[] test : windowTests) {
            String result = StringWindow.minWindow(test[0], test[1]);
            System.out.printf("s=\"%s\", t=\"%s\" -> \"%s\"%n",
                test[0], test[1], result);
        }
    }
}
```

---

### Pattern 4: Two Pointers + Window Hybrid

**Concept:** Combine sliding window with two-pointer techniques.

**Use case:** Character replacement, fruit baskets, longest repeating replacement.

```java
import java.util.*;

public class HybridWindow {

    /**
     * Problem: Longest repeating character replacement
     * Time: O(n), Space: O(1) - only 26 letters
     *
     * TODO: Implement window with character replacement
     */
    public static int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int left = 0, maxCount = 0, maxLen = 0;

        // TODO: For each right pointer:
        //   Increment count[s.charAt(right) - 'A']
        //   Update maxCount (most frequent char in window)
        //   If (window_size - maxCount) > k:
        //     Need to shrink window
        //     Decrement count[s.charAt(left) - 'A']
        //     left++
        //   Update maxLen

        return 0; // Replace with implementation
    }

    /**
     * Problem: Max consecutive ones with k flips
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement window tracking flips
     */
    public static int longestOnes(int[] nums, int k) {
        int left = 0, zeros = 0, maxLen = 0;

        // TODO: For each right pointer:
        //   If nums[right] == 0, increment zeros
        //   While zeros > k:
        //     If nums[left] == 0, decrement zeros
        //     left++
        //   Update maxLen

        return 0; // Replace with implementation
    }

    /**
     * Problem: Fruits into baskets (at most 2 types)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement window with at most 2 distinct elements
     */
    public static int totalFruit(int[] fruits) {
        Map<Integer, Integer> basket = new HashMap<>();
        int left = 0, maxFruits = 0;

        // TODO: For each right pointer:
        //   Add fruits[right] to basket
        //   While basket.size() > 2:
        //     Remove fruits[left] from basket
        //     left++
        //   Update maxFruits

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class HybridWindowClient {

    public static void main(String[] args) {
        System.out.println("=== Two Pointers + Window Hybrid ===\n");

        // Test 1: Character replacement
        System.out.println("--- Test 1: Character Replacement ---");
        String[] strings = {"ABAB", "AABABBA", "AAAA"};
        int[] kValues = {2, 1, 2};

        for (int i = 0; i < strings.length; i++) {
            int len = HybridWindow.characterReplacement(strings[i], kValues[i]);
            System.out.printf("s=\"%s\", k=%d -> %d%n",
                strings[i], kValues[i], len);
        }

        // Test 2: Max consecutive ones
        System.out.println("\n--- Test 2: Max Consecutive Ones ---");
        int[][] arrays = {
            {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1}
        };
        int[] flips = {2, 3};

        for (int i = 0; i < arrays.length; i++) {
            int len = HybridWindow.longestOnes(arrays[i], flips[i]);
            System.out.printf("Array: %s%n", Arrays.toString(arrays[i]));
            System.out.printf("k=%d -> %d%n%n", flips[i], len);
        }

        // Test 3: Fruits into baskets
        System.out.println("--- Test 3: Fruits Into Baskets ---");
        int[][] fruitArrays = {
            {1, 2, 1},
            {0, 1, 2, 2},
            {1, 2, 3, 2, 2}
        };

        for (int[] fruits : fruitArrays) {
            int total = HybridWindow.totalFruit(fruits);
            System.out.printf("Fruits: %s -> %d%n",
                Arrays.toString(fruits), total);
        }
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for when to use sliding window.

### Question 1: Is the subarray/substring contiguous?

Answer after solving problems:
- **Why contiguous matters?** _[Sliding window only works on contiguous data]_
- **Can sliding window work on non-contiguous?** _[No - need other techniques]_
- **Your observation:** _[Fill in based on testing]_

### Question 2: Fixed vs Dynamic window?

Answer for each pattern:

**Fixed window when:**
- Window size: _[Known constant k]_
- Movement rule: _[Always move both pointers together]_
- Example problems: _[Max average, nearby duplicate]_

**Dynamic window when:**
- Window size: _[Varies based on constraint]_
- Movement rule: _[Expand right, shrink left when needed]_
- Example problems: _[Longest substring, min subarray sum]_

### Question 3: What state to track?

Answer for different scenarios:

**For sum/count problems:**
- Track: _[Running sum, count]_
- Data structure: _[Variables, no extra space]_

**For unique elements:**
- Track: _[Set of current elements]_
- Data structure: _[HashSet]_

**For frequency:**
- Track: _[Count of each element]_
- Data structure: _[HashMap or frequency array]_

### Your Decision Tree

Build this after solving practice problems:

```
Sliding Window Pattern Selection
│
├─ Is subarray/substring contiguous?
│   ├─ NO → Use other technique (DP, backtracking)
│   └─ YES → Continue
│
├─ Is window size known?
│   ├─ YES (fixed k) → Use fixed window ✓
│   └─ NO (find optimal) → Use dynamic window ✓
│
├─ What to track in window?
│   ├─ Sum/Count → Variables O(1) space ✓
│   ├─ Unique elements → HashSet O(k) space ✓
│   ├─ Frequencies → HashMap/Array O(k) space ✓
│   └─ Maximum → Deque O(k) space ✓
│
└─ Shrink condition?
    ├─ Fixed: Always shrink after expanding
    └─ Dynamic: Shrink while constraint violated
```

### The "Kill Switch" - When NOT to use Sliding Window

**Don't use sliding window when:**
1. _[Need non-contiguous elements - use DP instead]_
2. _[Need all subsets/permutations - use backtracking]_
3. _[Data not linear (trees, graphs) - use DFS/BFS]_
4. _[No concept of "window" or "range" in problem]_

### The Rule of Three: Alternatives

**Option 1: Sliding Window**
- Pros: _[O(n) time, elegant solution]_
- Cons: _[Only works on contiguous subarrays]_
- Use when: _[Subarray/substring optimization problem]_

**Option 2: Two Pointers (no window)**
- Pros: _[More flexible, works on sorted arrays]_
- Cons: _[May not track window state efficiently]_
- Use when: _[Pair finding, partitioning, sorted data]_

**Option 3: Brute Force (nested loops)**
- Pros: _[Simple, works for any constraint]_
- Cons: _[O(n²) or O(n³) time complexity]_
- Use when: _[Very small input, one-time calculation]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**
- [ ] [643. Maximum Average Subarray I](https://leetcode.com/problems/maximum-average-subarray-i/)
  - Pattern: _[Fixed window]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [219. Contains Duplicate II](https://leetcode.com/problems/contains-duplicate-ii/)
  - Pattern: _[Fixed window with HashSet]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [1876. Substrings of Size Three with Distinct Characters](https://leetcode.com/problems/substrings-of-size-three-with-distinct-characters/)
  - Pattern: _[Fixed window]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [1456. Maximum Number of Vowels](https://leetcode.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/)
  - Pattern: _[Fixed window]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 3-4):**
- [ ] [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
  - Pattern: _[Dynamic window]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_
  - Mistake made: _[Fill in if any]_

- [ ] [424. Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/)
  - Pattern: _[Dynamic window with replacement]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [438. Find All Anagrams in a String](https://leetcode.com/problems/find-all-anagrams-in-a-string/)
  - Pattern: _[Fixed window with frequency]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [567. Permutation in String](https://leetcode.com/problems/permutation-in-string/)
  - Pattern: _[Fixed window with frequency]_
  - Comparison to 438: _[How similar?]_

**Hard (Optional):**
- [ ] [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
  - Pattern: _[Dynamic window with frequency]_
  - Key insight: _[Fill in after solving]_

- [ ] [239. Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)
  - Pattern: _[Fixed window with deque]_
  - Key insight: _[Monotonic deque technique]_

### Problem-Solving Template

For each problem:

```markdown
## Problem: [Name]

**Pattern identified:** _[Which sliding window variant?]_

**Why sliding window?** _[What makes this a window problem?]_

**Window type:** _[Fixed or dynamic?]_

**Approach:**
1. _[Step 1]_
2. _[Step 2]_
3. _[Step 3]_

**Code:**
```java
// Your solution
```

**Complexity:**
- Time: _[Fill in]_
- Space: _[Fill in]_

**Edge cases tested:**
1. _[Fill in]_
2. _[Fill in]_

**Mistakes made:**
- _[What went wrong initially?]_
- _[How did you fix it?]_
```

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Fixed window: max average, nearby duplicate, max sliding window all work
  - [ ] Dynamic window: longest substring, k distinct, min subarray sum all work
  - [ ] String window: find anagrams, check inclusion, min window all work
  - [ ] Hybrid: character replacement, max ones, fruit baskets all work
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify fixed vs dynamic window
  - [ ] Understand when to expand vs shrink
  - [ ] Know what state to track in window
  - [ ] Understand the contiguous requirement

- [ ] **Problem Solving**
  - [ ] Solved 4 easy problems
  - [ ] Solved 3-4 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Handled edge cases (empty, k > length)

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use sliding window
  - [ ] Can explain trade-offs vs other approaches

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand difference from two pointers

---

**Next Topic:** [03. Hash Tables →](03-hash-tables.md)

**Back to:** [01. Two Pointers ←](01-two-pointers.md)
