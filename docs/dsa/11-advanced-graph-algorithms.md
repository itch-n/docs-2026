# 11. Advanced Graph Algorithms

> Dijkstra's shortest path, Minimum Spanning Tree, Topological Sort, and distributed graph processing

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After learning graph algorithms, explain them simply.

**Prompts to guide you:**

1. **What does Dijkstra's algorithm find?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

2. **Why do we need Minimum Spanning Trees?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

3. **Real-world analogy for Dijkstra's algorithm:**
    - Example: "Dijkstra's algorithm is like finding the cheapest flight where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What is topological sorting used for?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

5. **Real-world analogy for MST:**
    - Example: "An MST is like connecting cities with the least amount of cable where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

6. **Why can't we use Dijkstra's for negative weights?**
    - Your answer: <span class="fill-in">[Fill in after practice]</span>

</div>

---

## Quick Quiz (Do BEFORE learning)

<div class="learner-section" markdown>

**Your task:** Test your intuition about graph algorithms without looking at details.

### Complexity Predictions

1. **Dijkstra's algorithm for shortest path:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

2. **Minimum Spanning Tree (Kruskal's):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - What data structure: <span class="fill-in">[Your guess]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Topological Sort (DFS-based):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - When does it fail: <span class="fill-in">[Your guess]</span>
    - Verified: <span class="fill-in">[Actual]</span>

### Scenario Predictions

**Scenario 1:** GPS navigation finding fastest route

- **Algorithm:** <span class="fill-in">[Dijkstra? A*? Bellman-Ford?]</span>
- **Edge weight:** <span class="fill-in">[Distance? Time? Both?]</span>
- **Challenge:** <span class="fill-in">[Traffic changes? One-way streets?]</span>

**Scenario 2:** Package dependency resolution (npm, pip)

- **Algorithm:** <span class="fill-in">[Topological sort? DFS? BFS?]</span>
- **Failure case:** <span class="fill-in">[Circular dependency?]</span>
- **Output:** <span class="fill-in">[Install order?]</span>

**Scenario 3:** Network cable installation (connect all offices)

- **Algorithm:** <span class="fill-in">[MST? Shortest path?]</span>
- **Goal:** <span class="fill-in">[Minimize what?]</span>
- **Constraint:** <span class="fill-in">[All connected? No cycles?]</span>

</div>

---

## Before/After: Why Advanced Graph Algorithms Matter

**Your task:** Compare naive approaches vs optimized algorithms to understand the impact.

### Example: Network Routing (Shortest Path)

**Problem:** Find shortest path between two cities in a road network with 10,000 intersections

#### Approach 1: Breadth-First Search (BFS) - Unweighted

```
Treats all roads as equal distance

Network:
A --100km--> B --5km--> C
A --50km--> D --50km--> C

BFS finds: A → B → C (2 hops)
Distance: 100 + 5 = 105 km

Ignores: A → D → C (2 hops)
Distance: 50 + 50 = 100 km ← Actually shorter!

Problem: BFS optimizes for fewest edges, not shortest distance
```

**BFS Execution:**
```
Queue: [A]
Visited: {}

Step 1: Visit A
Queue: [B, D]
Visited: {A}

Step 2: Visit B (first in queue)
Queue: [D, C]
Visited: {A, B}

Step 3: Visit D
Queue: [C, C]
Visited: {A, B, D}

Step 4: Visit C (from B path)
Queue: [C]
Visited: {A, B, D, C}

Result: A → B → C (wrong!)
Time: O(V + E) but gives wrong answer
```

#### Approach 2: Dijkstra's Algorithm (Weighted)

```
Same network:
A --100km--> B --5km--> C
A --50km--> D --50km--> C

Dijkstra finds: A → D → C
Distance: 100 km ← Optimal!

How? Explores paths by cumulative distance, not hop count
```

**Dijkstra Execution:**
```
Priority Queue: [(A, 0)]
Distances: {A: 0, others: ∞}

Step 1: Process A (distance 0)
Update neighbors:
  B: 0 + 100 = 100
  D: 0 + 50 = 50
PQ: [(D, 50), (B, 100)]

Step 2: Process D (distance 50, smallest)
Update neighbors:
  C: 50 + 50 = 100
PQ: [(B, 100), (C, 100)]

Step 3: Process B (distance 100)
Update neighbors:
  C: 100 + 5 = 105 (worse than current 100, don't update)
PQ: [(C, 100)]

Step 4: Process C (distance 100)
Done! Path: A → D → C

Result: 100 km (optimal!)
Time: O((V + E) log V) with binary heap
```

**Performance Comparison:**

| Metric | BFS | Dijkstra | Improvement |
|--------|-----|----------|-------------|
| Result | Wrong (105 km) | Correct (100 km) | 5% shorter |
| Time | O(V + E) | O((V + E) log V) | Slightly slower |
| Use case | Unweighted graphs | Weighted graphs | Essential! |
| Optimality | # of hops | Total weight | Correct metric |

**Real-world impact:**
- GPS navigation: 5-20% shorter routes with Dijkstra
- Network routing: Lower latency paths
- Cost: Minimal (few ms difference for practical graphs)

**Your calculation:** For 1000-node graph, 5000 edges:
- BFS time: <span class="fill-in">_____</span> (V + E)
- Dijkstra time: <span class="fill-in">_____</span> (E log V)
- Trade-off: <span class="fill-in">[Worth it?]</span>

---

## Core Concepts

### Topic 1: Dijkstra's Shortest Path Algorithm

**Concept:** Find shortest path from source to all other vertices in a weighted graph (non-negative weights).

**Algorithm Overview:**

```
Given graph G = (V, E) and source vertex s:

1. Initialize:
   - Distance[s] = 0
   - Distance[all others] = ∞
   - Priority Queue = [(s, 0)]
   - Visited = {}

2. While PQ not empty:
   a. Extract vertex u with min distance
   b. If u in Visited, skip
   c. Mark u as Visited
   d. For each neighbor v of u:
      - new_dist = Distance[u] + weight(u, v)
      - If new_dist < Distance[v]:
          Distance[v] = new_dist
          Parent[v] = u
          Add (v, new_dist) to PQ

3. Return Distance array and Parent pointers
```

**Example:**

```
Graph:
    A --1--> B
    |        |
    4        2
    |        |
    v        v
    C --1--> D

Source: A

Iteration 1: Process A (dist=0)
  Visit B: dist = 0+1 = 1
  Visit C: dist = 0+4 = 4
  PQ: [(B,1), (C,4)]
  Distances: {A:0, B:1, C:4, D:∞}

Iteration 2: Process B (dist=1)
  Visit D: dist = 1+2 = 3
  PQ: [(D,3), (C,4)]
  Distances: {A:0, B:1, C:4, D:3}

Iteration 3: Process D (dist=3)
  (No unvisited neighbors)
  PQ: [(C,4)]
  Distances: {A:0, B:1, C:4, D:3}

Iteration 4: Process C (dist=4)
  Visit D: dist = 4+1 = 5 (worse than 3, ignore)
  PQ: []
  Distances: {A:0, B:1, C:4, D:3}

Final shortest paths from A:
A → A: 0
A → B: 1 (path: A → B)
A → C: 4 (path: A → C)
A → D: 3 (path: A → B → D)
```

**Implementation:**

```java
class DijkstraShortestPath {
    static class Edge {
        int to;
        int weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    /**
     * Dijkstra's algorithm
     * Time: O((V + E) log V) with binary heap
     * Space: O(V)
     */
    public int[] dijkstra(List<List<Edge>> graph, int source) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // Priority queue: (vertex, distance)
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{source, 0});

        boolean[] visited = new boolean[n];

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];

            if (visited[u]) continue;
            visited[u] = true;

            // Relax edges
            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int newDist = dist[u] + edge.weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    pq.offer(new int[]{v, newDist});
                }
            }
        }

        return dist;
    }

    // Reconstruct path from source to target
    public List<Integer> getPath(int[] parent, int target) {
        List<Integer> path = new ArrayList<>();
        for (int v = target; v != -1; v = parent[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }
}
```

**Optimizations:**

**1. Fibonacci Heap (Theoretical):**
```
Time: O(E + V log V)
vs Binary Heap: O((V + E) log V)

Improvement: Better for dense graphs
Practical: Binary heap usually faster due to simpler implementation
```

**2. Bidirectional Dijkstra:**
```
Simultaneously search from source and target:

Forward:  s → ... → meet point
Backward: t → ... → meet point

Time: ~2x faster (searches half the graph)
Use case: Point-to-point shortest path (GPS navigation)
```

**3. A* Search (Heuristic):**
```
Dijkstra: f(n) = g(n)              (distance so far)
A*:       f(n) = g(n) + h(n)       (+ estimated distance to goal)

h(n) = heuristic (e.g., straight-line distance)

Explores fewer nodes by guiding search toward target
Optimal if h(n) is admissible (never overestimates)
```

**Limitations:**

```
Dijkstra FAILS with negative edge weights:

Graph:
A --1--> B
|        |
2       -5
|        |
v        v
C <------+

Dijkstra from A:
1. Process A: dist[B]=1, dist[C]=2
2. Process B: dist[C] = 1+(-5) = -4 (improvement!)
3. But B already visited, won't update C!

Result: dist[C] = 2 (wrong! should be -4)

Solution: Use Bellman-Ford for negative weights
```

**Use Cases:**
- GPS navigation (road networks)
- Network routing (OSPF protocol)
- Robotics path planning
- Social network analysis (degrees of separation)

---

### Topic 2: Minimum Spanning Tree (MST)

**Concept:** Subset of edges that connects all vertices with minimum total weight, no cycles.

**MST Properties:**

```
Given graph G = (V, E):
- MST has exactly V-1 edges
- MST is acyclic (it's a tree)
- MST connects all vertices
- MST has minimum total edge weight
- MST may not be unique (multiple MSTs with same weight)
```

**Kruskal's Algorithm:**

```
Greedy approach: Add edges in increasing weight order, skip if creates cycle

Algorithm:
1. Sort edges by weight (ascending)
2. Initialize Union-Find (each vertex in its own set)
3. For each edge (u, v, weight):
   - If u and v in different sets:
       Add edge to MST
       Union(u, v)
   - Else: Skip (would create cycle)
4. Return MST

Time: O(E log E) for sorting + O(E α(V)) for Union-Find ≈ O(E log E)
Space: O(V) for Union-Find
```

**Example:**

```
Graph:
    A
   /|\
  4 2 5
 /  |  \
B---3---C
     6
     |
     D

Edges sorted by weight:
(A,C): 2
(B,C): 3
(A,B): 4
(A,D): 5
(C,D): 6

Step 1: Add (A,C) weight 2
  Sets: {A,C}, {B}, {D}
  MST: {(A,C)}

Step 2: Add (B,C) weight 3
  Sets: {A,B,C}, {D}
  MST: {(A,C), (B,C)}

Step 3: Try (A,B) weight 4
  A and B already connected → Skip (would create cycle)

Step 4: Add (A,D) weight 5
  Sets: {A,B,C,D}
  MST: {(A,C), (B,C), (A,D)}

Step 5: Try (C,D) weight 6
  C and D already connected → Skip

MST edges: (A,C), (B,C), (A,D)
Total weight: 2 + 3 + 5 = 10
```

**Prim's Algorithm:**

```
Greedy approach: Grow MST from a starting vertex

Algorithm:
1. Start with arbitrary vertex s
2. Add s to MST
3. Repeat until all vertices in MST:
   - Find minimum weight edge (u, v) where u in MST, v not in MST
   - Add v to MST
   - Add edge (u, v) to MST

Time: O(E log V) with binary heap, O(E + V log V) with Fibonacci heap
Space: O(V)

Similar to Dijkstra but minimizes edge weight instead of path weight
```

**Implementation (Kruskal's):**

```java
class KruskalMST {
    static class Edge implements Comparable<Edge> {
        int u, v, weight;

        Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    // Union-Find data structure
    static class UnionFind {
        int[] parent, rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return false; // Already in same set

            // Union by rank
            if (rank[px] < rank[py]) {
                parent[px] = py;
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
            } else {
                parent[py] = px;
                rank[px]++;
            }
            return true;
        }
    }

    public List<Edge> kruskal(int n, List<Edge> edges) {
        Collections.sort(edges); // O(E log E)

        UnionFind uf = new UnionFind(n);
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) {
                mst.add(edge);
                if (mst.size() == n - 1) break; // MST complete
            }
        }

        return mst;
    }
}
```

**Use Cases:**
- Network design (minimize cable length)
- Clustering algorithms (single-linkage)
- Image segmentation
- Approximation algorithms (TSP)

---

### Topic 3: Topological Sort

**Concept:** Linear ordering of vertices in a directed acyclic graph (DAG) such that for every edge (u, v), u comes before v.

**Properties:**

```
Valid only for DAGs (Directed Acyclic Graphs):
- If graph has cycle → No topological ordering exists
- Multiple valid orderings may exist
- Used for dependency resolution, task scheduling
```

**DFS-Based Algorithm:**

```
Algorithm:
1. Mark all vertices as unvisited
2. For each unvisited vertex:
   - Perform DFS
   - After visiting all descendants, add vertex to result (reverse order)
3. Reverse result to get topological order

Time: O(V + E)
Space: O(V) for recursion stack

Key insight: Vertices with no outgoing edges go last
```

**Example:**

```
DAG (course prerequisites):
A (Intro) → B (Data Structures) → D (Algorithms)
         → C (Databases)        → D

Valid topological orders:
1. A, B, C, D
2. A, C, B, D
Both satisfy: A before B, A before C, B before D, C before D
```

**Detailed Execution:**

```
Graph:
A → B → D
↓   ↓
C   →

DFS from A:
  Visit A
    Visit B
      Visit D
        (No outgoing edges, add D to stack)
      (B done, add B to stack)
    Visit C
      Visit D (already visited, skip)
      (C done, add C to stack)
  (A done, add A to stack)

Stack (reverse order): [D, B, C, A]
Reverse: [A, C, B, D] or [A, B, C, D]
```

**Implementation:**

```java
class TopologicalSort {
    /**
     * DFS-based topological sort
     * Time: O(V + E), Space: O(V)
     */
    public List<Integer> topologicalSort(int n, List<List<Integer>> graph) {
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();

        // Visit all vertices
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, graph, visited, stack);
            }
        }

        // Build result (reverse of stack)
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private void dfs(int u, List<List<Integer>> graph, boolean[] visited, Stack<Integer> stack) {
        visited[u] = true;

        for (int v : graph.get(u)) {
            if (!visited[v]) {
                dfs(v, graph, visited, stack);
            }
        }

        stack.push(u); // Add after visiting all descendants
    }

    /**
     * Kahn's algorithm (BFS-based)
     * Detects cycles explicitly
     */
    public List<Integer> topologicalSortKahn(int n, List<List<Integer>> graph) {
        int[] inDegree = new int[n];

        // Calculate in-degrees
        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                inDegree[v]++;
            }
        }

        // Start with vertices that have no incoming edges
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);

            // Remove edges from u
            for (int v : graph.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // If result doesn't contain all vertices, graph has cycle
        if (result.size() != n) {
            throw new IllegalArgumentException("Graph has cycle!");
        }

        return result;
    }
}
```

**Cycle Detection:**

```
Kahn's algorithm automatically detects cycles:

If cycle exists:
  All vertices in cycle have inDegree > 0
  Never added to queue
  result.size() < n

Example:
A → B → C → A (cycle)

inDegree: A=1, B=1, C=1
Queue: [] (empty! all have incoming edges)
Result: [] (empty)
Cycle detected!
```

**Use Cases:**
- **Build systems:** Compile dependencies in correct order
- **Task scheduling:** Execute tasks respecting dependencies
- **Course prerequisites:** Determine valid course order
- **Package managers:** Install packages with dependencies
- **Git commit history:** Linearize parallel development

---

### Topic 4: Distributed Graph Processing

**Concept:** Process large-scale graphs that don't fit on a single machine using frameworks like Pregel, GraphX.

**Challenges:**

```
Large graphs (billions of vertices, trillions of edges):
- Don't fit in memory of single machine
- Communication overhead between machines
- Load balancing (hot vertices)
- Fault tolerance

Examples:
- Social networks (Facebook: 3B+ users)
- Web graphs (billions of pages)
- Knowledge graphs
```

**Vertex-Centric Model (Pregel/BSP):**

```
Think like a vertex:

Each vertex:
- Has state (value, neighbors)
- Receives messages from neighbors
- Computes new state
- Sends messages to neighbors

Computation in supersteps:
1. All vertices receive messages
2. All vertices compute in parallel
3. All vertices send messages
4. Repeat until convergence (no messages) or max iterations
```

**PageRank Example (Distributed):**

```
PageRank: Rank web pages by importance

Algorithm (vertex-centric):
  For each superstep:
    1. Receive rank contributions from in-neighbors
    2. Sum contributions
    3. Apply damping: rank = 0.15 + 0.85 × sum
    4. Send rank/out-degree to each out-neighbor

Iteration 1:
Vertex A (out-degree=2):
  rank = 1.0 (initial)
  Send 0.5 to B, 0.5 to C

Vertex B (out-degree=1):
  rank = 1.0
  Send 1.0 to C

Iteration 2:
Vertex A:
  Receive nothing
  rank = 0.15 + 0.85 × 0 = 0.15

Vertex B:
  Receive 0.5 from A
  rank = 0.15 + 0.85 × 0.5 = 0.575

Vertex C:
  Receive 0.5 from A, 1.0 from B
  rank = 0.15 + 0.85 × 1.5 = 1.425

Continue until convergence...
```

**Graph Partitioning:**

```
Goal: Divide graph across machines to minimize communication

Strategies:

1. Random partitioning:
   vertex_id % num_machines
   + Simple
   - Ignores structure, high communication

2. Hash partitioning:
   hash(vertex_id) % num_machines
   + Balanced load
   - Doesn't consider edges

3. Edge-cut partitioning:
   Minimize edges between partitions
   + Less communication
   - Harder to compute

4. Vertex-cut partitioning:
   Replicate high-degree vertices
   + Better for power-law graphs
   - Vertex duplication overhead
```

**Optimizations:**

**1. Combiner (Reduce Communication):**
```
Without combiner:
  Vertex sends 1M messages → Network overload

With combiner:
  Vertex combines messages locally → Send aggregated result
  Example: Sum 1M values locally, send one sum
```

**2. Asynchronous Processing:**
```
Synchronous (BSP):
  Wait for ALL vertices before next superstep
  Slow vertices delay everyone

Asynchronous:
  Process messages as they arrive
  No global barriers
  Faster convergence
```

**3. Dynamic Graph Partitioning:**
```
Monitor communication patterns
Repartition graph to co-locate frequently communicating vertices
Reduce cross-partition edges
```

---

## Decision Framework

### Question 1: Which shortest path algorithm?

**Use Dijkstra when:**
- Non-negative weights: <span class="fill-in">[Road networks, costs]</span>
- Single source: <span class="fill-in">[From one node to all others]</span>
- Dense graphs: <span class="fill-in">[Many edges]</span>

**Use Bellman-Ford when:**
- Negative weights allowed: <span class="fill-in">[Financial arbitrage]</span>
- Need cycle detection: <span class="fill-in">[Negative cycles]</span>
- Simple implementation: <span class="fill-in">[No priority queue]</span>

**Use A* when:**
- Point-to-point search: <span class="fill-in">[GPS navigation]</span>
- Heuristic available: <span class="fill-in">[Euclidean distance]</span>
- Want faster search: <span class="fill-in">[Guided by heuristic]</span>

### Question 2: MST Algorithm Choice?

**Use Kruskal when:**
- Sparse graph: <span class="fill-in">[E << V²]</span>
- Need simple implementation: <span class="fill-in">[Sort + Union-Find]</span>
- Edge list representation: <span class="fill-in">[Not adjacency list]</span>

**Use Prim when:**
- Dense graph: <span class="fill-in">[E ≈ V²]</span>
- Adjacency list: <span class="fill-in">[Efficient neighbor access]</span>
- Want incremental MST: <span class="fill-in">[Grow from one vertex]</span>

### Question 3: Topological Sort?

**Use DFS-based when:**
- Simple implementation needed
- Want to detect cycles during sort
- Graph fits in memory

**Use Kahn's (BFS) when:**
- Need explicit cycle detection
- Want lexicographically smallest ordering
- Parallel processing possible

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken graph algorithm implementations. This tests your understanding of algorithm correctness and edge cases.

### Challenge 1: Dijkstra's Distance Check Bug

```java
/**
 * Dijkstra's algorithm with a CRITICAL BUG.
 * Can return incorrect shortest paths!
 */
public class BuggyDijkstra {

    public int[] dijkstra(List<List<Edge>> graph, int source) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];

            // Missing check here!

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int newDist = dist[u] + edge.weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    pq.offer(new int[]{v, newDist});
                }
            }
        }

        return dist;
    }
}
```

**Your debugging:**

- Bug: <span class="fill-in">[What's missing after polling from PQ?]</span>

**Failure scenario:**

- Graph: 0→1(weight=5), 0→1(weight=3), 1→2(weight=1)
- Without fix: <span class="fill-in">[How many times do we process node 1?]</span>
- With fix: <span class="fill-in">[How many times should we process node 1?]</span>
- Impact: <span class="fill-in">[Correctness? Performance? Both?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Missing distance check after polling. Should verify that we haven't already found a better path to this node.

**Fix:**

```java
while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int u = curr[0];
    int d = curr[1];

    // Skip if we've already processed this node with better distance
    if (d > dist[u]) continue;

    // ... rest of code
}
```

**Why it matters:** We may add the same node to the priority queue multiple times with different distances. Without this check, we process outdated entries, doing unnecessary work and potentially corrupting results.

**Example trace without fix:**
```
Step 1: Process (0, dist=0), add (1, dist=5)
Step 2: Find shorter path, add (1, dist=3)
Step 3: Process (1, dist=3) ✓ (correct)
Step 4: Process (1, dist=5) ✗ (outdated entry, wastes time)
```

**With fix:** Step 4 is skipped because d=5 > dist[1]=3.
</details>

---

### Challenge 2: Kruskal's MST - Cycle Detection Miss

```java
/**
 * Kruskal's algorithm with MISSING CYCLE CHECK.
 * Can create cycles in MST!
 */
public class BuggyKruskalMST {

    public List<Edge> kruskal(int n, List<Edge> edges) {
        Collections.sort(edges); // Sort by weight

        UnionFind uf = new UnionFind(n);
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            // Missing cycle check!
            mst.add(edge);

            if (mst.size() == n - 1) break;
        }

        return mst;
    }
}
```

**Your debugging:**

- Bug: <span class="fill-in">[What's missing before adding edge to MST?]</span>

**Failure scenario:**

- Graph: Triangle with edges (0,1,1), (1,2,2), (2,0,3)
- With bug: MST edges = <span class="fill-in">[Which edges?]</span>
- Expected: MST edges = <span class="fill-in">[Which edges?]</span>
- Result: <span class="fill-in">[Valid tree? Contains cycle?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Missing Union-Find check to detect cycles. Must verify that edge doesn't connect two vertices already in the same component.

**Fix:**

```java
for (Edge edge : edges) {
    if (uf.union(edge.u, edge.v)) {  // Only add if doesn't create cycle
        mst.add(edge);
        if (mst.size() == n - 1) break;
    }
}
```

**Why it matters:** Kruskal's algorithm relies on Union-Find to detect cycles. Without this check, we'd add all edges in weight order, creating cycles instead of a tree.

**Correct behavior:**
- Edge (0,1,1): Add (different components) ✓
- Edge (1,2,2): Add (different components) ✓
- Edge (2,0,3): Skip (0 and 2 already connected via 1) ✗

**MST edges:** (0,1), (1,2) with total weight = 3
</details>

---

### Challenge 3: Topological Sort - Term Handling Bug

```java
/**
 * Topological sort (DFS-based) with CYCLE PROPAGATION BUG.
 * Fails to detect cycles properly!
 */
public class BuggyTopologicalSort {

    public List<Integer> topologicalSort(int n, List<List<Integer>> graph) {
        int[] visited = new int[n];  // 0: unvisited, 1: visiting, 2: visited
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            if (visited[i] == 0) {
                if (!dfs(i, graph, visited, stack)) {
                    return new ArrayList<>();  // Cycle detected
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private boolean dfs(int node, List<List<Integer>> graph,
                       int[] visited, Stack<Integer> stack) {
        visited[node] = 1;  // Mark as visiting

        for (int neighbor : graph.get(node)) {
            if (visited[neighbor] == 1) {
                return false;  // Cycle detected
            }
            if (visited[neighbor] == 0) {
                dfs(neighbor, graph, visited, stack);  // BUG: Missing return check!
            }
        }

        visited[node] = 2;  // Mark as visited
        stack.push(node);
        return true;
    }
}
```

**Your debugging:**

- Bug: <span class="fill-in">[What's missing in the recursive call?]</span>

**Failure scenario:**

- Graph: 0→1, 1→2, 2→0 (cycle)
- With bug: <span class="fill-in">[Does it detect the cycle?]</span>
- Expected: <span class="fill-in">[Should return empty list]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Not checking the return value of recursive DFS call. Even if a recursive call detects a cycle (returns false), we ignore it and continue.

**Fix:**

```java
if (visited[neighbor] == 0) {
    if (!dfs(neighbor, graph, visited, stack)) {
        return false;  // Propagate cycle detection
    }
}
```

**Why it matters:** Cycle detection must propagate back up the call stack. Without checking the return value, we lose the cycle detection signal.

**Trace with cycle:**
```
DFS(0): visits 1
  DFS(1): visits 2
    DFS(2): sees 0 is visiting → return false (cycle!)
  DFS(1): ignores return value, continues → BUG!
DFS(0): completes successfully → WRONG!
```

**Correct:** When DFS(2) detects cycle, DFS(1) should return false, then DFS(0) should return false, signaling cycle to main function.
</details>

---

### Challenge 4: Distributed Graph - Partition Imbalance

```java
/**
 * Graph partitioning with LOAD BALANCING BUG.
 * Creates severely unbalanced partitions!
 */
public class BuggyGraphPartitioner {

    public Map<Integer, Integer> partitionGraph(List<Edge> edges, int numPartitions) {
        Map<Integer, Integer> vertexToPartition = new HashMap<>();

        for (Edge edge : edges) {
            // Assign each vertex to partition based on vertex ID
            int partition = edge.u % numPartitions;
            vertexToPartition.put(edge.u, partition);
            vertexToPartition.put(edge.v, partition);  // BUG: Same partition!
        }

        return vertexToPartition;
    }
}
```

**Your debugging:**

- Bug: <span class="fill-in">[What's wrong with vertex assignment?]</span>

**Failure scenario:**

- Graph: Star topology with hub vertex 0, spokes 1-1000
- Edges: (0,1), (0,2), ..., (0,1000)
- 10 partitions
- With bug: Partition 0 has <span class="fill-in">[how many vertices?]</span>
- Expected: Each partition should have ~<span class="fill-in">[how many vertices?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Assigns both vertices of an edge to the same partition based on source vertex. This destroys any hash-based distribution and creates severe imbalance.

**Fix:**

```java
for (Edge edge : edges) {
    // Each vertex independently assigned to partition
    if (!vertexToPartition.containsKey(edge.u)) {
        vertexToPartition.put(edge.u, edge.u % numPartitions);
    }
    if (!vertexToPartition.containsKey(edge.v)) {
        vertexToPartition.put(edge.v, edge.v % numPartitions);
    }
}
```

**Why it matters:** In star topology with hub vertex 0:
- All edges processed assign both endpoints to partition 0 % 10 = 0
- Partition 0: 1001 vertices (hub + all spokes)
- Partitions 1-9: 0 vertices each
- Complete load imbalance!

**Correct distribution:** Each vertex independently hashed → ~100 vertices per partition.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

-   [ ] Found all 4+ bugs across graph algorithms
-   [ ] Understood correctness vs performance issues
-   [ ] Could explain WHY each bug causes failures
-   [ ] Learned common graph algorithm mistakes

**Common graph algorithm bugs you discovered:**

1. <span class="fill-in">[List patterns: distance checks, cycle detection, etc.]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

---

## Practice Scenarios

### Scenario 1: Ride-Sharing App (Shortest Path)

**Requirements:**
- Find fastest route between pickup and dropoff
- Consider traffic (dynamic edge weights)
- Multiple drivers, multiple riders
- Real-time updates

**Your design:**

Algorithm:
- Base: <span class="fill-in">[Dijkstra? A*?]</span>
- Optimization: <span class="fill-in">[Bidirectional? Landmarks?]</span>
- Traffic: <span class="fill-in">[Update weights how often?]</span>

Scalability:
- Precomputation: <span class="fill-in">[Contraction hierarchies?]</span>
- Caching: <span class="fill-in">[Common routes?]</span>

### Scenario 2: Network Infrastructure (MST)

**Requirements:**
- Connect 1000 datacenters
- Minimize fiber optic cable cost
- Ensure redundancy (k-connected)
- Latency constraints

**Your design:**

Algorithm:
- Primary: <span class="fill-in">[Kruskal? Prim?]</span>
- Redundancy: <span class="fill-in">[Add k-1 more MSTs?]</span>
- Constraints: <span class="fill-in">[Filter edges first?]</span>

Cost optimization:
- Weight: <span class="fill-in">[Distance? Cost? Both?]</span>
- Trade-offs: <span class="fill-in">[Cost vs latency?]</span>

### Scenario 3: Build System (Topological Sort)

**Requirements:**
- 10,000 source files
- Complex dependencies
- Parallel compilation
- Incremental builds

**Your design:**

Algorithm:
- Sort: <span class="fill-in">[DFS? Kahn's?]</span>
- Parallel: <span class="fill-in">[How to identify independent tasks?]</span>
- Incremental: <span class="fill-in">[Only rebuild affected files?]</span>

Optimization:
- Cycle detection: <span class="fill-in">[When to check?]</span>
- Caching: <span class="fill-in">[Store previous ordering?]</span>

---

## Review Checklist

Before moving to the next topic:

-   [ ] **Understanding**
    -   [ ] Understand Dijkstra's algorithm
    -   [ ] Know MST algorithms (Kruskal, Prim)
    -   [ ] Understand topological sort
    -   [ ] Know when each algorithm applies

-   [ ] **Implementation**
    -   [ ] Can implement Dijkstra
    -   [ ] Can implement Kruskal with Union-Find
    -   [ ] Can implement topological sort
    -   [ ] Understand complexity analysis

-   [ ] **Decision Making**
    -   [ ] Know algorithm trade-offs
    -   [ ] Can choose for requirements
    -   [ ] Understand limitations
    -   [ ] Completed practice scenarios

---

### Mastery Certification

**I certify that I can:**

-   [ ] Implement Dijkstra from memory
-   [ ] Explain MST algorithms
-   [ ] Detect negative weight cycles
-   [ ] Implement topological sort
-   [ ] Choose appropriate algorithm
-   [ ] Analyze time/space complexity
-   [ ] Optimize for real-world graphs
-   [ ] Debug graph algorithm issues
-   [ ] Apply to distributed systems
-   [ ] Teach concepts to others

**Self-assessment score:** ___/10

**If score < 8:** Review weak areas and retry.

**If score ≥ 8:** Congratulations! Proceed to next topic.