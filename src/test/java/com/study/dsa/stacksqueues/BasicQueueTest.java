package com.study.dsa.stacksqueues;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicQueueTest {

    // ---- QueueWithStacks ------------------------------------------------

    @Test
    void testQueueWithStacksFifoOrder() {
        // enqueue 1, enqueue 2  →  peek = 1, dequeue = 1, not empty, dequeue = 2, empty
        BasicQueue.QueueWithStacks queue = new BasicQueue.QueueWithStacks();
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(1, queue.peek());
        assertEquals(1, queue.dequeue());
        assertFalse(queue.empty());
        assertEquals(2, queue.dequeue());
        assertTrue(queue.empty());
    }

    @Test
    void testQueueWithStacksEmptyAfterCreation() {
        BasicQueue.QueueWithStacks queue = new BasicQueue.QueueWithStacks();
        assertTrue(queue.empty());
    }

    @Test
    void testQueueWithStacksThreeElements() {
        BasicQueue.QueueWithStacks queue = new BasicQueue.QueueWithStacks();
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());
        assertTrue(queue.empty());
    }

    // ---- CircularQueue --------------------------------------------------

    @Test
    void testCircularQueueBasicOperations() {
        BasicQueue.CircularQueue cq = new BasicQueue.CircularQueue(3);
        assertTrue(cq.enQueue(1));
        assertTrue(cq.enQueue(2));
        assertTrue(cq.enQueue(3));
        // Full — enQueue should return false
        assertFalse(cq.enQueue(4));
        assertEquals(1, cq.front());
        assertTrue(cq.isFull());
        // Dequeue then enqueue
        assertTrue(cq.deQueue());
        assertTrue(cq.enQueue(4));
        assertEquals(2, cq.front());
    }

    @Test
    void testCircularQueueIsEmpty() {
        BasicQueue.CircularQueue cq = new BasicQueue.CircularQueue(2);
        assertTrue(cq.isEmpty());
        cq.enQueue(5);
        assertFalse(cq.isEmpty());
        cq.deQueue();
        assertTrue(cq.isEmpty());
    }

    @Test
    void testCircularQueueFrontOnEmpty() {
        BasicQueue.CircularQueue cq = new BasicQueue.CircularQueue(1);
        // front() on empty queue should return -1
        assertEquals(-1, cq.front());
    }

    @Test
    void testCircularQueueWrapAround() {
        // Verify wraparound: fill, dequeue 2, enqueue 2 more, check order
        BasicQueue.CircularQueue cq = new BasicQueue.CircularQueue(3);
        cq.enQueue(1);
        cq.enQueue(2);
        cq.enQueue(3);
        cq.deQueue(); // remove 1
        cq.deQueue(); // remove 2
        cq.enQueue(4);
        cq.enQueue(5);
        assertEquals(3, cq.front());
    }
}
