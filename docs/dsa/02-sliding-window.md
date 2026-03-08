# Sliding Window

> Optimize subarray/substring problems from O(n²) to O(n)

---

## Learning Objectives

By the end of this topic you will be able to:

- Explain why sliding window eliminates redundant recalculation compared to nested loops
- Implement both fixed-size and dynamic-size window patterns
- Track window state using variables, HashSet, HashMap, or frequency arrays as appropriate
- Distinguish between sliding window and two-pointer techniques and select the right one
- Identify the shrink condition that determines when a dynamic window must contract
- Analyse time and space complexity for all four window variants

---

!!! note "Operational reality"
    TCP's congestion control is a sliding window over packets in flight — the window expands on success and shrinks on loss, which is exactly the variable-size shrinking logic from these exercises. Redis's built-in rate limiting primitives use the sliding window counter algorithm directly. Kafka Streams and Apache Flink both model time-based aggregations as windows over event streams; the "tumbling window" and "sliding window" in those systems are the same concept scaled to distributed infrastructure. Network monitoring tools that compute rolling metrics (p99 latency over the last 60 seconds) are sliding windows over time-series data.

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is the sliding window pattern in one sentence?**
    - Your answer: <span class="fill-in">[Instead of recalculating a subarray from scratch each time, sliding window ___ by removing the element that left the window and ___ the element that just entered, so each step costs ___ instead of O(k)]</span>

2. **How is it different from two pointers?**
    - Your answer: <span class="fill-in">[Two pointers typically searches for a pair satisfying a condition, while sliding window maintains ___ and tracks ___ within it; the key distinction is ___]</span>

3. **Real-world analogy:**
    - Example: "Sliding window is like a camera viewfinder moving across a scene..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **When does this pattern work?**
    - Your answer: <span class="fill-in">[Fill in after solving problems]</span>

5. **When does this pattern fail?**
    - Your answer: <span class="fill-in">[Fill in after trying non-contiguous problems]</span>

</div>

---

## Core Implementation

### Pattern 1: Fixed Window Size

**Concept:** Window size is constant, slide one position at a time.

**Use case:** Maximum/minimum of k consecutive elements, average of subarrays.

```java
--8<-- "com/study/dsa/slidingwindow/FixedWindow.java"
```


!!! warning "Debugging Challenge — Broken Fixed Window (Max Average)"
    The code below has **2 bugs**. Trace through `nums = [1, 12, -5, -6, 50, 3]`, `k = 4` before checking the answer.

    ```java
    public static double maxAverage_Buggy(int[] nums, int k) {
        int windowSum = 0;
        for (int i = 0; i <= k; i++) {        windowSum += nums[i];
        }
        double maxAvg = windowSum / k;
        for (int i = k; i < nums.length; i++) {
            windowSum = windowSum - nums[i - k] + nums[i];
            maxAvg = Math.max(maxAvg, windowSum / k);    }
        return maxAvg;
    }
    ```

    - Bug 1: <span class="fill-in">[What's the bug?]</span>
    - Bug 2: <span class="fill-in">[What's the bug?]</span>

    ??? success "Answer"
        **Bug 1 (loop bound):** The first loop uses `i <= k`, which reads `k + 1` elements instead of `k`. It should be `i < k`.

        **Bug 2 (integer division):** `windowSum / k` performs integer division. Both occurrences must cast to double: `windowSum / (double) k`.

        **Correct first window:**
        ```java
        for (int i = 0; i < k; i++) {     // Fixed: i < k
            windowSum += nums[i];
        }
        double maxAvg = windowSum / (double) k;   // Fixed: cast
        ```

---

### Pattern 2: Dynamic Window Size

**Concept:** Window expands and contracts based on condition.

**Use case:** Longest/shortest substring with constraint, subarray sum.

```java
--8<-- "com/study/dsa/slidingwindow/DynamicWindow.java"
```


!!! warning "Debugging Challenge — `if` vs `while` in Window Shrinking"
    The code below uses an `if` to shrink the window. It compiles but produces wrong results. Trace through `nums = [2, 3, 1, 2, 4, 3]`, `target = 7` before checking the answer.

    ```java
    public static int minSubArrayLen_Buggy(int target, int[] nums) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            if (sum >= target) {            minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }
    ```

    - Bug: <span class="fill-in">[Why does `if` fail here where `while` would succeed?]</span>

    ??? success "Answer"
        **Bug:** The shrink condition must be `while (sum >= target)`, not `if`. After shrinking once, the window may still satisfy `sum >= target` and could shrink further to reveal a shorter valid subarray. Using `if` only shrinks once per `right` step, so the algorithm misses the minimum.

        **Correct:**
        ```java
        while (sum >= target) {
            minLen = Math.min(minLen, right - left + 1);
            sum -= nums[left];
            left++;
        }
        ```

        **Key principle:** Dynamic windows need `while` for shrinking whenever the validity condition can remain true after one shrink step.

---

### Pattern 3: String Problems with Window

**Concept:** Track character frequencies in window for pattern matching.

**Use case:** Anagram problems, substring with all characters.

```java
--8<-- "com/study/dsa/slidingwindow/StringWindow.java"
```


---

### Pattern 4: Two Pointers + Window Hybrid

**Concept:** Combine sliding window with two-pointer techniques.

**Use case:** Character replacement, fruit baskets, longest repeating replacement.

```java
--8<-- "com/study/dsa/slidingwindow/HybridWindow.java"
```


---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand the impact.

### Example: Maximum Sum of K Consecutive Elements

**Problem:** Find the maximum sum of any k consecutive elements in an array.

#### Approach 1: Brute Force (Nested Loops)

```java
// Naive approach - Recalculate sum for each window
public static int maxSum_BruteForce(int[] nums, int k) {
    if (nums.length < k) return 0;

    int maxSum = Integer.MIN_VALUE;

    // For each possible starting position
    for (int i = 0; i <= nums.length - k; i++) {
        int windowSum = 0;

        // Calculate sum of k elements starting at i
        for (int j = i; j < i + k; j++) {
            windowSum += nums[j];
        }

        maxSum = Math.max(maxSum, windowSum);
    }

    return maxSum;
}
```

**Analysis:**

- Time: O(n × k) - For each of n-k positions, sum k elements
- Space: O(1) - No extra space
- For n = 10,000, k = 100: ~1,000,000 operations

#### Approach 2: Sliding Window (Optimized)

```java
// Optimized approach - Reuse previous sum by sliding
public static int maxSum_SlidingWindow(int[] nums, int k) {
    if (nums.length < k) return 0;

    // Calculate sum of first window
    int windowSum = 0;
    for (int i = 0; i < k; i++) {
        windowSum += nums[i];
    }
    int maxSum = windowSum;

    // Slide window: remove left, add right
    for (int i = k; i < nums.length; i++) {
        windowSum = windowSum - nums[i - k] + nums[i];  // KEY: Reuse previous sum!
        maxSum = Math.max(maxSum, windowSum);
    }

    return maxSum;
}
```

**Analysis:**

- Time: O(n) - One pass to build first window, one pass to slide
- Space: O(1) - Only track window sum
- For n = 10,000, k = 100: ~10,000 operations

#### Performance Comparison

| Array Size (n) | Window Size (k) | Brute Force (O(n×k)) | Sliding Window (O(n)) | Speedup |
|----------------|-----------------|----------------------|-----------------------|---------|
| n = 100        | k = 10          | 1,000 ops            | 100 ops               | 10x     |
| n = 1,000      | k = 100         | 100,000 ops          | 1,000 ops             | 100x    |
| n = 10,000     | k = 100         | 1,000,000 ops        | 10,000 ops            | 100x    |

**Your calculation:** For n = 5,000 and k = 50, the speedup is approximately _____ times faster.

#### Why Does Sliding Window Work?

!!! note "The reuse insight"
    Window 2 shares all but one element with Window 1. Sliding window **reuses** the previous sum rather than recalculating the shared portion. The only work per step is one subtraction (remove left element) and one addition (add right element) — constant time regardless of k.

In array `[1, 4, 2, 10, 2, 3]` with k = 3:

```
Window 1: [1, 4, 2]     sum = 7
Window 2:    [4, 2, 10]  sum = 16

Brute force: Calculate 4 + 2 + 10 = 16 (3 operations)
Sliding window: Previous sum (7) - 1 + 10 = 16 (2 operations)
```

**Visualization:**

```
[1, 4, 2, 10, 2, 3]
 ^-----^              Window 1: sum = 7
    ^-----^           Window 2: Remove 1, Add 10 → sum = 7 - 1 + 10 = 16
       ^-----^        Window 3: Remove 4, Add 2 → sum = 16 - 4 + 2 = 14
```

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- Why does the window "slide" instead of "jump"? <span class="fill-in">[Your answer]</span>
- What would happen if the window wasn't contiguous? <span class="fill-in">[Your answer]</span>
- How is this different from two pointers? <span class="fill-in">[Your answer]</span>

</div>

---

## Common Misconceptions

!!! danger "Misconception 1: Sliding window requires a sorted array"
    Unlike two-pointer pair-sum, sliding window has no sorting requirement. It works on any sequence because it relies only on **contiguity**, not on element order. `maxSlidingWindow`, `lengthOfLongestSubstring`, and `minSubArrayLen` all operate on unsorted input.

!!! danger "Misconception 2: You always need `while` to shrink a dynamic window"
    Use `while` when the constraint can remain satisfied after one shrink step (e.g., `sum >= target`). Use `if` when shrinking once is always sufficient to restore validity (e.g., removing the leftmost element of a fixed-size window). Choosing the wrong one is the single most common sliding window bug.

!!! danger "Misconception 3: Not removing zero-frequency keys from a HashMap breaks nothing"
    If you decrement a character's count to 0 but leave it in the map, `window.size()` never decreases. The shrink condition `window.size() > k` becomes permanently true, causing the window to collapse to size 1. Always remove a key when its frequency hits 0.

!!! warning "When it breaks"
    Sliding window requires a monotonic shrink condition: shrinking the window must make it strictly less valid (or vice versa). When this doesn't hold — for example, "longest subarray where max minus min ≤ k" — shrinking can restore validity and the standard shrink loop produces incorrect results. In those cases you need a data structure (monotonic deque) to track the max/min, which changes the approach entirely. The pattern also breaks for non-contiguous problems: two-sum and k-sum over unsorted data require a hash map, not a window.

---

## Decision Framework

<div class="learner-section" markdown>

**Your task:** Build decision trees for when to use sliding window.

### Question 1: Is the subarray/substring contiguous?

Answer after solving problems:

- **Why contiguous matters?** <span class="fill-in">[Sliding window only works on contiguous data]</span>
- **Can sliding window work on non-contiguous?** <span class="fill-in">[No - need other techniques]</span>
- **Your observation:** <span class="fill-in">[Fill in based on testing]</span>

### Question 2: Fixed vs Dynamic window?

Answer for each pattern:

**Fixed window when:**

- Window size: <span class="fill-in">[Known constant k]</span>
- Movement rule: <span class="fill-in">[Always move both pointers together]</span>
- Example problems: <span class="fill-in">[Max average, nearby duplicate]</span>

**Dynamic window when:**

- Window size: <span class="fill-in">[Varies based on constraint]</span>
- Movement rule: <span class="fill-in">[Expand right, shrink left when needed]</span>
- Example problems: <span class="fill-in">[Longest substring, min subarray sum]</span>

### Question 3: What state to track?

Answer for different scenarios:

**For sum/count problems:**

- Track: <span class="fill-in">[Running sum, count]</span>
- Data structure: <span class="fill-in">[Variables, no extra space]</span>

**For unique elements:**

- Track: <span class="fill-in">[Set of current elements]</span>
- Data structure: <span class="fill-in">[HashSet]</span>

**For frequency:**

- Track: <span class="fill-in">[Count of each element]</span>
- Data structure: <span class="fill-in">[HashMap or frequency array]</span>

### Your Decision Tree

Build this after solving practice problems:
```mermaid
flowchart LR
    Start["Sliding Window Pattern Selection"]

    Q1{"Is subarray/substring contiguous?"}
    Start --> Q1
    N2["Use other technique<br/>(DP, backtracking)"]
    Q1 -->|"NO"| N2
    N3["Continue"]
    Q1 -->|"YES"| N3
    Q4{"Is window size known?"}
    Start --> Q4
    N5(["Use fixed window ✓"])
    Q4 -->|"YES (fixed k)"| N5
    N6(["Use dynamic window ✓"])
    Q4 -->|"NO (find optimal)"| N6
    Q7{"What to track in window?"}
    Start --> Q7
    N8(["Variables O(1) space ✓"])
    Q7 -->|"Sum/Count"| N8
    N9(["HashSet O(k) space ✓"])
    Q7 -->|"Unique elements"| N9
    N10(["HashMap/Array O(k) space ✓"])
    Q7 -->|"Frequencies"| N10
    N11(["Deque O(k) space ✓"])
    Q7 -->|"Maximum"| N11
    Q12{"Shrink condition?"}
    Start --> Q12
```

</div>

---

## Practice

<div class="learner-section" markdown>

### LeetCode Problems

**Easy (Complete all 4):**

- [ ] [643. Maximum Average Subarray I](https://leetcode.com/problems/maximum-average-subarray-i/)
    - Pattern: <span class="fill-in">[Fixed window]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [219. Contains Duplicate II](https://leetcode.com/problems/contains-duplicate-ii/)
    - Pattern: <span class="fill-in">[Fixed window with HashSet]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [1876. Substrings of Size Three with Distinct Characters](https://leetcode.com/problems/substrings-of-size-three-with-distinct-characters/)
    - Pattern: <span class="fill-in">[Fixed window]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [1456. Maximum Number of Vowels](https://leetcode.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/)
    - Pattern: <span class="fill-in">[Fixed window]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Medium (Complete 3-4):**

- [ ] [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
    - Pattern: <span class="fill-in">[Dynamic window]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>
    - Mistake made: <span class="fill-in">[Fill in if any]</span>

- [ ] [424. Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/)
    - Pattern: <span class="fill-in">[Dynamic window with replacement]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [438. Find All Anagrams in a String](https://leetcode.com/problems/find-all-anagrams-in-a-string/)
    - Pattern: <span class="fill-in">[Fixed window with frequency]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [567. Permutation in String](https://leetcode.com/problems/permutation-in-string/)
    - Pattern: <span class="fill-in">[Fixed window with frequency]</span>
    - Comparison to 438: <span class="fill-in">[How similar?]</span>

**Hard (Optional):**

- [ ] [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
    - Pattern: <span class="fill-in">[Dynamic window with frequency]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [239. Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)
    - Pattern: <span class="fill-in">[Fixed window with deque]</span>
    - Key insight: <span class="fill-in">[Monotonic deque technique]</span>

**Failure modes:**

- What happens if the window condition is never satisfied — does your dynamic window correctly return 0 or an empty string, or does it return a sentinel value like `Integer.MAX_VALUE`? <span class="fill-in">[Fill in]</span>
- How does your implementation behave when the entire input string consists of a single repeated character and `k = 0` distinct characters are allowed? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these questions without looking at your notes. Write a sentence or two for each.

1. **A fixed window of size k is sliding across an array. The brute-force approach recomputes the sum from scratch each step. Explain exactly why the sliding window only needs two operations (one add, one subtract) per step, and what property of the problem makes this possible.**

    ??? success "Rubric"
        A complete answer addresses: (1) consecutive windows of size k overlap in k-1 elements — the new window is the old window minus the leftmost element plus one new rightmost element; (2) this reuse is safe because the problem asks about a contiguous subarray, so the shared elements contribute identically to both windows; (3) non-contiguous problems (e.g., "maximum sum of any k elements") cannot use this trick because there is no guaranteed overlap between optimal selections.

2. **You write a dynamic window solution for "longest substring with at most k distinct characters." It works on most inputs but fails on `"aab"` with `k = 1`. You suspect the shrink logic. Describe the most likely bug and trace through the failing case to confirm it.**

    ??? success "Rubric"
        A complete answer addresses: (1) the most likely bug is using `if` instead of `while` for the shrink loop — after removing one character from the left, the window may still have more than k distinct characters and must shrink again; (2) tracing `"aab"`, `k=1`: right=0 (`a`, size=1 ok), right=1 (`a`, size=1 ok), right=2 (`b`, size=2 > 1) — one `if`-shrink removes the first `a` but leaves the second `a` and `b` still in the map, so the window is still invalid; (3) `while` ensures the window shrinks until the invariant is restored.

3. **Minimum Window Substring (LeetCode 76) requires tracking "how many required characters are currently satisfied." Explain why simply comparing two frequency maps at each step would be too slow, and what optimisation the standard O(n) solution uses instead.**

    ??? success "Rubric"
        A complete answer addresses: (1) comparing two HashMaps costs O(|t|) per step — multiplied by O(n) steps this gives O(n × |t|), which is quadratic when |t| is large; (2) the standard optimisation maintains a single integer `formed` — a counter incremented when a character's window count reaches the required count, and decremented when it falls below; (3) when `formed == required` (number of distinct characters in t that are fully satisfied), the window is valid — this check is O(1) per step, giving overall O(n + |t|) time.

4. **When should you use a monotonic deque instead of a plain variable to track the window maximum? What does the deque store, and what is the invariant it maintains?**

    ??? success "Rubric"
        A complete answer addresses: (1) a plain `max` variable cannot efficiently update when the maximum element leaves the window — you would need an O(k) scan to find the new max, making the algorithm O(nk); (2) the deque stores indices of elements in decreasing order of their values — the front of the deque always holds the index of the current window maximum; (3) the invariant is that elements in the deque are in strictly decreasing order, so any element smaller than a newly added element is popped from the back (it can never be the future maximum while the larger element is still in the window).

5. **Sliding window and two-pointer techniques both use two indices. Give a concrete example of a problem that is naturally a sliding window problem, and explain why framing it as a two-pointer pair-search would not work.**

    ??? success "Rubric"
        A complete answer addresses: (1) a canonical example is "Minimum Window Substring" — the goal is to find a contiguous substring satisfying a character coverage constraint, not to find a pair of elements with a numeric relationship; (2) two-pointer pair-search relies on sorted order and a monotone sum relationship to know which pointer to advance; in sliding window the "value" of the window is multi-dimensional (a frequency map), so there is no scalar comparison that tells you which end to move; (3) the sliding window's shrink condition is driven by a constraint violation, not by a comparison of two element values.

---

## Connected Topics

<div class="bs-callout bs-callout-info" markdown>

**Where this topic connects**

- **01. Two Pointers** — the sliding window left/right boundary IS a two-pointer pattern; understanding opposite-direction pointers first builds intuition for same-direction window pointers → [01. Two Pointers](01-two-pointers.md)
- **03. Hash Tables** — variable-size sliding windows use a HashMap to track element frequencies inside the window in O(1) → [03. Hash Tables](03-hash-tables.md)
- **14. Prefix Sums** — both patterns answer range-query questions; prefix sums are better for static arrays with many queries, sliding window for single-pass streaming problems → [14. Prefix Sums](14-prefix-sums.md)

</div>
