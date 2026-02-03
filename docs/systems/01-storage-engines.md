# 01. Storage Engines

> B+Trees vs LSM Trees - The foundation of all database decisions

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing and testing both storage engines, explain them simply.

**Prompts to guide you:**

1. **What is a B+Tree in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

2. **Why do databases use B+Trees?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

3. **Real-world analogy for B+Tree:**
    - Example: "A B+Tree is like a filing cabinet where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What is an LSM Tree in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

5. **Why do write-heavy databases use LSM Trees?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

6. **Real-world analogy for LSM Tree:**
    - Example: "An LSM Tree is like a notebook where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

<div class="learner-section" markdown>

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **B+Tree insert operation:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified after implementation: <span class="fill-in">[Actual: O(?)]</span>

2. **LSM Tree write operation (to MemTable):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Performance calculation:**
    - For 100,000 writes, B+Tree = <span class="fill-in">_____</span> operations (if log base is 10)
    - For 100,000 writes, LSM Tree = <span class="fill-in">_____</span> operations (before flush)
    - Speedup factor for writes: LSM is approximately <span class="fill-in">_____</span> times faster

### Scenario Predictions

**Scenario 1:** Time-series metrics database (1M writes/second, rare reads)

- **Best storage engine?** <span class="fill-in">[B+Tree/LSM Tree - Why?]</span>
- **Key consideration:** <span class="fill-in">[Write amplification/Read speed/Range queries?]</span>
- **Why this choice?** <span class="fill-in">[Fill in your reasoning]</span>

**Scenario 2:** E-commerce inventory system (100k reads/sec, 5k writes/sec)

- **Best storage engine?** <span class="fill-in">[B+Tree/LSM Tree - Why?]</span>
- **What pattern benefits most?** <span class="fill-in">[Point lookups/Range scans/Random writes?]</span>

**Scenario 3:** Social media analytics (read historical posts by date range)

- **Which handles range queries better?** <span class="fill-in">[B+Tree/LSM Tree - Why?]</span>
- **Key data structure feature:** <span class="fill-in">[Linked leaves/Sorted SSTables?]</span>

### Trade-off Quiz

**Question:** When would B+Tree be BETTER than LSM Tree despite slower writes?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after benchmarking]</span>

**Question:** What's the MAIN advantage of LSM Trees for writes?

- [ ] No tree balancing required on each write
- [ ] Better space efficiency
- [ ] Faster range queries
- [ ] Lower read amplification

Verify after implementation: <span class="fill-in">[Which one(s)?]</span>

**Question:** What happens if you never compact an LSM Tree?

- Your prediction: <span class="fill-in">[What problem occurs?]</span>
- Verified: <span class="fill-in">[Fill in after testing]</span>

</div>

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized storage approaches to understand the trade-offs.

### Example: Write-Heavy Workload

**Problem:** Insert 10,000 key-value pairs as quickly as possible.

#### Approach 1: B+Tree (Immediate Persistence)

```java
// Every write requires tree traversal and potential rebalancing
BPlusTree<Integer, String> btree = new BPlusTree<>(128);

long start = System.nanoTime();
for (int i = 0; i < 10000; i++) {
    btree.insert(i, "Value" + i);  // Each insert: O(log N)
    // Must traverse tree from root to leaf
    // May trigger node splits (expensive)
    // Must maintain tree balance property
}
long duration = System.nanoTime() - start;
```

**Analysis:**

- Time: O(N log N) - Each insert is O(log N)
- Space: O(N) - Tree structure overhead
- For 10,000 inserts: ~10,000 * log(10,000) = ~130,000 operations
- Write amplification: High (each insert may split nodes, update parent pointers)

#### Approach 2: LSM Tree (Buffered Writes)

```java
// Writes go to in-memory MemTable (just a TreeMap insert)
LSMTree<Integer, String> lsm = new LSMTree<>(100);

long start = System.nanoTime();
for (int i = 0; i < 10000; i++) {
    lsm.put(i, "Value" + i);  // Each put: O(log M), M = memTable size
    // Only updates in-memory TreeMap
    // Occasional flush to disk (batched)
}
long duration = System.nanoTime() - start;
```

**Analysis:**

- Time: O(N log M) where M << N (M = MemTable size)
- Space: O(N) - Eventually flushes to SSTables
- For 10,000 inserts: ~10,000 * log(100) = ~20,000 operations
- Write amplification: Lower (batch writes to disk)

#### Performance Comparison

| Operation Count | B+Tree (O(N log N)) | LSM Tree (O(N log M)) | LSM Advantage |
|-----------------|---------------------|-----------------------|---------------|
| N = 1,000       | ~10,000 ops         | ~2,000 ops            | 5x faster     |
| N = 10,000      | ~130,000 ops        | ~20,000 ops           | 6.5x faster   |
| N = 100,000     | ~1,700,000 ops      | ~200,000 ops          | 8.5x faster   |

**Your calculation:** For N = 50,000 writes, LSM Tree is approximately _____ times faster.

#### Why Does LSM Win for Writes?

**Key insight to understand:**

B+Tree: Every insert = tree traversal + potential split

```
Insert key=50:

1. Traverse root → internal → leaf (3 disk seeks)
2. Insert in leaf (sorted position)
3. If leaf full, split node (expensive)
4. Update parent pointers (more writes)
Result: 1 logical write = 4-5 physical writes (write amplification!)
```

LSM Tree: Batched sequential writes

```
Insert key=50:

1. Insert into MemTable (in-memory TreeMap)
2. When MemTable full, flush entire batch to SSTable
3. Sequential write to disk (very fast)
Result: 1 logical write = 1 in-memory write (occasionally batched to disk)
```

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- <span class="fill-in">[Why does B+Tree require more writes per operation?]</span>
- <span class="fill-in">[How does LSM Tree achieve better write throughput?]</span>
- <span class="fill-in">[What's the trade-off for read performance?]</span>

</div>

---

### Example: Read-Heavy Workload

**Problem:** Perform 1,000 random lookups after loading 10,000 records.

#### Approach 1: B+Tree (Single Location Read)

```java
BPlusTree<Integer, String> btree = new BPlusTree<>(128);
// Load data...

long start = System.nanoTime();
for (int i = 0; i < 1000; i++) {
    int key = random.nextInt(10000);
    String value = btree.search(key);  // Single tree traversal: O(log N)
    // Root → Internal → Leaf (3-4 hops)
}
long duration = System.nanoTime() - start;
```

**Analysis:**

- Time: O(log N) per read
- For 1,000 reads: ~1,000 * log(10,000) = ~13,000 operations
- Read amplification: Low (single path through tree)

#### Approach 2: LSM Tree (Multiple Location Read)

```java
LSMTree<Integer, String> lsm = new LSMTree<>(100);
// Load data... (creates multiple SSTables)

long start = System.nanoTime();
for (int i = 0; i < 1000; i++) {
    int key = random.nextInt(10000);
    String value = lsm.get(key);  // Check MemTable + all SSTables
    // Must check MemTable (O(log M))
    // Then check SSTable-5 (O(log S))
    // Then check SSTable-4 (O(log S))
    // ... continue until found
}
long duration = System.nanoTime() - start;
```

**Analysis:**

- Time: O(log M + K * log S) where K = number of SSTables
- For 1,000 reads with 10 SSTables: ~1,000 * (10 * log(1000)) = ~100,000 operations
- Read amplification: High (must check multiple locations)

#### Performance Comparison

| SSTable Count | B+Tree (O(log N)) | LSM Tree (O(K * log S)) | B+Tree Advantage |
|---------------|-------------------|-------------------------|------------------|
| K = 1         | ~13 ops/read      | ~13 ops/read            | ~1x (equal)      |
| K = 5         | ~13 ops/read      | ~65 ops/read            | 5x faster        |
| K = 10        | ~13 ops/read      | ~130 ops/read           | 10x faster       |

**Your calculation:** With 20 SSTables, B+Tree is approximately _____ times faster for reads.

**Key insight:** This is why LSM Trees need **compaction** - to reduce SSTable count!

**After benchmarking, fill in:**

<div class="learner-section" markdown>

- <span class="fill-in">[What happens to LSM read performance as SSTables accumulate?]</span>
- <span class="fill-in">[Why doesn't B+Tree have this problem?]</span>
- <span class="fill-in">[How does compaction help LSM Trees?]</span>

</div>

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

        // TODO: Check SSTables from newest to oldest

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

<table class="benchmark-table">
<thead>
  <tr>
    <th>Metric</th>
    <th>B+Tree</th>
    <th>LSM Tree</th>
    <th>Winner</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>Write Performance (ms)</td>
    <td class="blank">___ ms</td>
    <td class="blank">___ ms</td>
    <td class="blank">___</td>
  </tr>
  <tr>
    <td>Write Throughput (ops/sec)</td>
    <td class="blank">___ writes/sec</td>
    <td class="blank">___ writes/sec</td>
    <td class="blank">___</td>
  </tr>
  <tr>
    <td>Read Performance (ms)</td>
    <td class="blank">___ ms</td>
    <td class="blank">___ ms</td>
    <td class="blank">___</td>
  </tr>
  <tr>
    <td>Read Throughput (ops/sec)</td>
    <td class="blank">___ reads/sec</td>
    <td class="blank">___ reads/sec</td>
    <td class="blank">___</td>
  </tr>
</tbody>
</table>

<div class="learner-section" markdown>

**Key insight:** <span class="fill-in">[Fill in why this difference exists]</span>

</div>

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken storage engine implementations. This tests your deep understanding.

### Challenge 1: Broken B+Tree Leaf Split

```java
/**
 * This splitLeaf method is supposed to split a full leaf node.
 * It has 3 CRITICAL BUGS. Find them!
 */
private void splitLeaf(LeafNode leaf) {
    LeafNode newLeaf = new LeafNode();
    int midpoint = leaf.keys.size() / 2;

    // Move half the keys/values to new leaf
    for (int i = midpoint; i < leaf.keys.size(); i++) {
        newLeaf.keys.add(leaf.keys.get(i));
        newLeaf.values.add(leaf.values.get(i));
    }

    // BUG 1: What's wrong with how we remove keys from old leaf?
    for (int i = midpoint; i < leaf.keys.size(); i++) {
        leaf.keys.remove(i);
        leaf.values.remove(i);
    }

    // BUG 2: Missing critical pointer update!
    // newLeaf.next = ???

    // BUG 3: What if leaf is the root?
    InternalNode parent = (InternalNode) leaf.parent;
    parent.keys.add(newLeaf.keys.get(0));
    parent.children.add(newLeaf);
}
```

**Your debugging:**

- **Bug 1 location:** <span class="fill-in">[Which lines?]</span>
- **Bug 1 explanation:** <span class="fill-in">[What happens when you remove while iterating?]</span>
- **Bug 1 fix:** <span class="fill-in">[How to correctly remove elements?]</span>

- **Bug 2 location:** <span class="fill-in">[What's missing?]</span>
- **Bug 2 explanation:** <span class="fill-in">[Why must leaf nodes be linked?]</span>
- **Bug 2 fix:** <span class="fill-in">[Write the correct code]</span>

- **Bug 3 location:** <span class="fill-in">[Which line?]</span>
- **Bug 3 explanation:** <span class="fill-in">[What if leaf.parent is null?]</span>
- **Bug 3 fix:** <span class="fill-in">[How to handle root split?]</span>

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Lines 13-16):** Removing elements while iterating forward breaks indices. Each removal shifts remaining
elements left.

**Fix:**

```java
// Remove from end to avoid index shifting
for (int i = leaf.keys.size() - 1; i >= midpoint; i--) {
    leaf.keys.remove(i);
    leaf.values.remove(i);
}
// OR use subList:
leaf.keys.subList(midpoint, leaf.keys.size()).clear();
leaf.values.subList(midpoint, leaf.values.size()).clear();
```

**Bug 2 (After line 11):** Must link new leaf into the leaf chain for range queries!

**Fix:**

```java
newLeaf.next = leaf.next;  // New leaf points to old leaf's next
leaf.next = newLeaf;       // Old leaf points to new leaf
```

**Bug 3 (Lines 22-23):** If leaf is root, parent is null - NullPointerException!

**Fix:**

```java
if (leaf.parent == null) {
    // Create new root
    InternalNode newRoot = new InternalNode();
    newRoot.keys.add(newLeaf.keys.get(0));
    newRoot.children.add(leaf);
    newRoot.children.add(newLeaf);
    leaf.parent = newRoot;
    newLeaf.parent = newRoot;
    root = newRoot;
} else {
    InternalNode parent = (InternalNode) leaf.parent;
    // Insert key in sorted position (not just add!)
    // Insert newLeaf in corresponding position
}
```

</details>

---

### Challenge 2: Broken LSM Tree Compaction

```java
/**
 * Compact all SSTables into one.
 * This has 2 LOGIC BUGS that cause data loss and incorrect ordering.
 */
public void compact() {
    if (sstables.size() <= 1) return;

    TreeMap<K, V> merged = new TreeMap<>();

    // BUG 1: Wrong iteration order!
    for (int i = sstables.size() - 1; i >= 0; i--) {
        SSTable<K, V> table = sstables.get(i);
        for (Map.Entry<K, V> entry : table.entrySet()) {
            merged.put(entry.getKey(), entry.getValue());
        }
    }

    // Create compacted SSTable
    SSTable<K, V> compacted = new SSTable<>(merged);

    // BUG 2: What about the sstables list?
    sstables.add(compacted);

    System.out.println("Compacted into 1 SSTable");
}
```

**Your debugging:**

- **Bug 1:** <span class="fill-in">[What's wrong with iterating newest to oldest?]</span>
- **Bug 1 explanation:** <span class="fill-in">[Which value should win for duplicate keys?]</span>
- **Bug 1 test case:** Insert key=5 with "Old", then update to "New". After compaction, what do you get?
- **Bug 1 fix:** <span class="fill-in">[Correct the iteration order]</span>

- **Bug 2:** <span class="fill-in">[What's wrong with line 18?]</span>
- **Bug 2 explanation:** <span class="fill-in">[What happens to old SSTables?]</span>
- **Bug 2 fix:** <span class="fill-in">[Write the correct code]</span>

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 9):** Iterating from newest to oldest means older values overwrite newer ones!

LSM Trees must keep the NEWEST value for each key. By iterating newest-to-oldest and using `put()`, when we encounter
the key again in an older SSTable, it overwrites the newer value.

**Fix:**

```java
// Iterate from OLDEST to NEWEST
for (int i = 0; i < sstables.size(); i++) {
    SSTable<K, V> table = sstables.get(i);
    for (Map.Entry<K, V> entry : table.entrySet()) {
        merged.put(entry.getKey(), entry.getValue());  // Later puts overwrite earlier
    }
}
```

**Bug 2 (Line 18):** We add the compacted SSTable but never remove the old ones! Memory leak!

**Fix:**

```java
sstables.clear();          // Remove all old SSTables
sstables.add(compacted);   // Add compacted one
```

</details>

---

### Challenge 3: B+Tree Search with Wrong Child Selection

```java
/**
 * Find the leaf node where key should be located.
 * This has 1 SUBTLE BUG in binary search logic.
 */
private LeafNode findLeaf(K key) {
    Node current = root;

    while (!current.isLeaf()) {
        InternalNode internal = (InternalNode) current;

        // BUG: Wrong child selection logic!
        int i = 0;
        while (i < internal.keys.size() && key.compareTo(internal.keys.get(i)) > 0) {
            i++;
        }

        current = internal.children.get(i);
    }

    return (LeafNode) current;
}
```

**Your debugging:**

- **Bug location:** <span class="fill-in">[Which lines?]</span>
- **Bug explanation:** <span class="fill-in">[What happens with keys equal to internal node keys?]</span>
- **Test case:** Tree has keys [10, 20, 30]. Search for key=20. Which child do you visit?
- **Bug fix:** <span class="fill-in">[Should comparison be >= or >?]</span>

**Trace through manually:**

```
Internal node: keys=[20], children=[ChildA, ChildB]
ChildA contains: [10, 15]
ChildB contains: [20, 25, 30]

Search key=20:

- Line 12: i=0, key(20) > keys[0](20)? NO
- i stays 0
- Visit children[0] = ChildA
- BUG: Key 20 is actually in ChildB!
```

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 12):** Using `>` instead of `>=` causes keys equal to split points to go left when they should go right.

**B+Tree invariant:** Internal node key K means "left child < K, right child >= K"

**Fix:**

```java
while (i < internal.keys.size() && key.compareTo(internal.keys.get(i)) >= 0) {
    i++;
}
```

OR be more explicit:

```java
int i;
for (i = 0; i < internal.keys.size(); i++) {
    if (key.compareTo(internal.keys.get(i)) < 0) {
        break;
    }
}
current = internal.children.get(i);
```

</details>

---

### Challenge 4: LSM Tree Missing Flush

```java
/**
 * This LSM Tree mysteriously loses data after many inserts.
 * Find the CRITICAL MISSING OPERATION.
 */
public class LSMTree<K extends Comparable<K>, V> {
    private TreeMap<K, V> memTable;
    private List<SSTable<K, V>> sstables;
    private final int memTableSize = 100;

    public void put(K key, V value) {
        memTable.put(key, value);

        // BUG: What's missing here?
        if (memTable.size() >= memTableSize) {
            flush();
        }
    }

    private void flush() {
        SSTable<K, V> newTable = new SSTable<>(memTable);
        sstables.add(newTable);
        // BUG: Missing critical step!
        System.out.println("Flushed to SSTable");
    }
}
```

**Your debugging:**

- **Bug location:** <span class="fill-in">[What's missing in flush()?]</span>
- **Bug explanation:** <span class="fill-in">[What happens to MemTable after flush?]</span>
- **Test case:** Insert 250 items. How many times does flush() run? How many items in memTable?
- **Expected:** MemTable has 50 items after 250 inserts (flushed 200)
- **Actual:** <span class="fill-in">[What really happens?]</span>
- **Bug fix:** <span class="fill-in">[Write the missing code]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (After line 20):** We never clear the MemTable after flushing!

**Result:** MemTable keeps growing forever. After first flush, memTable has 100 items. After second flush, it has 200.
Eventually OutOfMemoryError.

**Also:** Data gets duplicated across SSTables because we keep the same data in memory and keep flushing it again.

**Fix:**

```java
private void flush() {
    SSTable<K, V> newTable = new SSTable<>(memTable);
    sstables.add(newTable);
    memTable.clear();  // CRITICAL: Clear for new writes!
    System.out.println("Flushed to SSTable");
}
```

OR initialize a new MemTable:

```java
memTable = new TreeMap<>();
```

</details>

---

### Challenge 5: Range Query Doesn't Stop

```java
/**
 * B+Tree range query implementation.
 * This has 1 CRITICAL BUG causing incorrect results.
 */
public List<V> rangeQuery(K startKey, K endKey) {
    List<V> results = new ArrayList<>();

    LeafNode leaf = findLeaf(startKey);

    // Traverse leaves until we exceed endKey
    while (leaf != null) {
        for (int i = 0; i < leaf.keys.size(); i++) {
            K key = leaf.keys.get(i);

            // BUG: Wrong range check!
            if (key.compareTo(startKey) >= 0) {
                results.add(leaf.values.get(i));
            }
        }

        leaf = leaf.next;
    }

    return results;
}
```

**Your debugging:**

- **Bug location:** <span class="fill-in">[Which line?]</span>
- **Bug explanation:** <span class="fill-in">[What's missing from the range check?]</span>
- **Test case:** Tree has keys [1,3,5,7,9,11,13,15]. rangeQuery(5, 10). Expected: [5,7,9].
  Actual: <span class="fill-in">[What?]</span>
- **Bug fix:** <span class="fill-in">[Add missing condition]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Lines 11-17):** We check if key >= startKey but never check if key > endKey!

**Result:** Range query returns ALL keys from startKey to the end of the tree, ignoring endKey.

**Fix:**

```java
while (leaf != null) {
    for (int i = 0; i < leaf.keys.size(); i++) {
        K key = leaf.keys.get(i);

        if (key.compareTo(endKey) > 0) {
            return results;  // Stop when we exceed endKey
        }

        if (key.compareTo(startKey) >= 0) {
            results.add(leaf.values.get(i));
        }
    }

    leaf = leaf.next;
}
```

**Optimization:** Can also break out of outer loop:

```java
if (key.compareTo(endKey) > 0) {
    break;  // Exit inner loop
}
// After inner loop:
if (leaf.keys.size() > 0 &&
    leaf.keys.get(leaf.keys.size()-1).compareTo(endKey) > 0) {
    break;  // Exit outer loop
}
```

</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 9+ bugs across 5 challenges
- [ ] Understood WHY each bug causes data corruption or incorrect results
- [ ] Could explain the fix to someone else
- [ ] Learned common storage engine mistakes to avoid

**Common mistakes you discovered:**

1. <span class="fill-in">[Index manipulation while iterating]</span>
2. <span class="fill-in">[Missing pointer updates in tree structures]</span>
3. <span class="fill-in">[Wrong iteration order in merge operations]</span>
4. <span class="fill-in">[Forgetting to clear/reset data structures]</span>
5. <span class="fill-in">[Incomplete boundary checks in range queries]</span>

---

## Decision Framework

**Your task:** Build decision trees for when to use each storage engine.

### Question 1: Write-heavy or Read-heavy?

Answer after implementing and benchmarking:

- **My answer:** <span class="fill-in">[Fill in]</span>
- **Why does this matter?** <span class="fill-in">[Fill in]</span>
- **Performance difference I observed:** <span class="fill-in">[Fill in]</span>

### Question 2: Need range queries?

Answer:

- **Do B+Trees support range queries?** <span class="fill-in">[Yes/No - explain how]</span>
- **Do LSM Trees support range queries?** <span class="fill-in">[Yes/No - explain complexity]</span>
- **Which is faster for range queries?** <span class="fill-in">[Fill in after testing]</span>

### Question 3: Sequential or random writes?

Answer:

- **B+Tree with random writes:** <span class="fill-in">[What happens? Why is it slow?]</span>
- **LSM Tree with random writes:** <span class="fill-in">[What happens? Why is it fast?]</span>
- **Your observation from implementation:** <span class="fill-in">[Fill in]</span>

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

1. <span class="fill-in">[Fill in]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

**Don't use LSM Tree when:**

1. <span class="fill-in">[Fill in]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

### The Rule of Three: Alternative Approaches

For any storage decision, consider:

**Option 1: B+Tree**

- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>
- Use when: <span class="fill-in">[Fill in]</span>

**Option 2: LSM Tree**

- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>
- Use when: <span class="fill-in">[Fill in]</span>

**Option 3:** <span class="fill-in">[What's a third option? Hash index? Column store?]</span>

- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>
- Use when: <span class="fill-in">[Fill in]</span>

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

Storage engine choice: <span class="fill-in">[B+Tree or LSM?]</span>

Reasoning:

- Write volume: <span class="fill-in">[Fill in]</span>
- Read patterns: <span class="fill-in">[Fill in]</span>
- Your choice: <span class="fill-in">[Fill in]</span>

Index design:

1. <span class="fill-in">[What indexes would you create?]</span>
2. <span class="fill-in">[Why these specific indexes?]</span>
3. <span class="fill-in">[What's the column order and why?]</span>

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

Storage engine: <span class="fill-in">[Fill in]</span>

Why?

1. <span class="fill-in">[Write characteristics]</span>
2. <span class="fill-in">[Read characteristics]</span>
3. <span class="fill-in">[Time-series specific considerations]</span>

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

Storage engine: <span class="fill-in">[Fill in]</span>

Trade-offs you considered:

1. <span class="fill-in">[Fill in]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

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

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently
complete this section.

### Gate 1: Explain to a Database Engineer

**Scenario:** A database engineer asks you about storage engines for a new service.

**Your explanation (write it out):**

> "B+Trees and LSM Trees are two fundamental storage engine architectures..."
>
> <span class="fill-in">[Fill in your explanation in plain English - 4-5 sentences max]</span>

**Self-assessment:**

- Clarity score (1-10): <span class="fill-in">___</span>
- Could your explanation help someone make a real architectural decision? <span class="fill-in">[Yes/No]</span>
- Did you explain the fundamental trade-off (write vs read performance)? <span class="fill-in">[Yes/No]</span>

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw how B+Tree and LSM Tree handle the same write operation, showing the structural differences.

**Draw both approaches for inserting key=42:**

```
B+Tree Insert (key=42):
Step 1: [Your drawing - show tree structure]
        _________________________________

Step 2: [Show traversal from root to leaf]
        _________________________________

Step 3: [Show potential node split if needed]
        _________________________________


LSM Tree Insert (key=42):
Step 1: [Your drawing - show MemTable]
        _________________________________

Step 2: [Show what happens at flush threshold]
        _________________________________

Step 3: [Show SSTable creation]
        _________________________________
```

**Verification:**

- [ ] Drew B+Tree structure correctly (internal nodes vs leaf nodes)
- [ ] Showed leaf linking in B+Tree
- [ ] Drew LSM architecture (MemTable + SSTables)
- [ ] Explained why LSM is faster for this operation

---

### Gate 3: Pattern Recognition Test

**Without looking at your notes, classify these workloads:**

| Workload                                          | Best Engine (B+Tree/LSM)               | Why?                                   |
|---------------------------------------------------|----------------------------------------|----------------------------------------|
| Time-series sensor data (high write rate)         | <span class="fill-in">[Fill in]</span> | <span class="fill-in">[Explain]</span> |
| Banking transactions (needs consistency)          | <span class="fill-in">[Fill in]</span> | <span class="fill-in">[Explain]</span> |
| Analytics with date range queries                 | <span class="fill-in">[Fill in]</span> | <span class="fill-in">[Explain]</span> |
| Social media feeds (mostly recent reads)          | <span class="fill-in">[Fill in]</span> | <span class="fill-in">[Explain]</span> |
| Key-value cache (50/50 read/write)                | <span class="fill-in">[Fill in]</span> | <span class="fill-in">[Explain]</span> |
| Inventory system (read-heavy, occasional updates) | <span class="fill-in">[Fill in]</span> | <span class="fill-in">[Explain]</span> |

**Score:** ___/6 correct

If you scored below 5/6, review the decision framework and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Operation       | B+Tree | LSM Tree | Why Different?                                                      |
|-----------------|--------|----------|---------------------------------------------------------------------|
| Insert (single) | O(?)   | O(?)     | <span class="fill-in">[Explain]</span>                              |
| Search (single) | O(?)   | O(?)     | <span class="fill-in">[Explain]</span>                              |
| Range query     | O(?)   | O(?)     | <span class="fill-in">[Explain]</span>                              |
| Compaction      | N/A    | O(?)     | <span class="fill-in">[Explain why B+Tree doesn't need this]</span> |

**Deep questions:**

1. **Why is LSM Tree write O(log M) instead of O(log N)?**
    - Your answer: <span class="fill-in">[Fill in - explain M vs N]</span>

2. **What is "write amplification" and which engine has more?**
    - Your answer: <span class="fill-in">[Fill in - define and compare]</span>

3. **What is "read amplification" and which engine has more?**
    - Your answer: <span class="fill-in">[Fill in - define and compare]</span>

---

### Gate 5: Trade-off Decision

**Scenario:** You're designing storage for a monitoring system that collects 1M metrics/second. Reads are infrequent (
only for dashboards and alerts).

**Option A:** B+Tree

- Write cost: <span class="fill-in">[Fill in - operations per insert]</span>
- Read cost: <span class="fill-in">[Fill in]</span>
- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>

**Option B:** LSM Tree

- Write cost: <span class="fill-in">[Fill in - operations per insert]</span>
- Read cost: <span class="fill-in">[Fill in]</span>
- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>

**Your decision:** I would choose <span class="fill-in">[A/B]</span> because...

<span class="fill-in">[Fill in your reasoning - consider write volume, read patterns, and compaction strategy]</span>

**What would make you change your decision?**

- Scenario change 1: <span class="fill-in">[Fill in - what if reads increased 100x?]</span>
- Scenario change 2: <span class="fill-in">[Fill in - what if range queries became critical?]</span>

---

### Gate 6: Implementation from Memory (Final Test)

**Set a 15-minute timer. Implement the core operations without looking at notes:**

```java
/**
 * Implement: LSM Tree put and get operations
 */
public class LSMTree<K extends Comparable<K>, V> {
    private TreeMap<K, V> memTable;
    private List<SSTable<K, V>> sstables;
    private final int memTableSize;

    // Your implementation here:

    public void put(K key, V value) {
        // TODO: Implement




    }

    public V get(K key) {
        // TODO: Implement




        return null;
    }

    private void flush() {
        // TODO: Implement



    }
}
```

**After implementing, test with:**

- Insert 150 keys with memTableSize=50
- Expected: 3 SSTables created
- Read key that exists in MemTable
- Read key that exists in oldest SSTable

**Verification:**

- [ ] Implemented put() correctly with flush logic
- [ ] Implemented get() to check MemTable then SSTables
- [ ] Handles flush threshold correctly
- [ ] Returns correct values after flush

---

### Gate 7: Architectural Reasoning

**The ultimate test: Design a hybrid approach.**

**Task:** Design a storage engine that combines B+Tree and LSM Tree advantages.

Your design:

```
Hybrid Storage Engine:

Component 1: <span class="fill-in">[What would you use for writes?]</span>
    - Structure: <span class="fill-in">[Describe]</span>
    - Purpose: <span class="fill-in">[Why this choice?]</span>

Component 2: <span class="fill-in">[What would you use for reads?]</span>
    - Structure: <span class="fill-in">[Describe]</span>
    - Purpose: <span class="fill-in">[Why this choice?]</span>

Background Process: <span class="fill-in">[What maintains the system?]</span>
    - Frequency: <span class="fill-in">[How often?]</span>
    - Operation: <span class="fill-in">[What does it do?]</span>

Trade-offs: <span class="fill-in">[What did you sacrifice? What did you gain?]</span>
```

**Real-world comparison:**

- Does your design resemble any real database? <span class="fill-in">[Research: RocksDB, WiredTiger, LevelDB]</span>
- What did you discover? <span class="fill-in">[Fill in after researching]</span>

---

### Gate 8: Bug Prevention Checklist

**From your debugging experience, create a checklist for code reviews:**

**B+Tree Implementation Checklist:**

- [ ] <span class="fill-in">[Fill in - node split edge cases]</span>
- [ ] <span class="fill-in">[Fill in - leaf linking]</span>
- [ ] <span class="fill-in">[Fill in - boundary conditions in search]</span>
- [ ] <span class="fill-in">[Fill in]</span>

**LSM Tree Implementation Checklist:**

- [ ] <span class="fill-in">[Fill in - MemTable clearing after flush]</span>
- [ ] <span class="fill-in">[Fill in - SSTable iteration order in compaction]</span>
- [ ] <span class="fill-in">[Fill in - read amplification mitigation]</span>
- [ ] <span class="fill-in">[Fill in]</span>

**General Storage Engine Checklist:**

- [ ] <span class="fill-in">[Fill in]</span>
- [ ] <span class="fill-in">[Fill in]</span>

---

### Gate 9: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain write amplification to someone who has never heard of it.

Your explanation:

> "Write amplification happens when..."
>
> <span class="fill-in">[Fill in - use an analogy, then explain the technical concept]</span>

**Examples you would use:**

1. <span class="fill-in">[Real-world analogy]</span>
2. <span class="fill-in">[B+Tree example with numbers]</span>
3. <span class="fill-in">[LSM Tree example with numbers]</span>

**Why it matters:**

- Impact on SSD lifetime: <span class="fill-in">[Explain]</span>
- Impact on performance: <span class="fill-in">[Explain]</span>

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement B+Tree insert, search, and range query from memory
- [ ] Implement LSM Tree put, get, flush, and compact from memory
- [ ] Explain when and why to use each storage engine
- [ ] Identify the correct engine for new workloads without hesitation
- [ ] Analyze write and read amplification
- [ ] Debug common storage engine bugs
- [ ] Design compaction strategies for LSM Trees
- [ ] Teach these concepts to someone else

**Self-assessment score:** ___/10

**Benchmark results completed:**

- Write performance ratio (LSM vs B+Tree): <span class="fill-in">___</span>x faster
- Read performance ratio (B+Tree vs LSM): <span class="fill-in">___</span>x faster
- Understood why: <span class="fill-in">[Yes/No]</span>

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered storage engines. Proceed to the next topic.
