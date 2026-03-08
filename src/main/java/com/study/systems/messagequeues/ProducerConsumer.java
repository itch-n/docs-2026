package com.study.systems.messagequeues;

import java.util.concurrent.*;

import java.util.*;
/**
 * Producer-Consumer: Multiple producers and consumers processing work
 *
 * Key principles:
 * - Work distribution across consumers
 * - Load balancing
 * - Backpressure handling
 * - Graceful shutdown
 */

public class ProducerConsumer {

    private final SimpleMessageQueue queue;
    private final List<Thread> consumerThreads;
    private volatile boolean running;

    /**
     * Initialize producer-consumer system
     *
     * @param queueCapacity Queue size
     * @param numConsumers Number of consumer threads
     *
     * TODO: Initialize system
     * - Create message queue
     * - Create consumer threads
     * - Set running flag
     */
    public ProducerConsumer(int queueCapacity, int numConsumers) {
        // TODO: Create SimpleMessageQueue

        // TODO: Initialize consumer threads list

        // TODO: Track state

        this.queue = null; // Replace
        this.consumerThreads = null; // Replace
    }

    /**
     * Start all consumers
     *
     * TODO: Start consumer threads
     * - Each consumer polls queue and processes messages
     * - Handle InterruptedException
     * - Check running flag
     */
    public void start() {
        // TODO: Implement iteration/conditional logic
    }

    /**
     * Produce message (called by producers)
     *
     * TODO: Send message to queue
     */
    public void produce(String messageId, String content) throws InterruptedException {
        // TODO: Create Message and send to queue
    }

    /**
     * Process message (override in subclass for custom logic)
     *
     * TODO: Implement message processing
     * - Extract message content
     * - Perform work
     * - Handle errors
     */
    protected void processMessage(SimpleMessageQueue.Message message) {
        // TODO: Process message (simulated work)
        System.out.println(Thread.currentThread().getName() +
                          " processing: " + message);

        // TODO: Simulate work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Shutdown system
     *
     * TODO: Graceful shutdown
     * - Set running to false
     * - Wait for consumers to finish
     */
    public void shutdown() throws InterruptedException {
        // TODO: Track state

        // TODO: Interrupt all consumer threads

        // TODO: Wait for all threads to finish (join)
    }

    /**
     * Get queue statistics
     */
    public QueueStats getStats() {
        return new QueueStats(queue.size(), consumerThreads.size());
    }

    static class QueueStats {
        int queueSize;
        int activeConsumers;

        public QueueStats(int queueSize, int activeConsumers) {
            this.queueSize = queueSize;
            this.activeConsumers = activeConsumers;
        }
    }


    /**
     * Task: Unit of work to be processed
     * TODO: Add task fields (id, type, payload, priority, etc.)
     */
    static class Task {
        int id;
        Task(int id) { this.id = id; }
        @Override public String toString() { return "Task-" + id; }
    }

    /**
     * Producer: Puts tasks onto the queue
     * TODO: Implement production logic
     */
    static class Producer implements Runnable {
        private final BlockingQueue<Task> queue;
        private final int count;
        private final String name;

        Producer(BlockingQueue<Task> queue, int count, String name) {
            this.queue = queue;
            this.count = count;
            this.name = name;
        }

        @Override
        public void run() {
            // TODO: Produce 'count' tasks and put into queue
            try {
                for (int i = 0; i < count; i++) {
                    queue.put(new Task(i));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Consumer: Takes tasks off the queue and processes them
     * TODO: Implement consumption loop with graceful stop
     */
    static class Consumer implements Runnable {
        private final BlockingQueue<Task> queue;
        private final String name;
        private volatile boolean running = true;

        Consumer(BlockingQueue<Task> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        public void stop() { running = false; }

        @Override
        public void run() {
            // TODO: Poll queue while running, process each task
            try {
                while (running || !queue.isEmpty()) {
                    Task t = queue.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (t != null) {
                        System.out.println(name + " processed: " + t);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
