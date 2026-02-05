# Search & Indexing

> Inverted indexes, full-text search, ranking algorithms, and distributed search systems

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After learning search and indexing, explain them simply.

**Prompts to guide you:**

1. **What is an inverted index in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

2. **Why can't we just search through all documents linearly?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

3. **Real-world analogy for inverted index:**
    - Example: "An inverted index is like a book's index where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What makes search results "relevant"?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

5. **Real-world analogy for search ranking:**
    - Example: "TF-IDF is like voting where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

6. **Why do we need sharding for search?**
    - Your answer: <span class="fill-in">[Fill in after practice]</span>

</div>

---

## Quick Quiz (Do BEFORE learning)

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
- **How it works:** Companies like **Uber** (for searching trips), **Stack Overflow** (for finding questions), and *
  *Netflix** (for catalog search) use Elasticsearch. It automatically builds an inverted index on JSON documents. To
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
class InvertedIndex {
    // Term → Posting List
    Map<String, PostingList> index;

    class PostingList {
        List<Posting> postings;
        int documentFrequency; // How many docs contain this term
    }

    class Posting {
        int documentId;
        int termFrequency;     // How many times in this doc
        List<Integer> positions; // Where in doc
    }

    // Search for term
    PostingList search(String term) {
        return index.get(normalize(term));
    }

    // Boolean AND query: term1 AND term2
    List<Integer> intersect(String term1, String term2) {
        PostingList p1 = search(term1);
        PostingList p2 = search(term2);

        // Merge postings lists (efficient with sorted lists)
        return mergeSortedLists(p1, p2);
    }
}
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

---

## Decision Framework

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

---

## Practice Scenarios

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

---

## Review Checklist

Before moving to the next topic:

-   [ ] **Understanding**
    -   [ ] Understand inverted index structure
    -   [ ] Know tokenization pipeline
    -   [ ] Understand TF-IDF and BM25
    -   [ ] Know sharding and replication
    -   [ ] Understand query execution flow

-   [ ] **Implementation**
    -   [ ] Can design index mapping
    -   [ ] Know when to use analyzers
    -   [ ] Understand filter vs query context
    -   [ ] Can optimize queries

-   [ ] **Decision Making**
    -   [ ] Know when to shard
    -   [ ] Can choose ranking algorithm
    -   [ ] Understand trade-offs
    -   [ ] Completed practice scenarios

---

### Mastery Certification

**I certify that I can:**

-   [ ] Explain inverted index structure
-   [ ] Design search system for requirements
-   [ ] Choose appropriate text analysis
-   [ ] Implement ranking algorithms
-   [ ] Optimize query performance
-   [ ] Configure sharding strategy
-   [ ] Debug search relevance issues
-   [ ] Build autocomplete feature
-   [ ] Handle multi-language search
-   [ ] Teach search concepts to others

