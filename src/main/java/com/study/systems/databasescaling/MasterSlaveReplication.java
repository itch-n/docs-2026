package com.study.systems.databasescaling;

import java.util.*;
/**
 * Master-Slave Replication: One writer, multiple readers
 *
 * Key principles:
 * - Master handles all writes
 * - Slaves replicate data from master
 * - Slaves handle reads
 * - Eventual consistency
 */

public class MasterSlaveReplication {

    private final Database master;
    private final List<Database> slaves;
    private int readIndex; // For round-robin read distribution

    /**
     * Initialize master-slave replication
     *
     * @param numSlaves Number of read replicas
     *
     * TODO: Initialize replication
     * - Create master database
     * - Create slave databases
     * - Initialize read index
     */
    public MasterSlaveReplication(int numSlaves) {
        // TODO: Create master

        // TODO: Create slaves

        // TODO: Initialize readIndex to 0

        this.master = null; // Replace
        this.slaves = null; // Replace
    }

    /**
     * Write operation (goes to master)
     *
     * TODO: Implement write
     * 1. Write to master
     * 2. Replicate to all slaves
     *
     * Note: In production, replication is async
     */
    public void write(String key, String value) {
        // TODO: Write to master

        // TODO: Replicate to all slaves
    }

    /**
     * Read operation (load balanced across slaves)
     *
     * TODO: Implement read from slaves
     * - Use round robin to select slave
     * - Read from selected slave
     * - Fallback to master if slave fails
     */
    public synchronized String read(String key) {
        // TODO: Select slave using round robin
        // slave = slaves.get(readIndex)
        // readIndex = (readIndex + 1) % slaves.size()

        // TODO: Read from slave

        // TODO: Implement iteration/conditional logic

        return null; // Replace
    }

    /**
     * Delete operation (goes to master)
     */
    public void delete(String key) {
        // TODO: Delete from master

        // TODO: Replicate deletion to slaves
    }

    /**
     * Check replication lag
     *
     * TODO: Compare master and slave data
     * - Count keys that differ
     * - Return lag metrics
     */
    public ReplicationStats getReplicationStats() {
        // TODO: Compare master with each slave

        return null; // Replace
    }

    static class Database {
        String id;
        Map<String, String> data;

        public Database(String id) {
            this.id = id;
            this.data = new HashMap<>();
        }

        public void write(String key, String value) {
            data.put(key, value);
        }

        public String read(String key) {
            return data.get(key);
        }

        public void delete(String key) {
            data.remove(key);
        }

        public int size() {
            return data.size();
        }
    }

    static class ReplicationStats {
        int totalKeys;
        Map<String, Integer> slaveKeyCount;

        public ReplicationStats() {
            this.slaveKeyCount = new HashMap<>();
        }
    }
}
