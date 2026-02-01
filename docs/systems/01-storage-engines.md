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

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **B+Tree insert operation:**
    - Time complexity: _[Your guess: O(?)]_
    - Verified after implementation: _[Actual: O(?)]_

2. **LSM Tree write operation (to MemTable):**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified: _[Actual]_

3. **Performance calculation:**
    - For 100,000 writes, B+Tree = _____ operations (if log base is 10)
    - For 100,000 writes, LSM Tree = _____ operations (before flush)
    - Speedup factor for writes: LSM is approximately _____ times faster

### Scenario Predictions

**Scenario 1:** Time-series metrics database (1M writes/second, rare reads)

- **Best storage engine?** _[B+Tree/LSM Tree - Why?]_
- **Key consideration:** _[Write amplification/Read speed/Range queries?]_
- **Why this choice?** _[Fill in your reasoning]_

**Scenario 2:** E-commerce inventory system (100k reads/sec, 5k writes/sec)

- **Best storage engine?** _[B+Tree/LSM Tree - Why?]_
- **What pattern benefits most?** _[Point lookups/Range scans/Random writes?]_

**Scenario 3:** Social media analytics (read historical posts by date range)

- **Which handles range queries better?** _[B+Tree/LSM Tree - Why?]_
- **Key data structure feature:** _[Linked leaves/Sorted SSTables?]_

### Trade-off Quiz

**Question:** When would B+Tree be BETTER than LSM Tree despite slower writes?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after benchmarking]_

**Question:** What's the MAIN advantage of LSM Trees for writes?

- [ ] No tree balancing required on each write
- [ ] Better space efficiency
- [ ] Faster range queries
- [ ] Lower read amplification

Verify after implementation: _[Which one(s)?]_

**Question:** What happens if you never compact an LSM Tree?

- Your prediction: _[What problem occurs?]_
- Verified: _[Fill in after testing]_

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
|----------------|---------------------|----------------------|---------------|
| N = 1,000      | ~10,000 ops         | ~2,000 ops           | 5x faster     |
| N = 10,000     | ~130,000 ops        | ~20,000 ops          | 6.5x faster   |
| N = 100,000    | ~1,700,000 ops      | ~200,000 ops         | 8.5x faster   |

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

- _[Why does B+Tree require more writes per operation?]_
- _[How does LSM Tree achieve better write throughput?]_
- _[What's the trade-off for read performance?]_

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
|---------------|-------------------|------------------------|------------------|
| K = 1         | ~13 ops/read      | ~13 ops/read           | ~1x (equal)      |
| K = 5         | ~13 ops/read      | ~65 ops/read           | 5x faster        |
| K = 10        | ~13 ops/read      | ~130 ops/read          | 10x faster       |

**Your calculation:** With 20 SSTables, B+Tree is approximately _____ times faster for reads.

**Key insight:** This is why LSM Trees need **compaction** - to reduce SSTable count!

**After benchmarking, fill in:**

- _[What happens to LSM read performance as SSTables accumulate?]_
- _[Why doesn't B+Tree have this problem?]_
- _[How does compaction help LSM Trees?]_

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

- **Bug 1 location:** _[Which lines?]_
- **Bug 1 explanation:** _[What happens when you remove while iterating?]_
- **Bug 1 fix:** _[How to correctly remove elements?]_

- **Bug 2 location:** _[What's missing?]_
- **Bug 2 explanation:** _[Why must leaf nodes be linked?]_
- **Bug 2 fix:** _[Write the correct code]_

- **Bug 3 location:** _[Which line?]_
- **Bug 3 explanation:** _[What if leaf.parent is null?]_
- **Bug 3 fix:** _[How to handle root split?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Lines 13-16):** Removing elements while iterating forward breaks indices. Each removal shifts remaining elements left.

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

- **Bug 1:** _[What's wrong with iterating newest to oldest?]_
- **Bug 1 explanation:** _[Which value should win for duplicate keys?]_
- **Bug 1 test case:** Insert key=5 with "Old", then update to "New". After compaction, what do you get?
- **Bug 1 fix:** _[Correct the iteration order]_

- **Bug 2:** _[What's wrong with line 18?]_
- **Bug 2 explanation:** _[What happens to old SSTables?]_
- **Bug 2 fix:** _[Write the correct code]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 9):** Iterating from newest to oldest means older values overwrite newer ones!

LSM Trees must keep the NEWEST value for each key. By iterating newest-to-oldest and using `put()`, when we encounter the key again in an older SSTable, it overwrites the newer value.

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

- **Bug location:** _[Which lines?]_
- **Bug explanation:** _[What happens with keys equal to internal node keys?]_
- **Test case:** Tree has keys [10, 20, 30]. Search for key=20. Which child do you visit?
- **Bug fix:** _[Should comparison be >= or >?]_

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

- **Bug location:** _[What's missing in flush()?]_
- **Bug explanation:** _[What happens to MemTable after flush?]_
- **Test case:** Insert 250 items. How many times does flush() run? How many items in memTable?
- **Expected:** MemTable has 50 items after 250 inserts (flushed 200)
- **Actual:** _[What really happens?]_
- **Bug fix:** _[Write the missing code]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (After line 20):** We never clear the MemTable after flushing!

**Result:** MemTable keeps growing forever. After first flush, memTable has 100 items. After second flush, it has 200. Eventually OutOfMemoryError.

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

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[What's missing from the range check?]_
- **Test case:** Tree has keys [1,3,5,7,9,11,13,15]. rangeQuery(5, 10). Expected: [5,7,9]. Actual: _[What?]_
- **Bug fix:** _[Add missing condition]_

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

1. _[Index manipulation while iterating]_
2. _[Missing pointer updates in tree structures]_
3. _[Wrong iteration order in merge operations]_
4. _[Forgetting to clear/reset data structures]_
5. _[Incomplete boundary checks in range queries]_

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

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Database Engineer

**Scenario:** A database engineer asks you about storage engines for a new service.

**Your explanation (write it out):**

> "B+Trees and LSM Trees are two fundamental storage engine architectures..."
>
> _[Fill in your explanation in plain English - 4-5 sentences max]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation help someone make a real architectural decision? _[Yes/No]_
- Did you explain the fundamental trade-off (write vs read performance)? _[Yes/No]_

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

| Workload | Best Engine (B+Tree/LSM) | Why? |
|----------|-------------------------|------|
| Time-series sensor data (high write rate) | _[Fill in]_ | _[Explain]_ |
| Banking transactions (needs consistency) | _[Fill in]_ | _[Explain]_ |
| Analytics with date range queries | _[Fill in]_ | _[Explain]_ |
| Social media feeds (mostly recent reads) | _[Fill in]_ | _[Explain]_ |
| Key-value cache (50/50 read/write) | _[Fill in]_ | _[Explain]_ |
| Inventory system (read-heavy, occasional updates) | _[Fill in]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the decision framework and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Operation | B+Tree | LSM Tree | Why Different? |
|-----------|--------|----------|----------------|
| Insert (single) | O(?) | O(?) | _[Explain]_ |
| Search (single) | O(?) | O(?) | _[Explain]_ |
| Range query | O(?) | O(?) | _[Explain]_ |
| Compaction | N/A | O(?) | _[Explain why B+Tree doesn't need this]_ |

**Deep questions:**

1. **Why is LSM Tree write O(log M) instead of O(log N)?**
    - Your answer: _[Fill in - explain M vs N]_

2. **What is "write amplification" and which engine has more?**
    - Your answer: _[Fill in - define and compare]_

3. **What is "read amplification" and which engine has more?**
    - Your answer: _[Fill in - define and compare]_

---

### Gate 5: Trade-off Decision

**Scenario:** You're designing storage for a monitoring system that collects 1M metrics/second. Reads are infrequent (only for dashboards and alerts).

**Option A:** B+Tree
- Write cost: _[Fill in - operations per insert]_
- Read cost: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option B:** LSM Tree
- Write cost: _[Fill in - operations per insert]_
- Read cost: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Your decision:** I would choose _[A/B]_ because...

_[Fill in your reasoning - consider write volume, read patterns, and compaction strategy]_

**What would make you change your decision?**

- Scenario change 1: _[Fill in - what if reads increased 100x?]_
- Scenario change 2: _[Fill in - what if range queries became critical?]_

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

Component 1: _[What would you use for writes?]_
    - Structure: _[Describe]_
    - Purpose: _[Why this choice?]_

Component 2: _[What would you use for reads?]_
    - Structure: _[Describe]_
    - Purpose: _[Why this choice?]_

Background Process: _[What maintains the system?]_
    - Frequency: _[How often?]_
    - Operation: _[What does it do?]_

Trade-offs: _[What did you sacrifice? What did you gain?]_
```

**Real-world comparison:**

- Does your design resemble any real database? _[Research: RocksDB, WiredTiger, LevelDB]_
- What did you discover? _[Fill in after researching]_

---

### Gate 8: Bug Prevention Checklist

**From your debugging experience, create a checklist for code reviews:**

**B+Tree Implementation Checklist:**

- [ ] _[Fill in - node split edge cases]_
- [ ] _[Fill in - leaf linking]_
- [ ] _[Fill in - boundary conditions in search]_
- [ ] _[Fill in]_

**LSM Tree Implementation Checklist:**

- [ ] _[Fill in - MemTable clearing after flush]_
- [ ] _[Fill in - SSTable iteration order in compaction]_
- [ ] _[Fill in - read amplification mitigation]_
- [ ] _[Fill in]_

**General Storage Engine Checklist:**

- [ ] _[Fill in]_
- [ ] _[Fill in]_

---

### Gate 9: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain write amplification to someone who has never heard of it.

Your explanation:

> "Write amplification happens when..."
>
> _[Fill in - use an analogy, then explain the technical concept]_

**Examples you would use:**

1. _[Real-world analogy]_
2. _[B+Tree example with numbers]_
3. _[LSM Tree example with numbers]_

**Why it matters:**

- Impact on SSD lifetime: _[Explain]_
- Impact on performance: _[Explain]_

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

- Write performance ratio (LSM vs B+Tree): ___x faster
- Read performance ratio (B+Tree vs LSM): ___x faster
- Understood why: _[Yes/No]_

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered storage engines. Proceed to the next topic.
