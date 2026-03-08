package com.study.systems.concurrency;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

class ThreadSafeDataStructuresTest {

    // -------------------------------------------------------------------------
    // ConcurrentCache
    // -------------------------------------------------------------------------

    @Test
    void testPutIfAbsentStoresValue() {
        ThreadSafeDataStructures.ConcurrentCache<String, Integer> cache =
                new ThreadSafeDataStructures.ConcurrentCache<>();
        cache.putIfAbsent("k", 1);
        assertEquals(1, cache.get("k"));
    }

    @Test
    void testPutIfAbsentDoesNotOverwrite() {
        ThreadSafeDataStructures.ConcurrentCache<String, Integer> cache =
                new ThreadSafeDataStructures.ConcurrentCache<>();
        cache.putIfAbsent("k", 1);
        cache.putIfAbsent("k", 99);
        assertEquals(1, cache.get("k"));
    }

    @Test
    void testComputeIfAbsentComputes() {
        ThreadSafeDataStructures.ConcurrentCache<String, Integer> cache =
                new ThreadSafeDataStructures.ConcurrentCache<>();
        cache.computeIfAbsent("k", key -> 42);
        assertEquals(42, cache.get("k"));
    }

    @Test
    void testHitRateTracking() {
        ThreadSafeDataStructures.ConcurrentCache<String, Integer> cache =
                new ThreadSafeDataStructures.ConcurrentCache<>();
        for (int i = 0; i < 10; i++) {
            cache.putIfAbsent("key" + i, i);
        }
        for (int i = 0; i < 10; i++) {
            cache.get("key" + i);
        }
        assertEquals(1.0, cache.getHitRate(), 0.0001);
    }

    @Test
    void testConcurrentPutsAllSucceed() {
        ThreadSafeDataStructures.ConcurrentCache<String, Integer> cache =
                new ThreadSafeDataStructures.ConcurrentCache<>();

        // 10 threads, each inserting 10 unique keys (100 total)
        ExecutorService exec = Executors.newFixedThreadPool(10);
        for (int t = 0; t < 10; t++) {
            final int threadId = t;
            exec.submit(() -> {
                for (int k = 0; k < 10; k++) {
                    cache.putIfAbsent("thread" + threadId + "-key" + k, threadId * 10 + k);
                }
            });
        }
        exec.shutdown();

        await().atMost(500, MILLISECONDS).untilAsserted(() ->
                assertEquals(100, cache.size()));
    }

    // -------------------------------------------------------------------------
    // EventListeners
    // -------------------------------------------------------------------------

    @Test
    void testAddListenerIncreasesSize() {
        ThreadSafeDataStructures.EventListeners<String> listeners =
                new ThreadSafeDataStructures.EventListeners<>();
        listeners.addListener("L1");
        assertEquals(1, listeners.size());
    }

    @Test
    void testRemoveListenerDecreasesSize() {
        ThreadSafeDataStructures.EventListeners<String> listeners =
                new ThreadSafeDataStructures.EventListeners<>();
        listeners.addListener("L1");
        listeners.removeListener("L1");
        assertEquals(0, listeners.size());
    }

    @Test
    void testNotifyListenersCallsAction() {
        ThreadSafeDataStructures.EventListeners<String> listeners =
                new ThreadSafeDataStructures.EventListeners<>();
        listeners.addListener("L1");

        List<String> received = new ArrayList<>();
        listeners.notifyListeners(received::add);

        assertTrue(received.contains("L1"));
    }

    @Test
    void testConcurrentAddAndNotify() {
        ThreadSafeDataStructures.EventListeners<String> listeners =
                new ThreadSafeDataStructures.EventListeners<>();

        CountDownLatch latch = new CountDownLatch(6); // 5 adders + 1 notifier
        ExecutorService exec = Executors.newFixedThreadPool(6);

        for (int i = 0; i < 5; i++) {
            final int id = i;
            exec.submit(() -> {
                listeners.addListener("Listener" + id);
                latch.countDown();
            });
        }

        exec.submit(() -> {
            listeners.notifyListeners(l -> { /* no-op */ });
            latch.countDown();
        });

        exec.shutdown();

        await().atMost(200, MILLISECONDS).until(() -> latch.getCount() == 0);
    }

    // -------------------------------------------------------------------------
    // LockFreeCounter
    // -------------------------------------------------------------------------

    @Test
    void testIncrementReturnsNewValue() {
        ThreadSafeDataStructures.LockFreeCounter counter =
                new ThreadSafeDataStructures.LockFreeCounter();
        // increment() wraps getAndIncrement() — the counter value after first call is 1
        counter.increment();
        assertEquals(1L, counter.get());
    }

    @Test
    void testConcurrentIncrementsAreAccurate() {
        ThreadSafeDataStructures.LockFreeCounter counter =
                new ThreadSafeDataStructures.LockFreeCounter();
        int numThreads = 10;
        int incrementsPerThread = 10000;

        ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            exec.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }
        exec.shutdown();

        await().atMost(500, MILLISECONDS).untilAsserted(() ->
                assertEquals(100000L, counter.get()));
    }

    @Test
    void testIncrementIfEvenWorksOnEven() {
        ThreadSafeDataStructures.LockFreeCounter counter =
                new ThreadSafeDataStructures.LockFreeCounter();
        // Initial value is 0, which is even
        boolean result = counter.incrementIfEven();
        assertTrue(result);
        assertEquals(1L, counter.get());
    }

    @Test
    void testIncrementIfEvenFailsOnOdd() {
        ThreadSafeDataStructures.LockFreeCounter counter =
                new ThreadSafeDataStructures.LockFreeCounter();
        counter.increment(); // value is now 1 (odd)
        boolean result = counter.incrementIfEven();
        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // LockFreeStack
    // -------------------------------------------------------------------------

    @Test
    void testIsEmptyOnNewStack() {
        ThreadSafeDataStructures.LockFreeStack<Integer> stack =
                new ThreadSafeDataStructures.LockFreeStack<>();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testIsNotEmptyAfterPush() {
        ThreadSafeDataStructures.LockFreeStack<Integer> stack =
                new ThreadSafeDataStructures.LockFreeStack<>();
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    void testPushAndPopReturnsValues() {
        ThreadSafeDataStructures.LockFreeStack<Integer> stack =
                new ThreadSafeDataStructures.LockFreeStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        // LIFO: last pushed is first popped
        assertEquals(3, stack.pop());
    }

    @Test
    void testConcurrentPushesAllSucceed() {
        ThreadSafeDataStructures.LockFreeStack<Integer> stack =
                new ThreadSafeDataStructures.LockFreeStack<>();
        int numThreads = 5;
        int pushesPerThread = 100;

        ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            exec.submit(() -> {
                for (int j = 0; j < pushesPerThread; j++) {
                    stack.push(threadId * 1000 + j);
                }
            });
        }
        exec.shutdown();

        AtomicInteger popped = new AtomicInteger(0);
        await().atMost(500, MILLISECONDS).untilAsserted(() -> {
            while (!stack.isEmpty()) {
                stack.pop();
                popped.incrementAndGet();
            }
            assertEquals(500, popped.get());
        });
    }
}
