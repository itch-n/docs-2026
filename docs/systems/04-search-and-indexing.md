# Search & Indexing

> Inverted indexes, full-text search, ranking algorithms, and distributed search systems

---

## Learning Objectives

By the end of this topic you will be able to:

- Explain the structure of an inverted index and why it enables sub-second search over millions of documents
- Implement a basic inverted index with tokenisation, normalisation, and boolean query intersection
- Compare TF-IDF and BM25 scoring and explain why BM25 produces better ranking for varied document lengths
- Identify which text analysis technique (stemming, n-grams, fuzzy matching) is appropriate for a given search requirement
- Design a sharding and replication strategy for a distributed search cluster given throughput and availability requirements
- Choose between trie-based and edge-n-gram autocomplete given performance and storage constraints

---

!!! warning "Operational reality"
    Elasticsearch is significantly over-adopted. Postgres full-text search (`tsvector`, `GIN` indexes, `ts_rank`) handles the majority of "we need search" use cases without taking on a new distributed system to operate. Elasticsearch's practical issues are non-trivial: heap sizing is fiddly and getting it wrong causes OOM failures; mapping explosions (too many dynamic fields) degrade performance silently; reindexing a live index requires building a parallel index and swapping an alias — there is no in-place schema migration. Older clusters had split-brain failure modes that were a rite of passage for many backend teams.

    Reach for Elasticsearch (or OpenSearch, Typesense, Meilisearch) when you need relevance ranking, faceted search, or query complexity that genuinely exceeds what Postgres can express. For "search this table by name or description," check Postgres first.

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After learning search and indexing, explain them simply.

**Prompts to guide you:**

1. **What is an inverted index in one sentence?**
    - An inverted index is a data structure that maps each ___ to the list of <span class="fill-in">[___ that contain it, so a search query only needs to look up ___ rather than scanning every ___]</span>

2. **Why can't we just search through all documents linearly?**
    - Linear search is impractical because <span class="fill-in">[with N documents each of size M, every query costs O(___), which at 1M documents means ___ comparisons per search — roughly ___ ms per query]</span>

3. **Real-world analogy for inverted index:**
    - Example: "An inverted index is like a book's index where..."
    - Think about how a book's back-of-page index lists each topic and then the page numbers where it appears — rather than you reading every page to find a topic.
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What makes search results "relevant"?**
    - Relevance is determined by <span class="fill-in">[how ___ a term appears in a document (___), balanced against how ___ the term is across all documents (___), so common words like "the" get ___ weight]</span>

5. **Real-world analogy for search ranking:**
    - Example: "TF-IDF is like voting where..."
    - Think about how a reference appearing many times in a single thesis (high TF) but rarely in any other thesis (high IDF) signals a very specific and important topic.
    - Your analogy: <span class="fill-in">[Fill in]</span>

6. **Why do we need sharding for search?**
    - Sharding is needed when <span class="fill-in">[the index is too large to fit on ___ machine, so it is split across ___ shards that each search ___ of the data in ___, with results merged by ___]</span>

</div>

---

## Quick Quiz (Do BEFORE learning)

!!! tip "How to use this section"
    Complete your predictions now, before reading further. You will revisit and verify each answer after working through the core concepts.

<div class="learner-section" markdown>

**Your task:** Test your intuition about search without looking at details. Answer these, then verify after learning.

### Complexity Predictions

1. **Linear search through 1M documents:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Estimated time: <span class="fill-in">[Milliseconds? Seconds?]</span>
    - Verified: <span class="fill-in">[Actual]</span>

2. **Inverted index lookup:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space overhead: <span class="fill-in">[How much extra storage?]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Autocomplete suggestions with trie:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

### Scenario Predictions

**Scenario 1:** Search query "machine learning" in 10M documents

- **Without index:** <span class="fill-in">[How long?]</span>
- **With inverted index:** <span class="fill-in">[How long?]</span>
- **Speedup factor:** <span class="fill-in">[____x faster?]</span>

**Scenario 2:** E-commerce product search with typos

- **Exact match only:** <span class="fill-in">[User experience?]</span>
- **Fuzzy search:** <span class="fill-in">[How to implement?]</span>
- **Trade-off:** <span class="fill-in">[Performance vs accuracy?]</span>

**Scenario 3:** Ranking search results by relevance

- **Algorithm:** <span class="fill-in">[TF-IDF? BM25? Something else?]</span>
- **Factors to consider:** <span class="fill-in">[What matters?]</span>
- **Personalization:** <span class="fill-in">[How to incorporate?]</span>

</div>

---

## Core Concepts

### Topic 1: Inverted Index

**Concept:** Data structure that maps terms to documents, enabling fast full-text search.

**How It Works:**

**Forward Index (Document → Terms):**
```
doc1: "the quick brown fox"
doc2: "the lazy dog"
doc3: "quick brown dog"

Not efficient for search!
To find "quick", must scan all documents.
```

**Inverted Index (Term → Documents):**
```
"the"     → [doc1, doc2]
"quick"   → [doc1, doc3]
"brown"   → [doc1, doc3]
"fox"     → [doc1]
"lazy"    → [doc2]
"dog"     → [doc2, doc3]

Search "quick" → O(1) lookup → [doc1, doc3]
```

**With Positional Information:**
```
"quick" → [
  doc1: [positions: 1],
  doc3: [positions: 0]
]

"brown" → [
  doc1: [positions: 2],
  doc3: [positions: 1]
]

Phrase search "quick brown":

- Find docs with both terms
- Check if positions are adjacent
- doc1: positions 1,2 → ✓ Match!
- doc3: positions 0,1 → ✓ Match!
```

**Building an Inverted Index:**

```
Input documents:
doc1: "Machine Learning Basics"
doc2: "Deep Learning with Python"
doc3: "Machine Learning Algorithms"

Step 1: Tokenization
doc1: ["machine", "learning", "basics"]
doc2: ["deep", "learning", "with", "python"]
doc3: ["machine", "learning", "algorithms"]

Step 2: Normalization (lowercase, stemming)
doc1: ["machin", "learn", "basic"]
doc2: ["deep", "learn", "with", "python"]
doc3: ["machin", "learn", "algorithm"]

Step 3: Build index
{
  "machin":    [doc1, doc3],
  "learn":     [doc1, doc2, doc3],
  "basic":     [doc1],
  "deep":      [doc2],
  "with":      [doc2],
  "python":    [doc2],
  "algorithm": [doc3]
}

Step 4: Add term frequencies (TF)
{
  "learn": [
    {doc: doc1, tf: 1, positions: [1]},
    {doc: doc2, tf: 1, positions: [1]},
    {doc: doc3, tf: 1, positions: [1]}
  ]
}
```

**Inverted Index Data Structure (Simplified):**

```java
--8<-- "com/study/systems/searchindexing/InvertedIndex.java"
```

!!! warning "Debugging Challenge — Off-by-One in Posting List Intersection"

    The intersection below silently misses matching documents when the two posting lists have equal document IDs at a boundary. Find the bug.

    ```java
    List<Integer> intersect(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) < list2.get(j)) {
                i++;
            } else if (list1.get(i) > list2.get(j)) {
                j++;
            } else {
                result.add(list1.get(i));
                i++;
                // Missing advance of j!
            }
        }
        return result;
    }
    ```

    Trace with `list1 = [1, 3, 5]` and `list2 = [1, 1, 3]`.

    ??? success "Answer"

        **Bug:** When a match is found and `i` is incremented, `j` is not advanced. On the next iteration `list1.get(i)` has moved past `1` but `list2.get(j)` still points at the second `1`, causing the algorithm to miss the match for doc `3` in some configurations and potentially loop forever if the same ID appears twice in `list2`.

        **Fix:** Advance both pointers on a match:
        ```java
        result.add(list1.get(i));
        i++;
        j++;
        ```

**Time Complexity:**

| Operation | Complexity | Notes |
|-----------|-----------|-------|
| Single term lookup | O(1) or O(log V) | V = vocabulary size |
| Boolean AND (2 terms) | O(n₁ + n₂) | n = posting list sizes |
| Boolean OR | O(n₁ + n₂) | Merge lists |
| Phrase query | O(n × k) | k = terms in phrase |
| Index construction | O(N × M) | N = docs, M = avg size |

---

### Topic 2: Text Analysis & Tokenization

**Concept:** Process of converting raw text into searchable terms.

**Text Analysis Pipeline:**

```
Input: "The Quick Brown Fox's 2024 Adventure!"

Step 1: Character Filtering
→ "The Quick Brown Fox's 2024 Adventure"
(Remove special characters)

Step 2: Tokenization
→ ["The", "Quick", "Brown", "Fox's", "2024", "Adventure"]
(Split into words)

Step 3: Lowercase Filter
→ ["the", "quick", "brown", "fox's", "2024", "adventure"]

Step 4: Stop Word Removal (optional)
→ ["quick", "brown", "fox's", "2024", "adventure"]
(Remove "the")

Step 5: Stemming/Lemmatization
→ ["quick", "brown", "fox", "2024", "adventur"]
(fox's → fox, adventure → adventur)

Step 6: N-gram Generation (optional)
→ ["quick", "brown", "fox", "quick brown", "brown fox"]
```

**Tokenization Strategies:**

**Word-based tokenization:**
```
Input: "I'm learning Elasticsearch 8.0!"

Whitespace tokenizer:
→ ["I'm", "learning", "Elasticsearch", "8.0!"]

Standard tokenizer (Unicode-aware):
→ ["I'm", "learning", "Elasticsearch", "8", "0"]

Letter tokenizer (only letters):
→ ["I", "m", "learning", "Elasticsearch"]
```

**N-gram tokenization (for autocomplete, fuzzy search):**
```
Input: "search"

Unigrams: ["s", "e", "a", "r", "c", "h"]
Bigrams: ["se", "ea", "ar", "rc", "ch"]
Trigrams: ["sea", "ear", "arc", "rch"]

Use case: Fuzzy search
Query "serch" (typo):

- Trigrams: ["ser", "erc", "rch"]
- "search" trigrams: ["sea", "ear", "arc", "rch"]
- Overlap: ["erc", "rch"] → Possible match!
```

**Stemming vs Lemmatization:**

```
Stemming (rule-based, faster, less accurate):
running → run
runs → run
runner → runner (different stem!)
better → better (no change)

Lemmatization (dictionary-based, slower, accurate):
running → run
runs → run
runner → runner
better → good (finds lemma!)

Trade-off:
Stemming: Fast, simple, handles unknown words
Lemmatization: Accurate, context-aware, slower
```

---

### Topic 3: Ranking Algorithms

**Concept:** Algorithms to score and rank search results by relevance.

**TF-IDF (Term Frequency-Inverse Document Frequency):**

**Formula:**
```
TF-IDF(term, doc) = TF(term, doc) × IDF(term)

TF(term, doc) = (count of term in doc) / (total terms in doc)

IDF(term) = log(total documents / documents containing term)
```

**Example:**

```
Documents:
doc1: "cat dog cat"
doc2: "dog bird"
doc3: "cat bird fish"
doc4: "fish"

Query: "cat dog"

For "cat" in doc1:
TF = 2/3 = 0.67 (appears 2 times in 3 words)
IDF = log(4/2) = log(2) = 0.30 (in 2 out of 4 docs)
TF-IDF = 0.67 × 0.30 = 0.20

For "dog" in doc1:
TF = 1/3 = 0.33
IDF = log(4/2) = 0.30
TF-IDF = 0.33 × 0.30 = 0.10

doc1 score = 0.20 + 0.10 = 0.30
doc2 score = [calculate similarly]
doc3 score = [calculate similarly]

Ranked results:

1. doc1 (0.30) ← Both terms, high frequency
2. doc2 (0.15) ← Has "dog" only
3. doc3 (0.10) ← Has "cat" only
```

**BM25 (Best Match 25) - Modern Improvement:**

```
BM25 advantages over TF-IDF:

1. Diminishing returns for term frequency
   (10 occurrences not 10x better than 1)
2. Document length normalization
   (Shorter docs don't get unfair advantage)
3. Tunable parameters (k₁, b)

Formula (simplified):
score = IDF × (TF × (k₁ + 1)) / (TF + k₁ × (1 - b + b × docLen/avgDocLen))

Where:
k₁ = term frequency saturation (default: 1.2)
b = length normalization (default: 0.75)
```

!!! note "Why BM25 replaced TF-IDF in production systems"
    TF-IDF scores grow linearly with term frequency — a document mentioning "java" 100 times scores 100x higher than one mentioning it once, even if the first document is just repetitive filler. BM25's saturation parameter `k₁` caps this growth so that additional occurrences provide diminishing returns. Elasticsearch switched to BM25 as its default scorer in version 5.0 for exactly this reason.

**Practical Example (Elasticsearch):**

```json
Query: "machine learning tutorial"

BM25 scoring:
doc1: "Machine Learning Tutorial for Beginners"
  - All 3 terms present ✓
  - Terms in title (boosted) ✓
  - Short document ✓
  Score: 12.5

doc2: "Introduction to Machine Learning: A Complete Tutorial Guide..."
  - All 3 terms present ✓
  - "machine learning" together (phrase bonus) ✓
  - Long document ✗
  Score: 10.2

doc3: "Python Tutorial"
  - 1 term present ("tutorial")
  - Common term (lower IDF) ✗
  Score: 2.1

Ranking: doc1 > doc2 > doc3
```

**Field Boosting:**

```json
{
  "query": {
    "multi_match": {
      "query": "elasticsearch",
      "fields": [
        "title^3",        // 3x weight
        "category^2",     // 2x weight
        "content^1"       // 1x weight (default)
      ]
    }
  }
}

Effect:
Match in title = 3x more important than content
Prioritizes documents with query terms in prominent fields
```

---

### Topic 4: Advanced Search Features

**Concept:** Techniques for fuzzy matching, autocomplete, and query optimization.

**Fuzzy Search (Edit Distance):**

```
Levenshtein Distance: minimum edits to transform one string into another

Example: "quick" → "quik"
Operations:

1. Delete 'c' → "quik"
Distance = 1

Query: "elasticsarch" (typo)
Fuzzy search with distance 2:

- "elasticsearch" (distance 1: insert 'e')
- "elasticcache" (too far, distance 3)

Implementation:
query: {
  "fuzzy": {
    "name": {
      "value": "elasticsarch",
      "fuzziness": "AUTO"  // 0,1,2 based on term length
    }
  }
}
```

**Autocomplete with Tries:**

```
Trie structure for autocomplete:
         root
        /   \
       s     c
      / \     \
     e   t     a
    /     \     \
   a       o     t
  /         \
 r           p
/
c
h

Words: "search", "stop", "cat"

Autocomplete "se":

1. Traverse: root → s → e
2. Find all descendants: "search"
3. Return top 10 by frequency

Time: O(k) where k = prefix length
Space: O(N × M) where N = words, M = avg length
```

**Autocomplete with Edge N-grams:**

```
Term: "search"

Edge n-grams (prefix-based):
s
se
sea
sear
searc
search

Index these as tokens:
"s" → [search, ...]
"se" → [search, set, ...]
"sea" → [search, season, ...]

Query "sea":
→ Instant lookup → [search, season, ...]

Advantage: O(1) lookup vs Trie traversal
Disadvantage: Higher storage cost
```

**Highlighting:**

```
Query: "machine learning"
Result: "Introduction to Machine Learning algorithms..."

Highlighted:
"Introduction to <em>Machine</em> <em>Learning</em> algorithms..."

Implementation:

1. Find term positions from inverted index
2. Extract surrounding context (±50 chars)
3. Insert highlight tags
4. Return fragments

Advanced: Multi-field highlighting
Show snippets from title, description, and content
Rank snippets by relevance
```

---

### Topic 5: Distributed Search (Elasticsearch Architecture)

**Concept:** Scale search across multiple nodes for performance and reliability.

**Sharding:**

```
Index: 10M documents
Shard into 5 primary shards:

Shard 0: docs 0-2M
Shard 1: docs 2M-4M
Shard 2: docs 4M-6M
Shard 3: docs 6M-8M
Shard 4: docs 8M-10M

Query execution:

1. Send query to all 5 shards (parallel)
2. Each shard searches its 2M docs
3. Return top 10 from each shard
4. Coordinator merges 50 results
5. Return final top 10

Time: O(N/S) where S = shards
Parallelization: 5x faster (ideal)
```

**Replication:**

```
Cluster: 3 nodes
Index: 2 primary shards, 1 replica

Distribution:
Node 1: Primary 0, Replica 1
Node 2: Primary 1, Replica 0
Node 3: Replica 0, Replica 1

Availability:

- Node 1 fails → Node 2 has Primary 1 and Replica 0
- All data still accessible
- Automatic promotion of replicas

Performance:

- Reads can use replicas (load balancing)
- Writes go to primary, then replicated
```

**Query Execution Flow:**

```
Client → Load Balancer → Coordinating Node

Coordinating Node:

1. Parse query
2. Determine target shards
3. Broadcast to all shards
4. Collect results
5. Merge and rank
6. Return to client

           Coordinator
          /     |     \
     Shard0  Shard1  Shard2
       ↓       ↓       ↓
     [10]    [10]    [10]  ← Top 10 from each
       \       |      /
         Merge & Sort
              ↓
         Final Top 10
```

**Routing:**

```
Document ID → Shard assignment

Hash-based routing:
shard = hash(document_id) % num_shards

Custom routing (co-locate related docs):
PUT /users/_doc/user123?routing=tenant_A
→ All tenant_A docs in same shard
→ Faster tenant-scoped queries

Trade-off:

- Better locality
- Risk of unbalanced shards
```

---

### Topic 6: Search Optimization

**Concept:** Techniques to improve search performance and relevance.

**Index-Time Optimizations:**

**1. Selective Indexing:**
```json
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword",
        "index": false  // Don't index, only store
      },
      "title": {
        "type": "text",
        "analyzer": "english"  // Full-text search
      },
      "created_at": {
        "type": "date",
        "index": true  // For filtering/sorting
      }
    }
  }
}

Effect:

- Smaller index size
- Faster indexing
- Reduced memory usage
```

**2. Doc Values (Column Store):**
```
Traditional (row-oriented):
doc1: {name: "Alice", age: 30, city: "NYC"}
doc2: {name: "Bob", age: 25, city: "LA"}

Doc values (column-oriented):
name: ["Alice", "Bob"]
age: [30, 25]
city: ["NYC", "LA"]

Use case: Aggregations, sorting
Query: "Average age by city"
→ Scan age and city columns only
→ Much faster than loading full docs
```

**Query-Time Optimizations:**

**1. Filter Context vs Query Context:**
```json
// Query context (scored):
{
  "query": {
    "match": {
      "content": "elasticsearch"  // Score by relevance
    }
  }
}

// Filter context (not scored, cacheable):
{
  "query": {
    "bool": {
      "must": {
        "match": { "content": "elasticsearch" }
      },
      "filter": {
        "term": { "status": "published" }  // Binary: yes/no
      }
    }
  }
}

Filter benefits:

+ Cached (reused across queries)
+ Faster (no scoring)
+ Use for: dates, categories, flags
```

**2. Query Caching:**
```
Request cache (entire query result):
{
  "size": 0,  // Only aggregations, no docs
  "aggs": { ... }
}
→ Cached for 1 minute
→ Subsequent identical queries instant

Shard request cache:

- Caches query results per shard
- Invalidated on shard changes
- Shared across all queries hitting shard
```

**3. Pagination:**
```
Deep pagination problem:
GET /products?from=10000&size=10

Process:

1. Each shard returns top 10,010 results
2. Coordinator merges (5 shards × 10,010 = 50,050 docs)
3. Sorts all 50,050
4. Returns results 10,000-10,010

Memory intensive! CPU intensive!

Solution 1: Search After (cursor-based)
GET /products?search_after=[value]
→ Continue from last result
→ No offset limit

Solution 2: Scroll API (for exports)
GET /products?scroll=1m
→ Snapshot of results
→ Iterate through batches
```

!!! tip "Use filter context for structured criteria"
    Any condition that is a binary yes/no — status flags, date ranges, category IDs — should go in `filter` context rather than `must`/`should`. Filters are not scored, which makes them faster, and Elasticsearch caches filter results at the shard level. This is one of the highest-leverage query optimisations available.

---

## Before/After: Why Search Indexing Matters

**Your task:** Compare naive search vs indexed search to understand the impact.

### Example: Product Search on E-Commerce Site

**Problem:** Search for "wireless headphones" across 10 million products

#### Approach 1: Naive Linear Search

```sql
SELECT * FROM products
WHERE
  LOWER(name) LIKE '%wireless%'
  AND LOWER(name) LIKE '%headphones%'
OR
  LOWER(description) LIKE '%wireless%'
  AND LOWER(description) LIKE '%headphones%';
```

**What happens:**
```
Database scans all 10M rows:
Row 1: Check name, check description → No match
Row 2: Check name, check description → No match
...
Row 45,234: Check name → MATCH! (add to results)
...
Row 10,000,000: Check name, check description → No match

Time: ~10-30 seconds
Database CPU: 100%
User experience: Loading spinner... user leaves
```

**Problems:**

- O(N × M) where N = documents, M = avg document size
- No ranking (random order)
- No fuzzy matching ("wireles" returns nothing)
- Kills database under load

#### Approach 2: Inverted Index Search

**Index structure:**
```
Inverted Index:
"wireless" → [doc45234, doc89123, doc234556, ...]
"headphones" → [doc12345, doc45234, doc67890, ...]

Intersection: [doc45234] ← Both terms present

Ranking (TF-IDF):
doc45234: score = 8.7 (both in title, high frequency)
doc89123: score = 6.2 (both in description)
doc12345: score = 4.1 (only "headphones" in title)
```

**Query execution:**
```

1. Look up "wireless" → [45234, 89123, 234556, ...] (1ms)
2. Look up "headphones" → [12345, 45234, 67890, ...] (1ms)
3. Intersect lists → [45234, ...] (5ms)
4. Rank by score → sorted results (10ms)
5. Return top 20 results (1ms)

Total: ~20ms
Database CPU: 5%
User experience: Instant results ✓
```

**Performance comparison:**

| Metric | Linear Search | Inverted Index | Improvement |
|--------|--------------|----------------|-------------|
| Time | 10-30s | 20ms | 500-1500x faster |
| CPU usage | 100% | 5% | 20x less |
| Scalability | O(N) | O(log N) | Sublinear |
| Ranking | No | Yes (TF-IDF) | Better UX |
| Fuzzy match | Hard | Easy | More results |

**Real-world impact:**

- Without indexing: 80% bounce rate (users leave)
- With indexing: 5% bounce rate, 10x more conversions
- Cost: $100K/month in extra servers vs $10K/month with proper indexing

**Your calculation:** For 100M documents:

- Linear search time: <span class="fill-in">_____</span> seconds
- Indexed search time: <span class="fill-in">_____</span> ms
- Users served per second: Linear <span class="fill-in">_____</span> vs Indexed <span class="fill-in">_____</span>

!!! info "Loop back"
    Return to the Quick Quiz now and fill in your verified answers.

---

## Case Studies: Search & Indexing in the Wild

### Google Search: PageRank and the Inverted Index

- **Pattern:** Distributed Inverted Index combined with the PageRank ranking algorithm.
- **How it works:** Google's crawlers build a massive inverted index of the web. When you search, your query terms are
  used to retrieve a list of matching documents. The magic is in the ranking: PageRank analyzes the web's link
  structure, treating a link from page A to page B as a "vote" for page B. It ranks pages higher if they are linked to
  by many other high-ranking pages.
- **Key Takeaway:** A fast inverted index is only half the battle. The relevance of search results is determined by
  sophisticated ranking algorithms. PageRank revolutionized search by using the collective intelligence of the web
  itself to determine authority and importance.

### Elasticsearch: Powering Enterprise Search

- **Pattern:** Distributed, Sharded Inverted Index (using Apache Lucene).
- **How it works:** Companies like **Uber** (for searching trips), **Stack Overflow** (for finding questions), and
  **Netflix** (for catalog search) use Elasticsearch. It automatically builds an inverted index on JSON documents. To
  scale, it partitions the index into multiple **shards**, and replicates them for fault tolerance. A query is sent to
  all shards in parallel, and the results are aggregated by a coordinating node.
- **Key Takeaway:** Elasticsearch democratized high-quality search. It packages the complex concepts of inverted
  indexes, text analysis, and distributed systems into a scalable, easy-to-use product, making it the de-facto standard
  for adding search capabilities to applications.

### Algolia: Search-as-a-Service for Speed

- **Pattern:** In-Memory, Prefix-based Trie/Index Hybrid.
- **How it works:** Algolia is designed for "instant search" and autocomplete experiences. They store their indices
  entirely in RAM and distribute them across multiple data centers for low latency. Their ranking is often based on a
  tie-breaking algorithm that can be heavily customized with business metrics (e.g., for an e-commerce site, rank
  products with more sales higher).
- **Key Takeaway:** For user-facing search where speed is paramount, in-memory indices and pre-computed ranking can
  provide a superior user experience. The trade-off is higher cost and a focus on prefix-matching rather than complex
  full-text relevance ranking.

---

## Common Misconceptions

!!! warning "More shards always means faster search"
    Each shard adds coordination overhead. A query must be sent to every shard, results collected from all of them, and then merged and re-ranked by the coordinator. With 50 small shards on a 3-node cluster, the coordination overhead can outweigh the parallelism benefit. The Elasticsearch recommendation is to keep shard sizes between 10–50 GB. Fewer, larger shards often outperform many tiny ones.

!!! warning "Stop-word removal is always a good idea"
    Removing common words like "the", "a", "in" reduces index size and speeds up queries — but it can break phrase searches. A user searching for "The Who" (the band) or "to be or not to be" relies on those stop words being in the index. Modern systems prefer using IDF naturally to down-weight common terms rather than removing them entirely.

!!! warning "TF-IDF and BM25 measure semantic meaning"
    Both algorithms are purely statistical — they measure how often terms appear and how rare they are across documents. They have no understanding of meaning. "Bank" (financial institution) and "bank" (river bank) receive identical scores. Semantic search (using dense vector embeddings) is a separate technique that explicitly models meaning, and it complements rather than replaces inverted index search.

---

## Decision Framework

<div class="learner-section" markdown>

### Question 1: When to build inverted index?

**Build inverted index when:**

- Full-text search required: <span class="fill-in">[Search within documents]</span>
- Fast lookups critical: <span class="fill-in">[< 100ms query time]</span>
- Dataset size > 10K docs: <span class="fill-in">[Linear search too slow]</span>

**Skip inverted index when:**

- Exact key lookups only: <span class="fill-in">[Use hash table]</span>
- Tiny dataset (< 1K docs): <span class="fill-in">[Linear scan acceptable]</span>
- Write-heavy, rare reads: <span class="fill-in">[Index overhead not worth it]</span>

### Question 2: TF-IDF vs BM25?

**Use TF-IDF when:**

- Simple implementation needed
- Legacy system compatibility
- Small datasets

**Use BM25 when:**

- Production search system
- Varied document lengths
- Better ranking required (modern default)

### Question 3: Sharding strategy?

**Use hash-based sharding when:**

- Uniform distribution desired
- No co-location requirements
- Simple setup

**Use custom routing when:**

- Multi-tenancy (tenant per shard)
- Co-locate related documents
- Optimize specific query patterns

</div>

---

## Practice Scenarios

<div class="learner-section" markdown>

### Scenario 1: E-Commerce Product Search

**Requirements:**

- 10M products
- Autocomplete (< 50ms)
- Fuzzy matching for typos
- Category filters
- Price range filters
- Rank by relevance + popularity

**Your design:**

Index structure:

- Shards: <span class="fill-in">[How many?]</span>
- Replicas: <span class="fill-in">[How many for HA?]</span>
- Mapping: <span class="fill-in">[Which fields indexed?]</span>

Query strategy:

- Autocomplete: <span class="fill-in">[Trie? Edge n-grams?]</span>
- Fuzzy: <span class="fill-in">[Levenshtein distance?]</span>
- Ranking: <span class="fill-in">[BM25 + custom boost?]</span>

**Failure modes:**

- What happens if the primary shard for a heavily-queried product category becomes unavailable during a peak shopping period? <span class="fill-in">[Fill in]</span>
- How does your design behave when the autocomplete index becomes stale because real-time indexing falls behind a burst of 10,000 new product listings? <span class="fill-in">[Fill in]</span>

### Scenario 2: Log Search (Observability)

**Requirements:**

- 1B log entries/day
- Time-range queries
- Full-text search on messages
- Retention: 30 days
- Real-time ingestion

**Your design:**

Index strategy:

- Time-based indices: <span class="fill-in">[Daily? Hourly?]</span>
- Rollover policy: <span class="fill-in">[When?]</span>
- Deletion: <span class="fill-in">[How to handle retention?]</span>

Query optimization:

- Filter by time: <span class="fill-in">[Use filter context?]</span>
- Aggregations: <span class="fill-in">[Doc values?]</span>
- Pagination: <span class="fill-in">[Scroll API?]</span>

**Failure modes:**

- What happens if the log ingestion pipeline falls behind during an incident, causing a 30-minute gap in the time-based indices when an on-call engineer is searching for error patterns? <span class="fill-in">[Fill in]</span>
- How does your design behave when the 30-day retention policy deletion job runs and accidentally removes an entire index alias rather than just the expired daily index? <span class="fill-in">[Fill in]</span>

### Scenario 3: Knowledge Base Search

**Requirements:**

- 100K articles
- Multi-language support
- Highlight search terms
- "Did you mean?" suggestions
- Related articles

**Your design:**

Text analysis:

- Language detection: <span class="fill-in">[How?]</span>
- Stemming: <span class="fill-in">[Language-specific?]</span>
- Synonyms: <span class="fill-in">[How to implement?]</span>

Features:

- Highlighting: <span class="fill-in">[Unified? Plain?]</span>
- Suggestions: <span class="fill-in">[Fuzzy + frequency?]</span>
- Related: <span class="fill-in">[More Like This query?]</span>

**Failure modes:**

- What happens if the language detection service becomes unavailable and all new articles are indexed with the wrong analyzer, corrupting stemming and synonym expansion for one language? <span class="fill-in">[Fill in]</span>
- How does your design behave when a full re-index of 100K articles is triggered and the in-progress index swap causes a period where search returns zero results for all queries? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these without referring to your notes or implementation.

1. A term appears 10 times in document A (500 words) and 10 times in document B (5,000 words). Which document receives a higher TF score for that term, and why does this matter for ranking quality?

    ??? success "Rubric"
        A complete answer addresses: (1) raw TF treats both documents equally (10 occurrences each), but normalised TF (term count divided by document length) gives document A a score 10× higher because the term density is much greater in the shorter document, (2) BM25 applies length normalisation to prevent long documents from being artificially inflated simply by mentioning a term many times, and (3) without length normalisation, a 10,000-word article mentioning "price" 10 times would unfairly outrank a 100-word product title that also mentions "price" 10 times.

2. You have an Elasticsearch cluster with 5 shards. A query must return the top 10 results globally. Explain exactly how many documents are transferred between shards and the coordinator, and why deep pagination (page 1000) is expensive.

    ??? success "Rubric"
        A complete answer addresses: (1) each of the 5 shards returns its local top 10 candidates to the coordinator, so 50 documents are transferred for a single "top 10" query, (2) for deep pagination to page 1000 with 10 results per page, each shard must return its top 10,010 candidates — 50,050 documents transferred and re-ranked by the coordinator, and (3) this grows linearly with page depth, making the Scroll API or search_after the correct solution for deep or iterative result retrieval.

3. A product search returns zero results when a user types "wireles headphone" (two typos). What text analysis or query configuration change would fix this, and what is the performance trade-off?

    ??? success "Rubric"
        A complete answer addresses: (1) enabling fuzzy matching (e.g., `fuzziness: AUTO` in Elasticsearch) allows matches within an edit distance of 1–2 characters, catching both "wireles" and "headphone" as matches for the correct terms, (2) alternatively, edge n-gram indexing at ingest time pre-generates partial token variants so lookups remain exact-match speed at query time, and (3) the performance trade-off is that fuzzy matching increases query time and CPU usage because the engine must evaluate many candidate tokens, while n-gram indexing trades query speed for significantly increased index size.

4. Your team wants to add a filter for `status = "published"` to every search query. Should this go in `must` (query context) or `filter` (filter context)? Explain the difference in how Elasticsearch handles each.

    ??? success "Rubric"
        A complete answer addresses: (1) `status = "published"` should go in filter context because it is a binary yes/no condition with no meaningful relevance score contribution, (2) query context (`must`) calculates a relevance score for each matching document, which is wasted CPU for a boolean filter — filter context skips scoring entirely, and (3) filter context results are cached by Elasticsearch at the shard level using a bitset, so the same status filter used repeatedly across queries is served from cache after the first execution, dramatically reducing repeated query cost.

5. A colleague says "Our index has 200 shards across 5 nodes — more shards means more parallelism and faster queries." What is wrong with this reasoning, and what is the actual recommended guidance?

    ??? success "Rubric"
        A complete answer addresses: (1) each query is sent to all 200 shards, and the coordinator must collect and merge 200 sets of results — the coordination overhead grows with shard count and can easily dominate the parallelism benefit for small shards, (2) with 200 shards on 5 nodes each node handles 40 shard threads per query, competing for CPU and memory, and (3) Elasticsearch's guidance is to keep individual shard sizes between 10–50 GB and avoid having many shards smaller than a few GB; fewer, appropriately-sized shards typically outperform a large number of tiny ones.

---

## Review Checklist

<div class="learner-section" markdown>

Complete this checklist after implementing and studying search and indexing.

- [ ] Can explain how an inverted index is built and why it enables fast full-text search
- [ ] Can describe TF-IDF scoring and explain why IDF prevents common words from dominating results
- [ ] Can explain how Elasticsearch distributes a query across shards and coordinates the results
- [ ] Can describe fuzzy matching and articulate its accuracy/performance trade-off
- [ ] Can explain the difference between query context and filter context, and when to use each
- [ ] Can design a text analysis pipeline: tokenization, normalization, stemming, synonym expansion

</div>
