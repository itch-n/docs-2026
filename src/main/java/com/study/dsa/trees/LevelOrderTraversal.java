package com.study.dsa.trees;

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

        // TODO: Implement iteration/conditional logic
        // TODO: Create Queue<TreeNode>, add root

        // TODO: Implement iteration/conditional logic

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
