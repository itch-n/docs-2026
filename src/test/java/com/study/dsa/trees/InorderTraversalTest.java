package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class InorderTraversalTest {

    // Build the tree used in main():
    //       4
    //      / \
    //     2   6
    //    / \ / \
    //   1  3 5  7
    private InorderTraversal.TreeNode buildTestTree() {
        InorderTraversal.TreeNode root = new InorderTraversal.TreeNode(4);
        root.left = new InorderTraversal.TreeNode(2);
        root.right = new InorderTraversal.TreeNode(6);
        root.left.left = new InorderTraversal.TreeNode(1);
        root.left.right = new InorderTraversal.TreeNode(3);
        root.right.left = new InorderTraversal.TreeNode(5);
        root.right.right = new InorderTraversal.TreeNode(7);
        return root;
    }

    @Test
    void testInorderRecursive() {
        InorderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = InorderTraversal.inorderRecursive(root);
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), result);
    }

    @Test
    void testInorderIterative() {
        InorderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = InorderTraversal.inorderIterative(root);
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), result);
    }

    @Test
    void testInorderMorris() {
        InorderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = InorderTraversal.inorderMorris(root);
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), result);
    }

    @Test
    void testInorderSingleNode() {
        InorderTraversal.TreeNode root = new InorderTraversal.TreeNode(42);
        assertEquals(List.of(42), InorderTraversal.inorderRecursive(root));
        assertEquals(List.of(42), InorderTraversal.inorderIterative(root));
        assertEquals(List.of(42), InorderTraversal.inorderMorris(root));
    }
}
