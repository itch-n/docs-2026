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

**Next Topic:** [07. Trees - Recursion →](07-trees-recursion.md)

**Back to:** [05. Stacks & Queues ←](05-stacks--queues.md)
