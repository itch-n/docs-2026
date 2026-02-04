# 06. Trees - Traversals

> Visit every node in a tree using Inorder, Preorder, Postorder, or Level-Order

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What are tree traversals in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

2. **Why do we need different traversal orders?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

3. **Real-world analogy:**
    - Example: "Tree traversals are like different ways to read a family tree..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **When does each traversal order matter?**
    - Your answer: <span class="fill-in">[Fill in after solving problems]</span>

5. **What's the difference between iterative and recursive?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

<div class="learner-section" markdown>

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Visiting every node in a tree:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified after learning: <span class="fill-in">[Actual: O(?)]</span>

2. **Recursive inorder traversal:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Iterative vs Recursive space:**
    - Recursive space usage: <span class="fill-in">[O(?) - what uses the space?]</span>
    - Iterative space usage: <span class="fill-in">[O(?) - what data structure?]</span>
    - Morris traversal space: <span class="fill-in">[O(?)]</span>

### Scenario Predictions

**Scenario 1:** Traverse this BST and predict output for each traversal

```
Tree:     4
         / \
        2   6
       / \
      1   3
```

- **Inorder (Left, Root, Right):** <span class="fill-in">[Predict: ?, ?, ?, ?, ?]</span>
- **Preorder (Root, Left, Right):** <span class="fill-in">[Predict: ?, ?, ?, ?, ?]</span>
- **Postorder (Left, Right, Root):** <span class="fill-in">[Predict: ?, ?, ?, ?, ?]</span>
- **Level-order (BFS):** _[Predict: [[?], [?, ?], [?, ?]]]_

**Verify after implementation:** Were your predictions correct? <span class="fill-in">[Yes/No]</span>

**Scenario 2:** Why does inorder give sorted output for BST?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Scenario 3:** Which traversal to use for deleting a tree?

- Your guess: <span class="fill-in">[Inorder/Preorder/Postorder/Level-order - Why?]</span>
- Reasoning: <span class="fill-in">[Fill in your logic]</span>
- Verified: <span class="fill-in">[After implementation]</span>

### Trade-off Quiz

**Question:** When would iterative traversal be BETTER than recursive?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question:** What's the MAIN advantage of Morris traversal?

- [ ] Faster than recursive
- [ ] O(1) space complexity
- [ ] Easier to implement
- [ ] Works for all tree types

Verify after implementation: <span class="fill-in">[Which one(s)?]</span>

**Question:** Which traversal order matters for expression trees?

- Infix notation uses: <span class="fill-in">[Which traversal?]</span>
- Prefix notation uses: <span class="fill-in">[Which traversal?]</span>
- Postfix notation uses: <span class="fill-in">[Which traversal?]</span>

</div>

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

| Tree Size  | Collect + Sort (O(n log n)) | Inorder (O(n)) | Speedup |
|------------|-----------------------------|----------------|---------|
| n = 100    | ~664 ops                    | 100 ops        | 6.6x    |
| n = 1,000  | ~9,966 ops                  | 1,000 ops      | 10x     |
| n = 10,000 | ~132,877 ops                | 10,000 ops     | 13x     |

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

<div class="learner-section" markdown>

- <span class="fill-in">[Why does postorder make sense for tree deletion?]</span>
- <span class="fill-in">[Why does preorder make sense for copying a tree?]</span>
- <span class="fill-in">[When would level-order be preferred over depth-first?]</span>

</div>

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
        inorderRecursive_Buggy(root.left);        result.add(root.val);
        inorderRecursive_Buggy(root.right);
    }

    return result;}
```

**Your debugging:**

- Bug 1: <span class="fill-in">[What\'s the bug?]</span>

- Bug 2: <span class="fill-in">[What\'s the bug?]</span>

**Test case:**

- Input: Tree with values 1, 2, 3
- Expected: [1, 2, 3]
- Actual with buggy code: <span class="fill-in">[What do you get?]</span>

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

    while (!stack.isEmpty()) {        while (curr != null) {
            stack.push(curr);
            curr = curr.left;
        }

        TreeNode node = stack.pop();
        result.add(node.val);
        curr = curr.left;    }

    return result;
}
```

**Your debugging:**

- **Bug 1:** <span class="fill-in">[What's wrong with the while condition?]</span>
- **Bug 1 effect:** <span class="fill-in">[What happens? When does loop start/stop?]</span>
- **Bug 1 fix:** <span class="fill-in">[Correct condition]</span>

- **Bug 2:** <span class="fill-in">[Which direction should curr move?]</span>
- **Bug 2 effect:** <span class="fill-in">[What happens? Infinite loop? Wrong order?]</span>
- **Bug 2 fix:** <span class="fill-in">[Fill in]</span>

**Trace through example:**

- Input: Tree with values 1, 2, 3
- Expected: [1, 2, 3]
- With Bug 1: <span class="fill-in">[What happens?]</span>
- With Bug 2: <span class="fill-in">[What happens?]</span>

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** Loop condition should be `while (curr != null || !stack.isEmpty())`. Current code won't even start if root is
not null.

**Bug 2:** Should be `curr = node.right`, not `curr = curr.left`. After visiting a node, we need to go to its right
subtree.

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

- **Bug location:** <span class="fill-in">[Which lines push to stack1?]</span>
- **Bug explanation:** <span class="fill-in">[Why does order matter here?]</span>
- **Expected order:** <span class="fill-in">[Postorder is Left, Right, Root - so what should we push first?]</span>

**Think through it:**

- Postorder visits: Left, Right, Root
- Stack2 reverses the order
- So stack1 should create what order? <span class="fill-in">[Fill in your reasoning]</span>

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

- Bug: <span class="fill-in">[What\'s the bug?]</span>

**Test case:**

```
Tree:     4
         / \
        2   6
       /
      1
```

- Expected: [[4], [2, 6], [1]]
- Actual with buggy code: <span class="fill-in">[Trace through - what do you get?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** The inner while loop processes until the queue is empty, which means it processes ALL levels at once, not one
level at a time.

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

**Why:** By capturing `queue.size()` before the loop, we know exactly how many nodes are in the current level. New nodes
added during the loop belong to the NEXT level.
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

    Stack<TreeNode> stack = new Stack<>();    stack.push(root);

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

- **What traversal does this actually perform?** <span class="fill-in">[Hint: Stack = DFS, Queue = BFS]</span>
- **What would the output be for a simple tree?** <span class="fill-in">[Trace through]</span>
- **If we want level-order, what should we use?** <span class="fill-in">[Stack/Queue/Other?]</span>

**Key insight to understand:**

- Stack (LIFO) gives you: <span class="fill-in">[BFS/DFS - which one?]</span>
- Queue (FIFO) gives you: <span class="fill-in">[BFS/DFS - which one?]</span>

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
            while (pred.right != null) {                pred = pred.right;
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

- **Bug:** <span class="fill-in">[What causes infinite loop?]</span>
- **When does it happen?** <span class="fill-in">[When we revisit a threaded node]</span>
- **Fix:** <span class="fill-in">[What condition should we check in the while loop?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** The while loop `while (pred.right != null)` will loop forever once we create a thread (pred.right = curr),
because we never check if pred.right == curr.

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

1. <span class="fill-in">[List the patterns you noticed]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

**Key insights:**

- Recursive traversals need: <span class="fill-in">[What pattern for combining results?]</span>
- Iterative inorder needs: <span class="fill-in">[What loop condition?]</span>
- Level-order needs: <span class="fill-in">[Stack or Queue?]</span>
- Morris traversal needs: <span class="fill-in">[What check to avoid infinite loops?]</span>

---

## Decision Framework

**Your task:** Build decision trees for tree traversal selection.

### Question 1: Which traversal order do you need?

Answer after solving problems:

- **Need sorted order from BST?** <span class="fill-in">[Use inorder]</span>
- **Need to copy tree structure?** <span class="fill-in">[Use preorder]</span>
- **Need to delete tree safely?** <span class="fill-in">[Use postorder]</span>
- **Need level-by-level processing?** <span class="fill-in">[Use level-order]</span>

### Question 2: Recursive vs Iterative?

**Recursive approach:**

- Pros: <span class="fill-in">[Simpler code, cleaner logic]</span>
- Cons: <span class="fill-in">[O(h) stack space, risk of stack overflow]</span>
- Use when: <span class="fill-in">[Tree depth is reasonable]</span>

**Iterative approach:**

- Pros: <span class="fill-in">[Explicit control, no stack overflow]</span>
- Cons: <span class="fill-in">[More complex code, need explicit stack/queue]</span>
- Use when: <span class="fill-in">[Deep trees, production code]</span>

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


---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**

- [ ] [94. Binary Tree Inorder Traversal](https://leetcode.com/problems/binary-tree-inorder-traversal/)
    - Pattern: <span class="fill-in">[Inorder - recursive/iterative/Morris]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [144. Binary Tree Preorder Traversal](https://leetcode.com/problems/binary-tree-preorder-traversal/)
    - Pattern: <span class="fill-in">[Preorder]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [145. Binary Tree Postorder Traversal](https://leetcode.com/problems/binary-tree-postorder-traversal/)
    - Pattern: <span class="fill-in">[Postorder]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [102. Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/)
    - Pattern: <span class="fill-in">[Level-order BFS]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Medium (Complete 3-4):**

- [ ] [103. Binary Tree Zigzag Level Order Traversal](https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/)
    - Pattern: <span class="fill-in">[Level-order with direction alternation]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [199. Binary Tree Right Side View](https://leetcode.com/problems/binary-tree-right-side-view/)
    - Pattern: <span class="fill-in">[Level-order, track rightmost]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [107. Binary Tree Level Order Traversal II](https://leetcode.com/problems/binary-tree-level-order-traversal-ii/)
    - Pattern: <span class="fill-in">[Level-order bottom-up]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [230. Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)
    - Pattern: <span class="fill-in">[Inorder with counter]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Hard (Optional):**

- [ ] [297. Serialize and Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)
    - Pattern: <span class="fill-in">[Preorder or level-order]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [987. Vertical Order Traversal of a Binary Tree](https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/)
    - Pattern: <span class="fill-in">[Custom traversal with coordinates]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

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
