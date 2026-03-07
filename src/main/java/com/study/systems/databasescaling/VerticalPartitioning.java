package com.study.systems.databasescaling;

import java.util.*;
/**
 * Vertical Partitioning: Split tables by columns
 *
 * Key principles:
 * - Frequently accessed columns in one partition
 * - Rarely accessed columns in another
 * - Reduces I/O for common queries
 * - Requires joins for full records
 */

public class VerticalPartitioning {

    private final Map<String, HotData> hotStore;   // Frequently accessed
    private final Map<String, ColdData> coldStore; // Rarely accessed

    /**
     * Initialize vertical partitioning
     *
     * TODO: Initialize hot and cold stores
     */
    public VerticalPartitioning() {
        // TODO: Initialize both stores
        this.hotStore = null; // Replace
        this.coldStore = null; // Replace
    }

    /**
     * Insert full record
     *
     * TODO: Split record into hot and cold parts
     * - Store frequently accessed fields in hot store
     * - Store rarely accessed fields in cold store
     */
    public void insert(String id, String name, String email, String bio, byte[] largeData) {
        // TODO: Create HotData with id, name, email

        // TODO: Create ColdData with id, bio, largeData

        // TODO: Store in respective stores
    }

    /**
     * Get hot data only (fast, common query)
     *
     * TODO: Retrieve from hot store only
     */
    public HotData getHotData(String id) {
        // TODO: Get from hotStore
        return null; // Replace
    }

    /**
     * Get full record (requires join)
     *
     * TODO: Retrieve from both stores and merge
     */
    public FullRecord getFullRecord(String id) {
        // TODO: Get hot data

        // TODO: Get cold data

        // TODO: Combine into FullRecord

        return null; // Replace
    }

    /**
     * Update hot data (fast)
     */
    public void updateHotData(String id, String name, String email) {
        // TODO: Update hotStore only
    }

    /**
     * Update cold data (infrequent)
     */
    public void updateColdData(String id, String bio, byte[] largeData) {
        // TODO: Update coldStore only
    }

    /**
     * Get statistics
     */
    public PartitionStats getStats() {
        return new PartitionStats(hotStore.size(), coldStore.size());
    }

    static class HotData {
        String id;
        String name;
        String email;

        public HotData(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

    static class ColdData {
        String id;
        String bio;
        byte[] largeData;

        public ColdData(String id, String bio, byte[] largeData) {
            this.id = id;
            this.bio = bio;
            this.largeData = largeData;
        }
    }

    static class FullRecord {
        String id;
        String name;
        String email;
        String bio;
        byte[] largeData;

        public FullRecord(HotData hot, ColdData cold) {
            this.id = hot.id;
            this.name = hot.name;
            this.email = hot.email;
            this.bio = cold.bio;
            this.largeData = cold.largeData;
        }
    }

    static class PartitionStats {
        int hotRecords;
        int coldRecords;

        public PartitionStats(int hotRecords, int coldRecords) {
            this.hotRecords = hotRecords;
            this.coldRecords = coldRecords;
        }
    }
}
