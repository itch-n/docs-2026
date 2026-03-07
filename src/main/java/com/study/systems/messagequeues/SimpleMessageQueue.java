package com.study.systems.messagequeues;

import java.util.*;
import java.util.concurrent.*;

/**
 * Simple Message Queue: FIFO with blocking operations
 *
 * Key principles:
 * - First In First Out ordering
 * - Blocking when empty (wait for messages)
 * - Thread-safe operations
 * - Decouples producers and consumers
 */

public class SimpleMessageQueue {

    private final Queue<Message> queue;
    private final int capacity;
    private final Object lock = new Object();

    /**
     * Initialize simple message queue
     *
     * @param capacity Maximum queue size
     *
     * TODO: Initialize queue
     * - Create LinkedList for messages
     * - Set capacity limit
     */
    public SimpleMessageQueue(int capacity) {
        // TODO: Initialize queue (LinkedList)

        // TODO: Store capacity

        this.queue = null; // Replace
        this.capacity = 0;
    }

    /**
     * Send message to queue (producer)
     *
     * @param message Message to send
     * @throws InterruptedException if interrupted while waiting
     *
     * TODO: Implement send
     * 1. Wait if queue is full
     * 2. Add message to queue
     * 3. Notify waiting consumers
     *
     * Hint: Use wait() and notifyAll() with synchronized block
     */
    public void send(Message message) throws InterruptedException {
        synchronized (lock) {
            // TODO: Implement iteration/conditional logic

            // TODO: Add message to queue

            // TODO: Notify all waiting consumers
            // lock.notifyAll()
        }
    }

    /**
     * Receive message from queue (consumer)
     *
     * @return Next message from queue
     * @throws InterruptedException if interrupted while waiting
     *
     * TODO: Implement receive
     * 1. Wait if queue is empty
     * 2. Remove and return message
     * 3. Notify waiting producers
     */
    public Message receive() throws InterruptedException {
        synchronized (lock) {
            // TODO: Implement iteration/conditional logic

            // TODO: Remove message from queue

            // TODO: Notify all waiting producers
            // lock.notifyAll()

            // TODO: Return message

            return null; // Replace
        }
    }

    /**
     * Try to receive with timeout
     *
     * @param timeoutMs Timeout in milliseconds
     * @return Message or null if timeout
     */
    public Message receive(long timeoutMs) throws InterruptedException {
        synchronized (lock) {
            long deadline = System.currentTimeMillis() + timeoutMs;

            // TODO: Wait until message available or timeout

            // TODO: Implement iteration/conditional logic

            return null; // Replace (or message)
        }
    }

    /**
     * Get queue size
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * Check if queue is empty
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    static class Message {
        String id;
        String content;
        long timestamp;

        public Message(String id, String content) {
            this.id = id;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return "Message{id='" + id + "', content='" + content + "'}";
        }
    }
}
