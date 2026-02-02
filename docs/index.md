# Backend Engineer Study Guide

> A hands-on, fill-in-as-you-learn framework for systems design and algorithms

## How This Works

Numbered sequences you work through at your own pace. Each topic follows the same structure:

**ELI5** → **Quiz** → **Implementation** → **Decision Framework** → **Practice** → **Review Checklist**

- 🟨 **Yellow background** = Fill-in sections
- ⬜ **Gray code blocks** = Reference implementations

---

## Two Paths

### [Systems Design](systems/01-storage-engines.md) (15 Topics)

Build real implementations to understand how systems work:

- [ ] **Storage Engines** - Implement B+Trees & LSM Trees ← Start here
- [ ] **Indexing Strategies** - Primary, secondary, composite indexes
- [ ] **Caching Patterns** - LRU, LFU, cache-aside, write-through
- [ ] **API Design** - REST principles, versioning, pagination
- [ ] **Security Patterns** - JWT, RBAC, API keys, secrets management
- [ ] **Rate Limiting** - Token bucket, sliding window algorithms
- [ ] **Load Balancing** - Consistent hashing, health checks
- [ ] **Concurrency Patterns** - Locks, producer-consumer, thread safety
- [ ] **Database Scaling** - Replication, sharding, partitioning
- [ ] **Message Queues** - Queue vs pub/sub, delivery guarantees
- [ ] **Stream Processing** - Windowing, watermarks, exactly-once semantics
- [ ] **Observability** - Metrics, logging, tracing, SLOs
- [ ] **Distributed Transactions** - Saga pattern, idempotency
- [ ] **Consensus Patterns** - Raft, leader election, distributed locks
- [ ] **Full System Designs** - Apply everything (Instagram, Uber, etc.)

### [DSA](dsa/01-two-pointers.md) (16 Topics)

Pattern-based approach from easy to advanced:

- [ ] **Two Pointers** - Opposite directions, same direction, different speeds ← Start here
- [ ] **Sliding Window** - Fixed and variable window sizes for subarray problems
- [ ] **Hash Tables** - Fast lookups, grouping, frequency counting
- [ ] **Linked Lists** - Reversal, cycle detection, fast/slow pointers
- [ ] **Stacks & Queues** - LIFO/FIFO, monotonic stacks, deque operations
- [ ] **Trees - Traversals** - Inorder, preorder, postorder, level-order (BFS)
- [ ] **Trees - Recursion** - Height, diameter, LCA, path problems
- [ ] **Binary Search** - Classic search, rotated arrays, 2D matrices
- [ ] **Union-Find** - Disjoint sets for connectivity (before Graphs for MST algorithms)
- [ ] **Graphs (DFS/BFS)** - Traversals, shortest path, MST
- [ ] **Heaps** - Priority queues, top K problems
- [ ] **Backtracking** - Permutations, combinations, constraint satisfaction
- [ ] **Dynamic Programming - 1D** - Fibonacci, house robber, coin change
- [ ] **Dynamic Programming - 2D** - Knapsack, LCS, edit distance
- [ ] **Tries** - Prefix trees for string problems
- [ ] **Advanced Topics** - Bit manipulation, intervals, prefix sums

---

## Learning Process

- Implement code stubs → Run examples → Fill in explanations
- Build decision trees for when to use (and NOT use) each pattern
- Complete practice problems → Check off review checklist

---

## Key Principles

- **The Kill Switch:** Know when NOT to use each pattern or technology
- **Three Alternatives:** Compare trade-offs, not features
- **Implement First:** Build it to understand it, then explain it simply

---

## Getting Started

**Systems Design:** [01. Storage Engines →](systems/01-storage-engines.md) - Implement B+Tree and LSM Tree, benchmark performance

**DSA:** [01. Two Pointers →](dsa/01-two-pointers.md) - Learn three pointer patterns, build pattern recognition

---

**No pressure, no timelines. Just learn → implement → fill in → move on.**
