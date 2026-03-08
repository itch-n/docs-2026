# Software Engineering Study Guide

> A hands-on, fill-in-as-you-learn framework for systems design and algorithms

## How This Works

Each topic: implement the code stub → explain it in your own words → work through practice scenarios.

Orange fill-in prompts mark what you complete. Everything else is reference material.

---

## Quick Reference

Three pages for fast answers during interview prep or an incident:

- **[When It Breaks](reference/when-it-breaks.md)** — scale thresholds and failure conditions for all 35 topics
- **[Back of the Envelope](reference/back-of-envelope.md)** — hardware primitives, worked derivations, QPS/storage/write scale context
- **[Symptom → Pattern](reference/symptom-pattern.md)** — observable failure symptom → root cause → which concept to investigate

---

## Two Paths

### [Systems Design](systems/01-storage-engines.md) (20 Topics)

Build real implementations to understand how systems work:

- [ ] **[Storage Engines](systems/01-storage-engines.md)** - Implement B+Trees & LSM Trees
- [ ] **[Row vs Column Storage](systems/02-row-vs-column-storage.md)** - OLTP vs OLAP, columnar formats, compression
- [ ] **[Networking Fundamentals](systems/03-networking-fundamentals.md)** - TCP/UDP, HTTP versions, WebSockets, DNS, TLS
- [ ] **[Search & Indexing](systems/04-search-and-indexing.md)** - Inverted indexes, full-text search, ranking algorithms
- [ ] **[Caching Patterns](systems/05-caching-patterns.md)** - LRU, LFU, cache-aside, write-through
- [ ] **[API Design](systems/06-api-design.md)** - REST principles, versioning, pagination
- [ ] **[Security Patterns](systems/07-security-patterns.md)** - JWT, RBAC, API keys, secrets management
- [ ] **[Rate Limiting](systems/08-rate-limiting.md)** - Token bucket, sliding window algorithms
- [ ] **[Load Balancing](systems/09-load-balancing.md)** - Consistent hashing, health checks
- [ ] **[Concurrency Patterns](systems/10-concurrency-patterns.md)** - Locks, producer-consumer, thread safety
- [ ] **[Database Scaling](systems/11-database-scaling.md)** - Replication, sharding, partitioning
- [ ] **[Message Queues](systems/12-message-queues.md)** - Queue vs pub/sub, delivery guarantees
- [ ] **[Event Sourcing & CQRS](systems/13-event-sourcing-cqrs.md)** - Append-only event log, projections, read/write model separation
- [ ] **[Stream Processing](systems/14-stream-processing.md)** - Windowing patterns for real-time event aggregation
- [ ] **[Observability](systems/15-observability.md)** - Metrics, logging, tracing, SLOs
- [ ] **[Resilience Patterns](systems/16-resilience-patterns.md)** - Circuit breaker, bulkhead, retry/backoff, graceful degradation
- [ ] **[Distributed Transactions](systems/17-distributed-transactions.md)** - Saga pattern, idempotency
- [ ] **[Consensus Patterns](systems/18-consensus-patterns.md)** - Raft, leader election, distributed locks
- [ ] **[Microservices Patterns](systems/19-microservices-patterns.md)** - API gateway, service discovery, service mesh, decomposition
- [ ] **[Multi-Region Architecture](systems/20-multi-region.md)** - Active-active/passive, conflict resolution, geo-routing

### [DSA](dsa/01-two-pointers.md) (15 Topics)

Pattern-based approach from easy to advanced:

- [ ] **[Two Pointers](dsa/01-two-pointers.md)** - Opposite directions, same direction, different speeds
- [ ] **[Sliding Window](dsa/02-sliding-window.md)** - Fixed and variable window sizes for subarray problems
- [ ] **[Hash Tables](dsa/03-hash-tables.md)** - Fast lookups, grouping, frequency counting
- [ ] **[Linked Lists](dsa/04-linked-lists.md)** - Reversal, cycle detection, fast/slow pointers
- [ ] **[Stacks & Queues](dsa/05-stacks--queues.md)** - LIFO/FIFO, monotonic stacks, deque operations
- [ ] **[Trees](dsa/06-trees.md)** - Traversals (inorder/preorder/postorder/BFS) and recursive patterns (height, diameter, LCA)
- [ ] **[Binary Search](dsa/07-binary-search.md)** - Classic search, rotated arrays, 2D matrices
- [ ] **[Heaps](dsa/08-heaps.md)** - Priority queues, top K problems (prerequisite for Dijkstra)
- [ ] **[Union-Find](dsa/09-union-find.md)** - Disjoint sets for dynamic connectivity, Kruskal's MST
- [ ] **[Graphs (DFS/BFS)](dsa/10-graphs.md)** - Traversals, cycle detection, connected components
- [ ] **[Advanced Graphs](dsa/11-advanced-graphs.md)** - Topological Sort, Dijkstra, MST
- [ ] **[Backtracking](dsa/12-backtracking.md)** - Subsets, combinations, permutations, constraint satisfaction
- [ ] **[Dynamic Programming](dsa/13-dynamic-programming.md)** - Fibonacci, house robber, coin change
- [ ] **[Prefix Sums](dsa/14-prefix-sums.md)** - Range queries, subarray sum with HashMap, 2D prefix sum
- [ ] **[Intervals](dsa/15-intervals.md)** - Merge, insert, meeting rooms, greedy removal

---

## Learning Process

- Implement code stubs → fill in ELI5 explanations → work through debugging challenges
- Build Decision Frameworks for when to use (and NOT use) each pattern
- Complete practice scenarios → run through the Test Your Understanding rubric

---

## Key Principles

- **Implement First:** Build it to understand it, then explain it simply
- **Debug to Learn:** Fix broken implementations to understand edge cases
- **Practice Scenarios:** Apply concepts to real-world design problems

---

## Getting Started

**Systems Design:** [01. Storage Engines →](systems/01-storage-engines.md) - Implement B+Tree and LSM Tree, benchmark
performance

**DSA:** [01. Two Pointers →](dsa/01-two-pointers.md) - Learn three pointer patterns, build pattern recognition

---

**No pressure, no timelines. Just learn → implement → fill in → move on.**
