package com.study.systems.messagequeues;

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
}
