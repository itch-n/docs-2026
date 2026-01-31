# 05. Stacks & Queues

> LIFO and FIFO data structures for tracking state and processing order

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What are stacks and queues in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **What's the key difference between stack and queue?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "A stack is like a stack of plates - last one on, first one off..."
   - Your analogy for stack: _[Fill in]_
   - Your analogy for queue: _[Fill in]_

4. **When does each pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **What problems require stacks vs queues?**
   - Your answer: _[Fill in after practice]_

---

## Core Implementation

### Pattern 1: Basic Stack Operations

**Concept:** Last In, First Out (LIFO) - like a stack of plates.

**Use case:** Undo operations, expression evaluation, backtracking.

```java
import java.util.*;

public class BasicStack {

    /**
     * Problem: Valid parentheses - check if brackets are balanced
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using Stack
     */
    public static boolean isValid(String s) {
        // TODO: Create Stack<Character>

        // TODO: For each char in s:
        //   If opening bracket: push to stack
        //   If closing bracket:
        //     Check if stack empty (return false)
        //     Pop and verify it matches

        // TODO: Return stack.isEmpty()

        return false; // Replace with implementation
    }

    /**
     * Problem: Evaluate Reverse Polish Notation
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement RPN calculator
     */
    public static int evalRPN(String[] tokens) {
        // TODO: Create Stack<Integer>

        // TODO: For each token:
        //   If number: push to stack
        //   If operator:
        //     Pop two operands
        //     Apply operation
        //     Push result

        // TODO: Return stack.peek()

        return 0; // Replace with implementation
    }

    /**
     * Problem: Min Stack - stack with O(1) getMin()
     * Time: O(1) for all operations, Space: O(n)
     *
     * TODO: Implement MinStack class
     */
    static class MinStack {
        // TODO: Use two stacks: one for values, one for minimums

        public void push(int val) {
            // TODO: Push to main stack
            // TODO: Update min stack
        }

        public void pop() {
            // TODO: Pop from both stacks
        }

        public int top() {
            // TODO: Return top of main stack
            return 0;
        }

        public int getMin() {
            // TODO: Return top of min stack
            return 0;
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class BasicStackClient {

    public static void main(String[] args) {
        System.out.println("=== Basic Stack Operations ===\n");

        // Test 1: Valid parentheses
        System.out.println("--- Test 1: Valid Parentheses ---");
        String[] testStrings = {
            "()",
            "()[]{}",
            "(]",
            "([)]",
            "{[]}"
        };

        for (String s : testStrings) {
            boolean valid = BasicStack.isValid(s);
            System.out.printf("\"%s\" -> %s%n", s, valid ? "VALID" : "INVALID");
        }

        // Test 2: Evaluate RPN
        System.out.println("\n--- Test 2: Evaluate RPN ---");
        String[][] rpnTests = {
            {"2", "1", "+", "3", "*"},  // ((2 + 1) * 3) = 9
            {"4", "13", "5", "/", "+"}  // (4 + (13 / 5)) = 6
        };

        for (String[] tokens : rpnTests) {
            int result = BasicStack.evalRPN(tokens);
            System.out.printf("%s = %d%n", Arrays.toString(tokens), result);
        }

        // Test 3: Min Stack
        System.out.println("\n--- Test 3: Min Stack ---");
        BasicStack.MinStack minStack = new BasicStack.MinStack();

        System.out.println("Operations:");
        System.out.println("push(-2)");
        minStack.push(-2);
        System.out.println("push(0)");
        minStack.push(0);
        System.out.println("push(-3)");
        minStack.push(-3);
        System.out.println("getMin() -> " + minStack.getMin());
        System.out.println("pop()");
        minStack.pop();
        System.out.println("top() -> " + minStack.top());
        System.out.println("getMin() -> " + minStack.getMin());
    }
}
```

---

### Pattern 2: Monotonic Stack

**Concept:** Stack that maintains elements in monotonic order.

**Use case:** Next greater element, largest rectangle, temperature problems.

```java
import java.util.*;

public class MonotonicStack {

    /**
     * Problem: Next greater element to the right
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using monotonic decreasing stack
     */
    public static int[] nextGreaterElement(int[] nums) {
        // TODO: Create result array initialized to -1
        // TODO: Create Stack<Integer> to store indices

        // TODO: For i from 0 to nums.length-1:
        //   While stack not empty AND nums[i] > nums[stack.peek()]:
        //     Pop index and set result[index] = nums[i]
        //   Push i to stack

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: Daily temperatures - days until warmer temperature
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using monotonic stack
     */
    public static int[] dailyTemperatures(int[] temperatures) {
        // TODO: Similar to nextGreaterElement
        // TODO: Store days difference instead of values

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: Largest rectangle in histogram
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement using monotonic increasing stack
     */
    public static int largestRectangleArea(int[] heights) {
        // TODO: Use stack to track indices of bars
        // TODO: When we find smaller bar, calculate area
        // TODO: Track maximum area

        return 0; // Replace with implementation
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
        int[] nums1 = {2, 1, 2, 4, 3};

        System.out.println("Array: " + Arrays.toString(nums1));
        int[] result1 = MonotonicStack.nextGreaterElement(nums1);
        System.out.println("Next greater: " + Arrays.toString(result1));

        // Test 2: Daily temperatures
        System.out.println("\n--- Test 2: Daily Temperatures ---");
        int[] temps = {73, 74, 75, 71, 69, 72, 76, 73};

        System.out.println("Temperatures: " + Arrays.toString(temps));
        int[] result2 = MonotonicStack.dailyTemperatures(temps);
        System.out.println("Days to wait: " + Arrays.toString(result2));

        // Test 3: Largest rectangle
        System.out.println("\n--- Test 3: Largest Rectangle ---");
        int[] heights = {2, 1, 5, 6, 2, 3};

        System.out.println("Heights: " + Arrays.toString(heights));
        int maxArea = MonotonicStack.largestRectangleArea(heights);
        System.out.println("Largest rectangle area: " + maxArea);
    }
}
```

---

### Pattern 3: Basic Queue Operations

**Concept:** First In, First Out (FIFO) - like a line of people.

**Use case:** BFS, task scheduling, buffer management.

```java
import java.util.*;

public class BasicQueue {

    /**
     * Problem: Implement queue using two stacks
     * Time: O(1) amortized, Space: O(n)
     *
     * TODO: Implement QueueWithStacks class
     */
    static class QueueWithStacks {
        // TODO: Use two stacks: inbox and outbox

        public void enqueue(int x) {
            // TODO: Push to inbox
        }

        public int dequeue() {
            // TODO: If outbox empty, transfer from inbox
            // TODO: Pop from outbox
            return 0;
        }

        public int peek() {
            // TODO: If outbox empty, transfer from inbox
            // TODO: Peek outbox
            return 0;
        }

        public boolean empty() {
            // TODO: Check if both stacks empty
            return true;
        }
    }

    /**
     * Problem: Implement circular queue
     * Time: O(1), Space: O(k)
     *
     * TODO: Implement CircularQueue class
     */
    static class CircularQueue {
        private int[] data;
        private int front, rear, size, capacity;

        public CircularQueue(int k) {
            // TODO: Initialize array and pointers
        }

        public boolean enQueue(int value) {
            // TODO: Check if full
            // TODO: Add element at rear
            // TODO: Update rear pointer (circular)
            return false;
        }

        public boolean deQueue() {
            // TODO: Check if empty
            // TODO: Update front pointer (circular)
            return false;
        }

        public int front() {
            // TODO: Return element at front
            return -1;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == capacity;
        }
    }
}
```

**Runnable Client Code:**

```java
public class BasicQueueClient {

    public static void main(String[] args) {
        System.out.println("=== Basic Queue Operations ===\n");

        // Test 1: Queue with stacks
        System.out.println("--- Test 1: Queue Using Stacks ---");
        BasicQueue.QueueWithStacks queue = new BasicQueue.QueueWithStacks();

        System.out.println("Operations:");
        System.out.println("enqueue(1)");
        queue.enqueue(1);
        System.out.println("enqueue(2)");
        queue.enqueue(2);
        System.out.println("peek() -> " + queue.peek());
        System.out.println("dequeue() -> " + queue.dequeue());
        System.out.println("empty() -> " + queue.empty());

        // Test 2: Circular queue
        System.out.println("\n--- Test 2: Circular Queue ---");
        BasicQueue.CircularQueue circularQueue = new BasicQueue.CircularQueue(3);

        System.out.println("Operations on queue of size 3:");
        System.out.println("enQueue(1) -> " + circularQueue.enQueue(1));
        System.out.println("enQueue(2) -> " + circularQueue.enQueue(2));
        System.out.println("enQueue(3) -> " + circularQueue.enQueue(3));
        System.out.println("enQueue(4) -> " + circularQueue.enQueue(4)); // false, full
        System.out.println("front() -> " + circularQueue.front());
        System.out.println("isFull() -> " + circularQueue.isFull());
        System.out.println("deQueue() -> " + circularQueue.deQueue());
        System.out.println("enQueue(4) -> " + circularQueue.enQueue(4)); // now succeeds
        System.out.println("front() -> " + circularQueue.front());
    }
}
```

---

### Pattern 4: Deque (Double-Ended Queue)

**Concept:** Add/remove from both ends.

**Use case:** Sliding window maximum, palindrome check.

```java
import java.util.*;

public class DequeOperations {

    /**
     * Problem: Sliding window maximum
     * Time: O(n), Space: O(k)
     *
     * TODO: Implement using monotonic deque
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        // TODO: Use Deque<Integer> to store indices
        // TODO: Maintain decreasing order in deque

        // TODO: For each window:
        //   Remove indices outside window
        //   Remove smaller elements from back
        //   Add current index
        //   Record front element as max

        return new int[0]; // Replace with implementation
    }

    /**
     * Problem: Check if string can be rearranged into palindrome
     * Time: O(n), Space: O(n)
     *
     * TODO: Use deque for efficient insertion at both ends
     */
    public static boolean canFormPalindrome(String s) {
        // TODO: Use frequency map to check odd counts
        // TODO: At most one character can have odd count

        return false; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class DequeOperationsClient {

    public static void main(String[] args) {
        System.out.println("=== Deque Operations ===\n");

        // Test 1: Sliding window maximum
        System.out.println("--- Test 1: Sliding Window Maximum ---");
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;

        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("Window size: " + k);

        int[] result = DequeOperations.maxSlidingWindow(nums, k);
        System.out.println("Maximums: " + Arrays.toString(result));

        // Test 2: Can form palindrome
        System.out.println("\n--- Test 2: Can Form Palindrome ---");
        String[] testStrings = {"aab", "abc", "racecar", "hello"};

        for (String s : testStrings) {
            boolean canForm = DequeOperations.canFormPalindrome(s);
            System.out.printf("\"%s\" -> %s%n", s, canForm ? "YES" : "NO");
        }
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for stack/queue selection.

### Question 1: LIFO vs FIFO?

Answer after solving problems:
- **Need last item first?** _[Use stack]_
- **Need first item first?** _[Use queue]_
- **Need both ends?** _[Use deque]_
- **Your observation:** _[Fill in based on testing]_

### Question 2: When to use each pattern?

**Stack patterns:**
- Valid parentheses: _[Why stack?]_
- Expression evaluation: _[Why stack?]_
- Monotonic stack: _[What problems?]_

**Queue patterns:**
- BFS: _[Why queue?]_
- Level order traversal: _[Why queue?]_
- Task scheduling: _[Why queue?]_

### Your Decision Tree

```
Stack vs Queue Selection
│
├─ Need to track most recent?
│   └─ Use: Stack ✓
│
├─ Need to process in order?
│   └─ Use: Queue ✓
│
├─ Need to find next greater/smaller?
│   └─ Use: Monotonic Stack ✓
│
├─ Need to access both ends?
│   └─ Use: Deque ✓
│
└─ Need min/max with updates?
    └─ Use: Monotonic Deque ✓
```

### The "Kill Switch" - When NOT to use Stacks/Queues

**Don't use when:**
1. _[Need random access? Use array/list]_
2. _[Need to search? Use hash table]_
3. _[Need sorted order? Use heap/tree]_
4. _[Need both LIFO and FIFO? Use deque]_

### The Rule of Three: Alternatives

**Option 1: Stack**
- Pros: _[O(1) push/pop, simple]_
- Cons: _[Only access top, no random access]_
- Use when: _[LIFO order needed]_

**Option 2: Queue**
- Pros: _[O(1) enqueue/dequeue, FIFO]_
- Cons: _[Only access front/rear]_
- Use when: _[FIFO order needed]_

**Option 3: Deque**
- Pros: _[O(1) at both ends, flexible]_
- Cons: _[Slightly more complex]_
- Use when: _[Need access to both ends]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**
- [ ] [20. Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)
  - Pattern: _[Basic stack]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [232. Implement Queue using Stacks](https://leetcode.com/problems/implement-queue-using-stacks/)
  - Pattern: _[Queue with stacks]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [225. Implement Stack using Queues](https://leetcode.com/problems/implement-stack-using-queues/)
  - Pattern: _[Stack with queues]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [155. Min Stack](https://leetcode.com/problems/min-stack/)
  - Pattern: _[Stack with tracking]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 3-4):**
- [ ] [739. Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)
  - Pattern: _[Monotonic stack]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [150. Evaluate Reverse Polish Notation](https://leetcode.com/problems/evaluate-reverse-polish-notation/)
  - Pattern: _[Basic stack]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [394. Decode String](https://leetcode.com/problems/decode-string/)
  - Pattern: _[Stack]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [622. Design Circular Queue](https://leetcode.com/problems/design-circular-queue/)
  - Pattern: _[Circular queue]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

**Hard (Optional):**
- [ ] [84. Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/)
  - Pattern: _[Monotonic stack]_
  - Key insight: _[Fill in after solving]_

- [ ] [239. Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)
  - Pattern: _[Monotonic deque]_
  - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Basic stack: valid parentheses, RPN, min stack all work
  - [ ] Monotonic stack: next greater, daily temps all work
  - [ ] Queue: queue with stacks, circular queue both work
  - [ ] Deque: sliding window maximum works
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify when to use stack vs queue
  - [ ] Understand monotonic stack pattern
  - [ ] Know when to use deque
  - [ ] Recognize valid parentheses variants

- [ ] **Problem Solving**
  - [ ] Solved 4 easy problems
  - [ ] Solved 3-4 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Handled edge cases (empty, single element)

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use stacks/queues
  - [ ] Can explain LIFO vs FIFO clearly

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand amortized analysis for queue with stacks

---

**Next Topic:** [06. Trees - Traversals →](06-trees-traversals.md)

**Back to:** [04. Linked Lists ←](04-linked-lists.md)
