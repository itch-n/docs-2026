package com.study.systems.caching;

import java.util.*;

/**
 * LFU Cache - Evicts least frequently used items when full
 * Time: O(1) for get/put
 * Space: O(capacity)
 *
 * Key insight: Track frequency for each node, maintain lists per frequency level
 */
public class LFUCache<K, V> {

    private final int capacity;
    private int minFreq;
    private final Map<K, Node<K, V>> cache;
    private final Map<Integer, DoublyLinkedList<K, V>> freqMap; // freq -> list of nodes

    static class Node<K, V> {
        K key;
        V value;
        int freq;
        Node<K, V> prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    static class DoublyLinkedList<K, V> {
        Node<K, V> head, tail;

        DoublyLinkedList() {
            // TODO: Initialize sentinel nodes
        }

        void addToFront(Node<K, V> node) {
            // TODO: Add to front of list
        }

        void remove(Node<K, V> node) {
            // TODO: Remove from list
        }

        Node<K, V> removeLast() {
            // TODO: Remove least frequently used
            return null;
        }

        boolean isEmpty() {
            // TODO: Check if list has any nodes
            return true;
        }
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    /**
     * Get value for key
     * Time: O(1)
     *
     * TODO: Implement get
     */
    public V get(K key) {
        // TODO: Lookup and update frequency tracking

        return null; // Replace
    }

    /**
     * Put key-value pair
     * Time: O(1)
     *
     * TODO: Implement put
     */
    public void put(K key, V value) {
        if (capacity <= 0) return;

        // TODO: Handle updates and new insertions
        // Evict least frequently used when at capacity
        // Manage frequency tracking structures
    }

    /**
     * Update frequency of node
     *
     * TODO: Implement updateFrequency
     */
    private void updateFrequency(Node<K, V> node) {
        // TODO: Move node from current frequency list to next
    }
}
