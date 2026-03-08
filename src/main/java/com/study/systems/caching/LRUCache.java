package com.study.systems.caching;

import java.util.*;

/**
 * LRU Cache - Evicts least recently used items when full
 * Time: O(1) for get/put
 * Space: O(capacity)
 *
 * Key insight: Combine HashMap for O(1) lookup + Doubly Linked List for O(1) move/remove
 */
public class LRUCache<K, V> {

    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final DoublyLinkedList<K, V> list;

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    static class DoublyLinkedList<K, V> {
        Node<K, V> head, tail;

        DoublyLinkedList() {
            // TODO: Initialize sentinel nodes for cleaner edge case handling
        }

        /**
         * Add node to front (most recently used position)
         *
         * TODO: Implement addToFront
         */
        void addToFront(Node<K, V> node) {
            // TODO: Insert node right after head
        }

        /**
         * Remove node from list
         *
         * TODO: Implement remove
         */
        void remove(Node<K, V> node) {
            // TODO: Update prev/next pointers to bypass this node
        }

        /**
         * Remove and return least recently used (node before tail)
         *
         * TODO: Implement removeLast
         */
        Node<K, V> removeLast() {
            // TODO: Remove the node closest to tail
            // Handle empty list case

            return null; // Replace
        }

        /**
         * Move existing node to front
         *
         * TODO: Implement moveToFront
         */
        void moveToFront(Node<K, V> node) {
            // TODO: Reposition node to mark it as most recently used
        }
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.list = new DoublyLinkedList<>();
    }

    /**
     * Get value for key
     * Time: O(1)
     *
     * TODO: Implement get
     */
    public V get(K key) {
        // TODO: Lookup and update recency

        return null; // Replace
    }

    /**
     * Put key-value pair
     * Time: O(1)
     *
     * TODO: Implement put
     */
    public void put(K key, V value) {
        // TODO: Handle updates to existing keys
        // Handle eviction when at capacity
        // Add new entries appropriately
    }

    public int size() {
        return cache.size();
    }
}
