package com.study.systems.columnstorage;

import java.util.*;

/**
 * Row-Oriented Storage: All columns for a row stored together
 *
 * Use case: OLTP - transactional workloads
 * Optimized for: Point lookups, full row access
 */
public class RowStore {

    // Each row stored as a complete unit
    private Map<Integer, Row> rows = new HashMap<>();

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
     * Insert: O(1) - single write
     * All columns written together in one operation
     *
     * TODO: Implement insert
     */
    public void insert(Row row) {
        // TODO: Store entire row in map
        // In reality: Write entire row to one disk location
    }

    /**
     * Point lookup: O(1) - optimal!
     * Single disk read gets all columns
     *
     * TODO: Implement point lookup
     */
    public Row getById(int id) {
        // TODO: Retrieve row from map
        // In reality: One disk seek, read entire row
        return null;
    }

    /**
     * Column scan: O(N) - inefficient!
     * Must read entire rows even though we only need one column
     *
     * TODO: Implement column scan
     */
    public double avgSalary() {
        // TODO: Calculate average salary
        // Note: You're reading ALL columns just to get salary
        // This is the key inefficiency of row storage for analytics!

        return 0.0;
    }

    /**
     * Multi-column aggregation
     * Still reads full rows
     *
     * TODO: Implement aggregation by city
     */
    public Map<String, Double> avgSalaryByCity() {
        // TODO: Group salaries by city and calculate averages
        // Note: Still reading entire rows even though only using 2 columns

        return new HashMap<>();
    }
}
