# 01. Storage Engines

> B+Trees vs LSM Trees - The foundation of all database decisions

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing and testing both storage engines, explain them simply.

**Prompts to guide you:**

1. **What is a B+Tree in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do databases use B+Trees?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for B+Tree:**
   - Example: "A B+Tree is like a filing cabinet where..."
   - Your analogy: _[Fill in]_

4. **What is an LSM Tree in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **Why do write-heavy databases use LSM Trees?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for LSM Tree:**
   - Example: "An LSM Tree is like a notebook where..."
   - Your analogy: _[Fill in]_

---

## Core Implementation

### Part 1: B+Tree

**Your task:** Implement a simplified in-memory B+Tree.

```java
import java.util.*;

/**
 * B+Tree: Self-balancing tree optimized for range queries
 *
 * Properties:
 * - All values stored in leaf nodes
 * - Leaves are linked (for range scans)
 * - Height kept minimal
 * - Order K: max K keys per node
 */
public class BPlusTree<K extends Comparable<K>, V> {

    private final int order; // Max keys per node (e.g., 4)
    private Node root;

    // Base node class
    abstract class Node {
        List<K> keys;
        Node parent;

        Node() {
            this.keys = new ArrayList<>();
        }

        abstract boolean isLeaf();
    }

    // Internal nodes: have children, no values
    class InternalNode extends Node {
        List<Node> children;

        InternalNode() {
            super();
            this.children = new ArrayList<>();
        }

        @Override
        boolean isLeaf() {
            return false;
        }
    }

    // Leaf nodes: have values, no children, linked to next leaf
    class LeafNode extends Node {
        List<V> values;
        LeafNode next; // For range scans

        LeafNode() {
            super();
            this.values = new ArrayList<>();
        }

        @Override
        boolean isLeaf() {
            return true;
        }
    }

    public BPlusTree(int order) {
        this.order = order;
        this.root = new LeafNode();
    }

    /**
     * Insert key-value pair
     * Time: O(log N)
     *
     * TODO: Implement insertion logic
     * 1. Find correct leaf node
     * 2. Insert in sorted position
     * 3. If leaf overflows, split it
     * 4. Propagate split up the tree
     */
    public void insert(K key, V value) {
        // TODO: Your implementation here

        // Hint: Start by finding the leaf
        LeafNode leaf = findLeaf(key);

        // TODO: Insert key-value in sorted order

        // TODO: Check if leaf is full (keys.size() >= order)
        // If full, split the leaf

        // TODO: If split causes root to split, create new root
    }

    /**
     * Search for a key
     * Time: O(log N)
     *
     * TODO: Implement search logic
     * 1. Start at root
     * 2. At each internal node, find correct child
     * 3. At leaf, search for key
     */
    public V search(K key) {
        // TODO: Your implementation here

        LeafNode leaf = findLeaf(key);

        // TODO: Search for key in leaf.keys
        // Return corresponding value from leaf.values

        return null; // Replace with actual implementation
    }

    /**
     * Range query: all values where startKey <= key <= endKey
     * Time: O(log N + results)
     *
     * TODO: Implement range query
     * 1. Find leaf containing startKey
     * 2. Follow leaf.next pointers
     * 3. Collect values until endKey
     */
    public List<V> rangeQuery(K startKey, K endKey) {
        List<V> results = new ArrayList<>();

        // TODO: Find starting leaf
        LeafNode leaf = findLeaf(startKey);

        // TODO: Traverse leaves via next pointers
        // Collect all values in range

        return results;
    }

    /**
     * Helper: Find the leaf node where key should be
     */
    private LeafNode findLeaf(K key) {
        Node current = root;

        // TODO: Traverse down the tree
        // At each internal node, pick the correct child
        // Stop when you reach a leaf

        while (!current.isLeaf()) {
            InternalNode internal = (InternalNode) current;

            // TODO: Binary search to find correct child
            // If key < keys[i], go to children[i]
            // Else continue
        }

        return (LeafNode) current;
    }

    /**
     * Helper: Split a full leaf node
     */
    private void splitLeaf(LeafNode leaf) {
        // TODO: Create new leaf
        // Move half the keys/values to new leaf
        // Update next pointers
        // Insert middle key into parent (may cause parent split)
    }

    /**
     * Helper: Split a full internal node
     */
    private void splitInternal(InternalNode node) {
        // TODO: Similar to splitLeaf
        // Move half keys/children to new node
        // Push middle key up to parent
    }

    /**
     * Print tree structure (for debugging)
     */
    public void printTree() {
        printNode(root, 0);
    }

    private void printNode(Node node, int level) {
        String indent = "  ".repeat(level);
        System.out.println(indent + "Level " + level + ": " + node.keys);

        if (!node.isLeaf()) {
            InternalNode internal = (InternalNode) node;
            for (Node child : internal.children) {
                printNode(child, level + 1);
            }
        }
    }
}
```

**Runnable Client Code:**

```java
public class BPlusTreeClient {

    public static void main(String[] args) {
        // Create B+Tree with order 4 (max 4 keys per node)
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);

        System.out.println("=== B+Tree Demo ===\n");

        // Test 1: Sequential inserts
        System.out.println("Inserting keys 1-20...");
        for (int i = 1; i <= 20; i++) {
            tree.insert(i, "Value" + i);
        }

        System.out.println("\nTree structure:");
        tree.printTree();

        // Test 2: Point lookups
        System.out.println("\n--- Point Lookups ---");
        System.out.println("Search(10): " + tree.search(10));
        System.out.println("Search(15): " + tree.search(15));
        System.out.println("Search(100): " + tree.search(100)); // Not found

        // Test 3: Range queries
        System.out.println("\n--- Range Queries ---");
        List<String> range = tree.rangeQuery(5, 10);
        System.out.println("Range [5, 10]: " + range);

        range = tree.rangeQuery(15, 18);
        System.out.println("Range [15, 18]: " + range);

        // Test 4: Random inserts
        System.out.println("\n--- Random Inserts ---");
        BPlusTree<Integer, String> tree2 = new BPlusTree<>(4);
        int[] randomKeys = {45, 12, 67, 23, 89, 34, 56, 78, 90, 1};

        System.out.println("Inserting random keys...");
        for (int key : randomKeys) {
            tree2.insert(key, "Val" + key);
        }

        tree2.printTree();

        // Test 5: Verify sorted order
        System.out.println("\n--- Verify Sorted Order ---");
        List<String> allValues = tree2.rangeQuery(0, 100);
        System.out.println("All values in order: " + allValues);
    }
}
```

---

### Part 2: LSM Tree

**Your task:** Implement a simplified LSM Tree with MemTable and SSTables.

```java
import java.util.*;

/**
 * LSM Tree: Log-Structured Merge Tree optimized for writes
 *
 * Architecture:
 * - Writes go to in-memory MemTable (sorted)
 * - When MemTable full, flush to SSTable (immutable file)
 * - Reads check MemTable, then SSTables (newest first)
 * - Periodically compact SSTables (merge and remove duplicates)
 */
public class LSMTree<K extends Comparable<K>, V> {

    private final int memTableSize; // Max entries before flush
    private TreeMap<K, V> memTable; // In-memory sorted map
    private List<SSTable<K, V>> sstables; // On-disk sorted tables

    /**
     * SSTable: Sorted String Table (immutable)
     * In production: stored on disk
     * Here: simplified in-memory representation
     */
    static class SSTable<K extends Comparable<K>, V> {
        private final TreeMap<K, V> data;
        private final long timestamp; // When created (for ordering)

        SSTable(TreeMap<K, V> data) {
            this.data = new TreeMap<>(data); // Copy
            this.timestamp = System.currentTimeMillis();
        }

        V get(K key) {
            return data.get(key);
        }

        boolean containsKey(K key) {
            return data.containsKey(key);
        }

        Set<Map.Entry<K, V>> entrySet() {
            return data.entrySet();
        }

        int size() {
            return data.size();
        }
    }

    public LSMTree(int memTableSize) {
        this.memTableSize = memTableSize;
        this.memTable = new TreeMap<>();
        this.sstables = new ArrayList<>();
    }

    /**
     * Insert/Update key-value pair
     * Time: O(log M) where M = memTable size
     *
     * TODO: Implement write logic
     * 1. Insert into MemTable
     * 2. If MemTable full, flush to SSTable
     */
    public void put(K key, V value) {
        // TODO: Insert into memTable

        // TODO: Check if memTable.size() >= memTableSize
        // If so, flush to SSTable
    }

    /**
     * Retrieve value for key
     * Time: O(log M + N*log S) where N = number of SSTables, S = SSTable size
     *
     * TODO: Implement read logic
     * 1. Check MemTable first (most recent)
     * 2. Check SSTables in reverse order (newest first)
     * 3. Return first match
     */
    public V get(K key) {
        // TODO: Check memTable first
        if (memTable.containsKey(key)) {
            return memTable.get(key);
        }

        // TODO: Check SSTables from newest to oldest
        for (int i = sstables.size() - 1; i >= 0; i--) {
            SSTable<K, V> table = sstables.get(i);
            if (table.containsKey(key)) {
                return table.get(key);
            }
        }

        return null; // Not found
    }

    /**
     * Flush MemTable to SSTable (simulate disk write)
     */
    private void flush() {
        // TODO: Create new SSTable from current memTable

        // TODO: Add to sstables list

        // TODO: Clear memTable

        System.out.println("Flushed MemTable to SSTable. Total SSTables: " + sstables.size());
    }

    /**
     * Compact SSTables: Merge multiple tables, remove duplicates
     * Time: O(N * S * log S) where N = tables, S = size
     *
     * TODO: Implement compaction
     * 1. Merge all SSTables
     * 2. For duplicate keys, keep newest value
     * 3. Create new compacted SSTable
     */
    public void compact() {
        if (sstables.size() <= 1) {
            return; // Nothing to compact
        }

        // TODO: Merge all SSTables into one
        TreeMap<K, V> merged = new TreeMap<>();

        // TODO: Iterate through SSTables from oldest to newest
        // Later values overwrite earlier ones (keep newest)

        // TODO: Create new compacted SSTable

        // TODO: Replace old SSTables with compacted one

        System.out.println("Compacted " + sstables.size() + " SSTables into 1");
    }

    /**
     * Print current state
     */
    public void printState() {
        System.out.println("MemTable size: " + memTable.size());
        System.out.println("SSTables: " + sstables.size());
        for (int i = 0; i < sstables.size(); i++) {
            System.out.println("  SSTable " + i + ": " + sstables.get(i).size() + " entries");
        }
    }
}
```

**Runnable Client Code:**

```java
public class LSMTreeClient {

    public static void main(String[] args) {
        // Create LSM Tree with memTable size = 5
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        System.out.println("=== LSM Tree Demo ===\n");

        // Test 1: Sequential writes (triggers flush)
        System.out.println("--- Test 1: Sequential Writes ---");
        for (int i = 1; i <= 12; i++) {
            lsm.put(i, "Value" + i);
            System.out.println("Put(" + i + ", Value" + i + ")");
        }

        System.out.println("\nState after 12 inserts:");
        lsm.printState();

        // Test 2: Read values
        System.out.println("\n--- Test 2: Reads ---");
        System.out.println("Get(5): " + lsm.get(5));   // In SSTable
        System.out.println("Get(11): " + lsm.get(11)); // In MemTable
        System.out.println("Get(100): " + lsm.get(100)); // Not found

        // Test 3: Update existing keys
        System.out.println("\n--- Test 3: Updates ---");
        lsm.put(5, "UpdatedValue5");
        lsm.put(11, "UpdatedValue11");

        System.out.println("Get(5) after update: " + lsm.get(5));
        System.out.println("Get(11) after update: " + lsm.get(11));

        // Test 4: Trigger more flushes
        System.out.println("\n--- Test 4: More Writes ---");
        for (int i = 20; i <= 35; i++) {
            lsm.put(i, "Value" + i);
        }

        lsm.printState();

        // Test 5: Compaction
        System.out.println("\n--- Test 5: Compaction ---");
        lsm.compact();
        lsm.printState();

        // Test 6: Verify reads after compaction
        System.out.println("\n--- Test 6: Verify After Compaction ---");
        System.out.println("Get(5): " + lsm.get(5));
        System.out.println("Get(25): " + lsm.get(25));
    }
}
```

---

### Part 3: Benchmark Comparison

**Your task:** Compare B+Tree vs LSM Tree performance.

```java
public class StorageBenchmark {

    public static void main(String[] args) {
        System.out.println("=== Storage Engine Benchmark ===\n");

        benchmarkWrites();
        System.out.println();
        benchmarkReads();
        System.out.println();
        benchmarkMixed();
    }

    static void benchmarkWrites() {
        System.out.println("--- Write Performance ---");
        int numWrites = 10000;

        // B+Tree writes
        BPlusTree<Integer, String> btree = new BPlusTree<>(128);
        long start = System.nanoTime();
        for (int i = 0; i < numWrites; i++) {
            btree.insert(i, "Value" + i);
        }
        long btreeTime = System.nanoTime() - start;

        // LSM Tree writes
        LSMTree<Integer, String> lsm = new LSMTree<>(100);
        start = System.nanoTime();
        for (int i = 0; i < numWrites; i++) {
            lsm.put(i, "Value" + i);
        }
        long lsmTime = System.nanoTime() - start;

        System.out.printf("B+Tree: %.2f ms (%.0f writes/sec)%n",
            btreeTime/1e6, numWrites/(btreeTime/1e9));
        System.out.printf("LSM Tree: %.2f ms (%.0f writes/sec)%n",
            lsmTime/1e6, numWrites/(lsmTime/1e9));
        System.out.printf("LSM is %.2fx faster for writes%n",
            (double)btreeTime/lsmTime);
    }

    static void benchmarkReads() {
        System.out.println("--- Read Performance ---");
        int numEntries = 10000;
        int numReads = 1000;

        // Setup B+Tree
        BPlusTree<Integer, String> btree = new BPlusTree<>(128);
        for (int i = 0; i < numEntries; i++) {
            btree.insert(i, "Value" + i);
        }

        // Setup LSM Tree
        LSMTree<Integer, String> lsm = new LSMTree<>(100);
        for (int i = 0; i < numEntries; i++) {
            lsm.put(i, "Value" + i);
        }
        lsm.compact(); // Compact first for fair comparison

        // Benchmark reads
        Random rand = new Random(42);

        long start = System.nanoTime();
        for (int i = 0; i < numReads; i++) {
            int key = rand.nextInt(numEntries);
            btree.search(key);
        }
        long btreeTime = System.nanoTime() - start;

        rand = new Random(42); // Same sequence
        start = System.nanoTime();
        for (int i = 0; i < numReads; i++) {
            int key = rand.nextInt(numEntries);
            lsm.get(key);
        }
        long lsmTime = System.nanoTime() - start;

        System.out.printf("B+Tree: %.2f ms (%.0f reads/sec)%n",
            btreeTime/1e6, numReads/(btreeTime/1e9));
        System.out.printf("LSM Tree: %.2f ms (%.0f reads/sec)%n",
            lsmTime/1e6, numReads/(lsmTime/1e9));
        System.out.printf("B+Tree is %.2fx faster for reads%n",
            (double)lsmTime/btreeTime);
    }

    static void benchmarkMixed() {
        System.out.println("--- Mixed Workload (50% reads, 50% writes) ---");

        // TODO: Implement mixed workload benchmark
        // Interleave reads and writes
        // Compare performance

        System.out.println("TODO: Implement this benchmark");
    }
}
```

**Must complete:**
- [ ] Implement B+Tree insert, search, rangeQuery
- [ ] Implement LSM Tree put, get, flush, compact
- [ ] Run both client programs successfully
- [ ] Run benchmark and record results
- [ ] Understand WHY each performs better in different scenarios

**Your benchmark results:**
```
Write Performance:
B+Tree:    ___ ms (___ writes/sec)
LSM Tree:  ___ ms (___ writes/sec)
Winner: ___

Read Performance:
B+Tree:    ___ ms (___ reads/sec)
LSM Tree:  ___ ms (___ reads/sec)
Winner: ___

Key insight: ___ [Fill in why this difference exists]
```

---

## Decision Framework

**Your task:** Build decision trees for when to use each storage engine.

### Question 1: Write-heavy or Read-heavy?

Answer after implementing and benchmarking:
- **My answer:** _[Fill in]_
- **Why does this matter?** _[Fill in]_
- **Performance difference I observed:** _[Fill in]_

### Question 2: Need range queries?

Answer:
- **Do B+Trees support range queries?** _[Yes/No - explain how]_
- **Do LSM Trees support range queries?** _[Yes/No - explain complexity]_
- **Which is faster for range queries?** _[Fill in after testing]_

### Question 3: Sequential or random writes?

Answer:
- **B+Tree with random writes:** _[What happens? Why is it slow?]_
- **LSM Tree with random writes:** _[What happens? Why is it fast?]_
- **Your observation from implementation:** _[Fill in]_

### Your Decision Tree

Build this after understanding trade-offs:

```
Storage Engine Selection
│
├─ Write-heavy workload (>70% writes)?
│   ├─ YES → Continue to Q2
│   └─ NO → Continue to Q3
│
├─ Q2: Need strong consistency?
│   ├─ YES → Consider B+Tree (but optimize for writes)
│   └─ NO → Use LSM Tree ✓
│
└─ Q3: Read-heavy workload?
    ├─ YES, mostly point lookups → Use B+Tree ✓
    ├─ YES, many range scans → Use B+Tree ✓
    └─ Mixed workload → [Your decision here based on testing]
```

### The "Kill Switch" - When NOT to use each

**Don't use B+Tree when:**
1. _[Fill in]_
2. _[Fill in]_
3. _[Fill in]_

**Don't use LSM Tree when:**
1. _[Fill in]_
2. _[Fill in]_
3. _[Fill in]_

### The Rule of Three: Alternative Approaches

For any storage decision, consider:

**Option 1: B+Tree**
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

**Option 2: LSM Tree**
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

**Option 3:** _[What's a third option? Hash index? Column store?]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

---

## Practice

### Scenario 1: Social Media Posts Table

Design storage for this table:

```sql
CREATE TABLE posts (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    content TEXT,
    created_at TIMESTAMP,
    likes_count INT
);
```

**Queries:**
- Q1: Get recent posts by user (sorted by created_at)
- Q2: Get top posts by likes in last 24 hours
- Q3: Insert new posts (10,000 posts/sec)

**Your design:**

Storage engine choice: _[B+Tree or LSM?]_

Reasoning:
- Write volume: _[Fill in]_
- Read patterns: _[Fill in]_
- Your choice: _[Fill in]_

Index design:
1. _[What indexes would you create?]_
2. _[Why these specific indexes?]_
3. _[What's the column order and why?]_

### Scenario 2: Time-Series Metrics

Design storage for metrics data:

```sql
CREATE TABLE metrics (
    metric_name VARCHAR(100),
    timestamp TIMESTAMP,
    value DOUBLE,
    tags JSONB
);
```

**Access patterns:**
- Writes: 1M data points/second
- Reads: Recent data (last 1 hour) queried frequently
- Older data rarely accessed
- Retention: 30 days

**Your design:**

Storage engine: _[Fill in]_

Why?
1. _[Write characteristics]_
2. _[Read characteristics]_
3. _[Time-series specific considerations]_

### Scenario 3: E-commerce Inventory

```sql
CREATE TABLE inventory (
    product_id BIGINT PRIMARY KEY,
    quantity INT,
    reserved INT,
    last_updated TIMESTAMP
);
```

**Access patterns:**
- Reads: Very frequent (1M reads/sec)
- Writes: Updates when orders placed (10k writes/sec)
- Consistency: Critical (no overselling)

**Your design:**

Storage engine: _[Fill in]_

Trade-offs you considered:
1. _[Fill in]_
2. _[Fill in]_
3. _[Fill in]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] B+Tree insert, search, range query work correctly
  - [ ] LSM Tree put, get, flush, compact work correctly
  - [ ] All client code runs without errors
  - [ ] Benchmarks completed and results recorded

- [ ] **Understanding**
  - [ ] Can explain B+Tree in simple terms (filled ELI5)
  - [ ] Can explain LSM Tree in simple terms (filled ELI5)
  - [ ] Understand why writes are different speeds
  - [ ] Understand why reads are different speeds

- [ ] **Decision Making**
  - [ ] Built complete decision tree
  - [ ] Identified "kill switch" for each
  - [ ] Solved all 3 practice scenarios
  - [ ] Can justify each design choice

- [ ] **Mastery Check**
  - [ ] Could implement both from memory
  - [ ] Could explain trade-offs in an interview
  - [ ] Know when to use each without looking at notes

---

**Next Topic:** [02. Indexing Strategies →](02-indexing.md)

**Back to:** [Home](../index.md)
