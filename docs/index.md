# Backend Engineer Study Guide

> A hands-on, fill-in-as-you-learn framework for systems design and algorithms

## How This Works

This guide is organized as **numbered sequences** - no timelines, just ordered topics you work through at your own pace.

Each topic has the same structure:

1. **ELI5** - Answer prompts to explain concepts simply after implementing
2. **Quick Quiz** - Test your intuition before coding, verify after
3. **Implementation** - Complete code stubs and run client examples
4. **Decision Framework** - Build flowcharts for when to use what
5. **Practice** - Solve scenarios and problems to cement understanding
6. **Review Checklist** - Verify mastery before moving to the next topic

**Visual Guide:**

- 🟨 **Yellow background** = Sections you fill in as you learn
- ⬜ **Gray code blocks** = Reference implementations to study

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

For each topic:

1. **Start with code stubs** - Try implementing before reading solutions
2. **Run the client examples** - See how it works with real data
3. **Fill in the ELI5** - If you can't explain it simply, you don't understand it (yellow highlighted sections)
4. **Build decision trees** - Document when to use (and NOT use) each pattern
5. **Complete practice problems** - Verify your understanding
6. **Check off review checklist** - Ensure mastery before moving on

**The styling helps you know what to do:** Yellow sections are prompts for you to answer. Gray code blocks are examples to study and learn from.

---

## Key Principles

**"Focus on the Kill Switch"**

- For every technology, document why you should NOT use it
- Trade-offs matter more than features

**"The Rule of Three"**

- For every decision, identify three alternative approaches
- No "right" answer, only trade-offs

**"Trace the Request"**

- Follow data from user → logic → disk
- Understand the full stack

---

## Getting Started

**Choose your starting point:**

**For Systems Design:** [01. Storage Engines →](systems/01-storage-engines.md)

- Implement B+Tree and LSM Tree from scratch
- Benchmark read vs write performance
- Understand when to use each

**For DSA:** [01. Two Pointers →](dsa/01-two-pointers.md)

- Learn three two-pointer patterns
- Implement from scratch
- Solve problems to build pattern recognition

---

**No pressure, no timelines. Just learn → implement → fill in → move on.**
