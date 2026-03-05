package com.study.systems.storage;

/**
 * Simulates SSD I/O latency to demonstrate real-world performance characteristics.
 *
 * Typical SSD latencies:
 * - Random read/write: ~100 microseconds (0.1ms)
 * - Sequential read/write: ~50 microseconds (0.05ms)
 */
public class DiskSimulator {

    private static final int RANDOM_OP_US = 100;      // 0.1ms
    private static final int SEQUENTIAL_OP_US = 50;   // 0.05ms

    public void randomRead() {
        sleep(RANDOM_OP_US);
    }

    public void randomWrite() {
        sleep(RANDOM_OP_US);
    }

    public void sequentialRead() {
        sleep(SEQUENTIAL_OP_US);
    }

    public void sequentialWrite() {
        sleep(SEQUENTIAL_OP_US);
    }

    private void sleep(int microseconds) {
        try {
            Thread.sleep(microseconds / 1000, (microseconds % 1000) * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}