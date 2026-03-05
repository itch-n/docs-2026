# Networking Fundamentals

> TCP/IP vs UDP, HTTP versions, WebSockets, DNS, and TLS

---

## Learning Objectives

By the end of this topic you will be able to:

- Explain the reliability guarantees TCP provides and the cost at which it provides them, compared to UDP
- Compare HTTP/1.1, HTTP/2, and HTTP/3 and identify which problem each version solves
- Choose between HTTP polling, Server-Sent Events, and WebSockets given a real-time communication requirement
- Explain the DNS resolution chain and how TTL affects both performance and failover speed
- Describe the TLS 1.3 handshake and the strategies used to reduce its latency overhead

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After learning networking fundamentals, explain them simply.

**Prompts to guide you:**

1. **What is the difference between TCP and UDP in one sentence?**
    - TCP is a protocol that ___ every packet, while UDP is a protocol that <span class="fill-in">[___ packets without waiting for ___, trading ___ for lower ___]</span>

2. **Why do we need DNS?**
    - DNS exists because <span class="fill-in">[humans remember ___ but computers route traffic using ___, so DNS acts as a ___ that translates between them]</span>

3. **Real-world analogy for TCP vs UDP:**
    - Example: "TCP is like certified mail where..."
    - Think about the difference between sending a registered letter (confirmation of delivery) vs dropping a flyer in a post box (fire and forget).
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What does HTTP/2 multiplexing solve?**
    - HTTP/2 multiplexing solves ___ blocking by <span class="fill-in">[allowing ___ requests to travel over a ___ TCP connection simultaneously, so a slow response on stream ___ no longer blocks stream ___]</span>

5. **Real-world analogy for WebSockets:**
    - Example: "WebSockets are like a phone call where..."
    - Think about the difference between sending text messages (each a separate round trip) vs staying on an open phone call.
    - Your analogy: <span class="fill-in">[Fill in]</span>

</div>

---

## Quick Quiz (Do BEFORE learning)
    Complete your predictions now, before reading further. You will revisit and verify each answer after working through the core concepts.

<div class="learner-section" markdown>

**Your task:** Test your intuition about networking without looking at details. Answer these, then verify after learning.

### Protocol Predictions

1. **When would you choose UDP over TCP?**
    - Your guess: <span class="fill-in">[What scenarios?]</span>
    - Verified: <span class="fill-in">[Actual use cases]</span>

2. **What's the main benefit of HTTP/2 over HTTP/1.1?**
    - Your guess: <span class="fill-in">[Speed? Efficiency? Something else?]</span>
    - Verified: <span class="fill-in">[Actual benefit]</span>

3. **WebSockets vs HTTP polling - which is better for real-time chat?**
    - Your guess: <span class="fill-in">[Which one and why?]</span>
    - Verified: <span class="fill-in">[Actual answer with trade-offs]</span>

### Scenario Predictions

**Scenario 1:** Video streaming application with 1M concurrent users

- **Protocol choice:** <span class="fill-in">[TCP/UDP - Why?]</span>
- **DNS strategy:** <span class="fill-in">[How to handle this scale?]</span>

**Scenario 2:** Real-time multiplayer game with low latency requirement

- **Protocol choice:** <span class="fill-in">[TCP/UDP - Why?]</span>
- **Packet loss handling:** <span class="fill-in">[What happens?]</span>
- **Expected latency:** <span class="fill-in">[Milliseconds? Seconds?]</span>

**Scenario 3:** Financial trading platform requiring guaranteed message delivery

- **Protocol choice:** <span class="fill-in">[TCP/UDP - Why?]</span>
- **TLS overhead:** <span class="fill-in">[Worth it?]</span>
- **Connection pooling:** <span class="fill-in">[Helpful?]</span>

</div>

---

## Core Concepts

### Topic 1: Transport Layer - TCP vs UDP

**Concept:** Two fundamental protocols for sending data over networks, with different guarantees and trade-offs.

**Key Differences:**

**TCP (Transmission Control Protocol)**

- **Reliable:** Guarantees delivery, ordering, and error checking
- **Connection-oriented:** Establishes connection before data transfer (3-way handshake)
- **Flow control:** Prevents overwhelming receiver
- **Congestion control:** Adapts to network conditions
- **Overhead:** Higher due to acknowledgments and retransmissions
- **Use cases:** HTTP, email, file transfers, databases

**UDP (User Datagram Protocol)**

- **Unreliable:** Best-effort delivery, no guarantees
- **Connectionless:** Send packets without establishing connection
- **No flow/congestion control:** Fast but can lose packets
- **Low overhead:** Minimal protocol overhead
- **Use cases:** Video streaming, gaming, DNS, VoIP

**TCP 3-Way Handshake:**

```
Client                          Server
   |                              |
   |  1. SYN (seq=100)           |
   |----------------------------->|
   |                              |
   |     2. SYN-ACK (seq=300,    |
   |        ack=101)              |
   |<-----------------------------|
   |                              |
   |  3. ACK (ack=301)           |
   |----------------------------->|
   |                              |
   |  Connection established!     |
   |  Now can send data           |
```

**TCP Flow Control (Sliding Window):**

```
Sender                          Receiver
Window size = 4 packets

Send: [1][2][3][4]
      ↓  ↓  ↓  ↓
                  ←-------  ACK 1
                  ←-------  ACK 2
                  ←-------  ACK 3
Slide window →
Send: [5][6][7][8]
      ↓  ↓  ↓  ↓
                  ←-------  ACK 4
                  ←-------  ACK 5 (LOST!)

Timeout! Retransmit packet 5
      [5]
       ↓
                  ←-------  ACK 5 (success)
```

!!! note "Why TCP retransmission hurts real-time applications"
    When a TCP packet is lost, the sender stops advancing the window until that specific packet is acknowledged. This is called head-of-line blocking at the transport layer. For a video stream, waiting 50–200 ms to retransmit a frame that is already stale is worse than simply skipping it — which is exactly what UDP-based protocols do.

**Decision Matrix:**

| Requirement | Protocol | Why |
|-------------|----------|-----|
| Must guarantee delivery | TCP | Retransmission on loss |
| Need ordered packets | TCP | Sequence numbers |
| Low latency critical | UDP | No overhead |
| Can tolerate packet loss | UDP | No retransmission delay |
| Real-time streaming | UDP | Prefer fresh data |
| File transfer | TCP | Data integrity critical |
| Live video | UDP | Skip lost frames |
| Database connection | TCP | Reliability required |

**After learning, explain in your own words:**

<div class="learner-section" markdown>

- When would packet loss be acceptable? <span class="fill-in">[Your answer]</span>
- Why is TCP slower than UDP? <span class="fill-in">[Your answer]</span>
- What's the trade-off with reliability? <span class="fill-in">[Your answer]</span>

</div>

---

### Topic 2: HTTP Protocol Evolution

**Concept:** HTTP has evolved from simple request-response to sophisticated multiplexed, compressed connections.

**HTTP/1.0 → HTTP/1.1 → HTTP/2 → HTTP/3**

#### HTTP/1.1 (1997)

**Features:**

- Persistent connections (keep-alive)
- Pipelining (send multiple requests without waiting)
- Chunked transfer encoding
- Host header (virtual hosting)

**Problems:**

- Head-of-line blocking (HOL blocking)
- No multiplexing (sequential processing)
- Redundant headers (repeated with each request)
- Limited connections per domain (~6)

**Example of HOL Blocking:**

```
Browser needs: index.html, style.css, app.js, image.png

Connection 1:
  GET /index.html
  [WAIT 200ms]
  ← Response (large HTML file)

  GET /style.css
  [WAIT for index.html to finish!]
  ← Response

  GET /app.js
  [WAIT for style.css!]
  ← Response

Total time: Sequential, each waits for previous
```

#### HTTP/2 (2015)

**Major improvements:**

- **Binary protocol:** More efficient than text-based HTTP/1.1
- **Multiplexing:** Multiple requests/responses on single connection
- **Server push:** Proactively send resources
- **Header compression (HPACK):** Reduce overhead
- **Stream prioritization:** Critical resources first

**Multiplexing Example:**

```
Single TCP Connection
      ↓
[Stream 1: index.html ]
[Stream 2: style.css  ] ← All sent simultaneously
[Stream 3: app.js     ]
[Stream 4: image.png  ]
      ↓
Responses interleaved on same connection:
← [Stream 3 chunk] [Stream 1 chunk] [Stream 2 chunk] [Stream 4 chunk]

Total time: ~RTT + max(processing times)
Much faster than sequential!
```

**Header Compression (HPACK):**

```
HTTP/1.1 (repeated headers):
Request 1:
  GET /api/users
  Host: api.example.com
  User-Agent: Mozilla/5.0...
  Accept: application/json
  Cookie: session=abc123...
  (500 bytes total)

Request 2:
  GET /api/posts
  Host: api.example.com
  User-Agent: Mozilla/5.0...  ← Repeated!
  Accept: application/json     ← Repeated!
  Cookie: session=abc123...    ← Repeated!
  (500 bytes total)

HTTP/2 (with HPACK):
Request 1: 500 bytes (full headers)
Request 2: 50 bytes (only differences!)
Savings: 90% reduction
```

#### HTTP/3 (2020)

**Built on QUIC (UDP-based):**

- **No HOL blocking at transport layer:** HTTP/2 still suffered from TCP HOL blocking
- **Faster connection establishment:** 0-RTT handshake
- **Better mobile performance:** Connection migration (IP change resilience)
- **Improved congestion control:** Per-stream instead of per-connection

**HTTP/2 vs HTTP/3 (TCP vs QUIC):**

```
HTTP/2 (over TCP):
Packet 5 lost in stream 2
↓
All streams blocked until packet 5 retransmitted!
Streams 1, 3, 4 wait even though their packets arrived

HTTP/3 (over QUIC):
Packet 5 lost in stream 2
↓
Only stream 2 blocks, streams 1, 3, 4 continue
Independent stream recovery
```

**Performance Comparison:**

| Metric | HTTP/1.1 | HTTP/2 | HTTP/3 |
|--------|----------|--------|--------|
| Connections per domain | 6 | 1 | 1 |
| Multiplexing | No | Yes | Yes |
| Header compression | No | Yes (HPACK) | Yes (QPACK) |
| HOL blocking | Yes | Partial (TCP) | No |
| Connection setup | 1-2 RTT | 1-2 RTT | 0-1 RTT |
| Mobile resilience | Poor | Poor | Excellent |

---

### Topic 3: WebSockets & Real-Time Communication

**Concept:** Persistent, bidirectional communication channel for real-time updates.

**HTTP vs WebSockets:**

**HTTP (Request-Response):**
```
Client                    Server
   |                        |
   | HTTP GET              |
   |----------------------->|
   |                        |
   |         Response       |
   |<-----------------------|
   |                        |
   | (Connection closed)    |
```

**WebSocket (Persistent Connection):**
```
Client                    Server
   |                        |
   | HTTP Upgrade Request  |
   |----------------------->|
   |                        |
   |    101 Switching       |
   |<-----------------------|
   |                        |
   | ←------ Bidirectional --→ |
   | Message                |
   |----------------------->|
   |         Message        |
   |<-----------------------|
   | (Connection stays open) |
```

**WebSocket Handshake:**

```http
Client → Server:
GET /chat HTTP/1.1
Host: example.com
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==
Sec-WebSocket-Version: 13

Server → Client:
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=

[Connection now upgraded to WebSocket]
```

**Use Cases:**

| Scenario | HTTP Polling | Server-Sent Events | WebSockets |
|----------|-------------|-------------------|------------|
| Real-time chat | ✗ (wasteful) | ✗ (unidirectional) | ✓ (perfect) |
| Stock ticker | △ (acceptable) | ✓ (efficient) | ✓ (best) |
| Notifications | △ (wastes bandwidth) | ✓ (ideal) | △ (overkill) |
| Multiplayer game | ✗ (too slow) | ✗ (one-way) | ✓ (required) |
| Social feed | ✓ (simple) | ✓ (efficient) | △ (complex) |

**Polling vs WebSockets (Message Frequency):**

```
Polling (every 5 seconds):
┌─5s─┐─5s─┐─5s─┐─5s─┐
Req  Req  Req  Req  ...
  ↓    ↓    ↓    ↓
 Res  Res  Res  Res
 (empty)(data)(empty)(data)

Overhead: 4 requests for 2 updates
Latency: Up to 5s delay

WebSockets (event-driven):
Connection established
  ↓
[Open connection]
  ↓    ↓
 Data Data (instant push)

Overhead: 0 extra requests
Latency: ~RTT (milliseconds)
```

---

### Topic 4: DNS (Domain Name System)

**Concept:** Hierarchical, distributed system that translates domain names to IP addresses.

**DNS Hierarchy:**

```
                   . (root)
                   |
         ┌─────────┼─────────┐
         |         |         |
       .com      .org      .net
         |
         |
    example.com
         |
    ┌────┴────┐
   www      api
```

**DNS Resolution Process:**

```
User types: www.example.com

1. Browser cache check
   └─ Miss → Continue

2. OS cache check
   └─ Miss → Continue

3. Recursive resolver (ISP)
   └─ Queries root server

4. Root server
   └─ Returns .com nameserver address

5. .com nameserver
   └─ Returns example.com nameserver

6. example.com nameserver
   └─ Returns IP: 93.184.216.34

7. Resolver caches result (TTL: 3600s)

8. Returns IP to browser

9. Browser connects to 93.184.216.34
```

**DNS Record Types:**

| Record Type | Purpose | Example |
|------------|---------|---------|
| A | IPv4 address | example.com → 93.184.216.34 |
| AAAA | IPv6 address | example.com → 2606:2800:220:1:... |
| CNAME | Alias to another domain | www.example.com → example.com |
| MX | Mail server | example.com → mail.example.com |
| TXT | Arbitrary text | SPF, DKIM verification |
| NS | Name server | example.com → ns1.dns.com |

**DNS Caching & TTL:**

```
TTL (Time To Live) = 3600 seconds (1 hour)

Query at 12:00 PM:
  example.com → 1.2.3.4 (TTL: 3600)
  Cached until 1:00 PM

Query at 12:30 PM:
  ← Returns from cache (no network request)

Query at 1:01 PM:
  Cache expired → New DNS query
  example.com → 1.2.3.5 (IP changed!)
```

!!! tip "TTL is a trade-off between freshness and latency"
    A short TTL (e.g., 60 seconds) means DNS changes propagate quickly — useful during failovers — but every DNS query hits the resolver more often, adding latency. A long TTL (e.g., 86400 seconds) reduces resolver load but means clients may be pointing at a stale IP for up to a day after a change. Most production systems use 300–3600 seconds.

**DNS Load Balancing:**

```
Round-robin DNS:
example.com can return multiple A records:

Query 1: 1.2.3.4
Query 2: 1.2.3.5
Query 3: 1.2.3.6
Query 4: 1.2.3.4 (cycles back)

Poor man's load balancing:

+ Simple, no extra infrastructure
- No health checks
- Client caching interferes
- Not smart about server load
```

---

### Topic 5: TLS/SSL (Transport Layer Security)

**Concept:** Cryptographic protocol for secure communication over networks.

**TLS Handshake (TLS 1.3 - Simplified):**

```
Client                           Server
   |                               |
   | 1. ClientHello               |
   |   - Supported cipher suites  |
   |   - Random nonce             |
   |   - Key share                |
   |----------------------------->|
   |                               |
   |        2. ServerHello        |
   |   - Chosen cipher            |
   |   - Key share                |
   |   - Certificate              |
   |<-----------------------------|
   |                               |
   | 3. [Derive shared secret]    |
   | 4. Finished (encrypted)      |
   |----------------------------->|
   |                               |
   |        5. Finished           |
   |   (encrypted)                |
   |<-----------------------------|
   |                               |
   | ←------ Encrypted data ----→ |

Total: 1-RTT handshake (faster than TLS 1.2's 2-RTT)
```

**Certificate Chain Validation:**

```
Browser trusts: Root CA (in browser)
                    |
                Intermediate CA
                    |
              example.com cert

Server sends:
  1. example.com certificate
  2. Intermediate CA certificate

Browser validates:
  1. example.com cert signed by Intermediate CA? ✓
  2. Intermediate CA cert signed by Root CA? ✓
  3. Root CA in trusted store? ✓
  4. Certificate not expired? ✓
  5. Domain matches? ✓

All checks pass → Connection trusted
```

**Performance Impact:**

```
HTTPS (TLS) overhead:

- Initial handshake: +1 RTT (~50-100ms)
- Encryption/decryption CPU: ~5% server CPU
- Certificate chain: +2-4 KB per connection

Mitigation strategies:

- Session resumption (reuse session keys)
- TLS 1.3 0-RTT (resume with 0 round trips)
- OCSP stapling (reduce cert validation RTT)
- Connection pooling (amortize handshake cost)
```

!!! info "TCP and HTTP as load balancing layers"
    TCP (Layer 4) and HTTP (Layer 7) are also the two layers at which load balancers operate. A Layer 4 load balancer routes based on IP address and port without reading the payload; a Layer 7 load balancer reads HTTP headers and URLs and can make content-aware routing decisions. The choice between them and when each is appropriate is covered in [09. Load Balancing](09-load-balancing.md).

---

## Before/After: Why Networking Fundamentals Matter

**Your task:** Compare naive networking assumptions vs proper understanding to see the impact.

### Example: HTTP Connection Management

**Problem:** Mobile app making 100 API requests to load user dashboard

#### Approach 1: Naive HTTP/1.1 (Sequential Requests)

```
Client needs to load dashboard with:

- User profile (1 request)
- 20 recent posts (20 requests)
- 50 friend suggestions (50 requests)
- 10 notifications (10 requests)
- Analytics data (19 requests)

Total: 100 requests

HTTP/1.1 behavior:

- Opens connection
- Request 1 → Response 1
- Request 2 → Response 2
- ...
- Request 100 → Response 100
- Closes connection

Time analysis:

- Each request: ~50ms (network RTT) + processing
- Sequential: 100 * 50ms = 5,000ms = 5 seconds!
- User sees: Loading spinner for 5 seconds
```

**Problems:**

- Head-of-line blocking (each request waits for previous)
- Connection overhead repeated
- Poor mobile experience
- Inefficient bandwidth usage

#### Approach 2: HTTP/2 Multiplexing

```
HTTP/2 behavior:

- Opens single connection
- Sends all 100 requests simultaneously (multiplexed)
- Server streams responses back as ready
- Uses single TCP connection efficiently

Time analysis:

- All requests sent: ~50ms (single RTT)
- Server processing: ~200ms (parallel)
- Total: ~250ms vs 5,000ms
- 20x faster!

Additional benefits:

- Header compression (HPACK)
- Server push (preload resources)
- Stream prioritization
```

**Real-world impact:**

- HTTP/1.1: 5 second load time → user abandonment
- HTTP/2: 250ms load time → seamless experience
- Mobile data savings: ~40% from header compression

**Your calculation:** For 50 concurrent requests:

- HTTP/1.1 time: <span class="fill-in">_____</span> ms
- HTTP/2 time: <span class="fill-in">_____</span> ms
- Speedup factor: <span class="fill-in">_____</span>x

!!! info "Loop back"
    Return to the Quick Quiz now and fill in your verified answers.

---

## Case Studies: Networking in the Wild

### Online Gaming (Fortnite, Call of Duty): UDP for Speed

- **Pattern:** UDP for real-time game data.
- **How it works:** Player movements, actions, and shots are sent via UDP packets. If a packet is lost (e.g., showing a
  player's position from 50ms ago), the game doesn't wait. It simply discards the old data and uses the next available
  packet. Waiting for a TCP retransmission would cause noticeable lag (rubber-banding).
- **Key Takeaway:** For applications where the most recent data is more important than guaranteed delivery of every
  single piece of data, UDP is the superior choice. The trade-off is that the application layer must handle potential
  packet loss.

### Google & YouTube: HTTP/2 and HTTP/3 (QUIC) Adoption

- **Pattern:** Modern HTTP protocols for web performance.
- **How it works:** Google was a pioneer of both SPDY (the precursor to HTTP/2) and QUIC (the transport protocol for
  HTTP/3). On sites like YouTube, QUIC significantly reduces connection and stream setup time. This is especially
  noticeable on mobile networks, where it can seamlessly migrate a user's connection from Wi-Fi to cellular data without
  interrupting the video stream, a major weakness of TCP.
- **Key Takeaway:** Adopting modern protocols like HTTP/2 and HTTP/3 is critical for performance at scale. The move from
  TCP to a UDP-based protocol (QUIC) in HTTP/3 solves fundamental transport-layer problems like Head-of-Line blocking.

### Slack & Discord: WebSockets for Real-Time Chat

- **Pattern:** WebSockets for persistent, bidirectional communication.
- **How it works:** When you open Slack or Discord, your client establishes a single, long-lived WebSocket connection to
  their servers. When a new message is sent in a channel, the server pushes that message immediately to all connected
  clients in that channel.
- **Key Takeaway:** Compared to old-school HTTP polling, WebSockets reduce latency from seconds to milliseconds and
  drastically decrease unnecessary network traffic and server load, making them essential for any real-time interactive
  application.

### Netflix: DNS for Global Load Balancing

- **Pattern:** DNS-based Global Server Load Balancing (GSLB).
- **How it works:** When you press play on Netflix, your device makes a DNS request for the server hosting the video
  content. Netflix's DNS servers don't just return a single IP address; they return the IP address of the Open Connect
  Appliance (OCA) cache server that is geographically and topologically closest to you.
- **Key Takeaway:** DNS is not just for finding IPs. It's a powerful tool for global traffic routing. By directing users
  to the nearest server at the DNS level, Netflix ensures low latency, high-quality streaming and distributes load
  across its global content delivery network.

---

## Common Misconceptions

!!! warning "HTTPS is significantly slower than HTTP"
    TLS 1.3 adds only one additional round trip on the initial connection, and session resumption can reduce that to zero extra round trips (0-RTT). With connection pooling and keep-alive, the TLS handshake cost is amortised across hundreds of requests. The CPU overhead for modern AES-GCM encryption is under 5% on current hardware. The performance gap between HTTP and HTTPS is negligible in practice.

!!! warning "HTTP/2 eliminates all head-of-line blocking"
    HTTP/2 eliminates *application-layer* HOL blocking (multiple streams on one connection). However, because it runs over a single TCP connection, a lost TCP packet still blocks *all* HTTP/2 streams until the packet is retransmitted — this is *transport-layer* HOL blocking. HTTP/3 (QUIC) solves this by running streams independently over UDP.

!!! warning "DNS round-robin is sufficient for load balancing"
    DNS round-robin cycles IP addresses but has no health checks — it will happily return the IP of a crashed server. It also cannot respond to actual server load; an overloaded server receives the same share as an idle one. And client-side DNS caching means the "rotation" is unpredictable in practice. DNS round-robin is a last resort, not a real load balancing strategy.

---

## Decision Framework

<div class="learner-section" markdown>

**Your task:** Build decision trees for when to use each networking approach.

### Question 1: Which Protocol?

**Use TCP when:**

- Reliability is critical: <span class="fill-in">[Financial transactions, file transfers]</span>
- Ordering matters: <span class="fill-in">[Database replication, messaging]</span>
- Data integrity required: <span class="fill-in">[API calls, downloads]</span>

**Use UDP when:**

- Low latency critical: <span class="fill-in">[Gaming, VoIP]</span>
- Packet loss acceptable: <span class="fill-in">[Video streaming, DNS]</span>
- Real-time more important than reliability: <span class="fill-in">[Live broadcasts]</span>

### Question 2: HTTP Version?

**Use HTTP/1.1 when:**

- Legacy client support required
- Simple deployment (no special infrastructure)
- Low concurrency use case

**Use HTTP/2 when:**

- Modern browsers/clients
- High concurrency (many resources)
- API with many endpoints

**Use HTTP/3 when:**

- Mobile-first application
- Global user base (varying network quality)
- WebRTC or real-time features

### Question 3: Real-Time Communication?

**Use HTTP polling when:**

- Infrequent updates (> 30 seconds)
- Simple implementation required
- Client controls update frequency

**Use Server-Sent Events (SSE) when:**

- Server → Client updates only
- Text-based data (JSON)
- Browser compatibility important

**Use WebSockets when:**

- Bidirectional communication required
- Low latency critical (< 100ms)
- High message frequency (> 1/sec)

</div>

---

## Practice Scenarios

<div class="learner-section" markdown>

### Scenario 1: E-Commerce Platform

**Requirements:**

- Product catalog browsing
- Real-time inventory updates
- Checkout process
- 10K concurrent users
- Global user base

**Your design:**

Protocol choices:

- Catalog API: <span class="fill-in">[HTTP/2 or HTTP/3? Why?]</span>
- Inventory updates: <span class="fill-in">[WebSocket/SSE/Polling?]</span>
- Checkout: <span class="fill-in">[HTTPS with what considerations?]</span>

DNS strategy:

- TTL: <span class="fill-in">[How long?]</span>
- Multi-region: <span class="fill-in">[GeoDNS?]</span>

**Failure modes:**

- What happens if the WebSocket server handling real-time inventory updates crashes while 10K users are connected during a flash sale? <span class="fill-in">[Fill in]</span>
- How does your design behave when GeoDNS misconfiguration routes all global users to a single region, overloading that data centre? <span class="fill-in">[Fill in]</span>

### Scenario 2: Multiplayer Game

**Requirements:**

- 60 tick rate (updates per second)
- < 50ms latency requirement
- 100 players per game
- Unreliable networks (mobile)

**Your design:**

Protocol: <span class="fill-in">[TCP or UDP? Why?]</span>

Packet loss handling:

- Strategy: <span class="fill-in">[How to handle?]</span>
- Acceptable loss rate: <span class="fill-in">[X%?]</span>

Latency optimization:

- Connection pooling: <span class="fill-in">[Helpful?]</span>
- Regional servers: <span class="fill-in">[Required?]</span>

**Failure modes:**

- What happens if a regional game server becomes unavailable mid-match for 20 of the 100 players due to a network partition? <span class="fill-in">[Fill in]</span>
- How does your design behave when mobile network jitter pushes packet loss above your acceptable rate and the UDP-based game state diverges between clients? <span class="fill-in">[Fill in]</span>

### Scenario 3: Video Conferencing

**Requirements:**

- Real-time audio/video
- Screen sharing
- Chat messaging
- Recording capability

**Your design:**

Media protocols:

- Audio/Video: <span class="fill-in">[UDP/TCP/WebRTC?]</span>
- Chat: <span class="fill-in">[WebSocket/HTTP?]</span>
- Recording: <span class="fill-in">[How to implement?]</span>

Quality vs Latency:

- Packet loss: <span class="fill-in">[How to handle?]</span>
- Bandwidth adaptation: <span class="fill-in">[Strategy?]</span>

**Failure modes:**

- What happens if the TURN relay server (used for NAT traversal) becomes unavailable for participants behind strict firewalls? <span class="fill-in">[Fill in]</span>
- How does your design behave when a participant's available bandwidth drops sharply mid-call and the system cannot adapt quickly enough to prevent audio/video freeze? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these without referring to your notes or implementation.

1. A TCP packet is lost mid-stream on an HTTP/2 connection carrying four multiplexed streams. Describe precisely what happens to each of the four streams and explain why HTTP/3 behaves differently.

    ??? success "Rubric"
        A complete answer addresses: (1) when TCP detects the lost packet, it halts delivery of all data in the receive buffer to the application — all four HTTP/2 streams stall even if their data arrived intact, because TCP guarantees in-order delivery, (2) this is transport-layer head-of-line blocking, distinct from the application-layer HOL blocking that HTTP/2 already solved, and (3) HTTP/3 over QUIC handles each stream independently at the transport layer so a lost packet for stream 2 only blocks stream 2; streams 1, 3, and 4 continue uninterrupted.

2. You need to reduce the time it takes to fail over a service to a new IP address from 1 hour to under 2 minutes. What change do you make and when must you make it relative to the failover event?

    ??? success "Rubric"
        A complete answer addresses: (1) the 1-hour delay is caused by a DNS TTL of 3600 seconds — cached resolvers and clients will not query for a new IP until the TTL expires, (2) the fix is to lower the TTL (e.g., to 60 seconds) *before* the planned failover — ideally hours or days in advance, so old caches expire the long TTL before it matters, and (3) lowering TTL after the incident has already started does not help because caches are already holding the old value for up to the original TTL duration.

3. A chat application currently polls the server every 2 seconds. At 50,000 concurrent users, calculate the approximate number of HTTP requests per minute this generates, then explain what switching to WebSockets changes.

    ??? success "Rubric"
        A complete answer addresses: (1) 50,000 users × 30 polls/minute = 1,500,000 HTTP requests per minute, regardless of whether there are any new messages, (2) switching to WebSockets replaces this constant polling load with a persistent connection per user — the server only pushes data when a message actually exists, reducing request volume to approximately the actual message rate, and (3) WebSockets also eliminate the per-request HTTP overhead (headers, connection setup) and reduce server CPU and bandwidth proportionally.

4. A colleague says "We use TLS everywhere so our API is secure." What important security concern does TLS *not* address?

    ??? success "Rubric"
        A complete answer addresses: (1) TLS protects data *in transit* — it prevents eavesdropping and tampering on the network — but does nothing about data at rest, (2) TLS does not provide authentication or authorisation of the caller: it proves the server's identity via its certificate but does not verify that the authenticated server-side principal is *authorised* to perform the requested operation, and (3) TLS does not protect against application-layer vulnerabilities such as SQL injection, broken access control, or a compromised client sending valid but malicious requests.

---

## Review Checklist

<div class="learner-section" markdown>

Complete this checklist after implementing and studying all networking topics.

- [ ] Can explain TCP vs UDP trade-offs and cite concrete use cases for each
- [ ] Can describe HTTP/1.1 → HTTP/2 → HTTP/3 improvements and when each is preferred
- [ ] Can explain how WebSockets differ from HTTP polling and describe the upgrade handshake
- [ ] Can trace a DNS resolution from browser to IP address, including recursive vs iterative queries
- [ ] Can describe the TLS handshake steps and explain the performance cost

</div>

---

## Connected Topics

!!! info "Where this topic connects"

    - **06. API Design** — HTTP/1.1, HTTP/2, and HTTP/3 directly shape API design choices for latency, multiplexing, and connection management → [06. API Design](06-api-design.md)
    - **07. Security Patterns** — TLS (covered here) is the transport layer that makes token-based authentication safe; without it, JWTs and API keys are exposed in transit → [07. Security Patterns](07-security-patterns.md)
    - **09. Load Balancing** — TCP (Layer 4) and HTTP (Layer 7) are the two layers at which load balancers operate; the protocol coverage here provides context for that routing decision → [09. Load Balancing](09-load-balancing.md)
