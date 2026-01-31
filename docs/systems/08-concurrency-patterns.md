# 08. Concurrency Patterns

> Locks, thread pools, and synchronization - The foundation of multi-threaded systems

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing concurrency patterns, explain them simply.

**Prompts to guide you:**

1. **What is a lock in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do we need locks in concurrent programs?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for ReentrantLock:**
   - Example: "A ReentrantLock is like a bathroom key that you can use multiple times..."
   - Your analogy: _[Fill in]_

4. **What is a thread pool in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **Why use a thread pool instead of creating threads directly?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for BlockingQueue:**
   - Example: "A BlockingQueue is like a conveyor belt in a factory..."
   - Your analogy: _[Fill in]_

---

## Core Implementation

### Pattern 1: Lock-Based Synchronization

**Concept:** Using explicit locks to control access to shared resources and prevent race conditions.

**Use case:** Thread-safe counters, banking transactions, resource pooling.

```java
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Lock-Based Synchronization
 *
 * Key concepts:
 * - ReentrantLock: Lock that same thread can acquire multiple times
 * - Read-Write locks: Multiple readers OR single writer
 * - Lock ordering: Prevents deadlocks by consistent acquisition order
 * - Try-lock: Non-blocking lock attempts
 */
public class LockBasedSync {

    /**
     * Thread-safe counter with ReentrantLock
     * Better control than synchronized keyword
     */
    static class ThreadSafeCounter {
        private int count;
        private final ReentrantLock lock;

        public ThreadSafeCounter() {
            this.count = 0;
            this.lock = new ReentrantLock();
        }

        /**
         * Increment counter safely
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement thread-safe increment
         * 1. Acquire lock
         * 2. Increment count
         * 3. Release lock in finally block
         */
        public void increment() {
            // TODO: Acquire lock
            //   lock.lock();
            //   try {
            //     count++;
            //   } finally {
            //     lock.unlock(); // Always unlock in finally
            //   }
        }

        /**
         * Get current count
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement thread-safe read
         */
        public int getCount() {
            // TODO: Acquire lock for reading
            //   lock.lock();
            //   try {
            //     return count;
            //   } finally {
            //     lock.unlock();
            //   }
            return 0; // Replace
        }

        /**
         * Try to increment with timeout
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement try-lock with timeout
         * 1. Try to acquire lock with timeout
         * 2. If acquired, increment and return true
         * 3. If timeout, return false
         */
        public boolean tryIncrement(long timeoutMs) {
            // TODO: Try lock with timeout
            //   try {
            //     if (lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS)) {
            //       try {
            //         count++;
            //         return true;
            //       } finally {
            //         lock.unlock();
            //       }
            //     }
            //   } catch (InterruptedException e) {
            //     Thread.currentThread().interrupt();
            //   }
            return false; // Replace
        }

        /**
         * Check if lock is held by current thread
         */
        public boolean isLocked() {
            return lock.isHeldByCurrentThread();
        }
    }

    /**
     * Read-Write Lock: Multiple readers, single writer
     * Optimizes for read-heavy workloads
     */
    static class ReadWriteCache<K, V> {
        private final Map<K, V> cache;
        private final ReadWriteLock rwLock;
        private final Lock readLock;
        private final Lock writeLock;

        public ReadWriteCache() {
            this.cache = new HashMap<>();
            this.rwLock = new ReentrantReadWriteLock();
            this.readLock = rwLock.readLock();
            this.writeLock = rwLock.writeLock();
        }

        /**
         * Get value (multiple readers allowed)
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement read with read lock
         * 1. Acquire read lock (doesn't block other readers)
         * 2. Read from cache
         * 3. Release read lock
         */
        public V get(K key) {
            // TODO: Use read lock
            //   readLock.lock();
            //   try {
            //     return cache.get(key);
            //   } finally {
            //     readLock.unlock();
            //   }
            return null; // Replace
        }

        /**
         * Put value (exclusive write access)
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement write with write lock
         * 1. Acquire write lock (blocks all readers and writers)
         * 2. Write to cache
         * 3. Release write lock
         */
        public void put(K key, V value) {
            // TODO: Use write lock
            //   writeLock.lock();
            //   try {
            //     cache.put(key, value);
            //   } finally {
            //     writeLock.unlock();
            //   }
        }

        /**
         * Clear cache (write operation)
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement clear with write lock
         */
        public void clear() {
            // TODO: Use write lock to clear
            //   writeLock.lock();
            //   try {
            //     cache.clear();
            //   } finally {
            //     writeLock.unlock();
            //   }
        }

        public int size() {
            readLock.lock();
            try {
                return cache.size();
            } finally {
                readLock.unlock();
            }
        }
    }

    /**
     * Bank Transfer: Demonstrates lock ordering to prevent deadlock
     */
    static class BankAccount {
        private final int id;
        private int balance;
        private final ReentrantLock lock;

        public BankAccount(int id, int initialBalance) {
            this.id = id;
            this.balance = initialBalance;
            this.lock = new ReentrantLock();
        }

        /**
         * Transfer money between accounts
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement transfer with lock ordering
         * 1. Acquire locks in consistent order (by account ID)
         * 2. Check sufficient balance
         * 3. Perform transfer
         * 4. Release locks in reverse order
         *
         * CRITICAL: Always lock in same order to prevent deadlock!
         * If thread A locks account1 then account2, and thread B locks
         * account2 then account1, DEADLOCK can occur.
         */
        public static boolean transfer(BankAccount from, BankAccount to, int amount) {
            // TODO: Lock ordering - always lock lower ID first
            //   BankAccount first = from.id < to.id ? from : to;
            //   BankAccount second = from.id < to.id ? to : from;

            // TODO: Acquire locks in order
            //   first.lock.lock();
            //   try {
            //     second.lock.lock();
            //     try {
            //       // Check balance
            //       if (from.balance >= amount) {
            //         from.balance -= amount;
            //         to.balance += amount;
            //         return true;
            //       }
            //       return false;
            //     } finally {
            //       second.lock.unlock();
            //     }
            //   } finally {
            //     first.lock.unlock();
            //   }

            return false; // Replace
        }

        public int getBalance() {
            lock.lock();
            try {
                return balance;
            } finally {
                lock.unlock();
            }
        }

        public int getId() {
            return id;
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.concurrent.*;

public class LockBasedSyncClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Lock-Based Synchronization ===\n");

        // Test 1: Thread-safe counter
        System.out.println("--- Test 1: Thread-Safe Counter ---");
        testThreadSafeCounter();

        // Test 2: Read-write cache
        System.out.println("\n--- Test 2: Read-Write Cache ---");
        testReadWriteCache();

        // Test 3: Bank transfers (lock ordering)
        System.out.println("\n--- Test 3: Bank Transfers ---");
        testBankTransfers();
    }

    static void testThreadSafeCounter() throws InterruptedException {
        LockBasedSync.ThreadSafeCounter counter = new LockBasedSync.ThreadSafeCounter();
        int numThreads = 10;
        int incrementsPerThread = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Launch threads to increment
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        int expected = numThreads * incrementsPerThread;
        int actual = counter.getCount();
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
        System.out.println("Correct: " + (expected == actual));
    }

    static void testReadWriteCache() throws InterruptedException {
        LockBasedSync.ReadWriteCache<String, Integer> cache = new LockBasedSync.ReadWriteCache<>();

        // Writer thread
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cache.put("key" + i, i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Reader threads
        Thread[] readers = new Thread[5];
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    Integer val = cache.get("key" + j);
                    if (val != null) {
                        System.out.println(Thread.currentThread().getName() + " read: " + val);
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        writer.start();
        for (Thread reader : readers) {
            reader.start();
        }

        writer.join();
        for (Thread reader : readers) {
            reader.join();
        }

        System.out.println("Cache size: " + cache.size());
    }

    static void testBankTransfers() throws InterruptedException {
        LockBasedSync.BankAccount acc1 = new LockBasedSync.BankAccount(1, 1000);
        LockBasedSync.BankAccount acc2 = new LockBasedSync.BankAccount(2, 1000);

        System.out.println("Initial balances:");
        System.out.println("Account 1: " + acc1.getBalance());
        System.out.println("Account 2: " + acc2.getBalance());

        // Concurrent transfers
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                LockBasedSync.BankAccount.transfer(acc1, acc2, 10);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                LockBasedSync.BankAccount.transfer(acc2, acc1, 5);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("\nFinal balances:");
        System.out.println("Account 1: " + acc1.getBalance());
        System.out.println("Account 2: " + acc2.getBalance());
        System.out.println("Total: " + (acc1.getBalance() + acc2.getBalance()));
        System.out.println("Correct: " + ((acc1.getBalance() + acc2.getBalance()) == 2000));
    }
}
```

---

### Pattern 2: Producer-Consumer with BlockingQueue

**Concept:** Decoupling producers and consumers using a thread-safe bounded queue.

**Use case:** Task queues, message processing, batch processing pipelines.

```java
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
            //   for (int i = 0; i < numTasks; i++) {
            //     try {
            //       Task task = new Task(i, name + "-data-" + i);
            //       queue.put(task); // Blocks if queue is full
            //       System.out.println(name + " produced: " + task);
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
            //   while (running) {
            //     try {
            //       // poll with timeout to check running flag periodically
            //       Task task = queue.poll(100, TimeUnit.MILLISECONDS);
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
```

**Runnable Client Code:**

```java
import java.util.concurrent.*;

public class ProducerConsumerClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Producer-Consumer Pattern ===\n");

        // Test 1: Single producer, single consumer
        System.out.println("--- Test 1: Single Producer-Consumer ---");
        testSingleProducerConsumer();

        Thread.sleep(2000);

        // Test 2: Multiple producers, multiple consumers
        System.out.println("\n--- Test 2: Multiple Producers-Consumers ---");
        testMultipleProducersConsumers();

        Thread.sleep(2000);

        // Test 3: Different queue types
        System.out.println("\n--- Test 3: Queue Type Comparison ---");
        testQueueTypes();
    }

    static void testSingleProducerConsumer() throws InterruptedException {
        BlockingQueue<ProducerConsumer.Task> queue = new ArrayBlockingQueue<>(10);

        ProducerConsumer.Producer producer = new ProducerConsumer.Producer(queue, 20, "P1");
        ProducerConsumer.Consumer consumer = new ProducerConsumer.Consumer(queue, "C1");

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        Thread.sleep(1000); // Let consumer finish
        consumer.stop();
        consumerThread.join();

        System.out.println("Queue remaining: " + queue.size());
    }

    static void testMultipleProducersConsumers() throws InterruptedException {
        BlockingQueue<ProducerConsumer.Task> queue = new ArrayBlockingQueue<>(50);

        // Create 3 producers
        Thread[] producers = new Thread[3];
        for (int i = 0; i < producers.length; i++) {
            ProducerConsumer.Producer producer = new ProducerConsumer.Producer(queue, 10, "P" + (i+1));
            producers[i] = new Thread(producer);
            producers[i].start();
        }

        // Create 2 consumers
        ProducerConsumer.Consumer[] consumers = new ProducerConsumer.Consumer[2];
        Thread[] consumerThreads = new Thread[2];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new ProducerConsumer.Consumer(queue, "C" + (i+1));
            consumerThreads[i] = new Thread(consumers[i]);
            consumerThreads[i].start();
        }

        // Wait for all producers
        for (Thread producer : producers) {
            producer.join();
        }

        // Wait for queue to drain
        Thread.sleep(2000);

        // Stop consumers
        for (ProducerConsumer.Consumer consumer : consumers) {
            consumer.stop();
        }
        for (Thread thread : consumerThreads) {
            thread.join();
        }

        System.out.println("Final queue size: " + queue.size());
    }

    static void testQueueTypes() throws InterruptedException {
        System.out.println("ArrayBlockingQueue: Bounded, array-backed");
        System.out.println("LinkedBlockingQueue: Optionally bounded, linked-list");
        System.out.println("PriorityBlockingQueue: Unbounded, ordered by priority");
        System.out.println("SynchronousQueue: No capacity, direct handoff");

        // Example: PriorityBlockingQueue
        BlockingQueue<Integer> priorityQueue = new PriorityBlockingQueue<>();
        priorityQueue.put(5);
        priorityQueue.put(1);
        priorityQueue.put(10);
        priorityQueue.put(3);

        System.out.println("\nPriorityBlockingQueue order:");
        while (!priorityQueue.isEmpty()) {
            System.out.println("  " + priorityQueue.take());
        }
    }
}
```

---

### Pattern 3: Thread-Safe Data Structures

**Concept:** Using concurrent collections and lock-free algorithms for high-performance concurrent access.

**Use case:** Caches, shared state, counters in high-throughput systems.

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;

/**
 * Thread-Safe Data Structures
 *
 * Three approaches:
 * 1. ConcurrentHashMap: Lock striping for scalability
 * 2. CopyOnWriteArrayList: Snapshot iteration, write-heavy penalty
 * 3. Atomic variables: Lock-free using CAS (Compare-And-Swap)
 */
public class ThreadSafeDataStructures {

    /**
     * ConcurrentHashMap: Scalable concurrent hash table
     * Uses lock striping - different segments can be locked independently
     */
    static class ConcurrentCache<K, V> {
        private final ConcurrentHashMap<K, V> cache;
        private final AtomicLong hits;
        private final AtomicLong misses;

        public ConcurrentCache() {
            this.cache = new ConcurrentHashMap<>();
            this.hits = new AtomicLong(0);
            this.misses = new AtomicLong(0);
        }

        /**
         * Get with statistics tracking
         * Time: O(1) average, Space: O(1)
         *
         * TODO: Implement thread-safe get with metrics
         * 1. Try to get from cache
         * 2. Track hit/miss
         * 3. Return value
         */
        public V get(K key) {
            // TODO: Implement with hit/miss tracking
            //   V value = cache.get(key);
            //   if (value != null) {
            //     hits.incrementAndGet(); // Atomic increment
            //   } else {
            //     misses.incrementAndGet();
            //   }
            //   return value;
            return null; // Replace
        }

        /**
         * Put if absent (atomic operation)
         * Time: O(1) average, Space: O(1)
         *
         * TODO: Implement atomic put-if-absent
         * 1. Use putIfAbsent (atomic operation)
         * 2. Return previous value (null if inserted)
         */
        public V putIfAbsent(K key, V value) {
            // TODO: Use ConcurrentHashMap's putIfAbsent
            //   return cache.putIfAbsent(key, value);
            return null; // Replace
        }

        /**
         * Compute value atomically
         * Time: O(1) average, Space: O(1)
         *
         * TODO: Implement atomic compute
         * Use computeIfAbsent to atomically compute value if missing
         */
        public V computeIfAbsent(K key, java.util.function.Function<K, V> mappingFunction) {
            // TODO: Use computeIfAbsent
            //   return cache.computeIfAbsent(key, mappingFunction);
            return null; // Replace
        }

        /**
         * Atomic update
         * Time: O(1) average, Space: O(1)
         *
         * TODO: Implement atomic value update
         * Use compute to atomically update existing value
         */
        public V update(K key, java.util.function.BiFunction<K, V, V> remappingFunction) {
            // TODO: Use compute for atomic update
            //   return cache.compute(key, remappingFunction);
            return null; // Replace
        }

        public double getHitRate() {
            long totalHits = hits.get();
            long totalRequests = totalHits + misses.get();
            return totalRequests == 0 ? 0.0 : (double) totalHits / totalRequests;
        }

        public int size() {
            return cache.size();
        }
    }

    /**
     * Copy-On-Write List: Thread-safe list with snapshot iteration
     * Every write creates a new copy of underlying array
     * Great for read-heavy workloads with infrequent updates
     */
    static class EventListeners<T> {
        private final CopyOnWriteArrayList<T> listeners;

        public EventListeners() {
            this.listeners = new CopyOnWriteArrayList<>();
        }

        /**
         * Add listener (creates copy)
         * Time: O(N), Space: O(N)
         *
         * TODO: Implement add listener
         * CopyOnWriteArrayList handles synchronization
         */
        public void addListener(T listener) {
            // TODO: Add to list
            //   listeners.add(listener);
        }

        /**
         * Remove listener (creates copy)
         * Time: O(N), Space: O(N)
         *
         * TODO: Implement remove listener
         */
        public void removeListener(T listener) {
            // TODO: Remove from list
            //   listeners.remove(listener);
        }

        /**
         * Notify all listeners
         * Time: O(N), Space: O(1)
         *
         * Iteration uses snapshot - immune to concurrent modifications
         *
         * TODO: Implement notification
         */
        public void notifyListeners(java.util.function.Consumer<T> action) {
            // TODO: Iterate and apply action
            //   for (T listener : listeners) {
            //     action.accept(listener);
            //   }
        }

        public int size() {
            return listeners.size();
        }
    }

    /**
     * Lock-Free Counter: Using Compare-And-Swap (CAS)
     * No locks - uses CPU atomic instructions
     */
    static class LockFreeCounter {
        private final AtomicLong count;

        public LockFreeCounter() {
            this.count = new AtomicLong(0);
        }

        /**
         * Increment using CAS
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement CAS-based increment
         * 1. Read current value
         * 2. Compute new value
         * 3. CAS: if current unchanged, update to new value
         * 4. If CAS fails (value changed), retry
         */
        public long increment() {
            // TODO: Use getAndIncrement (implements CAS internally)
            //   return count.getAndIncrement();
            return 0; // Replace
        }

        /**
         * Add value using CAS
         * Time: O(1) expected, Space: O(1)
         *
         * TODO: Implement CAS-based add
         */
        public long addAndGet(long delta) {
            // TODO: Use addAndGet
            //   return count.addAndGet(delta);
            return 0; // Replace
        }

        /**
         * Custom CAS operation
         * Time: O(1) expected, Space: O(1)
         *
         * TODO: Implement custom CAS logic
         * Only update if current value is even
         */
        public boolean incrementIfEven() {
            // TODO: Implement CAS loop
            //   while (true) {
            //     long current = count.get();
            //     if (current % 2 != 0) {
            //       return false; // Odd, don't increment
            //     }
            //     long next = current + 1;
            //     if (count.compareAndSet(current, next)) {
            //       return true; // Successfully incremented
            //     }
            //     // CAS failed, retry
            //   }
            return false; // Replace
        }

        public long get() {
            return count.get();
        }
    }

    /**
     * Lock-Free Stack: Using CAS for push/pop
     */
    static class LockFreeStack<T> {
        private static class Node<T> {
            final T value;
            Node<T> next;

            Node(T value) {
                this.value = value;
            }
        }

        private final AtomicReference<Node<T>> head;

        public LockFreeStack() {
            this.head = new AtomicReference<>(null);
        }

        /**
         * Push element using CAS
         * Time: O(1) expected, Space: O(1)
         *
         * TODO: Implement lock-free push
         * 1. Create new node
         * 2. Set its next to current head
         * 3. CAS head to new node
         * 4. If CAS fails, retry
         */
        public void push(T value) {
            // TODO: Implement CAS-based push
            //   Node<T> newNode = new Node<>(value);
            //   while (true) {
            //     Node<T> current = head.get();
            //     newNode.next = current;
            //     if (head.compareAndSet(current, newNode)) {
            //       return;
            //     }
            //     // CAS failed, retry
            //   }
        }

        /**
         * Pop element using CAS
         * Time: O(1) expected, Space: O(1)
         *
         * TODO: Implement lock-free pop
         * 1. Read current head
         * 2. Get next node
         * 3. CAS head to next
         * 4. If CAS fails, retry
         */
        public T pop() {
            // TODO: Implement CAS-based pop
            //   while (true) {
            //     Node<T> current = head.get();
            //     if (current == null) {
            //       return null; // Empty
            //     }
            //     Node<T> next = current.next;
            //     if (head.compareAndSet(current, next)) {
            //       return current.value;
            //     }
            //     // CAS failed, retry
            //   }
            return null; // Replace
        }

        public boolean isEmpty() {
            return head.get() == null;
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.concurrent.*;

public class ThreadSafeDataStructuresClient {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread-Safe Data Structures ===\n");

        // Test 1: ConcurrentHashMap cache
        System.out.println("--- Test 1: ConcurrentHashMap Cache ---");
        testConcurrentCache();

        // Test 2: CopyOnWriteArrayList
        System.out.println("\n--- Test 2: Copy-On-Write List ---");
        testCopyOnWriteList();

        // Test 3: Lock-free counter
        System.out.println("\n--- Test 3: Lock-Free Counter ---");
        testLockFreeCounter();

        // Test 4: Lock-free stack
        System.out.println("\n--- Test 4: Lock-Free Stack ---");
        testLockFreeStack();
    }

    static void testConcurrentCache() throws InterruptedException {
        ThreadSafeDataStructures.ConcurrentCache<String, Integer> cache =
            new ThreadSafeDataStructures.ConcurrentCache<>();

        // Populate cache
        for (int i = 0; i < 100; i++) {
            cache.putIfAbsent("key" + i, i);
        }

        // Concurrent readers
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    int key = ThreadLocalRandom.current().nextInt(150);
                    cache.get("key" + key);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Cache size: " + cache.size());
        System.out.println("Hit rate: " + String.format("%.2f%%", cache.getHitRate() * 100));
    }

    static void testCopyOnWriteList() throws InterruptedException {
        ThreadSafeDataStructures.EventListeners<String> listeners =
            new ThreadSafeDataStructures.EventListeners<>();

        // Add initial listeners
        listeners.addListener("Listener1");
        listeners.addListener("Listener2");
        listeners.addListener("Listener3");

        // Concurrent notifications and modifications
        Thread notifier = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                listeners.notifyListeners(listener -> {
                    System.out.println("Notifying: " + listener);
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread modifier = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                listeners.addListener("NewListener" + i);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        notifier.start();
        modifier.start();
        notifier.join();
        modifier.join();

        System.out.println("Final listener count: " + listeners.size());
    }

    static void testLockFreeCounter() throws InterruptedException {
        ThreadSafeDataStructures.LockFreeCounter counter =
            new ThreadSafeDataStructures.LockFreeCounter();

        int numThreads = 10;
        int incrementsPerThread = 10000;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        long expected = (long) numThreads * incrementsPerThread;
        long actual = counter.get();
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
        System.out.println("Correct: " + (expected == actual));

        // Test incrementIfEven
        ThreadSafeDataStructures.LockFreeCounter counter2 =
            new ThreadSafeDataStructures.LockFreeCounter();
        System.out.println("\nTesting incrementIfEven:");
        System.out.println("Value: " + counter2.get() + ", incrementIfEven: " + counter2.incrementIfEven());
        System.out.println("Value: " + counter2.get() + ", incrementIfEven: " + counter2.incrementIfEven());
        System.out.println("Value: " + counter2.get() + ", incrementIfEven: " + counter2.incrementIfEven());
    }

    static void testLockFreeStack() throws InterruptedException {
        ThreadSafeDataStructures.LockFreeStack<Integer> stack =
            new ThreadSafeDataStructures.LockFreeStack<>();

        // Concurrent pushes
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    stack.push(threadId * 1000 + j);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Pop all elements
        System.out.println("Popping first 10 elements:");
        for (int i = 0; i < 10; i++) {
            System.out.println("  " + stack.pop());
        }

        int count = 10;
        while (!stack.isEmpty()) {
            stack.pop();
            count++;
        }
        System.out.println("Total elements popped: " + count);
    }
}
```

---

### Pattern 4: Thread Pools

**Concept:** Reusing threads from a pool instead of creating new threads for each task.

**Use case:** Web servers, background job processing, parallel computations.

```java
import java.util.concurrent.*;
import java.util.*;

/**
 * Thread Pool Patterns
 *
 * Benefits:
 * - Thread reuse (avoid creation/destruction overhead)
 * - Bounded resources (limit concurrent threads)
 * - Task queue management
 * - Lifecycle management (shutdown, termination)
 */
public class ThreadPoolPatterns {

    /**
     * Basic thread pool usage
     */
    static class BasicThreadPool {

        /**
         * Create fixed thread pool
         * Time: O(1), Space: O(N) where N = pool size
         *
         * TODO: Demonstrate different pool types
         */
        public static void demonstratePoolTypes() {
            // TODO: Create different pool types

            // Fixed thread pool: N worker threads, unbounded queue
            //   ExecutorService fixed = Executors.newFixedThreadPool(4);

            // Cached thread pool: Creates threads as needed, reuses idle ones
            //   ExecutorService cached = Executors.newCachedThreadPool();

            // Single thread executor: Only one worker thread
            //   ExecutorService single = Executors.newSingleThreadExecutor();

            // Scheduled thread pool: For delayed/periodic tasks
            //   ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);

            System.out.println("Pool types demonstrated");
        }

        /**
         * Submit tasks and handle results
         * Time: O(1) per task, Space: O(1)
         *
         * TODO: Implement task submission
         */
        public static void submitTasks() throws InterruptedException, ExecutionException {
            ExecutorService executor = Executors.newFixedThreadPool(4);

            // TODO: Submit Callable (returns result)
            //   Future<Integer> future = executor.submit(() -> {
            //     Thread.sleep(100);
            //     return 42;
            //   });

            // TODO: Get result (blocks until complete)
            //   Integer result = future.get();
            //   System.out.println("Result: " + result);

            // TODO: Submit Runnable (no result)
            //   executor.submit(() -> {
            //     System.out.println("Task executed");
            //   });

            // TODO: Shutdown
            //   executor.shutdown();
            //   executor.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

    /**
     * Custom ThreadPoolExecutor: Full control over pool behavior
     */
    static class CustomThreadPool {
        private final ThreadPoolExecutor executor;

        /**
         * Create custom thread pool
         *
         * TODO: Configure ThreadPoolExecutor
         * Parameters:
         * - corePoolSize: Min threads to keep alive
         * - maximumPoolSize: Max threads allowed
         * - keepAliveTime: How long excess idle threads wait
         * - workQueue: Queue for tasks before execution
         * - rejectedExecutionHandler: What to do when queue full
         */
        public CustomThreadPool(int corePoolSize, int maxPoolSize, int queueSize) {
            // TODO: Create ThreadPoolExecutor
            //   this.executor = new ThreadPoolExecutor(
            //     corePoolSize,
            //     maxPoolSize,
            //     60L, TimeUnit.SECONDS,
            //     new ArrayBlockingQueue<>(queueSize),
            //     new ThreadPoolExecutor.CallerRunsPolicy() // Rejection policy
            //   );

            this.executor = null; // Replace
        }

        /**
         * Submit task
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement task submission
         */
        public Future<?> submit(Runnable task) {
            // TODO: Submit task
            //   return executor.submit(task);
            return null; // Replace
        }

        /**
         * Get pool statistics
         *
         * TODO: Implement statistics gathering
         */
        public void printStats() {
            // TODO: Print executor statistics
            //   System.out.println("Pool Size: " + executor.getPoolSize());
            //   System.out.println("Active Threads: " + executor.getActiveCount());
            //   System.out.println("Completed Tasks: " + executor.getCompletedTaskCount());
            //   System.out.println("Queue Size: " + executor.getQueue().size());
        }

        /**
         * Shutdown pool
         *
         * TODO: Implement graceful shutdown
         * 1. Shutdown (no new tasks)
         * 2. Wait for completion
         * 3. Force shutdown if timeout
         */
        public void shutdown() throws InterruptedException {
            // TODO: Graceful shutdown
            //   executor.shutdown();
            //
            //   if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            //     // Timeout - force shutdown
            //     executor.shutdownNow();
            //
            //     if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            //       System.err.println("Pool did not terminate");
            //     }
            //   }
        }
    }

    /**
     * Work stealing pool: For recursive parallel tasks
     * Uses fork-join framework
     */
    static class WorkStealingPool {

        /**
         * Parallel sum using fork-join
         * Time: O(N) work, O(log N) span, Space: O(log N)
         *
         * TODO: Implement fork-join task
         */
        static class SumTask extends RecursiveTask<Long> {
            private final int[] array;
            private final int start;
            private final int end;
            private static final int THRESHOLD = 1000; // Sequential threshold

            public SumTask(int[] array, int start, int end) {
                this.array = array;
                this.start = start;
                this.end = end;
            }

            @Override
            protected Long compute() {
                // TODO: Implement fork-join logic
                //   if (end - start <= THRESHOLD) {
                //     // Small enough - compute directly
                //     long sum = 0;
                //     for (int i = start; i < end; i++) {
                //       sum += array[i];
                //     }
                //     return sum;
                //   }
                //
                //   // Split task
                //   int mid = start + (end - start) / 2;
                //   SumTask leftTask = new SumTask(array, start, mid);
                //   SumTask rightTask = new SumTask(array, mid, end);
                //
                //   leftTask.fork(); // Async execute
                //   long rightResult = rightTask.compute(); // Sync compute
                //   long leftResult = leftTask.join(); // Wait for result
                //
                //   return leftResult + rightResult;

                return 0L; // Replace
            }
        }

        public static long parallelSum(int[] array) {
            // TODO: Execute fork-join task
            //   ForkJoinPool pool = new ForkJoinPool();
            //   SumTask task = new SumTask(array, 0, array.length);
            //   return pool.invoke(task);
            return 0L; // Replace
        }
    }

    /**
     * Scheduled tasks: Delayed and periodic execution
     */
    static class ScheduledTasks {

        /**
         * Schedule tasks
         *
         * TODO: Demonstrate scheduled execution
         */
        public static void demonstrateScheduled() {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

            // TODO: Schedule one-time delayed task
            //   scheduler.schedule(() -> {
            //     System.out.println("Executed after 1 second");
            //   }, 1, TimeUnit.SECONDS);

            // TODO: Schedule periodic task (fixed rate)
            //   scheduler.scheduleAtFixedRate(() -> {
            //     System.out.println("Periodic task (fixed rate)");
            //   }, 0, 2, TimeUnit.SECONDS);

            // TODO: Schedule periodic task (fixed delay)
            //   scheduler.scheduleWithFixedDelay(() -> {
            //     System.out.println("Periodic task (fixed delay)");
            //   }, 0, 2, TimeUnit.SECONDS);

            System.out.println("Scheduled tasks started");
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.concurrent.*;
import java.util.*;

public class ThreadPoolPatternsClient {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Pool Patterns ===\n");

        // Test 1: Basic pool types
        System.out.println("--- Test 1: Pool Types ---");
        testPoolTypes();

        Thread.sleep(1000);

        // Test 2: Custom thread pool
        System.out.println("\n--- Test 2: Custom Thread Pool ---");
        testCustomThreadPool();

        Thread.sleep(1000);

        // Test 3: Work stealing (fork-join)
        System.out.println("\n--- Test 3: Work Stealing Pool ---");
        testWorkStealingPool();

        Thread.sleep(1000);

        // Test 4: Scheduled tasks
        System.out.println("\n--- Test 4: Scheduled Tasks ---");
        testScheduledTasks();
    }

    static void testPoolTypes() throws InterruptedException {
        // Fixed pool
        ExecutorService fixed = Executors.newFixedThreadPool(4);
        System.out.println("Fixed pool created with 4 threads");

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            fixed.submit(() -> {
                System.out.println("Fixed pool task " + taskId + " on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        fixed.shutdown();
        fixed.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Fixed pool completed");

        // Cached pool
        ExecutorService cached = Executors.newCachedThreadPool();
        System.out.println("\nCached pool created");

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            cached.submit(() -> {
                System.out.println("Cached pool task " + taskId + " on " + Thread.currentThread().getName());
            });
        }

        cached.shutdown();
        cached.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Cached pool completed");
    }

    static void testCustomThreadPool() throws InterruptedException {
        ThreadPoolPatterns.CustomThreadPool pool = new ThreadPoolPatterns.CustomThreadPool(2, 4, 10);

        // Submit many tasks
        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("Task " + taskId + " executing");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        Thread.sleep(1000);
        pool.printStats();

        pool.shutdown();
        System.out.println("Custom pool shutdown");
    }

    static void testWorkStealingPool() {
        int[] array = new int[100000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        // Sequential sum
        long start = System.nanoTime();
        long sequentialSum = 0;
        for (int val : array) {
            sequentialSum += val;
        }
        long sequentialTime = System.nanoTime() - start;

        // Parallel sum
        start = System.nanoTime();
        long parallelSum = ThreadPoolPatterns.WorkStealingPool.parallelSum(array);
        long parallelTime = System.nanoTime() - start;

        System.out.println("Sequential sum: " + sequentialSum + " (" + sequentialTime/1000 + " μs)");
        System.out.println("Parallel sum: " + parallelSum + " (" + parallelTime/1000 + " μs)");
        System.out.println("Speedup: " + String.format("%.2fx", (double)sequentialTime/parallelTime));
    }

    static void testScheduledTasks() throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        System.out.println("Scheduling tasks...");

        // Delayed task
        scheduler.schedule(() -> {
            System.out.println("[" + System.currentTimeMillis() + "] Delayed task executed");
        }, 1, TimeUnit.SECONDS);

        // Periodic task (fixed rate)
        ScheduledFuture<?> periodicTask = scheduler.scheduleAtFixedRate(() -> {
            System.out.println("[" + System.currentTimeMillis() + "] Periodic task (every 500ms)");
        }, 0, 500, TimeUnit.MILLISECONDS);

        // Let it run for 3 seconds
        Thread.sleep(3000);

        // Cancel periodic task
        periodicTask.cancel(false);
        scheduler.shutdown();
        scheduler.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Scheduled tasks completed");
    }
}
```

---

## Decision Framework

**Your task:** Build decision trees for when to use each concurrency pattern.

### Question 1: When to use locks vs lock-free algorithms?

Answer after implementation:

**Use Locks when:**
- Complex operations: _[Multiple steps that must be atomic]_
- Simple reasoning: _[Lock-based code is easier to understand]_
- Fairness needed: _[Locks can guarantee FIFO ordering]_
- Most use cases: _[Locks are sufficient for 90% of scenarios]_

**Use Lock-Free when:**
- High contention: _[Many threads competing for same resource]_
- Low-latency critical: _[Cannot afford lock overhead]_
- Progress guarantees: _[At least one thread makes progress]_
- Simple operations: _[Increment, swap, stack push/pop]_

### Question 2: Which thread pool to use?

**FixedThreadPool when:**
- Known workload: _[Consistent number of tasks]_
- Resource limiting: _[Don't want unbounded thread creation]_
- CPU-bound tasks: _[Pool size = CPU cores]_

**CachedThreadPool when:**
- Unpredictable load: _[Varying number of tasks]_
- I/O-bound tasks: _[Threads spend time waiting]_
- Short-lived tasks: _[Quick execution, many tasks]_

**ScheduledThreadPool when:**
- Delayed execution: _[Run after delay]_
- Periodic tasks: _[Cron-like scheduling]_
- Background jobs: _[Cleanup, monitoring, etc.]_

**ForkJoinPool when:**
- Recursive tasks: _[Divide-and-conquer algorithms]_
- Parallel algorithms: _[Parallel sort, sum, map-reduce]_
- Work stealing: _[Balance load across threads]_

### Question 3: Synchronized vs ReentrantLock?

**Synchronized when:**
- Simple use case: _[Just need mutual exclusion]_
- Less code: _[synchronized(this) { ... }]_
- No advanced features needed: _[Try-lock, interruptible, timeouts not needed]_

**ReentrantLock when:**
- Try-lock needed: _[Attempt lock without blocking]_
- Timeouts: _[Give up after waiting]_
- Interruptible: _[Can interrupt waiting thread]_
- Fairness: _[FIFO lock acquisition]_
- Condition variables: _[Complex waiting conditions]_

### Your Decision Tree

Build this after solving practice scenarios:

```
Concurrency Pattern Selection
│
├─ What's the workload?
│   ├─ Many independent tasks → Use thread pool
│   ├─ Shared mutable state → Need synchronization
│   └─ Message passing → Use BlockingQueue
│
├─ Need synchronization?
│   ├─ Simple critical section → synchronized
│   ├─ Advanced features → ReentrantLock
│   ├─ Read-heavy → ReadWriteLock
│   └─ High contention → Lock-free (Atomic)
│
├─ Thread pool sizing?
│   ├─ CPU-bound → cores + 1
│   ├─ I/O-bound → cores * 2 or more
│   └─ Mixed → Test and measure
│
└─ Queue sizing?
    ├─ Bounded → Prevent memory exhaustion
    ├─ Unbounded → Risk OOM, but never blocks producers
    └─ SynchronousQueue → Direct handoff, no buffering
```

### The "Kill Switch" - Concurrency Anti-Patterns

**Don't do this:**
1. **Creating threads manually** - _[Use thread pools instead]_
2. **Synchronized on String literals** - _[Strings are interned, global lock]_
3. **Nested locks without ordering** - _[Causes deadlocks]_
4. **Catching InterruptedException and ignoring** - _[Prevents proper shutdown]_
5. **Using Thread.stop()** - _[Unsafe, deprecated, corrupts state]_
6. **Unbounded thread creation** - _[OOM, CPU thrashing]_
7. **Busy waiting (while loops)** - _[Wastes CPU, use wait/notify or BlockingQueue]_

### The Rule of Three: Alternatives

**Option 1: Locks (ReentrantLock)**
- Pros: _[Simple mental model, guaranteed mutual exclusion, fair ordering]_
- Cons: _[Performance overhead, can deadlock, blocking]_
- Use when: _[Complex critical sections, most use cases]_

**Option 2: Lock-Free (Atomic/CAS)**
- Pros: _[No blocking, high performance under contention, progress guarantee]_
- Cons: _[Complex to implement, limited to simple operations, ABA problem]_
- Use when: _[High contention, simple operations like counters]_

**Option 3: Immutable + Message Passing**
- Pros: _[No synchronization needed, easy to reason about, functional style]_
- Cons: _[Memory overhead (copying), not always applicable]_
- Use when: _[Can afford immutability, actor model, functional programming]_

---

## Practice

### Scenario 1: Web Server Request Handler

**Requirements:**
- Handle 10,000 concurrent HTTP requests
- Each request: parse, validate, database query, response
- Database pool: 20 connections max
- CPU cores: 8
- Must handle bursts (spike to 50K requests)

**Your design:**

Thread pool configuration:
- Pool type: _[Fixed, Cached, or Custom?]_
- Core threads: _[How many?]_
- Max threads: _[How many?]_
- Queue size: _[Bounded or unbounded?]_
- Rejection policy: _[What happens when queue full?]_

Reasoning:
1. _[Why this pool type?]_
2. _[How did you calculate thread counts?]_
3. _[How does it handle bursts?]_
4. _[What about database connection pool coordination?]_

### Scenario 2: Real-Time Analytics Pipeline

**Requirements:**
- Ingest 1M events/second
- Processing stages: validate → enrich → aggregate → store
- Each stage is CPU-intensive (10ms per event)
- Must maintain order within same user_id
- Latency target: p99 < 100ms

**Your design:**

Pipeline architecture:
- Number of stages: _[How many?]_
- Queue between stages: _[Bounded or unbounded?]_
- Threads per stage: _[How many?]_
- Backpressure handling: _[What to do when backed up?]_

Ordering guarantee:
- How to maintain order per user_id: _[Your approach]_
- Trade-off: _[Ordering vs throughput]_

### Scenario 3: Distributed Cache

**Requirements:**
- In-memory cache with 1M entries
- Operations: get (90%), put (9%), delete (1%)
- Concurrent access: 1000 threads
- Must track hit rate, eviction stats
- LRU eviction policy

**Your design:**

Data structure:
- Base structure: _[ConcurrentHashMap? Why?]_
- Synchronization: _[Where do you need locks?]_
- Statistics tracking: _[Atomic variables?]_

LRU implementation:
- How to track access order: _[Your approach]_
- Thread-safe eviction: _[How to handle concurrent evictions?]_
- Read-write coordination: _[ReadWriteLock? Why or why not?]_

Trade-offs:
1. _[Accuracy vs performance]_
2. _[Lock-free vs locked]_
3. _[Memory overhead]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] ReentrantLock counter works correctly
  - [ ] ReadWriteLock cache works correctly
  - [ ] Bank transfer with lock ordering prevents deadlocks
  - [ ] Producer-consumer with BlockingQueue works
  - [ ] ConcurrentHashMap cache operations work
  - [ ] Lock-free counter with CAS works
  - [ ] Thread pool executors work correctly
  - [ ] All client code runs successfully

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Understand difference between synchronized and ReentrantLock
  - [ ] Understand read-write lock semantics
  - [ ] Understand why lock ordering prevents deadlocks
  - [ ] Understand BlockingQueue blocking behavior
  - [ ] Understand CAS and ABA problem
  - [ ] Understand thread pool sizing principles

- [ ] **Concurrency Principles**
  - [ ] Always release locks in finally blocks
  - [ ] Lock ordering prevents deadlocks
  - [ ] Use bounded queues to prevent OOM
  - [ ] Properly handle InterruptedException
  - [ ] Shutdown thread pools gracefully
  - [ ] Prefer higher-level abstractions (BlockingQueue, Atomic)
  - [ ] Measure contention before optimizing

- [ ] **Decision Making**
  - [ ] Know when to use locks vs lock-free
  - [ ] Know how to size thread pools
  - [ ] Completed practice scenarios
  - [ ] Can explain synchronization trade-offs

- [ ] **Mastery Check**
  - [ ] Could implement thread-safe cache from memory
  - [ ] Could design thread pool configuration for given workload
  - [ ] Understand concurrency bugs (deadlock, race conditions, starvation)
  - [ ] Know how to debug concurrency issues

---

**Next:** [09. Database Scaling →](09-database-scaling.md)

**Back:** [07. Load Balancing ←](07-load-balancing.md)
