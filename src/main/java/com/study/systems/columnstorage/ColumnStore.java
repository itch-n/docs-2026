package com.study.systems.columnstorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Column-Oriented Storage: Each column stored separately
 * <p>
 * Use case: OLAP - analytical workloads
 * Optimized for: Column scans, aggregations
 * <p>
 * Key implementation detail: numeric columns use int[] (not ArrayList<Integer>)
 * so values are stored contiguously in memory. This gives the CPU prefetcher
 * something to work with during scans — the hardware advantage that makes real
 * column stores fast, not just the algorithmic one.
 */
public class ColumnStore {

    private static final int INITIAL_CAPACITY = 1024;

    // Numeric columns: contiguous int[] for cache locality
    private int[] idColumn     = new int[INITIAL_CAPACITY];
    private int[] ageColumn    = new int[INITIAL_CAPACITY];
    private int[] salaryColumn = new int[INITIAL_CAPACITY];

    // String columns remain as lists (no primitive equivalent)
    private List<String> nameColumn  = new ArrayList<>(INITIAL_CAPACITY);
    private List<String> emailColumn = new ArrayList<>(INITIAL_CAPACITY);
    private List<String> cityColumn  = new ArrayList<>(INITIAL_CAPACITY);

    private int size = 0;

    private void ensureCapacity() {
        if (size == idColumn.length) {
            int newCapacity = idColumn.length * 2;
            idColumn     = Arrays.copyOf(idColumn,     newCapacity);
            ageColumn    = Arrays.copyOf(ageColumn,    newCapacity);
            salaryColumn = Arrays.copyOf(salaryColumn, newCapacity);
        }
    }

    static class Row {
        int id;
        String name;
        String email;
        int age;
        String city;
        int salary;

        Row(int id, String name, String email, int age, String city, int salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
            this.city = city;
            this.salary = salary;
        }
    }

    /**
     * Insert: O(C) where C = number of columns - slower!
     * Must write to each column separately
     */
    public void insert(Row row) {
        // Must write to 6 separate column stores
        // In reality: 6 separate disk writes - write amplification!
        ensureCapacity();
        idColumn[size]     = row.id;
        ageColumn[size]    = row.age;
        salaryColumn[size] = row.salary;
        nameColumn.add(row.name);
        emailColumn.add(row.email);
        cityColumn.add(row.city);
        size++;
    }

    /**
     * Point lookup: O(N) - inefficient!
     * Must scan id column then read from each column
     */
    public Row getById(int id) {
        for (int i = 0; i < size; i++) {
            if (idColumn[i] == id) {
                return new Row(
                        idColumn[i],
                        nameColumn.get(i),
                        emailColumn.get(i),
                        ageColumn[i],
                        cityColumn.get(i),
                        salaryColumn[i]
                );
            }
        }
        return null;
    }

    /**
     * Column scan: O(N) - optimal!
     * Only reads the salary int[] — one contiguous memory region
     * CPU prefetcher loads cache lines ahead; no pointer chasing
     */
    public double avgSalary() {
        // Key advantage: touch only salaryColumn, ignore everything else.
        // All values are adjacent ints — one cache line holds 16 of them.
        double sum = 0.0;
        for (int i = 0; i < size; i++) {
            sum += salaryColumn[i];
        }
        return sum / size;
    }

    /**
     * Multi-column aggregation - still efficient!
     * Only reads cityColumn + salaryColumn; ignores name/email/age/id
     */
    public Map<String, Double> avgSalaryByCity() {
        Map<String, Double> salaryByCity = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String city = cityColumn.get(i);
            salaryByCity.put(city, salaryByCity.getOrDefault(city, 0d) + salaryColumn[i]);
        }
        return salaryByCity;
    }

    /**
     * Column pruning: Read only what's needed
     * This is the killer feature of column stores
     */
    public List<Integer> getSalariesInCity(String targetCity) {
        // Only reads cityColumn + salaryColumn — ignores the other 4
        List<Integer> salaries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (cityColumn.get(i).equals(targetCity)) {
                salaries.add(salaryColumn[i]);
            }
        }
        return salaries;
    }
}
