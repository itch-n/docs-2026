package com.study.systems.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

class ProducerConsumerTest {

    // -------------------------------------------------------------------------
    // Producer
    // -------------------------------------------------------------------------

    @Test
    void testProducerFillsQueue() throws InterruptedException {
        BlockingQueue<ProducerConsumer.Task> queue = new ArrayBlockingQueue<>(100);
        ProducerConsumer.Producer producer = new ProducerConsumer.Producer(queue, 10, "P1");

        Thread t = new Thread(producer);
        t.start();
        t.join();

        assertEquals(10, queue.size());
    }

    @Test
    void testProducerWithMultipleProducers() {
        BlockingQueue<ProducerConsumer.Task> queue = new ArrayBlockingQueue<>(100);

        // 3 producers × 5 tasks each = 15 total
        for (int i = 0; i < 3; i++) {
            final String name = "P" + i;
            Thread t = new Thread(new ProducerConsumer.Producer(queue, 5, name));
            t.start();
        }

        await().atMost(200, MILLISECONDS).until(() -> queue.size() == 15);
    }

    // -------------------------------------------------------------------------
    // Consumer
    // -------------------------------------------------------------------------

    @Test
    void testConsumerDrainsQueue() throws InterruptedException {
        BlockingQueue<ProducerConsumer.Task> queue = new ArrayBlockingQueue<>(100);
        for (int i = 0; i < 5; i++) {
            queue.put(new ProducerConsumer.Task(i, "data" + i));
        }

        ProducerConsumer.Consumer consumer = new ProducerConsumer.Consumer(queue, "C1");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        await().atMost(200, MILLISECONDS).until(queue::isEmpty);

        consumer.stop();
        consumerThread.interrupt();
    }

    @Test
    void testConsumerStopsGracefully() {
        BlockingQueue<ProducerConsumer.Task> queue = new ArrayBlockingQueue<>(10);
        ProducerConsumer.Consumer consumer = new ProducerConsumer.Consumer(queue, "C1");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        consumer.stop();
        consumerThread.interrupt();

        await().atMost(200, MILLISECONDS).until(() -> !consumerThread.isAlive());
    }

    @Test
    void testProducerConsumerEndToEnd() throws InterruptedException {
        BlockingQueue<ProducerConsumer.Task> queue = new ArrayBlockingQueue<>(50);
        ProducerConsumer.Consumer consumer = new ProducerConsumer.Consumer(queue, "C1");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        ProducerConsumer.Producer producer = new ProducerConsumer.Producer(queue, 20, "P1");
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join(); // wait for producer to finish

        await().atMost(500, MILLISECONDS).until(queue::isEmpty);

        consumer.stop();
        consumerThread.interrupt();
    }

    // -------------------------------------------------------------------------
    // ProcessingPipeline
    // -------------------------------------------------------------------------

    @Test
    void testPipelineStartDoesNotThrow() {
        ProducerConsumer.ProcessingPipeline pipeline =
                new ProducerConsumer.ProcessingPipeline(50);
        assertDoesNotThrow(() -> pipeline.start(10));
    }

    @Test
    void testPipelineCreatesStage1Queue() {
        ProducerConsumer.ProcessingPipeline pipeline =
                new ProducerConsumer.ProcessingPipeline(50);
        assertNotNull(pipeline.getStage1Queue());
    }
}
