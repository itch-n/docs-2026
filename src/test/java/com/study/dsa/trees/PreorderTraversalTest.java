package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class PreorderTraversalTest {

    // Build the tree used in main():
    //       4
    //      / \
    //     2   6
    //    / \ / \
    //   1  3 5  7
    private PreorderTraversal.TreeNode buildTestTree() {
        PreorderTraversal.TreeNode root = new PreorderTraversal.TreeNode(4);
        root.left = new PreorderTraversal.TreeNode(2);
        root.right = new PreorderTraversal.TreeNode(6);
        root.left.left = new PreorderTraversal.TreeNode(1);
        root.left.right = new PreorderTraversal.TreeNode(3);
        root.right.left = new PreorderTraversal.TreeNode(5);
        root.right.right = new PreorderTraversal.TreeNode(7);
        return root;
    }

    @Test
    void testPreorderRecursive() {
        // Preorder: root, left, right -> 4, 2, 1, 3, 6, 5, 7
        PreorderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = PreorderTraversal.preorderRecursive(root);
        assertEquals(List.of(4, 2, 1, 3, 6, 5, 7), result);
    }

    @Test
    void testPreorderIterative() {
        PreorderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = PreorderTraversal.preorderIterative(root);
        assertEquals(List.of(4, 2, 1, 3, 6, 5, 7), result);
    }

    @Test
    void testPreorderIterativeNull() {
        List<Integer> result = PreorderTraversal.preorderIterative(null);
        assertEquals(List.of(), result);
    }

    @Test
    void testPreorderSingleNode() {
        PreorderTraversal.TreeNode root = new PreorderTraversal.TreeNode(99);
        assertEquals(List.of(99), PreorderTraversal.preorderRecursive(root));
        assertEquals(List.of(99), PreorderTraversal.preorderIterative(root));
    }
}
