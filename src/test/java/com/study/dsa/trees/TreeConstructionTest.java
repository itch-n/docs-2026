package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class TreeConstructionTest {

    // Collect inorder values of built tree for easy assertion
    private List<Integer> collectInorder(TreeConstruction.TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(TreeConstruction.TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.val);
        inorder(node.right, result);
    }

    @Test
    void testBuildTreePreIn() {
        // preorder: [3, 9, 20, 15, 7]
        // inorder:  [9, 3, 15, 20, 7]
        // The built tree should have inorder [9, 3, 15, 20, 7]
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};
        TreeConstruction.TreeNode root = TreeConstruction.buildTreePreIn(preorder, inorder);
        assertNotNull(root);
        assertEquals(3, root.val);  // root is first element of preorder
        assertEquals(List.of(9, 3, 15, 20, 7), collectInorder(root));
    }

    @Test
    void testBuildTreePreInRootValue() {
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};
        TreeConstruction.TreeNode root = TreeConstruction.buildTreePreIn(preorder, inorder);
        assertNotNull(root);
        // root.left should be 9 (only element left of 3 in inorder)
        assertNotNull(root.left);
        assertEquals(9, root.left.val);
        // root.right should be 20
        assertNotNull(root.right);
        assertEquals(20, root.right.val);
    }

    @Test
    void testBuildTreePostIn() {
        // postorder: [9, 15, 7, 20, 3]
        // inorder:   [9, 3, 15, 20, 7]
        // The built tree should have inorder [9, 3, 15, 20, 7]
        int[] postorder = {9, 15, 7, 20, 3};
        int[] inorder = {9, 3, 15, 20, 7};
        TreeConstruction.TreeNode root = TreeConstruction.buildTreePostIn(postorder, inorder);
        assertNotNull(root);
        assertEquals(3, root.val);  // root is last element of postorder
        assertEquals(List.of(9, 3, 15, 20, 7), collectInorder(root));
    }

    @Test
    void testBuildTreePreInSingleNode() {
        int[] preorder = {1};
        int[] inorder = {1};
        TreeConstruction.TreeNode root = TreeConstruction.buildTreePreIn(preorder, inorder);
        assertNotNull(root);
        assertEquals(1, root.val);
        assertNull(root.left);
        assertNull(root.right);
    }

    @Test
    void testBuildTreePostInSingleNode() {
        int[] postorder = {5};
        int[] inorder = {5};
        TreeConstruction.TreeNode root = TreeConstruction.buildTreePostIn(postorder, inorder);
        assertNotNull(root);
        assertEquals(5, root.val);
        assertNull(root.left);
        assertNull(root.right);
    }
}
