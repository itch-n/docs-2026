# Graph Algorithms: Optimization & Ordering

> Apply graph traversal to solve optimization problems: topological sort, shortest paths, and MST

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After learning these algorithms, explain them simply.

**Prompts to guide you:**

1. **What does Topological Sort give us?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

2. **When do we need Topological Sort?**
    - Your answer: <span class="fill-in">[Course prerequisites, build systems...]</span>

3. **What does Dijkstra's algorithm find?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

4. **Real-world analogy for Dijkstra's algorithm:**
    - Example: "Dijkstra's algorithm is like finding the cheapest flight where..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

5. **Why can't we use Dijkstra for negative weights?**
    - Your answer: <span class="fill-in">[Fill in after practice]</span>

6. **Why do we need Minimum Spanning Trees?**
    - Your answer: <span class="fill-in">[Fill in after learning]</span>

7. **What problem does Union-Find solve?**
    - Your answer: <span class="fill-in">[Dynamic connectivity...]</span>

</div>

---

## Quick Quiz (Do BEFORE learning)

<div class="learner-section" markdown>

**Your task:** Test your intuition about these algorithms before diving deep.

### Complexity Predictions

1. **Topological Sort (DFS-based):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - When does it fail: <span class="fill-in">[Your guess]</span>
    - Verified: <span class="fill-in">[Actual: O(V+E)]</span>

2. **Dijkstra's algorithm for shortest path:**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual: O((V+E)logV)]</span>

3. **Minimum Spanning Tree (Kruskal's):**
    - Time complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - What data structure: <span class="fill-in">[Union-Find]</span>
    - Verified: <span class="fill-in">[Actual: O(ElogE)]</span>

4. **Union-Find operations:**
    - Find/Union complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - With optimizations: <span class="fill-in">[Path compression + union by rank]</span>
    - Verified: <span class="fill-in">[Actual: O(α(n)) ≈ O(1)]</span>

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

**Scenario 4:** Dynamic friend groups (social network)

- **Algorithm:** <span class="fill-in">[Union-Find? DFS?]</span>
- **Operations:** <span class="fill-in">[Add friendship, check if connected]</span>
- **Why not DFS:** <span class="fill-in">[Union-Find is faster for dynamic updates]</span>

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

**Recommended study order:**
1. ⭐⭐⭐ Topological Sort (Topic 3 below) - Most common in interviews
2. ⭐⭐ Dijkstra's Algorithm (Topic 1 below) - Important for weighted graphs
3. ⭐⭐ Union-Find (add after completing above) - Dynamic connectivity
4. ⭐ MST (Topic 2 below) - Optional, low interview frequency

---

### Topic 1: Dijkstra's Shortest Path Algorithm

**Interview Priority: ⭐⭐ GOOD TO KNOW** - Appears in ~15% of graph problems

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

**Interview Priority: ⭐ OPTIONAL** - Appears in <5% of interviews, study if time permits

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

**Interview Priority: ⭐⭐⭐ CRITICAL** - Course Schedule is in top 20 most common problems!

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

### Topic 4: Union-Find (Disjoint Set Union)

**Interview Priority: ⭐⭐ IMPORTANT** - Key data structure for dynamic connectivity (~10% of graph problems)

**Concept:** Efficiently track and merge disjoint sets, primarily used for dynamic connectivity problems.

**Core Operations:**

```
1. Find(x): Which set does element x belong to?
   - Returns representative (root) of the set

2. Union(x, y): Merge the sets containing x and y
   - Connect roots of both sets

3. Connected(x, y): Are x and y in the same set?
   - Return Find(x) == Find(y)
```

**Optimizations:**

1. **Path Compression** (in Find):
   ```
   Make every node point directly to root
   Flattens tree structure
   Time: O(α(n)) amortized per operation
   ```

2. **Union by Rank**:
   ```
   Always attach smaller tree under larger tree
   Keeps tree balanced
   ```

**Implementation:**

```java
class UnionFind {
    private int[] parent;
    private int[] rank;

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;  // Each element is its own parent initially
            rank[i] = 0;
        }
    }

    // Find with path compression
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // Path compression
        }
        return parent[x];
    }

    // Union by rank
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;  // Already in same set
        }

        // Union by rank: attach smaller tree under larger
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        return true;
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }
}
```

**Common Problems:**

1. **Number of Connected Components**
   - Start with n components
   - Each union decreases count by 1
   - Final count = n - (number of successful unions)

2. **Detect Cycle in Undirected Graph**
   - For each edge (u, v):
     - If find(u) == find(v): cycle exists!
     - Else: union(u, v)

3. **Accounts Merge (LeetCode 721)**
   - Union accounts with common emails
   - Each component = one person

**Example: Detect Redundant Connection**

```
Problem: Find edge that creates cycle in undirected graph

Input: edges = [[1,2], [1,3], [2,3]]
Output: [2,3] (creates cycle)

Solution:
UnionFind uf = new UnionFind(n);
for (int[] edge : edges) {
    if (!uf.union(edge[0], edge[1])) {
        return edge;  // This edge creates cycle
    }
}
```

**Complexity:**

- Time: O(α(n)) per operation (inverse Ackermann, effectively O(1))
- Space: O(n) for parent and rank arrays

**When to Use Union-Find:**

✅ Dynamic connectivity (edges added over time)
✅ Detect cycles in undirected graphs
✅ Group elements by equivalence relation
✅ Kruskal's MST algorithm

❌ Need to remove edges (Union-Find doesn't support deletion)
❌ Directed graph cycle detection (use DFS instead)
❌ Shortest path queries (use BFS/Dijkstra)

**Use Cases:**
- Network connectivity
- Image segmentation (connected components)
- Kruskal's MST
- Social network friend groups
- Accounts merging

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

### Question 4: When to use Union-Find?

**Use Union-Find when:**
- Dynamic connectivity: <span class="fill-in">[Edges added over time]</span>
- Detect cycles in undirected graphs: <span class="fill-in">[Kruskal's MST]</span>
- Group by equivalence: <span class="fill-in">[Accounts merge, friend groups]</span>

**Don't use Union-Find when:**
- Need to remove edges: <span class="fill-in">[UF doesn't support deletion]</span>
- Directed graph cycles: <span class="fill-in">[Use DFS with states instead]</span>
- Need shortest paths: <span class="fill-in">[Use BFS/Dijkstra]</span>

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

### Your Debugging Scorecard

After finding and fixing all bugs:

-   [ ] Found all 3 bugs across different algorithms
-   [ ] Understood correctness vs performance issues
-   [ ] Could explain WHY each bug causes failures
-   [ ] Learned common algorithm implementation mistakes

**Common graph algorithm bugs you discovered:**

1. <span class="fill-in">[Missing distance check in Dijkstra after polling]</span>
2. <span class="fill-in">[Not using Union-Find to detect cycles in Kruskal's]</span>
3. <span class="fill-in">[Not propagating cycle detection in TopSort DFS]</span>

---

## Practice

### LeetCode Problems

**Focus on interview-critical patterns - practice in priority order:**

**Topological Sort (MUST DO - ⭐⭐⭐):**

- [ ] [207. Course Schedule](https://leetcode.com/problems/course-schedule/) ⭐⭐⭐
    - Pattern: <span class="fill-in">[Topological Sort / Cycle Detection]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) ⭐⭐⭐
    - Pattern: <span class="fill-in">[Topological Sort - return order]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [269. Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) (Premium) ⭐⭐
    - Pattern: <span class="fill-in">[Topological Sort]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>

**Union-Find (Important - ⭐⭐):**

- [ ] [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/) ⭐⭐
    - Pattern: <span class="fill-in">[Union-Find / Connected Components]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [684. Redundant Connection](https://leetcode.com/problems/redundant-connection/) ⭐⭐
    - Pattern: <span class="fill-in">[Union-Find / Cycle Detection]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>

- [ ] [721. Accounts Merge](https://leetcode.com/problems/accounts-merge/) ⭐⭐
    - Pattern: <span class="fill-in">[Union-Find / Grouping]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>

**Dijkstra (Good to know - ⭐⭐):**

- [ ] [743. Network Delay Time](https://leetcode.com/problems/network-delay-time/) ⭐⭐
    - Pattern: <span class="fill-in">[Dijkstra's Algorithm]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [787. Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/) ⭐⭐
    - Pattern: <span class="fill-in">[Modified Dijkstra with constraints]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>

**MST (Optional - ⭐):**

- [ ] [1584. Min Cost to Connect All Points](https://leetcode.com/problems/min-cost-to-connect-all-points/) ⭐
    - Pattern: <span class="fill-in">[Prim's MST]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Note: Low frequency - only if you have extra time

---

## Review Checklist

**Before moving to next topic, ensure you've mastered:**

-   [ ] **Understanding**
    -   [ ] Understand topological sort (DFS and Kahn's algorithms) ⭐⭐⭐
    -   [ ] Understand Dijkstra's algorithm ⭐⭐
    -   [ ] Know Union-Find with optimizations ⭐⭐
    -   [ ] Understand MST algorithms (Kruskal, Prim) ⭐
    -   [ ] Know when each algorithm applies

-   [ ] **Implementation**
    -   [ ] Can implement topological sort (both methods)
    -   [ ] Can implement Dijkstra with priority queue
    -   [ ] Can implement Union-Find with path compression
    -   [ ] Can implement Kruskal's MST (if time permits)
    -   [ ] Understand complexity analysis

-   [ ] **Pattern Recognition**
    -   [ ] Solved Course Schedule I & II (topological sort)
    -   [ ] Solved 2-3 Union-Find problems
    -   [ ] Attempted 1-2 Dijkstra problems
    -   [ ] Understand when to use each algorithm

-   [ ] **Decision Making**
    -   [ ] Know algorithm trade-offs
    -   [ ] Can choose correct algorithm for requirements
    -   [ ] Understand limitations (e.g., Dijkstra + negative weights)

---

### Mastery Certification

**I certify that I can:**

-   [ ] Implement topological sort from memory (both DFS and Kahn's) ⭐⭐⭐
-   [ ] Solve Course Schedule problems confidently
-   [ ] Implement Union-Find with path compression and union by rank ⭐⭐
-   [ ] Detect cycles using Union-Find
-   [ ] Implement Dijkstra's algorithm ⭐⭐
-   [ ] Explain when Dijkstra fails (negative weights)
-   [ ] Understand MST algorithms (Kruskal/Prim) ⭐
-   [ ] Choose appropriate algorithm for given problem
-   [ ] Analyze time/space complexity
-   [ ] Debug common algorithm issues
-   [ ] Explain these concepts to others

