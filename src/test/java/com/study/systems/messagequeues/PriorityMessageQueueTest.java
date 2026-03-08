package com.study.systems.messagequeues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriorityMessageQueueTest {

    @Test
    void testHighPriorityDequeuesBeforeLow() throws InterruptedException {
        PriorityMessageQueue queue = new PriorityMessageQueue(10);

        queue.send(new PriorityMessageQueue.PriorityMessage(
            "low", "Low priority", PriorityMessageQueue.Priority.LOW));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "high", "High priority", PriorityMessageQueue.Priority.HIGH));

        PriorityMessageQueue.PriorityMessage first = queue.receive();
        assertEquals("high", first.id);
    }

    @Test
    void testPriorityOrderHighMediumLow() throws InterruptedException {
        PriorityMessageQueue queue = new PriorityMessageQueue(10);

        queue.send(new PriorityMessageQueue.PriorityMessage(
            "low", "Low", PriorityMessageQueue.Priority.LOW));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "high", "High", PriorityMessageQueue.Priority.HIGH));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "med", "Medium", PriorityMessageQueue.Priority.MEDIUM));

        assertEquals("high", queue.receive().id);
        assertEquals("med", queue.receive().id);
        assertEquals("low", queue.receive().id);
    }

    @Test
    void testSizeAfterSends() throws InterruptedException {
        PriorityMessageQueue queue = new PriorityMessageQueue(10);

        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m1", "A", PriorityMessageQueue.Priority.LOW));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m2", "B", PriorityMessageQueue.Priority.HIGH));

        assertEquals(2, queue.size());
    }

    @Test
    void testSizeDecrementsAfterReceive() throws InterruptedException {
        PriorityMessageQueue queue = new PriorityMessageQueue(10);

        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m1", "A", PriorityMessageQueue.Priority.LOW));
        queue.send(new PriorityMessageQueue.PriorityMessage(
            "m2", "B", PriorityMessageQueue.Priority.HIGH));
        queue.receive();

        assertEquals(1, queue.size());
    }

    @Test
    void testMultipleHighPriorityPreserveInsertionOrder() throws InterruptedException {
        PriorityMessageQueue queue = new PriorityMessageQueue(10);

        // Two HIGH messages — earlier one should come out first within same priority
        PriorityMessageQueue.PriorityMessage first = new PriorityMessageQueue.PriorityMessage(
            "h1", "High 1", PriorityMessageQueue.Priority.HIGH);
        Thread.sleep(5); // Ensure distinct timestamps
        PriorityMessageQueue.PriorityMessage second = new PriorityMessageQueue.PriorityMessage(
            "h2", "High 2", PriorityMessageQueue.Priority.HIGH);

        queue.send(first);
        queue.send(second);

        assertEquals("h1", queue.receive().id);
        assertEquals("h2", queue.receive().id);
    }
}
