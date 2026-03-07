package com.study.systems.columnstorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Column-Oriented Storage: Each column stored separately
 * <p>
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
     */
    public void insert(Row row) {
        // Must write to 6 separate column lists
        // In reality: 6 separate disk writes - write amplification!
        idColumn.add(row.id);
        nameColumn.add(row.name);
        emailColumn.add(row.email);
        ageColumn.add(row.age);
        cityColumn.add(row.city);
        salaryColumn.add(row.salary);
    }

    /**
     * Point lookup: O(C) - inefficient!
     * Must read from each column file
     */
    public Row getById(int id) {
        Integer index = null;
        for (int i = 0; i < idColumn.size(); i++) {
            if (idColumn.get(i) == id) {
                index = i;
                break;
            }
        }
        if (index == null) return null;

        return new Row(
                idColumn.get(index),
                nameColumn.get(index),
                emailColumn.get(index),
                ageColumn.get(index),
                cityColumn.get(index),
                salaryColumn.get(index)
        );
    }

    /**
     * Column scan: O(N) - optimal!
     * Only read the column we need
     */
    public double avgSalary() {
        // Key advantage: Ignore all other columns!
        // If 1M rows: Column store reads 4MB, Row store reads 200MB
        double sum = 0.0;
        int count = 0;
        for (Integer salary : salaryColumn) {
            sum += salary;
            count++;
        }

        return sum / count;
    }

    /**
     * Multi-column aggregation - still efficient!
     * Only read the columns we need
     */
    public Map<String, Double> avgSalaryByCity() {
        Map<String, Double> salaryByCity = new HashMap<>();
        for (int i = 0; i < idColumn.size(); i++) {
            salaryByCity.put(cityColumn.get(i), salaryByCity.getOrDefault(cityColumn.get(i), 0d) + salaryColumn.get(i));
        }
        return salaryByCity;
    }

    /**
     * Column pruning: Read only what's needed
     * This is the killer feature of column stores
     */
    public List<Integer> getSalariesInCity(String targetCity) {
        // Only read city and salary columns - ignore the other 4!
        ArrayList<Integer> salaries = new ArrayList<>();
        for (int i = 0; i < idColumn.size(); i++) {
            if (cityColumn.get(i).equals(targetCity)) {
                salaries.add(salaryColumn.get(i));
            }
        }
        return salaries;
    }
}