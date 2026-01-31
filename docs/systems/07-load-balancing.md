# 06. Load Balancing

> Distributing requests across multiple servers for scalability and reliability

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing different load balancing algorithms, explain them simply.

**Prompts to guide you:**

1. **What is load balancing in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do we need load balancers?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for round robin:**
   - Example: "Round robin is like a carousel where..."
   - Your analogy: _[Fill in]_

4. **What is round robin in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **How is least connections different from round robin?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for consistent hashing:**
   - Example: "Consistent hashing is like a clock where..."
   - Your analogy: _[Fill in]_

7. **What is consistent hashing in one sentence?**
   - Your answer: _[Fill in after implementation]_

8. **When would you use weighted load balancing?**
   - Your answer: _[Fill in after implementation]_

---

## Core Implementation

### Part 1: Round Robin Load Balancer

**Your task:** Implement simple round robin algorithm.

```java
import java.util.*;

/**
 * Round Robin: Distribute requests evenly in rotation
 *
 * Key principles:
 * - Circular iteration through servers
 * - Simple and fair distribution
 * - Doesn't consider server load
 * - Works well for similar servers
 */

public class RoundRobinLoadBalancer {

    private final List<Server> servers;
    private int currentIndex;

    /**
     * Initialize round robin load balancer
     *
     * @param servers List of backend servers
     *
     * TODO: Initialize balancer
     * - Store server list
     * - Start index at 0
     */
    public RoundRobinLoadBalancer(List<Server> servers) {
        // TODO: Store servers (defensive copy)

        // TODO: Initialize currentIndex to 0

        this.servers = null; // Replace
        this.currentIndex = 0;
    }

    /**
     * Select next server using round robin
     *
     * @return Next server in rotation
     *
     * TODO: Implement round robin selection
     * 1. Get server at current index
     * 2. Increment index (with wraparound)
     * 3. Return server
     *
     * Hint: Use modulo for wraparound
     */
    public synchronized Server getNextServer() {
        // TODO: Check if servers list is empty

        // TODO: Get server at currentIndex

        // TODO: Increment currentIndex with wraparound
        // currentIndex = (currentIndex + 1) % servers.size()

        // TODO: Return selected server

        return null; // Replace
    }

    /**
     * Add server to pool
     */
    public synchronized void addServer(Server server) {
        // TODO: Add server to list
    }

    /**
     * Remove server from pool
     */
    public synchronized void removeServer(Server server) {
        // TODO: Remove server from list
        // TODO: Adjust currentIndex if needed
    }

    static class Server {
        String id;
        String host;
        int port;

        public Server(String id, String host, int port) {
            this.id = id;
            this.host = host;
            this.port = port;
        }

        @Override
        public String toString() {
            return id + " (" + host + ":" + port + ")";
        }
    }
}
```

### Part 2: Least Connections Load Balancer

**Your task:** Implement least connections algorithm.

```java
/**
 * Least Connections: Route to server with fewest active connections
 *
 * Key principles:
 * - Track active connections per server
 * - Select server with minimum load
 * - Better for varying request durations
 * - More overhead than round robin
 */

public class LeastConnectionsLoadBalancer {

    private final List<ServerWithStats> servers;

    /**
     * Initialize least connections load balancer
     *
     * @param servers List of backend servers
     *
     * TODO: Initialize balancer
     * - Create ServerWithStats for each server
     * - Initialize connection counters to 0
     */
    public LeastConnectionsLoadBalancer(List<RoundRobinLoadBalancer.Server> servers) {
        // TODO: Wrap each server with connection counter

        this.servers = null; // Replace
    }

    /**
     * Select server with fewest connections
     *
     * @return Server with minimum active connections
     *
     * TODO: Implement least connections selection
     * 1. Find server with minimum connections
     * 2. Increment its connection count
     * 3. Return server
     *
     * Hint: Break ties by choosing first found
     */
    public synchronized RoundRobinLoadBalancer.Server getNextServer() {
        // TODO: Check if servers list is empty

        // TODO: Find server with minimum connections
        // Iterate through servers, track min

        // TODO: Increment connection count for selected server

        // TODO: Return selected server

        return null; // Replace
    }

    /**
     * Release connection when request completes
     *
     * @param server Server to release connection from
     *
     * TODO: Decrement connection count
     * - Find server in list
     * - Decrement activeConnections
     * - Ensure doesn't go below 0
     */
    public synchronized void releaseConnection(RoundRobinLoadBalancer.Server server) {
        // TODO: Find ServerWithStats for given server

        // TODO: Decrement activeConnections (min 0)
    }

    /**
     * Get server statistics
     */
    public synchronized Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        for (ServerWithStats s : servers) {
            stats.put(s.server.id, s.activeConnections);
        }
        return stats;
    }

    static class ServerWithStats {
        RoundRobinLoadBalancer.Server server;
        int activeConnections;

        public ServerWithStats(RoundRobinLoadBalancer.Server server) {
            this.server = server;
            this.activeConnections = 0;
        }
    }
}
```

### Part 3: Weighted Round Robin

**Your task:** Implement weighted round robin for heterogeneous servers.

```java
/**
 * Weighted Round Robin: Distribute based on server capacity
 *
 * Key principles:
 * - Servers have different weights (capacity)
 * - Higher weight = more requests
 * - Smooth distribution using GCD algorithm
 * - Good for heterogeneous servers
 */

public class WeightedRoundRobinLoadBalancer {

    private final List<WeightedServer> servers;
    private int currentIndex;
    private int currentWeight;
    private int maxWeight;
    private int gcd; // Greatest common divisor of weights

    /**
     * Initialize weighted round robin
     *
     * @param servers List of servers with weights
     *
     * TODO: Initialize balancer
     * - Store servers
     * - Calculate max weight and GCD
     * - Initialize currentWeight to max
     */
    public WeightedRoundRobinLoadBalancer(List<WeightedServer> servers) {
        // TODO: Store servers

        // TODO: Calculate maxWeight (max of all weights)

        // TODO: Calculate GCD of all weights

        // TODO: Initialize currentIndex to -1

        // TODO: Initialize currentWeight to 0

        this.servers = null; // Replace
    }

    /**
     * Select next server using weighted round robin
     *
     * @return Next server based on weight
     *
     * TODO: Implement weighted selection
     * 1. Loop until finding server with weight >= currentWeight
     * 2. When loop completes, decrease currentWeight by GCD
     * 3. Return selected server
     *
     * Hint: This ensures smooth distribution
     */
    public synchronized RoundRobinLoadBalancer.Server getNextServer() {
        // TODO: Loop through servers
        while (true) {
            // TODO: Move to next index (wraparound)
            // currentIndex = (currentIndex + 1) % servers.size()

            // TODO: If back to start, decrease currentWeight
            // if (currentIndex == 0):
            //   currentWeight = currentWeight - gcd
            //   if (currentWeight <= 0):
            //     currentWeight = maxWeight

            // TODO: If current server weight >= currentWeight, return it

        }
    }

    /**
     * Calculate GCD of all weights
     */
    private int calculateGCD() {
        // TODO: Calculate GCD of all server weights
        // Hint: Use Euclidean algorithm
        return 1; // Replace
    }

    /**
     * Calculate GCD of two numbers
     */
    private int gcd(int a, int b) {
        // TODO: Implement Euclidean algorithm
        // while b != 0:
        //   temp = b
        //   b = a % b
        //   a = temp
        return 0; // Replace
    }

    static class WeightedServer {
        RoundRobinLoadBalancer.Server server;
        int weight; // Capacity/power of server

        public WeightedServer(RoundRobinLoadBalancer.Server server, int weight) {
            this.server = server;
            this.weight = weight;
        }
    }
}
```

### Part 4: Consistent Hashing

**Your task:** Implement consistent hashing for distributed caching.

```java
/**
 * Consistent Hashing: Map requests to servers using hash ring
 *
 * Key principles:
 * - Servers placed on virtual ring
 * - Request hashed to position on ring
 * - Clockwise walk to find server
 * - Minimal redistribution when servers change
 */

public class ConsistentHashingLoadBalancer {

    private final TreeMap<Integer, RoundRobinLoadBalancer.Server> ring;
    private final int virtualNodesPerServer;

    /**
     * Initialize consistent hashing
     *
     * @param servers List of servers
     * @param virtualNodesPerServer Number of virtual nodes per physical server
     *
     * TODO: Initialize hash ring
     * - Create TreeMap for ring
     * - Add each server with virtual nodes
     */
    public ConsistentHashingLoadBalancer(List<RoundRobinLoadBalancer.Server> servers, int virtualNodesPerServer) {
        // TODO: Initialize TreeMap

        // TODO: Store virtualNodesPerServer

        // TODO: Add all servers to ring

        this.ring = null; // Replace
        this.virtualNodesPerServer = 0;
    }

    /**
     * Get server for a given key
     *
     * @param key Request key (e.g., user ID, session ID)
     * @return Server to handle this key
     *
     * TODO: Implement consistent hashing lookup
     * 1. Hash the key to integer
     * 2. Find next server clockwise on ring
     * 3. If no server found, wrap to first server
     */
    public RoundRobinLoadBalancer.Server getServer(String key) {
        // TODO: Check if ring is empty

        // TODO: Hash key to integer position

        // TODO: Find next entry on ring (ceilingEntry)
        // If null, wrap to first entry (firstEntry)

        // TODO: Return server

        return null; // Replace
    }

    /**
     * Add server to hash ring
     *
     * TODO: Add server with virtual nodes
     * - For each virtual node:
     *   - Hash "serverId-virtualNodeIndex"
     *   - Place on ring
     */
    public void addServer(RoundRobinLoadBalancer.Server server) {
        // TODO: Add virtualNodesPerServer copies of this server
        // for i in 0..virtualNodesPerServer:
        //   key = server.id + "-" + i
        //   hash = hash(key)
        //   ring.put(hash, server)
    }

    /**
     * Remove server from hash ring
     *
     * TODO: Remove all virtual nodes for this server
     */
    public void removeServer(RoundRobinLoadBalancer.Server server) {
        // TODO: Remove all virtual nodes
        // for i in 0..virtualNodesPerServer:
        //   key = server.id + "-" + i
        //   hash = hash(key)
        //   ring.remove(hash)
    }

    /**
     * Hash function
     *
     * TODO: Implement simple hash function
     * - Use string hashCode()
     * - Ensure positive value
     */
    private int hash(String key) {
        // TODO: Hash key to integer
        // Hint: key.hashCode() & 0x7FFFFFFF (remove sign bit)
        return 0; // Replace
    }

    /**
     * Get ring statistics
     */
    public int getRingSize() {
        return ring.size();
    }
}
```

### Part 5: IP Hash Load Balancer

**Your task:** Implement IP hash for session persistence.

```java
/**
 * IP Hash: Route client to same server based on IP
 *
 * Key principles:
 * - Hash client IP to select server
 * - Ensures session persistence
 * - Client always hits same server
 * - Issues when server pool changes
 */

public class IPHashLoadBalancer {

    private final List<RoundRobinLoadBalancer.Server> servers;

    /**
     * Initialize IP hash load balancer
     *
     * @param servers List of backend servers
     */
    public IPHashLoadBalancer(List<RoundRobinLoadBalancer.Server> servers) {
        // TODO: Store servers
        this.servers = null; // Replace
    }

    /**
     * Select server based on client IP
     *
     * @param clientIP Client IP address
     * @return Server for this client
     *
     * TODO: Implement IP hash selection
     * 1. Hash the client IP
     * 2. Modulo by server count
     * 3. Return server at that index
     */
    public synchronized RoundRobinLoadBalancer.Server getServer(String clientIP) {
        // TODO: Check if servers list is empty

        // TODO: Hash clientIP to integer

        // TODO: Get index using modulo
        // index = abs(hash) % servers.size()

        // TODO: Return server at index

        return null; // Replace
    }

    /**
     * Hash IP address
     */
    private int hash(String ip) {
        // TODO: Hash IP string
        // Hint: ip.hashCode()
        return 0; // Replace
    }

    /**
     * Add server (warning: disrupts session persistence)
     */
    public synchronized void addServer(RoundRobinLoadBalancer.Server server) {
        // TODO: Add server
        // Note: This will change hash distribution
    }

    /**
     * Remove server (warning: disrupts session persistence)
     */
    public synchronized void removeServer(RoundRobinLoadBalancer.Server server) {
        // TODO: Remove server
        // Note: This will change hash distribution
    }
}
```

---

## Client Code

```java
import java.util.*;

public class LoadBalancingClient {

    public static void main(String[] args) {
        testRoundRobin();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testLeastConnections();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testWeightedRoundRobin();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testConsistentHashing();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testIPHash();
    }

    static void testRoundRobin() {
        System.out.println("=== Round Robin Test ===\n");

        // Create servers
        List<RoundRobinLoadBalancer.Server> servers = Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );

        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer(servers);

        // Test: Send 10 requests
        System.out.println("Sending 10 requests:");
        for (int i = 1; i <= 10; i++) {
            RoundRobinLoadBalancer.Server server = lb.getNextServer();
            System.out.println("Request " + i + " -> " + server);
        }
    }

    static void testLeastConnections() {
        System.out.println("=== Least Connections Test ===\n");

        List<RoundRobinLoadBalancer.Server> servers = Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );

        LeastConnectionsLoadBalancer lb = new LeastConnectionsLoadBalancer(servers);

        // Test: Send requests and simulate completion
        System.out.println("Request 1:");
        RoundRobinLoadBalancer.Server s1 = lb.getNextServer();
        System.out.println("Routed to: " + s1);
        System.out.println("Stats: " + lb.getStats());

        System.out.println("\nRequest 2:");
        RoundRobinLoadBalancer.Server s2 = lb.getNextServer();
        System.out.println("Routed to: " + s2);
        System.out.println("Stats: " + lb.getStats());

        System.out.println("\nRequest 1 completes:");
        lb.releaseConnection(s1);
        System.out.println("Stats: " + lb.getStats());

        System.out.println("\nRequest 3:");
        RoundRobinLoadBalancer.Server s3 = lb.getNextServer();
        System.out.println("Routed to: " + s3);
        System.out.println("Stats: " + lb.getStats());
    }

    static void testWeightedRoundRobin() {
        System.out.println("=== Weighted Round Robin Test ===\n");

        // Create servers with different capacities
        List<WeightedRoundRobinLoadBalancer.WeightedServer> servers = Arrays.asList(
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S1-Small", "10.0.0.1", 8080), 1),
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S2-Medium", "10.0.0.2", 8080), 2),
            new WeightedRoundRobinLoadBalancer.WeightedServer(
                new RoundRobinLoadBalancer.Server("S3-Large", "10.0.0.3", 8080), 3)
        );

        WeightedRoundRobinLoadBalancer lb = new WeightedRoundRobinLoadBalancer(servers);

        // Test: Send 12 requests (should distribute 2:4:6)
        System.out.println("Sending 12 requests (expected: 2:4:6 distribution):");
        Map<String, Integer> distribution = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            RoundRobinLoadBalancer.Server server = lb.getNextServer();
            distribution.merge(server.id, 1, Integer::sum);
            System.out.println("Request " + i + " -> " + server.id);
        }
        System.out.println("\nDistribution: " + distribution);
    }

    static void testConsistentHashing() {
        System.out.println("=== Consistent Hashing Test ===\n");

        List<RoundRobinLoadBalancer.Server> servers = Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );

        ConsistentHashingLoadBalancer lb = new ConsistentHashingLoadBalancer(servers, 3);

        // Test: Same key always goes to same server
        String[] keys = {"user123", "user456", "user789", "user123", "user456"};
        System.out.println("Routing requests by user ID:");
        for (String key : keys) {
            RoundRobinLoadBalancer.Server server = lb.getServer(key);
            System.out.println(key + " -> " + server.id);
        }

        // Test: Add server and see minimal redistribution
        System.out.println("\nAdding new server S4:");
        lb.addServer(new RoundRobinLoadBalancer.Server("S4", "10.0.0.4", 8080));
        for (String key : keys) {
            RoundRobinLoadBalancer.Server server = lb.getServer(key);
            System.out.println(key + " -> " + server.id);
        }
    }

    static void testIPHash() {
        System.out.println("=== IP Hash Test ===\n");

        List<RoundRobinLoadBalancer.Server> servers = Arrays.asList(
            new RoundRobinLoadBalancer.Server("S1", "10.0.0.1", 8080),
            new RoundRobinLoadBalancer.Server("S2", "10.0.0.2", 8080),
            new RoundRobinLoadBalancer.Server("S3", "10.0.0.3", 8080)
        );

        IPHashLoadBalancer lb = new IPHashLoadBalancer(servers);

        // Test: Same IP always goes to same server
        String[] clientIPs = {"192.168.1.100", "192.168.1.101", "192.168.1.102",
                             "192.168.1.100", "192.168.1.101"};
        System.out.println("Routing by client IP (session persistence):");
        for (String ip : clientIPs) {
            RoundRobinLoadBalancer.Server server = lb.getServer(ip);
            System.out.println(ip + " -> " + server.id);
        }
    }
}
```

---

## Decision Framework

**Questions to answer after implementation:**

### 1. Algorithm Selection

**When to use Round Robin?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Least Connections?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Weighted Round Robin?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use Consistent Hashing?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use IP Hash?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

### 2. Trade-offs

**Round Robin:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Least Connections:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**Consistent Hashing:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**IP Hash:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

### 3. Your Decision Tree

Build your decision tree after practicing:

```
What is your priority?
├─ Simple and fair distribution → ?
├─ Consider server load → ?
├─ Heterogeneous servers → ?
├─ Session persistence → ?
└─ Minimal redistribution on changes → ?
```

### 4. Kill Switch - Don't use when:

**Round Robin:**
1. _[When does round robin fail? Fill in]_
2. _[Another failure case]_

**Least Connections:**
1. _[When does least connections fail? Fill in]_
2. _[Another failure case]_

**Consistent Hashing:**
1. _[When does consistent hashing fail? Fill in]_
2. _[Another failure case]_

### 5. Rule of Three - Alternatives

For each scenario, identify alternatives and compare:

**Scenario: Distribute requests across web servers**
1. Option A: _[Fill in]_
2. Option B: _[Fill in]_
3. Option C: _[Fill in]_

---

## Practice

### Scenario 1: Load balance web application

**Requirements:**
- 5 web servers of equal capacity
- Stateless application
- Want even distribution
- Simple to understand

**Your design:**
- Which algorithm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle server failures? _[Fill in]_
- Health check strategy? _[Fill in]_

### Scenario 2: Load balance with session state

**Requirements:**
- Users have shopping carts
- Cart stored in server memory
- 10 application servers
- Need session persistence

**Your design:**
- Which algorithm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle server additions? _[Fill in]_
- Alternative to sticky sessions? _[Fill in]_

### Scenario 3: Distributed cache cluster

**Requirements:**
- Cache cluster with 20 nodes
- Frequently add/remove nodes
- Want to minimize cache misses
- Keys should stay on same node

**Your design:**
- Which algorithm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How many virtual nodes? _[Fill in]_
- Replication strategy? _[Fill in]_

---

## Review Checklist

- [ ] Round robin implemented with circular iteration
- [ ] Least connections implemented with connection tracking
- [ ] Weighted round robin implemented with GCD algorithm
- [ ] Consistent hashing implemented with virtual nodes
- [ ] IP hash implemented for session persistence
- [ ] Understand when to use each algorithm
- [ ] Can explain trade-offs between algorithms
- [ ] Built decision tree for algorithm selection
- [ ] Completed practice scenarios

---

**Next:** [08. Concurrency Patterns →](08-concurrency-patterns.md)

**Back:** [06. Rate Limiting ←](06-rate-limiting.md)
