package com.study.systems.storage;

import java.util.*;

/**
 * B+Tree: Self-balancing tree optimized for range queries
 *
 * Properties:
 * - All values stored in leaf nodes
 * - Leaves are linked (for range scans)
 * - Height kept minimal
 * - Order K: max K keys per node
 */
public class BPlusTree<K extends Comparable<K>, V> {

    private final int order; // Max keys per node (e.g., 4)
    private Node root;

    // Base node class
    abstract class Node {
        List<K> keys = new ArrayList<>(order);
        Node parent;
    }

    /**
     * Represents an internal node (hallway) that directs keys to the correct leaf (room).
     * <h3>The Club Analogy</h3>
     * Think of this node as a hallway with "Bouncers" (Keys) and "Rooms" (Children).
     * <ul>
     * <li><b>Bouncers (keys):</b> Set the age limit for the room to their right.</li>
     * <li><b>Rooms (children):</b> There is always one more room than there are bouncers.</li>
     * </ul>
     * <pre>
     *         (18)                 (21)             (50)
     *     [ Bouncer 0 ]       [ Bouncer 1 ]     [ Bouncer 2 ]
     *    /             \     /           \     /         \
     * [ Room 0 ]       [ Room 1 ]       [ Room 2 ]       [ Room 3 ]
     * (Kids)           (18+ Only)       (21+ Only)       (50+ Only)
     * </pre>
     * <h3>Navigation via Binary Search</h3>
     * To find the correct room index, we look for the <b>first bouncer strictly greater</b>
     * than our search key.
     * <p>Using {@link java.util.Collections#binarySearch(List, Object)}:</p>
     * <ul>
     * <li>If an exact match is found (index {@code i}): You are equal to the bouncer.
     * Per the "inclusive-right" rule, you move to the room to their right: {@code i + 1}.</li>
     * <li>If no match is found: The method returns {@code -(insertionPoint + 1)}.
     * The {@code insertionPoint} is the index of the first bouncer strictly greater
     * than you. You belong in the room at that same index.</li>
     * </ul>
     */
    class InternalNode extends Node {
        List<Node> children = new ArrayList<>(order);
    }

    // Leaf nodes: have values, no children, linked to prev and next leaf
    class LeafNode extends Node {
        List<V> values = new ArrayList<>(order);
        LeafNode next; // For range scans
    }

    public BPlusTree(int order) {
        this.order = order;
        this.root = new LeafNode();
    }

    /**
     * Insert key-value pair
     * Time: O(log N)
     *
     * <pre>
     * 1. Find correct leaf node
     * 2. Insert in sorted position
     * 3. If leaf overflows, split it
     * 4. Propagate split up the tree
     * </pre>
     */
    public void insert(K key, V value) {
        // Start by finding the leaf
        LeafNode leaf = findLeaf(key);

        int result = Collections.binarySearch(leaf.keys, key);
        if (result >= 0) { // key exists, overwrite data
            int existingKeyIndex = result;
            leaf.values.set(existingKeyIndex, value);
            return;
        }

        int insertionIndex = -result - 1;
        leaf.keys.add(insertionIndex, key);
        leaf.values.add(insertionIndex, value);

        // If full, split the leaf, then potentially split up index nodes if they're too full
        if (leaf.keys.size() > order) {
            splitLeaf(leaf);
            if (leaf == root) {
                root = leaf.parent;
            }

            // leaf split may have split the parent, so recursively split up
            InternalNode parent = (InternalNode) leaf.parent;
            while (parent != null && parent.keys.size() > order) {
                splitInternal(parent);

                // If you just split the root and created a new root, update this class state
                if (parent == root) {
                    root = parent.parent;
                }
                parent = (InternalNode) parent.parent;
            }
        }
    }

    /**
     * Search for a key
     * Time: O(log N)
     * <p>
     * 1. Start at root
     * 2. At each internal node, find correct child
     * 3. At leaf, search for key
     */
    public V search(K key) {
        LeafNode leaf = findLeaf(key);
        int result = Collections.binarySearch(leaf.keys, key);
        if (result < 0) {
            return null;
        }
        return leaf.values.get(result);
    }

    /**
     * Range query: all values where startKey <= key <= endKey
     * Time: O(log N + results)
     * <p>
     * 1. Find leaf containing startKey
     * 2. Follow leaf.next pointers
     * 3. Collect values until endKey
     */
    public List<V> rangeQuery(K startKey, K endKey) {
        List<V> results = new ArrayList<>();
        LeafNode leaf = findLeaf(startKey);

        while (leaf != null) {
            for (int i = 0; i < leaf.keys.size(); i++) {
                K key = leaf.keys.get(i);

                if (key.compareTo(endKey) > 0) return results;  // Past end
                if (key.compareTo(startKey) >= 0) {             // In range
                    results.add(leaf.values.get(i));
                }
            }
            leaf = leaf.next;
        }

        return results;
    }

    /**
     * Helper: Find the leaf node where key should be
     */
    private LeafNode findLeaf(K key) {
        Node current = root;

        // At each internal node, pick the correct child
        // Stop when you reach a leaf

        while (current instanceof InternalNode internalNode) {
            int result = Collections.binarySearch(internalNode.keys, key);

            // special java binary search return logic
            int childIndex;
            if (result < 0) {
                // Not found - happy case!
                /*
                Given [10,100,1000]
                5 has an insertionIndex of 0
                50 has an insertionIndex of 1
                500 has an insertionIndex of 2
                5000 has an insertionIndex of 3

                Which is the same as the b+tree child index!
                 */
                int insertionIndex = -result - 1;
                childIndex = insertionIndex;
            } else {
                // Found - OOBE case
                /*
                Given [10,100,1000]
                100 will be found at key index 1.
                But because of the way b+trees work,
                we know this key resides in child index 2
                 */
                childIndex = result + 1;
            }
            current = internalNode.children.get(childIndex);
        }

        return (LeafNode) current;
    }

    /**
     * Helper: Split a full leaf node
     * and add to the parent (creating a new parent if necessary)
     */
    private void splitLeaf(LeafNode leaf) {
        // Build the new node and update pointers of siblings
        int splitIndex = leaf.keys.size() / 2;
        LeafNode newRightLeaf = new LeafNode();
        newRightLeaf.keys.addAll(leaf.keys.subList(splitIndex, leaf.keys.size()));
        newRightLeaf.values.addAll(leaf.values.subList(splitIndex, leaf.values.size()));
        newRightLeaf.next = leaf.next;
        leaf.next = newRightLeaf;

        // Insert into parent
        K promotedKey = leaf.keys.get(splitIndex);
        insertIntoParent(leaf, promotedKey, newRightLeaf);

        // Clean up old node
        leaf.keys.subList(splitIndex, leaf.keys.size()).clear();
        leaf.values.subList(splitIndex, leaf.values.size()).clear();
    }

    /**
     * Helper: Split a full internal node
     * and add to the parent (creating a new parent if necessary)
     */
    private void splitInternal(InternalNode node) {
        // Build the new node and update pointers of its children
        int promotedKeyIndex = node.keys.size() / 2;
        InternalNode newRightNode = new InternalNode();
        newRightNode.keys.addAll(node.keys.subList(promotedKeyIndex + 1, node.keys.size()));
        newRightNode.children.addAll(node.children.subList(promotedKeyIndex + 1, node.children.size()));
        for (Node child : newRightNode.children) {
            child.parent = newRightNode;
        }

        // Insert into parent
        K promotedKey = node.keys.get(promotedKeyIndex);
        insertIntoParent(node, promotedKey, newRightNode);

        // Clean up old node
        node.keys.subList(promotedKeyIndex, node.keys.size()).clear();
        node.children.subList(promotedKeyIndex + 1, node.children.size()).clear();
    }

    /**
     * Helper: Insert promoted key and right child into parent node
     * Creates new parent if necessary
     */
    private void insertIntoParent(Node leftNode, K promotedKey, Node rightNode) {
        InternalNode parent = (InternalNode) leftNode.parent;

        // Ensure parent exists
        if (parent == null) {
            parent = new InternalNode();
            leftNode.parent = parent;
            parent.children.add(leftNode);
        }

        // Insert promoted key and new right child
        rightNode.parent = parent;
        int keyInsertionPoint = -Collections.binarySearch(parent.keys, promotedKey) - 1;
        parent.keys.add(keyInsertionPoint, promotedKey);
        parent.children.add(keyInsertionPoint + 1, rightNode);
    }

    /**
     * Print tree structure (for debugging)
     */
    public void printTree() {
        printNode(root, 0);
    }

    private void printNode(Node node, int level) {
        String indent = "  ".repeat(level);
        System.out.println(indent + "Level " + level + ": " + node.keys);

        if (node instanceof InternalNode internal) {
            for (Node child : internal.children) {
                printNode(child, level + 1);
            }
        }
    }
}
