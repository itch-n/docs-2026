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

**Next:** [04. API Design →](04-api-design.md)

**Back:** [02. Indexing Strategies ←](02-indexing.md)
