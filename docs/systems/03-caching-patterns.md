# 03. Caching Patterns

> Master LRU, LFU, and write policies for high-performance systems

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is caching in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **Why/when do we use caching?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
    - Example: "A cache is like keeping your favorite books on your desk instead of walking to the library..."
    - Your analogy: _[Fill in]_

4. **What's the difference between LRU and LFU?**
    - Your answer: _[Fill in after solving problems]_

5. **When should you use Write-Through vs Write-Back?**
    - Your answer: _[Fill in after practice]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Direct database query for each request:**
    - Time per request: _[Your guess: O(?)]_
    - If DB query takes 50ms, how many requests/sec can you handle? _____
    - Verified after learning: _[Actual]_

2. **Cache lookup + occasional DB query:**
    - Cache hit time: _[Your guess: O(?)]_
    - Cache miss time: _[Your guess: O(?)]_
    - If 90% cache hit rate, average latency: _____ms
    - Verified: _[Actual]_

3. **Hit rate calculation:**
    - 1000 requests, 900 cache hits, 100 misses
    - Hit rate: _____%
    - If cache saves 45ms per hit, total time saved: _____ms

### Scenario Predictions

**Scenario 1:** E-commerce product catalog with access pattern:
```
Product A: accessed 5 times
Product B: accessed 10 times
Product C: accessed 3 times
Product D: accessed 8 times
Cache capacity: 2 items
```

- **With LRU, which items remain after all accesses?** _[Fill in - trace manually]_
- **With LFU, which items remain?** _[Fill in - trace manually]_
- **Which is better for this pattern?** _[LRU/LFU - Why?]_

**Scenario 2:** User session cache (last access time matters)
```
Session A: last accessed 10 min ago
Session B: last accessed 2 min ago
Session C: last accessed 5 min ago
Cache full, new session arrives
```

- **Which eviction policy makes sense?** _[LRU/LFU - Why?]_
- **Which session gets evicted?** _[Fill in]_

**Scenario 3:** Write policies
```
Request: Update user profile
Write-Through: Cache + DB both take 5ms each
Write-Back: Cache takes 1ms, DB flush happens later
```

- **Write-Through total latency:** _____ms
- **Write-Back perceived latency:** _____ms
- **If DB fails during Write-Back flush, what happens?** _[Fill in]_
- **Which is safer?** _[Fill in - Why?]_

### Trade-off Quiz

**Question 1:** When would direct database queries be BETTER than caching?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question 2:** What's the MAIN benefit of caching?

- [ ] Reduces database load
- [ ] Reduces latency
- [ ] Saves money
- [ ] All of the above

Verify after implementation: _[Which one(s)?]_

**Question 3:** Cache hit rate drops from 90% to 50%. How does this affect performance?

- Your calculation: _[Fill in]_
- Verified impact: _[Fill in after implementation]_

---

## Before/After: Why This Pattern Matters

**Your task:** Compare direct database access vs caching to understand the impact.

### Example: Product Lookup API

**Problem:** Fetch product details for 1000 concurrent users.

#### Approach 1: Direct Database Query (No Cache)

```java
// Naive approach - Query database for every request
public class DirectDatabaseLookup {

    private final Database database;

    public Product getProduct(String productId) {
        // Direct database query every time
        return database.query("SELECT * FROM products WHERE id = ?", productId);
    }
}

// Performance test
public static void benchmarkDirectDB() {
    DirectDatabaseLookup service = new DirectDatabaseLookup(database);

    long start = System.currentTimeMillis();
    for (int i = 0; i < 1000; i++) {
        service.getProduct("prod-123"); // Same product queried 1000 times
    }
    long end = System.currentTimeMillis();

    System.out.println("Total time: " + (end - start) + "ms");
}
```

**Analysis:**

- Time per DB query: ~50ms
- For 1000 requests: 1000 × 50ms = **50,000ms (50 seconds)**

- Database load: 1000 queries/sec
- Cost: High (database compute, network latency)
- Throughput: ~20 requests/sec per thread

#### Approach 2: LRU Cache (Optimized)

```java
// Optimized approach - Cache frequent lookups
public class CachedProductLookup {

    private final LRUCache<String, Product> cache;
    private final Database database;

    public CachedProductLookup(int cacheSize, Database database) {
        this.cache = new LRUCache<>(cacheSize);
        this.database = database;
    }

    public Product getProduct(String productId) {
        // Try cache first (O(1), ~1ms)
        Product product = cache.get(productId);

        if (product != null) {
            return product; // Cache hit - fast!
        }

        // Cache miss - query database (~50ms)
        product = database.query("SELECT * FROM products WHERE id = ?", productId);

        if (product != null) {
            cache.put(productId, product); // Populate cache
        }

        return product;
    }
}

// Performance test
public static void benchmarkCached() {
    CachedProductLookup service = new CachedProductLookup(100, database);

    long start = System.currentTimeMillis();
    for (int i = 0; i < 1000; i++) {
        service.getProduct("prod-123"); // Same product queried 1000 times
    }
    long end = System.currentTimeMillis();

    System.out.println("Total time: " + (end - start) + "ms");
}
```

**Analysis:**

- First request (cache miss): ~50ms
- Subsequent requests (cache hits): ~1ms each
- For 1000 requests: 50ms + (999 × 1ms) = **1,049ms (~1 second)**

- Database load: 1 query for 1000 requests
- Cache hit rate: 99.9%
- Throughput: ~950 requests/sec per thread

#### Performance Comparison

| Metric | Direct DB | With Cache | Improvement |
|--------|-----------|------------|-------------|
| **Total time (1000 requests)** | 50,000ms | 1,049ms | **47.6x faster** |
| **Average latency** | 50ms | 1.05ms | **47.6x faster** |
| **Database queries** | 1000 | 1 | **99.9% reduction** |
| **Throughput** | 20 req/sec | 950 req/sec | **47.5x higher** |
| **DB cost (estimate)** | $100/day | $2/day | **$98/day savings** |

**Your calculation:** For 10,000 requests with 90% cache hit rate:
- Cache hits: 10,000 × 0.9 = _____ requests × 1ms = _____ms
- Cache misses: 10,000 × 0.1 = _____ requests × 50ms = _____ms
- Total time: _____ + _____ = _____ms
- Speedup vs direct DB: _____ times faster

#### Hit Rate Analysis

**How hit rate affects performance:**

```
Cache Hit Rate Analysis (1000 requests)

Hit Rate | Cache Hits | DB Queries | Total Time | Speedup
---------|------------|------------|------------|--------
   0%    |     0      |    1000    |  50,000ms  |   1x
  50%    |   500      |     500    |  25,500ms  |   2x
  75%    |   750      |     250    |  13,250ms  |   3.8x
  90%    |   900      |     100    |   5,900ms  |   8.5x
  95%    |   950      |      50    |   3,450ms  |  14.5x
  99%    |   990      |      10    |   1,490ms  |  33.6x
 99.9%   |   999      |      1     |   1,049ms  |  47.7x
```

**Key insight:** Even a modest 75% hit rate gives 3.8x speedup!

**After implementing, explain in your own words:**

- _[Why does caching provide such dramatic speedup?]_
- _[What happens when hit rate drops below 50%?]_
- _[When might caching not be worth it?]_

#### Write Policy Comparison

**Scenario:** Update user profile (name change)

```java
// Write-Through Example
public void updateUserProfile_WriteThrough(String userId, String newName) {
    long start = System.currentTimeMillis();

    // Write to cache (1ms)
    cache.put(userId, newName);

    // Write to database (50ms) - BLOCKS until complete
    database.update(userId, newName);

    long end = System.currentTimeMillis();
    System.out.println("Write-Through latency: " + (end - start) + "ms");
    // Output: ~51ms
}

// Write-Back Example
public void updateUserProfile_WriteBack(String userId, String newName) {
    long start = System.currentTimeMillis();

    // Write to cache (1ms) - IMMEDIATE RETURN
    cache.put(userId, newName);

    // Mark as dirty for async flush
    dirtyEntries.put(userId, newName);

    long end = System.currentTimeMillis();
    System.out.println("Write-Back latency: " + (end - start) + "ms");
    // Output: ~1ms

    // Database write happens later asynchronously
}
```

**Write Policy Performance:**

| Policy | User Latency | DB Write | Consistency | Data Loss Risk |
|--------|--------------|----------|-------------|----------------|
| Write-Through | 51ms | Synchronous | Immediate | None |
| Write-Back | 1ms | Asynchronous | Eventual | If crash before flush |
| **Speedup** | **51x faster** | - | Trade-off | Trade-off |

**Your analysis:** When would you choose each?
- Write-Through: _[Fill in scenarios]_
- Write-Back: _[Fill in scenarios]_

---

## Core Implementation

### Pattern 1: LRU Cache (Least Recently Used)

**Your task:** Implement LRU cache with O(1) get and put operations.

```java
import java.util.*;

/**
 * LRU Cache - Evicts least recently used items when full
 * Time: O(1) for get/put
 * Space: O(capacity)
 *
 * Key insight: Combine HashMap for O(1) lookup + Doubly Linked List for O(1) move/remove
 */
public class LRUCache<K, V> {

    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final DoublyLinkedList<K, V> list;

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    static class DoublyLinkedList<K, V> {
        Node<K, V> head, tail;

        DoublyLinkedList() {
            // TODO: Initialize dummy head and tail
            //   head = new Node<>(null, null);
            //   tail = new Node<>(null, null);
            //   head.next = tail;
            //   tail.prev = head;
        }

        /**
         * Add node to front (most recently used position)
         *
         * TODO: Implement addToFront
         * - Insert node right after head
         * - Update node.next, node.prev
         * - Update head.next.prev and head.next
         */
        void addToFront(Node<K, V> node) {
            // TODO: Insert after head
            //   node.next = head.next;
            //   node.prev = head;
            //   head.next.prev = node;
            //   head.next = node;
        }

        /**
         * Remove node from list
         *
         * TODO: Implement remove
         * - Update prev and next pointers to bypass node
         */
        void remove(Node<K, V> node) {
            // TODO: Bypass node
            //   node.prev.next = node.next;
            //   node.next.prev = node.prev;
        }

        /**
         * Remove and return least recently used (node before tail)
         *
         * TODO: Implement removeLast
         * - Check if list is empty (tail.prev == head)
         * - Remove tail.prev
         * - Return removed node
         */
        Node<K, V> removeLast() {
            // TODO: Remove LRU node
            //   if (tail.prev == head) return null;
            //   Node<K, V> last = tail.prev;
            //   remove(last);
            //   return last;

            return null; // Replace
        }

        /**
         * Move existing node to front
         *
         * TODO: Implement moveToFront
         * - Remove node from current position
         * - Add to front
         */
        void moveToFront(Node<K, V> node) {
            // TODO: Move to front
            //   remove(node);
            //   addToFront(node);
        }
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.list = new DoublyLinkedList<>();
    }

    /**
     * Get value for key
     * Time: O(1)
     *
     * TODO: Implement get
     * - Lookup in cache HashMap
     * - If found, move to front (mark as recently used)
     * - Return value or null
     */
    public V get(K key) {
        // TODO: Lookup in cache
        //   Node<K, V> node = cache.get(key);
        //   if (node == null) return null;

        // TODO: Move to front (most recently used)
        //   list.moveToFront(node);
        //   return node.value;

        return null; // Replace
    }

    /**
     * Put key-value pair
     * Time: O(1)
     *
     * TODO: Implement put
     * 1. If key exists, update value and move to front
     * 2. If key is new:
     *    - Check if at capacity, evict LRU if needed
     *    - Create new node
     *    - Add to front of list
     *    - Add to cache HashMap
     */
    public void put(K key, V value) {
        // TODO: Check if key exists
        //   Node<K, V> node = cache.get(key);

        // TODO: If exists, update and move to front
        //   if (node != null) {
        //     node.value = value;
        //     list.moveToFront(node);
        //     return;
        //   }

        // TODO: Check capacity and evict if needed
        //   if (cache.size() >= capacity) {
        //     Node<K, V> lru = list.removeLast();
        //     if (lru != null) cache.remove(lru.key);
        //   }

        // TODO: Add new node
        //   Node<K, V> newNode = new Node<>(key, value);
        //   list.addToFront(newNode);
        //   cache.put(key, newNode);
    }

    public int size() {
        return cache.size();
    }
}
```

---

### Pattern 2: LFU Cache (Least Frequently Used)

**Your task:** Implement LFU cache with O(1) get and put operations.

```java
import java.util.*;

/**
 * LFU Cache - Evicts least frequently used items when full
 * Time: O(1) for get/put
 * Space: O(capacity)
 *
 * Key insight: Track frequency for each node, maintain lists per frequency level
 */
public class LFUCache<K, V> {

    private final int capacity;
    private int minFreq;
    private final Map<K, Node<K, V>> cache;
    private final Map<Integer, DoublyLinkedList<K, V>> freqMap; // freq -> list of nodes

    static class Node<K, V> {
        K key;
        V value;
        int freq;
        Node<K, V> prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    static class DoublyLinkedList<K, V> {
        Node<K, V> head, tail;

        DoublyLinkedList() {
            // TODO: Same as LRU - initialize dummy nodes
        }

        void addToFront(Node<K, V> node) {
            // TODO: Same as LRU
        }

        void remove(Node<K, V> node) {
            // TODO: Same as LRU
        }

        Node<K, V> removeLast() {
            // TODO: Same as LRU
            return null;
        }

        boolean isEmpty() {
            // TODO: Check if list is empty
            //   return head.next == tail;
            return true;
        }
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    /**
     * Get value for key
     * Time: O(1)
     *
     * TODO: Implement get
     * - Lookup in cache
     * - If found, update frequency (move to next freq list)
     * - Return value
     */
    public V get(K key) {
        // TODO: Lookup node
        //   Node<K, V> node = cache.get(key);
        //   if (node == null) return null;

        // TODO: Update frequency
        //   updateFrequency(node);
        //   return node.value;

        return null; // Replace
    }

    /**
     * Put key-value pair
     * Time: O(1)
     *
     * TODO: Implement put
     * 1. If key exists, update value and frequency
     * 2. If key is new:
     *    - Check capacity, evict LFU if needed
     *    - Create node with freq=1
     *    - Add to freq=1 list
     *    - Set minFreq=1
     */
    public void put(K key, V value) {
        if (capacity <= 0) return;

        // TODO: Check if key exists
        //   Node<K, V> node = cache.get(key);

        // TODO: If exists, update
        //   if (node != null) {
        //     node.value = value;
        //     updateFrequency(node);
        //     return;
        //   }

        // TODO: Evict if at capacity
        //   if (cache.size() >= capacity) {
        //     DoublyLinkedList<K, V> minFreqList = freqMap.get(minFreq);
        //     Node<K, V> lfu = minFreqList.removeLast();
        //     if (lfu != null) cache.remove(lfu.key);
        //   }

        // TODO: Add new node
        //   Node<K, V> newNode = new Node<>(key, value);
        //   cache.put(key, newNode);
        //   minFreq = 1;
        //   freqMap.computeIfAbsent(1, k -> new DoublyLinkedList<>())
        //          .addToFront(newNode);
    }

    /**
     * Update frequency of node
     *
     * TODO: Implement updateFrequency
     * 1. Remove node from current frequency list
     * 2. If that was the only node at minFreq, increment minFreq
     * 3. Increment node.freq
     * 4. Add node to new frequency list
     */
    private void updateFrequency(Node<K, V> node) {
        // TODO: Get current frequency list
        //   int freq = node.freq;
        //   DoublyLinkedList<K, V> list = freqMap.get(freq);

        // TODO: Remove from current list
        //   list.remove(node);

        // TODO: Update minFreq if needed
        //   if (freq == minFreq && list.isEmpty()) {
        //     minFreq++;
        //   }

        // TODO: Increment frequency and add to new list
        //   node.freq++;
        //   freqMap.computeIfAbsent(node.freq, k -> new DoublyLinkedList<>())
        //          .addToFront(node);
    }
}
```

---

### Pattern 3: Write-Through Cache

**Your task:** Implement write-through cache pattern.

```java
/**
 * Write-Through Cache - Writes go to cache AND database synchronously
 *
 * Pros: Data consistency, simple
 * Cons: Higher write latency
 */
public class WriteThroughCache<K, V> {

    private final LRUCache<K, V> cache;
    private final Database<K, V> database;

    interface Database<K, V> {
        V read(K key);
        void write(K key, V value);
    }

    public WriteThroughCache(int capacity, Database<K, V> database) {
        this.cache = new LRUCache<>(capacity);
        this.database = database;
    }

    /**
     * Get value
     *
     * TODO: Implement cache-aside pattern
     * 1. Try cache first
     * 2. On miss, read from database
     * 3. Populate cache
     * 4. Return value
     */
    public V get(K key) {
        // TODO: Try cache
        //   V value = cache.get(key);
        //   if (value != null) return value;

        // TODO: Cache miss - read from database
        //   value = database.read(key);
        //   if (value != null) {
        //     cache.put(key, value);
        //   }
        //   return value;

        return null; // Replace
    }

    /**
     * Put value
     *
     * TODO: Implement write-through
     * - Write to cache
     * - Write to database (synchronously)
     * - Both must succeed
     */
    public void put(K key, V value) {
        // TODO: Write to both
        //   cache.put(key, value);
        //   database.write(key, value);
    }
}
```

---

### Pattern 4: Write-Back (Write-Behind) Cache

**Your task:** Implement write-back cache with async flush.

```java
import java.util.*;
import java.util.concurrent.*;

/**
 * Write-Back Cache - Writes go to cache immediately, database asynchronously
 *
 * Pros: Lower write latency
 * Cons: Risk of data loss, more complex
 */
public class WriteBackCache<K, V> {

    private final LRUCache<K, V> cache;
    private final Database<K, V> database;
    private final Map<K, V> dirtyEntries;
    private final ScheduledExecutorService flusher;

    interface Database<K, V> {
        V read(K key);
        void write(K key, V value);
    }

    public WriteBackCache(int capacity, Database<K, V> database, long flushIntervalMs) {
        this.cache = new LRUCache<>(capacity);
        this.database = database;
        this.dirtyEntries = new ConcurrentHashMap<>();
        this.flusher = Executors.newSingleThreadScheduledExecutor();

        // TODO: Schedule periodic flush
        //   flusher.scheduleAtFixedRate(
        //     this::flushDirtyEntries,
        //     flushIntervalMs,
        //     flushIntervalMs,
        //     TimeUnit.MILLISECONDS
        //   );
    }

    /**
     * Get value
     *
     * TODO: Implement get
     * 1. Try cache
     * 2. Try dirty entries (not yet flushed)
     * 3. Try database
     */
    public V get(K key) {
        // TODO: Try cache first
        //   V value = cache.get(key);
        //   if (value != null) return value;

        // TODO: Check dirty entries
        //   value = dirtyEntries.get(key);
        //   if (value != null) return value;

        // TODO: Read from database
        //   value = database.read(key);
        //   if (value != null) cache.put(key, value);
        //   return value;

        return null; // Replace
    }

    /**
     * Put value
     *
     * TODO: Implement write-back
     * - Write to cache immediately (fast)
     * - Mark as dirty for later flush
     * - Don't wait for database write
     */
    public void put(K key, V value) {
        // TODO: Write to cache immediately
        //   cache.put(key, value);

        // TODO: Mark as dirty
        //   dirtyEntries.put(key, value);
    }

    /**
     * Flush dirty entries to database
     *
     * TODO: Implement flush
     * - For each dirty entry, write to database
     * - Handle failures (retry, log, etc.)
     * - Clear dirty entries on success
     */
    private void flushDirtyEntries() {
        // TODO: Flush all dirty entries
        //   for (Map.Entry<K, V> entry : dirtyEntries.entrySet()) {
        //     try {
        //       database.write(entry.getKey(), entry.getValue());
        //     } catch (Exception e) {
        //       System.err.println("Failed to flush: " + entry.getKey());
        //     }
        //   }
        //   dirtyEntries.clear();
    }

    public void shutdown() {
        // TODO: Final flush before shutdown
        //   flushDirtyEntries();
        //   flusher.shutdown();
    }
}
```

---

## Client Code

```java
import java.util.*;

public class CachingPatternsClient {

    // Mock database for testing
    static class MockDatabase<K, V> implements WriteThroughCache.Database<K, V> {
        private final Map<K, V> storage = new HashMap<>();

        @Override
        public V read(K key) {
            System.out.println("  [DB READ] " + key);
            return storage.get(key);
        }

        @Override
        public void write(K key, V value) {
            System.out.println("  [DB WRITE] " + key + " = " + value);
            storage.put(key, value);
        }
    }

    public static void main(String[] args) {
        testLRUCache();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testLFUCache();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testWriteThroughCache();
    }

    static void testLRUCache() {
        System.out.println("=== LRU Cache Test ===\n");
        LRUCache<String, String> cache = new LRUCache<>(3);

        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");
        System.out.println("Cache size: " + cache.size());

        cache.get("user:1"); // Access Alice (makes it most recent)

        cache.put("user:4", "David"); // Should evict Bob (LRU)

        System.out.println("Get user:1: " + cache.get("user:1")); // Should be Alice
        System.out.println("Get user:2: " + cache.get("user:2")); // Should be null (evicted)
        System.out.println("Get user:3: " + cache.get("user:3")); // Should be Charlie
        System.out.println("Get user:4: " + cache.get("user:4")); // Should be David
    }

    static void testLFUCache() {
        System.out.println("=== LFU Cache Test ===\n");
        LFUCache<String, String> cache = new LFUCache<>(2);

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println("Get key1: " + cache.get("key1")); // freq: key1=2, key2=1

        cache.put("key3", "value3"); // Should evict key2 (LFU)

        System.out.println("Get key2: " + cache.get("key2")); // Should be null (evicted)
        System.out.println("Get key3: " + cache.get("key3")); // Should be value3
        System.out.println("Get key1: " + cache.get("key1")); // Should be value1
    }

    static void testWriteThroughCache() {
        System.out.println("=== Write-Through Cache Test ===\n");
        MockDatabase<String, String> db = new MockDatabase<>();
        WriteThroughCache<String, String> cache = new WriteThroughCache<>(2, db);

        System.out.println("Put user:1");
        cache.put("user:1", "Alice"); // Writes to both cache and DB

        System.out.println("\nGet user:1");
        System.out.println("Value: " + cache.get("user:1")); // Cache hit (no DB read)

        System.out.println("\nGet user:2");
        db.storage.put("user:2", "Bob"); // Add directly to DB
        System.out.println("Value: " + cache.get("user:2")); // Cache miss, DB hit
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken caching implementations. This tests your understanding.

### Challenge 1: Cache Stampede Bug

```java
/**
 * This cache has a CRITICAL BUG during cache misses.
 * Multiple threads can cause "cache stampede" - all hit DB simultaneously!
 */
public class StampedeLRUCache<K, V> {

    private final LRUCache<K, V> cache;
    private final Database<K, V> database;

    public V get(K key) {
        V value = cache.get(key);

        if (value == null) {
            // BUG: No synchronization!
            // If 1000 threads miss cache at same time,
            // all 1000 will query database for same key!
            value = database.read(key);

            if (value != null) {
                cache.put(key, value);
            }
        }

        return value;
    }
}
```

**Your debugging:**

- **Bug location:** _[Which lines?]_
- **Bug explanation:** _[What happens with concurrent requests?]_
- **Bug impact:** _[How does this affect database?]_
- **Fix approach 1 (Locking):** _[How to fix with synchronized?]_
- **Fix approach 2 (Better):** _[How to fix with double-checked locking or other pattern?]_

**Test case to expose the bug:**
```java
// Simulate 1000 concurrent requests for same key (cache miss)
ExecutorService executor = Executors.newFixedThreadPool(1000);
for (int i = 0; i < 1000; i++) {
    executor.submit(() -> cache.get("popular-item"));
}
// Expected: 1 DB query
// Actual with bug: _____ DB queries
```

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** No synchronization during cache miss. Multiple threads can simultaneously detect cache miss and all query the database.

**Fix 1 - Simple locking (but blocks all reads):**
```java
public synchronized V get(K key) {
    // ... same logic
}
```

**Fix 2 - Better: Per-key locking to avoid thundering herd:**
```java
private final ConcurrentHashMap<K, CompletableFuture<V>> inFlightRequests = new ConcurrentHashMap<>();

public V get(K key) {
    V value = cache.get(key);
    if (value != null) return value;

    // Only one thread per key will query DB
    CompletableFuture<V> future = inFlightRequests.computeIfAbsent(key, k -> {
        return CompletableFuture.supplyAsync(() -> {
            V dbValue = database.read(k);
            if (dbValue != null) cache.put(k, dbValue);
            return dbValue;
        });
    });

    try {
        value = future.get();
        inFlightRequests.remove(key);
        return value;
    } catch (Exception e) {
        inFlightRequests.remove(key);
        throw new RuntimeException(e);
    }
}
```

**Key insight:** Cache stampede can overwhelm your database. Always protect cache misses with per-key synchronization.
</details>

---

### Challenge 2: Stale Data Bug

```java
/**
 * This write-back cache has a STALE DATA bug.
 * Readers can get old data even after a write!
 */
public class StaleWriteBackCache<K, V> {

    private final LRUCache<K, V> cache;
    private final Map<K, V> dirtyEntries;
    private final Database<K, V> database;

    public V get(K key) {
        // BUG: Wrong order of checks!
        V value = cache.get(key);
        if (value != null) return value;

        // Check dirty entries
        value = dirtyEntries.get(key);
        if (value != null) return value;

        // Read from database
        return database.read(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
        dirtyEntries.put(key, value);
    }

    public void flush() {
        // Async flush to database
        for (Map.Entry<K, V> entry : dirtyEntries.entrySet()) {
            database.write(entry.getKey(), entry.getValue());
        }
        dirtyEntries.clear();
    }
}
```

**Your debugging:**

- **Bug:** _[What's wrong with the get() logic?]_
- **Scenario that breaks:**
```
1. put("key1", "value1") - goes to cache and dirty
2. cache evicts key1 (capacity full)
3. get("key1") - what do you get?
```
- **Expected:** "value1" (from dirty entries)
- **Actual:** _[What happens?]_
- **Fix:** _[Correct the order of checks]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Cache lookup happens before dirty entries check. If cache evicts an item that's in dirtyEntries, we'll miss the latest value.

**Correct order:**
```java
public V get(K key) {
    // Check dirty entries FIRST (most recent writes)
    V value = dirtyEntries.get(key);
    if (value != null) return value;

    // Then check cache
    value = cache.get(key);
    if (value != null) return value;

    // Finally, check database
    return database.read(key);
}
```

**Key insight:** With write-back caching, dirty entries hold the "source of truth" until flushed. Always check them first!
</details>

---

### Challenge 3: Thundering Herd on Expiration

```java
/**
 * This cache has TTL support but causes "thundering herd"
 * when many items expire at the same time.
 */
public class TTLCache<K, V> {

    static class CacheEntry<V> {
        V value;
        long expiryTime;

        CacheEntry(V value, long ttlMs) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttlMs;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final Database<K, V> database;
    private final long ttlMs;

    public TTLCache(Database<K, V> database, long ttlMs) {
        this.database = database;
        this.ttlMs = ttlMs;
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);

        // Check expiration
        if (entry == null || entry.isExpired()) {
            // BUG: At 10:00 AM, 1000 items all expire at once
            // All requests hit database simultaneously!
            V value = database.read(key);
            if (value != null) {
                cache.put(key, new CacheEntry<>(value, ttlMs));
            }
            return value;
        }

        return entry.value;
    }
}
```

**Your debugging:**

- **Bug:** _[What causes thundering herd?]_
- **Scenario:**
```
10:00:00 - Cache is populated with 1000 items, all expire at 10:05:00
10:05:00 - First request arrives
What happens?
```
- **Expected:** Smooth database load
- **Actual:** _[What happens to database?]_
- **Fix approach 1:** _[Add jitter to TTL]_
- **Fix approach 2:** _[Implement probabilistic early expiration]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** All items created at the same time will expire at the same time, causing synchronized cache misses and database overload.

**Fix 1 - Add jitter to TTL:**
```java
private final Random random = new Random();

public V get(K key) {
    // ... existing logic ...
    if (value != null) {
        // Add ±20% jitter to TTL
        long jitter = (long) (ttlMs * (0.8 + random.nextDouble() * 0.4));
        cache.put(key, new CacheEntry<>(value, jitter));
    }
    return value;
}
```

**Fix 2 - Probabilistic early expiration (XFetch algorithm):**
```java
public V get(K key) {
    CacheEntry<V> entry = cache.get(key);

    if (entry == null) {
        return refreshFromDB(key);
    }

    // Probabilistic early expiration
    // As item gets older, higher chance of refresh
    long timeLeft = entry.expiryTime - System.currentTimeMillis();
    double refreshProbability = 1.0 - ((double) timeLeft / ttlMs);

    if (entry.isExpired() || random.nextDouble() < refreshProbability * 0.1) {
        // Refresh asynchronously
        CompletableFuture.runAsync(() -> refreshFromDB(key));
    }

    return entry.value;
}

private V refreshFromDB(K key) {
    V value = database.read(key);
    if (value != null) {
        long jitter = (long) (ttlMs * (0.8 + random.nextDouble() * 0.4));
        cache.put(key, new CacheEntry<>(value, jitter));
    }
    return value;
}
```

**Key insight:** Synchronized expiration creates thundering herd. Add jitter and probabilistic early expiration to spread load.
</details>

---

### Challenge 4: LFU Frequency Update Bug

```java
/**
 * This LFU cache has a subtle frequency update bug.
 * Can you spot it?
 */
public class BuggyLFUCache<K, V> {

    static class Node<K, V> {
        K key;
        V value;
        int freq;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    private final Map<K, Node<K, V>> cache;
    private final Map<Integer, LinkedHashSet<K>> freqMap;
    private int minFreq;
    private final int capacity;

    public V get(K key) {
        Node<K, V> node = cache.get(key);
        if (node == null) return null;

        // BUG: Frequency update is incomplete!
        updateFrequency(node);
        return node.value;
    }

    private void updateFrequency(Node<K, V> node) {
        int freq = node.freq;

        // Remove from current frequency list
        freqMap.get(freq).remove(node.key);

        // BUG: What if freqMap.get(freq) is now empty?
        // And what if freq == minFreq?

        // Increment frequency
        node.freq++;

        // Add to new frequency list
        freqMap.computeIfAbsent(node.freq, k -> new LinkedHashSet<>())
               .add(node.key);
    }

    public void put(K key, V value) {
        // ... implementation
    }
}
```

**Your debugging:**

- **Bug 1:** _[What happens to minFreq?]_
- **Bug 2:** _[What about empty frequency lists?]_
- **Test case:**
```
Cache capacity = 2
put("A", 1) - freq=1, minFreq=1
get("A")    - freq=2
put("B", 2) - freq=1, minFreq should be?
// At this point, what is minFreq? What should it be?
```
- **Fix:** _[Complete the updateFrequency method]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug 1:** When the last node at minFreq is moved to a higher frequency, we must update minFreq.

**Bug 2:** Empty frequency lists should be removed from freqMap to save memory.

**Correct implementation:**
```java
private void updateFrequency(Node<K, V> node) {
    int freq = node.freq;

    // Remove from current frequency list
    LinkedHashSet<K> freqList = freqMap.get(freq);
    freqList.remove(node.key);

    // If this was the last node at minFreq, increment minFreq
    if (freq == minFreq && freqList.isEmpty()) {
        minFreq++;
    }

    // Remove empty frequency list
    if (freqList.isEmpty()) {
        freqMap.remove(freq);
    }

    // Increment frequency
    node.freq++;

    // Add to new frequency list
    freqMap.computeIfAbsent(node.freq, k -> new LinkedHashSet<>())
           .add(node.key);
}
```

**Key insight:** LFU requires careful maintenance of minFreq and frequency lists. Missing updates cause incorrect evictions.
</details>

---

### Challenge 5: Cache Invalidation Race Condition

```java
/**
 * This cache has invalidation logic but contains a race condition.
 * Data can become inconsistent between cache and database.
 */
public class InvalidationCache<K, V> {

    private final LRUCache<K, V> cache;
    private final Database<K, V> database;

    public V get(K key) {
        V value = cache.get(key);

        if (value == null) {
            value = database.read(key);
            if (value != null) {
                cache.put(key, value);
            }
        }

        return value;
    }

    public void update(K key, V newValue) {
        // BUG: Race condition between these two operations!

        // Thread 1: Writes to database
        database.write(key, newValue);

        // Thread 2: Between these two lines, another thread could:
        //   1. Read stale value from cache
        //   2. Not see the invalidation yet

        // Thread 1: Invalidates cache
        cache.remove(key);
    }
}
```

**Your debugging:**

- **Bug:** _[What race condition exists?]_
- **Scenario that fails:**
```
T0: cache contains key="user:1" value="Alice"
T1: Thread 1 calls update("user:1", "Bob")
    - Writes "Bob" to database
T2: Thread 2 calls get("user:1")
    - Reads "Alice" from cache (stale!)
T3: Thread 1 invalidates cache
    - Too late! Thread 2 already returned stale data
```
- **Fix approach 1:** _[Invalidate before write]_
- **Fix approach 2:** _[Use versioning]_
- **Fix approach 3:** _[Use cache-aside with write-through]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Cache invalidation happens AFTER database write, creating a window where stale data is served.

**Fix 1 - Invalidate before write:**
```java
public void update(K key, V newValue) {
    // Invalidate FIRST
    cache.remove(key);

    // Then write to database
    database.write(key, newValue);

    // Small window where cache misses hit DB, but at least no stale data
}
```

**Fix 2 - Use versioning:**
```java
static class VersionedValue<V> {
    V value;
    long version;
}

public void update(K key, V newValue) {
    // Increment version
    long newVersion = getNextVersion(key);

    // Write to DB with version
    database.write(key, newValue, newVersion);

    // Update cache with version
    cache.put(key, new VersionedValue<>(newValue, newVersion));
}

public V get(K key) {
    VersionedValue<V> cached = cache.get(key);
    VersionedValue<V> dbValue = database.read(key);

    // Compare versions, use latest
    if (cached != null && dbValue != null) {
        return cached.version >= dbValue.version ? cached.value : dbValue.value;
    }
    // ... handle nulls
}
```

**Fix 3 - Cache-aside with write-through (best):**
```java
public void update(K key, V newValue) {
    // Write to both atomically (within transaction if possible)
    cache.put(key, newValue);
    database.write(key, newValue);
    // No invalidation needed - cache is always up to date
}
```

**Key insight:** Cache invalidation is notoriously hard. Order matters: invalidate before write, or use write-through to avoid invalidation entirely.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 5+ critical caching bugs
- [ ] Understood WHY each bug causes problems
- [ ] Could explain the fix to someone else
- [ ] Learned common caching mistakes to avoid

**Common caching bugs you discovered:**

1. Cache stampede (no synchronization on miss)
2. Stale data (wrong read order in write-back)
3. Thundering herd (synchronized expiration)
4. Frequency tracking errors (LFU minFreq bug)
5. Invalidation race conditions

**Defensive caching patterns to remember:**

- _[Fill in patterns you learned]_
- _[Fill in]_
- _[Fill in]_

---

## Decision Framework

**Questions to answer after implementation:**

### 1. When to use LRU vs LFU?

**LRU Cache:**

- When to use: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Example scenarios: _[Fill in]_

**LFU Cache:**

- When to use: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Example scenarios: _[Fill in]_

### 2. When to use Write-Through vs Write-Back?

**Write-Through:**

- When to use: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Example scenarios: _[Fill in]_

**Write-Back:**

- When to use: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Example scenarios: _[Fill in]_

### 3. Your Decision Tree

Build this after solving practice scenarios:

```
Should I use caching?
│
├─ What's the access pattern?
│   ├─ Recent items accessed again → ?
│   ├─ Popular items accessed frequently → ?
│   └─ Mixed/unknown → ?
│
├─ What's the write pattern?
│   ├─ Consistency critical → ?
│   ├─ Performance critical → ?
│   └─ Mixed → ?
│
└─ Other considerations?
    ├─ Memory constraints → ?
    ├─ Data freshness requirements → ?
    └─ Failure tolerance → ?
```

### The "Kill Switch" - Don't use caching when:

1. _[When does caching hurt more than help?]_
2. _[Fill in after understanding trade-offs]_
3. _[Fill in after practice]_
4. _[Fill in]_
5. _[Fill in]_

### The Rule of Three: Alternatives

For each scenario, compare three approaches:

**Option 1: Caching**

- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

**Option 2: Database Indexing**

- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

**Option 3: Read Replicas**

- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Use when: _[Fill in]_

---

## Practice

### Scenario 1: E-commerce Product Catalog

**Requirements:**

- 1M products, 100K frequently viewed
- Reads: 10K/sec, Writes: 100/sec
- Users browse recent and popular products

**Your cache design:**

- Eviction policy: _[LRU or LFU? Why?]_
- Write policy: _[Write-Through or Write-Back? Why?]_
- Capacity: _[How much?]_
- TTL: _[Time-to-live strategy?]_
- Invalidation: _[When to invalidate?]_

### Scenario 2: Social Media Feed

**Requirements:**

- Each user has 500 followers
- Feed shows recent posts (last 24h)
- High read:write ratio (100:1)
- Eventually consistent is OK

**Your cache design:**

- Eviction policy: _[Fill in]_
- Write policy: _[Fill in]_
- Cache key structure: _[What to cache?]_
- Invalidation strategy: _[Fill in]_
- Capacity planning: _[Fill in]_

### Scenario 3: Session Store

**Requirements:**

- Store user sessions (auth tokens, preferences)
- Sessions expire after 30 minutes of inactivity
- High read frequency
- Consistency required (can't lose session data)

**Your cache design:**

- Eviction policy: _[Fill in]_
- Write policy: _[Fill in]_
- TTL strategy: _[Fill in]_
- Persistence: _[How to ensure durability?]_

### LeetCode Problem

**Problem:** [146. LRU Cache](https://leetcode.com/problems/lru-cache/)

Design and implement a data structure for Least Recently Used (LRU) cache.

**Your approach:**

1. _[Data structures needed?]_
2. _[How to achieve O(1) for both get and put?]_
3. _[Edge cases to handle?]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] LRU Cache works with O(1) operations
    - [ ] LFU Cache works with frequency tracking
    - [ ] Write-Through pattern implemented correctly
    - [ ] Write-Back pattern with async flush works
    - [ ] All client code runs successfully

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Understand LRU vs LFU trade-offs
    - [ ] Understand Write-Through vs Write-Back trade-offs
    - [ ] Built decision tree

- [ ] **Decision Making**
    - [ ] Know when to use LRU vs LFU
    - [ ] Know when to use Write-Through vs Write-Back
    - [ ] Completed practice scenarios
    - [ ] Can explain trade-offs to someone else

- [ ] **Mastery Check**
    - [ ] Could implement LRU from memory
    - [ ] Could implement LFU from memory
    - [ ] Could design cache for new scenario
    - [ ] Understand when NOT to use caching

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Product Manager

**Scenario:** Your PM asks why we need caching and what the trade-offs are.

**Your explanation (write it out):**

> "Caching is a technique where..."
>
> _[Fill in your explanation in plain English - 3-4 sentences max]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by a non-technical person? _[Yes/No]_
- Did you explain the trade-offs (not just benefits)? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw the LRU cache eviction process without looking at code.

**Draw the cache state at each step:**

```
Initial state: Cache capacity = 3
Cache: [Empty]

After put("A", 1):
Most Recent → Least Recent
[Your drawing: ________________]

After put("B", 2):
[Your drawing: ________________]

After put("C", 3):
[Your drawing: ________________]

After get("A"):  // Accessing A makes it most recent
[Your drawing: ________________]

After put("D", 4):  // Cache full, evict LRU
[Your drawing: ________________]
Which item was evicted? _____
Why? _____________________
```

**Verification:**

- [ ] Drew initial state correctly
- [ ] Showed access order updates
- [ ] Identified correct eviction (B, since it's now LRU after A was accessed)
- [ ] Explained why that item was chosen

---

### Gate 3: Pattern Recognition Test

**Without looking at your notes, classify these scenarios:**

| Scenario | Best Cache Policy | Write Strategy | Why? |
|----------|------------------|----------------|------|
| User sessions (timeout-based) | _[LRU/LFU?]_ | _[Write-Through/Write-Back?]_ | _[Explain]_ |
| Video streaming (popular content) | _[LRU/LFU?]_ | _[Read-only/Write?]_ | _[Explain]_ |
| Shopping cart (consistency critical) | _[LRU/LFU?]_ | _[Write-Through/Write-Back?]_ | _[Explain]_ |
| DNS resolution (hot domains) | _[LRU/LFU?]_ | _[TTL strategy?]_ | _[Explain]_ |
| Real-time analytics (write-heavy) | _[Cache at all?]_ | _[Write-Back/No cache?]_ | _[Explain]_ |
| Product catalog (read-heavy) | _[LRU/LFU?]_ | _[Write-Through/Write-Back?]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the decision framework and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Operation | LRU Cache | LFU Cache | Direct DB | Why? |
|-----------|-----------|-----------|-----------|------|
| Get (hit) | O(?) | O(?) | O(?) | _[Explain]_ |
| Get (miss) | O(?) | O(?) | O(?) | _[Explain]_ |
| Put (no eviction) | O(?) | O(?) | O(?) | _[Explain]_ |
| Put (with eviction) | O(?) | O(?) | O(?) | _[Explain]_ |

**Deep question:** Why is LRU O(1) but requires two data structures?

Your answer: _[Fill in - explain HashMap + Doubly Linked List necessity]_

**Cache hit rate impact:**

- If hit rate = 90%, average latency = ?
- If hit rate drops to 50%, average latency = ?
- Calculate: _[Show your work]_

---

### Gate 5: Trade-off Decision

**Scenario:** You're building a news feed API with these requirements:
- 10M users
- Each user has ~500 followers
- Average 100 new posts/sec across all users
- Users check feed every 5 minutes
- Data freshness: 1-minute lag acceptable

**Design your caching strategy:**

**1. Should you cache at all?**

- Decision: _[Yes/No]_
- Reasoning: _[Fill in - consider read/write ratio, data size, freshness]_

**2. What should you cache?**

- [ ] User profiles
- [ ] Individual posts
- [ ] Pre-computed feeds
- [ ] Follower lists
- Explain choices: _[Fill in]_

**3. Eviction policy:**

- Choice: _[LRU/LFU/TTL/Hybrid]_
- Reasoning: _[Fill in - consider access pattern]_

**4. Write policy:**

- Choice: _[Write-Through/Write-Back/Cache-Aside]_
- Reasoning: _[Fill in - consider consistency requirements]_

**5. Cache capacity calculation:**

- Average feed size: 100 posts × 1KB = 100KB
- Active users: 10M × 20% (Pareto) = 2M
- Cache size needed: _[Calculate]_
- Per-server capacity (if 10 servers): _[Calculate]_

**6. Invalidation strategy:**

- When new post created: _[Invalidate? Update? Ignore?]_
- When user unfollows: _[What to do?]_
- Strategy: _[Fill in your approach]_

**What would make you change your decision?**

- _[Fill in - what constraints would flip your choices?]_

---

### Gate 6: Code from Memory (Final Test)

**Set a 15-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: LRU Cache with get and put operations
 * Both operations must be O(1)
 */
public class LRUCache<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // Your initialization here
    }

    public V get(K key) {
        // Your implementation here



        return null; // Replace
    }

    public void put(K key, V value) {
        // Your implementation here



    }
}
```

**After implementing, test with:**
```java
LRUCache<String, Integer> cache = new LRUCache<>(2);
cache.put("A", 1);
cache.put("B", 2);
System.out.println(cache.get("A")); // 1
cache.put("C", 3); // Evicts B
System.out.println(cache.get("B")); // null (evicted)
```

**Verification:**

- [ ] Implemented correctly without looking
- [ ] Both get and put are O(1)
- [ ] Handles eviction correctly
- [ ] Used HashMap + Doubly Linked List
- [ ] Edge cases handled (capacity=1, null keys, etc.)

---

### Gate 7: Debugging Mastery

**Task:** Identify the bug in this cache implementation without running it.

```java
public class BuggyCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final int capacity;

    public synchronized V get(K key) {
        return cache.get(key);
    }

    public synchronized void put(K key, V value) {
        if (cache.size() >= capacity) {
            // BUG: What's wrong here?
            K firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }
        cache.put(key, value);
    }
}
```

**Your analysis:**

- **Bug identified:** _[What's wrong?]_
- **Why it fails:** _[Explain the issue]_
- **What gets evicted:** _[Random key or LRU?]_
- **Fix:** _[How to make it truly LRU?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Uses HashMap which doesn't maintain insertion order. `keySet().iterator().next()` returns an arbitrary key, not the least recently used one.

**Fix:** Use LinkedHashMap with access order, or implement proper LRU with HashMap + Doubly Linked List.

```java
private final Map<K, V> cache = new LinkedHashMap<>(capacity, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
};
```
</details>

---

### Gate 8: Performance Analysis

**Scenario:** Your cache metrics show:
- Hit rate: 60%
- Average cache latency: 1ms
- Average DB latency: 50ms
- Requests/sec: 1000

**Calculate:**

1. **Current average latency:**
    - Calculation: _[Show work]_
    - Result: _____ms

2. **Database queries/sec:**
    - Calculation: _[Show work]_
    - Result: _____

3. **If you improve hit rate to 90%:**
    - New average latency: _____ms
    - Improvement: _____x faster
    - DB queries/sec reduced to: _____

4. **ROI analysis:**
    - Current DB cost: $1000/day
    - With 90% hit rate: $___/day
    - Savings: $___/day
    - Is cache worth maintaining? _[Your decision]_

---

### Gate 9: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain to an imaginary junior developer the most important lesson about caching.

Your explanation:

> "The most important thing to understand about caching is..."
>
> _[Fill in - what's the ONE critical insight?]_

**Common pitfalls to warn them about:**

1. _[Most common mistake?]_
2. _[Second most common?]_
3. _[Third most common?]_

**When NOT to use caching:**

1. _[Scenario where caching hurts]_
2. _[Another scenario]_
3. _[Another scenario]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement LRU cache from memory in under 15 minutes
- [ ] Implement LFU cache with correct frequency tracking
- [ ] Explain when to use LRU vs LFU with real examples
- [ ] Explain when to use Write-Through vs Write-Back
- [ ] Calculate cache hit rates and performance impact
- [ ] Identify and fix common caching bugs (stampede, stale data, etc.)
- [ ] Design caching strategy for a new system
- [ ] Explain trade-offs to both technical and non-technical audiences

**Self-assessment score:** ___/10

**Real-world application:**

- Have you used caching in a project? _[Yes/No]_
- If yes, what was the hit rate? _____
- What policy did you use? _[LRU/LFU/TTL/Other]_
- What would you do differently now? _[Fill in]_

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered caching patterns. Proceed to the next topic.
