package com.study.systems.concurrency;

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
        }

        /**
         * Get current count
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement thread-safe read
         */
        public int getCount() {
            // TODO: Acquire lock for reading
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
        }

        /**
         * Clear cache (write operation)
         * Time: O(1), Space: O(1)
         *
         * TODO: Implement clear with write lock
         */
        public void clear() {
            // TODO: Use write lock to clear
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

            // TODO: Acquire locks in order

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
