# 03. Hash Tables

> O(1) average lookup, insertion, and deletion using key-value mapping

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is a hash table in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why is O(1) lookup possible?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "Hash tables are like a library card catalog..."
   - Your analogy: _[Fill in]_

4. **When does this pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **What happens when two keys hash to the same location?**
   - Your answer: _[Fill in after learning about collisions]_

---

## Core Implementation

### Pattern 1: Basic Hash Map Operations

**Concept:** Fast lookups using key-value pairs.

**Use case:** Frequency counting, two-sum problems, duplicate detection.

```java
public class BasicHashMap {

    /**
     * Problem: Two Sum - find indices of two numbers that sum to target
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using HashMap for O(1) lookup
     */
    public static int[] twoSum(int[] nums, int target) {
        // TODO: Create HashMap<Integer, Integer> to store value -> index

        // TODO: For each num in nums:
        //   Calculate complement = target - num
        //   If map contains complement, return [map.get(complement), currentIndex]
        //   Otherwise, put num -> currentIndex in map

        return new int[] {-1, -1}; // Replace with implementation
    }

    /**
     * Problem: Count frequency of each character
     * Time: O(n), Space: O(k) where k = unique characters
     *
     * TODO: Implement frequency counter
     */
    public static Map<Character, Integer> countCharacters(String s) {
        // TODO: Create HashMap<Character, Integer>

        // TODO: For each char in s:
        //   map.put(char, map.getOrDefault(char, 0) + 1)

        return new HashMap<>(); // Replace with implementation
    }

    /**
     * Problem: Contains duplicate - check if array has duplicates
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using HashSet
     */
    public static boolean containsDuplicate(int[] nums) {
        // TODO: Create HashSet<Integer>

        // TODO: For each num in nums:
        //   If set contains num, return true
        //   Otherwise, add num to set

        // TODO: Return false if no duplicates found

        return false; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class BasicHashMapClient {

    public static void main(String[] args) {
        System.out.println("=== Basic Hash Map Operations ===\n");

        // Test 1: Two Sum
        System.out.println("--- Test 1: Two Sum ---");
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] result = BasicHashMap.twoSum(nums, target);
        System.out.printf("Array: %s%n", Arrays.toString(nums));
        System.out.printf("Target: %d%n", target);
        System.out.printf("Indices: %s%n", Arrays.toString(result));
        if (result[0] != -1) {
            System.out.printf("Values: %d + %d = %d%n",
                nums[result[0]], nums[result[1]], target);
        }

        // Test 2: Character frequency
        System.out.println("\n--- Test 2: Character Frequency ---");
        String[] testStrings = {"hello", "mississippi", "aabbcc"};

        for (String s : testStrings) {
            Map<Character, Integer> freq = BasicHashMap.countCharacters(s);
            System.out.printf("String: \"%s\"%n", s);
            System.out.println("Frequency: " + freq);
            System.out.println();
        }

        // Test 3: Contains duplicate
        System.out.println("--- Test 3: Contains Duplicate ---");
        int[][] testArrays = {
            {1, 2, 3, 4, 5},
            {1, 2, 3, 1},
            {1, 1, 1, 3, 3, 4, 3, 2, 4, 2}
        };

        for (int[] arr : testArrays) {
            boolean hasDup = BasicHashMap.containsDuplicate(arr);
            System.out.printf("Array: %s -> %s%n",
                Arrays.toString(arr), hasDup ? "HAS duplicates" : "NO duplicates");
        }
    }
}
```

---

### Pattern 2: Hash Set for Fast Membership Testing

**Concept:** Use HashSet for O(1) membership checks.

**Use case:** Finding missing numbers, intersection/union operations.

```java
import java.util.*;

public class HashSetOperations {

    /**
     * Problem: Find intersection of two arrays
     * Time: O(n + m), Space: O(min(n, m))
     *
     * TODO: Implement using HashSet
     */
    public static int[] intersection(int[] nums1, int[] nums2) {
        // TODO: Add all elements from nums1 to HashSet

        // TODO: Create result set to store intersection
        // TODO: For each element in nums2:
        //   If set1 contains element, add to result set

        // TODO: Convert result set to array and return

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: Find missing number from 0 to n
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using HashSet
     */
    public static int missingNumber(int[] nums) {
        // TODO: Add all nums to HashSet

        // TODO: For i from 0 to nums.length:
        //   If set doesn't contain i, return i

        return -1; // Replace with implementation
    }

    /**
     * Problem: Longest consecutive sequence
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using HashSet
     */
    public static int longestConsecutive(int[] nums) {
        // TODO: Add all nums to HashSet

        // TODO: For each num in set:
        //   If num-1 not in set (it's a sequence start):
        //     Count consecutive numbers (num+1, num+2, ...)
        //     Track maximum length

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class HashSetOperationsClient {

    public static void main(String[] args) {
        System.out.println("=== Hash Set Operations ===\n");

        // Test 1: Intersection
        System.out.println("--- Test 1: Intersection ---");
        int[] arr1 = {1, 2, 2, 1};
        int[] arr2 = {2, 2};

        int[] intersection = HashSetOperations.intersection(arr1, arr2);
        System.out.printf("Array 1: %s%n", Arrays.toString(arr1));
        System.out.printf("Array 2: %s%n", Arrays.toString(arr2));
        System.out.printf("Intersection: %s%n", Arrays.toString(intersection));

        int[] arr3 = {4, 9, 5};
        int[] arr4 = {9, 4, 9, 8, 4};

        int[] intersection2 = HashSetOperations.intersection(arr3, arr4);
        System.out.printf("\nArray 1: %s%n", Arrays.toString(arr3));
        System.out.printf("Array 2: %s%n", Arrays.toString(arr4));
        System.out.printf("Intersection: %s%n", Arrays.toString(intersection2));

        // Test 2: Missing number
        System.out.println("\n--- Test 2: Missing Number ---");
        int[][] testArrays = {
            {3, 0, 1},
            {0, 1},
            {9, 6, 4, 2, 3, 5, 7, 0, 1}
        };

        for (int[] arr : testArrays) {
            int missing = HashSetOperations.missingNumber(arr);
            System.out.printf("Array: %s -> Missing: %d%n",
                Arrays.toString(arr), missing);
        }

        // Test 3: Longest consecutive sequence
        System.out.println("\n--- Test 3: Longest Consecutive ---");
        int[][] sequenceArrays = {
            {100, 4, 200, 1, 3, 2},
            {0, 3, 7, 2, 5, 8, 4, 6, 0, 1},
            {9, 1, -3, 2, 4, 8, 3, -1, 6, -2, -4, 7}
        };

        for (int[] arr : sequenceArrays) {
            int length = HashSetOperations.longestConsecutive(arr);
            System.out.printf("Array: %s%n", Arrays.toString(arr));
            System.out.printf("Longest consecutive: %d%n%n", length);
        }
    }
}
```

---

### Pattern 3: Hash Map for Grouping

**Concept:** Group elements by a computed key.

**Use case:** Anagrams, group by property, categorization.

```java
import java.util.*;

public class HashMapGrouping {

    /**
     * Problem: Group anagrams together
     * Time: O(n * k log k) where k = max word length, Space: O(n * k)
     *
     * TODO: Implement using HashMap with sorted string as key
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        // TODO: Create HashMap<String, List<String>>

        // TODO: For each string:
        //   Sort characters to create key
        //   Add original string to list for that key

        // TODO: Return all values from map

        return new ArrayList<>(); // Replace with implementation
    }

    /**
     * Problem: Group numbers by digit sum
     * Time: O(n * d) where d = digits, Space: O(n)
     *
     * TODO: Implement custom grouping
     */
    public static Map<Integer, List<Integer>> groupByDigitSum(int[] nums) {
        // TODO: Create HashMap<Integer, List<Integer>>

        // TODO: For each num:
        //   Calculate sum of digits
        //   Add num to list for that sum

        return new HashMap<>(); // Replace with implementation
    }

    /**
     * Problem: Find all strings that start with same character
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement grouping by first character
     */
    public static Map<Character, List<String>> groupByFirstChar(String[] words) {
        // TODO: Create HashMap<Character, List<String>>

        // TODO: For each word:
        //   Get first character
        //   Add word to list for that character

        return new HashMap<>(); // Replace with implementation
    }

    // Helper: Calculate digit sum
    private static int digitSum(int n) {
        int sum = 0;
        n = Math.abs(n);
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class HashMapGroupingClient {

    public static void main(String[] args) {
        System.out.println("=== Hash Map Grouping ===\n");

        // Test 1: Group anagrams
        System.out.println("--- Test 1: Group Anagrams ---");
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};

        List<List<String>> groups = HashMapGrouping.groupAnagrams(words);
        System.out.println("Words: " + Arrays.toString(words));
        System.out.println("Grouped:");
        for (List<String> group : groups) {
            System.out.println("  " + group);
        }

        // Test 2: Group by digit sum
        System.out.println("\n--- Test 2: Group by Digit Sum ---");
        int[] numbers = {12, 21, 13, 31, 100, 10, 1, 23, 32};

        Map<Integer, List<Integer>> digitGroups = HashMapGrouping.groupByDigitSum(numbers);
        System.out.println("Numbers: " + Arrays.toString(numbers));
        System.out.println("Grouped by digit sum:");
        for (Map.Entry<Integer, List<Integer>> entry : digitGroups.entrySet()) {
            System.out.printf("  Sum %d: %s%n", entry.getKey(), entry.getValue());
        }

        // Test 3: Group by first character
        System.out.println("\n--- Test 3: Group by First Character ---");
        String[] dictionary = {"apple", "ant", "ball", "bear", "cat", "car", "dog"};

        Map<Character, List<String>> charGroups = HashMapGrouping.groupByFirstChar(dictionary);
        System.out.println("Words: " + Arrays.toString(dictionary));
        System.out.println("Grouped by first character:");
        for (Map.Entry<Character, List<String>> entry : charGroups.entrySet()) {
            System.out.printf("  %c: %s%n", entry.getKey(), entry.getValue());
        }
    }
}
```

---

### Pattern 4: Hash Map for Sliding Window with Constraints

**Concept:** Track window state using frequency map.

**Use case:** Substring problems with character constraints.

```java
import java.util.*;

public class HashMapWindow {

    /**
     * Problem: Minimum window substring containing all chars of target
     * Time: O(n + m), Space: O(k) where k = unique chars
     *
     * TODO: Implement using HashMap to track frequencies
     */
    public static String minWindow(String s, String t) {
        // TODO: Create frequency map for target string t

        // TODO: Use sliding window with two pointers
        // TODO: Expand right until all chars found
        // TODO: Contract left while valid
        // TODO: Track minimum window

        return ""; // Replace with implementation
    }

    /**
     * Problem: Check if s2 contains permutation of s1
     * Time: O(n), Space: O(1) - only 26 chars
     *
     * TODO: Implement using frequency comparison
     */
    public static boolean checkInclusion(String s1, String s2) {
        // TODO: Create frequency map for s1

        // TODO: Use fixed window of size s1.length()
        // TODO: Compare frequencies

        return false; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class HashMapWindowClient {

    public static void main(String[] args) {
        System.out.println("=== Hash Map Sliding Window ===\n");

        // Test 1: Minimum window substring
        System.out.println("--- Test 1: Minimum Window ---");
        String[][] testCases = {
            {"ADOBECODEBANC", "ABC"},
            {"a", "a"},
            {"a", "aa"}
        };

        for (String[] test : testCases) {
            String s = test[0];
            String t = test[1];
            String result = HashMapWindow.minWindow(s, t);
            System.out.printf("s=\"%s\", t=\"%s\" -> \"%s\"%n", s, t, result);
        }

        // Test 2: Check inclusion (permutation)
        System.out.println("\n--- Test 2: Check Inclusion ---");
        String[][] inclusionTests = {
            {"ab", "eidbaooo"},
            {"ab", "eidboaoo"},
            {"abc", "ccccbbbbaaaa"}
        };

        for (String[] test : inclusionTests) {
            String s1 = test[0];
            String s2 = test[1];
            boolean result = HashMapWindow.checkInclusion(s1, s2);
            System.out.printf("s1=\"%s\", s2=\"%s\" -> %b%n", s1, s2, result);
        }
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for when to use hash tables.

### Question 1: What operation do you need?

Answer after solving problems:
- **Fast lookup by key?** _[When to use HashMap vs array?]_
- **Fast membership test?** _[When to use HashSet?]_
- **Frequency counting?** _[Why is HashMap ideal?]_
- **Your observation:** _[Fill in based on testing]_

### Question 2: What are the time/space trade-offs?

Answer for each pattern:

**HashMap for lookups:**
- Time complexity: _[Average case? Worst case?]_
- Space complexity: _[How much extra space?]_
- Best use cases: _[List problems you solved]_

**HashSet for membership:**
- Time complexity: _[Compare to linear search]_
- Space complexity: _[Trade-off worth it when?]_
- Best use cases: _[List problems you solved]_

**HashMap for grouping:**
- Time complexity: _[Depends on what?]_
- Space complexity: _[How to estimate?]_
- Best use cases: _[List problems you solved]_

### Your Decision Tree

Build this after solving practice problems:

```
Hash Table Pattern Selection
│
├─ What do you need?
│   │
│   ├─ Fast lookup by key?
│   │   └─ Use: HashMap ✓
│   │
│   ├─ Fast membership test?
│   │   └─ Use: HashSet ✓
│   │
│   ├─ Count frequencies?
│   │   └─ Use: HashMap<Key, Integer> ✓
│   │
│   ├─ Group by property?
│   │   └─ Use: HashMap<Key, List<Value>> ✓
│   │
│   └─ Track window state?
│       └─ Use: HashMap with sliding window ✓
│
└─ Space constraint?
    ├─ Yes → Consider alternatives
    └─ No → Hash table is usually best choice
```

### The "Kill Switch" - When NOT to use Hash Tables

**Don't use hash tables when:**
1. _[Need sorted order? What to use instead?]_
2. _[Space is critical constraint? What alternatives?]_
3. _[Keys don't have good hash function? What happens?]_
4. _[Need to maintain insertion order? What to use?]_

### The Rule of Three: Alternatives

**Option 1: Hash Table**
- Pros: _[Fill in - average O(1) operations?]_
- Cons: _[Fill in - space overhead, no order?]_
- Use when: _[Fill in - lookup speed critical?]_

**Option 2: Sorted Array + Binary Search**
- Pros: _[Fill in - less space, sorted order?]_
- Cons: _[Fill in - O(log n) lookup?]_
- Use when: _[Fill in - data rarely changes?]_

**Option 3: Linear Search**
- Pros: _[Fill in - no extra space, simple?]_
- Cons: _[Fill in - O(n) lookup?]_
- Use when: _[Fill in - small dataset?]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**
- [ ] [1. Two Sum](https://leetcode.com/problems/two-sum/)
  - Pattern: _[Which one?]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [217. Contains Duplicate](https://leetcode.com/problems/contains-duplicate/)
  - Pattern: _[Which one?]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [242. Valid Anagram](https://leetcode.com/problems/valid-anagram/)
  - Pattern: _[Which one?]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [349. Intersection of Two Arrays](https://leetcode.com/problems/intersection-of-two-arrays/)
  - Pattern: _[Which one?]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 3-4):**
- [ ] [49. Group Anagrams](https://leetcode.com/problems/group-anagrams/)
  - Pattern: _[Which one?]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_
  - Mistake made: _[Fill in if any]_

- [ ] [128. Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/)
  - Pattern: _[Which one?]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [560. Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/)
  - Pattern: _[Which one?]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Prefix sum + HashMap]_

- [ ] [387. First Unique Character in a String](https://leetcode.com/problems/first-unique-character-in-a-string/)
  - Pattern: _[Which one?]_
  - Comparison: _[Two-pass vs one-pass?]_

**Hard (Optional):**
- [ ] [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
  - Pattern: _[Sliding window + HashMap]_
  - Key insight: _[Fill in after solving]_

- [ ] [30. Substring with Concatenation of All Words](https://leetcode.com/problems/substring-with-concatenation-of-all-words/)
  - Pattern: _[Which variant?]_
  - Key insight: _[Fill in after solving]_

### Problem-Solving Template

For each problem:

```markdown
## Problem: [Name]

**Pattern identified:** _[Which hash table variant?]_

**Why hash table?** _[What makes this a hash table problem?]_

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
  - [ ] Basic HashMap: two sum, frequency counting, contains duplicate all work
  - [ ] HashSet: intersection, missing number, longest consecutive all work
  - [ ] Grouping: group anagrams, custom grouping all work
  - [ ] Window: minimum window substring, check inclusion work
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify when to use HashMap vs HashSet
  - [ ] Understand frequency counting pattern
  - [ ] Understand grouping pattern
  - [ ] Know when hash table beats other approaches

- [ ] **Problem Solving**
  - [ ] Solved 4 easy problems
  - [ ] Solved 3-4 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Understood collision handling (conceptually)

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use hash tables
  - [ ] Can explain trade-offs vs other approaches

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand hash table internals (basic level)

---

**Next Topic:** [04. Linked Lists →](04-linked-lists.md)

**Back to:** [02. Sliding Window ←](02-sliding-window.md)
