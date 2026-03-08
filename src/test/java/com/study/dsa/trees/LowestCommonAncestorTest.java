package com.study.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LowestCommonAncestorTest {

    // Build the BST from main():
    //       6
    //      / \
    //     2   8
    //    / \ / \
    //   0  4 7  9
    //     / \
    //    3   5
    private LowestCommonAncestor.TreeNode buildBST() {
        LowestCommonAncestor.TreeNode root = new LowestCommonAncestor.TreeNode(6);
        root.left = new LowestCommonAncestor.TreeNode(2);
        root.right = new LowestCommonAncestor.TreeNode(8);
        root.left.left = new LowestCommonAncestor.TreeNode(0);
        root.left.right = new LowestCommonAncestor.TreeNode(4);
        root.left.right.left = new LowestCommonAncestor.TreeNode(3);
        root.left.right.right = new LowestCommonAncestor.TreeNode(5);
        root.right.left = new LowestCommonAncestor.TreeNode(7);
        root.right.right = new LowestCommonAncestor.TreeNode(9);
        return root;
    }

    @Test
    void testLCAOfTwoAndEight() {
        // LCA(2, 8) = 6 (the root, since 2 is in left subtree, 8 in right)
        LowestCommonAncestor.TreeNode root = buildBST();
        LowestCommonAncestor.TreeNode p = root.left;       // node 2
        LowestCommonAncestor.TreeNode q = root.right;      // node 8
        LowestCommonAncestor.TreeNode lca = LowestCommonAncestor.lowestCommonAncestor(root, p, q);
        assertNotNull(lca);
        assertEquals(6, lca.val);
    }

    @Test
    void testLCAOfTwoAndFour() {
        // LCA(2, 4) = 2 (2 is ancestor of 4)
        LowestCommonAncestor.TreeNode root = buildBST();
        LowestCommonAncestor.TreeNode p = root.left;             // node 2
        LowestCommonAncestor.TreeNode q = root.left.right;       // node 4
        LowestCommonAncestor.TreeNode lca = LowestCommonAncestor.lowestCommonAncestor(root, p, q);
        assertNotNull(lca);
        assertEquals(2, lca.val);
    }

    @Test
    void testLCABSTOfTwoAndFour() {
        // BST-optimized: LCA(2, 4) = 2
        LowestCommonAncestor.TreeNode root = buildBST();
        LowestCommonAncestor.TreeNode p = root.left;             // node 2
        LowestCommonAncestor.TreeNode q = root.left.right;       // node 4
        LowestCommonAncestor.TreeNode lca = LowestCommonAncestor.lowestCommonAncestorBST(root, p, q);
        assertNotNull(lca);
        assertEquals(2, lca.val);
    }

    @Test
    void testLCABSTOfTwoAndEight() {
        // BST-optimized: LCA(2, 8) = 6
        LowestCommonAncestor.TreeNode root = buildBST();
        LowestCommonAncestor.TreeNode p = root.left;       // node 2
        LowestCommonAncestor.TreeNode q = root.right;      // node 8
        LowestCommonAncestor.TreeNode lca = LowestCommonAncestor.lowestCommonAncestorBST(root, p, q);
        assertNotNull(lca);
        assertEquals(6, lca.val);
    }

    @Test
    void testLCABSTOfSevenAndNine() {
        // BST-optimized: LCA(7, 9) = 8
        LowestCommonAncestor.TreeNode root = buildBST();
        LowestCommonAncestor.TreeNode p = root.right.left;   // node 7
        LowestCommonAncestor.TreeNode q = root.right.right;  // node 9
        LowestCommonAncestor.TreeNode lca = LowestCommonAncestor.lowestCommonAncestorBST(root, p, q);
        assertNotNull(lca);
        assertEquals(8, lca.val);
    }
}
