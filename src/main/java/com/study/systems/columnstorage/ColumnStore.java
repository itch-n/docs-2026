package com.study.systems.columnstorage;

import java.util.*;

/**
 * Column-Oriented Storage: Each column stored separately
 *
 * Use case: OLAP - analytical workloads
 * Optimized for: Column scans, aggregations
 */
public class ColumnStore {

    // Each column stored separately
    private List<Integer> idColumn = new ArrayList<>();
    private List<String> nameColumn = new ArrayList<>();
    private List<String> emailColumn = new ArrayList<>();
    private List<Integer> ageColumn = new ArrayList<>();
    private List<String> cityColumn = new ArrayList<>();
    private List<Integer> salaryColumn = new ArrayList<>();

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
     *
     * TODO: Implement insert
     */
    public void insert(Row row) {
        // TODO: Add each field to its corresponding column
        // Must write to 6 separate column lists
        // In reality: 6 separate disk writes - write amplification!
    }

    /**
     * Point lookup: O(C) - inefficient!
     * Must read from each column file
     *
     * TODO: Implement point lookup
     */
    public Row getById(int id) {
        // TODO: Find the index for this ID
        // TODO: Read from each column at that index
        // In reality: 6 disk seeks (one per column)

        return null;
    }

    /**
     * Column scan: O(N) - optimal!
     * Only read the column we need
     *
     * TODO: Implement column scan
     */
    public double avgSalary() {
        // TODO: Calculate average of salary column only
        // Key advantage: Ignore all other columns!
        // If 1M rows: Column store reads 4MB, Row store reads 200MB

        return 0.0;
    }

    /**
     * Multi-column aggregation - still efficient!
     * Only read the columns we need
     *
     * TODO: Implement aggregation by city
     */
    public Map<String, Double> avgSalaryByCity() {
        // TODO: Read only city and salary columns
        // TODO: Group by city and calculate averages
        // Key advantage: Only 2 columns read instead of all 6

        return new HashMap<>();
    }

    /**
     * Column pruning: Read only what's needed
     * This is the killer feature of column stores
     *
     * TODO: Implement selective column query
     */
    public List<Integer> getSalariesInCity(String targetCity) {
        // TODO: Filter city column and return matching salaries
        // Only read city and salary columns - ignore the other 4!

        return new ArrayList<>();
    }
}
