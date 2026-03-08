# When It Breaks

> Pre-interview reference. One-liner thresholds and failure conditions for every topic. Each entry links to the full explanation in the topic page.

This is a review sheet — not a substitute for doing the work. The numbers and conditions here will only stick if you've already worked through the implementations.

---

## Systems Design

| Topic | Breaks when... |
|-------|----------------|
| [Storage Engines](systems/01-storage-engines.md) | **B+Tree:** page splits cascade at ~10k writes/sec on spinning disk. **LSM:** write rate exceeds compaction throughput — RocksDB slowdown trigger at 20 L0 files, stop at 36. |
| [Row vs Column Storage](systems/02-row-vs-column-storage.md) | **Row:** full table scan reads all columns even for a 3-of-200 column query; cliff around 10M+ rows for aggregation workloads. **Column:** point updates touch every column file — expensive for OLTP. |
| [Networking Fundamentals](systems/03-networking-fundamentals.md) | **TCP:** 0.1% packet loss → 200ms p99 spike (RTO floor). **HTTP/1.1:** 6-connection browser limit. **DNS:** wrong TTL during deploys (too low = query flood; too high = stale failover). **TLS:** ~100ms handshake intercontinentally without session resumption. |
| [Search & Indexing](systems/04-search-and-indexing.md) | **Elasticsearch:** field data cache >50% JVM heap → circuit breaker. Write rate >10k docs/sec saturates the 1s refresh interval. Eventually consistent by design — written docs invisible for up to 1s. |
| [Caching Patterns](systems/05-caching-patterns.md) | **Per-instance:** breaks at 2+ servers (no invalidation propagation). **Redis single node:** ~100k ops/sec. **Thundering herd:** popular TTL expiry under high concurrency overwhelms the database. |
| [API Design](systems/06-api-design.md) | **Offset pagination:** O(n) scan at deep pages (`OFFSET 1,000,000` scans 1M rows). **GraphQL:** unrestricted depth is a DoS vector — N+1 queries behind one request. **REST versioning:** >2–3 active versions is operationally unsustainable. |
| [Security Patterns](systems/07-security-patterns.md) | **JWT:** no revocation mechanism — logout requires a denylist (which negates statelessness) or short expiry. **RBAC:** unmanageable at ~20–30 roles; role proliferation is the norm, not the exception. |
| [Rate Limiting](systems/08-rate-limiting.md) | **Per-node:** N nodes = N× the intended limit. **Redis-backed:** bottleneck at ~50k req/s. **Fixed window:** double-rate attack at window boundary — 2× limit for a brief window. |
| [Load Balancing](systems/09-load-balancing.md) | **Round robin + stateful services:** node removal drops all its sessions. **L7 TLS termination:** saturates at ~100k connections/sec. **Connection draining:** 30s drain × 50 nodes = 25 minutes for a rolling restart. |
| [Concurrency Patterns](systems/10-concurrency-patterns.md) | **`synchronized` under contention:** throughput declines as threads are added (opposite of expected). **CAS:** silent wrong results under ABA. **Thread pools:** I/O blocking exhausts all threads; new tasks queue indefinitely. |
| [Database Scaling](systems/11-database-scaling.md) | **Read replicas:** replication lag causes stale reads (write then immediately read returns nothing). **Sharding:** cross-shard JOINs require scatter-gather. **Single PostgreSQL primary:** ~10–20k writes/sec ceiling. **Connection pool:** >max_connections (default 100) OOMs the DB server. |
| [Message Queues](systems/12-message-queues.md) | **At-least-once delivery:** breaks idempotency assumptions silently (duplicate payments, emails, decrements). **Multiple consumers:** breaks ordering. **Dead letter queue:** poison pills fill it silently while the system appears healthy. |
| [Event Sourcing & CQRS](systems/13-event-sourcing-cqrs.md) | **Event log size:** 10k events/sec = 864M events/day; cold replay becomes impractical. **Schema evolution:** can't migrate historical events in place. **CQRS:** dual-store synchronisation failures create consistency windows teams underestimate. |
| [Stream Processing](systems/14-stream-processing.md) | **Exactly-once:** 10–20% throughput vs at-least-once. **State > memory:** disk spill reduces throughput ~10×. **Watermarks:** too aggressive drops late events silently; too generous doubles memory requirements. |
| [Observability](systems/15-observability.md) | **High-cardinality labels** (user ID, request ID) explode Prometheus time series — limit labels to <~100 distinct values. **Low sampling rate:** 1% sampling misses a bug affecting 0.5% of requests. **Missing context propagation:** trace appears to terminate mid-chain. |
| [Resilience Patterns](systems/16-resilience-patterns.md) | **Circuit breaker threshold too low:** opens on transient errors, amplifying outages. **Backoff without jitter:** all callers retry simultaneously, spiking load exactly during recovery. **Non-idempotent retry:** retrying a payment or email send is worse than failing once. |
| [Distributed Transactions](systems/17-distributed-transactions.md) | **2PC:** coordinator crash after "prepare" holds all participant locks indefinitely — lock duration bounded only by coordinator recovery time. **Saga compensation:** impossible for some operations (email sent, notification published). |
| [Consensus Patterns](systems/18-consensus-patterns.md) | **Quorum unavailable:** 3-node cluster loses 2 nodes → writes halt entirely (correct but surprising). **Clock skew > election timeout:** cluster enters repeated election cycles. etcd/ZooKeeper documented thundering herd on leader loss requires tuned heartbeat parameters. |
| [Microservices Patterns](systems/19-microservices-patterns.md) | **Service mesh:** control plane degrades super-linearly at ~50+ services. **API gateway as shared SPOF:** one misconfiguration takes down all services. **Service discovery:** high registration churn floods the registry. |
| [Multi-Region Architecture](systems/20-multi-region.md) | **Active-active:** concurrent writes produce conflicts; last-write-wins loses data. **Active-passive:** DNS TTL-based failover = 30–120s outage. **Synchronous cross-region writes:** speed of light between US-East and US-West is ~70ms — incompatible with <100ms write SLOs. |

---

## DSA

| Topic | Breaks when... |
|-------|----------------|
| [Two Pointers](dsa/01-two-pointers.md) | **Opposite-direction:** requires sorted input; silent wrong results on unsorted arrays. **Duplicates:** pointer advance without skipping all matches produces duplicate result pairs. |
| [Sliding Window](dsa/02-sliding-window.md) | **Non-monotonic shrink condition:** e.g. "max minus min ≤ k" — shrinking can restore validity, making the standard loop wrong. **Non-contiguous problems:** two-sum / k-sum need a hash map, not a window. |
| [Hash Tables](dsa/03-hash-tables.md) | **Adversarial inputs** without hash randomisation collapse O(1) to O(n). **<~10 keys:** array linear scan is often faster (cache locality). **Two Sum:** same-index reuse check (`index != i`) is easy to omit. |
| [Linked Lists](dsa/04-linked-lists.md) | **Random access:** O(n) — wrong for index-based lookup or binary search. **Cache:** pointer chasing causes cache misses; arrays with O(n) insert are often faster in practice. Floyd's algorithm requires the second phase to find the cycle entry node. |
| [Stacks & Queues](dsa/05-stacks--queues.md) | **Monotonic stack:** storing values instead of indices produces silent wrong answers with duplicates. **Two-stack queue:** peek is O(n), not O(1). **Dual extremes:** tracking both max and minimum simultaneously requires two separate deques. |
| [Trees](dsa/06-trees.md) | **Deep recursion:** stack overflow at ~500–1000 frames (degenerate tree of 10k nodes). **Unbalanced BST:** sorted insertions degrade to O(n); always use AVL/red-black in production. **Diameter without single-pass trick:** O(n²) on unbalanced trees. |
| [Binary Search](dsa/07-binary-search.md) | **Non-monotonic predicate:** algorithm skips the answer or loops. **Float convergence condition:** loops indefinitely due to precision — use fixed iteration count (50–100). **Expensive `canAchieve()`:** multiplied by O(log range) may be impractical. |
| [Heaps](dsa/08-heaps.md) | **Arbitrary deletion:** O(n) find required. **Lazy deletion:** heap grows unbounded if deletions dominate inserts. **Two-heap median with deletions:** O(n) balance maintenance — use an order-statistics tree instead. |
| [Union-Find](dsa/09-union-find.md) | **Directed graphs:** edge direction ignored, wrong connectivity. **Deletions:** union is one-way; splits are not supported. **Persistent snapshots:** path compression mutates on read, making rollback impossible. |
| [Graphs](dsa/10-graphs.md) | **BFS memory:** frontier grows to O(V) for high-branching graphs — use bidirectional BFS. **DFS ≠ shortest path.** **Adjacency matrix:** 1M nodes = 1TB memory; only appropriate when edges ≈ V². |
| [Advanced Graphs](dsa/11-advanced-graphs.md) | **Dijkstra:** fails with negative edge weights (use Bellman-Ford). **Topological sort:** undefined on graphs with cycles. **Kruskal's MST:** undirected graphs only — directed equivalent requires Edmonds' algorithm. |
| [Backtracking](dsa/12-backtracking.md) | **No pruning:** O(n!) or O(k^n) — a few extra elements turns milliseconds into minutes. **Overlapping subproblems:** same `(start, state)` in multiple branches = redundant work → memoize → DP. |
| [Dynamic Programming](dsa/13-dynamic-programming.md) | **State space too large:** 3D table over n=1000 needs 10⁹ cells. **No overlapping subproblems:** memoization adds overhead with no benefit. **`Integer.MAX_VALUE + 1`:** overflows silently on addition — use `MAX_VALUE / 2` as sentinel. |
| [Prefix Sums](dsa/14-prefix-sums.md) | **Array mutation:** prefix array is stale after any update — use a Fenwick tree or segment tree. **Max/min subarray sum:** prefix + HashMap is for counting; use Kadane's for optimisation. **2D formula:** four off-by-one failure points in inclusion-exclusion. |
| [Intervals](dsa/15-intervals.md) | **2D rectangles:** need a sweep line algorithm, not sort-and-scan. **Meeting Rooms II:** greedy heap gives the count, not which events conflict. **Dynamic insert/delete:** static sort breaks — use an interval tree (O(log n) per operation). |
