package com.study.systems.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolPatternsTest {

    // -------------------------------------------------------------------------
    // CustomThreadPool
    // -------------------------------------------------------------------------

    @Test
    void testSubmitReturnsNonNullFuture() {
        ThreadPoolPatterns.CustomThreadPool pool =
                new ThreadPoolPatterns.CustomThreadPool(2, 4, 10);
        Future<?> future = pool.submit(() -> {});
        assertNotNull(future);
    }

    @Test
    void testSubmittedTaskExecutes() {
        ThreadPoolPatterns.CustomThreadPool pool =
                new ThreadPoolPatterns.CustomThreadPool(2, 4, 10);
        AtomicBoolean flag = new AtomicBoolean(false);

        pool.submit(() -> flag.set(true));

        await().atMost(2, SECONDS).until(flag::get);
    }

    @Test
    void testMultipleTasksAllExecute() throws InterruptedException {
        ThreadPoolPatterns.CustomThreadPool pool =
                new ThreadPoolPatterns.CustomThreadPool(2, 4, 10);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            pool.submit(latch::countDown);
        }

        await().atMost(5, SECONDS).until(() -> latch.getCount() == 0);

        pool.shutdown();
    }

    @Test
    void testShutdownCompletesGracefully() {
        ThreadPoolPatterns.CustomThreadPool pool =
                new ThreadPoolPatterns.CustomThreadPool(2, 4, 10);
        pool.submit(() -> {});
        pool.submit(() -> {});

        assertDoesNotThrow(pool::shutdown);
    }

    // -------------------------------------------------------------------------
    // WorkStealingPool
    // -------------------------------------------------------------------------

    @Test
    void testParallelSumOfSequentialArray() {
        int[] array = {1, 2, 3, 4, 5};
        long result = ThreadPoolPatterns.WorkStealingPool.parallelSum(array);
        assertEquals(15L, result);
    }

    @Test
    void testParallelSumLargeArray() {
        int[] array = new int[100];
        for (int i = 0; i < 100; i++) {
            array[i] = i + 1; // 1..100
        }
        long result = ThreadPoolPatterns.WorkStealingPool.parallelSum(array);
        assertEquals(5050L, result);
    }

    @Test
    void testParallelSumSingleElement() {
        int[] array = {42};
        long result = ThreadPoolPatterns.WorkStealingPool.parallelSum(array);
        assertEquals(42L, result);
    }

    @Test
    void testParallelSumEmptyArray() {
        int[] array = {};
        long result = ThreadPoolPatterns.WorkStealingPool.parallelSum(array);
        assertEquals(0L, result);
    }
}
