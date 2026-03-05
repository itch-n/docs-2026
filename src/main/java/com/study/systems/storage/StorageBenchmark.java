package com.study.systems.storage;

import java.util.Random;

public class StorageBenchmark {

    public static void main(String[] args) {
        System.out.println("=== Storage Engine Benchmark ===\n");

        benchmarkWrites();
        System.out.println();
        benchmarkReads();
        System.out.println();
        benchmarkMixed();
    }

    static void benchmarkWrites() {
        System.out.println("--- Write Performance (with SSD I/O simulation) ---");
        int numWrites = 10000;

        // B+Tree writes
        BPlusTree<Integer, String> btree = new BPlusTree<>(128);
        long start = System.nanoTime();
        for (int i = 0; i < numWrites; i++) {
            btree.insert(i, "Value" + i);
        }
        long btreeTime = System.nanoTime() - start;

        // LSM Tree writes
        LSMTree<Integer, String> lsm = new LSMTree<>(100);
        start = System.nanoTime();
        for (int i = 0; i < numWrites; i++) {
            lsm.put(i, "Value" + i);
        }
        long lsmTime = System.nanoTime() - start;

        System.out.printf("B+Tree: %.2f ms (%.0f writes/sec)%n", btreeTime / 1e6, numWrites / (btreeTime / 1e9));
        System.out.printf("LSM Tree: %.2f ms (%.0f writes/sec)%n", lsmTime / 1e6, numWrites / (lsmTime / 1e9));
        System.out.printf("LSM is %.2fx faster for writes%n", (double) btreeTime / lsmTime);
    }

    static void benchmarkReads() {
        System.out.println("--- Read Performance (with SSD I/O simulation) ---");
        int numEntries = 10000;
        int numReads = 1000;

        // Setup B+Tree
        BPlusTree<Integer, String> btree = new BPlusTree<>(128);
        for (int i = 0; i < numEntries; i++) {
            btree.insert(i, "Value" + i);
        }

        // Setup LSM Tree
        LSMTree<Integer, String> lsm = new LSMTree<>(100);
        for (int i = 0; i < numEntries; i++) {
            lsm.put(i, "Value" + i);
        }

        // Benchmark reads
        Random rand = new Random(42);

        long start = System.nanoTime();
        for (int i = 0; i < numReads; i++) {
            int key = rand.nextInt(numEntries);
            btree.search(key);
        }
        long btreeTime = System.nanoTime() - start;

        rand = new Random(42); // Same sequence
        start = System.nanoTime();
        for (int i = 0; i < numReads; i++) {
            int key = rand.nextInt(numEntries);
            lsm.get(key);
        }
        long lsmTime = System.nanoTime() - start;

        System.out.printf("B+Tree: %.2f ms (%.0f reads/sec)%n",
                btreeTime / 1e6, numReads / (btreeTime / 1e9));
        System.out.printf("LSM Tree: %.2f ms (%.0f reads/sec)%n",
                lsmTime / 1e6, numReads / (lsmTime / 1e9));
        System.out.printf("B+Tree is %.2fx faster for reads%n",
                (double) lsmTime / btreeTime);
    }

    static void benchmarkMixed() {
        System.out.println("--- Mixed Workload (50% reads, 50% writes) ---");

        int numWrites = 1000;
        int numReads = 1000;

        // Benchmark read-writes
        Random rand = new Random(42);

        BPlusTree<Integer, String> btree = new BPlusTree<>(128);
        long start = System.nanoTime();
        for (int i = 0; i < numWrites; i++) {
            btree.insert(i, "Value" + i);
        }
        for (int i = 0; i < numReads; i++) {
            int key = rand.nextInt(numWrites);
            btree.search(key);
        }
        long btreeTime = System.nanoTime() - start;

        rand = new Random(42); // Same sequence
        LSMTree<Integer, String> lsm = new LSMTree<>(100);
        start = System.nanoTime();
        for (int i = 0; i < numWrites; i++) {
            lsm.put(i, "Value" + i);
        }
        for (int i = 0; i < numReads; i++) {
            int key = rand.nextInt(numWrites);
            lsm.get(key);
        }
        long lsmTime = System.nanoTime() - start;

        System.out.printf("B+Tree: %.2f ms (%.0f reads/sec, %.0f writes/sec)%n",
                btreeTime / 1e6, numReads / (btreeTime / 1e9), numWrites / (btreeTime / 1e9));
        System.out.printf("LSM Tree: %.2f ms (%.0f reads/sec, %.0f writes/sec)%n",
                lsmTime / 1e6, numReads / (lsmTime / 1e9), numWrites / (lsmTime / 1e9));
    }
}
