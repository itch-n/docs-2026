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

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition about indexes before writing code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Full table scan to find a record:**
    - Time complexity: _[Your guess: O(?)]_
    - Verified after learning: _[Actual: O(?)]_

2. **B+tree index lookup for a single record:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified: _[Actual]_

3. **Performance calculation:**
    - If n = 1,000,000 records, table scan = _____ disk reads
    - With B+tree index (height=4) = _____ disk reads
    - Speedup factor: _____ times faster

### Scenario Predictions

**Scenario 1:** Query: `SELECT * FROM users WHERE email = 'john@example.com'`
- Table has 10 million users
- No index on email column

- **How many rows scanned?** _[Your guess]_
- **Approximate time?** _[Fast/Medium/Slow - Why?]_
- **Would index help?** _[Yes/No - Why?]_

**Scenario 2:** Query: `SELECT * FROM orders WHERE customer_id = 42 AND created_at > '2024-01-01'`
- Table has 5 million orders
- Index option A: Single index on `customer_id`
- Index option B: Composite index on `(customer_id, created_at)`

- **Which index is better?** _[A/B - Why?]_
- **Can you use just `created_at` index?** _[Yes/No - Why?]_

**Scenario 3:** Query: `SELECT * FROM products WHERE category = 'electronics'`
- Table has 1 million products
- Category column has only 5 distinct values (books, electronics, clothing, toys, food)

- **Should you create an index on category?** _[Yes/No - Why?]_
- **What's the problem with this index?** _[Fill in]_

### Index Type Quiz

**Question:** When is a hash index BETTER than a B+tree index?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question:** What's the MAIN benefit of a covering index?

- [ ] Faster writes
- [ ] Uses less disk space
- [ ] Query doesn't need to access table data
- [ ] Better for range queries

Verify after implementation: _[Which one?]_

**Question:** In a composite index on (A, B, C), which queries can use the index?

- [ ] WHERE A = ?
- [ ] WHERE B = ?
- [ ] WHERE A = ? AND B = ?
- [ ] WHERE A = ? AND C = ?
- [ ] WHERE B = ? AND C = ?

Verify after implementation: _[Which ones and why?]_

---

## Before/After: Why Indexes Matter

**Your task:** Compare performance with and without indexes to understand the impact.

### Example: Find User by Email

**Problem:** Find a user record by email in a table with 1 million users.

#### Approach 1: Full Table Scan (No Index)

```java
// No index - must scan every record
public class TableScan {
    private List<User> users; // 1 million records

    /**
     * Find user by email - must check every record
     * Time: O(n) where n = number of records
     */
    public User findByEmail(String email) {
        for (User user : users) {
            if (user.email.equals(email)) {
                return user;
            }
        }
        return null;
    }
}
```

**Analysis:**

- Time: O(n) - Must scan entire table
- For n = 1,000,000: ~1,000,000 comparisons
- Disk I/O: Read ALL pages from disk (assume 100 records/page = 10,000 disk reads)
- Average time: ~2-5 seconds on spinning disk

#### Approach 2: B+Tree Index (Optimized)

```java
// With B+tree index on email column
public class BTreeIndexedTable {
    private TreeMap<String, User> emailIndex; // B+tree structure

    /**
     * Find user by email - use index
     * Time: O(log n) where n = number of records
     */
    public User findByEmail(String email) {
        return emailIndex.get(email); // Direct lookup via B+tree
    }
}
```

**Analysis:**

- Time: O(log n) - Navigate tree levels
- For n = 1,000,000: ~20 comparisons (log₁₀₀(1,000,000) ≈ 3-4 tree levels)
- Disk I/O: Read only 4-5 pages (one per tree level)
- Average time: ~5-10ms on spinning disk

#### Performance Comparison

| Table Size | No Index (O(n)) | B+Tree Index (O(log n)) | Speedup |
|------------|-----------------|-------------------------|---------|
| n = 1,000  | 1,000 ops      | 10 ops                  | 100x    |
| n = 10,000 | 10,000 ops     | 13 ops                  | 769x    |
| n = 100,000| 100,000 ops    | 17 ops                  | 5,882x  |
| n = 1,000,000 | 1,000,000 ops | 20 ops               | 50,000x |

**Your calculation:** For n = 5,000,000, the speedup is approximately _____ times faster.

#### Real-World Example: Disk I/O Impact

**Setup:**

- 1 million user records
- Each database page holds 100 records
- Disk seek time: 5ms per page read

**Without Index:**
```
Total pages to scan: 1,000,000 / 100 = 10,000 pages
Disk I/O time: 10,000 pages × 5ms = 50,000ms = 50 seconds
```

**With B+Tree Index (height=4):**
```
Tree levels to traverse: 4 pages (root → branch → branch → leaf)
Then read data page: 1 page
Total disk I/O: 5 pages × 5ms = 25ms
```

**Result:** 50 seconds → 25ms = **2,000x faster**

#### Why Does B+Tree Index Work?

**Key insight to understand:**

B+Tree organizes data hierarchically:

```
                    [Root: J, S]
                   /      |      \
              [A-I]    [J-R]    [S-Z]
              /  \      /  \      /  \
          [A-D][E-I][J-M][N-R][S-V][W-Z]
```

Looking for email starting with 'M':
```
Step 1: Read root → "M is between J-R" → Go middle branch
Step 2: Read branch → "M is between J-M" → Go left child
Step 3: Read leaf → Found email starting with M!
Step 4: Read data page → Got user record

Total: 4 disk reads instead of 10,000
```

**After implementing, explain in your own words:**

- _[Why does tree height stay small even for millions of records?]_
- _[What's the trade-off: why do inserts become slower?]_
- _[When would a table scan actually be faster than using an index?]_

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

## Debugging Challenges

**Your task:** Diagnose and fix index-related bugs. This tests your deep understanding of indexing.

### Challenge 1: Missing Index Performance Bug

```sql
-- Table with 5 million orders
CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    customer_id BIGINT,
    created_at TIMESTAMP,
    status VARCHAR(20),
    total_amount DECIMAL
);

-- Index exists
CREATE INDEX idx_customer ON orders(customer_id);

-- Query is SLOW (takes 30 seconds!)
SELECT *
FROM orders
WHERE customer_id = 12345
  AND created_at >= '2024-01-01'
ORDER BY created_at DESC
LIMIT 10;
```

**Your debugging:**

- **Bug identification:** _[Why is this query slow despite having an index?]_
- **What's happening:** _[Explain the query execution]_
    - Step 1: _[What does the index do?]_
    - Step 2: _[What happens after using the index?]_
    - Step 3: _[Why is this inefficient?]_

- **The fix:** _[What index would make this fast?]_
- **Better index:**
  ```sql
  CREATE INDEX _____ ON orders(_____);
  ```

- **Why is the new index better?** _[Fill in]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug:** The query uses `idx_customer(customer_id)` to find all orders for customer 12345. But then it must:
1. Scan all those results (could be 10,000 orders)
2. Filter by `created_at >= '2024-01-01'` (another scan)
3. Sort by `created_at` (expensive!)
4. Take top 10

**Fix:** Create composite index:
```sql
CREATE INDEX idx_customer_time ON orders(customer_id, created_at);
```

**Why better:**

- Index already sorted by (customer_id, created_at)
- Can seek directly to customer 12345, date 2024-01-01
- No need to sort (data already in order)
- Just read first 10 matching records
- Time: O(log n + 10) instead of O(n log n)

</details>

---

### Challenge 2: Over-Indexing Write Slowdown

```java
/**
 * Insert performance degraded from 1000 inserts/sec to 50 inserts/sec!
 * Table has 6 indexes. Find the problematic ones.
 */

// Schema
CREATE TABLE events (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    event_type VARCHAR(50),
    created_at TIMESTAMP,
    processed BOOLEAN,      -- only TRUE or FALSE
    metadata JSON
);

// Indexes
CREATE INDEX idx_user ON events(user_id);
CREATE INDEX idx_type ON events(event_type);
CREATE INDEX idx_time ON events(created_at);
CREATE INDEX idx_processed ON events(processed);  -- Suspicious?
CREATE INDEX idx_user_type ON events(user_id, event_type);
CREATE INDEX idx_user_time ON events(user_id, created_at);

// Queries we actually run
SELECT * FROM events WHERE user_id = ? AND event_type = ?;
SELECT * FROM events WHERE user_id = ? AND created_at > ?;
```

**Your debugging:**

- **Problem indexes:** _[Which indexes are hurting performance?]_
  1. _[Index name and reason]_
  2. _[Index name and reason]_

- **Why these are bad:**
    - _[What happens on every INSERT?]_
    - _[Calculate: inserting 1 row updates how many indexes?]_

- **Which indexes to DROP:**
  ```sql
  DROP INDEX _____;
  DROP INDEX _____;
  ```

- **Which indexes to KEEP:** _[List and explain why]_

<details markdown>
<summary>Click to verify your answers</summary>

**Problematic indexes:**

1. **`idx_processed`** - Low cardinality (only 2 values: true/false)
    - Database will likely do table scan anyway
    - Index barely helps queries
    - But slows every INSERT/UPDATE

2. **`idx_user`** - Redundant! Covered by `idx_user_type` and `idx_user_time`
    - Composite index on (user_id, X) can serve queries on just user_id
    - No benefit, just write overhead

3. **`idx_type`** - Likely redundant
    - Query always filters by user_id AND event_type (uses `idx_user_type`)
    - Rarely filter by type alone
    - If needed occasionally, table scan on event_type is acceptable

**Keep:**

- Primary key (must keep)
- `idx_user_type` (matches first query)
- `idx_user_time` (matches second query)
- `idx_time` (only if we have queries that filter by created_at alone)

**Impact:** Every INSERT was updating 7 structures (1 table + 6 indexes). Dropping 3 indexes = ~2x faster writes.

</details>

---

### Challenge 3: Wrong Index Type

```java
/**
 * Hash index vs B+tree index confusion
 * Query performance is bad despite having an index!
 */

// Table with 10 million products
CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    sku VARCHAR(50),
    name VARCHAR(200),
    price DECIMAL,
    category_id INT
);

// Created hash index (in databases that support it)
CREATE INDEX idx_price USING HASH ON products(price);

// This query is FAST
SELECT * FROM products WHERE price = 29.99;

// This query is SLOW (doesn't use index!)
SELECT * FROM products WHERE price BETWEEN 20.00 AND 50.00;

// This query is also SLOW
SELECT * FROM products WHERE price > 100.00 ORDER BY price;
```

**Your debugging:**

- **What's wrong with the hash index?** _[Fill in]_

- **Why does exact match work but range fails?** _[Explain]_
    - Hash index: _[What does it optimize for?]_
    - Range query needs: _[What property?]_

- **The fix:** _[What index type should we use?]_
  ```sql
  DROP INDEX idx_price;
  CREATE INDEX _____ ON products(_____);
  ```

- **When WOULD a hash index be appropriate?** _[Give an example]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug:** Hash index only supports equality checks (=), not ranges or ordering.

**Why:**

- Hash index: `hash(29.99) → bucket 1847` (fast exact lookup)
- But hash(30.00) might → bucket 4521 (no adjacency!)
- Hash destroys ordering, so range queries can't work

**Fix:**
```sql
DROP INDEX idx_price USING HASH;
CREATE INDEX idx_price USING BTREE ON products(price);
```

**When to use hash index:**

- Exact match queries only: `WHERE id = ?`, `WHERE email = ?`
- No range queries needed
- No sorting needed
- Slightly faster equality checks and less memory
- Example: Index on UUID primary key with only point lookups

**When to use B+tree index:**

- Range queries: `WHERE price BETWEEN ? AND ?`
- Sorting: `ORDER BY created_at`
- Prefix matching: `WHERE name LIKE 'Apple%'`
- Default choice for most cases

</details>

---

### Challenge 4: Composite Index Column Order Bug

```java
/**
 * Composite index created but queries not using it!
 * EXPLAIN shows "table scan" instead of "index scan"
 */

// Created composite index
CREATE INDEX idx_search ON orders(created_at, customer_id, status);

// Query 1: Uses index ✓
SELECT * FROM orders
WHERE created_at >= '2024-01-01'
  AND customer_id = 12345
  AND status = 'pending';

// Query 2: Does NOT use index! (table scan)
SELECT * FROM orders
WHERE customer_id = 12345
  AND status = 'pending';

// Query 3: Partially uses index (inefficient)
SELECT * FROM orders
WHERE customer_id = 12345
  AND created_at >= '2024-01-01';
```

**Your debugging:**

- **Why doesn't Query 2 use the index?** _[Fill in]_
    - Composite index rule: _[Explain "leftmost prefix" rule]_
    - Query 2 skips: _[Which column?]_

- **Why is Query 3 inefficient?** _[Fill in]_

- **Fix the index column order:**
  ```sql
  DROP INDEX idx_search;
  CREATE INDEX idx_search ON orders(_____, _____, _____);
  ```
    - Order rule: _[Equality → Range → Sort]_

- **Verify each query:**
    - Query 1 with new index: _[Will it work?]_
    - Query 2 with new index: _[Will it work?]_
    - Query 3 with new index: _[Will it work?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug:** Column order violates the "leftmost prefix" rule.

**Leftmost prefix rule:** Composite index on (A, B, C) can serve:
- WHERE A = ?
- WHERE A = ? AND B = ?
- WHERE A = ? AND B = ? AND C = ?

But CANNOT serve:
- WHERE B = ? (skips A)
- WHERE C = ? (skips A and B)
- WHERE B = ? AND C = ? (skips A)

**Original index:** (created_at, customer_id, status)
- Query 2 filters on (customer_id, status) → skips created_at → index not used!
- Query 3 filters on (customer_id, created_at) → skips first column → can't seek efficiently

**Fix:** Reorder based on query patterns
```sql
CREATE INDEX idx_search ON orders(customer_id, created_at, status);
```

**Ordering rule:**

1. Equality filters first (customer_id = ?)
2. Range filters second (created_at >= ?)
3. Sort columns third (ORDER BY status)

**Verification:**

- Query 1 (all three): Uses index ✓
- Query 2 (customer_id, status): Uses index ✓ (leftmost prefix satisfied)
- Query 3 (customer_id, created_at): Uses index ✓ (leftmost prefix satisfied)

**Key insight:** Put the most selective/frequently queried columns first!

</details>

---

### Challenge 5: Index Not Used (Implicit Conversion)

```sql
-- Table schema
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255),
    phone VARCHAR(20),  -- Stored as string!
    created_at TIMESTAMP
);

-- Index exists
CREATE INDEX idx_phone ON users(phone);

-- Query 1: Uses index ✓
SELECT * FROM users WHERE phone = '1234567890';

-- Query 2: Does NOT use index! (table scan)
SELECT * FROM users WHERE phone = 1234567890;  -- Number instead of string
```

**Your debugging:**

- **Why doesn't Query 2 use the index?** _[Fill in]_
    - Data type in query: _[What type?]_
    - Data type in column: _[What type?]_
    - What happens: _[Implicit conversion]_

- **Similar bugs:**

```sql
-- Index on created_at (TIMESTAMP)
WHERE created_at = '2024-01-01'  -- String! Needs conversion

-- Index on status (VARCHAR)
WHERE status = 1  -- Number! Needs conversion
```

- **The rule:** _[Explain why type mismatch prevents index usage]_

- **How to detect in real databases:** _[What tool/command?]_
    - Hint: _[EXPLAIN or EXPLAIN ANALYZE]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug:** Implicit type conversion prevents index usage.

**Why:** Database must convert EVERY row's `phone` column from VARCHAR to INT to compare with 1234567890. This requires scanning the entire table.

**Rule:** When query value type doesn't match column type, database either:
1. Converts column values (requires table scan, index useless)
2. Converts query value (index can be used)

**Direction matters:**

- `WHERE phone = '1234567890'` → Query matches column type → index used
- `WHERE phone = 1234567890` → Must convert column → index not used

**Similar issues:**
```sql
-- Don't do this:
WHERE CAST(created_at AS DATE) = '2024-01-01'  -- Function on column = no index
WHERE UPPER(email) = 'JOHN@EXAMPLE.COM'  -- Function on column = no index

-- Do this instead:
WHERE created_at >= '2024-01-01' AND created_at < '2024-01-02'
WHERE email = 'john@example.com'  -- Store lowercase, search lowercase
```

**Detection:** Use EXPLAIN:
```sql
EXPLAIN SELECT * FROM users WHERE phone = 1234567890;
-- Shows: "Table scan" or "Full table scan" instead of "Index scan"
```

</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Identified missing composite index causing slow queries
- [ ] Found redundant and low-cardinality indexes hurting writes
- [ ] Understood hash vs B+tree index trade-offs
- [ ] Mastered composite index column ordering rules
- [ ] Recognized implicit conversion preventing index usage

**Common indexing mistakes you discovered:**

1. _[List the patterns you noticed]_
2. _[Fill in]_
3. _[Fill in]_

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

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about database indexes.

**Your explanation (write it out):**

> "A database index is..."
>
> _[Fill in your explanation in plain English - 3-4 sentences max]_

**Include in your explanation:**

- What problem indexes solve
- The trade-off they introduce
- When you should use them

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by a non-technical person? _[Yes/No]_
- Did you explain both benefits AND costs? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Index Design Exercise

**Task:** Design indexes for this table without looking at your notes.

**Schema:**
```sql
CREATE TABLE posts (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    title VARCHAR(200),
    content TEXT,
    likes_count INT,
    created_at TIMESTAMP,
    is_published BOOLEAN
);
```

**Queries (in order of frequency):**

1. Get user's published posts sorted by recency (runs 1000x/sec)
2. Find posts created in date range (runs 100x/sec)
3. Top liked posts in last 7 days (runs 10x/sec)
4. Search posts by title prefix (runs 50x/sec)

**Your index design:**

1. **Index 1:**
   ```sql
   CREATE INDEX _____ ON posts(_____);
   ```
    - Serves query: _[Which one?]_
    - Column order reasoning: _[Why this order?]_

2. **Index 2:**
   ```sql
   CREATE INDEX _____ ON posts(_____);
   ```
    - Serves query: _[Which one?]_
    - Why needed: _[Can't Index 1 serve this?]_

3. **Index 3 (if needed):**
   ```sql
   CREATE INDEX _____ ON posts(_____);
   ```
    - Or: _[Maybe we don't need this one? Explain.]_

**Indexes you WON'T create:**

- `is_published` alone → Why not? _[Low cardinality]_
- `likes_count` alone → Why not? _[Fill in]_

**Verification:**

- [ ] Considered query frequency in design
- [ ] Applied composite index column ordering rules
- [ ] Avoided redundant indexes
- [ ] Didn't over-index low-cardinality columns

---

### Gate 3: Debugging Real Query

**Given:**
```sql
-- Existing indexes
CREATE INDEX idx_user ON orders(user_id);
CREATE INDEX idx_created ON orders(created_at);

-- Query taking 45 seconds
EXPLAIN ANALYZE
SELECT *
FROM orders
WHERE user_id = 12345
  AND created_at >= '2024-01-01'
  AND status = 'pending'
ORDER BY created_at DESC
LIMIT 20;

-- EXPLAIN output shows:
-- 1. Index Scan using idx_user (cost=0.56..1234.56 rows=2500)
-- 2. Filter: created_at >= '2024-01-01' AND status = 'pending'
-- 3. Sort: created_at DESC (cost=5678.90)
```

**Your analysis:**

1. **What index is being used?** _[Fill in]_

2. **What's happening after the index scan?** _[List the steps]_
    - _[Step 1]_
    - _[Step 2]_
    - _[Step 3]_

3. **Why is this slow?** _[Identify the bottleneck]_

4. **Your solution:**
   ```sql
   CREATE INDEX _____ ON orders(_____);
   ```

5. **Why your index is better:** _[Explain the improvement]_
    - Reduces rows scanned from _____ to _____
    - Eliminates _[which operation?]_
    - Estimated new query time: _____

6. **Would a covering index help more?** _[Yes/No - Explain]_
   ```sql
   CREATE INDEX _____ ON orders(_____, _____, _____) INCLUDE (_____);
   ```

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Index Type | Point Lookup | Range Query | Insert | Space |
|------------|-------------|-------------|---------|-------|
| B+Tree     | O(?)        | O(?)        | O(?)    | O(?)  |
| Hash       | O(?)        | O(?)        | O(?)    | O(?)  |
| No Index   | O(?)        | O(?)        | O(?)    | O(?)  |

**Deep questions:**

1. **Why is B+tree insert O(log n) but table scan insert is O(1)?**
    - Your answer: _[Fill in - explain the trade-off]_

2. **A table with 10 million rows and 5 secondary indexes:**
    - Single insert without indexes: ___ operations
    - Single insert with indexes: ___ operations (1 table + 5 indexes)
    - Write amplification factor: _____

3. **Hash index is O(1) but B+tree is O(log n). Why use B+tree?**
    - Your answer: _[List at least 3 reasons]_

---

### Gate 5: Trade-off Decision

**Scenario:** E-commerce orders table with 100 million rows.

**Current situation:**

- 1,000 writes/second (new orders)
- 10,000 reads/second (order lookups)
- Read queries use: `WHERE customer_id = ? AND created_at > ?`

**Two engineers propose different solutions:**

**Engineer A:** "Add composite index on (customer_id, created_at)"

- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Impact on writes: _[Slower/Same/Faster - Why?]_
- Impact on reads: _[Slower/Same/Faster - Why?]_

**Engineer B:** "No index, partition table by created_at month, scan partitions"

- Pros: _[Fill in]_
- Cons: _[Fill in]_
- Impact on writes: _[Slower/Same/Faster - Why?]_
- Impact on reads: _[Slower/Same/Faster - Why?]_

**Your decision:** I would choose _[A/B]_ because...

_[Fill in your reasoning - consider read/write ratio, scalability, maintenance]_

**What would make you change your decision?**

- If write volume increases to _____: _[Would you reconsider?]_
- If read patterns change to _____: _[Would you reconsider?]_

---

### Gate 6: Column Ordering Master Challenge

**For each composite index, mark which queries can use it efficiently:**

**Index:** `CREATE INDEX idx ON orders(customer_id, status, created_at)`

| Query | Uses Index? | Efficiency |
|-------|-------------|------------|
| `WHERE customer_id = ?` | _[Y/N]_ | _[Full/Partial/None]_ |
| `WHERE status = ?` | _[Y/N]_ | _[Full/Partial/None]_ |
| `WHERE customer_id = ? AND status = ?` | _[Y/N]_ | _[Full/Partial/None]_ |
| `WHERE customer_id = ? AND created_at > ?` | _[Y/N]_ | _[Full/Partial/None]_ |
| `WHERE customer_id = ? AND status = ? AND created_at > ?` | _[Y/N]_ | _[Full/Partial/None]_ |
| `WHERE status = ? AND created_at > ?` | _[Y/N]_ | _[Full/Partial/None]_ |

**For each "Partial" efficiency, explain why:**

- _[Fill in the limitation]_

**Optimal column order for these queries:**
```sql
CREATE INDEX idx_optimal ON orders(_____, _____, _____);
```
- Reasoning: _[Why this order?]_

---

### Gate 7: Covering Index Deep Dive

**Schema:**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255),
    name VARCHAR(100),
    age INT,
    city VARCHAR(100),
    created_at TIMESTAMP
);

-- Frequent query
SELECT name, city
FROM users
WHERE email = 'john@example.com';
```

**Option 1: Simple secondary index**
```sql
CREATE INDEX idx_email ON users(email);
```

**Query execution:**

1. _[Use index to find row ID]_
2. _[Access main table to get name and city]_
3. _[Total disk reads: ___ ]_

**Option 2: Covering index**
```sql
CREATE INDEX idx_email_covering ON users(email) INCLUDE (name, city);
-- Or: CREATE INDEX idx_email_covering ON users(email, name, city);
```

**Query execution:**

1. _[Use index to find row]_
2. _[Get name and city from index itself]_
3. _[Total disk reads: ___ ]_

**Analysis:**

- Speed improvement: _[Approximately how much faster?]_
- Space cost: _[How much larger is the index?]_
- Write cost: _[Impact on INSERT/UPDATE]_

**When NOT to use covering index:**

- _[Scenario 1]_
- _[Scenario 2]_

---

### Gate 8: Real-World Diagnosis

**You're debugging slow queries in production. Given this EXPLAIN output:**

```sql
EXPLAIN ANALYZE
SELECT *
FROM products
WHERE category = 'electronics'
  AND price > 100
  AND brand = 'Apple';

-- Output:
-- Seq Scan on products (cost=0.00..1234567.89 rows=50000)
--   Filter: category = 'electronics' AND price > 100 AND brand = 'Apple'
-- Planning time: 0.123 ms
-- Execution time: 45678.901 ms
```

**Analysis:**

1. **What's wrong?** _[No index being used]_

2. **How can you tell?** _[Seq Scan = table scan]_

3. **Design the optimal index:**
   ```sql
   CREATE INDEX _____ ON products(_____);
   ```

4. **Column order reasoning:**
    - First column: _____ because _____
    - Second column: _____ because _____
    - Third column: _____ because _____

5. **Expected improvement:**
    - Rows examined: 50,000 → _____
    - Execution time: 45 seconds → _____

---

### Mastery Certification

**I certify that I can:**

- [ ] Explain indexes clearly to any audience
- [ ] Design efficient indexes for complex query patterns
- [ ] Apply composite index column ordering rules correctly
- [ ] Identify and debug index performance issues
- [ ] Analyze query execution plans
- [ ] Make informed trade-off decisions
- [ ] Recognize when NOT to use indexes
- [ ] Teach indexing concepts to others

**Self-assessment score:** ___/10

**Areas needing more practice:**

1. _[Fill in if any]_
2. _[Fill in if any]_

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered database indexing. Proceed to the next topic.
