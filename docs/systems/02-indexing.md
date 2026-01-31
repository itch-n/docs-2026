# 02. Indexing Strategies

> Primary, secondary, and composite indexes - The key to fast queries

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing indexes, explain them simply.

**Prompts to guide you:**

1. **What is a database index in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do indexes speed up queries?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "An index is like a book's table of contents that lets you jump to specific chapters..."
   - Your analogy: _[Fill in]_

4. **What's the cost of indexes?**
   - Your answer: _[Fill in after practice]_

5. **When should you NOT create an index?**
   - Your answer: _[Fill in after understanding trade-offs]_

---

## Core Implementation

### Part 1: Primary Index (Clustered)

**Your task:** Implement a primary index where data is stored with the index.

```java
import java.util.*;

/**
 * Primary Index: Clustered index where data is stored with keys
 *
 * Properties:
 * - Only one per table
 * - Data physically ordered by key
 * - Fast range queries
 * - Usually on primary key
 */
class Record {
    long id;        // Primary key
    long userId;
    String content;
    long createdAt;

    public Record(long id, long userId, String content, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Record{id=%d, userId=%d, content='%s'}", id, userId, content);
    }
}

public class PrimaryIndex {

    // Using TreeMap as clustered B+Tree (data stored with keys)
    private TreeMap<Long, Record> index;

    public PrimaryIndex() {
        this.index = new TreeMap<>();
    }

    /**
     * Insert record with primary key
     * Time: O(log n)
     *
     * TODO: Implement insert
     * - Add record to TreeMap using id as key
     * - TreeMap keeps data sorted by key
     */
    public void insert(Record record) {
        // TODO: Insert into index
        //   index.put(record.id, record);
    }

    /**
     * Point lookup by primary key
     * Time: O(log n)
     *
     * TODO: Implement get
     * - Return record by id
     */
    public Record get(long id) {
        // TODO: Lookup in index
        //   return index.get(id);

        return null; // Replace
    }

    /**
     * Range query: Get records where id BETWEEN start AND end
     * Time: O(log n + k) where k = results
     *
     * TODO: Implement range query
     * - Use TreeMap.subMap for efficient range
     */
    public List<Record> rangeQuery(long startId, long endId) {
        List<Record> results = new ArrayList<>();

        // TODO: Get submap from startId to endId (inclusive)
        //   NavigableMap<Long, Record> range = index.subMap(startId, true, endId, true);
        //   results.addAll(range.values());

        return results; // Replace
    }

    /**
     * Delete by primary key
     * Time: O(log n)
     *
     * TODO: Implement delete
     */
    public void delete(long id) {
        // TODO: Remove from index
        //   index.remove(id);
    }

    public int size() {
        return index.size();
    }
}
```

---

### Part 2: Secondary Index (Non-Clustered)

**Your task:** Implement a secondary index that points to primary keys.

```java
import java.util.*;

/**
 * Secondary Index: Points to primary keys, not actual data
 *
 * Properties:
 * - Multiple per table
 * - Separate from data
 * - Stores: secondary key → primary key(s)
 * - Requires additional lookup in primary index
 */
public class SecondaryIndex {

    private PrimaryIndex primaryIndex; // Reference to primary
    private TreeMap<Long, Set<Long>> index; // secondary key → set of primary keys
    private String indexName;

    public SecondaryIndex(String indexName, PrimaryIndex primaryIndex) {
        this.indexName = indexName;
        this.primaryIndex = primaryIndex;
        this.index = new TreeMap<>();
    }

    /**
     * Add entry to secondary index
     * Time: O(log n)
     *
     * TODO: Implement add
     * - Map secondary key to primary key
     * - One secondary key can map to multiple primary keys
     */
    public void add(long secondaryKey, long primaryKey) {
        // TODO: Add to index
        //   index.computeIfAbsent(secondaryKey, k -> new HashSet<>())
        //        .add(primaryKey);
    }

    /**
     * Query by secondary key
     * Time: O(log n + k) where k = matching records
     *
     * TODO: Implement query
     * 1. Lookup secondary key to get primary keys
     * 2. For each primary key, fetch actual record from primary index
     */
    public List<Record> query(long secondaryKey) {
        List<Record> results = new ArrayList<>();

        // TODO: Get primary keys for secondary key
        //   Set<Long> primaryKeys = index.get(secondaryKey);
        //   if (primaryKeys == null) return results;

        // TODO: Fetch each record from primary index
        //   for (Long primaryKey : primaryKeys) {
        //     Record record = primaryIndex.get(primaryKey);
        //     if (record != null) results.add(record);
        //   }

        return results; // Replace
    }

    /**
     * Range query on secondary key
     * Time: O(log n + k)
     *
     * TODO: Implement range query
     */
    public List<Record> rangeQuery(long startKey, long endKey) {
        List<Record> results = new ArrayList<>();

        // TODO: Get range of secondary keys
        //   NavigableMap<Long, Set<Long>> range =
        //     index.subMap(startKey, true, endKey, true);

        // TODO: For each secondary key, fetch all records
        //   for (Set<Long> primaryKeys : range.values()) {
        //     for (Long primaryKey : primaryKeys) {
        //       Record record = primaryIndex.get(primaryKey);
        //       if (record != null) results.add(record);
        //     }
        //   }

        return results; // Replace
    }

    /**
     * Remove entry from secondary index
     * Time: O(log n)
     *
     * TODO: Implement remove
     */
    public void remove(long secondaryKey, long primaryKey) {
        // TODO: Remove mapping
        //   Set<Long> primaryKeys = index.get(secondaryKey);
        //   if (primaryKeys != null) {
        //     primaryKeys.remove(primaryKey);
        //     if (primaryKeys.isEmpty()) {
        //       index.remove(secondaryKey);
        //     }
        //   }
    }
}
```

---

### Part 3: Composite Index (Multi-Column)

**Your task:** Implement a composite index on multiple columns.

```java
import java.util.*;

/**
 * Composite Index: Index on multiple columns
 *
 * Key insight: Column order matters!
 * Index on (col1, col2, col3) can serve:
 * - WHERE col1 = ?
 * - WHERE col1 = ? AND col2 = ?
 * - WHERE col1 = ? AND col2 = ? AND col3 = ?
 *
 * But NOT:
 * - WHERE col2 = ? (skips col1)
 * - WHERE col3 = ? (skips col1 and col2)
 */

class CompositeKey implements Comparable<CompositeKey> {
    long col1;
    long col2;

    public CompositeKey(long col1, long col2) {
        this.col1 = col1;
        this.col2 = col2;
    }

    @Override
    public int compareTo(CompositeKey other) {
        // Compare col1 first, then col2
        int cmp = Long.compare(this.col1, other.col1);
        if (cmp != 0) return cmp;
        return Long.compare(this.col2, other.col2);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CompositeKey)) return false;
        CompositeKey other = (CompositeKey) obj;
        return col1 == other.col1 && col2 == other.col2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col1, col2);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", col1, col2);
    }
}

public class CompositeIndex {

    private PrimaryIndex primaryIndex;
    private TreeMap<CompositeKey, Set<Long>> index;
    private String indexName;

    public CompositeIndex(String indexName, PrimaryIndex primaryIndex) {
        this.indexName = indexName;
        this.primaryIndex = primaryIndex;
        this.index = new TreeMap<>();
    }

    /**
     * Add entry to composite index
     * Time: O(log n)
     *
     * TODO: Implement add
     */
    public void add(long col1, long col2, long primaryKey) {
        CompositeKey key = new CompositeKey(col1, col2);

        // TODO: Add to index
        //   index.computeIfAbsent(key, k -> new HashSet<>())
        //        .add(primaryKey);
    }

    /**
     * Query with both columns (exact match)
     * Time: O(log n + k)
     *
     * TODO: Implement exact query
     */
    public List<Record> query(long col1, long col2) {
        List<Record> results = new ArrayList<>();
        CompositeKey key = new CompositeKey(col1, col2);

        // TODO: Lookup and fetch records
        //   Set<Long> primaryKeys = index.get(key);
        //   if (primaryKeys != null) {
        //     for (Long pk : primaryKeys) {
        //       results.add(primaryIndex.get(pk));
        //     }
        //   }

        return results; // Replace
    }

    /**
     * Query with first column only
     * Time: O(log n + k)
     *
     * TODO: Implement prefix query
     * - Can use index efficiently for first column
     * - Create range: col1 from (col1, MIN) to (col1, MAX)
     */
    public List<Record> queryByFirstColumn(long col1) {
        List<Record> results = new ArrayList<>();

        // TODO: Create range for all keys with matching col1
        //   CompositeKey start = new CompositeKey(col1, Long.MIN_VALUE);
        //   CompositeKey end = new CompositeKey(col1, Long.MAX_VALUE);
        //
        //   NavigableMap<CompositeKey, Set<Long>> range =
        //     index.subMap(start, true, end, true);
        //
        //   for (Set<Long> primaryKeys : range.values()) {
        //     for (Long pk : primaryKeys) {
        //       results.add(primaryIndex.get(pk));
        //     }
        //   }

        return results; // Replace
    }

    /**
     * Range query on composite key
     * Example: WHERE col1 = 100 AND col2 BETWEEN 50 AND 100
     * Time: O(log n + k)
     *
     * TODO: Implement range query
     */
    public List<Record> rangeQuery(long col1, long col2Start, long col2End) {
        List<Record> results = new ArrayList<>();

        // TODO: Create range keys
        //   CompositeKey start = new CompositeKey(col1, col2Start);
        //   CompositeKey end = new CompositeKey(col1, col2End);

        // TODO: Get range and fetch records

        return results; // Replace
    }
}
```

---

## Client Code

```java
public class IndexingClient {

    public static void main(String[] args) {
        testPrimaryIndex();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testSecondaryIndex();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testCompositeIndex();
    }

    static void testPrimaryIndex() {
        System.out.println("=== Primary Index (Clustered) ===\n");

        PrimaryIndex posts = new PrimaryIndex();

        // Insert posts
        posts.insert(new Record(1, 101, "Hello World", 1000));
        posts.insert(new Record(5, 102, "Java Tips", 2000));
        posts.insert(new Record(10, 101, "Design Patterns", 3000));
        posts.insert(new Record(15, 103, "Algorithms", 4000));
        posts.insert(new Record(20, 102, "Data Structures", 5000));

        System.out.println("Inserted " + posts.size() + " posts");

        // Point lookup
        System.out.println("\n--- Point Lookup ---");
        Record post = posts.get(10);
        System.out.println("Post 10: " + post);

        // Range query
        System.out.println("\n--- Range Query (ids 5-15) ---");
        List<Record> range = posts.rangeQuery(5, 15);
        for (Record r : range) {
            System.out.println("  " + r);
        }
    }

    static void testSecondaryIndex() {
        System.out.println("=== Secondary Index (Non-Clustered) ===\n");

        PrimaryIndex posts = new PrimaryIndex();
        SecondaryIndex userIndex = new SecondaryIndex("user_id_idx", posts);

        // Insert posts and build secondary index
        Record[] records = {
            new Record(1, 101, "Post 1", 1000),
            new Record(2, 102, "Post 2", 2000),
            new Record(3, 101, "Post 3", 3000),
            new Record(4, 103, "Post 4", 4000),
            new Record(5, 101, "Post 5", 5000)
        };

        for (Record r : records) {
            posts.insert(r);
            userIndex.add(r.userId, r.id); // Map userId -> postId
        }

        System.out.println("Created secondary index on user_id");

        // Query by user_id (secondary key)
        System.out.println("\n--- Query: Posts by user 101 ---");
        List<Record> userPosts = userIndex.query(101);
        for (Record r : userPosts) {
            System.out.println("  " + r);
        }
    }

    static void testCompositeIndex() {
        System.out.println("=== Composite Index (user_id, created_at) ===\n");

        PrimaryIndex posts = new PrimaryIndex();
        CompositeIndex userTimeIndex = new CompositeIndex("user_time_idx", posts);

        // Insert posts
        Record[] records = {
            new Record(1, 101, "Morning post", 1000),
            new Record(2, 101, "Afternoon post", 2000),
            new Record(3, 102, "Evening post", 1500),
            new Record(4, 101, "Night post", 3000),
            new Record(5, 102, "Late night", 2500)
        };

        for (Record r : records) {
            posts.insert(r);
            userTimeIndex.add(r.userId, r.createdAt, r.id);
        }

        System.out.println("Created composite index on (user_id, created_at)");

        // Query with both columns
        System.out.println("\n--- Exact Match: user=101, time=2000 ---");
        List<Record> exact = userTimeIndex.query(101, 2000);
        for (Record r : exact) {
            System.out.println("  " + r);
        }

        // Query with first column only
        System.out.println("\n--- Prefix Query: user=101 (all times) ---");
        List<Record> byUser = userTimeIndex.queryByFirstColumn(101);
        for (Record r : byUser) {
            System.out.println("  " + r);
        }
    }
}
```

---

## Decision Framework

**Questions to answer after implementation:**

### 1. Primary vs Secondary Index?

**Primary Index:**
- When: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Secondary Index:**
- When: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

### 2. When to use Composite Index?

**Use composite index when:**
- Query filters: _[Multiple columns together]_
- Column order: _[Fill in rule]_
- Example: _[Fill in]_

**Column ordering rule:**
1. _[Columns in equality filters first]_
2. _[Then range filter columns]_
3. _[Then sort columns]_

### 3. Covering Index?

**What is it:** _[Index contains all columns needed by query]_

**Benefit:** _[No need to access main table]_

**Example:** _[Fill in after practice]_

### Your Decision Tree

Build this after solving practice scenarios:

```
Should I create an index?
│
├─ Query uses this column in WHERE?
│   ├─ NO → Skip index
│   └─ YES → Continue
│
├─ Column has high cardinality?
│   ├─ NO (few distinct values) → Skip index
│   └─ YES (many distinct values) → Continue
│
├─ Table is write-heavy?
│   ├─ YES → Consider trade-off (indexes slow writes)
│   └─ NO → Safe to create index
│
├─ Multiple columns in WHERE clause?
│   ├─ YES → Use composite index
│   │   └─ Order: Equality → Range → Sort
│   └─ NO → Use single-column index
│
└─ Query selects many columns?
    ├─ FEW → Consider covering index
    └─ MANY → Regular index
```

### The "Kill Switch" - Don't index when:

1. **Low cardinality** - _[Column has few distinct values (e.g., boolean, status with 3 values)]_
2. **Write-heavy workload** - _[Index maintenance slows inserts/updates/deletes]_
3. **Small table** - _[Table scan faster than index for tiny tables]_
4. **Never queried** - _[Index on column never used in WHERE/JOIN/ORDER BY]_
5. **Too many indexes** - _[Each index has memory/write cost]_

### The Rule of Three: Alternatives

**Option 1: B+Tree Index (TreeMap)**
- Pros: _[Range queries, sorted order, balanced]_
- Cons: _[Write overhead, memory usage]_
- Use when: _[Range queries, ORDER BY, most general case]_

**Option 2: Hash Index (HashMap)**
- Pros: _[O(1) lookups, less memory]_
- Cons: _[No range queries, no sorting]_
- Use when: _[Exact match only, high cardinality]_

**Option 3: No Index (Table Scan)**
- Pros: _[No write overhead, no memory cost]_
- Cons: _[O(n) scans]_
- Use when: _[Small tables, write-heavy, infrequent queries]_

---

## Practice

### Scenario 1: Posts Table

**Schema:**
```sql
CREATE TABLE posts (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    created_at TIMESTAMP,
    likes_count INT,
    content TEXT
);
```

**Queries:**
1. Get posts by user, sorted by recency
2. Top posts by likes in last 24h
3. User's posts with > 100 likes

**Your index design:**
- Index 1: _[Fill in]_
- Index 2: _[Fill in]_
- Reasoning: _[Why these indexes?]_

---

### Scenario 2: Orders Table

**Schema:**
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    customer_id BIGINT,
    status VARCHAR(20), -- 'pending', 'shipped', 'delivered'
    created_at TIMESTAMP,
    total_amount DECIMAL
);
```

**Queries:**
1. Customer's orders by recency
2. Pending orders older than 24h
3. High-value orders (amount > $1000) this month

**Your index design:**
- Index 1: _[Fill in]_
- Index 2: _[Fill in]_
- Reasoning: _[Explain your choices]_

---

### Scenario 3: Events Table (Time-Series)

**Schema:**
```sql
CREATE TABLE events (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    event_type VARCHAR(50),
    timestamp TIMESTAMP,
    properties JSON
);
```

**Queries:**
1. User's events in time range
2. All 'click' events today
3. Events by type for specific user

**Your index design:**
- Index 1: _[Fill in]_
- Index 2: _[Fill in]_
- Why composite? _[Explain]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
  - [ ] Primary index works for insert/get/range/delete
  - [ ] Secondary index correctly maps to primary keys
  - [ ] Composite index handles both columns
  - [ ] All client code runs successfully

- [ ] **Understanding**
  - [ ] Filled in all ELI5 explanations
  - [ ] Understand clustered vs non-clustered
  - [ ] Know composite index column ordering rules
  - [ ] Understand covering indexes

- [ ] **Decision Making**
  - [ ] Built decision tree for index creation
  - [ ] Know when NOT to create index
  - [ ] Completed practice scenarios
  - [ ] Can explain index trade-offs

- [ ] **Mastery Check**
  - [ ] Could design indexes for new table
  - [ ] Understand query plan implications
  - [ ] Know how to validate index usage
  - [ ] Can explain to someone else

---

**Next:** [03. Caching Patterns →](03-caching-patterns.md)

**Back:** [01. Storage Engines ←](01-storage-engines.md)
