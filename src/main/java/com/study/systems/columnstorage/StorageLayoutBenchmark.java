package com.study.systems.columnstorage;

import java.util.*;

/**
 * Benchmarks row-oriented vs column-oriented storage across four workloads.
 *
 * Uses SimulatedRowStore and SimulatedColumnStore (inner classes below), which
 * inject Thread.sleep calls proportional to the I/O each operation would issue
 * on NVMe hardware:
 *
 *   Sequential write/read:  1 ms per 4 KB page   (NVMe ~50 µs, scaled x20)
 *   Random read (seek):     2 ms per operation    (NVMe ~100 µs, scaled x20)
 *
 * Wall time is driven entirely by the injected sleeps, so the ratios reliably
 * reflect real I/O patterns rather than JVM overhead (HashMap costs, GC, JIT).
 */
public class StorageLayoutBenchmark {

    static final int  PAGE_BYTES    = 4_096;
    static final long SEQ_SLEEP_MS  = 1L;   // 1 ms/page  (NVMe ~50 µs, x20)
    static final long RAND_SLEEP_MS = 2L;   // 2 ms/seek  (NVMe ~100 µs, x20)
    static final int  BYTES_PER_ROW = 212;  // id(4) + name(50) + email(100) + age(4) + city(50) + salary(4)
    static final int  BYTES_SALARY  = 4;
    static final int  BYTES_CITY    = 50;
    static final int  NUM_COLUMNS   = 6;

    private static final String[] CITIES = {"New York", "San Francisco", "Chicago", "Austin", "Seattle"};

    public static void main(String[] args) {
        System.out.println("=== Row vs Column Storage Benchmark ===");
        System.out.printf("I/O model: %d ms/page sequential   %d ms random (NVMe x20 scale)%n%n",
                SEQ_SLEEP_MS, RAND_SLEEP_MS);
        benchmarkInserts();
        System.out.println();
        benchmarkPointLookups();
        System.out.println();
        benchmarkColumnScans();
        System.out.println();
        benchmarkAggregations();
    }

    static void benchmarkInserts() {
        System.out.println("--- Insert Performance (100 rows) ---");
        int numRows = 100;

        SimulatedRowStore rowStore = new SimulatedRowStore();
        long start = System.nanoTime();
        for (int i = 0; i < numRows; i++) rowStore.insert(rowStoreRow(i));
        long rowMs = msElapsed(start);

        SimulatedColumnStore colStore = new SimulatedColumnStore();
        start = System.nanoTime();
        for (int i = 0; i < numRows; i++) colStore.insert(colStoreRow(i));
        long colMs = msElapsed(start);

        printRow("Row Store",    rowMs, numRows,               "writes");
        printRow("Column Store", colMs, numRows * NUM_COLUMNS, "writes");
        System.out.printf("  Column Store does %dx more writes (write amplification)%n", NUM_COLUMNS);
    }

    static void benchmarkPointLookups() {
        System.out.println("--- Point Lookup Performance (10 lookups, 100 rows) ---");
        int numRows = 100, numLookups = 10;

        SimulatedRowStore rowStore = new SimulatedRowStore();
        SimulatedColumnStore colStore = new SimulatedColumnStore();
        for (int i = 0; i < numRows; i++) {
            rowStore.preload(rowStoreRow(i));
            colStore.preload(colStoreRow(i));
        }

        Random rand = new Random(42);
        long start = System.nanoTime();
        for (int i = 0; i < numLookups; i++) rowStore.getById(rand.nextInt(numRows));
        long rowMs = msElapsed(start);

        rand = new Random(42);
        start = System.nanoTime();
        for (int i = 0; i < numLookups; i++) colStore.getById(rand.nextInt(numRows));
        long colMs = msElapsed(start);

        printRow("Row Store",    rowMs, numLookups,               "reads");
        printRow("Column Store", colMs, numLookups * NUM_COLUMNS, "reads");
        System.out.printf("  Column Store does %dx more seeks (one per column file)%n", NUM_COLUMNS);
    }

    static void benchmarkColumnScans() {
        System.out.println("--- Column Scan: avgSalary (100 rows) ---");
        int numRows = 100;

        SimulatedRowStore rowStore = new SimulatedRowStore();
        SimulatedColumnStore colStore = new SimulatedColumnStore();
        for (int i = 0; i < numRows; i++) {
            rowStore.preload(rowStoreRow(i));
            colStore.preload(colStoreRow(i));
        }

        long start = System.nanoTime();
        double rowAvg = rowStore.avgSalary();
        long rowMs = msElapsed(start);

        start = System.nanoTime();
        double colAvg = colStore.avgSalary();
        long colMs = msElapsed(start);

        int rowPages = rowStore.pageCount(BYTES_PER_ROW);
        int colPages = colStore.pageCount(BYTES_SALARY);
        printRow("Row Store",    rowMs, rowPages, "page reads");
        printRow("Column Store", colMs, colPages, "page reads");
        System.out.printf("  Column Store reads %.0fx fewer pages (salary only)%n",
                (double) rowPages / colPages);
        System.out.printf("  Result check: row=%.2f col=%.2f (should match)%n", rowAvg, colAvg);
    }

    static void benchmarkAggregations() {
        System.out.println("--- Aggregation: avgSalaryByCity (100 rows) ---");
        int numRows = 100;

        SimulatedRowStore rowStore = new SimulatedRowStore();
        SimulatedColumnStore colStore = new SimulatedColumnStore();
        for (int i = 0; i < numRows; i++) {
            rowStore.preload(rowStoreRow(i));
            colStore.preload(colStoreRow(i));
        }

        long start = System.nanoTime();
        rowStore.avgSalaryByCity();
        long rowMs = msElapsed(start);

        start = System.nanoTime();
        colStore.avgSalaryByCity();
        long colMs = msElapsed(start);

        int rowPages = rowStore.pageCount(BYTES_PER_ROW);
        int colPages = colStore.pageCount(BYTES_CITY + BYTES_SALARY);
        printRow("Row Store",    rowMs, rowPages, "page reads");
        printRow("Column Store", colMs, colPages, "page reads");
        System.out.printf("  Column Store reads %.1fx fewer pages (city + salary only)%n",
                (double) rowPages / colPages);
    }

    // -------------------------------------------------------------------------
    // Simulated stores (inner classes)
    // -------------------------------------------------------------------------

    /**
     * Row store instrumented with NVMe latencies via Thread.sleep.
     * 1 write per insert, 1 seek per lookup, full-row page scan for queries.
     */
    static class SimulatedRowStore extends RowStore {
        private int rowCount = 0;

        /** Inserts without sleeping. Use this to populate before benchmarking. */
        public void preload(Row row) { rowCount++; super.insert(row); }

        @Override public void insert(Row row) {
            sleep(SEQ_SLEEP_MS); rowCount++; super.insert(row);
        }
        @Override public Row getById(int id) {
            sleep(RAND_SLEEP_MS); return super.getById(id);
        }
        @Override public double avgSalary() {
            scanPages(BYTES_PER_ROW); return super.avgSalary();
        }
        @Override public Map<String, Double> avgSalaryByCity() {
            scanPages(BYTES_PER_ROW); return super.avgSalaryByCity();
        }

        public int pageCount(int bytesPerRow) {
            return Math.max(1, (int) Math.ceil((double) rowCount * bytesPerRow / PAGE_BYTES));
        }
        private void scanPages(int bytesPerRow) {
            for (int p = 0; p < pageCount(bytesPerRow); p++) sleep(SEQ_SLEEP_MS);
        }
    }

    /**
     * Column store instrumented with NVMe latencies via Thread.sleep.
     * NUM_COLUMNS writes per insert (write amplification), NUM_COLUMNS seeks per
     * lookup, but column pruning means far fewer page reads for analytics.
     */
    static class SimulatedColumnStore extends ColumnStore {
        private int rowCount = 0;

        /** Inserts without sleeping. Use this to populate before benchmarking. */
        public void preload(Row row) { rowCount++; super.insert(row); }

        @Override public void insert(Row row) {
            for (int c = 0; c < NUM_COLUMNS; c++) sleep(SEQ_SLEEP_MS);
            rowCount++; super.insert(row);
        }
        @Override public Row getById(int id) {
            for (int c = 0; c < NUM_COLUMNS; c++) sleep(RAND_SLEEP_MS);
            return super.getById(id);
        }
        @Override public double avgSalary() {
            scanPages(BYTES_SALARY); return super.avgSalary();
        }
        @Override public Map<String, Double> avgSalaryByCity() {
            scanPages(BYTES_CITY + BYTES_SALARY); return super.avgSalaryByCity();
        }

        public int pageCount(int bytesPerValue) {
            return Math.max(1, (int) Math.ceil((double) rowCount * bytesPerValue / PAGE_BYTES));
        }
        private void scanPages(int bytesPerValue) {
            for (int p = 0; p < pageCount(bytesPerValue); p++) sleep(SEQ_SLEEP_MS);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
    private static void printRow(String label, long wallMs, int ops, String opLabel) {
        System.out.printf("  %-14s  %4d ms   %3d %s%n", label + ":", wallMs, ops, opLabel);
    }
    private static long msElapsed(long startNs) {
        return (System.nanoTime() - startNs) / 1_000_000L;
    }
    private static RowStore.Row rowStoreRow(int i) {
        return new RowStore.Row(i, "User" + i, "user" + i + "@example.com",
                25 + (i % 40), CITIES[i % CITIES.length], 50_000 + (i * 7) % 100_000);
    }
    private static ColumnStore.Row colStoreRow(int i) {
        return new ColumnStore.Row(i, "User" + i, "user" + i + "@example.com",
                25 + (i % 40), CITIES[i % CITIES.length], 50_000 + (i * 7) % 100_000);
    }
}
