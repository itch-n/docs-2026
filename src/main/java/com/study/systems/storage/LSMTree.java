package com.study.systems.storage;

import java.util.*;

import static java.util.Comparator.comparingLong;

/**
 * LSM Tree: Log-Structured Merge Tree optimized for writes
 * <p>
 * Architecture:
 * - Writes go to in-memory MemTable (sorted)
 * - When MemTable full, flush to SSTable (immutable file)
 * - Reads check MemTable, then SSTables (newest first)
 * - Periodically compact SSTables (merge and remove duplicates)
 */
public class LSMTree<K extends Comparable<K>, V> {

    private final int memTableSize; // Max entries before flush
    private TreeMap<K, V> memTable = new TreeMap<>(); // In-memory sorted map
    private List<SSTable<K, V>> sstables = new ArrayList<>(); // On-disk sorted tables
    private DiskSimulator disk = new DiskSimulator();

    /**
     * SSTable: Sorted String Table (immutable)
     * In production: stored on disk
     * Here: simplified in-memory representation
     */
    static class SSTable<K extends Comparable<K>, V> {
        private final SortedMap<K, V> data;
        private final long timestamp; // When created (for ordering)

        SSTable(SortedMap<K, V> data) {
            this.data = Collections.unmodifiableSortedMap(new TreeMap<>(data));
            this.timestamp = System.currentTimeMillis();
        }

        V get(K key) {
            return data.get(key);
        }

        boolean containsKey(K key) {
            return data.containsKey(key);
        }

        Set<Map.Entry<K, V>> entrySet() {
            return data.entrySet();
        }

        int size() {
            return data.size();
        }
    }

    public LSMTree(int memTableSize) {
        this.memTableSize = memTableSize;
    }

    /**
     * Insert/Update key-value pair
     * Time: O(log M) where M = memTable size
     * <p>
     * 1. Insert into MemTable
     * 2. If MemTable full, flush to SSTable
     */
    public void put(K key, V value) {
        memTable.put(key, value);

        if (memTable.size() > memTableSize) {
            flush();
        }
    }

    /**
     * Retrieve value for key
     * Time: O(log M + N*log S) where N = number of SSTables, S = SSTable size
     * <p>
     * 1. Check MemTable first (most recent)
     * 2. Check SSTables in reverse order (newest first)
     * 3. Return first match
     */
    public V get(K key) {
        // Check memTable first (in-memory, no I/O)
        if (memTable.containsKey(key)) {
            return memTable.get(key);
        }

        // Check SSTables from newest to oldest (random reads)
        for (int i = sstables.size() - 1; i >= 0; i--) {
            disk.randomRead();  // Read SSTable from disk
            SSTable<K, V> table = sstables.get(i);
            if (table.containsKey(key)) {
                return table.get(key);
            }
        }

        return null; // Not found
    }

    /**
     * Flush MemTable to SSTable (simulate disk write)
     */
    private void flush() {
        // Sequential write of entire MemTable to disk
        for (int i = 0; i < memTable.size() / 10; i++) {  // Batch writes
            disk.sequentialWrite();
        }

        sstables.add(new SSTable<>(memTable));
        memTable.clear();

        System.out.println("Flushed MemTable to SSTable. Total SSTables: " + sstables.size());
    }

    /**
     * Compact SSTables: Merge multiple tables, remove duplicates
     * Time: O(N * S * log S) where N = tables, S = size
     * <p>
     * 1. Merge all SSTables
     * 2. For duplicate keys, keep newest value
     * 3. Create new compacted SSTable
     */
    public void compact() {
        int beforeSize = sstables.size();
        if (beforeSize <= 1) {
            return; // Nothing to compact
        }

        // Read all SSTables sequentially
        for (SSTable<K, V> table : sstables) {
            for (int i = 0; i < table.size() / 10; i++) {  // Batch reads
                disk.sequentialRead();
            }
        }

        // Iterate through SSTables from oldest to newest
        // Later values overwrite earlier ones (keep newest)
        TreeMap<K, V> merged = new TreeMap<>();
        sstables.stream()
                .sorted(comparingLong(sst -> sst.timestamp))
                .forEach(sst -> merged.putAll(sst.data));

        // Write compacted SSTable sequentially
        for (int i = 0; i < merged.size() / 10; i++) {  // Batch writes
            disk.sequentialWrite();
        }

        // Replace old SSTables with compacted one
        sstables.clear();
        sstables.add(new SSTable<>(merged));

        System.out.println("Compacted " + beforeSize + " SSTables into 1");
    }

    /**
     * Print current state
     */
    public void printState() {
        System.out.println("MemTable size: " + memTable.size());
        System.out.println("SSTables: " + sstables.size());
        for (int i = 0; i < sstables.size(); i++) {
            System.out.println("  SSTable " + i + ": " + sstables.get(i).size() + " entries");
        }
    }

    public static void main(String[] args) {
        // Create LSM Tree with memTable size = 5
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        System.out.println("=== LSM Tree Demo ===\n");

        // Test 1: Sequential writes (triggers flush)
        System.out.println("--- Test 1: Sequential Writes ---");
        for (int i = 1; i <= 12; i++) {
            lsm.put(i, "Value" + i);
            System.out.println("Put(" + i + ", Value" + i + ")");
        }

        System.out.println("\nState after 12 inserts:");
        lsm.printState();

        // Test 2: Read values
        System.out.println("\n--- Test 2: Reads ---");
        System.out.println("Get(5): " + lsm.get(5));   // In SSTable
        System.out.println("Get(11): " + lsm.get(11)); // In MemTable
        System.out.println("Get(100): " + lsm.get(100)); // Not found

        // Test 3: Update existing keys
        System.out.println("\n--- Test 3: Updates ---");
        lsm.put(5, "UpdatedValue5");
        lsm.put(11, "UpdatedValue11");

        System.out.println("Get(5) after update: " + lsm.get(5));
        System.out.println("Get(11) after update: " + lsm.get(11));

        // Test 4: Trigger more flushes
        System.out.println("\n--- Test 4: More Writes ---");
        for (int i = 20; i <= 35; i++) {
            lsm.put(i, "Value" + i);
        }

        lsm.printState();

        // Test 5: Compaction
        System.out.println("\n--- Test 5: Compaction ---");
        lsm.printState();
        lsm.compact();
        lsm.printState();

        // Test 6: Verify reads after compaction
        System.out.println("\n--- Test 6: Verify After Compaction ---");
        System.out.println("Get(5): " + lsm.get(5));
        System.out.println("Get(25): " + lsm.get(25));
    }
}