package com.study.systems.messagequeues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeadLetterQueueTest {

    @Test
    void testSuccessfulMessageProcessingReturnsTrue() throws InterruptedException {
        DeadLetterQueue dlq = new DeadLetterQueue(10, 3);

        dlq.send(new SimpleMessageQueue.Message("msg1", "Good message"));

        DeadLetterQueue.MessageProcessor goodProcessor = message -> {
            // No exception — successful processing
        };

        boolean result = dlq.processWithRetry(goodProcessor);
        assertTrue(result);
    }

    @Test
    void testFailingMessageReturnsFalse() throws InterruptedException {
        DeadLetterQueue dlq = new DeadLetterQueue(10, 3);

        dlq.send(new SimpleMessageQueue.Message("msg1", "Bad message"));

        DeadLetterQueue.MessageProcessor failingProcessor = message -> {
            throw new Exception("Simulated failure");
        };

        boolean result = dlq.processWithRetry(failingProcessor);
        assertFalse(result);
    }

    @Test
    void testStatsAfterSend() throws InterruptedException {
        DeadLetterQueue dlq = new DeadLetterQueue(10, 3);

        dlq.send(new SimpleMessageQueue.Message("msg1", "A"));
        dlq.send(new SimpleMessageQueue.Message("msg2", "B"));

        DeadLetterQueue.DLQStats stats = dlq.getStats();
        assertEquals(2, stats.mainQueueSize);
        assertEquals(0, stats.dlqSize);
    }

    @Test
    void testMessageMovesToDLQAfterMaxRetries() throws InterruptedException {
        DeadLetterQueue dlq = new DeadLetterQueue(10, 2);

        dlq.send(new SimpleMessageQueue.Message("bad", "Always fails"));

        DeadLetterQueue.MessageProcessor failingProcessor = message -> {
            throw new Exception("Always fails");
        };

        // maxRetries=2: fail attempt 1 (retry 1), fail attempt 2 (retry 2 exhausted → DLQ)
        dlq.processWithRetry(failingProcessor);
        dlq.processWithRetry(failingProcessor);
        dlq.processWithRetry(failingProcessor); // After this call, should be in DLQ

        DeadLetterQueue.DLQStats stats = dlq.getStats();
        assertEquals(1, stats.dlqSize);
    }
}
