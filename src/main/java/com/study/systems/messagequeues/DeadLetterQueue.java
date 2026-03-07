package com.study.systems.messagequeues;

import java.util.*;
/**
 * Dead Letter Queue: Handle messages that fail processing
 *
 * Key principles:
 * - Retry failed messages
 * - Max retry limit
 * - Move to DLQ after max retries
 * - Allows manual inspection/reprocessing
 */

public class DeadLetterQueue {

    private final SimpleMessageQueue mainQueue;
    private final SimpleMessageQueue dlq;
    private final int maxRetries;
    private final Map<String, Integer> retryCount;

    /**
     * Initialize dead letter queue system
     *
     * @param capacity Queue capacity
     * @param maxRetries Maximum retry attempts
     *
     * TODO: Initialize queues
     * - Create main queue
     * - Create DLQ
     * - Initialize retry counter
     */
    public DeadLetterQueue(int capacity, int maxRetries) {
        // TODO: Create main queue

        // TODO: Create DLQ

        // TODO: Store maxRetries

        // TODO: Initialize retry counter map

        this.mainQueue = null; // Replace
        this.dlq = null; // Replace
        this.maxRetries = 0;
        this.retryCount = null; // Replace
    }

    /**
     * Send message to main queue
     */
    public void send(SimpleMessageQueue.Message message) throws InterruptedException {
        // TODO: Send to main queue
        // Initialize retry count to 0
    }

    /**
     * Process message with retry logic
     *
     * @param processor Message processor
     * @return true if processed successfully
     *
     * TODO: Process with retries
     * 1. Receive message from main queue
     * 2. Try to process
     * 3. If fails, check retry count
     * 4. If under limit, requeue with incremented count
     * 5. If over limit, move to DLQ
     */
    public boolean processWithRetry(MessageProcessor processor) throws InterruptedException {
        // TODO: Receive message from main queue

        // TODO: Try to process message
        try {
            // processor.process(message)
            // return true if successful
        } catch (Exception e) {
            // TODO: Get current retry count

            // TODO: Implement iteration/conditional logic

            // TODO: Implement iteration/conditional logic
        }

        return false; // Replace
    }

    /**
     * Get message from DLQ for manual processing
     */
    public SimpleMessageQueue.Message receiveDLQ() throws InterruptedException {
        return dlq.receive();
    }

    /**
     * Reprocess message from DLQ (manual retry)
     */
    public void reprocessFromDLQ(SimpleMessageQueue.Message message) throws InterruptedException {
        // TODO: Reset retry count and send to main queue
    }

    /**
     * Get statistics
     */
    public DLQStats getStats() {
        return new DLQStats(
            mainQueue.size(),
            dlq.size(),
            retryCount.size()
        );
    }

    interface MessageProcessor {
        void process(SimpleMessageQueue.Message message) throws Exception;
    }

    static class DLQStats {
        int mainQueueSize;
        int dlqSize;
        int messagesWithRetries;

        public DLQStats(int mainQueueSize, int dlqSize, int messagesWithRetries) {
            this.mainQueueSize = mainQueueSize;
            this.dlqSize = dlqSize;
            this.messagesWithRetries = messagesWithRetries;
        }

        @Override
        public String toString() {
            return "DLQStats{main=" + mainQueueSize +
                   ", dlq=" + dlqSize +
                   ", retrying=" + messagesWithRetries + "}";
        }
    }
}
