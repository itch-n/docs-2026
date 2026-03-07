package com.study.systems.databasescaling;

import java.util.*;

/**
 * Hash-Based Sharding: Distribute data across shards using hash function
 *
 * Key principles:
 * - Hash key determines shard
 * - Even distribution of data
 * - Simple and predictable
 * - Resharding is expensive
 */

public class HashBasedSharding {

    private final List<DatabaseShard> shards;

    /**
     * Initialize hash-based sharding
     *
     * @param numShards Number of database shards
     *
     * TODO: Initialize sharding
     * - Create list of shards
     * - Initialize each shard
     */
    public HashBasedSharding(int numShards) {
        // TODO: Initialize shards list

        // TODO: Create numShards DatabaseShard instances

        this.shards = null; // Replace
    }

    /**
     * Get shard for a given key
     *
     * @param key Record key (e.g., user ID)
     * @return Shard that should store this key
     *
     * TODO: Implement shard selection
     * 1. Hash the key
     * 2. Modulo by number of shards
     * 3. Return shard at that index
     */
    public DatabaseShard getShard(String key) {
        // TODO: Hash key to integer

        // TODO: Get shard index using modulo
        // index = abs(hash) % shards.size()

        // TODO: Return shard at index

        return null; // Replace
    }

    /**
     * Insert record
     *
     * TODO: Route to correct shard and insert
     */
    public void insert(String key, String value) {
        // TODO: Get shard for key

        // TODO: Insert into shard
    }

    /**
     * Get record
     *
     * TODO: Route to correct shard and retrieve
     */
    public String get(String key) {
        // TODO: Get shard for key

        // TODO: Get from shard

        return null; // Replace
    }

    /**
     * Delete record
     *
     * TODO: Route to correct shard and delete
     */
    public void delete(String key) {
        // TODO: Get shard for key

        // TODO: Delete from shard
    }

    /**
     * Get statistics for all shards
     */
    public Map<Integer, Integer> getStats() {
        Map<Integer, Integer> stats = new HashMap<>();
        for (int i = 0; i < shards.size(); i++) {
            stats.put(i, shards.get(i).getRecordCount());
        }
        return stats;
    }

    /**
     * Hash function
     */
    private int hash(String key) {
        // TODO: Hash key to integer
        // Hint: key.hashCode() & 0x7FFFFFFF
        return 0; // Replace
    }

}
