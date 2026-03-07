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
    private TreeMap<K, V> memTable; // In-memory sorted map
    private List<SSTable<K, V>> sstables; // On-disk sorted tables

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
        this.memTable = new TreeMap<>();
        this.sstables = new ArrayList<>();
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
        // Check memTable first
        if (memTable.containsKey(key)) {
            return memTable.get(key);
        }

        // Check SSTables from newest to oldest
        for (int i = sstables.size() - 1; i >= 0; i--) {
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

        // Iterate through SSTables from oldest to newest
        // Later values overwrite earlier ones (keep newest)
        TreeMap<K, V> merged = new TreeMap<>();
        sstables.stream()
                .sorted(comparingLong(sst -> sst.timestamp))
                .forEach(sst -> merged.putAll(sst.data));

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
}
