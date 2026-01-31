# 07. Database Scaling

> Strategies for handling growing data and traffic through sharding, replication, and partitioning

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing database scaling strategies, explain them simply.

**Prompts to guide you:**

1. **What is database scaling in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do databases need to scale?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for sharding:**
   - Example: "Sharding is like having multiple filing cabinets where..."
   - Your analogy: _[Fill in]_

4. **What is sharding in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **How is replication different from sharding?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for replication:**
   - Example: "Replication is like photocopying important documents where..."
   - Your analogy: _[Fill in]_

7. **What is partitioning in one sentence?**
   - Your answer: _[Fill in after implementation]_

8. **When would you use horizontal vs vertical scaling?**
   - Your answer: _[Fill in after implementation]_

---

## Core Implementation

### Part 1: Hash-Based Sharding

**Your task:** Implement hash-based sharding for distributing data.

```java
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

    static class DatabaseShard {
        int shardId;
        Map<String, String> data;

        public DatabaseShard(int shardId) {
            this.shardId = shardId;
            this.data = new HashMap<>();
        }

        public void insert(String key, String value) {
            data.put(key, value);
        }

        public String get(String key) {
            return data.get(key);
        }

        public void delete(String key) {
            data.remove(key);
        }

        public int getRecordCount() {
            return data.size();
        }
    }
}
```

### Part 2: Range-Based Sharding

**Your task:** Implement range-based sharding for ordered data.

```java
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
        // for i in 0 to ranges.size():
        //   Create shard
        //   Put range boundary -> shard in map

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
    public HashBasedSharding.DatabaseShard getShard(String key) {
        // TODO: Look up range in TreeMap
        // entry = rangeMap.ceilingEntry(key)

        // TODO: If entry found, return its shard

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
```

### Part 3: Master-Slave Replication

**Your task:** Implement master-slave replication for read scaling.

```java
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
        // for each slave:
        //   slave.write(key, value)
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

        // TODO: If slave fails, fallback to master

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
```

### Part 4: Vertical Partitioning

**Your task:** Implement vertical partitioning (column splitting).

```java
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
```

### Part 5: Consistent Hashing for Dynamic Sharding

**Your task:** Implement consistent hashing for easier resharding.

```java
/**
 * Consistent Hashing Sharding: Minimal data movement on resharding
 *
 * Key principles:
 * - Uses hash ring (from load balancing)
 * - Adding/removing shards affects limited keys
 * - Virtual nodes for better distribution
 * - Popular for distributed databases
 */

public class ConsistentHashSharding {

    private final TreeMap<Integer, HashBasedSharding.DatabaseShard> ring;
    private final Map<Integer, HashBasedSharding.DatabaseShard> shards;
    private final int virtualNodesPerShard;
    private int nextShardId;

    /**
     * Initialize consistent hash sharding
     *
     * @param initialShards Number of initial shards
     * @param virtualNodesPerShard Virtual nodes per physical shard
     *
     * TODO: Initialize hash ring
     * - Create TreeMap for ring
     * - Add initial shards with virtual nodes
     */
    public ConsistentHashSharding(int initialShards, int virtualNodesPerShard) {
        // TODO: Initialize structures

        // TODO: Add initial shards

        this.ring = null; // Replace
        this.shards = null; // Replace
        this.virtualNodesPerShard = 0;
    }

    /**
     * Get shard for key
     *
     * TODO: Use consistent hashing to find shard
     */
    public HashBasedSharding.DatabaseShard getShard(String key) {
        // TODO: Hash key

        // TODO: Find next shard on ring (ceilingEntry)

        // TODO: If null, wrap to first shard

        return null; // Replace
    }

    /**
     * Add new shard
     *
     * TODO: Add shard with virtual nodes
     * - Place virtual nodes on ring
     * - Migrate data from affected keys
     */
    public void addShard() {
        // TODO: Create new shard

        // TODO: Add virtual nodes to ring

        // TODO: In production: migrate affected data
    }

    /**
     * Remove shard
     *
     * TODO: Remove shard and virtual nodes
     * - Remove from ring
     * - Migrate data to other shards
     */
    public void removeShard(int shardId) {
        // TODO: Get shard

        // TODO: Remove all virtual nodes from ring

        // TODO: In production: migrate data
    }

    /**
     * Insert/Get/Delete operations
     */
    public void insert(String key, String value) {
        getShard(key).insert(key, value);
    }

    public String get(String key) {
        return getShard(key).get(key);
    }

    public void delete(String key) {
        getShard(key).delete(key);
    }

    /**
     * Hash function
     */
    private int hash(String key) {
        return key.hashCode() & 0x7FFFFFFF;
    }

    /**
     * Get statistics
     */
    public Map<Integer, Integer> getStats() {
        Map<Integer, Integer> stats = new HashMap<>();
        for (Map.Entry<Integer, HashBasedSharding.DatabaseShard> entry : shards.entrySet()) {
            stats.put(entry.getKey(), entry.getValue().getRecordCount());
        }
        return stats;
    }
}
```

---

## Client Code

```java
import java.util.*;

public class DatabaseScalingClient {

    public static void main(String[] args) {
        testHashBasedSharding();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testRangeBasedSharding();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testMasterSlaveReplication();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testVerticalPartitioning();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testConsistentHashSharding();
    }

    static void testHashBasedSharding() {
        System.out.println("=== Hash-Based Sharding Test ===\n");

        HashBasedSharding db = new HashBasedSharding(3);

        // Insert data
        String[] users = {"user1", "user2", "user3", "user4", "user5",
                         "user6", "user7", "user8", "user9", "user10"};

        System.out.println("Inserting 10 users:");
        for (String user : users) {
            db.insert(user, user + "_data");
            System.out.println(user + " -> Shard " + db.getShard(user).shardId);
        }

        System.out.println("\nShard distribution:");
        System.out.println(db.getStats());

        // Test retrieval
        System.out.println("\nRetrieving user3:");
        System.out.println(db.get("user3"));
    }

    static void testRangeBasedSharding() {
        System.out.println("=== Range-Based Sharding Test ===\n");

        // Ranges: A-M, M-Z, Z+
        List<String> ranges = Arrays.asList("M", "Z");
        RangeBasedSharding db = new RangeBasedSharding(ranges);

        // Insert data
        String[] names = {"Alice", "Bob", "Charlie", "Mike", "Nancy",
                         "Oscar", "Peter", "Zoe", "Zachary"};

        System.out.println("Inserting names (range-based):");
        for (String name : names) {
            db.insert(name, name + "_data");
            System.out.println(name + " -> Shard " + db.getShard(name).shardId);
        }

        System.out.println("\nShard distribution:");
        System.out.println(db.getStats());

        // Test range query
        System.out.println("\nRange query: M-P");
        List<String> results = db.rangeQuery("M", "P");
        System.out.println("Results: " + results);
    }

    static void testMasterSlaveReplication() {
        System.out.println("=== Master-Slave Replication Test ===\n");

        MasterSlaveReplication db = new MasterSlaveReplication(2);

        // Test writes (go to master, replicate to slaves)
        System.out.println("Writing to master:");
        db.write("key1", "value1");
        db.write("key2", "value2");
        db.write("key3", "value3");

        // Test reads (distributed across slaves)
        System.out.println("\nReading from slaves (round-robin):");
        for (int i = 0; i < 6; i++) {
            String value = db.read("key" + (i % 3 + 1));
            System.out.println("Read " + (i+1) + ": " + value);
        }

        // Check replication
        System.out.println("\nReplication stats:");
        System.out.println(db.getReplicationStats());
    }

    static void testVerticalPartitioning() {
        System.out.println("=== Vertical Partitioning Test ===\n");

        VerticalPartitioning db = new VerticalPartitioning();

        // Insert records
        System.out.println("Inserting records (hot+cold data):");
        db.insert("user1", "Alice", "alice@example.com",
                 "Long bio...", new byte[1000]);
        db.insert("user2", "Bob", "bob@example.com",
                 "Long bio...", new byte[1000]);

        // Fast query (hot data only)
        System.out.println("\nFast query (hot data only):");
        VerticalPartitioning.HotData hot = db.getHotData("user1");
        System.out.println("Name: " + hot.name + ", Email: " + hot.email);

        // Full query (requires join)
        System.out.println("\nFull query (hot + cold data):");
        VerticalPartitioning.FullRecord full = db.getFullRecord("user1");
        System.out.println("Name: " + full.name);
        System.out.println("Bio length: " + full.bio.length());
        System.out.println("Data size: " + full.largeData.length + " bytes");

        // Stats
        System.out.println("\nPartition stats:");
        VerticalPartitioning.PartitionStats stats = db.getStats();
        System.out.println("Hot: " + stats.hotRecords + ", Cold: " + stats.coldRecords);
    }

    static void testConsistentHashSharding() {
        System.out.println("=== Consistent Hash Sharding Test ===\n");

        ConsistentHashSharding db = new ConsistentHashSharding(3, 3);

        // Insert data
        String[] keys = {"key1", "key2", "key3", "key4", "key5"};
        System.out.println("Initial distribution (3 shards):");
        for (String key : keys) {
            db.insert(key, key + "_data");
            System.out.println(key + " -> Shard " + db.getShard(key).shardId);
        }

        System.out.println("\nStats: " + db.getStats());

        // Add shard (minimal redistribution)
        System.out.println("\nAdding 4th shard:");
        db.addShard();

        System.out.println("New distribution:");
        for (String key : keys) {
            System.out.println(key + " -> Shard " + db.getShard(key).shardId);
        }

        System.out.println("\nStats: " + db.getStats());
    }
}
```

---

## Decision Framework

**Questions to answer after implementation:**

### 1. Scaling Strategy Selection

**When to use Hash-Based Sharding?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Range-Based Sharding?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Master-Slave Replication?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Vertical Partitioning?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Consistent Hash Sharding?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

### 2. Trade-offs

**Hash-Based Sharding:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Range-Based Sharding:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Master-Slave Replication:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Vertical Partitioning:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

### 3. Your Decision Tree

Build your decision tree after practicing:

```
What is your bottleneck?
├─ Read traffic → ?
├─ Write traffic → ?
├─ Data size → ?
├─ Query patterns → ?
└─ Operational complexity → ?
```

### 4. Kill Switch - Don't use when:

**Sharding:**
1. _[When does sharding fail? Fill in]_
2. _[Another failure case]_

**Replication:**
1. _[When does replication fail? Fill in]_
2. _[Another failure case]_

**Partitioning:**
1. _[When does partitioning fail? Fill in]_
2. _[Another failure case]_

### 5. Rule of Three - Alternatives

For each scenario, identify alternatives and compare:

**Scenario: Social network with 100M users**
1. Option A: _[Fill in]_
2. Option B: _[Fill in]_
3. Option C: _[Fill in]_

---

## Practice

### Scenario 1: Scale read-heavy application

**Requirements:**
- 90% reads, 10% writes
- Single database becoming bottleneck
- Need to scale to 10x traffic
- Can tolerate slight staleness

**Your design:**
- Which strategy would you choose? _[Fill in]_
- Why? _[Fill in]_
- How many replicas? _[Fill in]_
- Consistency guarantees? _[Fill in]_

### Scenario 2: Scale social media platform

**Requirements:**
- 500M users
- User profiles, posts, followers
- Need to distribute data
- Want fast user lookups

**Your design:**
- Which sharding strategy? _[Fill in]_
- What's the shard key? _[Fill in]_
- How to handle hot users (celebrities)? _[Fill in]_
- Cross-shard queries? _[Fill in]_

### Scenario 3: Time-series data storage

**Requirements:**
- IoT sensor data
- Queries by time range
- Recent data accessed frequently
- Old data rarely accessed

**Your design:**
- Which partitioning strategy? _[Fill in]_
- How to partition? _[Fill in]_
- Archival strategy? _[Fill in]_
- Query optimization? _[Fill in]_

---

## Review Checklist

- [ ] Hash-based sharding implemented
- [ ] Range-based sharding implemented
- [ ] Master-slave replication implemented
- [ ] Vertical partitioning implemented
- [ ] Consistent hash sharding implemented
- [ ] Understand when to use each strategy
- [ ] Can explain trade-offs between strategies
- [ ] Built decision tree for strategy selection
- [ ] Completed practice scenarios

---

**Next:** [10. Message Queues →](10-message-queues.md)

**Back:** [08. Concurrency Patterns ←](08-concurrency-patterns.md)
