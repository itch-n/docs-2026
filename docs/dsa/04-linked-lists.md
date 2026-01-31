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

**Next Topic:** [05. Stacks & Queues →](05-stacks--queues.md)

**Back to:** [03. Hash Tables ←](03-hash-tables.md)
