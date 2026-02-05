# Networking Fundamentals

> TCP/IP vs UDP, HTTP versions, WebSockets, DNS, TLS, and load balancing

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After learning networking fundamentals, explain them simply.

**Prompts to guide you:**

1. **What is the difference between TCP and UDP in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

2. **Why do we need DNS?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

3. **Real-world analogy for TCP vs UDP:**
    - Example: "TCP is like certified mail where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What does HTTP/2 multiplexing solve?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

5. **Real-world analogy for WebSockets:**
    - Example: "WebSockets are like a phone call where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

6. **Why do we need load balancers?**
    - Your answer: <span class="fill-in">[Fill in after practice]</span>

</div>

---

## Quick Quiz (Do BEFORE learning)

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
- **Load balancing:** <span class="fill-in">[L4 or L7?]</span>

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

---

### Topic 6: Load Balancing (L4 vs L7)

**Concept:** Distribute traffic across multiple servers for reliability and performance.

**Layer 4 (Transport Layer) Load Balancing:**

```
Client connects to: lb.example.com:443

L4 Load Balancer:
  ┌─ Looks at: IP address, Port, Protocol
  │  Does NOT look at: HTTP headers, cookies, URLs
  │
  └─ Forwards TCP connection to backend:
      Server 1: 10.0.1.5:443
      Server 2: 10.0.1.6:443
      Server 3: 10.0.1.7:443

Pros:

+ Fast (no packet inspection)
+ Works for any TCP/UDP protocol
+ Low latency (simple forwarding)

Cons:

- Can't route based on URL/headers
- No application-aware decisions
- Sticky sessions require IP hashing
```

**Layer 7 (Application Layer) Load Balancing:**

```
Client request: https://example.com/api/users

L7 Load Balancer:
  ┌─ Terminates TLS connection
  │  Reads full HTTP request
  │  Inspects: URL path, headers, cookies
  │
  └─ Routes based on rules:
      /api/*     → API servers (10.0.2.x)
      /static/*  → CDN servers (10.0.3.x)
      /admin/*   → Admin servers (10.0.4.x)

Pros:

+ Intelligent routing (URL, headers, etc.)
+ Session affinity (cookie-based)
+ Content-based caching
+ SSL termination

Cons:

- Slower (packet inspection overhead)
- More CPU intensive
- Application-specific (HTTP/gRPC/etc.)
```

**Load Balancing Algorithms:**

| Algorithm | How It Works | Use Case |
|-----------|-------------|----------|
| Round Robin | Cycle through servers 1→2→3→1 | Uniform workload |
| Least Connections | Send to server with fewest active connections | Varied request duration |
| IP Hash | Hash client IP to pick server | Session persistence |
| Least Response Time | Route to fastest server | Performance-critical |
| Weighted Round Robin | More traffic to powerful servers | Heterogeneous servers |

**Health Checks:**

```
Load balancer → Server health check

Active checks (periodic):
  Every 10s: GET /health
  If 3 consecutive failures → Mark unhealthy
  If 2 consecutive successes → Mark healthy

Passive checks (observed):
  Track response times, error rates
  If error rate > 5% → Reduce traffic
  If response time > 1s → Reduce traffic

Unhealthy server:
  ┌─ Stop sending new requests
  └─ Drain existing connections (graceful)
```

---

## Decision Framework

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

### Question 4: Load Balancer Layer?

**Use L4 load balancing when:**

- Protocol-agnostic (TCP/UDP)
- Maximum performance needed
- Simple traffic distribution

**Use L7 load balancing when:**

- Content-based routing required
- SSL termination beneficial
- Application-aware features needed

---

## Practice Scenarios

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

Load balancing:

- Layer: <span class="fill-in">[L4 or L7? Why?]</span>
- Algorithm: <span class="fill-in">[Which algorithm?]</span>
- Sticky sessions: <span class="fill-in">[Needed?]</span>

DNS strategy:

- TTL: <span class="fill-in">[How long?]</span>
- Multi-region: <span class="fill-in">[GeoDNS?]</span>

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

---

## Review Checklist

Before moving to the next topic:

-   [ ] **Understanding**
    -   [ ] Understand TCP 3-way handshake
    -   [ ] Know difference between TCP and UDP
    -   [ ] Understand HTTP/2 multiplexing
    -   [ ] Know how WebSockets work
    -   [ ] Understand DNS resolution process
    -   [ ] Know TLS handshake basics
    -   [ ] Understand L4 vs L7 load balancing

-   [ ] **Protocol Selection**
    -   [ ] Can choose between TCP and UDP
    -   [ ] Know when to use HTTP/2 vs HTTP/3
    -   [ ] Understand WebSocket use cases
    -   [ ] Can design DNS strategy

-   [ ] **Performance Optimization**
    -   [ ] Know connection pooling benefits
    -   [ ] Understand header compression
    -   [ ] Can calculate latency impact
    -   [ ] Know caching strategies

-   [ ] **Decision Making**
    -   [ ] Completed practice scenarios
    -   [ ] Can explain trade-offs
    -   [ ] Understand failure modes

---

### Mastery Certification

**I certify that I can:**

-   [ ] Explain TCP vs UDP trade-offs
-   [ ] Design protocol choice for new system
-   [ ] Understand HTTP evolution benefits
-   [ ] Choose appropriate real-time protocol
-   [ ] Configure DNS for performance
-   [ ] Explain TLS handshake process
-   [ ] Select load balancing strategy
-   [ ] Optimize network performance
-   [ ] Debug networking issues
-   [ ] Teach networking concepts to others

