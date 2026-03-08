package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TreeHeightDepthTest {

    // Build balanced tree:
    //       4
    //      / \
    //     2   6
    //    / \ / \
    //   1  3 5  7
    private TreeHeightDepth.TreeNode buildBalancedTree() {
        TreeHeightDepth.TreeNode root = new TreeHeightDepth.TreeNode(4);
        root.left = new TreeHeightDepth.TreeNode(2);
        root.right = new TreeHeightDepth.TreeNode(6);
        root.left.left = new TreeHeightDepth.TreeNode(1);
        root.left.right = new TreeHeightDepth.TreeNode(3);
        root.right.left = new TreeHeightDepth.TreeNode(5);
        root.right.right = new TreeHeightDepth.TreeNode(7);
        return root;
    }

    // Build unbalanced tree:
    //   1
    //  /
    // 2
    //  \  (actually left-only chain for simplicity as in main())
    // Actually main() uses:
    //   1
    //  /
    // 2
    // /
    //3
    private TreeHeightDepth.TreeNode buildUnbalancedTree() {
        TreeHeightDepth.TreeNode root = new TreeHeightDepth.TreeNode(1);
        root.left = new TreeHeightDepth.TreeNode(2);
        root.left.left = new TreeHeightDepth.TreeNode(3);
        return root;
    }

    @Test
    void testHeightBalanced() {
        // Height of a 3-level tree = 3
        TreeHeightDepth.TreeNode root = buildBalancedTree();
        assertEquals(3, TreeHeightDepth.height(root));
    }

    @Test
    void testHeightUnbalanced() {
        // Chain of 3 = height 3
        TreeHeightDepth.TreeNode root = buildUnbalancedTree();
        assertEquals(3, TreeHeightDepth.height(root));
    }

    @Test
    void testIsBalancedTrue() {
        TreeHeightDepth.TreeNode root = buildBalancedTree();
        assertTrue(TreeHeightDepth.isBalanced(root));
    }

    @Test
    void testIsBalancedFalse() {
        TreeHeightDepth.TreeNode root = buildUnbalancedTree();
        assertFalse(TreeHeightDepth.isBalanced(root));
    }

    @Test
    void testIsBalancedNull() {
        assertTrue(TreeHeightDepth.isBalanced(null));
    }

    @Test
    void testMinDepthBalanced() {
        // All leaves at depth 3; min depth = 3
        TreeHeightDepth.TreeNode root = buildBalancedTree();
        assertEquals(3, TreeHeightDepth.minDepth(root));
    }

    @Test
    void testMinDepthUnbalanced() {
        // Left chain 1->2->3 has no right children, min depth = 3
        // (minDepth counts only root-to-leaf paths)
        TreeHeightDepth.TreeNode root = buildUnbalancedTree();
        assertEquals(3, TreeHeightDepth.minDepth(root));
    }

    @Test
    void testMinDepthOneChildOnly() {
        // Tree:  1
        //       / \
        //      2   3
        // where 3 is a leaf: min depth = 2
        TreeHeightDepth.TreeNode root = new TreeHeightDepth.TreeNode(1);
        root.left = new TreeHeightDepth.TreeNode(2);
        root.right = new TreeHeightDepth.TreeNode(3);
        assertEquals(2, TreeHeightDepth.minDepth(root));
    }
}
