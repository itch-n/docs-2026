# 07. Trees - Recursion

> Solve tree problems using recursive divide-and-conquer approach

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is tree recursion in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why is recursion natural for trees?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "Tree recursion is like solving a puzzle by breaking it into smaller identical puzzles..."
   - Your analogy: _[Fill in]_

4. **When does recursion work well for trees?**
   - Your answer: _[Fill in after solving problems]_

5. **What's the base case pattern for tree recursion?**
   - Your answer: _[Fill in after implementation]_

---

## Core Implementation

### Pattern 1: Height and Depth

**Concept:** Calculate tree metrics recursively.

**Use case:** Tree height, depth, balanced tree check.

```java
public class TreeHeightDepth {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Calculate height of tree
     * Time: O(n), Space: O(h) for recursion stack
     *
     * TODO: Implement recursive height calculation
     */
    public static int height(TreeNode root) {
        // TODO: Base case: if root is null, return 0

        // TODO: Recursively get left height
        // TODO: Recursively get right height
        // TODO: Return 1 + max(leftHeight, rightHeight)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Check if tree is balanced
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement balanced tree check
     */
    public static boolean isBalanced(TreeNode root) {
        // TODO: Tree is balanced if:
        //   1. Left subtree is balanced
        //   2. Right subtree is balanced
        //   3. Height difference <= 1

        // TODO: Return checkBalance(root) \!= -1
        return false; // Replace with implementation
    }

    private static int checkBalance(TreeNode root) {
        // TODO: Base case: null returns 0

        // TODO: Check left subtree balance
        // TODO: If left returns -1, return -1 (unbalanced)

        // TODO: Check right subtree balance
        // TODO: If right returns -1, return -1 (unbalanced)

        // TODO: If abs(left - right) > 1, return -1
        // TODO: Otherwise return 1 + max(left, right)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Calculate minimum depth (shortest path to leaf)
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement minimum depth
     */
    public static int minDepth(TreeNode root) {
        // TODO: Base case: if root is null, return 0

        // TODO: If no left child, return 1 + minDepth(right)
        // TODO: If no right child, return 1 + minDepth(left)

        // TODO: Both children exist: return 1 + min(left, right)

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class TreeHeightDepthClient {

    public static void main(String[] args) {
        System.out.println("=== Tree Height and Depth ===\n");

        // Create balanced tree:
        //       4
        //      / \
        //     2   6
        //    / \ / \
        //   1  3 5  7
        TreeNode balanced = new TreeNode(4);
        balanced.left = new TreeNode(2);
        balanced.right = new TreeNode(6);
        balanced.left.left = new TreeNode(1);
        balanced.left.right = new TreeNode(3);
        balanced.right.left = new TreeNode(5);
        balanced.right.right = new TreeNode(7);

        System.out.println("--- Test 1: Balanced Tree ---");
        System.out.println("Height: " + TreeHeightDepth.height(balanced));
        System.out.println("Is balanced: " + TreeHeightDepth.isBalanced(balanced));
        System.out.println("Min depth: " + TreeHeightDepth.minDepth(balanced));

        // Create unbalanced tree:
        //       1
        //      /
        //     2
        //    /
        //   3
        TreeNode unbalanced = new TreeNode(1);
        unbalanced.left = new TreeNode(2);
        unbalanced.left.left = new TreeNode(3);

        System.out.println("\n--- Test 2: Unbalanced Tree ---");
        System.out.println("Height: " + TreeHeightDepth.height(unbalanced));
        System.out.println("Is balanced: " + TreeHeightDepth.isBalanced(unbalanced));
        System.out.println("Min depth: " + TreeHeightDepth.minDepth(unbalanced));
    }
}
```

---

### Pattern 2: Diameter and Paths

**Concept:** Find longest paths in tree.

**Use case:** Tree diameter, max path sum, all paths.

```java
import java.util.*;

public class TreeDiameterPaths {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Calculate diameter (longest path between any two nodes)
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement diameter calculation
     */
    public static int diameter(TreeNode root) {
        int[] maxDiameter = new int[1];

        // TODO: Helper function to calculate height and update diameter
        // TODO: For each node, diameter through it = leftHeight + rightHeight
        // TODO: Track maximum diameter seen

        calculateHeight(root, maxDiameter);
        return maxDiameter[0];
    }

    private static int calculateHeight(TreeNode root, int[] maxDiameter) {
        // TODO: Base case: null returns 0

        // TODO: Get left and right heights
        // TODO: Update maxDiameter: max(current, left + right)
        // TODO: Return 1 + max(left, right)

        return 0; // Replace with implementation
    }

    /**
     * Problem: Check if path exists with given sum
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement path sum check
     */
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        // TODO: Base case: if root is null, return false

        // TODO: If leaf node, check if val == targetSum

        // TODO: Recursively check left and right subtrees
        // TODO: with targetSum - root.val

        return false; // Replace with implementation
    }

    /**
     * Problem: Find all root-to-leaf paths with given sum
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement path sum II with backtracking
     */
    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();

        // TODO: Use backtracking to explore all paths
        // TODO: Add node to path, recurse, remove node (backtrack)

        return result; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class TreeDiameterPathsClient {

    public static void main(String[] args) {
        System.out.println("=== Tree Diameter and Paths ===\n");

        // Create tree:
        //       5
        //      / \
        //     4   8
        //    /   / \
        //   11  13  4
        //  / \      / \
        // 7   2    5   1
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(11);
        root.left.left.left = new TreeNode(7);
        root.left.left.right = new TreeNode(2);
        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(4);
        root.right.right.left = new TreeNode(5);
        root.right.right.right = new TreeNode(1);

        System.out.println("--- Test 1: Diameter ---");
        System.out.println("Diameter: " + TreeDiameterPaths.diameter(root));

        System.out.println("\n--- Test 2: Has Path Sum (22) ---");
        System.out.println("Has path: " + TreeDiameterPaths.hasPathSum(root, 22));

        System.out.println("\n--- Test 3: All Paths with Sum 22 ---");
        List<List<Integer>> paths = TreeDiameterPaths.pathSum(root, 22);
        System.out.println("Paths: " + paths);
    }
}
```

---

### Pattern 3: Lowest Common Ancestor (LCA)

**Concept:** Find common ancestor of two nodes.

**Use case:** LCA in binary tree, LCA in BST, distance between nodes.

```java
public class LowestCommonAncestor {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Find LCA in binary tree
     * Time: O(n), Space: O(h)
     *
     * TODO: Implement LCA for binary tree
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // TODO: Base case: if root is null or matches p or q, return root

        // TODO: Recursively search in left and right subtrees

        // TODO: If both left and right found nodes, root is LCA
        // TODO: If only left found, return left
        // TODO: If only right found, return right

        return null; // Replace with implementation
    }

    /**
     * Problem: Find LCA in BST (optimized)
     * Time: O(h), Space: O(h)
     *
     * TODO: Implement LCA for BST
     */
    public static TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        // TODO: If both p and q are less than root, search left
        // TODO: If both p and q are greater than root, search right
        // TODO: Otherwise, root is LCA

        return null; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class LowestCommonAncestorClient {

    public static void main(String[] args) {
        System.out.println("=== Lowest Common Ancestor ===\n");

        // Create BST:
        //       6
        //      / \
        //     2   8
        //    / \ / \
        //   0  4 7  9
        //     / \
        //    3   5
        TreeNode root = new TreeNode(6);
        root.left = new TreeNode(2);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(4);
        root.left.right.left = new TreeNode(3);
        root.left.right.right = new TreeNode(5);
        root.right.left = new TreeNode(7);
        root.right.right = new TreeNode(9);

        TreeNode p = root.left; // Node 2
        TreeNode q = root.right; // Node 8

        System.out.println("--- Test 1: LCA of 2 and 8 ---");
        TreeNode lca = LowestCommonAncestor.lowestCommonAncestor(root, p, q);
        System.out.println("LCA: " + (lca \!= null ? lca.val : "null"));

        p = root.left; // Node 2
        q = root.left.right; // Node 4

        System.out.println("\n--- Test 2: LCA of 2 and 4 ---");
        lca = LowestCommonAncestor.lowestCommonAncestor(root, p, q);
        System.out.println("LCA: " + (lca \!= null ? lca.val : "null"));

        System.out.println("\n--- Test 3: LCA in BST (optimized) ---");
        lca = LowestCommonAncestor.lowestCommonAncestorBST(root, p, q);
        System.out.println("LCA: " + (lca \!= null ? lca.val : "null"));
    }
}
```

---

### Pattern 4: Tree Construction

**Concept:** Build tree from traversal arrays.

**Use case:** Construct from inorder/preorder, inorder/postorder.

```java
import java.util.*;

public class TreeConstruction {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Problem: Build tree from preorder and inorder traversals
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement tree construction
     */
    public static TreeNode buildTreePreIn(int[] preorder, int[] inorder) {
        // TODO: Create map of inorder indices for O(1) lookup
        // TODO: Use helper with preorder index pointer
        // TODO: For each preorder element:
        //   Find position in inorder
        //   Elements left of position = left subtree
        //   Elements right of position = right subtree

        return null; // Replace with implementation
    }

    /**
     * Problem: Build tree from postorder and inorder traversals
     * Time: O(n), Space: O(n)
     *
     * TODO: Implement tree construction
     */
    public static TreeNode buildTreePostIn(int[] postorder, int[] inorder) {
        // TODO: Similar to preorder approach
        // TODO: But process postorder from right to left
        // TODO: Build right subtree before left

        return null; // Replace with implementation
    }

    // Helper: Print tree inorder
    static void printInorder(TreeNode root) {
        if (root == null) return;
        printInorder(root.left);
        System.out.print(root.val + " ");
        printInorder(root.right);
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class TreeConstructionClient {

    public static void main(String[] args) {
        System.out.println("=== Tree Construction ===\n");

        // Test 1: Build from preorder and inorder
        System.out.println("--- Test 1: Build from Preorder and Inorder ---");
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};

        System.out.println("Preorder: " + Arrays.toString(preorder));
        System.out.println("Inorder:  " + Arrays.toString(inorder));

        TreeNode root1 = TreeConstruction.buildTreePreIn(preorder, inorder);
        System.out.print("Built tree (inorder): ");
        TreeConstruction.printInorder(root1);
        System.out.println();

        // Test 2: Build from postorder and inorder
        System.out.println("\n--- Test 2: Build from Postorder and Inorder ---");
        int[] postorder = {9, 15, 7, 20, 3};
        int[] inorder2 = {9, 3, 15, 20, 7};

        System.out.println("Postorder: " + Arrays.toString(postorder));
        System.out.println("Inorder:   " + Arrays.toString(inorder2));

        TreeNode root2 = TreeConstruction.buildTreePostIn(postorder, inorder2);
        System.out.print("Built tree (inorder): ");
        TreeConstruction.printInorder(root2);
        System.out.println();
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for tree recursion.

### Question 1: What information flows up the tree?

Answer after solving problems:
- **Single value (height, count)?** _[Simple recursion]_
- **Multiple values (balanced + height)?** _[Use array or class]_
- **Path information?** _[Use backtracking]_
- **Global state?** _[Use instance variable]_

### Question 2: When to use helper functions?

**Direct recursion:**
- Use when: _[Single return value, no extra state]_
- Example: _[Height calculation]_

**Helper with extra parameters:**
- Use when: _[Need to track state, indices, ranges]_
- Example: _[Tree construction from arrays]_

**Helper with global variables:**
- Use when: _[Need to update global max/min]_
- Example: _[Diameter, max path sum]_

### Your Decision Tree

```
Tree Recursion Pattern Selection
│
├─ What are you calculating?
│   │
│   ├─ Single metric (height, count)?
│   │   └─ Use: Direct recursion ✓
│   │
│   ├─ Path-based (sum, all paths)?
│   │   └─ Use: Backtracking recursion ✓
│   │
│   ├─ Find node(s) (LCA, search)?
│   │   └─ Use: Search recursion ✓
│   │
│   └─ Build structure?
│       └─ Use: Construction recursion ✓
│
└─ How to track state?
    ├─ No extra state → Direct recursion
    ├─ Track max/min → Helper with array/class
    └─ Track path → Backtracking with list
```

### The "Kill Switch" - When NOT to use Recursion

**Don't use recursion when:**
1. _[Tree is very deep? Risk stack overflow]_
2. _[Need iterative control? Use explicit stack]_
3. _[Performance critical? Recursion has overhead]_
4. _[Tail recursion not optimized? Use iteration]_

### The Rule of Three: Alternatives

**Option 1: Pure Recursion**
- Pros: _[Clean code, natural for trees]_
- Cons: _[Stack space, potential overflow]_
- Use when: _[Reasonable depth, clarity matters]_

**Option 2: Recursion with Memoization**
- Pros: _[Avoid recomputation, faster]_
- Cons: _[Extra space, added complexity]_
- Use when: _[Overlapping subproblems]_

**Option 3: Iterative with Stack**
- Pros: _[No stack overflow, explicit control]_
- Cons: _[More complex code]_
- Use when: _[Deep trees, production code]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 4):**
- [ ] [104. Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/)
  - Pattern: _[Height calculation]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [110. Balanced Binary Tree](https://leetcode.com/problems/balanced-binary-tree/)
  - Pattern: _[Balance check]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [111. Minimum Depth of Binary Tree](https://leetcode.com/problems/minimum-depth-of-binary-tree/)
  - Pattern: _[Min depth]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

- [ ] [112. Path Sum](https://leetcode.com/problems/path-sum/)
  - Pattern: _[Path recursion]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 3-4):**
- [ ] [543. Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/)
  - Pattern: _[Diameter calculation]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [236. Lowest Common Ancestor of a Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)
  - Pattern: _[LCA]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [105. Construct Binary Tree from Preorder and Inorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)
  - Pattern: _[Tree construction]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [113. Path Sum II](https://leetcode.com/problems/path-sum-ii/)
  - Pattern: _[Backtracking paths]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

**Hard (Optional):**
- [ ] [124. Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/)
  - Pattern: _[Global max tracking]_
  - Key insight: _[Fill in after solving]_

- [ ] [297. Serialize and Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)
  - Pattern: _[Construction/serialization]_
  - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Height and depth: all metrics work
  - [ ] Diameter and paths: calculation and search work
  - [ ] LCA: binary tree and BST both work
  - [ ] Construction: preorder/inorder and postorder/inorder work
  - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
  - [ ] Can identify when to use recursion
  - [ ] Understand base case patterns
  - [ ] Know when to use helper functions
  - [ ] Recognize backtracking patterns

- [ ] **Problem Solving**
  - [ ] Solved 4 easy problems
  - [ ] Solved 3-4 medium problems
  - [ ] Analyzed time/space complexity
  - [ ] Handled edge cases (null, single node, leaf)

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Built decision tree
  - [ ] Identified when NOT to use recursion
  - [ ] Can explain recursion flow for each pattern

- [ ] **Mastery Check**
  - [ ] Could implement all patterns from memory
  - [ ] Could recognize pattern in new problem
  - [ ] Could explain to someone else
  - [ ] Understand recursion stack and space complexity

---

**Next Topic:** [08. Binary Search →](08-binary-search.md)

**Back to:** [06. Trees - Traversals ←](06-trees-traversals.md)
