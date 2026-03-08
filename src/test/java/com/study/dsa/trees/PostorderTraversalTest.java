package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class PostorderTraversalTest {

    // Build the tree used in main():
    //       4
    //      / \
    //     2   6
    //    / \ / \
    //   1  3 5  7
    private PostorderTraversal.TreeNode buildTestTree() {
        PostorderTraversal.TreeNode root = new PostorderTraversal.TreeNode(4);
        root.left = new PostorderTraversal.TreeNode(2);
        root.right = new PostorderTraversal.TreeNode(6);
        root.left.left = new PostorderTraversal.TreeNode(1);
        root.left.right = new PostorderTraversal.TreeNode(3);
        root.right.left = new PostorderTraversal.TreeNode(5);
        root.right.right = new PostorderTraversal.TreeNode(7);
        return root;
    }

    @Test
    void testPostorderRecursive() {
        // Postorder: left, right, root -> 1, 3, 2, 5, 7, 6, 4
        PostorderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = PostorderTraversal.postorderRecursive(root);
        assertEquals(List.of(1, 3, 2, 5, 7, 6, 4), result);
    }

    @Test
    void testPostorderIterative() {
        PostorderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = PostorderTraversal.postorderIterative(root);
        assertEquals(List.of(1, 3, 2, 5, 7, 6, 4), result);
    }

    @Test
    void testPostorderIterativeNull() {
        List<Integer> result = PostorderTraversal.postorderIterative(null);
        assertEquals(List.of(), result);
    }

    @Test
    void testPostorderSingleNode() {
        PostorderTraversal.TreeNode root = new PostorderTraversal.TreeNode(7);
        assertEquals(List.of(7), PostorderTraversal.postorderRecursive(root));
        assertEquals(List.of(7), PostorderTraversal.postorderIterative(root));
    }
}
