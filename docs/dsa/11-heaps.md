# 10. Heaps (Priority Queues)

> O(log n) insert/delete with O(1) access to min/max element

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is a heap in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why is it called a priority queue?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "A heap is like a hospital emergency room where patients are seen by urgency..."
   - Your analogy: _[Fill in]_

4. **When does this pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **What's the difference between min-heap and max-heap?**
   - Your answer: _[Fill in after implementation]_

---

## Core Implementation

### Pattern 1: Basic Heap Operations

**Concept:** Maintain heap property through insert and extract operations.

**Use case:** Find kth largest/smallest, top K elements.

```java
import java.util.*;

public class BasicHeapOperations {

    /**
     * Problem: Find Kth largest element in array
     * Time: O(n log k), Space: O(k)
     *
     * TODO: Implement using min-heap of size k
     */
    public static int findKthLargest(int[] nums, int k) {
        // TODO: Create PriorityQueue (min-heap) of size k
        // TODO: For each num in nums:
        //   Add num to heap
        //   If heap size > k, remove smallest (poll)
        // TODO: Return heap.peek() (kth largest)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Find K largest elements
     * Time: O(n log k), Space: O(k)
     *
     * TODO: Implement K largest elements
     */
    public static List<Integer> kLargest(int[] nums, int k) {
        // TODO: Use min-heap of size k
        // TODO: Maintain k largest elements
        // TODO: Return heap as list

        return new ArrayList<>(); // Replace with implementation
    }

    /**
     * Problem: Sort array using heap
     * Time: O(n log n), Space: O(n)
     *
     * TODO: Implement heap sort
     */
    public static int[] heapSort(int[] nums) {
        // TODO: Add all elements to max-heap
        // TODO: Extract max repeatedly to get sorted array

        return new int[0]; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class BasicHeapOperationsClient {

    public static void main(String[] args) {
        System.out.println("=== Basic Heap Operations ===\n");

        // Test 1: Kth largest
        System.out.println("--- Test 1: Kth Largest ---");
        int[] arr = {3, 2, 1, 5, 6, 4};
        int k = 2;

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("k = " + k);
        int kthLargest = BasicHeapOperations.findKthLargest(arr, k);
        System.out.println("Kth largest: " + kthLargest);

        // Test 2: K largest elements
        System.out.println("\n--- Test 2: K Largest Elements ---");
        int[] arr2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        k = 4;

        System.out.println("Array: " + Arrays.toString(arr2));
        System.out.println("k = " + k);
        List<Integer> kLargest = BasicHeapOperations.kLargest(arr2, k);
        System.out.println("K largest: " + kLargest);

        // Test 3: Heap sort
        System.out.println("\n--- Test 3: Heap Sort ---");
        int[] arr3 = {5, 2, 8, 1, 9, 3};
        System.out.println("Before: " + Arrays.toString(arr3));
        int[] sorted = BasicHeapOperations.heapSort(arr3);
        System.out.println("After:  " + Arrays.toString(sorted));
    }
}
```

---

### Pattern 2: Merge K Sorted Lists/Arrays

**Concept:** Use min-heap to merge multiple sorted sequences efficiently.

**Use case:** Merge K sorted lists, merge K sorted arrays.

```java
import java.util.*;

public class MergeKSorted {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Merge K sorted linked lists
     * Time: O(n log k), Space: O(k) where n = total nodes, k = lists
     *
     * TODO: Implement using min-heap
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        // TODO: Create min-heap with comparator on node.val
        // TODO: Add first node from each list to heap
        // TODO: While heap not empty:
        //   Extract min node
        //   Add to result
        //   If min node has next, add next to heap

        return null; // Replace with implementation
    }

    /**
     * Problem: Merge K sorted arrays
     * Time: O(n log k), Space: O(k)
     *
     * TODO: Implement using min-heap with array indices
     */
    public static List<Integer> mergeKArrays(int[][] arrays) {
        // TODO: Create heap with [value, arrayIndex, elementIndex]
        // TODO: Add first element from each array
        // TODO: Extract min and add next from same array

        return new ArrayList<>(); // Replace with implementation
    }

    /**
     * Problem: Find smallest range covering elements from K lists
     * Time: O(n log k), Space: O(k)
     *
     * TODO: Implement using heap tracking max
     */
    public static int[] smallestRange(List<List<Integer>> nums) {
        // TODO: Use min-heap, track current max
        // TODO: Range = [heap.peek(), currentMax]
        // TODO: Minimize range size

        return new int[]{0, 0}; // Replace with implementation
    }

    // Helper: Create linked list from array
    static ListNode createList(int[] values) {
        if (values.length == 0) return null;
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        return head;
    }

    // Helper: Print linked list
    static void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val);
            if (current.next != null) System.out.print(" -> ");
            current = current.next;
        }
        System.out.println();
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class MergeKSortedClient {

    public static void main(String[] args) {
        System.out.println("=== Merge K Sorted ===\n");

        // Test 1: Merge K lists
        System.out.println("--- Test 1: Merge K Linked Lists ---");
        ListNode[] lists = new ListNode[3];
        lists[0] = MergeKSorted.createList(new int[]{1, 4, 5});
        lists[1] = MergeKSorted.createList(new int[]{1, 3, 4});
        lists[2] = MergeKSorted.createList(new int[]{2, 6});

        System.out.println("List 1: ");
        MergeKSorted.printList(lists[0]);
        System.out.println("List 2: ");
        MergeKSorted.printList(lists[1]);
        System.out.println("List 3: ");
        MergeKSorted.printList(lists[2]);

        ListNode merged = MergeKSorted.mergeKLists(lists);
        System.out.print("Merged: ");
        MergeKSorted.printList(merged);

        // Test 2: Merge K arrays
        System.out.println("\n--- Test 2: Merge K Arrays ---");
        int[][] arrays = {
            {1, 3, 5, 7},
            {2, 4, 6, 8},
            {0, 9, 10, 11}
        };

        for (int i = 0; i < arrays.length; i++) {
            System.out.println("Array " + (i + 1) + ": " + Arrays.toString(arrays[i]));
        }

        List<Integer> mergedArray = MergeKSorted.mergeKArrays(arrays);
        System.out.println("Merged: " + mergedArray);

        // Test 3: Smallest range
        System.out.println("\n--- Test 3: Smallest Range ---");
        List<List<Integer>> nums = Arrays.asList(
            Arrays.asList(4, 10, 15, 24, 26),
            Arrays.asList(0, 9, 12, 20),
            Arrays.asList(5, 18, 22, 30)
        );

        System.out.println("Lists: " + nums);
        int[] range = MergeKSorted.smallestRange(nums);
        System.out.println("Smallest range: [" + range[0] + ", " + range[1] + "]");
    }
}
```

---

### Pattern 3: Top K Frequent Elements

**Concept:** Use heap to find most/least frequent elements.

**Use case:** Top K frequent elements, sort by frequency.

```java
import java.util.*;

public class TopKFrequent {

    /**
     * Problem: Find K most frequent elements
     * Time: O(n log k), Space: O(n)
     *
     * TODO: Implement using frequency map + min-heap
     */
    public static List<Integer> topKFrequent(int[] nums, int k) {
        // TODO: Count frequencies with HashMap
        // TODO: Create min-heap of size k, ordered by frequency
        // TODO: For each entry in frequency map:
        //   Add to heap
        //   If size > k, remove min frequency
        // TODO: Return heap contents

        return new ArrayList<>(); // Replace with implementation
    }

    /**
     * Problem: Sort array by frequency
     * Time: O(n log n), Space: O(n)
     *
     * TODO: Implement frequency sort
     */
    public static int[] frequencySort(int[] nums) {
        // TODO: Count frequencies
        // TODO: Sort by frequency (descending), then by value (ascending)

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: K closest points to origin
     * Time: O(n log k), Space: O(k)
     *
     * TODO: Implement using max-heap of size k
     */
    public static int[][] kClosest(int[][] points, int k) {
        // TODO: Create max-heap ordered by distance
        // TODO: Maintain k closest points
        // TODO: Distance = x^2 + y^2 (no need for sqrt)

        return new int[0][0]; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class TopKFrequentClient {

    public static void main(String[] args) {
        System.out.println("=== Top K Frequent ===\n");

        // Test 1: Top K frequent
        System.out.println("--- Test 1: Top K Frequent ---");
        int[] arr = {1, 1, 1, 2, 2, 3};
        int k = 2;

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("k = " + k);
        List<Integer> topK = TopKFrequent.topKFrequent(arr, k);
        System.out.println("Top K frequent: " + topK);

        // Test 2: Frequency sort
        System.out.println("\n--- Test 2: Frequency Sort ---");
        int[] arr2 = {1, 1, 2, 2, 2, 3};
        System.out.println("Before: " + Arrays.toString(arr2));
        int[] sorted = TopKFrequent.frequencySort(arr2);
        System.out.println("After:  " + Arrays.toString(sorted));

        // Test 3: K closest points
        System.out.println("\n--- Test 3: K Closest Points ---");
        int[][] points = {{1, 3}, {-2, 2}, {5, 8}, {0, 1}};
        k = 2;

        System.out.println("Points:");
        for (int[] point : points) {
            System.out.println("  " + Arrays.toString(point));
        }
        System.out.println("k = " + k);

        int[][] closest = TopKFrequent.kClosest(points, k);
        System.out.println("K closest points:");
        for (int[] point : closest) {
            System.out.println("  " + Arrays.toString(point));
        }
    }
}
```

---

### Pattern 4: Two Heaps (Find Median)

**Concept:** Use two heaps to maintain running median.

**Use case:** Find median from data stream, sliding window median.

```java
import java.util.*;

public class TwoHeaps {

    /**
     * MedianFinder: Maintain running median
     * Time: O(log n) insert, O(1) find median
     * Space: O(n)
     *
     * TODO: Implement using two heaps
     */
    static class MedianFinder {
        // TODO: maxHeap stores smaller half (max at top)
        // TODO: minHeap stores larger half (min at top)
        // TODO: Keep heaps balanced: |size difference| <= 1

        public MedianFinder() {
            // TODO: Initialize PriorityQueue for max-heap (reverse order)
            // TODO: Initialize PriorityQueue for min-heap (natural order)
        }

        public void addNum(int num) {
            // TODO: Add to appropriate heap
            // TODO: Balance heaps if needed
            // Hint: Always add to maxHeap first, then move to minHeap if needed
        }

        public double findMedian() {
            // TODO: If heaps same size: average of two tops
            // TODO: If different size: top of larger heap
            return 0.0; // Replace with implementation
        }
    }

    /**
     * Problem: Sliding window median
     * Time: O(n * k), Space: O(k)
     *
     * TODO: Implement sliding window median
     */
    public static double[] medianSlidingWindow(int[] nums, int k) {
        // TODO: Use two heaps approach
        // TODO: Handle removal from window
        // Note: This is complex - use TreeMap or simpler approach

        return new double[0]; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class TwoHeapsClient {

    public static void main(String[] args) {
        System.out.println("=== Two Heaps ===\n");

        // Test 1: Median finder
        System.out.println("--- Test 1: Find Median from Data Stream ---");
        TwoHeaps.MedianFinder mf = new TwoHeaps.MedianFinder();

        int[] stream = {1, 2, 3, 4, 5};
        System.out.println("Data stream: " + Arrays.toString(stream));
        System.out.println("Median after each insertion:");

        for (int num : stream) {
            mf.addNum(num);
            System.out.printf("  After adding %d: %.1f%n", num, mf.findMedian());
        }

        // Test 2: Another stream
        System.out.println("\n--- Test 2: Another Stream ---");
        TwoHeaps.MedianFinder mf2 = new TwoHeaps.MedianFinder();
        int[] stream2 = {5, 15, 1, 3};

        System.out.println("Data stream: " + Arrays.toString(stream2));
        System.out.println("Median after each insertion:");

        for (int num : stream2) {
            mf2.addNum(num);
            System.out.printf("  After adding %d: %.1f%n", num, mf2.findMedian());
        }

        // Test 3: Sliding window median
        System.out.println("\n--- Test 3: Sliding Window Median ---");
        int[] arr = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Window size: " + k);
        double[] medians = TwoHeaps.medianSlidingWindow(arr, k);
        System.out.println("Medians: " + Arrays.toString(medians));
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for when to use heaps.

### Question 1: What do you need to track?

Answer after solving problems:
- **Need min/max element repeatedly?** _[Use heap]_
- **Need Kth largest/smallest?** _[Use heap of size K]_
- **Need median?** _[Use two heaps]_
- **Your observation:** _[Fill in based on testing]_

### Question 2: What are the time/space trade-offs?

Answer for each pattern:

**Basic heap operations:**
- Time complexity: _[Insert? Extract? Peek?]_
- Space complexity: _[How much space?]_
- Best use cases: _[List problems you solved]_

**Merge K sorted:**
- Time complexity: _[Compare to merge two at a time]_
- Space complexity: _[Just heap or output too?]_
- Best use cases: _[List problems you solved]_

**Top K frequent:**
- Time complexity: _[Why log k not log n?]_
- Space complexity: _[Frequency map + heap]_
- Best use cases: _[List problems you solved]_

**Two heaps:**
- Time complexity: _[Insert? Find median?]_
- Space complexity: _[Both heaps needed?]_
- Best use cases: _[List problems you solved]_

### Your Decision Tree

Build this after solving practice problems:

```
Heap Pattern Selection
│
├─ Need Kth largest/smallest?
│   └─ Use: Min-heap of size K (for largest) ✓
│        Max-heap of size K (for smallest) ✓
│
├─ Merge K sorted sequences?
│   └─ Use: Min-heap with K elements ✓
│
├─ Need top K frequent?
│   └─ Use: Frequency map + heap ✓
│
├─ Need running median?
│   └─ Use: Two heaps (max + min) ✓
│
└─ Need all elements sorted?
    └─ Consider: Heap sort vs other sorts ✓
```

### The "Kill Switch" - When NOT to use Heaps

**Don't use heaps when:**
1. _[Need all elements in sorted order? Sort might be better]_
2. _[K is very large? Different approach needed]_
3. _[Need to find element by value? Use hash table]_
4. _[Don't need priority access? Use array/list]_

### The Rule of Three: Alternatives

**Option 1: Heap (Priority Queue)**
- Pros: _[O(log n) insert/delete, O(1) peek]_
- Cons: _[Not sorted, just maintains priority]_
- Use when: _[Need repeated min/max access]_

**Option 2: Sorting**
- Pros: _[All elements sorted]_
- Cons: _[O(n log n) upfront, can't add efficiently]_
- Use when: _[Need all sorted once]_

**Option 3: Quick Select**
- Pros: _[O(n) average for Kth element]_
- Cons: _[Modifies array, no repeated access]_
- Use when: _[One-time Kth element query]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 3):**
- [ ] [703. Kth Largest Element in a Stream](https://leetcode.com/problems/kth-largest-element-in-a-stream/)
  - Pattern: _[Min-heap of size K]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [1046. Last Stone Weight](https://leetcode.com/problems/last-stone-weight/)
  - Pattern: _[Max-heap simulation]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [1337. The K Weakest Rows in a Matrix](https://leetcode.com/problems/the-k-weakest-rows-in-a-matrix/)
  - Pattern: _[Heap with custom comparator]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 3-4):**
- [ ] [215. Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/)
  - Pattern: _[Min-heap approach]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [347. Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)
  - Pattern: _[Frequency + heap]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [973. K Closest Points to Origin](https://leetcode.com/problems/k-closest-points-to-origin/)
  - Pattern: _[Max-heap of size K]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [295. Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/)
  - Pattern: _[Two heaps]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

**Hard (Optional):**
- [ ] [23. Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/)
  - Pattern: _[Min-heap with K nodes]_
  - Key insight: _[Fill in after solving]_

- [ ] [480. Sliding Window Median](https://leetcode.com/problems/sliding-window-median/)
  - Pattern: _[Two heaps with removal]_
  - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Basic operations: Kth largest, K largest, heap sort all work
  - [ ] Merge K: lists and arrays both work
  - [ ] Top K: frequent elements and closest points work
  - [ ] Two heaps: median finder works correctly
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify when to use min-heap vs max-heap
  - [ ] Understand when heap size should be K vs N
  - [ ] Know when to use two heaps
  - [ ] Recognize merge K sorted pattern

- [ ] **Problem Solving**
  - [ ] Solved 3 easy problems
  - [ ] Solved 3-4 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Understood heap vs sort trade-offs

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use heaps
  - [ ] Can explain heap property and operations

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand PriorityQueue in Java

---

**Next Topic:** [12. Backtracking →](12-backtracking.md)

**Back to:** [10. Graphs ←](10-graphs.md)
