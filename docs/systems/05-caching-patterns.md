# Caching Patterns

> Master LRU, LFU, and write policies for high-performance systems

---

## Learning Objectives

By the end of this topic you will be able to:

- Implement LRU and LFU caches with O(1) get and put operations using the correct combination of data structures
- Explain why the combination of a HashMap and a doubly linked list achieves O(1) LRU operations
- Compare Write-Through and Write-Back policies and choose the right one given consistency and latency requirements
- Calculate the performance impact of a given cache hit rate on average latency and database load
- Identify and fix the five common caching bugs: stampede, stale data, thundering herd on expiration, LFU minFreq error, and invalidation race condition
- Design a complete caching strategy (eviction policy, write policy, TTL, invalidation) for a given production scenario

---

!!! warning "Operational reality"
    Cache invalidation is famously one of the two hard problems in computer science for good reason. The gap between "add a cache" and "run a reliable cache" is significant. The thundering herd problem — where a popular cache entry expires and thousands of requests simultaneously hit the database — is underappreciated until it takes down production. Probabilistic early expiration and cache stampede locks exist precisely because TTL-based expiry does not compose well with high traffic.

    Redis used as a primary data store (not a cache) is a data loss risk: the default configuration is not crash-safe, and AOF persistence adds latency. Teams regularly discover this after an unexpected restart. Cache-aside sounds simple in interviews; in production it requires careful thought about consistency windows, cold-start behaviour, and what happens when the cache is unavailable.

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is caching in one sentence?**
    - Caching is a technique where you store <span class="fill-in">[the result of an expensive ___ in a faster location so that repeated requests for the same ___ can be answered in ___ instead of ___]</span>

2. **Why/when do we use caching?**
    - Caching is used when <span class="fill-in">[the cost of computing or fetching a value is ___ and the same value is requested ___, making it cheaper to ___ the result than to ___ it again]</span>

3. **Real-world analogy:**
    - Example: "A cache is like keeping your favorite books on your desk instead of walking to the library..."
    - Think about how a chef keeps frequently used spices on the counter rather than the stock room.
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What's the difference between LRU and LFU?**
    - LRU evicts the item that was <span class="fill-in">[accessed ___ recently, assuming that items not used lately will not be needed ___]</span>
    - LFU evicts the item that was <span class="fill-in">[accessed ___ frequently overall, assuming that items rarely requested are ___ likely to be needed]</span>

5. **When should you use Write-Through vs Write-Back?**
    - Use Write-Through when <span class="fill-in">[___ is critical and you cannot afford to lose a write, because ___ updates both ___ and ___ synchronously]</span>
    - Use Write-Back when <span class="fill-in">[___ latency matters more than strict consistency, because the write is acknowledged after updating ___ only, with the ___ flush happening ___]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

!!! tip "How to use this section"
    Complete your predictions now, before reading further. You will revisit and verify each answer after running the implementations.

<div class="learner-section" markdown>

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Direct database query for each request:**
    - Time per request: <span class="fill-in">[Your guess: O(?)]</span>
    - If DB query takes 50ms, how many requests/sec can you handle? _____
    - Verified after learning: <span class="fill-in">[Actual]</span>

2. **Cache lookup + occasional DB query:**
    - Cache hit time: <span class="fill-in">[Your guess: O(?)]</span>
    - Cache miss time: <span class="fill-in">[Your guess: O(?)]</span>
    - If 90% cache hit rate, average latency: <span class="fill-in">_____</span>ms
    - Verified: <span class="fill-in">[Actual]</span>

3. **Hit rate calculation:**
    - 1000 requests, 900 cache hits, 100 misses
    - Hit rate: <span class="fill-in">_____</span>%
    - If cache saves 45ms per hit, total time saved: <span class="fill-in">_____</span>ms

### Scenario Predictions

**Scenario 1:** E-commerce product catalog with access pattern:

```
Product A: accessed 5 times
Product B: accessed 10 times
Product C: accessed 3 times
Product D: accessed 8 times
Cache capacity: 2 items
```

- **With LRU, which items remain after all accesses?** <span class="fill-in">[Fill in - trace manually]</span>
- **With LFU, which items remain?** <span class="fill-in">[Fill in - trace manually]</span>
- **Which is better for this pattern?** <span class="fill-in">[LRU/LFU - Why?]</span>

**Scenario 2:** User session cache (last access time matters)

```
Session A: last accessed 10 min ago
Session B: last accessed 2 min ago
Session C: last accessed 5 min ago
Cache full, new session arrives
```

- **Which eviction policy makes sense?** <span class="fill-in">[LRU/LFU - Why?]</span>
- **Which session gets evicted?** <span class="fill-in">[Fill in]</span>

**Scenario 3:** Write policies

```
Request: Update user profile
Write-Through: Cache + DB both take 5ms each
Write-Back: Cache takes 1ms, DB flush happens later
```

- **Write-Through total latency:** _____ms
- **Write-Back perceived latency:** _____ms
- **If DB fails during Write-Back flush, what happens?** <span class="fill-in">[Fill in]</span>
- **Which is safer?** <span class="fill-in">[Fill in - Why?]</span>

### Trade-off Quiz

**Question 1:** When would direct database queries be BETTER than caching?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question 2:** What's the MAIN benefit of caching?

- [ ] Reduces database load
- [ ] Reduces latency
- [ ] Saves money
- [ ] All of the above

Verify after implementation: <span class="fill-in">[Which one(s)?]</span>

**Question 3:** Cache hit rate drops from 90% to 50%. How does this affect performance?

- Your calculation: <span class="fill-in">[Fill in]</span>
- Verified impact: <span class="fill-in">[Fill in after implementation]</span>

</div>

---

## Core Implementation

### Pattern 1: LRU Cache (Least Recently Used)

**Your task:** Implement LRU cache with O(1) get and put operations.

```java
--8<-- "com/study/systems/caching/LRUCache.java"
```

!!! warning "Debugging Challenge — Broken LRU Eviction Order"

    The `put()` below has a subtle bug that evicts the wrong entry when the cache is full.

    ```java
    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            node.value = value;
            list.moveToFront(node);
            return;
        }

        Node<K, V> node = new Node<>(key, value);
        cache.put(key, node);
        list.addToFront(node);

        if (cache.size() > capacity) {
            Node<K, V> lru = list.removeLast();
            // Bug is here
            cache.remove(lru.key);
        }
    }
    ```

    Trace with capacity=2, then: `put(A)`, `put(B)`, `get(A)`, `put(C)`.
    After `put(C)`, what is in the cache and what should be?

    ??? success "Answer"

        The logic above is actually correct for this trace — `B` is the LRU and gets evicted. The subtle bug that learners typically introduce is **not removing the node from `cache` before or after `removeLast()`**, causing `cache.size()` to remain inflated. If `cache.remove(lru.key)` is omitted or the node's key is not stored, the size check `cache.size() > capacity` never triggers correctly on future insertions.

        Always verify that both the HashMap **and** the linked list are updated atomically on every eviction.

---

### Pattern 2: LFU Cache (Least Frequently Used)

**Your task:** Implement LFU cache with O(1) get and put operations.

```java
--8<-- "com/study/systems/caching/LFUCache.java"
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
     */
    public V get(K key) {
        // TODO: Check cache first, then fallback to database

        return null; // Replace
    }

    /**
     * Put value
     *
     * TODO: Implement write-through
     */
    public void put(K key, V value) {
        // TODO: Update both cache and database synchronously
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

        // TODO: Schedule background flush task
    }

    /**
     * Get value
     *
     * TODO: Implement get
     */
    public V get(K key) {
        // TODO: Check cache, dirty entries, then database

        return null; // Replace
    }

    /**
     * Put value
     *
     * TODO: Implement write-back
     */
    public void put(K key, V value) {
        // TODO: Update cache immediately
        // Mark for later database flush
    }

    /**
     * Flush dirty entries to database
     *
     * TODO: Implement flush
     */
    private void flushDirtyEntries() {
        // TODO: Write all dirty entries to database
        // Handle failures appropriately
    }

    public void shutdown() {
        // TODO: Ensure all data is flushed before shutdown
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

- **Bug location:** <span class="fill-in">[Which lines?]</span>
- **Bug explanation:** <span class="fill-in">[What happens with concurrent requests?]</span>

**Test case to expose the bug:**

```java
// Simulate 1000 concurrent requests for same key (cache miss)
ExecutorService executor = Executors.newFixedThreadPool(1000);
for (int i = 0; i < 1000; i++) {
    executor.submit(() -> cache.get("popular-item"));
}
// Expected: 1 DB query
// Actual with bug: <span class="fill-in">_____</span> DB queries
```

??? success "Answer"

    **Bug:** No synchronization during cache miss. Multiple threads can simultaneously detect cache miss and all query the
    database.

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

- **Bug:** <span class="fill-in">[What's wrong with the get() logic?]</span>
- **Scenario that breaks:**

```

1. put("key1", "value1") - goes to cache and dirty
2. cache evicts key1 (capacity full)
3. get("key1") - what do you get?
```

- **Expected:** "value1" (from dirty entries)
- **Actual:** <span class="fill-in">[What happens?]</span>
- **Fix:** <span class="fill-in">[Correct the order of checks]</span>

??? success "Answer"

    **Bug:** Cache lookup happens before dirty entries check. If cache evicts an item that's in dirtyEntries, we'll miss the
    latest value.

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

    **Key insight:** With write-back caching, dirty entries hold the "source of truth" until flushed. Always check them
    first!

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

- **Bug:** <span class="fill-in">[What causes thundering herd?]</span>
- **Scenario:**

```

10:00:00 - Cache is populated with 1000 items, all expire at 10:05:00
10:05:00 - First request arrives
What happens?
```

- **Expected:** Smooth database load
- **Actual:** <span class="fill-in">[What happens to database?]</span>

??? success "Answer"

    **Bug:** All items created at the same time will expire at the same time, causing synchronized cache misses and database
    overload.

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

    **Key insight:** Synchronized expiration creates thundering herd. Add jitter and probabilistic early expiration to
    spread load.

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

        updateFrequency(node);
        return node.value;
    }

    private void updateFrequency(Node<K, V> node) {
        int freq = node.freq;

        // Remove from current frequency list
        freqMap.get(freq).remove(node.key);

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

- **Bug 1:** <span class="fill-in">[What happens to minFreq?]</span>
- **Bug 2:** <span class="fill-in">[What about empty frequency lists?]</span>

```
Cache capacity = 2
put("A", 1) - freq=1, minFreq=1
get("A")    - freq=2
put("B", 2) - freq=1, minFreq should be?
// At this point, what is minFreq? What should it be?
```

- **Fix:** <span class="fill-in">[Complete the updateFrequency method]</span>

??? success "Answer"

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

    **Key insight:** LFU requires careful maintenance of minFreq and frequency lists. Missing updates cause incorrect
    evictions.

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

- **Bug:** <span class="fill-in">[What race condition exists?]</span>
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


??? success "Answer"

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

    **Key insight:** Cache invalidation is notoriously hard. Order matters: invalidate before write, or use write-through to
    avoid invalidation entirely.

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

- <span class="fill-in">[Fill in patterns you learned]</span>
- <span class="fill-in">[Fill in]</span>
- <span class="fill-in">[Fill in]</span>

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

| Metric                         | Direct DB  | With Cache  | Improvement         |
|--------------------------------|------------|-------------|---------------------|
| **Total time (1000 requests)** | 50,000ms   | 1,049ms     | **47.6x faster**    |
| **Average latency**            | 50ms       | 1.05ms      | **47.6x faster**    |
| **Database queries**           | 1000       | 1           | **99.9% reduction** |
| **Throughput**                 | 20 req/sec | 950 req/sec | **47.5x higher**    |
| **DB cost (estimate)**         | $100/day   | $2/day      | **$98/day savings** |

**Your calculation:** For 10,000 requests with 90% cache hit rate:

- Cache hits: 10,000 × 0.9 = <span class="fill-in">_____</span> requests × 1ms = <span class="fill-in">_____</span>ms
- Cache misses: 10,000 × 0.1 = <span class="fill-in">_____</span> requests × 50ms = <span class="fill-in">_____</span>ms
- Total time: <span class="fill-in">_____</span> + _____ = <span class="fill-in">_____</span>ms
- Speedup vs direct DB: <span class="fill-in">_____</span> times faster

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

!!! note "Key insight"
    Even a modest 75% hit rate gives 3.8x speedup. The relationship between hit rate and speedup is non-linear — the last few percentage points of hit rate improvement produce the largest gains.

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- Why does caching provide such dramatic speedup? <span class="fill-in">[Your answer]</span>
- What happens when hit rate drops below 50%? <span class="fill-in">[Your answer]</span>
- When might caching not be worth it? <span class="fill-in">[Your answer]</span>

</div>

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

| Policy        | User Latency   | DB Write     | Consistency | Data Loss Risk        |
|---------------|----------------|--------------|-------------|-----------------------|
| Write-Through | 51ms           | Synchronous  | Immediate   | None                  |
| Write-Back    | 1ms            | Asynchronous | Eventual    | If crash before flush |
| **Speedup**   | **51x faster** | -            | Trade-off   | Trade-off             |

**Your analysis:** When would you choose each?

- Write-Through: <span class="fill-in">[Fill in scenarios]</span>
- Write-Back: <span class="fill-in">[Fill in scenarios]</span>

!!! info "Loop back"
    Return to the Quick Quiz now and fill in your verified answers.

---

## Case Studies: Caching in the Wild

### Facebook's Social Graph: TAO and Memcached

- **Pattern:** Cache-Aside with a custom distributed caching layer (TAO).
- **How it works:** Facebook's social graph (friends, posts, comments) is too large and interconnected to query from a
  database for every request. They built TAO, a geographically distributed caching system on top of Memcached. When a
  user requests their feed, the application first queries TAO. If the data is present (cache hit), it's returned
  instantly. If not (cache miss), TAO fetches the data from the master database (MySQL), populates the cache, and then
  returns it.
- **Key Takeaway:** At massive scale, a simple cache isn't enough. Facebook needed to build a custom caching *service*
  that handles eventual consistency, replication across data centers, and the "thundering herd" problem. It showcases
  the cache-aside pattern on a global scale.

### Twitter's Timeline Cache: Redis for Real-Time Feeds

- **Pattern:** Pre-computed timelines with a Cache-Aside strategy.
- **How it works:** A user's home timeline is one of the most frequently read pieces of data. Generating it on-the-fly
  for every request is too slow. Instead, Twitter pre-computes user timelines and stores them in a massive Redis
  cluster. When you open Twitter, your app fetches this pre-computed list directly from the cache. When a user you
  follow tweets, a background "fan-out" service pushes that tweet into the timeline caches of all their followers.
- **Key Takeaway:** For read-heavy workloads with complex data generation, it's often better to do the work ahead of
  time and cache the *results*. Redis is a perfect fit for this due to its high performance and versatile data
  structures (like sorted lists for timelines).

### Content Delivery Networks (CDNs): Caching at the Edge

- **Pattern:** Multi-layered caching with LRU/LFU eviction.
- **How it works:** A Content Delivery Network (CDN) like Cloudflare or Akamai acts as a massive, distributed cache for
  a website's static assets (images, CSS, JS). When a user in London requests an image, it's served from the CDN's
  London edge server, not from the origin server in California. The first request might be slow, but subsequent requests
  from that region are served from the local cache.
- **Key Takeaway:** Caching isn't just for databases; it's for any data that can be served closer to the user. CDNs
  demonstrate how layered caching and intelligent eviction policies (like LRU to keep popular assets hot) can
  dramatically improve website performance and reduce bandwidth costs for the origin server.

---

## Common Misconceptions

!!! warning "LFU is always better than LRU for production caches"
    LFU is better when access frequency is stable and predictable (e.g., a product catalog where bestsellers stay popular). But LFU is slow to adapt to changing patterns — a new viral post gets evicted immediately because its frequency count starts at 1. LRU handles temporal locality better and is the default in most production caches (including Redis) for this reason.

!!! warning "A high cache hit rate means the cache is working well"
    Hit rate measures how often the cache answers requests, not whether the cached data is fresh or correct. A cache full of stale data can have a 99% hit rate while serving wrong answers. Always monitor both hit rate *and* cache invalidation correctness. A low hit rate can also indicate a cache that is too small, a poor eviction policy, or a workload with low data reuse.

!!! warning "Write-Back is dangerous and should be avoided"
    Write-Back carries data loss risk only during the window between a write and the next flush. With a durable write-ahead log (WAL) or periodic snapshots, the risk can be bounded to seconds of data. Many high-performance databases (including MySQL InnoDB's buffer pool) use write-back internally. The choice is about acceptable durability guarantees, not a blanket safety rule.

---

## Decision Framework

<div class="learner-section" markdown>

**Questions to answer after implementation:**

### 1. When to use LRU vs LFU?

**LRU Cache:**

- When to use: <span class="fill-in">[Fill in]</span>
- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>
- Example scenarios: <span class="fill-in">[Fill in]</span>

**LFU Cache:**

- When to use: <span class="fill-in">[Fill in]</span>
- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>
- Example scenarios: <span class="fill-in">[Fill in]</span>

### 2. When to use Write-Through vs Write-Back?

**Write-Through:**

- When to use: <span class="fill-in">[Fill in]</span>
- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>
- Example scenarios: <span class="fill-in">[Fill in]</span>

**Write-Back:**

- When to use: <span class="fill-in">[Fill in]</span>
- Pros: <span class="fill-in">[Fill in]</span>
- Cons: <span class="fill-in">[Fill in]</span>
- Example scenarios: <span class="fill-in">[Fill in]</span>

### 3. Your Decision Tree

Build this after solving practice scenarios:
```mermaid
flowchart LR
    Start["Should I use caching?"]

    Q1{"What's the access pattern?"}
    Start --> Q1
    N2["?"]
    Q1 -->|"Recent items accessed again"| N2
    N3["?"]
    Q1 -->|"Popular items accessed frequently"| N3
    N4["?"]
    Q1 -->|"Mixed/unknown"| N4
    Q5{"What's the write pattern?"}
    Start --> Q5
    N6["?"]
    Q5 -->|"Consistency critical"| N6
    N7["?"]
    Q5 -->|"Performance critical"| N7
    N8["?"]
    Q5 -->|"Mixed"| N8
    Q9{"Other considerations?"}
    Start --> Q9
    N10["?"]
    Q9 -->|"Memory constraints"| N10
    N11["?"]
    Q9 -->|"Data freshness requirements"| N11
    N12["?"]
    Q9 -->|"Failure tolerance"| N12
```

</div>

---

## Practice

<div class="learner-section" markdown>

### Scenario 1: E-commerce Product Catalog

**Requirements:**

- 1M products, 100K frequently viewed
- Reads: 10K/sec, Writes: 100/sec
- Users browse recent and popular products

**Your cache design:**

- Eviction policy: <span class="fill-in">[LRU or LFU? Why?]</span>
- Write policy: <span class="fill-in">[Write-Through or Write-Back? Why?]</span>
- Capacity: <span class="fill-in">[How much?]</span>
- TTL: <span class="fill-in">[Time-to-live strategy?]</span>
- Invalidation: <span class="fill-in">[When to invalidate?]</span>

**Failure modes:**

- What happens if the cache node becomes unavailable during a high-traffic flash sale? <span class="fill-in">[Fill in]</span>
- How does your design behave when a popular product is updated and cache invalidation is delayed? <span class="fill-in">[Fill in]</span>

### Scenario 2: Social Media Feed

**Requirements:**

- Each user has 500 followers
- Feed shows recent posts (last 24h)
- High read:write ratio (100:1)
- Eventually consistent is OK

**Your cache design:**

- Eviction policy: <span class="fill-in">[Fill in]</span>
- Write policy: <span class="fill-in">[Fill in]</span>
- Cache key structure: <span class="fill-in">[What to cache?]</span>
- Invalidation strategy: <span class="fill-in">[Fill in]</span>
- Capacity planning: <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the Redis cluster storing pre-computed feeds goes down during peak usage? <span class="fill-in">[Fill in]</span>
- How does your design behave when a user with 10,000 followers posts and the fan-out write overwhelms the cache layer? <span class="fill-in">[Fill in]</span>

### Scenario 3: Session Store

**Requirements:**

- Store user sessions (auth tokens, preferences)
- Sessions expire after 30 minutes of inactivity
- High read frequency
- Consistency required (can't lose session data)

**Your cache design:**

- Eviction policy: <span class="fill-in">[Fill in]</span>
- Write policy: <span class="fill-in">[Fill in]</span>
- TTL strategy: <span class="fill-in">[Fill in]</span>
- Persistence: <span class="fill-in">[How to ensure durability?]</span>

**Failure modes:**

- What happens if the session cache crashes and users are logged out mid-session at scale? <span class="fill-in">[Fill in]</span>
- How does your design behave when a Write-Back flush to the persistent store fails after a cache node restart? <span class="fill-in">[Fill in]</span>

### LeetCode Problem

**Problem:** [146. LRU Cache](https://leetcode.com/problems/lru-cache/)

Design and implement a data structure for Least Recently Used (LRU) cache.

**Your approach:**

1. <span class="fill-in">[Data structures needed?]</span>
2. <span class="fill-in">[How to achieve O(1) for both get and put?]</span>
3. <span class="fill-in">[Edge cases to handle?]</span>

</div>

---

## Test Your Understanding

Answer these without referring to your notes or implementation.

1. Explain why an LRU cache requires both a HashMap *and* a doubly linked list. What does each structure contribute, and why can neither alone achieve O(1) for all operations?

    ??? success "Rubric"
        A complete answer addresses: (1) the HashMap provides O(1) key lookup to find a node's position, (2) the doubly linked list provides O(1) move-to-front and remove operations using prev/next pointers, (3) a HashMap alone cannot reorder elements in O(1), and a linked list alone cannot locate a node without O(n) traversal.

2. A Write-Back cache has 500 dirty entries when the application crashes. What data is lost, and what design decision controls how much data can be lost in this scenario?

    ??? success "Rubric"
        A complete answer addresses: (1) all 500 dirty entries not yet flushed to the database are permanently lost, (2) the flush interval (or dirty-entry threshold) determines the maximum loss window, (3) a write-ahead log (WAL) or periodic snapshot can bound the loss to a configurable number of seconds of writes.

3. Your product page cache has a 60% hit rate. DB queries take 80ms and cache hits take 1ms. Calculate the average latency at 60% and at 90% hit rate. What business case does this support?

    ??? success "Rubric"
        A complete answer addresses: (1) at 60%: (0.6 × 1ms) + (0.4 × 80ms) = 32.6ms average; at 90%: (0.9 × 1ms) + (0.1 × 80ms) = 8.9ms average, (2) moving from 60% to 90% hit rate yields a ~3.7x latency improvement, (3) the business case is faster page loads, higher conversion rates, and reduced database cost.

4. You are choosing between LRU and LFU for a news aggregator where articles trend for 2–3 hours and then go cold. Which eviction policy fits better, and why?

    ??? success "Rubric"
        A complete answer addresses: (1) LRU fits better because it evicts based on recency, naturally expiring cold articles that haven't been accessed recently, (2) LFU would retain old viral articles long after they've gone cold because their historical frequency count remains high, (3) LRU's temporal locality assumption matches the trending-then-cold access pattern.

5. A colleague says "Cache invalidation is easy — just delete the key when the data changes." Describe a specific race condition that this simple approach fails to prevent, and what a safer alternative looks like.

    ??? success "Rubric"
        A complete answer addresses: (1) the race condition: Thread A deletes the key, Thread B reads from DB and populates the cache with stale data before Thread A's DB write completes, leaving stale data in cache, (2) a safer alternative is cache-aside with compare-and-swap (CAS) or versioned keys so a write only populates the cache if the version matches, (3) event-driven invalidation (publish a cache-bust event after the DB write commits) further reduces the race window.

---

## Connected Topics

!!! info "Where this topic connects"

    - **09. Load Balancing** — consistent hashing is used in both caching (distributing cache keys) and load balancing (distributing requests); the algorithm is identical → [09. Load Balancing](09-load-balancing.md)
    - **11. Database Scaling** — caching reduces read load on primary replicas; write strategy (write-through vs write-behind) affects database durability guarantees → [11. Database Scaling](11-database-scaling.md)
    - **17. Distributed Transactions** — cache invalidation across multiple nodes is a distributed consistency problem; cache + database writes together require careful coordination → [17. Distributed Transactions](17-distributed-transactions.md)
