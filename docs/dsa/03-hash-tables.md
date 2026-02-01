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

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Linear search through array to find if element exists:**
    - Time complexity: _[Your guess: O(?)]_
    - Verified after learning: _[Actual: O(?)]_

2. **Hash table lookup to find if element exists:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified: _[Actual]_

3. **Speedup calculation:**
    - If n = 1,000, linear search = n = _____ operations
    - Hash table lookup = _____ operations (average case)
    - Speedup factor: _____ times faster

### Scenario Predictions

**Scenario 1:** Count frequency of each word in `["cat", "dog", "cat", "bird", "dog", "cat"]`

- **Can you use a hash map?** _[Yes/No - Why?]_
- **What would be the key?** _[Fill in]_
- **What would be the value?** _[Fill in]_
- **What's the final map for "cat"?** _[Key: "cat", Value: ?]_

**Scenario 2:** Find two numbers that sum to 10 in `[2, 7, 11, 15]`

- **Can you use a hash map?** _[Yes/No - Why?]_
- **What would you store in the map?** _[Fill in]_
- **How does hash map help vs nested loops?** _[Fill in]_

**Scenario 3:** Group anagrams: `["eat", "tea", "tan", "ate", "nat", "bat"]`

- **What makes two strings anagrams?** _[Fill in]_
- **What should be the map key?** _[Fill in your idea]_
- **How to create the key from "eat"?** _[Fill in]_

### Hash Collision Quiz

**Question:** What happens when two different keys hash to the same location?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question:** Why is HashMap lookup O(1) average but O(n) worst case?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

### Trade-off Quiz

**Question:** When would sorting + binary search be BETTER than using a HashMap?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question:** What's the MAIN advantage of HashMap over arrays?

- [ ] Always uses less memory
- [ ] Can use non-integer keys
- [ ] Maintains sorted order
- [ ] Always faster for all operations

Verify after implementation: _[Which one(s)?]_

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand the impact.

### Example: Two Sum Problem

**Problem:** Find two numbers in an array that sum to a target value.

#### Approach 1: Brute Force (Nested Loops)

```java
// Naive approach - Check all possible pairs
public static int[] twoSum_BruteForce(int[] nums, int target) {
    for (int i = 0; i < nums.length; i++) {
        for (int j = i + 1; j < nums.length; j++) {
            if (nums[i] + nums[j] == target) {
                return new int[] {i, j};
            }
        }
    }
    return new int[] {-1, -1};
}
```

**Analysis:**

- Time: O(n²) - For each element, check all remaining elements
- Space: O(1) - No extra space
- For n = 10,000: ~100,000,000 comparisons

#### Approach 2: HashMap (Optimized)

```java
// Optimized approach - Use HashMap for O(1) lookup
public static int[] twoSum_HashMap(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();

    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[] {map.get(complement), i};
        }
        map.put(nums[i], i);
    }

    return new int[] {-1, -1};
}
```

**Analysis:**

- Time: O(n) - Single pass through array, O(1) lookups
- Space: O(n) - Store up to n elements in map
- For n = 10,000: ~10,000 operations

#### Performance Comparison

| Array Size | Brute Force (O(n²)) | HashMap (O(n)) | Speedup |
|------------|---------------------|----------------|---------|
| n = 100    | 10,000 ops         | 100 ops        | 100x    |
| n = 1,000  | 1,000,000 ops      | 1,000 ops      | 1,000x  |
| n = 10,000 | 100,000,000 ops    | 10,000 ops     | 10,000x |

**Your calculation:** For n = 5,000, the speedup is approximately _____ times faster.

#### Why Does HashMap Work?

**Key insight to understand:**

In array `[2, 7, 11, 15]` looking for sum = 9:

```
Step 1: num=2, complement=7, map={} → not found, add 2→0
Step 2: num=7, complement=2, map={2→0} → FOUND! Return [0, 1]
```

**Why is this faster?**

- Instead of checking every pair (2,7), (2,11), (2,15), (7,11), (7,15), (11,15)
- We check each number once and use O(1) lookup to find its complement
- HashMap eliminates the need for the inner loop!

**After implementing, explain in your own words:**

- _[Why does O(1) lookup matter?]_
- _[What's the space/time trade-off?]_

---

### Example: Finding Duplicates

**Problem:** Check if an array contains any duplicate values.

#### Approach 1: Linear Search for Each Element

```java
// Naive approach - For each element, search rest of array
public static boolean containsDuplicate_LinearSearch(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
        for (int j = i + 1; j < nums.length; j++) {
            if (nums[i] == nums[j]) {
                return true;
            }
        }
    }
    return false;
}
```

**Analysis:**

- Time: O(n²) - Nested loops
- Space: O(1) - No extra space

#### Approach 2: HashSet (Optimized)

```java
// Optimized approach - Use HashSet for O(1) membership test
public static boolean containsDuplicate_HashSet(int[] nums) {
    Set<Integer> seen = new HashSet<>();

    for (int num : nums) {
        if (seen.contains(num)) {
            return true;  // Found duplicate!
        }
        seen.add(num);
    }

    return false;
}
```

**Analysis:**

- Time: O(n) - Single pass, O(1) contains/add operations
- Space: O(n) - Store up to n elements

#### Performance Comparison

| Array Size | Linear Search (O(n²)) | HashSet (O(n)) | Speedup |
|------------|-----------------------|----------------|---------|
| n = 100    | 10,000 ops           | 100 ops        | 100x    |
| n = 1,000  | 1,000,000 ops        | 1,000 ops      | 1,000x  |
| n = 10,000 | 100,000,000 ops      | 10,000 ops     | 10,000x |

**Key insight:**

- HashSet remembers what we've seen in O(1) time
- No need to repeatedly search through previous elements
- Trade memory for speed!

**After implementing, answer:**

- _[When is the space trade-off worth it?]_
- _[When might you prefer the O(1) space solution?]_

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

## Debugging Challenges

**Your task:** Find and fix bugs in broken implementations. This tests your understanding of hash tables.

### Challenge 1: Broken Two Sum with HashMap

```java
/**
 * This code is supposed to find two numbers that sum to target.
 * It has 2 BUGS. Find them!
 */
public static int[] twoSum_Buggy(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();

    for (int i = 0; i < nums.length; i++) {
        map.put(nums[i], i);  // BUG 1: When should we add to map?
    }

    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[] {map.get(complement), i};  // BUG 2: Edge case issue
        }
    }

    return new int[] {-1, -1};
}
```

**Your debugging:**

- **Bug 1 location:** _[Which line?]_
- **Bug 1 explanation:** _[Why separate loops? What's inefficient?]_
- **Bug 1 fix:** _[How to combine into one loop?]_

- **Bug 2 location:** _[Which line?]_
- **Bug 2 explanation:** _[What if nums[i] + nums[i] = target? Same index used twice!]_
- **Bug 2 fix:** _[How to check indices are different?]_

**Test case to expose Bug 2:**

- Input: `nums = [3, 2, 4], target = 6`
- Expected: `[1, 2]` (indices of 2 and 4)
- Buggy output: _[What happens if we check 3+3?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** Two separate loops are inefficient (though not technically wrong). Better to check and add in single loop.

**Bug 2:** Could return `[i, i]` if the same element appears twice. Fix:
```java
if (map.containsKey(complement) && map.get(complement) != i) {
    return new int[] {map.get(complement), i};
}
map.put(nums[i], i);
```

**Better solution - check BEFORE adding:**
```java
for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (map.containsKey(complement)) {
        return new int[] {map.get(complement), i};
    }
    map.put(nums[i], i);  // Add after checking
}
```
</details>

---

### Challenge 2: Broken Frequency Counter

```java
/**
 * Count character frequencies.
 * This has 1 NULL POINTER BUG.
 */
public static Map<Character, Integer> countChars_Buggy(String s) {
    Map<Character, Integer> freq = new HashMap<>();

    for (char c : s.toCharArray()) {
        int count = freq.get(c);  // BUG: What if c not in map yet?
        freq.put(c, count + 1);
    }

    return freq;
}
```

**Your debugging:**

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[What error occurs?]_
- **Bug fix:** _[Two different ways to fix]_

**Test case:**

- Input: `"hello"`
- Expected: `{h=1, e=1, l=2, o=1}`
- Buggy output: _[What exception?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug:** `freq.get(c)` returns `null` for first occurrence, causing `NullPointerException` when adding 1.

**Fix Option 1 - Use getOrDefault:**
```java
int count = freq.getOrDefault(c, 0);
freq.put(c, count + 1);
```

**Fix Option 2 - Check containsKey:**
```java
if (freq.containsKey(c)) {
    freq.put(c, freq.get(c) + 1);
} else {
    freq.put(c, 1);
}
```

**Fix Option 3 - Use compute:**
```java
freq.compute(c, (key, val) -> val == null ? 1 : val + 1);
```
</details>

---

### Challenge 3: Broken Group Anagrams

```java
/**
 * Group anagrams together.
 * This has a LOGIC BUG with the key generation.
 */
public static List<List<String>> groupAnagrams_Buggy(String[] strs) {
    Map<String, List<String>> groups = new HashMap<>();

    for (String s : strs) {
        String key = s.toLowerCase();  // BUG: Is lowercase enough for anagrams?

        if (!groups.containsKey(key)) {
            groups.put(key, new ArrayList<>());
        }
        groups.get(key).add(s);
    }

    return new ArrayList<>(groups.values());
}
```

**Your debugging:**

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[What makes strings anagrams? Same letters in different order!]_
- **Bug fix:** _[How to create a key that's same for all anagrams?]_

**Test case:**

- Input: `["eat", "tea", "tan", "ate", "nat", "bat"]`
- Expected: `[["eat", "tea", "ate"], ["tan", "nat"], ["bat"]]`
- Buggy output: _[Each word in its own group!]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Lowercase doesn't make anagrams have the same key. "eat" and "tea" are different when lowercased.

**Fix - Sort characters:**
```java
char[] chars = s.toCharArray();
Arrays.sort(chars);
String key = new String(chars);
```

Now "eat", "tea", and "ate" all become "aet" when sorted!
</details>

---

### Challenge 4: Hash Collision Awareness

```java
/**
 * This code works but has PERFORMANCE issues due to collisions.
 * Identify the problem.
 */
public static class BadHashCode {
    private String name;
    private int age;

    @Override
    public int hashCode() {
        return 42;  // BUG: Why is this terrible?
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BadHashCode)) return false;
        BadHashCode other = (BadHashCode) o;
        return this.name.equals(other.name) && this.age == other.age;
    }
}
```

**Your debugging:**

- **Bug:** _[What's wrong with always returning 42?]_
- **Performance impact:** _[What's the time complexity now?]_
- **Explanation:** _[How does HashMap work with this hashCode?]_

**Real-world scenario:**

- HashMap with 10,000 BadHashCode objects
- Expected lookup: O(1)
- Actual lookup: _[What happens?]_

<details markdown>
<summary>Click to verify your understanding</summary>

**Problem:** All objects hash to the same bucket (42), creating a massive collision.

**Performance:** HashMap degrades to O(n) for all operations because all entries are in one linked list/tree.

**Why it's bad:**

- HashMap has many buckets (default 16, grows to thousands)
- All 10,000 objects go into ONE bucket
- Lookup requires scanning through all 10,000 objects
- Defeats the entire purpose of hashing!

**Correct implementation:**
```java
@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

**Key lesson:** Good hash functions distribute objects evenly across buckets.
</details>

---

### Challenge 5: Missing Null Checks

```java
/**
 * Find intersection of two arrays.
 * This has MULTIPLE null-safety bugs.
 */
public static int[] intersection_Buggy(int[] nums1, int[] nums2) {
    Set<Integer> set1 = new HashSet<>();

    for (int num : nums1) {  // BUG 1: What if nums1 is null?
        set1.add(num);
    }

    Set<Integer> result = new HashSet<>();
    for (int num : nums2) {  // BUG 2: What if nums2 is null?
        if (set1.contains(num)) {
            result.add(num);
        }
    }

    return result.stream().mapToInt(i -> i).toArray();  // BUG 3: result could be empty
}
```

**Your debugging:**

- **Bug 1:** _[What happens if nums1 is null?]_
- **Bug 2:** _[What happens if nums2 is null?]_
- **Bug 3:** _[Is this actually a bug? Empty result valid?]_

**Fixes:**

- _[Should you check for null? Return empty array? Throw exception?]_
- _[Your defensive programming strategy]_

<details markdown>
<summary>Click to verify your approach</summary>

**Bugs 1 & 2:** NullPointerException if either array is null.

**Fix - Add null checks:**
```java
public static int[] intersection_Fixed(int[] nums1, int[] nums2) {
    if (nums1 == null || nums2 == null) {
        return new int[0];  // or throw IllegalArgumentException
    }

    // ... rest of implementation
}
```

**Bug 3:** Not actually a bug - empty result is valid when there's no intersection.

**Design decision:**

- Return empty array: Easier for caller, no exception handling
- Throw exception: Fail fast, makes null input a programmer error
- Which is better? Depends on your API design philosophy!
</details>

---

### Challenge 6: Longest Consecutive Sequence - Off By One

```java
/**
 * Find longest consecutive sequence.
 * This has a SUBTLE off-by-one bug.
 */
public static int longestConsecutive_Buggy(int[] nums) {
    Set<Integer> numSet = new HashSet<>();
    for (int num : nums) {
        numSet.add(num);
    }

    int maxLength = 0;

    for (int num : numSet) {
        if (!numSet.contains(num - 1)) {  // Start of sequence
            int currentNum = num;
            int currentLength = 1;

            while (numSet.contains(currentNum + 1)) {
                currentNum++;
                currentLength++;
            }

            maxLength = Math.max(maxLength, currentLength);
        }
    }

    return maxLength;  // BUG: What if nums is empty?
}
```

**Your debugging:**

- **Bug:** _[What's returned for empty array?]_
- **Expected:** _[What should be returned?]_
- **Fix:** _[Is 0 correct for empty array? Or should it be different?]_

**Test cases:**

- Input: `[]` → Expected: 0 or -1? _[Your decision]_
- Input: `[100, 4, 200, 1, 3, 2]` → Expected: 4 (sequence: 1,2,3,4)

<details markdown>
<summary>Click to verify</summary>

**Bug:** For empty array, returns 0. Is this a bug?

**Answer:** Depends on problem specification!
- Returning 0 is often correct (no elements = sequence length 0)
- Some problems might expect -1 or throw exception

**The code is actually CORRECT!** This was a trick question to make you think about edge cases.

**Real lesson:** Always verify edge case behavior matches problem requirements.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found the two-sum same-index bug
- [ ] Fixed null pointer in frequency counter (knew 2+ solutions)
- [ ] Corrected anagram key generation (sorted characters)
- [ ] Understood hash collision performance impact
- [ ] Added null checks for defensive programming
- [ ] Analyzed edge cases (empty array behavior)

**Common hash table mistakes you discovered:**

1. _[List the patterns you noticed]_
2. _[Fill in]_
3. _[Fill in]_

**Best practices learned:**

1. _[When to use getOrDefault vs containsKey?]_
2. _[Why check before adding to map in some cases?]_
3. _[How to handle null inputs?]_

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

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about hash tables.

**Your explanation (write it out):**

> "Hash tables are a data structure that..."
>
> _[Fill in your explanation in plain English - 3-4 sentences max]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by a non-technical person? _[Yes/No]_
- Did you use analogies or real-world examples? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw how HashMap solves Two Sum, without looking at code.

**Draw the data structure and operations:**

```
Array: [2, 7, 11, 15], Target: 9

Step 1: [Your drawing - show map state and current element]
        Map: {}
        Current: nums[0] = 2
        _________________________________

Step 2: [Your drawing - after adding to map]
        Map: {2 → 0}
        Current: nums[1] = 7
        _________________________________

Step 3: [Continue until found]
        _________________________________
```

**Verification:**

- [ ] Drew initial empty map correctly
- [ ] Showed key-value pairs accurately (what's key? what's value?)
- [ ] Explained the lookup operation
- [ ] Identified when solution is found

---

### Gate 3: Pattern Recognition Test

**Without looking at your notes, classify these problems:**

| Problem | Data Structure (HashMap/HashSet) | Why? |
|---------|----------------------------------|------|
| Count word frequency | _[Fill in]_ | _[Explain]_ |
| Find if array has duplicates | _[Fill in]_ | _[Explain]_ |
| Group strings by first letter | _[Fill in]_ | _[Explain]_ |
| Check if two strings are anagrams | _[Fill in]_ | _[Explain]_ |
| Find missing number from 1 to n | _[Fill in]_ | _[Explain]_ |
| Two sum | _[Fill in]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the patterns and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Operation | HashMap Time | HashMap Space | HashSet Time | HashSet Space |
|-----------|--------------|---------------|--------------|---------------|
| Add/Put | O(?) | O(?) | O(?) | O(?) |
| Lookup/Contains | O(?) | O(?) | O(?) | O(?) |
| Remove | O(?) | O(?) | O(?) | O(?) |

**Deep question:** Why is hash table lookup O(1) average but O(n) worst case?

Your answer: _[Fill in - explain hash collisions and bucket distribution]_

---

### Gate 5: Trade-off Decision

**Scenario:** You need to find duplicate elements in an array of 1 million integers.

**Option A:** Sort first (O(n log n)), then scan for adjacent duplicates (O(n))
- Total complexity: _[Fill in]_
- Space complexity: _[Fill in - in-place sort vs new array]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option B:** Use HashSet (O(n) to build and check)
- Total complexity: _[Fill in]_
- Space complexity: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Your decision:** I would choose _[A/B]_ because...

_[Fill in your reasoning - consider time, space, and constraints]_

**What would make you change your decision?**

- _[Fill in - what constraints would flip your choice?]_

---

### Gate 6: Hash Collision Understanding

**Scenario:** Explain hash collisions to someone who's never used hash tables.

**Your explanation:**

> "A hash collision happens when..."
>
> _[Fill in - explain what happens and how it's handled]_

**Follow-up questions to answer:**

1. What's a good hash function? _[Fill in]_
2. How does Java's HashMap handle collisions? _[Fill in - linked list/tree]_
3. What happens to performance with many collisions? _[Fill in]_

**Verification:**

- [ ] Can explain hash collisions in simple terms
- [ ] Understand impact on time complexity
- [ ] Know how Java resolves collisions (at least conceptually)

---

### Gate 7: Code from Memory (Final Test)

**Set a 10-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: Group anagrams together
 * Input: ["eat", "tea", "tan", "ate", "nat", "bat"]
 * Output: [["eat","tea","ate"], ["tan","nat"], ["bat"]]
 */
public static List<List<String>> groupAnagrams(String[] strs) {
    // Your implementation here




    return new ArrayList<>(); // Replace
}
```

**After implementing, test with:**

- Input: `["eat", "tea", "tan", "ate", "nat", "bat"]`
- Expected: `[["eat","tea","ate"], ["tan","nat"], ["bat"]]` (order may vary)

**Verification:**

- [ ] Implemented correctly without looking
- [ ] Handles edge cases (empty array, single string)
- [ ] Time complexity is O(n * k log k) where k = max string length
- [ ] Space complexity is O(n * k)

---

### Gate 8: When NOT to Use Hash Tables

**The ultimate test of understanding is knowing limitations.**

**Task:** Explain to an imaginary person when NOT to use hash tables.

Your explanation:

> "You should NOT use hash tables when..."
>
> _[Fill in - list 3-4 scenarios and explain why]_

**Examples of when it fails or is suboptimal:**

1. _[Scenario where hash table doesn't work - e.g., need sorted order]_
2. _[Scenario where another approach is better - e.g., small fixed dataset]_
3. _[Scenario where space is critical - e.g., embedded systems]_
4. _[Fill in]_

---

### Gate 9: Real-World Application

**Scenario:** Design a simple cache for web requests.

**Requirements:**

- Store recent API responses (URL → response data)
- Fast lookup: Given a URL, return cached response if exists
- Limited capacity: Only cache last 100 requests

**Your design:**

**Data structure choice:** _[HashMap? Why?]_

**Key:** _[What's the key? URL string?]_

**Value:** _[What's the value? Response object?]_

**How to handle capacity limit:** _[Fill in - what happens when 101st item added?]_

**Code outline:**
```java
// Your design
class SimpleCache {
    // What fields?

    // What methods?
}
```

**Alternative considered:** _[What other data structure? Why not use it?]_

---

### Gate 10: Teaching Check

**Task:** Create a simple analogy for hash tables that a 10-year-old would understand.

Your analogy:

> "Imagine you have a huge toy box..."
>
> _[Fill in your complete analogy - make it relatable and accurate]_

**Elements to include:**

- [ ] What's the "key"? (toy name, category?)
- [ ] What's the "value"? (the actual toy?)
- [ ] How do you find things fast? (organized sections?)
- [ ] What's a collision? (two toys in same spot?)

**Test it:** Would a non-programmer understand this? _[Yes/No]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement HashMap and HashSet patterns from memory
- [ ] Explain when and why to use each data structure
- [ ] Identify the correct pattern for new problems
- [ ] Analyze time and space complexity (including collision impact)
- [ ] Compare trade-offs with alternative approaches
- [ ] Debug common hash table mistakes (null checks, collisions, key design)
- [ ] Teach this concept to someone else using analogies
- [ ] Design real-world systems using hash tables

**Self-assessment score:** ___/10

**What I still need to work on:**

1. _[Fill in areas of weakness]_
2. _[Fill in]_
3. _[Fill in]_

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered hash tables. Proceed to the next topic.
