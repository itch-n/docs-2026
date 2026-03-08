package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TreeDiameterPathsTest {

    // Build the tree from main():
    //         5
    //        / \
    //       4   8
    //      /   / \
    //    11  13   4
    //   / \      / \
    //  7   2    5   1
    private TreeDiameterPaths.TreeNode buildTestTree() {
        TreeDiameterPaths.TreeNode root = new TreeDiameterPaths.TreeNode(5);
        root.left = new TreeDiameterPaths.TreeNode(4);
        root.right = new TreeDiameterPaths.TreeNode(8);
        root.left.left = new TreeDiameterPaths.TreeNode(11);
        root.left.left.left = new TreeDiameterPaths.TreeNode(7);
        root.left.left.right = new TreeDiameterPaths.TreeNode(2);
        root.right.left = new TreeDiameterPaths.TreeNode(13);
        root.right.right = new TreeDiameterPaths.TreeNode(4);
        root.right.right.left = new TreeDiameterPaths.TreeNode(5);
        root.right.right.right = new TreeDiameterPaths.TreeNode(1);
        return root;
    }

    @Test
    void testDiameter() {
        // Longest path: 7->11->4->5->8->4->5 or 7->11->4->5->8->4->1
        // Heights: left subtree from root: 4->11->7 = depth 3
        //          right subtree from root: 8->4->5 = depth 3
        // Diameter passing through root = 3 + 3 = 6
        TreeDiameterPaths.TreeNode root = buildTestTree();
        assertEquals(6, TreeDiameterPaths.diameter(root));
    }

    @Test
    void testHasPathSumTrue() {
        // Path 5->4->11->2 = 22
        TreeDiameterPaths.TreeNode root = buildTestTree();
        assertTrue(TreeDiameterPaths.hasPathSum(root, 22));
    }

    @Test
    void testHasPathSumFalse() {
        // No root-to-leaf path sums to 1
        TreeDiameterPaths.TreeNode root = buildTestTree();
        assertFalse(TreeDiameterPaths.hasPathSum(root, 1));
    }

    @Test
    void testPathSum() {
        // Paths that sum to 22:
        //   5->4->11->2 = 22
        //   5->8->4->5  = 22
        TreeDiameterPaths.TreeNode root = buildTestTree();
        List<List<Integer>> paths = TreeDiameterPaths.pathSum(root, 22);
        assertEquals(2, paths.size());
        assertTrue(paths.contains(List.of(5, 4, 11, 2)));
        assertTrue(paths.contains(List.of(5, 8, 4, 5)));
    }

    @Test
    void testPathSumNoMatch() {
        TreeDiameterPaths.TreeNode root = buildTestTree();
        List<List<Integer>> paths = TreeDiameterPaths.pathSum(root, 999);
        assertEquals(0, paths.size());
    }

    @Test
    void testDiameterSimple() {
        // Tree: 1 -> 2 -> 3 (left chain), diameter = 2
        TreeDiameterPaths.TreeNode root = new TreeDiameterPaths.TreeNode(1);
        root.left = new TreeDiameterPaths.TreeNode(2);
        root.left.left = new TreeDiameterPaths.TreeNode(3);
        assertEquals(2, TreeDiameterPaths.diameter(root));
    }
}
