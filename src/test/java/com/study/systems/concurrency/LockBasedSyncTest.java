package com.study.systems.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

class LockBasedSyncTest {

    // -------------------------------------------------------------------------
    // ThreadSafeCounter
    // -------------------------------------------------------------------------

    @Test
    void testInitialCountIsZero() {
        LockBasedSync.ThreadSafeCounter counter = new LockBasedSync.ThreadSafeCounter();
        assertEquals(0, counter.getCount());
    }

    @Test
    void testSingleIncrementWorks() {
        LockBasedSync.ThreadSafeCounter counter = new LockBasedSync.ThreadSafeCounter();
        counter.increment();
        assertEquals(1, counter.getCount());
    }

    @Test
    void testTryIncrementWithSufficientTime() {
        LockBasedSync.ThreadSafeCounter counter = new LockBasedSync.ThreadSafeCounter();
        boolean acquired = counter.tryIncrement(1000);
        assertTrue(acquired);
    }

    @Test
    void testConcurrentIncrementsAreAccurate() {
        LockBasedSync.ThreadSafeCounter counter = new LockBasedSync.ThreadSafeCounter();
        int numThreads = 10;
        int incrementsPerThread = 1000;

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
                assertEquals(10000, counter.getCount()));
    }

    // -------------------------------------------------------------------------
    // ReadWriteCache
    // -------------------------------------------------------------------------

    @Test
    void testPutAndGetReturnsSameValue() {
        LockBasedSync.ReadWriteCache<String, String> cache = new LockBasedSync.ReadWriteCache<>();
        cache.put("k", "v");
        assertEquals("v", cache.get("k"));
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        LockBasedSync.ReadWriteCache<String, String> cache = new LockBasedSync.ReadWriteCache<>();
        assertNull(cache.get("absent"));
    }

    @Test
    void testSizeReflectsInserts() {
        LockBasedSync.ReadWriteCache<String, Integer> cache = new LockBasedSync.ReadWriteCache<>();
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        assertEquals(3, cache.size());
    }

    @Test
    void testClearEmptiesCache() {
        LockBasedSync.ReadWriteCache<String, Integer> cache = new LockBasedSync.ReadWriteCache<>();
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    void testConcurrentReadsDontBlock() {
        LockBasedSync.ReadWriteCache<String, String> cache = new LockBasedSync.ReadWriteCache<>();
        cache.put("shared", "value");

        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            exec.submit(() -> {
                cache.get("shared");
                latch.countDown();
            });
        }
        exec.shutdown();

        await().atMost(200, MILLISECONDS).until(() -> latch.getCount() == 0);
    }

    // -------------------------------------------------------------------------
    // BankAccount
    // -------------------------------------------------------------------------

    @Test
    void testTransferMovesMoneyBetweenAccounts() {
        LockBasedSync.BankAccount acc1 = new LockBasedSync.BankAccount(1, 1000);
        LockBasedSync.BankAccount acc2 = new LockBasedSync.BankAccount(2, 500);

        boolean result = LockBasedSync.BankAccount.transfer(acc1, acc2, 200);

        assertTrue(result);
        assertEquals(800, acc1.getBalance());
        assertEquals(700, acc2.getBalance());
    }

    @Test
    void testTransferFailsIfInsufficientFunds() {
        LockBasedSync.BankAccount acc1 = new LockBasedSync.BankAccount(1, 1000);
        LockBasedSync.BankAccount acc2 = new LockBasedSync.BankAccount(2, 500);

        boolean result = LockBasedSync.BankAccount.transfer(acc1, acc2, 2000);

        assertFalse(result);
        assertEquals(1000, acc1.getBalance());
        assertEquals(500, acc2.getBalance());
    }

    @Test
    void testConcurrentTransfersMaintainTotalBalance() {
        LockBasedSync.BankAccount acc1 = new LockBasedSync.BankAccount(1, 1000);
        LockBasedSync.BankAccount acc2 = new LockBasedSync.BankAccount(2, 1000);

        ExecutorService exec = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            final int threadIndex = i;
            exec.submit(() -> {
                if (threadIndex % 2 == 0) {
                    LockBasedSync.BankAccount.transfer(acc1, acc2, 10);
                } else {
                    LockBasedSync.BankAccount.transfer(acc2, acc1, 10);
                }
            });
        }
        exec.shutdown();

        await().atMost(500, MILLISECONDS).untilAsserted(() ->
                assertEquals(2000, acc1.getBalance() + acc2.getBalance()));
    }
}
