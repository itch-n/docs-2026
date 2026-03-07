package com.study.systems.messagequeues;

import java.util.*;
/**
 * Priority Message Queue: Process high-priority messages first
 *
 * Key principles:
 * - Priority levels (HIGH, MEDIUM, LOW)
 * - Higher priority processed first
 * - FIFO within same priority
 * - Prevents starvation of low priority
 */

public class PriorityMessageQueue {

    private final PriorityQueue<PriorityMessage> queue;
    private final Object lock = new Object();
    private final int capacity;

    /**
     * Initialize priority queue
     *
     * @param capacity Maximum queue size
     *
     * TODO: Initialize priority queue
     * - Create PriorityQueue with comparator
     * - Sort by priority then timestamp
     */
    public PriorityMessageQueue(int capacity) {
        // TODO: Create PriorityQueue with comparator
        // Comparator: First by priority (descending), then timestamp (ascending)

        // TODO: Store capacity

        this.queue = null; // Replace
        this.capacity = 0;
    }

    /**
     * Send message with priority
     *
     * TODO: Add message to priority queue
     * - Wait if queue is full
     * - Add message
     * - Notify consumers
     */
    public void send(PriorityMessage message) throws InterruptedException {
        synchronized (lock) {
            // TODO: Wait while queue is full

            // TODO: Add message to queue

            // TODO: Notify waiting consumers
        }
    }

    /**
     * Receive highest priority message
     *
     * TODO: Get message with highest priority
     * - Wait if queue is empty
     * - Remove highest priority message
     * - Notify producers
     */
    public PriorityMessage receive() throws InterruptedException {
        synchronized (lock) {
            // TODO: Wait while queue is empty

            // TODO: Poll highest priority message

            // TODO: Notify waiting producers

            return null; // Replace
        }
    }

    /**
     * Get queue size
     */
    public synchronized int size() {
        return queue.size();
    }

    static class PriorityMessage implements Comparable<PriorityMessage> {
        String id;
        String content;
        Priority priority;
        long timestamp;

        public PriorityMessage(String id, String content, Priority priority) {
            this.id = id;
            this.content = content;
            this.priority = priority;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(PriorityMessage other) {
            // TODO: Compare by priority first (higher priority first)

            // Hint:
            // int priorityCompare = other.priority.value - this.priority.value;
            // if (priorityCompare != 0) return priorityCompare;
            // return Long.compare(this.timestamp, other.timestamp);

            return 0; // Replace
        }

        @Override
        public String toString() {
            return "PriorityMessage{id='" + id + "', priority=" + priority + "}";
        }
    }

    enum Priority {
        LOW(1), MEDIUM(2), HIGH(3);

        final int value;

        Priority(int value) {
            this.value = value;
        }
    }
}
