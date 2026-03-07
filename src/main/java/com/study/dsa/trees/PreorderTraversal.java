package com.study.dsa.trees;

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

        // TODO: Handle base case

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

        // TODO: Implement iteration/conditional logic
        // TODO: Create Stack<TreeNode>, push root

        // TODO: Implement iteration/conditional logic

        return result; // Replace with implementation
    }
}
