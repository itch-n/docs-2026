# Hash Tables

> O(1) average lookup, insertion, and deletion using key-value mapping

---

## Learning Objectives

By the end of this topic you will be able to:

- Explain how a hash function maps keys to bucket indices and why this enables O(1) average lookups
- Implement four core patterns: basic lookup, set membership, grouping, and sliding window state tracking
- Design appropriate keys for grouping problems (e.g., sorted-character keys for anagram grouping)
- Diagnose and fix common bugs: `NullPointerException` from missing `getOrDefault`, same-index reuse in Two Sum, and stale zero-frequency keys
- Articulate the O(n) worst-case scenario caused by hash collisions and explain what a good hash function prevents
- Choose between HashMap, HashSet, and array-based frequency tables based on key type and space constraints

---

!!! note "Operational reality"
    HashDoS — crafting inputs that all hash to the same bucket, forcing O(n) lookup — is a real attack. Python randomised hash seeds per-process from 3.3 onward, Ruby did the same in 1.9, and most modern runtimes followed. The attack works against any system that exposes hash-derived behaviour externally, which is why you should never use raw hash values in URLs or API responses. Git uses SHA hashes to deduplicate objects in its content-addressable object store — a commit, tree, and blob are all just hash table lookups. Database query planners use hash joins as an alternative to nested-loop joins when the inner table fits in memory.

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is a hash table in one sentence?**
    - Your answer: <span class="fill-in">[A hash table is a data structure that converts a key into an index using a hash function so that ___, giving O(1) average lookup instead of ___]</span>

2. **Why is O(1) lookup possible?**
    - Your answer: <span class="fill-in">[Instead of scanning the entire collection, the hash function tells you ___ directly, so the lookup cost does not grow with ___]</span>

3. **Real-world analogy:**
    - Example: "Hash tables are like a library card catalog..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **When does this pattern work?**
    - Your answer: <span class="fill-in">[Fill in after solving problems]</span>

5. **What happens when two keys hash to the same location?**
    - Your answer: <span class="fill-in">[Fill in after learning about collisions]</span>

</div>

---

## Core Implementation

### Pattern 1: Basic Hash Map Operations

**Concept:** Fast lookups using key-value pairs.

**Use case:** Frequency counting, two-sum problems, duplicate detection.

```java
--8<-- "com/study/dsa/hashtables/BasicHashMap.java"
```


!!! warning "Debugging Challenge — Broken Two Sum"
    The code below has **2 bugs**. Test with `nums = [3, 2, 4], target = 6` before checking the answer.

    ```java
    public static int[] twoSum_Buggy(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);    }
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] {map.get(complement), i};        }
        }
        return new int[] {-1, -1};
    }
    ```

    - Bug 1: <span class="fill-in">[What's the structural inefficiency in the two-pass approach?]</span>
    - Bug 2: <span class="fill-in">[What if nums[i] + nums[i] == target? Which index is returned twice?]</span>

    ??? success "Answer"
        **Bug 1:** Building the entire map first and then searching in a second pass allows `map.get(complement)` to return the same index `i` when `complement == nums[i]` (e.g., `nums = [3, 2, 4], target = 6` — index 0 maps to `3`, and `6 - 3 = 3` finds itself).

        **Bug 2:** The same-index problem: add a guard `map.get(complement) != i`.

        **Best fix — check before adding:**
        ```java
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] {map.get(complement), i};
            }
            map.put(nums[i], i);  // Add AFTER checking
        }
        ```
        Adding after checking eliminates the same-index problem entirely without extra guards.

---

### Pattern 2: Hash Set for Fast Membership Testing

**Concept:** Use HashSet for O(1) membership checks.

**Use case:** Finding missing numbers, intersection/union operations.

```java
--8<-- "com/study/dsa/hashtables/HashSetOperations.java"
```


---

### Pattern 3: Hash Map for Grouping

**Concept:** Group elements by a computed key.

**Use case:** Anagrams, group by property, categorization.

```java
--8<-- "com/study/dsa/hashtables/HashMapGrouping.java"
```


!!! warning "Debugging Challenge — Broken Group Anagrams Key"
    The code below groups words but gives wrong results. Test with `["eat", "tea", "tan", "ate", "nat", "bat"]` before checking the answer.

    ```java
    public static List<List<String>> groupAnagrams_Buggy(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String s : strs) {
            String key = s.toLowerCase();
            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<>());
            }
            groups.get(key).add(s);
        }
        return new ArrayList<>(groups.values());
    }
    ```

    - Bug: <span class="fill-in">[What property must the key encode that `toLowerCase()` misses?]</span>

    ??? success "Answer"
        **Bug:** `toLowerCase()` does not change character order. `"eat"` and `"tea"` lowercase to `"eat"` and `"tea"` — still different keys. Anagrams share the **same characters in the same frequencies**, which is captured by sorting the characters.

        **Fix:**
        ```java
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        String key = new String(chars);
        ```
        Now `"eat"`, `"tea"`, and `"ate"` all produce the key `"aet"`.

---

### Pattern 4: Hash Map for Sliding Window with Constraints

**Concept:** Track window state using frequency map.

**Use case:** Substring problems with character constraints.

```java
--8<-- "com/study/dsa/hashtables/HashMapWindow.java"
```


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
| n = 100    | 10,000 ops          | 100 ops        | 100x    |
| n = 1,000  | 1,000,000 ops       | 1,000 ops      | 1,000x  |
| n = 10,000 | 100,000,000 ops     | 10,000 ops     | 10,000x |

**Your calculation:** For n = 5,000, the speedup is approximately _____ times faster.

#### Why Does HashMap Work?

!!! note "The complement lookup insight"
    Instead of checking every pair `(nums[i], nums[j])`, for each element we ask "does its complement already exist in the map?" Because `map.containsKey()` is O(1) on average, the inner loop is eliminated entirely. The map acts as a memory of everything seen so far, so finding the complement is instant.

In array `[2, 7, 11, 15]` looking for sum = 9:

```
Step 1: num=2, complement=7, map={} → not found, add 2→0
Step 2: num=7, complement=2, map={2→0} → FOUND! Return [0, 1]
```

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- Why does O(1) lookup matter? <span class="fill-in">[Your answer]</span>
- What's the space/time trade-off? <span class="fill-in">[Your answer]</span>

</div>

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
| n = 100    | 10,000 ops            | 100 ops        | 100x    |
| n = 1,000  | 1,000,000 ops         | 1,000 ops      | 1,000x  |
| n = 10,000 | 100,000,000 ops       | 10,000 ops     | 10,000x |

**Key insight:**

- HashSet remembers what we've seen in O(1) time
- No need to repeatedly search through previous elements
- Trade memory for speed!

**After implementing, answer:**

<div class="learner-section" markdown>

- When is the space trade-off worth it? <span class="fill-in">[Your answer]</span>
- When might you prefer the O(1) space solution? <span class="fill-in">[Your answer]</span>

</div>

---

## Common Misconceptions

!!! danger "Misconception 1: HashMap lookup is always O(1)"
    HashMap lookup is O(1) **on average**, assuming a good hash function. If all keys collide into one bucket (see the `BadHashCode` challenge), every operation degrades to O(n) because the bucket becomes a linear list. In practice, Java's HashMap uses tree-based buckets after a threshold, improving worst case to O(log n), but this is still far from O(1).

!!! danger "Misconception 2: `freq.get(c)` is safe for the first occurrence"
    `HashMap.get()` returns `null` when a key is absent — it does not return 0. Calling `freq.get(c) + 1` on a missing key throws a `NullPointerException`. Always use `freq.getOrDefault(c, 0)` or check `containsKey` first. This is the single most common hash map bug.

!!! danger "Misconception 3: HashMap and HashSet maintain insertion order"
    Standard `HashMap` and `HashSet` do **not** guarantee ordering. If you need insertion order, use `LinkedHashMap` or `LinkedHashSet`. If you need sorted order, use `TreeMap` or `TreeSet`. Confusing these leads to non-deterministic iteration bugs that are hard to reproduce.

!!! warning "When it breaks"
    Hash tables break for ordered operations: iterating in sorted order requires a separate structure (tree map or sorted array). They break under adversarial inputs without hash randomisation — crafted inputs that all collide in the same bucket degrade O(1) lookup to O(n). For very small maps (under ~10 keys), array linear scan is often faster due to cache locality. The complement lookup pattern (Two Sum) breaks when the same index cannot be reused — you must check `index != i` before returning, which the naive implementation omits.

---

## Decision Framework

<div class="learner-section" markdown>

**Your task:** Build decision trees for when to use hash tables.

### Question 1: What operation do you need?

Answer after solving problems:

- **Fast lookup by key?** <span class="fill-in">[When to use HashMap vs array?]</span>
- **Fast membership test?** <span class="fill-in">[When to use HashSet?]</span>
- **Frequency counting?** <span class="fill-in">[Why is HashMap ideal?]</span>
- **Your observation:** <span class="fill-in">[Fill in based on testing]</span>

### Question 2: What are the time/space trade-offs?

Answer for each pattern:

**HashMap for lookups:**

- Time complexity: <span class="fill-in">[Average case? Worst case?]</span>
- Space complexity: <span class="fill-in">[How much extra space?]</span>
- Best use cases: <span class="fill-in">[List problems you solved]</span>

**HashSet for membership:**

- Time complexity: <span class="fill-in">[Compare to linear search]</span>
- Space complexity: <span class="fill-in">[Trade-off worth it when?]</span>
- Best use cases: <span class="fill-in">[List problems you solved]</span>

**HashMap for grouping:**

- Time complexity: <span class="fill-in">[Depends on what?]</span>
- Space complexity: <span class="fill-in">[How to estimate?]</span>
- Best use cases: <span class="fill-in">[List problems you solved]</span>

### Your Decision Tree

Build this after solving practice problems:
```mermaid
flowchart LR
    Start["Hash Table Pattern Selection"]

    Q1{"What do you need?"}
    Start --> Q1
    Q2{"Fast lookup by key?"}
    Q3{"Fast membership test?"}
    Q4{"Count frequencies?"}
    Q5{"Group by property?"}
    Q6{"Track window state?"}
    Q7{"Space constraint?"}
    Start --> Q7
    N8["Consider alternatives"]
    Q7 -->|"Yes"| N8
    N9["Hash table is usually best choice"]
    Q7 -->|"No"| N9
```

</div>

---

## Practice

<div class="learner-section" markdown>

### LeetCode Problems

**Easy (Complete all 4):**

- [ ] [1. Two Sum](https://leetcode.com/problems/two-sum/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [217. Contains Duplicate](https://leetcode.com/problems/contains-duplicate/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [242. Valid Anagram](https://leetcode.com/problems/valid-anagram/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [349. Intersection of Two Arrays](https://leetcode.com/problems/intersection-of-two-arrays/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Medium (Complete 3-4):**

- [ ] [49. Group Anagrams](https://leetcode.com/problems/group-anagrams/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>
    - Mistake made: <span class="fill-in">[Fill in if any]</span>

- [ ] [128. Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [560. Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Prefix sum + HashMap]</span>

- [ ] [387. First Unique Character in a String](https://leetcode.com/problems/first-unique-character-in-a-string/)
    - Pattern: <span class="fill-in">[Which one?]</span>
    - Comparison: <span class="fill-in">[Two-pass vs one-pass?]</span>

**Hard (Optional):**

- [ ] [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
    - Pattern: <span class="fill-in">[Sliding window + HashMap]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [30. Substring with Concatenation of All Words](https://leetcode.com/problems/substring-with-concatenation-of-all-words/)
    - Pattern: <span class="fill-in">[Which variant?]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

**Failure modes:**

- What happens when two keys collide at extreme scale — does your HashMap still return correct values, and what is the actual worst-case time complexity when all n keys hash to the same bucket? <span class="fill-in">[Fill in]</span>
- How does your implementation behave when `getOrDefault` is replaced by a raw `get` call and the key is absent for the first time — specifically, what runtime error occurs and on which line? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these questions without looking at your notes. Write a sentence or two for each.

1. **Two Sum can be solved with O(n²) time and O(1) space (nested loops) or O(n) time and O(n) space (HashMap). Describe a real scenario where you would deliberately choose the slower O(n²) approach, and justify your choice.**

    ??? success "Rubric"
        A complete answer addresses: (1) the clearest justification is extreme memory pressure — if n is large and the system has very limited RAM (embedded systems, memory-mapped files), the O(n) extra space for a HashMap may be impractical while O(1) space is free; (2) a second valid scenario is when the array is extremely small (n < 10) — the constant-factor overhead of HashMap (boxing, hashing, bucket allocation) makes nested loops faster in practice; (3) a strong answer also notes that if the array is sorted, two pointers give O(n) time AND O(1) space, making the HashMap trade-off moot.

2. **Your `groupAnagrams` implementation passes most tests but fails on `["", ""]`. Trace through what happens with empty strings and identify the bug. What does the sorted empty string key look like?**

    ??? success "Rubric"
        A complete answer addresses: (1) sorting an empty char array produces an empty char array, and `new String(new char[]{})` produces `""` — so both empty strings produce the same key `""` and should correctly group together; (2) if the implementation fails, the most likely bug is that it returns all groups including a group for `""` but the test expects them merged — the algorithm itself is correct; (3) a subtler bug is using `s.toCharArray()` on an empty string, then calling `Arrays.sort`, then trying to access the array — this is safe in Java because `Arrays.sort` on a zero-length array is a no-op; the real check is whether the grouping map handles duplicate keys correctly.

3. **Explain what happens to HashMap performance when a custom object is used as a key but only `equals()` is overridden without `hashCode()`. What is the Java contract between `equals` and `hashCode`, and what breaks when you violate it?**

    ??? success "Rubric"
        A complete answer addresses: (1) Java's contract: if `a.equals(b)` is true, then `a.hashCode() == b.hashCode()` must also be true — violating this means two logically equal objects hash to different buckets; (2) without overriding `hashCode`, Java uses the identity hash (memory address), so two distinct objects that are `equals` land in different buckets — `map.get(equalKey)` returns `null` even though the key is logically present; (3) the symptom is that `containsKey` returns false for an equal-but-not-identical object, causing silent logic errors rather than exceptions.

4. **Longest Consecutive Sequence (LeetCode 128) requires O(n) time. Why does storing all numbers in a HashSet and then only starting counts from sequence-start elements achieve O(n) even though each sequence is traversed fully? Why doesn't this give O(n²) in the worst case?**

    ??? success "Rubric"
        A complete answer addresses: (1) a number `n` is a sequence-start only if `n-1` is NOT in the set — this check is O(1); (2) each number in the array is visited at most twice: once to check if it is a start, and at most once as part of counting from a start; the total work across all sequences is O(n), not O(n) per sequence; (3) the key insight is that the inner `while` loop only runs for sequence-start elements — non-start elements are never inner-loop heads, so the amortised cost per element is O(1).

5. **You are implementing a frequency counter using a `HashMap<Character, Integer>`. A colleague suggests using an `int[26]` array instead. Under what conditions is the array strictly better, and what does it assume about the input?**

    ??? success "Rubric"
        A complete answer addresses: (1) the array is strictly better when the key space is small and known at compile time — for lowercase ASCII letters, `int[26]` has O(1) access with no boxing, no hashing, and no collision handling; (2) the array assumes the input contains only lowercase English letters (or whatever range it covers) — passing a character outside this range causes an `ArrayIndexOutOfBoundsException`; (3) the HashMap is necessary when the key space is large, unknown, or non-integer (e.g., Unicode strings, arbitrary objects), or when you need to iterate only over keys that actually appeared rather than all 26 slots.

---

## Connected Topics

<div class="bs-callout bs-callout-info" markdown>

**Where this topic connects**

- **02. Sliding Window** — HashMap frequency counting is the standard inner data structure for variable-size sliding window problems → [02. Sliding Window](02-sliding-window.md)
- **14. Prefix Sums** — HashMap + prefix sum is the canonical pattern for subarray-sum-equals-k; the hash table stores prefix sum frequencies → [14. Prefix Sums](14-prefix-sums.md)
- **13. Dynamic Programming** — bottom-up DP often uses a HashMap to memoize subproblems when keys are non-integer or sparse → [13. Dynamic Programming](13-dynamic-programming.md)

</div>
