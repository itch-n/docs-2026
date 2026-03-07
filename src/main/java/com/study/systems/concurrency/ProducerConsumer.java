package com.study.systems.concurrency;

import java.util.concurrent.*;
import java.util.*;

/**
 * Producer-Consumer Pattern
 *
 * Classic concurrency problem:
 * - Producers generate work items
 * - Consumers process work items
 * - Bounded buffer prevents memory overflow
 * - BlockingQueue handles all synchronization
 */
public class ProducerConsumer {

    /**
     * Work item representation
     */
    static class Task {
        private final int id;
        private final String data;
        private final long createdAt;

        public Task(int id, String data) {
            this.id = id;
            this.data = data;
            this.createdAt = System.currentTimeMillis();
        }

        public int getId() {
            return id;
        }

        public String getData() {
            return data;
        }

        public long getAge() {
            return System.currentTimeMillis() - createdAt;
        }

        @Override
        public String toString() {
            return "Task{id=" + id + ", data='" + data + "'}";
        }
    }

    /**
     * Producer: Generates tasks and puts them in queue
     */
    static class Producer implements Runnable {
        private final BlockingQueue<Task> queue;
        private final int numTasks;
        private final String name;

        public Producer(BlockingQueue<Task> queue, int numTasks, String name) {
            this.queue = queue;
            this.numTasks = numTasks;
            this.name = name;
        }

        /**
         * Produce tasks
         *
         * TODO: Implement producer logic
         * 1. Generate tasks
         * 2. Put into queue (blocks if queue is full)
         * 3. Handle interruption
         */
        @Override
        public void run() {
            // TODO: Produce numTasks
            //
            //       // Simulate work
            //       Thread.sleep(10);
            //     } catch (InterruptedException e) {
            //       Thread.currentThread().interrupt();
            //       break;
            //     }
            //   }
            //   System.out.println(name + " finished producing");
        }
    }

    /**
     * Consumer: Takes tasks from queue and processes them
     */
    static class Consumer implements Runnable {
        private final BlockingQueue<Task> queue;
        private final String name;
        private volatile boolean running = true;

        public Consumer(BlockingQueue<Task> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        /**
         * Consume tasks
         *
         * TODO: Implement consumer logic
         * 1. Take from queue (blocks if empty)
         * 2. Process task
         * 3. Check for poison pill (shutdown signal)
         */
        @Override
        public void run() {
            // TODO: Consume tasks until stopped
            //
            //       if (task != null) {
            //         // Process task
            //         processTask(task);
            //       }
            //     } catch (InterruptedException e) {
            //       Thread.currentThread().interrupt();
            //       break;
            //     }
            //   }
            //   System.out.println(name + " stopped");
        }

        private void processTask(Task task) {
            System.out.println(name + " processing: " + task + " (age: " + task.getAge() + "ms)");
            // Simulate processing
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void stop() {
            running = false;
        }
    }

    /**
     * Pipeline: Multiple stages of processing
     * Each stage is a producer-consumer pair
     */
    static class ProcessingPipeline {
        private final BlockingQueue<Task> stage1Queue;
        private final BlockingQueue<Task> stage2Queue;
        private final BlockingQueue<Task> stage3Queue;

        public ProcessingPipeline(int queueCapacity) {
            this.stage1Queue = new ArrayBlockingQueue<>(queueCapacity);
            this.stage2Queue = new ArrayBlockingQueue<>(queueCapacity);
            this.stage3Queue = new ArrayBlockingQueue<>(queueCapacity);
        }

        /**
         * Start pipeline processing
         * Time: O(N) where N = tasks, Space: O(Q) where Q = queue capacity
         *
         * TODO: Implement multi-stage pipeline
         * Stage 1: Validate tasks
         * Stage 2: Transform tasks
         * Stage 3: Save results
         */
        public void start(int numTasks) {
            // TODO: Create stage 1 workers (producers to stage1Queue)
            // TODO: Create stage 2 workers (consume stage1, produce stage2)
            // TODO: Create stage 3 workers (consume stage2, produce stage3)
            // TODO: Create final consumers (consume stage3)

            System.out.println("Pipeline started with " + numTasks + " tasks");
        }

        public BlockingQueue<Task> getStage1Queue() {
            return stage1Queue;
        }
    }
}
