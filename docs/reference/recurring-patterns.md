# Recurring Patterns Across Abstractions

<p class="lead">Twenty-three problems that keep reappearing at every layer of the stack — from CPU microarchitecture to distributed systems to algorithms. Recognising the pattern lets you reason about an unfamiliar system by analogy.</p>

Each entry names the core tension, lists where the pattern appears across the stack, and states the invariant that makes it self-similar.

---

## Ordering and Concurrency

### 1. Head-of-line blocking

A single ordered channel creates a bottleneck at gaps. Slow or lost entries stall everything behind them, even entries that are independent.

- **TCP** — a lost segment stalls delivery of all bytes that arrived after it, across all HTTP/2 streams on that connection
- **HTTP/2** — eliminates application-level HOL blocking but inherits TCP's transport-level version
- **QUIC / HTTP/3** — fixes transport-level HOL by giving each stream independent packet numbering
- **Kafka partitions** — ordering guaranteed within a partition; a stalled consumer blocks that partition only. More partitions = more independent ordered lanes
- **CPU reorder buffer** — instructions execute out of order but the ROB commits in-order; a stalled instruction at the head blocks all commit behind it
- **Database WAL replay** — log entries applied in sequence; a gap stalls recovery

**The pattern:** a single ordered channel is simple to reason about but creates a bottleneck at gaps. The fix is always the same — push the ordering constraint up a level so gaps in one lane don't block others.

---

### 2. Monotonic counters as ordering primitives

When you need a total order across distributed or concurrent events, a globally (or locally) increasing number is the simplest primitive that works.

- **TCP sequence numbers** — every byte has a position; retransmission and reordering are detected by gaps
- **Kafka offsets** — consumers track position in a partition; replay is free
- **Raft term numbers** — higher term = more recent leader; stale leaders are rejected
- **MVCC transaction IDs** — each transaction gets an ID; snapshot isolation is "visible if committed before your ID"
- **ZooKeeper zxid** — every write gets a monotonically increasing transaction ID; clients can assert "I need state at least as fresh as zxid N"
- **Lamport timestamps** — logical clocks give partial ordering without synchronized clocks
- **Git commit graph** — commits form a DAG; ancestry is the ordering relation

**The pattern:** a counter is cheap to generate and compare. Once you have a total order, you can detect gaps, detect stale state, and replay from any point.

---

### 3. Optimistic vs pessimistic concurrency

Either assume conflict is rare (read freely, detect at commit) or assume conflict is common (lock before reading). The right choice depends on contention and the cost of rollback.

- **Database 2PL** — pessimistic: hold locks for the duration of the transaction
- **MVCC** — optimistic: readers never block writers; conflict detected at commit by comparing read set against concurrent writes
- **Git merge** — optimistic: work independently, resolve conflicts at merge time
- **HTTP ETags / conditional PUT** — optimistic: `If-Match: <etag>` fails if the resource changed since you read it
- **CAS (compare-and-swap)** — optimistic at the hardware level: the write only succeeds if the value hasn't changed
- **Lock-based version control (Perforce locks)** — pessimistic: check out = exclusive lock

**The pattern:** optimistic wins when conflicts are rare (reads dominate); pessimistic wins when conflicts are common (contention is high). The crossover point is determined by the cost of rollback vs the cost of waiting for a lock.

**Pitfall:** Applying optimistic concurrency to a resource with known high contention (e.g., a global counter for a popular item). The high rate of conflicts will cause constant transaction rollbacks, leading to worse performance and throughput than a simple pessimistic lock.

---

### 4. Two-phase pattern (prepare, then commit)

Separate "reserve/prepare" from "commit/execute." This gives you a checkpoint to verify preconditions before side effects are permanent, and a rollback point if something goes wrong.

- **Two-phase commit (2PC)** — prepare: all participants vote yes/no. Commit: coordinator sends commit only if all voted yes
- **Two-phase locking (2PL)** — growing phase: acquire locks. Shrinking phase: release. No new locks after first release
- **TCP handshake** — SYN reserves a slot; SYN-ACK confirms; ACK completes. Neither side commits until both have confirmed
- **Git staging area** — `git add` is prepare; `git commit` is commit. Lets you inspect exactly what will be committed
- **Database WAL** — write to log (prepare); apply to data pages (commit). The log is the durable record; the pages are the materialized view
- **Optimistic concurrency validation** — read phase (no locks); commit phase (validate + write)

**The pattern:** doing work in two phases lets you fail cheap (during prepare) before doing anything irreversible (during commit). The cost is latency — you pay at least two round trips.

---

### 5. The coordinator bottleneck

Centralized coordination is simple to reason about but becomes a throughput bottleneck or single point of failure as the system scales.

- **Kafka controller** — one broker coordinates partition leadership. At scale, controller becomes a bottleneck; KRaft (Kafka's Raft) distributes this
- **ZooKeeper leader** — all writes go through the leader; reads can be served by any follower but may be stale
- **Database primary** — all writes to one node; read replicas lag behind
- **Git central repo** — every push/pull goes through origin; DVCS makes every clone a full copy but coordination still converges on one remote
- **Raft leader** — all log entries flow through the leader; leader is bottleneck for write throughput

**The fix is always one of:** partition leadership across multiple coordinators (Kafka partition leaders), make coordination leaderless via quorum writes (Dynamo, Cassandra), or eliminate global coordination by scoping it to a smaller domain.

---

## Storage and Retrieval

### 6. Append-only writes + compaction

Writing is always an append; the data structure grows monotonically. Reads eventually suffer from scattered versions. Periodic compaction reorganizes data for read efficiency.

- **LSM trees** — writes go to memtable → SSTables; compaction merges SSTables and discards obsolete versions
- **Kafka log** — messages always appended; log compaction removes superseded keys, keeping only the latest value per key
- **Git pack files** — loose objects accumulate; `git gc` packs them and delta-compresses similar objects
- **JVM garbage collection** — objects allocated in eden; GC compacts survivors into old gen, freeing fragmented space
- **Copy-on-write filesystems (ZFS, Btrfs)** — writes never overwrite; old blocks become garbage until GC

**The pattern:** append-only makes writes fast and crash-safe (no partial overwrites). Compaction is the mandatory tax — skip it and reads degrade, space grows unbounded.

**Pitfall:** Using an LSM-tree-based system for a workload with a high rate of in-place updates or deletes. This generates excessive "garbage" (obsolete records or tombstones) that leads to high write amplification and wastes CPU/IO on compaction.

---

### 7. Tombstones / soft delete

In an append-only system, deletion is itself a write — a marker that records the absence of a key. The tombstone travels through the system until compaction can safely discard both it and the original value.

- **LSM tree deletes** — a delete writes a tombstone; compaction sees tombstone + original and drops both
- **Kafka log compaction** — a null-value message is a tombstone; compaction removes all earlier messages for that key
- **Cassandra** — deletes write a tombstone with a timestamp; GC grace period ensures all replicas receive it before compaction
- **DNS negative cache (NXDOMAIN TTL)** — "this name does not exist" is cached, preventing repeated lookups for a missing record
- **Git** — deleted files persist in history; you deleted a reference, not the object

**The pattern:** you can't mark something as gone in a system that only appends. The tombstone travels downstream until the system is sure every reader has seen it.

---

### 8. Indirection enables remapping

Never store the physical location of data directly; store a pointer to it. Moving or reorganizing data becomes updating a pointer, not copying data.

- **B-tree heap pointers** — leaf nodes store row IDs (heap pointers), not physical offsets; VACUUM can move rows without updating the index
- **Inode table** — a filename → inode mapping; the inode holds block addresses. Hard links are multiple names pointing to one inode
- **Virtual memory page table** — virtual address → physical frame; the OS can swap pages, remap, or copy-on-write without the process knowing
- **Consistent hashing virtual nodes** — each physical node owns multiple virtual nodes; rebalancing moves virtual nodes, not data
- **Git refs** — `HEAD`, branch names, and tags are text files containing a SHA. Moving a branch is updating a 40-byte file

**The pattern:** one level of indirection is almost free. Two levels (pointer to pointer) is how you get copy-on-write, snapshotting, and live migration.

---

### 9. Space–time tradeoff

You can always trade memory for speed (precompute and store) or speed for memory (compute on demand). Every cache, index, and precomputed aggregate is this tradeoff.

- **Hash tables** — O(1) lookup by storing keys in a precomputed bucket; O(n) extra space
- **Database indexes** — precomputed sort order or hash; reads are faster, writes pay maintenance cost
- **Memoization / DP table** — store subproblem results to avoid recomputation; classic space-for-time
- **Bloom filters** — O(1) probabilistic membership test with a small fixed bit array; trades accuracy for space
- **Precomputed aggregates (materialized views, OLAP cubes)** — compute rollups at write time so reads are instant
- **CPU branch predictor** — uses history table to predict branches; misprediction flushes pipeline (time cost) vs larger history table (space cost)

**The pattern:** nothing is free. Every speed improvement you get from storage is paid for at write time or in memory. The tradeoff is worthwhile only if reads dominate writes.

---

### 10. Compression via shared context

If sender and receiver share state, you can transmit diffs instead of full values. The more state they share, the better the compression.

- **HPACK (HTTP/2 header compression)** — client and server maintain a shared header table; repeated headers become 1–2 byte indices
- **Git delta compression** — pack files store similar objects as base + delta; `git clone` sends a pack, not individual objects
- **Video codecs (P-frames)** — I-frames (keyframes) are full images; P-frames encode only the diff from the previous frame
- **Database WAL replication** — replica applies the same log as primary; only the log (not the full page) is transmitted
- **SSH compression** — shared session state enables LZ77 compression of the stream

**The pattern:** the compression ratio is bounded by how much shared context exists. The risk is context corruption — if one side loses state, both sides must resync from scratch (HPACK header table reset, keyframe resync in video).

---

### 11. Partitioning via hashing

Distribute data across buckets, partitions, or nodes using a hash of the key. The hash function determines evenness of distribution; the number of buckets determines parallelism. The hard problem is what happens when the number of buckets changes.

- **Hash map** — the canonical single-node case: key → bucket via hash. Resize doubles capacity and rehashes everything rather than adding one bucket at a time, because `key % N` remaps almost every key when N changes
- **Kafka partition keys** — a key's hash determines which partition it lands on; all messages for a key arrive in order on the same partition
- **DynamoDB / Cassandra partition keys** — the partition key is hashed to find which node(s) in the ring own that data
- **Consistent hashing** — places both keys and nodes on a ring so that adding/removing a node only remaps keys in the adjacent segment, not the whole dataset
- **Sharded SQL databases** — shard key hashed to a database instance; schema migrations and resharding are the painful operational cost

**The pattern:** a hash function maps a key to exactly one owner. The quality of the hash determines distribution evenness — a bad hash creates hot spots. The cost of changing the number of buckets is what motivates consistent hashing: naive modulo remaps `(N-1)/N` keys on every resize; consistent hashing remaps only `K/N` keys.

**Pitfall:** choosing naive modulo hashing for a system that needs elastic scaling. Adding one node reshuffles ~all keys, causing a thundering herd of cache misses or data migrations. Consistent hashing or fixed-partition-count schemes (Kafka, Redis Cluster) are the standard mitigations.

---

## Distribution and Replication

### 12. Write-ahead before acknowledging

Don't tell the caller an operation succeeded until you have written proof of it to durable storage. The log entry is the durable record; everything else is a materialized view of the log.

- **Database WAL** — the transaction is durable once its log record is fsynced; the data pages can be written lazily
- **Kafka `acks=all`** — producer waits for all in-sync replicas to write before considering the message committed
- **ZooKeeper** — every write is committed to the transaction log on a quorum of nodes before the client receives an OK
- **Redis AOF** — `appendfsync always` fsyncs on every write; `appendfsync everysec` risks one second of data
- **Git** — `git commit` writes the commit object to `.git/objects` before updating the ref; a crashed commit leaves no ref, so no data loss

**The pattern:** the log is the ground truth. Everything downstream (indexes, caches, read replicas) can be rebuilt from it. Losing the log = losing the data.

---

### 13. Fan-out on write vs fan-out on read

When data must reach many consumers, you can push at write time (compute the fan-out eagerly) or pull at read time (compute it lazily when someone asks).

- **Twitter timeline fan-out** — for most users: write to each follower's feed at tweet time (fan-out on write). For celebrities with millions of followers: compute the merged feed at read time (fan-out on read). The crossover point is follower count
- **Database normalization vs denormalization** — normalized = fan-out on read (join at query time). Denormalized = fan-out on write (replicate data into each consumer's table)
- **Kafka consumer groups** — the broker retains one log; each consumer group reads independently (fan-out on read). No write amplification
- **CDN push vs pull** — push: origin pushes content to all edge nodes at publish time. Pull: edge fetches from origin on first miss, then caches

**The pattern:** fan-out on write is fast to read but expensive to write and hard to keep consistent. Fan-out on read is cheap to write but adds latency to reads. The right choice depends on read/write ratio and how many consumers exist.

---

### 14. Heartbeat = presence; silence = failure

You cannot distinguish "slow" from "dead" without a timeout. Any resource held by a node (a lock, a partition lease, a session) must be renewed on an interval or it expires.

- **ZooKeeper sessions** — ephemeral nodes disappear when the session expires; failure detection is automatic
- **Kafka consumer group** — a consumer that stops sending heartbeats is removed from the group; its partitions are rebalanced to others
- **TCP keepalive** — sends zero-length probes on idle connections; if no response, the connection is declared dead
- **Kubernetes liveness probe** — kubelet kills and restarts a container if the probe fails N consecutive times
- **Distributed lock TTL (Redis Redlock)** — the lock expires if the holder doesn't renew it; prevents a crashed holder from blocking forever
- **Raft leader lease** — the leader steps down if it doesn't receive heartbeat acks from a quorum within the election timeout

**The pattern:** leases and heartbeats are the only mechanism for failure detection in an asynchronous network. Setting the timeout too short causes false positives (flapping); too long causes slow recovery.

---

### 15. You can't guarantee both sides agree (Two Generals)

In the presence of network partitions, it is impossible to guarantee that two parties both know the outcome of an action. Every protocol that claims "exactly once" either relies on assumptions that can be violated, or pushes the uncertainty somewhere else.

- **TCP 3-way handshake** — the initiator can never be certain the final ACK arrived; the protocol accepts this and uses timeouts
- **Two-phase commit** — if the coordinator crashes after "prepare" but before "commit," participants are blocked indefinitely waiting for a decision
- **Raft leader election** — split votes require another round; there is no bound on the number of rounds, only probabilistic convergence
- **At-least-once vs exactly-once delivery** — Kafka exactly-once is "at-least-once + idempotent consumer + transactional producer." The guarantee is built on top of a weaker guarantee, not derived from first principles

**The pattern:** acknowledge the impossibility and design around it. Use idempotency to make at-least-once safe. Use consensus (Raft, Paxos) to bound the uncertainty to a known failure model. Never assume the other side received your last message.

---

### 16. Gossip / epidemic spread

Rather than a central broadcast (which creates a coordinator bottleneck), each node tells a random subset of its neighbors. Information spreads probabilistically but without a central authority.

- **Cassandra ring membership** — nodes gossip state to 3 random peers every second; every node eventually knows the full ring topology
- **Bitcoin block propagation** — a node that receives a new block announces it to its peers; propagation is O(log N) hops
- **Dynamo/Cassandra failure detection** — Phi accrual failure detector uses gossip to estimate node liveness
- **Epidemic algorithms** — the math is identical to SIR disease models; convergence time is O(log N) gossip rounds

**The pattern:** gossip trades consistency for availability. Every node's view converges eventually, but at any point two nodes may disagree. This is the right tradeoff for membership and topology state, but not for financial transactions.

---

## System Design Tradeoffs

### 17. Back-pressure

A fast producer will eventually overwhelm a slow consumer. Back-pressure is the mechanism by which a consumer signals "slow down" to its upstream producer.

- **TCP sliding window** — receiver advertises how many bytes it can buffer; sender cannot exceed this window
- **Kafka consumer lag** — lag is the back-pressure signal; if lag grows, the consumer is too slow and needs more partitions or instances
- **Reactive streams (Project Reactor, RxJava)** — `request(n)` is explicit back-pressure; the subscriber controls the flow rate
- **OS pipe buffer** — `write()` blocks when the pipe buffer is full; the writing process is suspended until the reader drains it
- **Rate limiting** — back-pressure expressed as policy: "you may not produce faster than N/s"

**The pattern:** without back-pressure, the system buffers until memory is exhausted, then drops. Back-pressure moves the slow-down signal upstream to the source — the only place it can be acted on.

---

### 18. The thundering herd

Many actors simultaneously converge on one resource after a shared trigger: a lock expires, a cache entry is evicted, a server restarts. The resulting spike often exceeds the system's capacity.

- **Cache stampede** — a popular cache entry expires; hundreds of requests simultaneously miss and all query the database
- **All consumers reconnecting after broker restart** — every Kafka consumer reconnects at the same moment; the broker is overwhelmed by simultaneous session setups
- **TCP SYN flood on server restart** — clients that were connected all attempt to reconnect simultaneously
- **Birthday problem in hash tables** — many keys hash to the same bucket when the hash function has poor distribution

**The fix is always one of:** jitter (add random delay to spread the spike), probabilistic early expiry (one node refreshes before the entry expires), or circuit breakers (refuse new requests until recovery).

---

### 19. Idempotency as the escape hatch for exactly-once

Exactly-once delivery is either impossible or very expensive. The practical alternative is at-least-once delivery with idempotent operations — you may receive a message twice, but applying it twice has the same effect as applying it once.

- **HTTP PUT and DELETE** — idempotent by definition; a duplicate PUT replaces the same resource with the same value
- **Kafka idempotent producer** — the broker deduplicates retried produces using a sequence number; at-least-once network semantics, exactly-once effect
- **TCP retransmission** — duplicate segments are detected via sequence numbers and discarded
- **Stripe/Braintree idempotency keys** — sending the same payment request twice with the same key charges once
- **Database upserts** — `INSERT ... ON CONFLICT DO UPDATE` is idempotent; running it twice leaves the same row

**The pattern:** design every state mutation to be safe to retry. Then at-least-once delivery is sufficient. This is almost always cheaper than building a true exactly-once protocol.

---

### 20. Push vs pull

Either the producer pushes data to consumers (low latency, tight coupling), or consumers pull from a durable log (natural back-pressure, replay, decoupling). The tradeoff is latency vs flexibility.

- **SSE / WebSockets vs polling** — push gives ~RTT latency; pull lets the consumer control rate
- **Kafka consumer model** — pull-based: consumers fetch at their own pace, enabling replay and independent lag management. Traditional message queues (RabbitMQ) are push-based: the broker delivers to consumers
- **CDN push vs pull** — push: origin pre-populates edge nodes at publish time (low first-request latency, storage cost). Pull: edge fetches on first miss (zero pre-work, cold start penalty)
- **Database replication** — streaming replication pushes WAL from primary to replica (low replication lag). Logical replication can be pull-based
- **Email vs webhooks vs polling APIs** — the same spectrum at the API layer

**The pattern:** push optimizes for latency and simplicity. Pull optimizes for resilience (consumer controls rate), replay (re-read from offset 0), and decoupling (consumer and producer don't need to be up simultaneously). This is the consumer-side view of the fan-out on write vs. read pattern (#13).

---

### 21. The log as the source of truth

State is derived from a sequence of events. Replay the log and you reconstruct state. This makes the log the only thing you need to persist durably — everything else is a cache.

- **Database WAL** — the pages on disk are a cache of what the log says the state should be; WAL replay reconstructs them
- **Kafka** — the log is the system. Consumer offsets, topic state, and compacted views are all derived from the log
- **Event Sourcing** — application state is a fold over an event log; projections are materialized views, not the source of truth
- **Git commit history** — the working tree is a materialized view of the commit log; `git checkout` replays history to a point
- **Redis AOF** — the append-only file is the log; the in-memory state is rebuilt on restart by replaying it
- **ZooKeeper transaction log** — every state change is a log entry; the in-memory tree is derived from it

**The pattern:** if the log is durable, nothing else needs to be. Every other representation (indexes, caches, read replicas, projections) is a performance optimization that can be rebuilt. The log is the only irreplaceable artifact. This is the architectural principle enabled by append-only writes (#6) and write-ahead before acknowledging (#12).

---

### 22. Amortization

A fixed setup cost is too high to pay per operation. Pay it once and spread it across many operations. The efficiency gain is only realized if the amortized operations outnumber the setup.

- **TCP connection reuse (HTTP keep-alive)** — the 3-way handshake + TLS handshake cost ~2 RTT; paying it per request kills throughput
- **HPACK header compression** — the header table is built once per connection; subsequent requests send diffs
- **JIT compilation** — the JVM interprets bytecode until a method is "hot," then pays the compilation cost once for many subsequent calls
- **Database connection pooling** — connection setup costs ~5–50ms; a pool of 20 connections serves thousands of requests/second
- **B-tree page reads** — reading a 16KB page to find one row is expensive; buffer pool caches pages so subsequent lookups are free
- **Kafka producer batching** — accumulate messages for 5ms before sending; the per-batch overhead (headers, network round trip) is amortized over many messages

**The pattern:** amortization is only a win if the reuse rate is high enough. Connection pools that are too large waste connections; pools too small under-amortize. The optimal size is a function of request rate and setup cost.

---

### 23. Tail latency and the slowest participant

When a request fans out to N participants, the caller must wait for all N. The response time distribution is dominated by the maximum, not the mean. At large N, tail latency is almost certain to be hit.

- **Scatter-gather database queries** — a query that fans out to 100 shards must wait for the slowest shard; p99 of 100 independent p99s is much worse than a single p99
- **Microservice fan-out** — a synchronous call chain through 5 services compounds latency; one slow service stalls the entire request
- **MapReduce** — the reduce phase cannot start until all map tasks finish; one slow mapper (a "straggler") delays the job
- **Kafka consumer group at partition rebalance** — all consumers pause while the group coordinator reassigns partitions
- **DNS resolution** — a page load makes 10–20 DNS lookups; one slow resolver delays the whole page

**The fix is always one of:** speculative execution (send the request to two nodes, take the first response), hedged requests (resend after a timeout without cancelling the first), or reducing fan-out (fewer, larger shards).

**The pattern:** the mean latency of N independent requests is bounded; the maximum is not. Design for p99 of N, not mean of N. At N=100, even a 1% tail latency event is near-certain to be hit.

**Pitfall:** Failing to propagate deadlines and cancellation signals downstream in a scatter-gather system. Without it, the system continues to do useless work for requests that have already timed out upstream, wasting resources and potentially causing cascading failures.
