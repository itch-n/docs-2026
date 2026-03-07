package com.study.dsa.stacksqueues;

import java.util.*;

public class BasicQueue {

    /**
     * Problem: Implement queue using two stacks
     * Time: O(1) amortized, Space: O(n)
     *
     * TODO: Implement QueueWithStacks class
     */
    static class QueueWithStacks {
        // TODO: Use two stacks: inbox and outbox

        public void enqueue(int x) {
            // TODO: Push to inbox
        }

        public int dequeue() {
            // TODO: Implement iteration/conditional logic
            // TODO: Pop from outbox
            return 0;
        }

        public int peek() {
            // TODO: Implement iteration/conditional logic
            // TODO: Peek outbox
            return 0;
        }

        public boolean empty() {
            // TODO: Check if both stacks empty
            return true;
        }
    }

    /**
     * Problem: Implement circular queue
     * Time: O(1), Space: O(k)
     *
     * TODO: Implement CircularQueue class
     */
    static class CircularQueue {
        private int[] data;
        private int front, rear, size, capacity;

        public CircularQueue(int k) {
            // TODO: Initialize array and pointers
        }

        public boolean enQueue(int value) {
            // TODO: Check if full
            // TODO: Add element at rear
            // TODO: Update rear pointer (circular)
            return false;
        }

        public boolean deQueue() {
            // TODO: Check if empty
            // TODO: Update front pointer (circular)
            return false;
        }

        public int front() {
            // TODO: Return element at front
            return -1;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == capacity;
        }
    }
}
