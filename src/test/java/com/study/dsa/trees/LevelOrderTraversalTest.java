package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class LevelOrderTraversalTest {

    // Build the tree used in main():
    //       4
    //      / \
    //     2   6
    //    / \ / \
    //   1  3 5  7
    private LevelOrderTraversal.TreeNode buildTestTree() {
        LevelOrderTraversal.TreeNode root = new LevelOrderTraversal.TreeNode(4);
        root.left = new LevelOrderTraversal.TreeNode(2);
        root.right = new LevelOrderTraversal.TreeNode(6);
        root.left.left = new LevelOrderTraversal.TreeNode(1);
        root.left.right = new LevelOrderTraversal.TreeNode(3);
        root.right.left = new LevelOrderTraversal.TreeNode(5);
        root.right.right = new LevelOrderTraversal.TreeNode(7);
        return root;
    }

    @Test
    void testLevelOrder() {
        // Should be: [[4], [2, 6], [1, 3, 5, 7]]
        LevelOrderTraversal.TreeNode root = buildTestTree();
        List<List<Integer>> result = LevelOrderTraversal.levelOrder(root);
        assertEquals(List.of(List.of(4), List.of(2, 6), List.of(1, 3, 5, 7)), result);
    }

    @Test
    void testZigzagLevelOrder() {
        // Level 0 (L->R): [4]
        // Level 1 (R->L): [6, 2]
        // Level 2 (L->R): [1, 3, 5, 7]
        LevelOrderTraversal.TreeNode root = buildTestTree();
        List<List<Integer>> result = LevelOrderTraversal.zigzagLevelOrder(root);
        assertEquals(List.of(List.of(4), List.of(6, 2), List.of(1, 3, 5, 7)), result);
    }

    @Test
    void testRightSideView() {
        // Right-most node at each level: 4, 6, 7
        LevelOrderTraversal.TreeNode root = buildTestTree();
        List<Integer> result = LevelOrderTraversal.rightSideView(root);
        assertEquals(List.of(4, 6, 7), result);
    }

    @Test
    void testRightSideViewLeftSkewed() {
        // Tree:  1
        //       /
        //      2
        //     /
        //    3
        // Right side view: [1, 2, 3]
        LevelOrderTraversal.TreeNode root = new LevelOrderTraversal.TreeNode(1);
        root.left = new LevelOrderTraversal.TreeNode(2);
        root.left.left = new LevelOrderTraversal.TreeNode(3);
        List<Integer> result = LevelOrderTraversal.rightSideView(root);
        assertEquals(List.of(1, 2, 3), result);
    }
}
