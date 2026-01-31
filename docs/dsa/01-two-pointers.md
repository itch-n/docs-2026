# 01. Two Pointers

> Reduce O(n²) to O(n) by using two indices moving through data

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all three patterns, explain them simply.

**Prompts to guide you:**

1. **What is the two pointers pattern in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why is it faster than nested loops?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "Two pointers is like two people searching..."
   - Your analogy: _[Fill in]_

4. **When does this pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **When does this pattern fail?**
   - Your answer: _[Fill in after trying unsorted arrays]_

---

## Core Implementation

### Pattern 1: Opposite Direction Pointers

**Concept:** Start from both ends, move toward each other.

**Use case:** Palindromes, pair sum in sorted array.

```java
public class OppositeDirectionPointers {

    /**
     * Problem: Check if string is a palindrome
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using two pointers from opposite ends
     */
    public static boolean isPalindrome(String s) {
        // TODO: Initialize left = 0, right = s.length() - 1

        // TODO: While left < right
        //   Compare s.charAt(left) with s.charAt(right)
        //   If different, return false
        //   Move pointers: left++, right--

        // TODO: If loop completes, return true

        return false; // Replace with implementation
    }

    /**
     * Problem: Find pair in sorted array that sums to target
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement two-pointer pair sum
     */
    public static int[] twoSum(int[] nums, int target) {
        // TODO: Initialize left = 0, right = nums.length - 1

        // TODO: While left < right
        //   Calculate sum = nums[left] + nums[right]
        //   If sum == target, return [left, right]
        //   If sum < target, move left++ (need larger sum)
        //   If sum > target, move right-- (need smaller sum)

        return new int[] {-1, -1}; // Replace with implementation
    }

    /**
     * Problem: Reverse array in-place
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using two pointers
     */
    public static void reverseArray(int[] arr) {
        // TODO: left = 0, right = arr.length - 1
        // TODO: While left < right, swap arr[left] with arr[right]
        // TODO: left++, right--
    }
}
```

**Runnable Client Code:**

```java
public class OppositeDirectionClient {

    public static void main(String[] args) {
        System.out.println("=== Opposite Direction Two Pointers ===\n");

        // Test 1: Palindrome check
        System.out.println("--- Test 1: Palindrome ---");
        String[] testStrings = {"racecar", "hello", "noon", "a", ""};

        for (String s : testStrings) {
            boolean result = OppositeDirectionPointers.isPalindrome(s);
            System.out.printf("isPalindrome(\"%s\") = %b%n", s, result);
        }

        // Test 2: Two sum in sorted array
        System.out.println("\n--- Test 2: Two Sum ---");
        int[] sortedArray = {1, 3, 5, 7, 9, 11};
        int target = 12;

        int[] result = OppositeDirectionPointers.twoSum(sortedArray, target);
        System.out.printf("Array: %s%n", Arrays.toString(sortedArray));
        System.out.printf("Target: %d%n", target);
        System.out.printf("Pair indices: %s%n", Arrays.toString(result));
        if (result[0] != -1) {
            System.out.printf("Values: %d + %d = %d%n",
                sortedArray[result[0]], sortedArray[result[1]], target);
        }

        // Test 3: Reverse array
        System.out.println("\n--- Test 3: Reverse Array ---");
        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("Before: " + Arrays.toString(arr));
        OppositeDirectionPointers.reverseArray(arr);
        System.out.println("After:  " + Arrays.toString(arr));
    }
}
```

---

### Pattern 2: Same Direction Pointers (Slow/Fast)

**Concept:** Both pointers move left to right, at different speeds.

**Use case:** Remove duplicates, partition array, in-place modifications.

```java
public class SameDirectionPointers {

    /**
     * Problem: Remove duplicates from sorted array in-place
     * Return new length
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using slow/fast pointers
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        // TODO: slow = 0 (tracks position of last unique element)
        // TODO: fast = 1 (explores array)

        // TODO: While fast < nums.length
        //   If nums[fast] != nums[slow]
        //     slow++
        //     nums[slow] = nums[fast]
        //   fast++

        // TODO: Return slow + 1 (new length)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Move all zeros to end, maintain order of non-zeros
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using slow/fast pointers
     */
    public static void moveZeroes(int[] nums) {
        // TODO: slow = 0 (position to place next non-zero)
        // TODO: fast = 0 (explores array)

        // TODO: While fast < nums.length
        //   If nums[fast] != 0
        //     Swap nums[slow] with nums[fast]
        //     slow++
        //   fast++
    }

    /**
     * Problem: Partition array - all elements < pivot go left
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement partition logic
     */
    public static int partition(int[] arr, int pivot) {
        // TODO: slow = 0 (boundary between < pivot and >= pivot)
        // TODO: fast = 0 (explores array)

        // TODO: While fast < arr.length
        //   If arr[fast] < pivot
        //     Swap arr[slow] with arr[fast]
        //     slow++
        //   fast++

        // TODO: Return slow (partition index)

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class SameDirectionClient {

    public static void main(String[] args) {
        System.out.println("=== Same Direction Two Pointers ===\n");

        // Test 1: Remove duplicates
        System.out.println("--- Test 1: Remove Duplicates ---");
        int[] arr1 = {1, 1, 2, 2, 2, 3, 4, 4, 5};
        System.out.println("Before: " + Arrays.toString(arr1));

        int newLength = SameDirectionPointers.removeDuplicates(arr1);
        System.out.println("After:  " + Arrays.toString(Arrays.copyOf(arr1, newLength)));
        System.out.println("New length: " + newLength);

        // Test 2: Move zeros
        System.out.println("\n--- Test 2: Move Zeros ---");
        int[] arr2 = {0, 1, 0, 3, 12, 0, 5};
        System.out.println("Before: " + Arrays.toString(arr2));
        SameDirectionPointers.moveZeroes(arr2);
        System.out.println("After:  " + Arrays.toString(arr2));

        // Test 3: Partition
        System.out.println("\n--- Test 3: Partition ---");
        int[] arr3 = {7, 2, 9, 1, 5, 3, 8};
        int pivot = 5;
        System.out.println("Before: " + Arrays.toString(arr3));
        System.out.println("Pivot:  " + pivot);

        int partitionIdx = SameDirectionPointers.partition(arr3, pivot);
        System.out.println("After:  " + Arrays.toString(arr3));
        System.out.println("Partition index: " + partitionIdx);
        System.out.println("(All elements before index " + partitionIdx + " are < " + pivot + ")");
    }
}
```

---

### Pattern 3: Different Speed Pointers

**Concept:** One pointer moves faster than the other.

**Use case:** Linked list cycle detection, finding middle element.

```java
public class DifferentSpeedPointers {

    // Simple ListNode definition
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Detect cycle in linked list
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using slow/fast pointers
     */
    public static boolean hasCycle(ListNode head) {
        // TODO: slow = head, fast = head

        // TODO: While fast != null && fast.next != null
        //   slow = slow.next (move 1 step)
        //   fast = fast.next.next (move 2 steps)
        //   If slow == fast, return true (cycle detected)

        // TODO: Return false (no cycle)

        return false; // Replace with implementation
    }

    /**
     * Problem: Find middle of linked list
     * If even length, return second middle node
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using slow/fast pointers
     */
    public static ListNode findMiddle(ListNode head) {
        // TODO: slow = head, fast = head

        // TODO: While fast != null && fast.next != null
        //   slow = slow.next
        //   fast = fast.next.next

        // TODO: Return slow (it's at middle when fast reaches end)

        return null; // Replace with implementation
    }

    /**
     * Problem: Find kth node from end
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement using two pointers with gap
     */
    public static ListNode findKthFromEnd(ListNode head, int k) {
        // TODO: fast = head, slow = head

        // TODO: Move fast k steps ahead
        //   For i = 0 to k-1: fast = fast.next
        //   If fast becomes null, k is too large

        // TODO: Move both pointers until fast reaches end
        //   While fast.next != null:
        //     slow = slow.next
        //     fast = fast.next

        // TODO: Return slow (it's k nodes from end)

        return null; // Replace with implementation
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
public class DifferentSpeedClient {

    public static void main(String[] args) {
        System.out.println("=== Different Speed Two Pointers ===\n");

        // Test 1: Cycle detection
        System.out.println("--- Test 1: Cycle Detection ---");

        // List without cycle: 1 -> 2 -> 3 -> 4 -> 5
        ListNode list1 = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("List: ");
        DifferentSpeedPointers.printList(list1);
        System.out.println("Has cycle: " + DifferentSpeedPointers.hasCycle(list1));

        // List with cycle: 1 -> 2 -> 3 -> 4 -> 5 -> (back to 3)
        ListNode list2 = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        ListNode node3 = list2.next.next; // Node with value 3
        ListNode tail = list2.next.next.next.next; // Node with value 5
        tail.next = node3; // Create cycle

        System.out.println("\nList with cycle (5 -> 3):");
        System.out.println("Has cycle: " + DifferentSpeedPointers.hasCycle(list2));

        // Test 2: Find middle
        System.out.println("\n--- Test 2: Find Middle ---");
        ListNode list3 = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("List (odd length): ");
        DifferentSpeedPointers.printList(list3);

        ListNode middle = DifferentSpeedPointers.findMiddle(list3);
        System.out.println("Middle value: " + middle.val);

        ListNode list4 = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5, 6});
        System.out.print("List (even length): ");
        DifferentSpeedPointers.printList(list4);

        middle = DifferentSpeedPointers.findMiddle(list4);
        System.out.println("Middle value: " + middle.val);

        // Test 3: Kth from end
        System.out.println("\n--- Test 3: Kth From End ---");
        ListNode list5 = DifferentSpeedPointers.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("List: ");
        DifferentSpeedPointers.printList(list5);

        for (int k = 1; k <= 3; k++) {
            ListNode kthNode = DifferentSpeedPointers.findKthFromEnd(list5, k);
            System.out.printf("%dth from end: %d%n", k, kthNode.val);
        }
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for when to use two pointers.

### Question 1: Is the data sorted?

Answer after solving problems:
- **Why does sorting matter?** _[Fill in]_
- **Can two pointers work on unsorted arrays?** _[Yes/No - when?]_
- **Your observation:** _[Fill in based on testing]_

### Question 2: What are you looking for?

Answer for each pattern:

**Opposite direction when:**
- Looking for: _[Pairs? Palindromes? What else?]_
- Movement rule: _[How do pointers move?]_
- Example problems: _[List problems you solved]_

**Same direction when:**
- Looking for: _[Duplicates? Partitioning? What else?]_
- Movement rule: _[How do slow/fast pointers move?]_
- Example problems: _[List problems you solved]_

**Different speed when:**
- Looking for: _[Cycles? Middle? What else?]_
- Movement rule: _[How do pointers move at different speeds?]_
- Example problems: _[List problems you solved]_

### Your Decision Tree

Build this after solving practice problems:

```
Two Pointers Pattern Selection
│
├─ Is data sorted?
│   ├─ YES → Continue
│   └─ NO → Can you sort? Or use same-direction for partitioning
│
├─ What are you searching for?
│   │
│   ├─ Pair with property (sum, product)?
│   │   └─ Use: Opposite direction ✓
│   │
│   ├─ Modify array in-place?
│   │   └─ Use: Same direction (slow/fast) ✓
│   │
│   ├─ Linked list property (cycle, middle, kth)?
│   │   └─ Use: Different speed ✓
│   │
│   └─ Check palindrome?
│       └─ Use: Opposite direction ✓
```

### The "Kill Switch" - When NOT to use Two Pointers

**Don't use two pointers when:**
1. _[Fill in - what kind of problems?]_
2. _[Fill in - what data structures?]_
3. _[Fill in - what requirements break it?]_

### The Rule of Three: Alternatives

**Option 1: Two Pointers**
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

**Option 2: Hash Table**
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

**Option 3: Brute Force (Nested Loops)**
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 3):**
- [ ] [125. Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)
  - Pattern: _[Which one?]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [26. Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/)
  - Pattern: _[Which one?]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [283. Move Zeroes](https://leetcode.com/problems/move-zeroes/)
  - Pattern: _[Which one?]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 2-3):**
- [ ] [15. 3Sum](https://leetcode.com/problems/3sum/)
  - Pattern: _[Extension of which pattern?]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_
  - Mistake made: _[Fill in if any]_

- [ ] [11. Container With Most Water](https://leetcode.com/problems/container-with-most-water/)
  - Pattern: _[Which one?]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [167. Two Sum II](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/)
  - Pattern: _[Which one?]_
  - Comparison to Two Sum I: _[How is it different?]_

**Hard (Optional):**
- [ ] [42. Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)
  - Pattern: _[Which variant?]_
  - Key insight: _[Fill in after solving]_

### Problem-Solving Template

For each problem:

```markdown
## Problem: [Name]

**Pattern identified:** _[Which two-pointer variant?]_

**Why two pointers?** _[What makes this a two-pointer problem?]_

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
  - [ ] Opposite direction: palindrome, two sum, reverse all work
  - [ ] Same direction: remove duplicates, move zeros, partition all work
  - [ ] Different speed: cycle detection, find middle, kth from end all work
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify which pattern to use for new problems
  - [ ] Understand when each pattern applies
  - [ ] Know the movement rules for each variant

- [ ] **Problem Solving**
  - [ ] Solved 3 easy problems
  - [ ] Solved 2-3 medium problems
  - [ ] Analyzed time/space complexity

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use two pointers
  - [ ] Can explain trade-offs vs other approaches

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else

---

**Next Topic:** [02. Sliding Window →](02-sliding-window.md)

**Back to:** [Home](../index.md)
