# Load Balancing

> Distributing traffic across multiple servers for availability and performance

---

## Learning Objectives

By the end of this topic you will be able to:

- Implement round robin, least connections, weighted round robin, consistent hashing, and IP hash algorithms from scratch
- Explain why consistent hashing minimises key redistribution when servers are added or removed compared to simple modulo hashing
- Compare the five algorithms across their handling of heterogeneous servers, stateful sessions, and server failures
- Identify thread-safety bugs in load balancer implementations including missing synchronisation and race conditions on the healthy-server list
- Choose the appropriate algorithm given requirements for session persistence, capacity difference, or cache-key locality
- Design virtual-node placement for consistent hashing and explain the trade-off between node count and distribution uniformity

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing load balancing algorithms, explain them simply.

**Prompts to guide you:**

1. **What is load balancing in one sentence?**
    - Your answer: <span class="fill-in">Load balancing is a ___ that works by ___</span>

2. **Why do we need load balancing?**
    - Your answer: <span class="fill-in">Without load balancing, a single server would ___, which causes ___</span>

3. **Real-world analogy for round robin:**
    - Example: "Round robin is like a teacher calling on students in order..."
    - Your analogy: <span class="fill-in">Think about how a checkout manager would assign customers to available cashiers one at a time — what rule would make it fair?</span>

4. **Real-world analogy for least connections:**
    - Example: "Least connections is like a bank choosing the shortest queue..."
    - Your analogy: <span class="fill-in">Think about how a supermarket manager watching multiple checkout lines would decide where to direct the next customer in line...</span>

5. **Why is consistent hashing useful?**
    - Your answer: <span class="fill-in">Consistent hashing is preferred when ___ because it avoids the cost of ___</span>

6. **What problem does IP hash solve that round robin can't?**
    - Your answer: <span class="fill-in">IP hash ensures ___ by ___, which round robin cannot guarantee because ___</span>

</div>

---

## Case Studies: Load Balancing in the Wild

### Netflix: Consistent Hashing for CDN Content Routing

- **Pattern:** Consistent hashing for routing video content requests.
- **How it works:** Netflix's CDN must serve video chunks to millions of concurrent viewers. Each video chunk is
  identified by a key. Using consistent hashing, a request for a specific chunk is always routed to the same CDN
  server, maximizing cache hits. When a server is added or removed (e.g., during scaling or a failure), consistent
  hashing ensures that only a small fraction of keys (~1/N) need to be remapped, dramatically reducing cache misses
  compared to a naive `key % N` approach.
- **Key Takeaway:** For caching systems where cache-hit rate is critical, consistent hashing is the standard choice.
  The virtual node technique further smooths the distribution across physical servers.

### AWS Elastic Load Balancer: Least Outstanding Requests

- **Pattern:** A variant of least connections called Least Outstanding Requests.
- **How it works:** AWS ELB's Application Load Balancer uses a "least outstanding requests" algorithm by default. This
  is similar to least connections but counts in-flight requests rather than total connections, making it more accurate
  for HTTP/2 connections that multiplex many requests. Each target's request count is tracked, and new requests are
  sent to the target with the fewest outstanding requests. This naturally handles heterogeneous workloads where some
  requests take much longer than others.
- **Key Takeaway:** For modern HTTP workloads with variable request duration, least-connections-style algorithms
  significantly outperform round robin. They are especially powerful when some requests are 10x or 100x slower than
  the average, as they prevent slow servers from accumulating a backlog.

### Memcached / Redis Clusters: Client-Side Consistent Hashing

- **Pattern:** Client-side consistent hashing for cache key distribution.
- **How it works:** In a Memcached or Redis cluster, clients (not a central proxy) implement consistent hashing to
  decide which server to contact for a given cache key. The client library maintains the hash ring in memory and
  performs the lookup locally, eliminating the central load balancer as a bottleneck or single point of failure.
  Libraries like Jedis (Java), ioredis (Node.js), and pylibmc (Python) implement this transparently.
- **Key Takeaway:** For high-throughput caching systems, client-side load balancing eliminates the latency and
  availability risk of a centralized load balancer. The trade-off is increased client complexity and the need to
  propagate cluster topology changes to all clients.

---

## Core Implementation

### Round Robin Flow

```mermaid
flowchart LR
    LB["Load Balancer\n(Round Robin)"]
    R1["Request 1"]
    R2["Request 2"]
    R3["Request 3"]
    R4["Request 4"]
    S1["Server 1"]
    S2["Server 2"]
    S3["Server 3"]

    R1 --> LB --> S1
    R2 --> LB --> S2
    R3 --> LB --> S3
    R4 --> LB --> S1
```

### Part 1: Round Robin Load Balancer

**Your task:** Implement round robin algorithm for equal traffic distribution.

```java
--8<-- "com/study/systems/loadbalancing/RoundRobinLoadBalancer.java"
```

!!! warning "Debugging Challenge — Missing Wraparound in Round Robin"

    The implementation below works for the first N requests, then throws an exception. Find the bug.

    ```java
    public synchronized Server getNextServer_Buggy() {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No servers available");
        }

        Server server = servers.get(currentIndex);
        currentIndex = currentIndex + 1;
        return server;
    }
    ```

    Trace what happens on request N+1 when `servers.size() == 3` and `currentIndex == 3`.

    ??? success "Answer"

        **Bug:** `currentIndex` increments without wrapping around. After the third request, `currentIndex = 3`, and `servers.get(3)` throws `IndexOutOfBoundsException` on a list of size 3.

        **Fix:**
        ```java
        currentIndex = (currentIndex + 1) % servers.size();
        ```

        The modulo ensures the index wraps to 0 after reaching the end, creating circular iteration. This is one of the most common bugs in round-robin implementations.

---

### Part 2: Least Connections Load Balancer

**Your task:** Implement least connections algorithm.

```java
--8<-- "com/study/systems/loadbalancing/LeastConnectionsLoadBalancer.java"
```

### Part 3: Weighted Round Robin

**Your task:** Implement weighted round robin for heterogeneous servers.

```java
--8<-- "com/study/systems/loadbalancing/WeightedRoundRobinLoadBalancer.java"
```

### Part 4: Consistent Hashing

**Your task:** Implement consistent hashing for distributed caching.

!!! tip "Why virtual nodes matter"
    Without virtual nodes, each physical server occupies only one point on the ring. With an unlucky placement, one server could end up responsible for 80% of the key space. Virtual nodes (placing each server at N positions) smooth the distribution: with 100–150 virtual nodes per server, distribution error typically falls below 5%.

```mermaid
graph TD
    Ring(["Hash Ring (0–360°)"])

    NodeA["Node A\n@ position 60"]
    NodeB["Node B\n@ position 150"]
    NodeC["Node C\n@ position 270"]

    Key1["Key 'user:42'\nhash=80 → Node B"]
    Key2["Key 'session:x'\nhash=200 → Node C"]
    Key3["Key 'order:7'\nhash=310 → Node A\n(wraps around)"]

    Ring --> NodeA
    Ring --> NodeB
    Ring --> NodeC
    NodeB --> Key1
    NodeC --> Key2
    NodeA --> Key3

    Note1["Virtual nodes: each physical\nserver occupies N ring positions\nfor even distribution"]
    Ring --> Note1
```

```java
--8<-- "com/study/systems/loadbalancing/ConsistentHashingLoadBalancer.java"
```

### Part 5: IP Hash Load Balancer

**Your task:** Implement IP hash for session persistence.

```java
--8<-- "com/study/systems/loadbalancing/IPHashLoadBalancer.java"
```

---

## Layer 4 vs Layer 7

The five algorithms above answer *how* requests are distributed. A separate question is *where* in the network stack the load balancer sits.

| | L4 — Transport layer | L7 — Application layer |
|---|---|---|
| **Sees** | IP address, port, protocol | HTTP headers, URL path, cookies |
| **SSL termination** | No — TLS passes through opaque | Yes — LB decrypts, inspects, re-encrypts |
| **Routing basis** | IP + port only | URL path, hostname, header values, cookies |
| **Overhead** | Low — no protocol parsing | Higher — full HTTP parsing per request |
| **Typical use** | Raw TCP, non-HTTP protocols | HTTP/HTTPS, microservices, API gateways |

**Choose L4 when** you need minimum overhead, are balancing non-HTTP protocols (game servers, MQTT, raw gRPC), or don't need to inspect request content.

**Choose L7 when** you need URL-based routing (e.g., `/api/*` → service-A, `/static/*` → CDN), SSL termination at the load balancer, cookie-based sticky sessions, or canary/A-B deployments.

!!! warning "L7 doesn't replace algorithm choice — it stacks on top of it"
    Choosing L7 determines what information the load balancer can use. You still need to choose *which algorithm* routes those requests. An L7 load balancer commonly runs round robin or least connections across the backend pool, while using URL path or headers to decide *which pool* to send to at all.

---

## Common Misconceptions

!!! warning "Least connections always outperforms round robin"
    Least connections is better when request duration varies widely (some requests take 10ms, others 2 seconds). For truly uniform request durations, round robin is equivalent in outcome and has lower overhead — it needs no per-server connection counter updates on every request start and end. Use least connections when request duration variance is high; use round robin when servers are homogeneous and requests are similar in cost.

!!! warning "Consistent hashing eliminates all redistribution when servers change"
    Consistent hashing minimises redistribution: adding one server to a ring of N causes only ~1/(N+1) of keys to move, compared to ~N/(N+1) with simple modulo hashing. It does not eliminate redistribution. If you need truly zero redistribution for a specific key, you need a manually managed key-to-server mapping table, which comes with its own operational cost.

!!! warning "IP hash provides reliable session persistence at scale"
    IP hash breaks when clients are behind a NAT gateway (many users share one IP) or when users are behind a proxy (IP changes between requests). It also breaks session persistence whenever the server list changes, since all hashes shift. For robust session persistence, prefer consistent hashing with the session ID as the key, or externalise session state to a shared store (Redis) so any server can serve the session.

---

## Decision Framework

<div class="learner-section" markdown>

**Questions to answer after implementation:**

### 1. Algorithm Selection

**When to use Round Robin?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use Least Connections?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use Weighted Round Robin?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use Consistent Hashing?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use IP Hash?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

### 2. Trade-offs

**Round Robin:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**Least Connections:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**Consistent Hashing:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**IP Hash:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

### 3. Your Decision Tree

Build your decision tree after practicing:
```mermaid
flowchart LR
    Start["What is your priority?"]

    N1["?"]
    Start -->|"Simple and fair distribution"| N1
    N2["?"]
    Start -->|"Consider server load"| N2
    N3["?"]
    Start -->|"Heterogeneous servers"| N3
    N4["?"]
    Start -->|"Session persistence"| N4
    N5["?"]
    Start -->|"Minimal redistribution on changes"| N5
```

### 4. Layer selection

**Use L4 when:**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key signals: <span class="fill-in">[Fill in]</span>

**Use L7 when:**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key signals: <span class="fill-in">[Fill in]</span>

</div>

---

## Practice

<div class="learner-section" markdown>

### Scenario 1: Load balance web application

**Requirements:**

- 5 web servers of equal capacity
- Stateless application
- Want even distribution
- Simple to understand

**Your design:**

- Which algorithm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How to handle server failures? <span class="fill-in">[Fill in]</span>
- Health check strategy? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if one of the 5 web servers becomes unavailable mid-rotation? <span class="fill-in">[Fill in]</span>
- How does your design behave when all 5 servers are overloaded simultaneously and new requests arrive? <span class="fill-in">[Fill in]</span>

### Scenario 2: Load balance with session state

**Requirements:**

- Users have shopping carts
- Cart stored in server memory
- 10 application servers
- Need session persistence

**Your design:**

- Which algorithm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How to handle server additions? <span class="fill-in">[Fill in]</span>
- Alternative to sticky sessions? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the server holding a user's in-memory cart goes down? <span class="fill-in">[Fill in]</span>
- How does your design behave when a server is removed from the pool while users have active sessions mapped to it? <span class="fill-in">[Fill in]</span>

### Scenario 3: Distributed cache cluster

**Requirements:**

- Cache cluster with 20 nodes
- Frequently add/remove nodes
- Want to minimize cache misses
- Keys should stay on same node

**Your design:**

- Which algorithm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How many virtual nodes? <span class="fill-in">[Fill in]</span>
- Replication strategy? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if a cache node is removed suddenly and the hash ring is not updated immediately? <span class="fill-in">[Fill in]</span>
- How does your design behave when a hot key causes a single node to receive a disproportionate share of requests? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these without referring to your notes or implementation.

1. You have a cluster of 10 cache servers using simple modulo hashing (`key % 10`). You add one more server, making it 11. Approximately what percentage of cached keys will need to move? Now explain how consistent hashing reduces this. Show the math for both.

    ??? success "Rubric"
        A complete answer addresses: (1) with modulo hashing, ~10/11 ≈ 91% of keys map to a different server when N changes from 10 to 11, (2) with consistent hashing, only ~1/11 ≈ 9% of keys move because only the keys between the new node and its predecessor on the ring are affected, (3) the derivation: modulo redistributes nearly everything; consistent hashing bounds redistribution to the fraction of the ring the new node occupies.

2. Your least-connections load balancer is supposed to be thread-safe because every method is `synchronized`. A colleague points out a race condition in `releaseConnection`. Describe a scenario with two threads that produces an incorrect connection count despite `synchronized` methods.

    ??? success "Rubric"
        A complete answer addresses: (1) the race window: two threads each call `releaseConnection` for the same server near-simultaneously — each reads count=1, each decrements to 0, result is 0 not -1 but the decrement may produce a negative value if not guarded, (2) `synchronized` prevents interleaved execution within a single method call but does not prevent a thread from reading a stale value if the method re-reads the field after another thread's decrement has been committed, (3) the correct fix is to guard against going below zero and confirm the atomicity of read-modify-write within the synchronized block.

3. A user complains their shopping cart disappears randomly. Your load balancer uses round robin. Explain precisely why this happens and describe two solutions — one that keeps round robin and one that does not.

    ??? success "Rubric"
        A complete answer addresses: (1) round robin sends successive requests from the same user to different servers; in-memory cart is only on the server that first received the add-to-cart request, (2) solution keeping round robin: externalise session state to a shared store (Redis/Memcached) so any server can read the cart regardless of which one is chosen, (3) solution replacing round robin: use IP hash or consistent hashing by session ID to ensure the same user always routes to the same server (with the caveats about what happens on server changes).

4. You are configuring consistent hashing for a cache cluster. The documentation says to use 150 virtual nodes per server. Your manager asks why not just 1, since that's simpler. Give a concrete explanation with an example showing what goes wrong with 1 virtual node and why 150 fixes it.

    ??? success "Rubric"
        A complete answer addresses: (1) with 1 virtual node per server, the physical placement on the ring is random; if three servers happen to land at hash positions 10, 12, and 900 on a 1000-point ring, the third server owns 898/1000 of the key space — extremely uneven, (2) with 150 virtual nodes per server, each physical server occupies 150 positions scattered across the ring, averaging out placement variance so each server ends up owning close to 1/N of the key space, (3) the trade-off is memory for the ring data structure, which is negligible in practice.

5. A colleague says "IP hash is strictly better than round robin for any application that has user sessions, because it keeps users on the same server." What is wrong with this claim? Describe at least two real-world scenarios where IP hash would give worse results than round robin plus externalised sessions.

    ??? success "Rubric"
        A complete answer addresses: (1) NAT: many users behind a corporate gateway or mobile carrier share one public IP, so all those users hash to the same server — creating a hot server while others sit idle, (2) server pool change: adding or removing a server reshuffles all IP-to-server mappings because modulo arithmetic changes, breaking session persistence for every user simultaneously, (3) IP instability: mobile users or users on DHCP change IPs between requests, so IP hash provides no persistence guarantee for these clients.

6. Your team needs to route `/api/*` requests to a backend service cluster and `/static/*` requests to an object-storage CDN, while also terminating TLS at the load balancer. Should this be an L4 or L7 load balancer? What specifically breaks if you use the other layer instead?

    ??? success "Rubric"
        A complete answer addresses: (1) L7 is required because URL path inspection (`/api/*` vs `/static/*`) requires parsing the HTTP request, which is only available at the application layer, (2) TLS termination is also an L7 concern — L4 passes opaque TLS bytes through without decrypting, so the load balancer cannot read the URL path at all, (3) an L4 balancer sees only IP address and port; it cannot distinguish request types or terminate TLS, making both URL-based routing and SSL offloading impossible.

---

## Connected Topics

!!! info "Where this topic connects"

    - **05. Caching Patterns** — consistent hashing was developed for distributed caching; load balancing reuses the same algorithm to minimise key redistribution when servers change → [05. Caching Patterns](05-caching-patterns.md)
    - **11. Database Scaling** — read replicas use load balancing to distribute query traffic; round robin and least-connections apply directly to database proxy routing → [11. Database Scaling](11-database-scaling.md)
    - **03. Networking Fundamentals** — the L4/L7 distinction (transport vs application layer) determines what information the load balancer can use for routing decisions → [03. Networking Fundamentals](03-networking-fundamentals.md)
