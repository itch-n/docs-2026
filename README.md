# Backend Engineering Study Guide

Your hands-on, fill-in-as-you-learn framework for mastering systems design and algorithms.

## What You Have

✅ **Complete structure** - 31 numbered topics ready to learn (15 Systems + 16 DSA)  
✅ **Interview-ready coverage** - All critical topics for Backend Engineer interviews  
✅ **Code stubs** - Method signatures and TODOs for every implementation  
✅ **Runnable examples** - Client code to test with real data  
✅ **Fill-in templates** - ELI5, decision trees, and practice sections  

## Quick Start

### 1. View the Site

```bash
uv run mkdocs serve
```

Open: http://127.0.0.1:8000

### 2. Choose Your Path

**Systems Design** → Start at [01-storage-engines.md](docs/systems/01-storage-engines.md)
- Implement B+Tree and LSM Tree from scratch
- Run client code with test data
- Fill in ELI5 explanations
- Build decision frameworks
- Complete practice scenarios

**DSA** → Start at [01-two-pointers.md](docs/dsa/01-two-pointers.md)
- Implement three two-pointer patterns
- Run client code with examples
- Solve LeetCode problems
- Build pattern recognition

### 3. Learn → Fill In → Move On

For each topic:
1. **Try implementing first** (don't peek at solutions!)
2. **Run the client code** to verify it works
3. **Fill in ELI5 section** (if you can't explain it simply, you don't understand it)
4. **Build decision trees** (when to use, when NOT to use)
5. **Complete practice** problems/scenarios
6. **Check off review list** before moving to next topic

## File Structure

```
docs/
├── index.md                           # Start here
│
├── systems/                           # 15 topics
│   ├── 01-storage-engines.md         # B+Trees & LSM Trees
│   ├── 02-indexing.md                # Primary, secondary, composite indexes
│   ├── 03-caching-patterns.md        # LRU, LFU, write policies
│   ├── 04-api-design.md              # REST, RPC, versioning
│   ├── 05-security-patterns.md       # JWT, RBAC, secrets management
│   ├── 06-rate-limiting.md           # Token bucket, sliding window
│   ├── 07-load-balancing.md          # Consistent hashing, health checks
│   ├── 08-concurrency-patterns.md    # Locks, thread safety, thread pools
│   ├── 09-database-scaling.md        # Replication, sharding, partitioning
│   ├── 10-message-queues.md          # Queue vs pub/sub, delivery guarantees
│   ├── 11-stream-processing.md       # Windowing, watermarks, exactly-once
│   ├── 12-observability.md           # Metrics, logging, tracing, SLOs
│   ├── 13-distributed-transactions.md # Saga pattern, idempotency
│   ├── 14-consensus-patterns.md      # Raft, leader election, quorums
│   └── 15-full-system-designs.md     # Apply everything
│
└── dsa/                               # 16 topics
    ├── 01-two-pointers.md            # Three pointer patterns
    ├── 02-sliding-window.md          # Fixed & variable window
    ├── 03-hash-tables.md             # HashMap operations, grouping
    ├── 04-linked-lists.md            # Reverse, cycle detection
    ├── 05-stacks--queues.md          # Stack, monotonic stack, deque
    ├── 06-trees-traversals.md        # Inorder, preorder, postorder, level-order
    ├── 07-trees-recursion.md         # Height, diameter, LCA
    ├── 08-binary-search.md           # Classic BS, rotated array, 2D matrix
    ├── 09-union-find.md              # Disjoint sets (moved before Graphs)
    ├── 10-graphs.md                  # DFS, BFS, Dijkstra, MST
    ├── 11-heaps.md                   # Min/max heap, top K problems
    ├── 12-backtracking.md            # Permutations, combinations, subsets
    ├── 13-dynamic-programming-1d.md  # Fibonacci, coin change, LIS
    ├── 14-dynamic-programming-2d.md  # Knapsack, LCS, edit distance
    ├── 15-tries.md                   # Prefix trees, autocomplete
    └── 16-advanced-topics.md         # Bit manipulation, intervals
```

## Every Topic Has

### 1. ELI5 Section
Prompts to help you explain concepts simply. If you can't fill this in, you need to study more.

### 2. Implementation Section
- Java code stubs with TODO comments
- Method signatures provided
- Runnable client code with test data
- You fill in the logic

### 3. Decision Framework Section
- Questions to answer
- Decision trees to build
- "Kill switch" - when NOT to use
- "Rule of Three" - alternative approaches

### 4. Practice Section
- Systems: Real design scenarios
- DSA: LeetCode problems with links
- Space to document your solutions

### 5. Review Checklist
Don't move on until all boxes checked!

## Example: How to Use Topic 01 (Storage Engines)

**Day 1-2:**
```java
// Open: docs/systems/01-storage-engines.md
// Find: BPlusTree class with TODOs
// Implement: insert(), search(), rangeQuery()
// Run: BPlusTreeClient.main()
// Verify: All tests pass
```

**Day 3:**
```java
// Implement: LSMTree class
// Implement: put(), get(), flush(), compact()
// Run: LSMTreeClient.main()
// Verify: Flushes and compaction work
```

**Day 4:**
```java
// Run: StorageBenchmark.main()
// Record results in markdown
// Understand WHY LSM faster for writes
// Understand WHY B+Tree faster for reads
```

**Day 5:**
```markdown
// Fill in ELI5 section:
- Explain B+Tree like a filing cabinet
- Explain LSM Tree like a notebook
- Answer all prompts

// Build decision tree:
- When write-heavy? → LSM
- When read-heavy? → B+Tree
- When range queries? → B+Tree

// Complete practice scenarios:
- Design storage for Posts table
- Design storage for Metrics
- Design storage for Inventory
```

**Day 6:**
```markdown
// Review checklist - check ALL boxes
// Verify you can implement from memory
// Verify you can explain trade-offs
// Move to Topic 02 when ready!
```

## Principles

**"Focus on the Kill Switch"**

- For every tech, document why you should NOT use it
- Understanding failures > understanding features

**"The Rule of Three"**

- For every decision, identify 3 alternatives
- No "right" answer, only trade-offs

**"Implement Before Reading"**

- True learning happens through struggle
- Code stubs guide you, but YOU must fill them in

## Setup

### Prerequisites

- Python 3.13+
- UV package manager

### Install

```bash
# Install UV
curl -LsSf https://astral.sh/uv/install.sh | sh

# Install dependencies
uv sync

# Serve locally
uv run mkdocs serve
```

## Next Steps

1. **Open the site**: `uv run mkdocs serve`
2. **Pick a path**: Systems or DSA (or both!)
3. **Start with Topic 01** (the complete examples)
4. **Learn by doing**: Fill in the stubs as you go
5. **Don't rush**: Master each topic before moving on

---

**No timelines. No pressure. Just learn → implement → understand → move on.**

Ready? → [Open the guide](http://127.0.0.1:8000) and start with Topic 01!
