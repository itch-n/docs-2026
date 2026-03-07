package com.study.systems.columnstorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Row-Oriented Storage: All columns for a row stored together
 * <p>
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
     */
    public void insert(Row row) {
        // In reality: Write entire row to one disk location
        rows.put(row.id, row);
    }

    /**
     * Point lookup: O(1) - optimal!
     * Single disk read gets all columns
     */
    public Row getById(int id) {
        // In reality: One disk seek, read entire row
        return rows.get(id);
    }

    /**
     * Column scan: O(N) - inefficient!
     * Must read entire rows even though we only need one column
     */
    public double avgSalary() {
        // Note: You're reading ALL columns just to get salary
        // This is the key inefficiency of row storage for analytics!
        double sum = 0.0;
        int count = 0;
        for (Map.Entry<Integer, Row> entry : rows.entrySet()) {
            sum += entry.getValue().salary;
            count++;

        }
        return sum / count;
    }

    /**
     * Multi-column aggregation
     * Still reads full rows
     */
    public Map<String, Double> avgSalaryByCity() {
        // Note: Still reading entire rows even though only using 2 columns
        Map<String, Double> salaryByCity = new HashMap<>();
        for (Map.Entry<Integer, Row> entry : rows.entrySet()) {
            Row row = entry.getValue();
            salaryByCity.put(row.city, salaryByCity.getOrDefault(row.city, 0d) + row.salary);
        }
        return salaryByCity;
    }
}