# 06. Trees - Traversals

> Visit every node in a tree using Inorder, Preorder, Postorder, or Level-Order

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What are tree traversals in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **Why do we need different traversal orders?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
    - Example: "Tree traversals are like different ways to read a family tree..."
    - Your analogy: _[Fill in]_

4. **When does each traversal order matter?**
    - Your answer: _[Fill in after solving problems]_

5. **What's the difference between iterative and recursive?**
    - Your answer: _[Fill in after implementation]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Visiting every node in a tree:**
    - Time complexity: _[Your guess: O(?)]_
    - Verified after learning: _[Actual: O(?)]_

2. **Recursive inorder traversal:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified: _[Actual]_

3. **Iterative vs Recursive space:**
    - Recursive space usage: _[O(?) - what uses the space?]_
    - Iterative space usage: _[O(?) - what data structure?]_
    - Morris traversal space: _[O(?)]_

### Scenario Predictions

**Scenario 1:** Traverse this BST and predict output for each traversal

```
Tree:     4
         / \
        2   6
       / \
      1   3
```

- **Inorder (Left, Root, Right):** _[Predict: ?, ?, ?, ?, ?]_
- **Preorder (Root, Left, Right):** _[Predict: ?, ?, ?, ?, ?]_
- **Postorder (Left, Right, Root):** _[Predict: ?, ?, ?, ?, ?]_
- **Level-order (BFS):** _[Predict: [[?], [?, ?], [?, ?]]]_

**Verify after implementation:** Were your predictions correct? _[Yes/No]_

**Scenario 2:** Why does inorder give sorted output for BST?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Scenario 3:** Which traversal to use for deleting a tree?

- Your guess: _[Inorder/Preorder/Postorder/Level-order - Why?]_
- Reasoning: _[Fill in your logic]_
- Verified: _[After implementation]_

### Trade-off Quiz

**Question:** When would iterative traversal be BETTER than recursive?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question:** What's the MAIN advantage of Morris traversal?

- [ ] Faster than recursive
- [ ] O(1) space complexity
- [ ] Easier to implement
- [ ] Works for all tree types

Verify after implementation: _[Which one(s)?]_

**Question:** Which traversal order matters for expression trees?

- Infix notation uses: _[Which traversal?]_
- Prefix notation uses: _[Which traversal?]_
- Postfix notation uses: _[Which traversal?]_

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand the impact.

### Example: Collecting Tree Values

**Problem:** Get all values from a tree in sorted order (BST).

#### Approach 1: Collect All, Then Sort

```java
// Naive approach - Collect values in any order, then sort
public static List<Integer> getTreeValues_BruteForce(TreeNode root) {
    List<Integer> result = new ArrayList<>();

    // Collect values using preorder (any order)
    collectValues(root, result);

    // Sort the collected values
    Collections.sort(result);

    return result;
}

private static void collectValues(TreeNode node, List<Integer> result) {
    if (node == null) return;
    result.add(node.val);
    collectValues(node.left, result);
    collectValues(node.right, result);
}
```

**Analysis:**

- Time: O(n log n) - O(n) to collect + O(n log n) to sort
- Space: O(n) for list + O(log n) for recursion stack
- For n = 10,000: ~140,000 operations

#### Approach 2: Inorder Traversal (Optimized)

```java
// Optimized approach - Use inorder traversal for BST
public static List<Integer> getTreeValues_Inorder(TreeNode root) {
    List<Integer> result = new ArrayList<>();

    inorderHelper(root, result);

    return result; // Already sorted!
}

private static void inorderHelper(TreeNode node, List<Integer> result) {
    if (node == null) return;

    inorderHelper(node.left, result);   // Visit left subtree
    result.add(node.val);                // Visit root
    inorderHelper(node.right, result);   // Visit right subtree
}
```

**Analysis:**

- Time: O(n) - Visit each node exactly once
- Space: O(n) for list + O(h) for recursion stack (h = height)
- For n = 10,000: ~10,000 operations

#### Performance Comparison

| Tree Size | Collect + Sort (O(n log n)) | Inorder (O(n)) | Speedup |
|-----------|----------------------------|----------------|---------|
| n = 100   | ~664 ops                   | 100 ops        | 6.6x    |
| n = 1,000 | ~9,966 ops                 | 1,000 ops      | 10x     |
| n = 10,000| ~132,877 ops               | 10,000 ops     | 13x     |

**Your calculation:** For n = 5,000, the speedup is approximately _____ times faster.

#### Recursive vs Iterative: Stack Space

**Problem:** Inorder traversal of a deeply nested tree.

**Approach 1: Recursive**

```java
public static void inorderRecursive(TreeNode root) {
    if (root == null) return;

    inorderRecursive(root.left);
    System.out.print(root.val + " ");
    inorderRecursive(root.right);
}
```

**Analysis:**

- Space: O(h) where h = tree height
- For balanced tree: h = log n (good!)
- For skewed tree: h = n (danger of stack overflow!)
- Example: Tree with 100,000 nodes in a line = 100,000 recursive calls

**Approach 2: Iterative with Explicit Stack**

```java
public static void inorderIterative(TreeNode root) {
    Stack<TreeNode> stack = new Stack<>();
    TreeNode curr = root;

    while (curr != null || !stack.isEmpty()) {
        while (curr != null) {
            stack.push(curr);
            curr = curr.left;
        }
        curr = stack.pop();
        System.out.print(curr.val + " ");
        curr = curr.right;
    }
}
```

**Analysis:**

- Space: O(h) - same as recursive, but explicit stack
- No stack overflow risk - heap memory is larger
- More control over the process
- Production-safe for deep trees

#### Why Does Traversal Order Matter?

**Key insight to understand:**

For this tree:
```
       4
      / \
     2   6
    / \
   1   3
```

**Inorder (Left, Root, Right):** 1, 2, 3, 4, 6
- Visits left child before parent
- **Use case:** Get sorted values from BST

**Preorder (Root, Left, Right):** 4, 2, 1, 3, 6
- Visits parent before children
- **Use case:** Copy tree structure, serialize tree

**Postorder (Left, Right, Root):** 1, 3, 2, 6, 4
- Visits children before parent
- **Use case:** Delete tree (delete children first!)

**Level-order (BFS):** [[4], [2, 6], [1, 3]]
- Visits level by level
- **Use case:** Find shortest path, level-wise processing

**After implementing, explain in your own words:**

- _[Why does postorder make sense for tree deletion?]_
- _[Why does preorder make sense for copying a tree?]_
- _[When would level-order be preferred over depth-first?]_

---

## Core Implementation

### Pattern 1: Inorder Traversal (Left, Root, Right)

**Concept:** Visit left subtree, then root, then right subtree.

**Use case:** Get sorted order from BST, expression evaluation.

```java
import java.util.*;

public class InorderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Inorder traversal recursively
     * Time: O(n), Space: O(h) where h = height for recursion stack
     *
     * TODO: Implement recursive inorder
     */
    public static List<Integer> inorderRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // TODO: Base case: if root is null, return

        // TODO: Recursively traverse left subtree
        // TODO: Visit root (add to result)
        // TODO: Recursively traverse right subtree

        return result; // Replace with implementation
    }

    /**
     * Problem: Inorder traversal iteratively using stack
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement iterative inorder
     */
    public static List<Integer> inorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        // TODO: Create Stack<TreeNode>

        // TODO: curr = root
        // TODO: While curr != null OR stack not empty:
        //   While curr != null:
        //     Push curr to stack
        //     Move curr = curr.left
        //   Pop curr from stack
        //   Visit curr (add to result)
        //   Move curr = curr.right

        return result; // Replace with implementation
    }

    /**
     * Problem: Inorder traversal with Morris (no extra space)
     * Time: O(n), Space: O(1)
     *
     * TODO: Implement Morris traversal
     */
    public static List<Integer> inorderMorris(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // TODO: curr = root
        // TODO: While curr != null:
        //   If curr.left is null:
        //     Visit curr, move curr = curr.right
        //   Else:
        //     Find predecessor (rightmost in left subtree)
        //     If predecessor.right is null:
        //       Make predecessor.right = curr (thread)
        //       Move curr = curr.left
        //     Else:
        //       Remove thread, visit curr, move curr = curr.right

        return result; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class InorderTraversalClient {

    public static void main(String[] args) {
        System.out.println("=== Inorder Traversal ===\n");

        // Create tree:
        //       4
        //      / \
        //     2   6
        //    / \ / \
        //   1  3 5  7
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);

        System.out.println("Tree structure:");
        System.out.println("       4");
        System.out.println("      / \\");
        System.out.println("     2   6");
        System.out.println("    / \\ / \\");
        System.out.println("   1  3 5  7");
        System.out.println();

        // Test 1: Recursive inorder
        System.out.println("--- Test 1: Recursive Inorder ---");
        List<Integer> recursive = InorderTraversal.inorderRecursive(root);
        System.out.println("Result: " + recursive);
        System.out.println("(Should be: [1, 2, 3, 4, 5, 6, 7])");

        // Test 2: Iterative inorder
        System.out.println("\n--- Test 2: Iterative Inorder ---");
        List<Integer> iterative = InorderTraversal.inorderIterative(root);
        System.out.println("Result: " + iterative);

        // Test 3: Morris inorder
        System.out.println("\n--- Test 3: Morris Inorder (O(1) space) ---");
        List<Integer> morris = InorderTraversal.inorderMorris(root);
        System.out.println("Result: " + morris);
    }
}
```

---

### Pattern 2: Preorder Traversal (Root, Left, Right)

**Concept:** Visit root first, then left subtree, then right subtree.

**Use case:** Create copy of tree, prefix expression, serialize tree.

```java
import java.util.*;

public class PreorderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Preorder traversal recursively
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement recursive preorder
     */
    public static List<Integer> preorderRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // TODO: Base case: if root is null, return

        // TODO: Visit root (add to result)
        // TODO: Recursively traverse left subtree
        // TODO: Recursively traverse right subtree

        return result; // Replace with implementation
    }

    /**
     * Problem: Preorder traversal iteratively using stack
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement iterative preorder
     */
    public static List<Integer> preorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // TODO: If root is null, return empty
        // TODO: Create Stack<TreeNode>, push root

        // TODO: While stack not empty:
        //   Pop node
        //   Visit node (add to result)
        //   Push right child (if exists)
        //   Push left child (if exists)

        return result; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class PreorderTraversalClient {

    public static void main(String[] args) {
        System.out.println("=== Preorder Traversal ===\n");

        // Create tree:
        //       4
        //      / \
        //     2   6
        //    / \ / \
        //   1  3 5  7
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);

        System.out.println("Tree structure:");
        System.out.println("       4");
        System.out.println("      / \\");
        System.out.println("     2   6");
        System.out.println("    / \\ / \\");
        System.out.println("   1  3 5  7");
        System.out.println();

        // Test 1: Recursive preorder
        System.out.println("--- Test 1: Recursive Preorder ---");
        List<Integer> recursive = PreorderTraversal.preorderRecursive(root);
        System.out.println("Result: " + recursive);
        System.out.println("(Should be: [4, 2, 1, 3, 6, 5, 7])");

        // Test 2: Iterative preorder
        System.out.println("\n--- Test 2: Iterative Preorder ---");
        List<Integer> iterative = PreorderTraversal.preorderIterative(root);
        System.out.println("Result: " + iterative);
    }
}
```

---

### Pattern 3: Postorder Traversal (Left, Right, Root)

**Concept:** Visit left subtree, then right subtree, then root.

**Use case:** Delete tree, calculate directory size, postfix expression.

```java
import java.util.*;

public class PostorderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Postorder traversal recursively
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement recursive postorder
     */
    public static List<Integer> postorderRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // TODO: Base case: if root is null, return

        // TODO: Recursively traverse left subtree
        // TODO: Recursively traverse right subtree
        // TODO: Visit root (add to result)

        return result; // Replace with implementation
    }

    /**
     * Problem: Postorder traversal iteratively using two stacks
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement iterative postorder
     */
    public static List<Integer> postorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // TODO: If root is null, return empty
        // TODO: Create two stacks: stack1 and stack2
        // TODO: Push root to stack1

        // TODO: While stack1 not empty:
        //   Pop node from stack1
        //   Push node to stack2
        //   Push left child to stack1 (if exists)
        //   Push right child to stack1 (if exists)

        // TODO: Pop all from stack2 to result

        return result; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class PostorderTraversalClient {

    public static void main(String[] args) {
        System.out.println("=== Postorder Traversal ===\n");

        // Create tree:
        //       4
        //      / \
        //     2   6
        //    / \ / \
        //   1  3 5  7
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);

        System.out.println("Tree structure:");
        System.out.println("       4");
        System.out.println("      / \\");
        System.out.println("     2   6");
        System.out.println("    / \\ / \\");
        System.out.println("   1  3 5  7");
        System.out.println();

        // Test 1: Recursive postorder
        System.out.println("--- Test 1: Recursive Postorder ---");
        List<Integer> recursive = PostorderTraversal.postorderRecursive(root);
        System.out.println("Result: " + recursive);
        System.out.println("(Should be: [1, 3, 2, 5, 7, 6, 4])");

        // Test 2: Iterative postorder
        System.out.println("\n--- Test 2: Iterative Postorder ---");
        List<Integer> iterative = PostorderTraversal.postorderIterative(root);
        System.out.println("Result: " + iterative);
    }
}
```

---

### Pattern 4: Level-Order Traversal (BFS)

**Concept:** Visit nodes level by level, left to right.

**Use case:** Find shortest path, serialize by level, level-wise processing.

```java
import java.util.*;

public class LevelOrderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Level-order traversal using queue
     * Time: O(n), Space: O(w) where w = max width
     *
     * TODO: Implement BFS using queue
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();

        // TODO: If root is null, return empty
        // TODO: Create Queue<TreeNode>, add root

        // TODO: While queue not empty:
        //   Get level size = queue.size()
        //   Create level list
        //   For i from 0 to level size:
        //     Poll node
        //     Add node.val to level list
        //     Add left child to queue (if exists)
        //     Add right child to queue (if exists)
        //   Add level list to result

        return result; // Replace with implementation
    }

    /**
     * Problem: Level-order traversal in zigzag pattern
     * Time: O(n), Space: O(w)
     *
     * TODO: Implement zigzag traversal
     */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();

        // TODO: Similar to levelOrder but alternate direction
        // TODO: Use boolean flag to track left-to-right vs right-to-left
        // TODO: Reverse level list when going right-to-left

        return result; // Replace with implementation
    }

    /**
     * Problem: Right side view of tree
     * Time: O(n), Space: O(w)
     *
     * TODO: Implement right side view
     */
    public static List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        // TODO: Use level-order traversal
        // TODO: Add last node of each level to result

        return result; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class LevelOrderTraversalClient {

    public static void main(String[] args) {
        System.out.println("=== Level-Order Traversal ===\n");

        // Create tree:
        //       4
        //      / \
        //     2   6
        //    / \ / \
        //   1  3 5  7
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);

        System.out.println("Tree structure:");
        System.out.println("       4");
        System.out.println("      / \\");
        System.out.println("     2   6");
        System.out.println("    / \\ / \\");
        System.out.println("   1  3 5  7");
        System.out.println();

        // Test 1: Level-order
        System.out.println("--- Test 1: Level-Order Traversal ---");
        List<List<Integer>> levels = LevelOrderTraversal.levelOrder(root);
        System.out.println("Result: " + levels);
        System.out.println("(Should be: [[4], [2, 6], [1, 3, 5, 7]])");

        // Test 2: Zigzag level-order
        System.out.println("\n--- Test 2: Zigzag Level-Order ---");
        List<List<Integer>> zigzag = LevelOrderTraversal.zigzagLevelOrder(root);
        System.out.println("Result: " + zigzag);
        System.out.println("(Should be: [[4], [6, 2], [1, 3, 5, 7]])");

        // Test 3: Right side view
        System.out.println("\n--- Test 3: Right Side View ---");
        List<Integer> rightView = LevelOrderTraversal.rightSideView(root);
        System.out.println("Result: " + rightView);
        System.out.println("(Should be: [4, 6, 7])");
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken implementations. This tests your understanding of traversal mechanics.

### Challenge 1: Broken Inorder Traversal

```java
/**
 * This recursive inorder is supposed to return [1, 2, 3, 4, 5]
 * for a BST, but it has 2 BUGS. Find them!
 */
public static List<Integer> inorderRecursive_Buggy(TreeNode root) {
    List<Integer> result = new ArrayList<>();

    if (root != null) {  // Base case check
        inorderRecursive_Buggy(root.left);   // BUG 1: What's missing?
        result.add(root.val);
        inorderRecursive_Buggy(root.right);
    }

    return result;  // BUG 2: What does this return?
}
```

**Your debugging:**

- **Bug 1 location:** _[Which lines?]_
- **Bug 1 explanation:** _[What's wrong with the recursive calls?]_
- **Bug 1 fix:** _[How to fix?]_

- **Bug 2 location:** _[Which line?]_
- **Bug 2 explanation:** _[What gets returned? Why is the result empty?]_
- **Bug 2 fix:** _[How should this work?]_

**Test case:**

- Input: Tree with values 1, 2, 3
- Expected: [1, 2, 3]
- Actual with buggy code: _[What do you get?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** The recursive calls don't use the returned result! Each recursive call creates a new empty list.

**Bug 2:** We're creating a new `result` list in each call, so left and right subtree results are lost.

**Fix - Need to pass result as parameter:**
```java
public static List<Integer> inorderRecursive(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    inorderHelper(root, result);
    return result;
}

private static void inorderHelper(TreeNode root, List<Integer> result) {
    if (root == null) return;

    inorderHelper(root.left, result);
    result.add(root.val);
    inorderHelper(root.right, result);
}
```
</details>

---

### Challenge 2: Broken Iterative Inorder

```java
/**
 * Iterative inorder with stack.
 * This has 2 CRITICAL BUGS with stack logic.
 */
public static List<Integer> inorderIterative_Buggy(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Stack<TreeNode> stack = new Stack<>();
    TreeNode curr = root;

    while (!stack.isEmpty()) {  // BUG 1: Wrong loop condition!
        while (curr != null) {
            stack.push(curr);
            curr = curr.left;
        }

        TreeNode node = stack.pop();
        result.add(node.val);
        curr = curr.left;  // BUG 2: Wrong direction!
    }

    return result;
}
```

**Your debugging:**

- **Bug 1:** _[What's wrong with the while condition?]_
- **Bug 1 effect:** _[What happens? When does loop start/stop?]_
- **Bug 1 fix:** _[Correct condition]_

- **Bug 2:** _[Which direction should curr move?]_
- **Bug 2 effect:** _[What happens? Infinite loop? Wrong order?]_
- **Bug 2 fix:** _[Fill in]_

**Trace through example:**

- Input: Tree with values 1, 2, 3
- Expected: [1, 2, 3]
- With Bug 1: _[What happens?]_
- With Bug 2: _[What happens?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** Loop condition should be `while (curr != null || !stack.isEmpty())`. Current code won't even start if root is not null.

**Bug 2:** Should be `curr = node.right`, not `curr = curr.left`. After visiting a node, we need to go to its right subtree.

**Correct code:**
```java
while (curr != null || !stack.isEmpty()) {
    while (curr != null) {
        stack.push(curr);
        curr = curr.left;
    }
    TreeNode node = stack.pop();
    result.add(node.val);
    curr = node.right;  // Go right after visiting
}
```
</details>

---

### Challenge 3: Broken Postorder Traversal

```java
/**
 * Postorder using two stacks.
 * This has 1 LOGIC BUG in the order of operations.
 */
public static List<Integer> postorderIterative_Buggy(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;

    Stack<TreeNode> stack1 = new Stack<>();
    Stack<TreeNode> stack2 = new Stack<>();
    stack1.push(root);

    while (!stack1.isEmpty()) {
        TreeNode node = stack1.pop();
        stack2.push(node);

        // BUG: Wrong order! What should the order be?
        if (node.right != null) stack1.push(node.right);
        if (node.left != null) stack1.push(node.left);
    }

    while (!stack2.isEmpty()) {
        result.add(stack2.pop().val);
    }

    return result;
}
```

**Your debugging:**

- **Bug location:** _[Which lines push to stack1?]_
- **Bug explanation:** _[Why does order matter here?]_
- **Expected order:** _[Postorder is Left, Right, Root - so what should we push first?]_

**Think through it:**

- Postorder visits: Left, Right, Root
- Stack2 reverses the order
- So stack1 should create what order? _[Fill in your reasoning]_

<details markdown>
<summary>Click to verify your answer</summary>

**Actually, this code is CORRECT!** It's a trick question.

**Why it works:**

- We want postorder: Left, Right, Root
- Stack2 reverses the order we put in
- So we create: Root, Right, Left (which reverses to Left, Right, Root)
- To create Root, Right, Left in stack2, we push Right then Left to stack1

**The code is fine as-is.** This tests if you understand the two-stack technique!
</details>

---

### Challenge 4: Broken Level-Order Traversal

```java
/**
 * Level-order traversal using queue.
 * This has 2 BUGS with level tracking.
 */
public static List<List<Integer>> levelOrder_Buggy(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
        List<Integer> level = new ArrayList<>();

        // BUG 1: Not tracking level size correctly!
        while (!queue.isEmpty()) {  // Wrong loop!
            TreeNode node = queue.poll();
            level.add(node.val);

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        result.add(level);
    }

    return result;
}
```

**Your debugging:**

- **Bug location:** _[Which loop is wrong?]_
- **Bug explanation:** _[What happens? Do all levels get mixed together?]_
- **Bug fix:** _[How to track each level separately?]_

**Test case:**
```
Tree:     4
         / \
        2   6
       /
      1
```
- Expected: [[4], [2, 6], [1]]
- Actual with buggy code: _[Trace through - what do you get?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** The inner while loop processes until the queue is empty, which means it processes ALL levels at once, not one level at a time.

**Fix - Capture level size before inner loop:**
```java
while (!queue.isEmpty()) {
    int levelSize = queue.size();  // Capture current level size
    List<Integer> level = new ArrayList<>();

    for (int i = 0; i < levelSize; i++) {  // Only process current level
        TreeNode node = queue.poll();
        level.add(node.val);

        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }

    result.add(level);
}
```

**Why:** By capturing `queue.size()` before the loop, we know exactly how many nodes are in the current level. New nodes added during the loop belong to the NEXT level.
</details>

---

### Challenge 5: Stack vs Queue Confusion

```java
/**
 * This is supposed to do level-order traversal.
 * But someone used the WRONG data structure!
 */
public static List<Integer> traversal_Buggy(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;

    Stack<TreeNode> stack = new Stack<>();  // BUG: Wrong data structure!
    stack.push(root);

    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);

        if (node.left != null) stack.push(node.left);
        if (node.right != null) stack.push(node.right);
    }

    return result;
}
```

**Your debugging:**

- **What traversal does this actually perform?** _[Hint: Stack = DFS, Queue = BFS]_
- **What would the output be for a simple tree?** _[Trace through]_
- **If we want level-order, what should we use?** _[Stack/Queue/Other?]_

**Key insight to understand:**

- Stack (LIFO) gives you: _[BFS/DFS - which one?]_
- Queue (FIFO) gives you: _[BFS/DFS - which one?]_

<details markdown>
<summary>Click to verify your answer</summary>

**This performs PREORDER (DFS), not LEVEL-ORDER (BFS)!**

**Why:**

- Stack is LIFO (Last In, First Out) - goes deep first
- Queue is FIFO (First In, First Out) - goes wide first

**Fix:**
```java
Queue<TreeNode> queue = new LinkedList<>();  // Use Queue!
queue.offer(root);

while (!queue.isEmpty()) {
    TreeNode node = queue.poll();
    result.add(node.val);

    if (node.left != null) queue.offer(node.left);
    if (node.right != null) queue.offer(node.right);
}
```
</details>

---

### Challenge 6: Null Pointer Trap

```java
/**
 * Morris traversal attempt.
 * This has a CRITICAL null pointer bug!
 */
public static List<Integer> morrisTraversal_Buggy(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    TreeNode curr = root;

    while (curr != null) {
        if (curr.left == null) {
            result.add(curr.val);
            curr = curr.right;
        } else {
            // Find predecessor
            TreeNode pred = curr.left;
            while (pred.right != null) {  // BUG: What if pred.right == curr?
                pred = pred.right;
            }

            // Create thread
            pred.right = curr;
            curr = curr.left;
        }
    }

    return result;
}
```

**Your debugging:**

- **Bug:** _[What causes infinite loop?]_
- **When does it happen?** _[When we revisit a threaded node]_
- **Fix:** _[What condition should we check in the while loop?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** The while loop `while (pred.right != null)` will loop forever once we create a thread (pred.right = curr), because we never check if pred.right == curr.

**Fix - Check for existing thread:**
```java
TreeNode pred = curr.left;
while (pred.right != null && pred.right != curr) {  // Check for thread!
    pred = pred.right;
}

if (pred.right == null) {
    // No thread yet - create it
    pred.right = curr;
    curr = curr.left;
} else {
    // Thread exists - remove it, visit node, go right
    pred.right = null;
    result.add(curr.val);
    curr = curr.right;
}
```
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 8+ bugs across 6 challenges
- [ ] Understood WHY each bug causes incorrect behavior
- [ ] Could explain the fix to someone else
- [ ] Learned common traversal mistakes to avoid

**Common mistakes you discovered:**

1. _[List the patterns you noticed]_
2. _[Fill in]_
3. _[Fill in]_

**Key insights:**

- Recursive traversals need: _[What pattern for combining results?]_
- Iterative inorder needs: _[What loop condition?]_
- Level-order needs: _[Stack or Queue?]_
- Morris traversal needs: _[What check to avoid infinite loops?]_

---

## Decision Framework

**Your task:** Build decision trees for tree traversal selection.

### Question 1: Which traversal order do you need?

Answer after solving problems:
- **Need sorted order from BST?** _[Use inorder]_
- **Need to copy tree structure?** _[Use preorder]_
- **Need to delete tree safely?** _[Use postorder]_
- **Need level-by-level processing?** _[Use level-order]_

### Question 2: Recursive vs Iterative?

**Recursive approach:**

- Pros: _[Simpler code, cleaner logic]_
- Cons: _[O(h) stack space, risk of stack overflow]_
- Use when: _[Tree depth is reasonable]_

**Iterative approach:**

- Pros: _[Explicit control, no stack overflow]_
- Cons: _[More complex code, need explicit stack/queue]_
- Use when: _[Deep trees, production code]_

### Your Decision Tree

```
Tree Traversal Selection
│
├─ What order do you need?
│   │
│   ├─ Left, Root, Right (sorted in BST)?
│   │   └─ Use: Inorder ✓
│   │
│   ├─ Root, Left, Right (copy/serialize)?
│   │   └─ Use: Preorder ✓
│   │
│   ├─ Left, Right, Root (delete/cleanup)?
│   │   └─ Use: Postorder ✓
│   │
│   └─ Level by level (BFS)?
│       └─ Use: Level-Order ✓
│
└─ Implementation choice?
    ├─ Recursive → Simpler, but O(h) space
    └─ Iterative → More code, but safer
```

### The "Kill Switch" - When NOT to use Tree Traversals

**Don't use when:**

1. _[Need to search for specific value? Use BST search instead]_
2. _[Tree is extremely deep? Risk stack overflow with recursion]_
3. _[Need random access? Trees don't support O(1) access]_
4. _[Need sorted iteration frequently? Store in array instead]_

### The Rule of Three: Alternatives

**Option 1: Inorder Traversal**

- Pros: _[Sorted order in BST, standard for iteration]_
- Cons: _[Not useful for non-BST trees]_
- Use when: _[Need sorted processing of BST]_

**Option 2: Level-Order Traversal**

- Pros: _[Shortest path, level-wise processing]_
- Cons: _[More memory for wide trees]_
- Use when: _[BFS needed, level matters]_

**Option 3: Preorder/Postorder**

- Pros: _[Structural operations, serialization]_
- Cons: _[No sorted order guarantee]_
- Use when: _[Copy, serialize, or delete tree]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**

- [ ] [94. Binary Tree Inorder Traversal](https://leetcode.com/problems/binary-tree-inorder-traversal/)
    - Pattern: _[Inorder - recursive/iterative/Morris]_
    - Your solution time: ___
    - Key insight: _[Fill in after solving]_

- [ ] [144. Binary Tree Preorder Traversal](https://leetcode.com/problems/binary-tree-preorder-traversal/)
    - Pattern: _[Preorder]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

- [ ] [145. Binary Tree Postorder Traversal](https://leetcode.com/problems/binary-tree-postorder-traversal/)
    - Pattern: _[Postorder]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

- [ ] [102. Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/)
    - Pattern: _[Level-order BFS]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

**Medium (Complete 3-4):**

- [ ] [103. Binary Tree Zigzag Level Order Traversal](https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/)
    - Pattern: _[Level-order with direction alternation]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [199. Binary Tree Right Side View](https://leetcode.com/problems/binary-tree-right-side-view/)
    - Pattern: _[Level-order, track rightmost]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [107. Binary Tree Level Order Traversal II](https://leetcode.com/problems/binary-tree-level-order-traversal-ii/)
    - Pattern: _[Level-order bottom-up]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [230. Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)
    - Pattern: _[Inorder with counter]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

**Hard (Optional):**

- [ ] [297. Serialize and Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)
    - Pattern: _[Preorder or level-order]_
    - Key insight: _[Fill in after solving]_

- [ ] [987. Vertical Order Traversal of a Binary Tree](https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/)
    - Pattern: _[Custom traversal with coordinates]_
    - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] Inorder: recursive, iterative, Morris all work
    - [ ] Preorder: recursive and iterative both work
    - [ ] Postorder: recursive and iterative both work
    - [ ] Level-order: standard, zigzag, right view all work
    - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
    - [ ] Can identify which traversal order to use
    - [ ] Understand when to use recursive vs iterative
    - [ ] Know BFS vs DFS trade-offs
    - [ ] Recognize when Morris traversal helps

- [ ] **Problem Solving**
    - [ ] Solved 4 easy problems
    - [ ] Solved 3-4 medium problems
    - [ ] Analyzed time/space complexity
    - [ ] Handled edge cases (null, single node)

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Built decision tree
    - [ ] Identified when NOT to use traversals
    - [ ] Can explain each traversal order's purpose

- [ ] **Mastery Check**
    - [ ] Could implement all patterns from memory
    - [ ] Could recognize pattern in new problem
    - [ ] Could explain to someone else
    - [ ] Understand stack space vs heap space trade-offs

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about tree traversals.

**Your explanation (write it out):**

> "Tree traversals are different ways to visit every node in a tree..."
>
> _[Fill in your explanation in plain English - 3-4 sentences max]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by a non-technical person? _[Yes/No]_
- Did you use analogies or real-world examples? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw the execution of inorder traversal on this tree, without looking at code.

**Draw the tree and visitation order:**

```
Tree:     4
         / \
        2   6
       / \
      1   3

Step-by-step inorder visitation (Left, Root, Right):

1. [Your drawing - show what happens first]
   _________________________________

2. [Your drawing - next step]
   _________________________________

3. [Continue until all nodes visited]
   _________________________________

Final order: _________________________________
```

**Verification:**

- [ ] Drew call stack or iteration steps correctly
- [ ] Showed correct visitation order
- [ ] Explained why each node was visited in that order
- [ ] Final result is: [1, 2, 3, 4, 6]

---

### Gate 3: Pattern Recognition Test

**Without looking at your notes, classify these problems:**

| Problem | Which Traversal? | Why? |
|---------|-----------------|------|
| Get sorted values from BST | _[Fill in]_ | _[Explain]_ |
| Copy a tree structure | _[Fill in]_ | _[Explain]_ |
| Delete entire tree safely | _[Fill in]_ | _[Explain]_ |
| Find shortest path in tree | _[Fill in]_ | _[Explain]_ |
| Serialize tree for storage | _[Fill in]_ | _[Explain]_ |
| Find kth smallest in BST | _[Fill in]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the patterns and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Traversal | Time Complexity | Space Complexity (Recursive) | Space Complexity (Iterative) | Why? |
|-----------|----------------|------------------------------|------------------------------|------|
| Inorder | O(?) | O(?) | O(?) | _[Explain]_ |
| Preorder | O(?) | O(?) | O(?) | _[Explain]_ |
| Postorder | O(?) | O(?) | O(?) | _[Explain]_ |
| Level-order | O(?) | N/A | O(?) | _[Explain]_ |
| Morris | O(?) | N/A | O(?) | _[Explain]_ |

**Deep question:** Why is Morris traversal O(1) space even though we're "modifying" the tree?

Your answer: _[Fill in - explain the threading technique]_

**Deep question 2:** For a balanced tree, what's the space difference between recursive and iterative?

Your answer: _[Fill in - both are O(h) = O(log n), but why does it matter?]_

---

### Gate 5: Trade-off Decision

**Scenario:** You need to traverse a binary tree with 1 million nodes, but the tree is highly skewed (like a linked list).

**Option A:** Recursive inorder
- Stack depth: _[Fill in - how deep?]_
- Risk: _[Fill in - what could go wrong?]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option B:** Iterative inorder with explicit stack
- Stack depth: _[Fill in - same as recursive?]_
- Risk: _[Fill in - lower risk?]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option C:** Morris traversal
- Space complexity: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Your decision:** I would choose _[A/B/C]_ because...

_[Fill in your reasoning - consider stack overflow risk, code complexity, space constraints]_

**What would make you change your decision?**

- _[Fill in - what constraints would flip your choice?]_

---

### Gate 6: Code from Memory (Final Test)

**Set a 10-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: Iterative inorder traversal using a stack
 * Return list of values in inorder
 */
public static List<Integer> inorderIterative(TreeNode root) {
    // Your implementation here







    return null; // Replace
}
```

**After implementing, test with:**
```
Tree:     4
         / \
        2   6
       / \
      1   3
```
- Expected: [1, 2, 3, 4, 6]

**Verification:**

- [ ] Implemented correctly without looking
- [ ] Handles edge cases (null root, single node)
- [ ] Time complexity is O(n)
- [ ] Space complexity is O(h)

---

### Gate 7: Traversal Order Quiz

**Given this tree, predict the output for each traversal:**

```
Tree:        8
           /   \
          3     10
         / \      \
        1   6     14
           / \    /
          4   7  13
```

**Your predictions:**

1. **Inorder (Left, Root, Right):**
    - Your answer: _[Fill in]_
    - Verify: Should be sorted for BST

2. **Preorder (Root, Left, Right):**
    - Your answer: _[Fill in]_
    - Verify: Root comes first

3. **Postorder (Left, Right, Root):**
    - Your answer: _[Fill in]_
    - Verify: Root comes last

4. **Level-order (BFS):**
    - Your answer: _[Fill in by level]_
    - Verify: [[8], [3, 10], [1, 6, 14], [4, 7, 13]]

**Score:** ___/4 correct predictions

If you got any wrong, trace through manually and understand why.

---

### Gate 8: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain to an imaginary person when to use EACH traversal type.

Your explanation:

> **Inorder:** "Use inorder when..."
>
> _[Fill in - give clear use cases and why]_

> **Preorder:** "Use preorder when..."
>
> _[Fill in - give clear use cases and why]_

> **Postorder:** "Use postorder when..."
>
> _[Fill in - give clear use cases and why]_

> **Level-order:** "Use level-order when..."
>
> _[Fill in - give clear use cases and why]_

**Examples where each shines:**

1. **Inorder use case:** _[Real example, e.g., "Finding kth smallest in BST"]_
2. **Preorder use case:** _[Real example, e.g., "Creating a copy of tree"]_
3. **Postorder use case:** _[Real example, e.g., "Calculating directory sizes"]_
4. **Level-order use case:** _[Real example, e.g., "Finding shortest path"]_

---

### Gate 9: Stack vs Queue Understanding

**Critical concept check:**

**Question 1:** Why does level-order traversal use a Queue?

Your answer: _[Fill in - explain FIFO and why it matters]_

**Question 2:** Why do iterative DFS traversals use a Stack?

Your answer: _[Fill in - explain LIFO and why it matters]_

**Question 3:** What happens if you swap them?
- Use Stack for level-order: _[What traversal do you get?]_
- Use Queue for DFS: _[What traversal do you get?]_

**Verification:**

- [ ] Understand FIFO vs LIFO
- [ ] Can explain why Queue gives level-order
- [ ] Can explain why Stack gives depth-first
- [ ] Know what happens when you swap them

---

### Gate 10: Recursive vs Iterative Decision

**Scenario-based questions:**

**Scenario 1:** Binary tree with max depth of 10,000 (very skewed)
- Would you use recursive or iterative? _[Fill in]_
- Why? _[Fill in reasoning]_

**Scenario 2:** Balanced binary tree with 1,000 nodes (depth ~10)
- Would you use recursive or iterative? _[Fill in]_
- Why? _[Fill in reasoning]_

**Scenario 3:** You need O(1) space and can't use system stack
- Which approach? _[Recursive/Iterative/Morris]_
- Why? _[Fill in reasoning]_

**General rule you learned:**

- Use recursive when: _[Fill in]_
- Use iterative when: _[Fill in]_
- Use Morris when: _[Fill in]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement all four traversal patterns (inorder, preorder, postorder, level-order) from memory
- [ ] Implement both recursive and iterative versions
- [ ] Explain when and why to use each traversal order
- [ ] Identify the correct traversal for new problems
- [ ] Analyze time and space complexity for each approach
- [ ] Debug common traversal mistakes (stack/queue confusion, wrong loop conditions)
- [ ] Choose between recursive, iterative, and Morris based on constraints
- [ ] Teach this concept to someone else

**Self-assessment score:** ___/10

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered tree traversals. Proceed to the next topic.
