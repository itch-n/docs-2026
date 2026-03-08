package com.study.systems.messagequeues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleMessageQueueTest {

    @Test
    void testSendAndReceiveMessage() throws InterruptedException {
        SimpleMessageQueue queue = new SimpleMessageQueue(5);
        SimpleMessageQueue.Message msg = new SimpleMessageQueue.Message("msg1", "Hello");

        queue.send(msg);
        SimpleMessageQueue.Message received = queue.receive();

        assertNotNull(received);
        assertEquals("msg1", received.id);
        assertEquals("Hello", received.content);
    }

    @Test
    void testFifoOrdering() throws InterruptedException {
        SimpleMessageQueue queue = new SimpleMessageQueue(5);

        queue.send(new SimpleMessageQueue.Message("m1", "First"));
        queue.send(new SimpleMessageQueue.Message("m2", "Second"));
        queue.send(new SimpleMessageQueue.Message("m3", "Third"));

        assertEquals("m1", queue.receive().id);
        assertEquals("m2", queue.receive().id);
        assertEquals("m3", queue.receive().id);
    }

    @Test
    void testSizeAfterSends() throws InterruptedException {
        SimpleMessageQueue queue = new SimpleMessageQueue(5);

        queue.send(new SimpleMessageQueue.Message("m1", "A"));
        queue.send(new SimpleMessageQueue.Message("m2", "B"));

        assertEquals(2, queue.size());
    }

    @Test
    void testIsEmptyWhenNoMessages() {
        SimpleMessageQueue queue = new SimpleMessageQueue(5);
        assertTrue(queue.isEmpty());
    }

    @Test
    void testIsNotEmptyAfterSend() throws InterruptedException {
        SimpleMessageQueue queue = new SimpleMessageQueue(5);
        queue.send(new SimpleMessageQueue.Message("m1", "A"));

        assertFalse(queue.isEmpty());
    }

    @Test
    void testSizeDecrementsAfterReceive() throws InterruptedException {
        SimpleMessageQueue queue = new SimpleMessageQueue(5);

        queue.send(new SimpleMessageQueue.Message("m1", "A"));
        queue.send(new SimpleMessageQueue.Message("m2", "B"));
        queue.receive();

        assertEquals(1, queue.size());
    }

    @Test
    void testProducerConsumerConcurrency() throws InterruptedException {
        SimpleMessageQueue queue = new SimpleMessageQueue(10);
        int[] receivedCount = {0};

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    queue.send(new SimpleMessageQueue.Message("m" + i, "Content " + i));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    SimpleMessageQueue.Message msg = queue.receive();
                    if (msg != null) receivedCount[0]++;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        assertEquals(5, receivedCount[0]);
        assertEquals(0, queue.size());
    }
}
