package com.study.systems.databasescaling;

import java.util.*;
/**
 * Range-Based Sharding: Distribute data by key ranges
 *
 * Key principles:
 * - Continuous key ranges per shard
 * - Good for range queries
 * - Risk of hotspots
 * - Easier to add shards
 */

public class RangeBasedSharding {

    private final TreeMap<String, DatabaseShard> rangeMap;
    private final List<DatabaseShard> shards;

    /**
     * Initialize range-based sharding
     *
     * @param ranges List of range boundaries (sorted)
     *
     * TODO: Initialize range sharding
     * - Create TreeMap for range lookup
     * - Assign shard to each range
     *
     * Example: ranges = ["M", "Z"] creates 3 shards
     *   Shard 0: keys < "M"
     *   Shard 1: keys >= "M" and < "Z"
     *   Shard 2: keys >= "Z"
     */
    public RangeBasedSharding(List<String> ranges) {
        // TODO: Initialize rangeMap and shards

        // TODO: Create shard for each range

        this.rangeMap = null; // Replace
        this.shards = null; // Replace
    }

    /**
     * Get shard for a given key
     *
     * @param key Record key
     * @return Shard that should store this key
     *
     * TODO: Implement range lookup
     * 1. Find first range >= key (ceilingEntry)
     * 2. If null, use last shard
     * 3. Return shard
     */
    public DatabaseShard getShard(String key) {
        // TODO: Look up range in TreeMap
        // entry = rangeMap.ceilingEntry(key)

        // TODO: Implement iteration/conditional logic

        // TODO: Otherwise return last shard (for keys >= last boundary)

        return null; // Replace
    }

    /**
     * Insert record
     */
    public void insert(String key, String value) {
        // TODO: Get shard for key and insert
    }

    /**
     * Get record
     */
    public String get(String key) {
        // TODO: Get shard for key and retrieve
        return null; // Replace
    }

    /**
     * Range query (scan multiple shards if needed)
     *
     * TODO: Find all shards in range and query them
     */
    public List<String> rangeQuery(String startKey, String endKey) {
        // TODO: Find first shard containing startKey

        // TODO: Query all shards until endKey

        // TODO: Combine results

        return null; // Replace
    }

    /**
     * Get statistics
     */
    public Map<Integer, Integer> getStats() {
        Map<Integer, Integer> stats = new HashMap<>();
        for (int i = 0; i < shards.size(); i++) {
            stats.put(i, shards.get(i).getRecordCount());
        }
        return stats;
    }
}
