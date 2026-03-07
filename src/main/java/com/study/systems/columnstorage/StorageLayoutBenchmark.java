package com.study.systems.columnstorage;

import java.util.*;

public class StorageLayoutBenchmark {

    public static void main(String[] args) {
        System.out.println("=== Row vs Column Storage Benchmark ===\n");

        benchmarkInserts();
        System.out.println();
        benchmarkPointLookups();
        System.out.println();
        benchmarkColumnScans();
        System.out.println();
        benchmarkAggregations();
    }

    private static final String[] CITIES = {"New York", "San Francisco", "Chicago", "Austin", "Seattle"};

    static void benchmarkInserts() {
        System.out.println("--- Insert Performance ---");
        int numRows = 100000;

        RowStore rowStore = new RowStore();
        long start = System.nanoTime();
        for (int i = 0; i < numRows; i++) {
            int salary = 50000 + (i * 7) % 100000;
            rowStore.insert(new RowStore.Row(i, "User" + i, "user" + i + "@example.com", 25 + (i % 40), CITIES[i % CITIES.length], salary));
        }
        long rowTime = System.nanoTime() - start;

        ColumnStore colStore = new ColumnStore();
        start = System.nanoTime();
        for (int i = 0; i < numRows; i++) {
            int salary = 50000 + (i * 7) % 100000;
            colStore.insert(new ColumnStore.Row(i, "User" + i, "user" + i + "@example.com", 25 + (i % 40), CITIES[i % CITIES.length], salary));
        }
        long colTime = System.nanoTime() - start;

        System.out.printf("Row Store: %.2f ms (%.0f inserts/sec)%n",
                rowTime / 1e6, numRows / (rowTime / 1e9));
        System.out.printf("Column Store: %.2f ms (%.0f inserts/sec)%n",
                colTime / 1e6, numRows / (colTime / 1e9));
        System.out.printf("Row store is %.2fx faster for inserts%n",
                (double) colTime / rowTime);
    }

    static void benchmarkPointLookups() {
        System.out.println("--- Point Lookup Performance ---");
        int numRows = 100000;
        int numLookups = 1000;

        RowStore rowStore = new RowStore();
        ColumnStore colStore = new ColumnStore();
        for (int i1 = 0; i1 < numRows; i1++) {
            int salary = 50000 + (i1 * 7) % 100000;
            String city = CITIES[i1 % CITIES.length];
            rowStore.insert(new RowStore.Row(i1, "User" + i1, "user" + i1 + "@example.com", 25 + (i1 % 40), city, salary));
            colStore.insert(new ColumnStore.Row(i1, "User" + i1, "user" + i1 + "@example.com", 25 + (i1 % 40), city, salary));
        }

        Random rand = new Random(42);
        long start = System.nanoTime();
        for (int i = 0; i < numLookups; i++) {
            rowStore.getById(rand.nextInt(numRows));
        }
        long rowTime = System.nanoTime() - start;

        rand = new Random(42);
        start = System.nanoTime();
        for (int i = 0; i < numLookups; i++) {
            colStore.getById(rand.nextInt(numRows));
        }
        long colTime = System.nanoTime() - start;

        System.out.printf("Row Store: %.2f ms (%.0f lookups/sec)%n",
                rowTime / 1e6, numLookups / (rowTime / 1e9));
        System.out.printf("Column Store: %.2f ms (%.0f lookups/sec)%n",
                colTime / 1e6, numLookups / (colTime / 1e9));
        System.out.printf("Row store is %.2fx faster for point lookups%n",
                (double) colTime / rowTime);
    }

    static void benchmarkColumnScans() {
        System.out.println("--- Column Scan Performance (avg salary) ---");
        int numRows = 100000;

        RowStore rowStore = new RowStore();
        ColumnStore colStore = new ColumnStore();
        for (int i = 0; i < numRows; i++) {
            int salary = 50000 + (i * 7) % 100000;
            String city = CITIES[i % CITIES.length];
            rowStore.insert(new RowStore.Row(i, "User" + i, "user" + i + "@example.com", 25 + (i % 40), city, salary));
            colStore.insert(new ColumnStore.Row(i, "User" + i, "user" + i + "@example.com", 25 + (i % 40), city, salary));
        }

        long start = System.nanoTime();
        double rowAvg = rowStore.avgSalary();
        long rowTime = System.nanoTime() - start;

        start = System.nanoTime();
        double colAvg = colStore.avgSalary();
        long colTime = System.nanoTime() - start;

        System.out.printf("Row Store: %.2f ms (result: %.2f)%n",
                rowTime / 1e6, rowAvg);
        System.out.printf("Column Store: %.2f ms (result: %.2f)%n",
                colTime / 1e6, colAvg);
        System.out.printf("Column store is %.2fx faster for column scans%n",
                (double) rowTime / colTime);
    }

    static void benchmarkAggregations() {
        System.out.println("--- Aggregation Performance (avg salary by city) ---");
        int numRows = 100000;

        RowStore rowStore = new RowStore();
        ColumnStore colStore = new ColumnStore();
        for (int i = 0; i < numRows; i++) {
            int salary = 50000 + (i * 7) % 100000;
            String city = CITIES[i % CITIES.length];
            rowStore.insert(new RowStore.Row(i, "User" + i, "user" + i + "@example.com", 25 + (i % 40), city, salary));
            colStore.insert(new ColumnStore.Row(i, "User" + i, "user" + i + "@example.com", 25 + (i % 40), city, salary));
        }

        long start = System.nanoTime();
        rowStore.avgSalaryByCity();
        long rowTime = System.nanoTime() - start;

        start = System.nanoTime();
        colStore.avgSalaryByCity();
        long colTime = System.nanoTime() - start;

        System.out.printf("Row Store: %.2f ms%n", rowTime / 1e6);
        System.out.printf("Column Store: %.2f ms%n", colTime / 1e6);
        System.out.printf("Column store is %.2fx faster for aggregations%n",
                (double) rowTime / colTime);
    }
}
