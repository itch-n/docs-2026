# Symptom → Pattern

> You observe X. The root cause is Y. The concept to investigate is Z.

Use this page when debugging production incidents or reasoning about a design question. Each entry maps an observable symptom to its likely root cause and the systems pattern that addresses it.

This is a diagnostic starting point, not a decision tree. Real incidents have multiple contributing factors. Use the topic links to go deeper.

---

## Performance Degradation

| Symptom | Root Cause | Pattern |
|---------|-----------|---------|
| Query latency spikes from 5ms to 500ms during write bursts | B+Tree page splits cascade up the tree, triggering multiple read-modify-write operations on parent pages | [Storage Engines](systems/01-storage-engines.md) — consider LSM for write-heavy workloads |
| Analytics query over 1B rows takes 30 seconds despite ample CPU | Row storage reads every column; 96% of I/O is wasted reading columns you don't need | [Row vs Column Storage](systems/02-row-vs-column-storage.md) — columnar storage for selective OLAP queries |
| Read latency grows as write rate increases; no schema changes made | LSM SSTable count accumulates without compaction; reads must check every level | [Storage Engines](systems/01-storage-engines.md) — compaction is mandatory, not optional |
| HTTP page load takes 5s for 100 assets; network throughput is fine | HTTP/1.1 head-of-line blocking: requests serialised behind each other per TCP connection | [Networking](systems/03-networking-fundamentals.md) — HTTP/2 multiplexing |
| p99 latency spikes while p50 stays flat; no obvious cause | Single packet loss stalls entire HTTP/2 connection (transport-level HOL blocking) | [Networking](systems/03-networking-fundamentals.md) — HTTP/3/QUIC for per-stream recovery |
| Search returns 0 results for "wireles headphone" (user typo) | Inverted index stores exact tokens; no fuzzy matching or n-gram index built | [Search & Indexing](systems/04-search-and-indexing.md) — edge n-grams at index time or Levenshtein at query time |
| Elasticsearch queries slow as index grows from 10GB to 100GB | Too many small shards (e.g. 200 on 5 nodes); coordination overhead exceeds parallelism benefit | [Search & Indexing](systems/04-search-and-indexing.md) — keep shard size 10–50GB |
| Cache misses spike after deploy; DB load quadruples | No cache invalidation strategy; stale entries not cleared on write | [Caching Patterns](systems/05-caching-patterns.md) — invalidate before write, or use write-through |
| Adding a new field to Elasticsearch slows all queries | Dynamic field mapping created thousands of new indexed fields consuming JVM heap | [Search & Indexing](systems/04-search-and-indexing.md) — disable dynamic mapping; define fields explicitly |

---

## Availability / Reliability

| Symptom | Root Cause | Pattern |
|---------|-----------|---------|
| All users time out simultaneously when a popular cache entry expires | Thundering herd: all threads detect miss at once and all query the DB concurrently | [Caching Patterns](systems/05-caching-patterns.md) — per-key locking or probabilistic early expiration (XFetch) |
| Rate limiter allows 2× the configured limit for a few seconds at window boundaries | Fixed-window boundary attack: attacker sends requests at end of window N and start of window N+1 | [Rate Limiting](systems/08-rate-limiting.md) — token bucket or sliding window log |
| Shopping carts disappear when requests hit different servers | Stateful session stored in-process; round-robin LB routes same user to different instances | [Load Balancing](systems/09-load-balancing.md) — sticky sessions or external session store (Redis) |
| Two users simultaneously withdraw more than the account balance | Check-then-act race condition; balance check and debit are not atomic | [Concurrency](systems/10-concurrency-patterns.md) — SELECT FOR UPDATE or optimistic locking with version |
| System crashes hard under load instead of degrading gracefully | No bulkhead isolation; all thread pool threads blocked waiting on slow downstream calls | [Resilience Patterns](systems/16-resilience-patterns.md) — bulkhead + circuit breaker |
| One slow downstream dependency brings down the entire service | Cascading failure: blocked threads exhaust shared thread pool; new requests queue indefinitely | [Resilience Patterns](systems/16-resilience-patterns.md) — timeout + circuit breaker per dependency |
| All callers retry simultaneously; load spikes exactly during recovery | Exponential backoff without jitter causes synchronised retry waves | [Resilience Patterns](systems/16-resilience-patterns.md) — full jitter (random(0, cap)) |
| Duplicate emails or payments after client retries on timeout | At-least-once delivery without idempotency; retry processes the same request twice | [Message Queues](systems/12-message-queues.md) — idempotency keys on every non-read operation |
| Dead letter queue accumulates silently; system appears healthy | Poison pill messages fail processing repeatedly; DLQ fills but no alerts configured | [Message Queues](systems/12-message-queues.md) — DLQ alerting is mandatory, not optional |
| All threads permanently hang after one exception in synchronised code | Lock acquired but not released on exception; `unlock()` not in `finally` block | [Concurrency](systems/10-concurrency-patterns.md) — always use try-finally with ReentrantLock |

---

## Data Consistency

| Symptom | Root Cause | Pattern |
|---------|-----------|---------|
| Cache shows stale data seconds after a write that succeeded | Invalidation race: cache delete races with concurrent reads that re-cache the old value | [Caching Patterns](systems/05-caching-patterns.md) — invalidate before write, not after |
| "Write then immediately read" returns nothing | Replication lag: write goes to primary, read hits replica before replication completes | [Database Scaling](systems/11-database-scaling.md) — read-your-writes: route post-write reads to primary |
| Payment processed twice; idempotency key is set but doesn't help | Idempotency key stored inside the same transaction as the payment; rollback removes both | [API Design](systems/06-api-design.md) — idempotency store must survive transaction rollback |
| User deleted from auth service but still appears in search results | CQRS read model out of sync; projection not updated after write-side event | [Event Sourcing & CQRS](systems/13-event-sourcing-cqrs.md) — explicit consistency model + compensation |
| Two active-active regions both accept writes to the same record; one silently lost | Concurrent writes produce a conflict; last-write-wins silently drops the lower-timestamp write | [Multi-Region](systems/20-multi-region.md) — conflict resolution strategy required upfront |
| Saga compensation fails; partial transaction leaves data in inconsistent state | Compensation not possible for already-published side effects (email sent, webhook fired) | [Distributed Transactions](systems/17-distributed-transactions.md) — avoid uncommittable side effects early in saga |
| 2PC transaction holds locks indefinitely after coordinator crash | Coordinator crashed after sending prepare but before sending commit; participants block waiting | [Distributed Transactions](systems/17-distributed-transactions.md) — coordinator crash is the failure mode 2PC cannot recover from |

---

## Scale Ceiling Hit

| Symptom | Root Cause | Pattern |
|---------|-----------|---------|
| DB CPU is low but write throughput plateaus at ~10–20k writes/sec | Single PostgreSQL primary fsync bottleneck: ~1ms per fsync × group commit size = hard ceiling | [Database Scaling](systems/11-database-scaling.md) — write sharding or Kafka write buffer in front of DB |
| Connections exhausted at the DB despite low CPU; new requests rejected | Each app instance opens its own full connection pool; 50 pods × 20 connections = 1,000 raw DB connections | [Database Scaling](systems/11-database-scaling.md) — PgBouncer in transaction mode (100–200× multiplier) |
| Redis becomes bottleneck at ~50k req/sec despite fast hardware | Centralized Redis rate-limit counter lookup ~0.5ms each; single node serialises all checks | [Rate Limiting](systems/08-rate-limiting.md) — per-node token bucket + periodic Redis sync |
| Cold start of a new consumer takes hours; log is old | Event sourcing with no snapshots; must replay from the beginning of the event log | [Event Sourcing & CQRS](systems/13-event-sourcing-cqrs.md) — periodic snapshots are not optional above ~100M events |
| Stream processing falls behind during high-volume windows | State store exceeds JVM heap; spill to disk causes ~10× throughput drop | [Stream Processing](systems/14-stream-processing.md) — tune state store size or narrow window |
| Service mesh control plane degrades as microservice count grows | Control plane overhead scales super-linearly; documented degradation starts around 50+ services | [Microservices](systems/19-microservices-patterns.md) — benchmark control plane at target service count before adopting |
| Write latency exceeds SLO on any cross-region synchronous operation | Speed of light: US East ↔ US West ~70ms round trip — a hard physics floor, not a config issue | [Multi-Region](systems/20-multi-region.md) — synchronous cross-region writes are incompatible with <100ms write SLOs |

---

## Ops / Observability Blind Spots

| Symptom | Root Cause | Pattern |
|---------|-----------|---------|
| DNS failover takes 60+ minutes; clients stuck on dead IP | TTL set to 3600s; clients must cache the old IP until it expires naturally | [Networking](systems/03-networking-fundamentals.md) — lower TTL to 300s before planned maintenance |
| Prometheus memory usage explodes after adding one new label | High-cardinality label (user_id, request_id) multiplies existing time series count multiplicatively | [Observability](systems/15-observability.md) — keep label distinct values < ~100 |
| A bug affecting 0.5% of requests never appears in traces | Sampling rate at 1%; issues affecting < 1% of traffic are statistically invisible | [Observability](systems/15-observability.md) — adaptive sampling; increase rate on error paths |
| Can't determine if latency spike is DB, cache, or network | No distributed tracing; logs exist per service but with no request correlation | [Observability](systems/15-observability.md) — trace context propagation (OpenTelemetry W3C headers) |
| Replication lag invisible until users report stale data | No lag monitoring on replicas; replica appears healthy until application-level inconsistency surfaces | [Database Scaling](systems/11-database-scaling.md) — monitor replica lag (`pg_stat_replication`) as a primary metric |
| TCP packet loss invisible in application latency metrics | TCP retransmission handled silently below the application layer; only visible in OS-level counters | [Networking](systems/03-networking-fundamentals.md) — monitor `tcp_retransmits` alongside app-level metrics |
| Raft cluster enters repeated election cycles; no nodes failing | Clock skew between nodes exceeds election timeout; false timeouts trigger spurious elections | [Consensus](systems/18-consensus-patterns.md) — NTP synchronisation + tuned heartbeat/election timeout ratio |

---

## Security Gaps

| Symptom | Root Cause | Pattern |
|---------|-----------|---------|
| Expired JWT still accepted by the API | Signature validated but `exp` claim never checked; authentication ≠ freshness | [Security Patterns](systems/07-security-patterns.md) — validate both signature AND claims; they are separate checks |
| Attacker determines correct API key one character at a time via response timing | String comparison short-circuits on first mismatch; response time leaks character position | [Security Patterns](systems/07-security-patterns.md) — constant-time comparison (MessageDigest.isEqual) |
| Forced logout doesn't prevent the token from working | JWT is stateless; no revocation mechanism exists without a centralised denylist | [Security Patterns](systems/07-security-patterns.md) — short TTL + refresh tokens, or Redis denylist with EXPIRE |
| Admin user accesses another tenant's data | RBAC grants role at org level; no tenant-scoped resource check on individual queries | [Security Patterns](systems/07-security-patterns.md) — resource-level authorisation check on every query |
| Credentials found in version control; attacker gains DB access | Secrets committed to source control as plaintext | [Security Patterns](systems/07-security-patterns.md) — secrets manager (Vault, AWS SM) + pre-commit hooks |
| 1,000 login attempts per second against authentication endpoint | No rate limiting on auth; credential stuffing attacks proceed unimpeded | [Rate Limiting](systems/08-rate-limiting.md) — rate limit by IP and username; exponential backoff after N failures |
