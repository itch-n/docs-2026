# 04. Linked Lists

> Pointer manipulation for reversing, detecting cycles, and merging lists

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is a linked list in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **Why can't we use array indices?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
    - Example: "Linked lists are like a treasure hunt where each clue leads to the next..."
    - Your analogy: _[Fill in]_

4. **When does this pattern work?**
    - Your answer: _[Fill in after solving problems]_

5. **What's the key difference between singly and doubly linked lists?**
    - Your answer: _[Fill in after implementation]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Reversing a linked list iteratively:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified after learning: _[Actual]_

2. **Reversing a linked list recursively:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified: _[Actual - why different from iterative?]_

3. **Finding middle of linked list:**
    - If you traverse once to count, then again to middle: _[O(?)]_
    - Using slow/fast pointers: _[O(?)]_
    - Speedup: _[How much better?]_

### Scenario Predictions

**Scenario 1:** Reverse list `1 -> 2 -> 3 -> 4 -> 5`

- **How many pointers needed?** _[Fill in: prev, curr, next - why each?]_
- **Initial state:** prev = ___, curr = ___, next = ___
- **After first iteration:** List becomes _[Draw it: ? -> ? -> ? -> ?]_
- **What's the new head?** _[Which pointer points to it?]_

**Scenario 2:** Detect cycle in `1 -> 2 -> 3 -> 4 -> 2` (cycle back to 2)

- **Can you use fast/slow pointers?** _[Yes/No - Why?]_
- **Starting positions:** slow = ___, fast = ___
- **Will they ever meet?** _[Yes/No - Why/Why not?]_
- **Where will they meet?** _[At which node?]_

**Scenario 3:** Remove 2nd node from end in `1 -> 2 -> 3 -> 4 -> 5`

- **Which pattern applies?** _[Two pointers with gap - why?]_
- **Gap size needed:** _[How far apart should pointers be?]_
- **Result should be:** _[Which nodes remain?]_

### Trade-off Quiz

**Question:** When would recursion be WORSE than iteration for reversing?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning - consider stack space]_

**Question:** What's the MAIN advantage of linked lists over arrays?

- [ ] Faster random access
- [ ] Less memory usage
- [ ] O(1) insertion/deletion at known position
- [ ] Better cache locality

Verify after implementation: _[Which one(s)?]_

---

## Before/After: Why This Pattern Matters

**Your task:** Compare array operations vs linked list operations to understand trade-offs.

### Example 1: Insertion at Beginning

**Problem:** Insert element at the beginning of a collection.

#### Approach 1: Array (Dynamic)

```java
// Array approach - Shift all elements
public static int[] insertAtBeginning_Array(int[] arr, int value) {
    int[] newArr = new int[arr.length + 1];
    newArr[0] = value;

    // Copy all existing elements (shifted right)
    for (int i = 0; i < arr.length; i++) {
        newArr[i + 1] = arr[i];
    }

    return newArr;
}
```

**Analysis:**

- Time: O(n) - Must shift all elements
- Space: O(n) - Need new array
- For n = 10,000: ~10,000 copy operations

#### Approach 2: Linked List

```java
// Linked list approach - Just update pointers
public static ListNode insertAtBeginning_List(ListNode head, int value) {
    ListNode newNode = new ListNode(value);
    newNode.next = head;
    return newNode;  // New head
}
```

**Analysis:**

- Time: O(1) - Just two pointer assignments
- Space: O(1) - Single new node
- For n = 10,000: Always 2 operations (constant!)

#### Performance Comparison

| Collection Size | Array (O(n)) | Linked List (O(1)) | Speedup |
|----------------|--------------|-------------------|---------|
| n = 100        | 100 ops      | 2 ops             | 50x     |
| n = 1,000      | 1,000 ops    | 2 ops             | 500x    |
| n = 10,000     | 10,000 ops   | 2 ops             | 5,000x  |

**Your calculation:** For n = 5,000, inserting at beginning is _____ times faster with linked list.

---

### Example 2: Access Middle Element

**Problem:** Access the middle element of a collection.

#### Approach 1: Array

```java
// Array approach - Direct access
public static int getMiddle_Array(int[] arr) {
    return arr[arr.length / 2];  // O(1) access!
}
```

**Analysis:**

- Time: O(1) - Direct index access
- Space: O(1)
- Operations: 1 (always!)

#### Approach 2: Linked List (Naive)

```java
// Linked list approach - Must traverse
public static int getMiddle_List_Naive(ListNode head) {
    // First pass: count nodes
    int count = 0;
    ListNode curr = head;
    while (curr != null) {
        count++;
        curr = curr.next;
    }

    // Second pass: traverse to middle
    curr = head;
    for (int i = 0; i < count / 2; i++) {
        curr = curr.next;
    }

    return curr.val;
}
```

**Analysis:**

- Time: O(n) - Must traverse to middle
- Space: O(1)
- For n = 10,000: ~15,000 steps (count + traverse to middle)

#### Approach 3: Linked List (Optimized with Fast/Slow)

```java
// Optimized: slow/fast pointers - single pass
public static int getMiddle_List_Optimized(ListNode head) {
    ListNode slow = head, fast = head;

    while (fast != null && fast.next != null) {
        slow = slow.next;        // Move 1 step
        fast = fast.next.next;   // Move 2 steps
    }

    return slow.val;  // Slow is at middle when fast reaches end
}
```

**Analysis:**

- Time: O(n) - Single pass
- Space: O(1)
- For n = 10,000: ~5,000 steps (half as many as naive)

#### Performance Comparison

| Collection Size | Array (O(1)) | List Naive (O(n)) | List Optimized (O(n)) |
|----------------|--------------|-------------------|---------------------|
| n = 100        | 1 op         | 150 ops           | 50 ops              |
| n = 1,000      | 1 op         | 1,500 ops         | 500 ops             |
| n = 10,000     | 1 op         | 15,000 ops        | 5,000 ops           |

**Key insight:** Arrays win for access, linked lists win for insertion/deletion.

---

### Why Does Pointer Manipulation Work?

**Key insight to understand:**

When reversing `1 -> 2 -> 3 -> null`:

```
Initial:     1 -> 2 -> 3 -> null
             ^
            head

Goal:   null <- 1 <- 2 <- 3
                         ^
                       new head

How? Change each 'next' pointer to point backwards!
```

**Step-by-step visualization:**

```
Step 0: prev = null, curr = 1
        null    1 -> 2 -> 3 -> null
        ^       ^
       prev    curr

Step 1: Save next, reverse curr.next, move forward
        null <- 1    2 -> 3 -> null
                ^    ^
               prev curr

Step 2: Continue
        null <- 1 <- 2    3 -> null
                     ^    ^
                    prev curr

Step 3: Continue
        null <- 1 <- 2 <- 3    null
                          ^
                         prev (new head!)
```

**After implementing, explain in your own words:**

- _[Why do we need three pointers (prev, curr, next)?]_
- _[What happens if we skip saving 'next'?]_
- _[Why does prev end up as the new head?]_

---

## Core Implementation

### Pattern 1: Reverse Linked List

**Concept:** Change direction of next pointers.

**Use case:** Reverse entire list, reverse in groups, reverse between positions.

```java
public class ReverseLinkedList {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Reverse entire linked list
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement iteratively using three pointers
     */
    public static ListNode reverseList(ListNode head) {
        // TODO: prev = null, curr = head

        // TODO: While curr != null:
        //   Save next = curr.next
        //   Reverse pointer: curr.next = prev
        //   Move prev = curr
        //   Move curr = next

        // TODO: Return prev (new head)

        return null; // Replace with implementation
    }

    /**
     * Problem: Reverse linked list recursively
     * Time: O(n), Space: O(n) due to recursion stack
     *
     * TODO: Implement recursive reversal
     */
    public static ListNode reverseListRecursive(ListNode head) {
        // TODO: Base case: if head == null or head.next == null, return head

        // TODO: Recursively reverse rest: newHead = reverse(head.next)

        // TODO: Reverse current: head.next.next = head
        // TODO: Set head.next = null

        // TODO: Return newHead

        return null; // Replace with implementation
    }

    /**
     * Problem: Reverse first K nodes
     * Time: O(k), Space: O(1)
     *
     * TODO: Implement partial reversal
     */
    public static ListNode reverseFirstK(ListNode head, int k) {
        // TODO: Similar to reverseList but count k steps
        // TODO: Return new head and keep rest unchanged

        return null; // Replace with implementation
    }

    // Helper: Create list from array
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

    // Helper: Print list
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
public class ReverseLinkedListClient {

    public static void main(String[] args) {
        System.out.println("=== Reverse Linked List ===\n");

        // Test 1: Reverse entire list
        System.out.println("--- Test 1: Reverse Entire List ---");
        int[] values = {1, 2, 3, 4, 5};
        ListNode list = ReverseLinkedList.createList(values);

        System.out.print("Original: ");
        ReverseLinkedList.printList(list);

        ListNode reversed = ReverseLinkedList.reverseList(list);
        System.out.print("Reversed: ");
        ReverseLinkedList.printList(reversed);

        // Test 2: Reverse recursively
        System.out.println("\n--- Test 2: Reverse Recursively ---");
        ListNode list2 = ReverseLinkedList.createList(new int[]{1, 2, 3, 4, 5});

        System.out.print("Original: ");
        ReverseLinkedList.printList(list2);

        ListNode reversedRec = ReverseLinkedList.reverseListRecursive(list2);
        System.out.print("Reversed: ");
        ReverseLinkedList.printList(reversedRec);

        // Test 3: Reverse first K
        System.out.println("\n--- Test 3: Reverse First K Nodes ---");
        ListNode list3 = ReverseLinkedList.createList(new int[]{1, 2, 3, 4, 5, 6, 7});
        int k = 3;

        System.out.print("Original: ");
        ReverseLinkedList.printList(list3);
        System.out.println("K = " + k);

        ListNode reversedK = ReverseLinkedList.reverseFirstK(list3, k);
        System.out.print("Result:   ");
        ReverseLinkedList.printList(reversedK);
    }
}
```

---

### Pattern 2: Cycle Detection

**Concept:** Use Floyd's cycle detection (slow/fast pointers).

**Use case:** Detect cycle, find cycle start, remove cycle.

```java
public class CycleDetection {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Detect if linked list has a cycle
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement Floyd's cycle detection
     */
    public static boolean hasCycle(ListNode head) {
        // TODO: slow = head, fast = head

        // TODO: While fast != null && fast.next != null:
        //   slow = slow.next (1 step)
        //   fast = fast.next.next (2 steps)
        //   If slow == fast, return true

        // TODO: Return false

        return false; // Replace with implementation
    }

    /**
     * Problem: Find the node where cycle begins
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement cycle start detection
     */
    public static ListNode detectCycle(ListNode head) {
        // TODO: First detect if cycle exists (same as hasCycle)

        // TODO: If cycle found:
        //   Reset slow to head
        //   Move both slow and fast one step at a time
        //   They meet at cycle start

        return null; // Replace with implementation
    }

    /**
     * Problem: Remove cycle from linked list
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement cycle removal
     */
    public static void removeCycle(ListNode head) {
        // TODO: Find cycle start
        // TODO: Traverse to find node before cycle start
        // TODO: Set that node's next to null
    }

    // Helper: Create list from array
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
}
```

**Runnable Client Code:**

```java
public class CycleDetectionClient {

    public static void main(String[] args) {
        System.out.println("=== Cycle Detection ===\n");

        // Test 1: No cycle
        System.out.println("--- Test 1: No Cycle ---");
        ListNode list1 = CycleDetection.createList(new int[]{1, 2, 3, 4, 5});

        System.out.println("List: 1 -> 2 -> 3 -> 4 -> 5");
        System.out.println("Has cycle: " + CycleDetection.hasCycle(list1));

        // Test 2: Cycle exists
        System.out.println("\n--- Test 2: Cycle Exists ---");
        ListNode list2 = CycleDetection.createList(new int[]{1, 2, 3, 4, 5});

        // Create cycle: 5 -> 3
        ListNode node3 = list2.next.next; // node 3
        ListNode tail = list2.next.next.next.next; // node 5
        tail.next = node3;

        System.out.println("List: 1 -> 2 -> 3 -> 4 -> 5 -> (back to 3)");
        System.out.println("Has cycle: " + CycleDetection.hasCycle(list2));

        // Test 3: Find cycle start
        System.out.println("\n--- Test 3: Find Cycle Start ---");
        ListNode cycleStart = CycleDetection.detectCycle(list2);
        if (cycleStart != null) {
            System.out.println("Cycle starts at node with value: " + cycleStart.val);
        }

        // Test 4: Remove cycle
        System.out.println("\n--- Test 4: Remove Cycle ---");
        CycleDetection.removeCycle(list2);
        System.out.println("After removing cycle:");
        System.out.println("Has cycle: " + CycleDetection.hasCycle(list2));
    }
}
```

---

### Pattern 3: Merge Lists

**Concept:** Merge two or more sorted lists.

**Use case:** Merge two sorted lists, merge K sorted lists.

```java
import java.util.*;

public class MergeLists {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Merge two sorted linked lists
     * Time: O(n + m), Space: O(1)
     *
     * TODO: Implement iterative merge
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // TODO: Create dummy node to simplify edge cases

        // TODO: While both lists have nodes:
        //   Compare values
        //   Attach smaller node to result
        //   Advance pointer

        // TODO: Attach remaining nodes from non-empty list

        // TODO: Return dummy.next

        return null; // Replace with implementation
    }

    /**
     * Problem: Merge two sorted lists recursively
     * Time: O(n + m), Space: O(n + m) due to recursion
     *
     * TODO: Implement recursive merge
     */
    public static ListNode mergeTwoListsRecursive(ListNode l1, ListNode l2) {
        // TODO: Base cases: if l1 null return l2, if l2 null return l1

        // TODO: Compare values:
        //   If l1.val < l2.val:
        //     l1.next = merge(l1.next, l2)
        //     return l1
        //   Else:
        //     l2.next = merge(l1, l2.next)
        //     return l2

        return null; // Replace with implementation
    }

    /**
     * Problem: Merge K sorted linked lists
     * Time: O(N log k) where N = total nodes, k = number of lists
     * Space: O(k) for priority queue
     *
     * TODO: Implement using min heap (PriorityQueue)
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        // TODO: Create PriorityQueue with custom comparator

        // TODO: Add all list heads to queue

        // TODO: Create dummy node

        // TODO: While queue not empty:
        //   Poll minimum node
        //   Add to result
        //   If polled node has next, add next to queue

        // TODO: Return dummy.next

        return null; // Replace with implementation
    }

    // Helper: Create list
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

    // Helper: Print list
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
public class MergeListsClient {

    public static void main(String[] args) {
        System.out.println("=== Merge Linked Lists ===\n");

        // Test 1: Merge two sorted lists
        System.out.println("--- Test 1: Merge Two Lists ---");
        ListNode l1 = MergeLists.createList(new int[]{1, 3, 5, 7});
        ListNode l2 = MergeLists.createList(new int[]{2, 4, 6, 8});

        System.out.print("List 1: ");
        MergeLists.printList(l1);
        System.out.print("List 2: ");
        MergeLists.printList(l2);

        ListNode merged = MergeLists.mergeTwoLists(l1, l2);
        System.out.print("Merged: ");
        MergeLists.printList(merged);

        // Test 2: Merge recursively
        System.out.println("\n--- Test 2: Merge Recursively ---");
        ListNode l3 = MergeLists.createList(new int[]{1, 2, 4});
        ListNode l4 = MergeLists.createList(new int[]{1, 3, 4});

        System.out.print("List 1: ");
        MergeLists.printList(l3);
        System.out.print("List 2: ");
        MergeLists.printList(l4);

        ListNode mergedRec = MergeLists.mergeTwoListsRecursive(l3, l4);
        System.out.print("Merged: ");
        MergeLists.printList(mergedRec);

        // Test 3: Merge K lists
        System.out.println("\n--- Test 3: Merge K Lists ---");
        ListNode[] lists = {
            MergeLists.createList(new int[]{1, 4, 5}),
            MergeLists.createList(new int[]{1, 3, 4}),
            MergeLists.createList(new int[]{2, 6})
        };

        System.out.println("Lists:");
        for (int i = 0; i < lists.length; i++) {
            System.out.print("  List " + (i + 1) + ": ");
            MergeLists.printList(lists[i]);
        }

        ListNode mergedK = MergeLists.mergeKLists(lists);
        System.out.print("Merged: ");
        MergeLists.printList(mergedK);
    }
}
```

---

### Pattern 4: Remove Nth Node

**Concept:** Use two-pointer technique with gap.

**Use case:** Remove Nth from end, remove duplicates, partition list.

```java
public class RemoveNode {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Remove Nth node from end of list
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using two pointers with gap
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        // TODO: Create dummy node pointing to head (handles edge case)

        // TODO: fast = dummy, slow = dummy

        // TODO: Move fast n+1 steps ahead

        // TODO: Move both pointers until fast reaches end

        // TODO: Remove node: slow.next = slow.next.next

        // TODO: Return dummy.next

        return null; // Replace with implementation
    }

    /**
     * Problem: Remove all duplicates (sorted list)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement duplicate removal
     */
    public static ListNode deleteDuplicates(ListNode head) {
        // TODO: current = head

        // TODO: While current and current.next exist:
        //   If current.val == current.next.val:
        //     Skip next node
        //   Else:
        //     Move to next

        return null; // Replace with implementation
    }

    // Helper: Create list
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

    // Helper: Print list
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
public class RemoveNodeClient {

    public static void main(String[] args) {
        System.out.println("=== Remove Node Operations ===\n");

        // Test 1: Remove Nth from end
        System.out.println("--- Test 1: Remove Nth from End ---");
        ListNode list1 = RemoveNode.createList(new int[]{1, 2, 3, 4, 5});
        int n = 2;

        System.out.print("Original: ");
        RemoveNode.printList(list1);
        System.out.println("Remove " + n + "th from end");

        ListNode result1 = RemoveNode.removeNthFromEnd(list1, n);
        System.out.print("Result:   ");
        RemoveNode.printList(result1);

        // Test 2: Delete duplicates
        System.out.println("\n--- Test 2: Delete Duplicates ---");
        ListNode list2 = RemoveNode.createList(new int[]{1, 1, 2, 3, 3, 3, 4, 5, 5});

        System.out.print("Original: ");
        RemoveNode.printList(list2);

        ListNode result2 = RemoveNode.deleteDuplicates(list2);
        System.out.print("Result:   ");
        RemoveNode.printList(result2);
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken linked list implementations. This tests your understanding of pointer manipulation.

### Challenge 1: Broken Reverse Implementation

```java
/**
 * This code is supposed to reverse a linked list.
 * It has 2 CRITICAL BUGS. Find them!
 */
public static ListNode reverseList_Buggy(ListNode head) {
    ListNode prev = null;
    ListNode curr = head;

    while (curr != null) {
        ListNode next = curr.next;
        curr.next = prev;
        curr = next;           // BUG 1: What's missing?
    }

    return curr;  // BUG 2: Wrong return value!
}
```

**Your debugging:**

- **Bug 1 location:** _[Which line?]_
- **Bug 1 explanation:** _[What pointer update is missing?]_
- **Bug 1 fix:** _[What should be added?]_

- **Bug 2 location:** _[Which line?]_
- **Bug 2 explanation:** _[What does curr point to at the end?]_
- **Bug 2 fix:** _[What should we return instead?]_

**Trace through example:**

- Input: `1 -> 2 -> 3 -> null`
- Expected: `3 -> 2 -> 1 -> null`
- With bugs: _[What happens? Where does it fail?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 10):** Missing `prev = curr` before `curr = next`. The prev pointer never advances, so we lose track of the reversed portion.

**Correct:**
```java
while (curr != null) {
    ListNode next = curr.next;
    curr.next = prev;
    prev = curr;    // MUST update prev!
    curr = next;
}
```

**Bug 2 (Line 13):** Returning `curr` which is null at the end. Should return `prev`, which points to the new head (the last node we processed).

**Correct:** `return prev;`
</details>

---

### Challenge 2: Lost References in Cycle Detection

```java
/**
 * Detect if linked list has a cycle and return the cycle start node.
 * This has 1 SUBTLE BUG that causes NullPointerException.
 */
public static ListNode detectCycle_Buggy(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;

    // Find if cycle exists
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;

        if (slow == fast) {
            // Cycle found, now find start
            slow = head;
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next.next;  // BUG: What's wrong here?
            }
            return slow;
        }
    }

    return null;
}
```

**Your debugging:**

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[Why does this cause NullPointerException?]_
- **Bug fix:** _[How should fast move in the second phase?]_

**Trace through example:**

- List with cycle: `1 -> 2 -> 3 -> 4 -> 2` (cycle back to 2)
- Expected: Return node 2
- With bug: _[What happens in second while loop?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 18):** In the second phase (finding cycle start), both pointers should move ONE step at a time, not two steps for fast.

**Why it fails:** After finding the cycle, we're looking for the entry point. Moving fast by 2 steps can skip over the cycle start or cause fast to go past the end (if there's a path outside the cycle).

**Correct:**
```java
slow = head;
while (slow != fast) {
    slow = slow.next;
    fast = fast.next;  // Move ONE step, not two!
}
```

**Mathematical proof:** When slow and fast meet at distance k from cycle start, resetting slow to head and moving both one step at a time guarantees they meet at the cycle start. Moving fast by 2 breaks this property.
</details>

---

### Challenge 3: Merge Lists Pointer Loss

```java
/**
 * Merge two sorted linked lists.
 * This code has 1 CRITICAL BUG causing infinite loop or lost nodes.
 */
public static ListNode mergeTwoLists_Buggy(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;

    while (l1 != null && l2 != null) {
        if (l1.val < l2.val) {
            curr.next = l1;
            l1 = l1.next;
        } else {
            curr.next = l2;
            l2 = l2.next;
        }
        // BUG: Missing something here!
    }

    // Attach remaining nodes
    if (l1 != null) curr.next = l1;
    if (l2 != null) curr.next = l2;

    return dummy.next;
}
```

**Your debugging:**

- **Bug location:** _[What's missing in the while loop?]_
- **Bug explanation:** _[What happens to curr? Why does this cause problems?]_
- **Bug fix:** _[What statement should be added?]_

**Trace through example:**

- l1: `1 -> 3 -> 5`
- l2: `2 -> 4 -> 6`
- Expected: `1 -> 2 -> 3 -> 4 -> 5 -> 6`
- With bug: _[What does curr point to throughout?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (After line 16):** Missing `curr = curr.next;`. The curr pointer never advances, so we keep overwriting curr.next instead of building a chain.

**Correct:**
```java
while (l1 != null && l2 != null) {
    if (l1.val < l2.val) {
        curr.next = l1;
        l1 = l1.next;
    } else {
        curr.next = l2;
        l2 = l2.next;
    }
    curr = curr.next;  // MUST advance curr!
}
```

**Why it matters:** Without advancing curr, curr.next always points to the last node we attached. We're not building a proper linked list chain.
</details>

---

### Challenge 4: Remove Nth From End - Off By One

```java
/**
 * Remove the Nth node from the end of list.
 * This has 2 BUGS causing incorrect removal.
 */
public static ListNode removeNthFromEnd_Buggy(ListNode head, int n) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    ListNode fast = dummy;
    ListNode slow = dummy;

    // Move fast n steps ahead
    for (int i = 0; i < n; i++) {  // BUG 1: Should be n or n+1?
        fast = fast.next;
    }

    // Move both until fast reaches end
    while (fast != null) {  // BUG 2: Should check fast or fast.next?
        slow = slow.next;
        fast = fast.next;
    }

    // Remove node
    slow.next = slow.next.next;

    return dummy.next;
}
```

**Your debugging:**

- **Bug 1:** _[Should loop run n times or n+1 times? Why?]_
- **Bug 2:** _[Should we check `fast != null` or `fast.next != null`? Why?]_

**Test case:**

- Input: `1 -> 2 -> 3 -> 4 -> 5`, n = 2
- Expected: `1 -> 2 -> 3 -> 5` (remove 4)
- With bugs: _[Which node gets removed?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 11):** Loop should run `n + 1` times, not `n` times.

**Why:** We want slow to be positioned ONE NODE BEFORE the node to remove, not at the node itself. This requires an extra step of gap.

**Correct:** `for (int i = 0; i <= n; i++)`

**Bug 2 (Line 16):** Should check `fast.next != null`, not `fast != null`.

**Why:** We want to stop when fast is at the last node (so fast.next is null), not when fast goes past it. This positions slow at the node before the one to remove.

**Correct:** `while (fast.next != null)`

**Combined fix:**
```java
// Move fast n+1 steps ahead
for (int i = 0; i <= n; i++) {
    fast = fast.next;
}

// Move both until fast reaches end
while (fast.next != null) {
    slow = slow.next;
    fast = fast.next;
}
```
</details>

---

### Challenge 5: Accidental Cycle Creation

```java
/**
 * Reverse first K nodes of a linked list.
 * This code creates an ACCIDENTAL CYCLE! Find why.
 */
public static ListNode reverseFirstK_Buggy(ListNode head, int k) {
    if (head == null || k <= 1) return head;

    ListNode prev = null;
    ListNode curr = head;
    ListNode tail = head;  // Remember original head (will be tail after reverse)

    // Reverse first k nodes
    int count = 0;
    while (curr != null && count < k) {
        ListNode next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
        count++;
    }

    // BUG: This creates a cycle!
    tail.next = curr;

    return prev;  // New head after reversing first k
}
```

**Your debugging:**

- **Bug location:** _[Which line creates the cycle?]_
- **Bug explanation:** _[Why does tail.next = curr create a cycle?]_
- **Bug fix:** _[What's the issue and how to fix it?]_

**Trace through example:**

- Input: `1 -> 2 -> 3 -> 4 -> 5`, k = 3
- Expected: `3 -> 2 -> 1 -> 4 -> 5`
- With bug: _[What cycle is created? Draw it]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 22):** Actually, this code is CORRECT! There's no bug here - it's a trick question to test your understanding.

**Why it works:**

1. `tail` points to original head (value 1)
2. After reversing first k nodes, the list is: `3 -> 2 -> 1` (with 1.next = null)
3. `curr` points to node 4 (the first node after k nodes)
4. Setting `tail.next = curr` connects: `3 -> 2 -> 1 -> 4 -> 5`

**No cycle created!** The original head (now tail of reversed portion) correctly points to the rest of the list.

**However, there IS a subtle issue:** If k >= list length, curr will be null, and tail.next should remain null. The code handles this correctly because setting tail.next = null is fine.

**Actually, if you thought there was a bug because you weren't sure, you're thinking critically - good! But trace through carefully and you'll see it works.**

The real lesson: Always trace through pointer manipulations step by step to verify correctness.
</details>

---

### Challenge 6: Null Pointer Nightmare

```java
/**
 * Find the middle node of a linked list.
 * This has a NULL POINTER exception waiting to happen.
 */
public static ListNode findMiddle_Buggy(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;

    while (fast.next != null) {  // BUG: What if head is null? What about fast.next.next?
        slow = slow.next;
        fast = fast.next.next;
    }

    return slow;
}
```

**Your debugging:**

- **Bug 1:** _[What happens if head is null?]_
- **Bug 2:** _[What happens if list has even length?]_
- **Bug fix:** _[What should the while condition be?]_

**Test cases that expose bugs:**

- Input: `null` → _[What error?]_
- Input: `1 -> 2` → _[What error?]_
- Input: `1 -> 2 -> 3` → _[Does this work?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** If `head` is null, then `fast.next` throws NullPointerException.

**Bug 2:** If list has even length (e.g., `1 -> 2`), then fast.next.next will try to access next of null on second iteration.

**Fix:** Check both `fast != null` AND `fast.next != null`:

```java
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
```

**Why both checks:**

- `fast != null` - Handles odd-length lists (fast reaches last node)
- `fast.next != null` - Handles even-length lists (fast reaches null)
- Also handles empty list (fast is null initially)

**Rule of thumb:** When doing `fast.next.next`, ALWAYS check both `fast != null && fast.next != null`.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 8+ bugs across 6 challenges
- [ ] Understood WHY each bug causes incorrect behavior
- [ ] Could explain the fix to someone else
- [ ] Learned common linked list mistakes to avoid

**Common mistakes you discovered:**

1. _[Forgetting to update pointers (prev, curr)]_
2. _[Null pointer checks - always verify before accessing .next]_
3. _[Off-by-one errors in two-pointer gap problems]_
4. _[Missing curr = curr.next when building lists]_
5. _[Fill in others you noticed]_

---

## Decision Framework

**Your task:** Build decision trees for linked list operations.

### Question 1: What operation do you need?

Answer after solving problems:
- **Reverse?** _[Iterative vs recursive - trade-offs?]_
- **Detect cycle?** _[Why Floyd's algorithm?]_
- **Merge?** _[Two lists vs K lists - approach difference?]_
- **Remove node?** _[Why use dummy node?]_

### Question 2: Pointer patterns

**Reversal pattern:**

- Three pointers: prev, curr, next
- Use cases: _[List problems you solved]_

**Cycle detection pattern:**

- Slow/fast pointers
- Use cases: _[List problems you solved]_

**Two pointer with gap:**

- Maintain fixed distance
- Use cases: _[List problems you solved]_

### Your Decision Tree

```
Linked List Pattern Selection
│
├─ Need to reverse?
│   └─ Use: Three pointers (prev, curr, next) ✓
│
├─ Detect cycle or find middle?
│   └─ Use: Floyd's algorithm (slow/fast) ✓
│
├─ Merge sorted lists?
│   ├─ Two lists → Use: Iterative merge ✓
│   └─ K lists → Use: Min heap ✓
│
├─ Remove node from end?
│   └─ Use: Two pointers with gap ✓
│
└─ Remove duplicates?
    └─ Use: Single pointer ✓
```

### The "Kill Switch" - When NOT to use Linked Lists

**Don't use linked lists when:**

1. _[Need random access? Use array instead?]_
2. _[Memory overhead matters? Each node has pointer overhead]_
3. _[Cache locality important? Arrays are better]_
4. _[Need to search frequently? O(n) is slow]_

### The Rule of Three: Alternatives

**Option 1: Linked List**

- Pros: _[O(1) insertion/deletion at known position]_
- Cons: _[O(n) access, extra space for pointers]_
- Use when: _[Frequent insertions/deletions]_

**Option 2: Dynamic Array (ArrayList)**

- Pros: _[O(1) random access, cache friendly]_
- Cons: _[O(n) insertion/deletion in middle]_
- Use when: _[Frequent access, rare modifications]_

**Option 3: Doubly Linked List**

- Pros: _[Bidirectional traversal, O(1) deletion with node reference]_
- Cons: _[Extra space for prev pointer]_
- Use when: _[Need to traverse backwards]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**

- [ ] [206. Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/)
    - Pattern: _[Which one?]_
    - Your solution time: ___
    - Key insight: _[Fill in after solving]_

- [ ] [141. Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/)
    - Pattern: _[Which one?]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

- [ ] [21. Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/)
    - Pattern: _[Which one?]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

- [ ] [83. Remove Duplicates from Sorted List](https://leetcode.com/problems/remove-duplicates-from-sorted-list/)
    - Pattern: _[Which one?]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

**Medium (Complete 3-4):**

- [ ] [19. Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/)
    - Pattern: _[Which one?]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [142. Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/)
    - Pattern: _[Which one?]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Why does Floyd's algorithm work?]_

- [ ] [2. Add Two Numbers](https://leetcode.com/problems/add-two-numbers/)
    - Pattern: _[Which one?]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [143. Reorder List](https://leetcode.com/problems/reorder-list/)
    - Pattern: _[Combination of which patterns?]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

**Hard (Optional):**

- [ ] [23. Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/)
    - Pattern: _[Min heap approach]_
    - Key insight: _[Fill in after solving]_

- [ ] [25. Reverse Nodes in k-Group](https://leetcode.com/problems/reverse-nodes-in-k-group/)
    - Pattern: _[Extension of reversal]_
    - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] Reverse: iterative and recursive both work
    - [ ] Cycle: detection and find start both work
    - [ ] Merge: two lists and K lists both work
    - [ ] Remove: Nth from end and duplicates both work
    - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
    - [ ] Can identify when to use reversal pattern
    - [ ] Understand Floyd's cycle detection
    - [ ] Know when to use dummy node
    - [ ] Recognize two-pointer with gap pattern

- [ ] **Problem Solving**
    - [ ] Solved 4 easy problems
    - [ ] Solved 3-4 medium problems
    - [ ] Analyzed time/space complexity
    - [ ] Handled edge cases (null, single node)

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Built decision tree
    - [ ] Identified when NOT to use linked lists
    - [ ] Can explain trade-offs vs arrays

- [ ] **Mastery Check**
    - [ ] Could implement all patterns from memory
    - [ ] Could recognize pattern in new problem
    - [ ] Could explain to someone else
    - [ ] Understand pointer manipulation deeply

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about linked list pointer manipulation.

**Your explanation (write it out):**

> "Linked list operations work by..."
>
> _[Fill in your explanation in plain English - 3-4 sentences max]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by someone new to programming? _[Yes/No]_
- Did you use analogies or visual examples? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw the reversal of a 3-node list step-by-step, without looking at code.

**Draw the pointer movements:**

```
Initial: 1 -> 2 -> 3 -> null

Step 1: [Your drawing - show prev, curr, next positions]
        _________________________________

Step 2: [Your drawing - after first reversal]
        _________________________________

Step 3: [Your drawing - after second reversal]
        _________________________________

Final: [Your drawing - after all reversals]
        _________________________________
```

**Verification:**

- [ ] Drew initial state with all three pointers correctly
- [ ] Showed each pointer update step-by-step
- [ ] Explained why we save 'next' before changing curr.next
- [ ] Identified which pointer becomes the new head

---

### Gate 3: Pattern Recognition Test

**Without looking at your notes, classify these problems:**

| Problem | Pattern (Reverse/Cycle/Merge/Remove) | Key Technique |
|---------|--------------------------------------|---------------|
| Reverse a linked list | _[Fill in]_ | _[3 pointers: prev, curr, next]_ |
| Detect if list has a cycle | _[Fill in]_ | _[Fast/slow pointers]_ |
| Find middle of linked list | _[Fill in]_ | _[Fast/slow pointers]_ |
| Merge two sorted lists | _[Fill in]_ | _[Dummy node + pointer]_ |
| Remove nth from end | _[Fill in]_ | _[Two pointers with gap]_ |
| Find cycle start node | _[Fill in]_ | _[Fast/slow meet, then reset]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the patterns and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Operation | Time Complexity | Space Complexity | Why? |
|-----------|----------------|------------------|------|
| Reverse iteratively | O(?) | O(?) | _[Explain]_ |
| Reverse recursively | O(?) | O(?) | _[Why different?]_ |
| Detect cycle (Floyd's) | O(?) | O(?) | _[Explain]_ |
| Merge two lists | O(?) | O(?) | _[Explain]_ |
| Merge K lists (heap) | O(?) | O(?) | _[Explain]_ |

**Deep question:** Why is recursive reversal O(n) space while iterative is O(1)?

Your answer: _[Fill in - explain the stack frames]_

---

### Gate 5: Trade-off Decision

**Scenario:** You need to frequently insert elements and occasionally access the middle element.

**Option A:** Use a linked list
- Insertion: _[O(?)]_
- Access middle: _[O(?)]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option B:** Use an array (ArrayList)
- Insertion at beginning: _[O(?)]_
- Access middle: _[O(?)]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Your decision:** I would choose _[A/B]_ because...

_[Fill in your reasoning - consider frequency of operations]_

**What would make you change your decision?**

- _[Fill in - what constraints would flip your choice?]_

---

### Gate 6: Pointer Manipulation from Memory (Final Test)

**Set a 10-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: Reverse a linked list iteratively
 * Return new head
 */
public static ListNode reverseList(ListNode head) {
    // Your implementation here




    return null; // Replace
}
```

**After implementing, test with:**

- Input: `1 -> 2 -> 3 -> null`
- Expected: `3 -> 2 -> 1 -> null`

**Verification:**

- [ ] Implemented correctly without looking
- [ ] Handles edge cases (null, single node)
- [ ] Uses exactly three pointers (prev, curr, next)
- [ ] Returns correct new head (prev)
- [ ] Time: O(n), Space: O(1)

---

### Gate 7: Debugging Without Hints

**Find the bug in this code (no hints given):**

```java
public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;

    while (l1 != null && l2 != null) {
        if (l1.val < l2.val) {
            curr.next = l1;
            l1 = l1.next;
        } else {
            curr.next = l2;
            l2 = l2.next;
        }
    }

    if (l1 != null) curr.next = l1;
    if (l2 != null) curr.next = l2;

    return dummy.next;
}
```

**Your analysis:**

- Bug location: _[Which section?]_
- Bug explanation: _[What's wrong?]_
- Bug fix: _[Write the corrected code]_

**If you found it in under 2 minutes:** Excellent debugging skills!

**If it took longer:** Review the debugging challenges section.

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Missing `curr = curr.next;` in the while loop. Without it, we never advance curr, so we keep overwriting the same pointer.

**Fix:** Add `curr = curr.next;` at the end of the while loop.
</details>

---

### Gate 8: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain why we need a dummy node in merge operations.

Your explanation:

> "We use a dummy node because..."
>
> _[Fill in - explain the edge case it solves]_

**Example without dummy node:**

- _[What problem occurs?]_

**Example with dummy node:**

- _[How does it solve the problem?]_

---

### Gate 9: Pattern Combination

**Advanced challenge:** Some problems require combining multiple patterns.

**Problem:** Reorder a linked list: `L0 → L1 → L2 → ... → Ln-1 → Ln` becomes `L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → ...`

**Your approach (don't write code, just describe):**

1. Step 1: _[Which pattern to find middle?]_
2. Step 2: _[Which pattern to reverse second half?]_
3. Step 3: _[Which pattern to merge?]_

**Patterns used:** _[List all patterns involved]_

**Complexity:** _[Overall time and space]_

---

### Gate 10: Null Safety Mastery

**Question:** For the fast/slow pointer pattern, why do we check `fast != null && fast.next != null`?

**Your explanation:**

- Why both checks? _[Fill in]_
- What happens if we only check `fast != null`? _[Fill in]_
- What happens if we only check `fast.next != null`? _[Fill in]_

**Example test cases:**

- Empty list: _[Which check saves us?]_
- Single node: _[Which check saves us?]_
- Two nodes: _[Which check saves us?]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Reverse a linked list (both iterative and recursive) from memory
- [ ] Detect and find the start of a cycle using Floyd's algorithm
- [ ] Merge two sorted lists without bugs
- [ ] Remove nth node from end using two pointers with gap
- [ ] Explain when to use linked lists vs arrays
- [ ] Debug common pointer manipulation errors
- [ ] Analyze time and space complexity of all patterns
- [ ] Teach these concepts to someone else

**Self-assessment score:** ___/10

**Critical skills check:**

- [ ] Can you trace through pointer updates step-by-step?
- [ ] Do you always check for null before accessing .next?
- [ ] Do you understand why we use dummy nodes?
- [ ] Can you identify which pattern to use for new problems?

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered linked list operations. Proceed to the next topic.

---

### Real-World Application Test

**Scenario:** You're implementing an undo feature for a text editor.

**Question 1:** Would you use an array or linked list to store the history of changes?
- Your answer: _[Fill in]_
- Reasoning: _[Consider operations: add, remove from end, traverse]_

**Question 2:** If you used a doubly linked list, what advantage does that give?
- Your answer: _[Fill in]_

**Question 3:** What's the space trade-off?
- Linked list overhead: _[Fill in - what extra space per node?]_
- Worth it for this use case? _[Yes/No - Why?]_
