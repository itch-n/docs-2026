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

1. **Storage Engines** - Implement B+Trees & LSM Trees ← Start here
2. **Indexing Strategies** - Primary, secondary, composite indexes
3. **Caching Patterns** - LRU, LFU, cache-aside, write-through
4. **API Design** - REST principles, versioning, pagination
5. **Security Patterns** - JWT, RBAC, API keys, secrets management
6. **Rate Limiting** - Token bucket, sliding window algorithms
7. **Load Balancing** - Consistent hashing, health checks
8. **Concurrency Patterns** - Locks, producer-consumer, thread safety
9. **Database Scaling** - Replication, sharding, partitioning
10. **Message Queues** - Queue vs pub/sub, delivery guarantees
11. **Stream Processing** - Windowing, watermarks, exactly-once semantics
12. **Observability** - Metrics, logging, tracing, SLOs
13. **Distributed Transactions** - Saga pattern, idempotency
14. **Consensus Patterns** - Raft, leader election, distributed locks
15. **Full System Designs** - Apply everything (Instagram, Uber, etc.)

### [DSA](dsa/01-two-pointers.md) (16 Topics)

Pattern-based approach from easy to advanced:

1. **Two Pointers** - Opposite directions, same direction, different speeds ← Start here
2. **Sliding Window** - Fixed and variable window sizes for subarray problems
3. **Hash Tables** - Fast lookups, grouping, frequency counting
4. **Linked Lists** - Reversal, cycle detection, fast/slow pointers
5. **Stacks & Queues** - LIFO/FIFO, monotonic stacks, deque operations
6. **Trees - Traversals** - Inorder, preorder, postorder, level-order (BFS)
7. **Trees - Recursion** - Height, diameter, LCA, path problems
8. **Binary Search** - Classic search, rotated arrays, 2D matrices
9. **Union-Find** - Disjoint sets for connectivity (before Graphs for MST algorithms)
10. **Graphs (DFS/BFS)** - Traversals, shortest path, MST
11. **Heaps** - Priority queues, top K problems
12. **Backtracking** - Permutations, combinations, constraint satisfaction
13. **Dynamic Programming - 1D** - Fibonacci, house robber, coin change
14. **Dynamic Programming - 2D** - Knapsack, LCS, edit distance
15. **Tries** - Prefix trees for string problems
16. **Advanced Topics** - Bit manipulation, intervals, prefix sums

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
