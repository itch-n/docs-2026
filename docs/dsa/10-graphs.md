# 09. Graphs

> Traverse and search graph structures using DFS, BFS, and specialized algorithms

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is a graph in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **Why/when do we use graphs?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
    - Example: "A graph is like a social network where people are nodes and friendships are edges..."
    - Your analogy: _[Fill in]_

4. **What's the difference between DFS and BFS?**
    - Your answer: _[Fill in after solving problems]_

5. **When should you use DFS vs BFS?**
    - Your answer: _[Fill in after practice]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **BFS to find shortest path in unweighted graph:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity: _[Your guess: O(?)]_
    - Verified after learning: _[Actual: O(?)]_

2. **DFS to explore all paths in graph:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity (recursion stack): _[Your guess: O(?)]_
    - Verified: _[Actual]_

3. **Dijkstra's Algorithm for weighted graph:**
    - Time complexity with priority queue: _[Your guess: O(?)]_
    - Why is it slower than BFS? _[Fill in]_
    - Verified: _[Actual]_

### Scenario Predictions

**Scenario 1:** Find shortest path from A to E in graph: A→B→E, A→C→D→E

- **Which algorithm?** _[BFS/DFS/Dijkstra - Why?]_
- **BFS will find path in how many steps?** _[Fill in]_
- **DFS might find which path first?** _[Fill in]_
- **Why does BFS guarantee shortest?** _[Explain]_

**Scenario 2:** Detect if course prerequisites have a circular dependency

- **Which algorithm?** _[DFS/BFS/Topological Sort - Why?]_
- **What data structure represents this?** _[Directed/Undirected graph]_
- **How do you detect the cycle?** _[Fill in your approach]_

**Scenario 3:** Count number of islands in 2D grid

```
[['1','1','0'],
 ['1','0','0'],
 ['0','0','1']]
```

- **How many islands?** _[Your guess]_
- **Which algorithm?** _[DFS/BFS - Why?]_
- **What marks a cell as visited?** _[Fill in]_

**Scenario 4:** Find shortest path in weighted graph with edges: (A,B,4), (A,C,2), (C,B,1)

- **Path from A to B using BFS:** _[What would BFS find?]_
- **Optimal path using Dijkstra:** _[What's the shortest?]_
- **Why can't BFS find optimal path here?** _[Explain]_

### Trade-off Quiz

**Question 1:** When would DFS be BETTER than BFS?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question 2:** What's the MAIN requirement for Topological Sort to work?

- [ ] Graph must be undirected
- [ ] Graph must have no cycles (DAG)
- [ ] Graph must be weighted
- [ ] Graph must be connected

Verify after implementation: _[Which one(s)?]_

**Question 3:** Adjacency Matrix vs Adjacency List - which is better for sparse graphs?

- Your answer: _[Matrix/List - Why?]_
- Space complexity comparison: _[Fill in after learning]_

### Graph Representation Quiz

Given graph: 0→1, 0→2, 1→3, 2→3

**Adjacency List representation:**
```
Your answer:
_[Draw/write the adjacency list structure]_
```

**Adjacency Matrix representation:**
```
Your answer:
_[Draw the 4x4 matrix]_
```

**Which uses less space?** _[Fill in and explain why]_

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand the impact.

### Example 1: Find Shortest Path

**Problem:** Find shortest path between two nodes in an unweighted graph.

#### Approach 1: DFS (Suboptimal)

```java
// Naive approach - DFS finds A path, not shortest path
public static int findPath_DFS(Map<Integer, List<Integer>> graph, int start, int end) {
    Set<Integer> visited = new HashSet<>();
    return dfsPathLength(graph, start, end, visited, 0);
}

private static int dfsPathLength(Map<Integer, List<Integer>> graph,
                                 int current, int end, Set<Integer> visited, int length) {
    if (current == end) return length;
    if (visited.contains(current)) return Integer.MAX_VALUE;

    visited.add(current);
    int minPath = Integer.MAX_VALUE;

    for (int neighbor : graph.getOrDefault(current, new ArrayList<>())) {
        int pathLen = dfsPathLength(graph, neighbor, end, visited, length + 1);
        minPath = Math.min(minPath, pathLen);
    }

    visited.remove(current); // Backtrack
    return minPath;
}
```

**Analysis:**

- Time: O(V!) in worst case - explores all possible paths
- Space: O(V) - recursion stack
- Problem: Explores unnecessary paths, no guarantee of finding shortest first

#### Approach 2: BFS (Optimized)

```java
// Optimized approach - BFS guarantees shortest path
public static int findPath_BFS(Map<Integer, List<Integer>> graph, int start, int end) {
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    queue.offer(start);
    visited.add(start);
    int length = 0;

    while (!queue.isEmpty()) {
        int size = queue.size();

        for (int i = 0; i < size; i++) {
            int node = queue.poll();

            if (node == end) return length;

            for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        length++;
    }

    return -1; // Not found
}
```

**Analysis:**

- Time: O(V + E) - visits each node and edge once
- Space: O(V) - queue storage
- Benefit: Explores level-by-level, first path found is shortest

#### Performance Comparison

| Graph Size | DFS (worst case) | BFS | Speedup |
|------------|------------------|-----|---------|
| V = 10, E = 20 | ~3,628,800 ops (10!) | 30 ops | ~120,000x |
| V = 6, E = 10 | ~720 ops (6!) | 16 ops | 45x |
| V = 100, E = 200 | Intractable | 300 ops | Infinite |

**Your calculation:** For a graph with V = 8 nodes, BFS would be approximately _____ times faster in worst case.

#### Why Does BFS Find Shortest Path?

**Key insight to understand:**

In graph A→B→D, A→C→D looking for path from A to D:

```
BFS Level 0: [A]
BFS Level 1: [B, C]        (distance = 1 from A)
BFS Level 2: [D, D]        (distance = 2 from A, found first time)
```

DFS might go: A → B → D (found) but doesn't know if A → C → D is shorter without exploring everything.

**Why BFS guarantees shortest:**

- Explores all nodes at distance k before distance k+1
- First time we reach target = minimum distance
- No need to explore further paths

**After implementing, explain in your own words:**

- _[Why does level-order exploration matter?]_
- _[When would DFS accidentally find shortest path?]_

---

### Example 2: Graph Representation

**Problem:** Store graph with 1000 nodes and 5000 edges.

#### Approach 1: Adjacency Matrix

```java
// Matrix representation - simple but space-inefficient for sparse graphs
public class GraphMatrix {
    private int[][] matrix;
    private int V;

    public GraphMatrix(int vertices) {
        this.V = vertices;
        this.matrix = new int[V][V];
    }

    public void addEdge(int src, int dst) {
        matrix[src][dst] = 1;  // O(1) to add
    }

    public boolean hasEdge(int src, int dst) {
        return matrix[src][dst] == 1;  // O(1) to check
    }

    public List<Integer> getNeighbors(int node) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (matrix[node][i] == 1) {
                neighbors.add(i);
            }
        }
        return neighbors;  // O(V) to get all neighbors
    }
}
```

**Analysis:**

- Space: O(V²) = O(1,000,000) for 1000 nodes
- Add edge: O(1)
- Check edge: O(1)
- Get neighbors: O(V)
- Memory usage: 1000 × 1000 = 1,000,000 integers ≈ 4 MB

#### Approach 2: Adjacency List

```java
// List representation - space-efficient for sparse graphs
public class GraphList {
    private Map<Integer, List<Integer>> adjList;

    public GraphList() {
        this.adjList = new HashMap<>();
    }

    public void addEdge(int src, int dst) {
        adjList.computeIfAbsent(src, k -> new ArrayList<>()).add(dst);  // O(1) average
    }

    public boolean hasEdge(int src, int dst) {
        return adjList.getOrDefault(src, new ArrayList<>()).contains(dst);  // O(degree)
    }

    public List<Integer> getNeighbors(int node) {
        return adjList.getOrDefault(node, new ArrayList<>());  // O(1) to get list
    }
}
```

**Analysis:**

- Space: O(V + E) = O(1000 + 5000) = O(6000)
- Add edge: O(1) average
- Check edge: O(degree of node)
- Get neighbors: O(1)
- Memory usage: ~6000 integers ≈ 24 KB

#### Space Comparison

| Graph Type | Adjacency Matrix | Adjacency List | Better Choice |
|------------|------------------|----------------|---------------|
| Dense (V=1000, E=500,000) | 1M ints (4 MB) | 501K ints (2 MB) | Matrix (similar) |
| Sparse (V=1000, E=5000) | 1M ints (4 MB) | 6K ints (24 KB) | **List (167x less)** |
| Very Sparse (V=1000, E=100) | 1M ints (4 MB) | 1.1K ints (4 KB) | **List (1000x less)** |

**Your calculation:** For V = 5000 nodes and E = 10,000 edges, adjacency list uses _____ less space than matrix.

#### When to Use Each?

**Use Adjacency Matrix when:**

- Graph is dense (E ≈ V²)
- Need O(1) edge existence checks frequently
- All operations need to be simple array lookups

**Use Adjacency List when:**

- Graph is sparse (E << V²)
- Need to iterate through neighbors frequently
- Memory is limited

**After implementing, explain in your own words:**

- _[Why does sparse vs dense matter?]_
- _[What operations are faster with each representation?]_

---

### Example 3: Dijkstra vs BFS

**Problem:** Find shortest path in weighted graph from A to D.

Graph: A→B(weight=1), A→C(weight=4), B→D(weight=5), C→D(weight=1)

#### Approach 1: BFS (Wrong for Weighted Graphs)

```java
// BFS finds shortest in terms of edges, not weights
public static int shortestPath_BFS(Map<Integer, List<int[]>> graph, int start, int end) {
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    queue.offer(start);
    visited.add(start);
    int edges = 0;

    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            int node = queue.poll();
            if (node == end) return edges;  // Returns 2 (A→B→D)

            for (int[] neighbor : graph.getOrDefault(node, new ArrayList<>())) {
                if (!visited.contains(neighbor[0])) {
                    queue.offer(neighbor[0]);
                    visited.add(neighbor[0]);
                }
            }
        }
        edges++;
    }
    return -1;
}
// Result: 2 edges (A→B→D), but total weight = 1+5 = 6
```

**Analysis:**

- Finds: A → B → D (2 edges, weight = 6)
- Misses: A → C → D (2 edges, weight = 5) - **optimal path!**

- Problem: Doesn't consider edge weights at all

#### Approach 2: Dijkstra (Correct for Weighted Graphs)

```java
// Dijkstra finds shortest in terms of total weight
public static int shortestPath_Dijkstra(Map<Integer, List<int[]>> graph, int start, int end) {
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]); // [distance, node]
    Map<Integer, Integer> dist = new HashMap<>();

    pq.offer(new int[]{0, start});
    dist.put(start, 0);

    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int d = curr[0], node = curr[1];

        if (node == end) return d;  // Returns 5 (A→C→D)
        if (d > dist.getOrDefault(node, Integer.MAX_VALUE)) continue;

        for (int[] neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            int next = neighbor[0], weight = neighbor[1];
            int newDist = d + weight;

            if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
                dist.put(next, newDist);
                pq.offer(new int[]{newDist, next});
            }
        }
    }
    return -1;
}
// Result: A→C→D, total weight = 4+1 = 5 (optimal!)
```

**Analysis:**

- Time: O((V + E) log V) - priority queue operations
- Finds: A → C → D (weight = 5)
- Correct: Considers cumulative weights, not edge count

#### Algorithm Comparison

| Scenario | BFS Result | Dijkstra Result | Correct? |
|----------|-----------|-----------------|----------|
| Unweighted graph | Shortest path | Shortest path | Both ✓ |
| Weighted (all weights = 1) | Shortest path | Shortest path | Both ✓ |
| Weighted (varying weights) | **Wrong** (min edges) | Shortest path | Dijkstra ✓ |
| Negative weights | Wrong | **Wrong** | Neither (use Bellman-Ford) |

**Your analysis:** When all edge weights are equal, BFS is _____ than Dijkstra because _____.

**After implementing, explain in your own words:**

- _[Why does BFS fail on weighted graphs?]_
- _[What does the priority queue do in Dijkstra?]_
- _[When is BFS actually better than Dijkstra?]_

---

## Core Implementation

### Pattern 1: DFS (Depth-First Search)

**Concept:** Explore as far as possible along each branch before backtracking.

**Use case:** Detect cycles, find paths, connected components, backtracking problems.

```java
import java.util.*;

public class DFS {

    /**
     * Problem: Number of islands (connected components)
     * Time: O(m*n), Space: O(m*n) for recursion stack
     *
     * TODO: Implement DFS to count islands
     */
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int count = 0;

        // TODO: For each cell in grid:
        //   If cell is '1' (land) and not visited:
        //     Increment count
        //     Call dfs(grid, i, j) to mark entire island

        return 0; // Replace with implementation
    }

    private static void dfs(char[][] grid, int i, int j) {
        // TODO: Base cases
        //   If out of bounds: return
        //   If water ('0'): return

        // TODO: Mark current cell as visited (change '1' to '0' or use visited set)
        //   grid[i][j] = '0';

        // TODO: Recursively visit 4 neighbors
        //   dfs(grid, i+1, j);  // down
        //   dfs(grid, i-1, j);  // up
        //   dfs(grid, i, j+1);  // right
        //   dfs(grid, i, j-1);  // left
    }

    /**
     * Problem: Has path in graph (adjacency list)
     * Time: O(V + E), Space: O(V)
     *
     * TODO: Implement DFS path finding
     */
    public static boolean hasPath(Map<Integer, List<Integer>> graph, int start, int end) {
        Set<Integer> visited = new HashSet<>();

        // TODO: Call recursive DFS helper
        return false; // Replace
    }

    private static boolean dfsPath(Map<Integer, List<Integer>> graph,
                                   int current, int target, Set<Integer> visited) {
        // TODO: If current == target, return true

        // TODO: If visited contains current, return false

        // TODO: Mark current as visited

        // TODO: For each neighbor of current:
        //   If dfsPath(graph, neighbor, target, visited) returns true:
        //     Return true

        // TODO: Return false if no path found

        return false; // Replace
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class DFSClient {

    public static void main(String[] args) {
        System.out.println("=== DFS Pattern ===\n");

        // Test 1: Number of islands
        System.out.println("--- Test 1: Number of Islands ---");
        char[][] grid1 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };

        System.out.println("Grid:");
        for (char[] row : grid1) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("Islands: " + DFS.numIslands(grid1));

        // Test 2: Has path
        System.out.println("\n--- Test 2: Has Path ---");
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(3));
        graph.put(2, Arrays.asList(3));
        graph.put(3, Arrays.asList());

        System.out.println("Graph: " + graph);
        System.out.println("Path 0 -> 3? " + DFS.hasPath(graph, 0, 3));
        System.out.println("Path 0 -> 4? " + DFS.hasPath(graph, 0, 4));
    }
}
```

---

### Pattern 2: BFS (Breadth-First Search)

**Concept:** Explore all neighbors at current depth before moving deeper.

**Use case:** Shortest path in unweighted graph, level-order traversal, minimum steps.

```java
import java.util.*;

public class BFS {

    /**
     * Problem: Shortest path in unweighted graph
     * Time: O(V + E), Space: O(V)
     *
     * TODO: Implement BFS shortest path
     */
    public static int shortestPath(Map<Integer, List<Integer>> graph, int start, int end) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        int steps = 0;

        // TODO: Add start to queue and visited
        //   queue.offer(start);
        //   visited.add(start);

        // TODO: While queue not empty:
        //   int size = queue.size();
        //   For each node in current level:
        //     int node = queue.poll();
        //     If node == end, return steps
        //     For each neighbor:
        //       If not visited:
        //         Add to queue and visited
        //   Increment steps

        return -1; // Not found
    }

    /**
     * Problem: Minimum knight moves to reach target
     * Time: O(n^2), Space: O(n^2)
     *
     * TODO: Implement BFS for chess board
     */
    public static int minKnightMoves(int targetX, int targetY) {
        int[][] directions = {{2,1}, {2,-1}, {-2,1}, {-2,-1},
                              {1,2}, {1,-2}, {-1,2}, {-1,-2}};

        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // TODO: Start from (0, 0)
        //   queue.offer(new int[]{0, 0});
        //   visited.add("0,0");

        int moves = 0;

        // TODO: While queue not empty:
        //   Process all positions at current distance
        //   For each position:
        //     If reached target, return moves
        //     Try all 8 knight moves
        //     Add unvisited positions to queue
        //   Increment moves

        return -1; // Replace
    }

    /**
     * Problem: Rotting oranges (multi-source BFS)
     * Time: O(m*n), Space: O(m*n)
     *
     * TODO: Implement multi-source BFS
     */
    public static int orangesRotting(int[][] grid) {
        Queue<int[]> queue = new LinkedList<>();
        int fresh = 0;
        int minutes = 0;

        // TODO: Count fresh oranges and add rotten to queue
        //   For each cell in grid:
        //     If grid[i][j] == 1, increment fresh
        //     If grid[i][j] == 2, add (i, j) to queue

        // TODO: BFS to rot adjacent oranges
        //   While queue not empty and fresh > 0:
        //     Process all rotten at current minute
        //     For each rotten, check 4 neighbors
        //     If neighbor is fresh, rot it and add to queue
        //     Increment minutes

        // TODO: Return minutes if all fresh rotted, else -1

        return 0; // Replace
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class BFSClient {

    public static void main(String[] args) {
        System.out.println("=== BFS Pattern ===\n");

        // Test 1: Shortest path
        System.out.println("--- Test 1: Shortest Path ---");
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(3));
        graph.put(2, Arrays.asList(3, 4));
        graph.put(3, Arrays.asList(4));
        graph.put(4, Arrays.asList());

        System.out.println("Graph: " + graph);
        System.out.println("Shortest path 0 -> 4: " + BFS.shortestPath(graph, 0, 4));

        // Test 2: Knight moves
        System.out.println("\n--- Test 2: Knight Moves ---");
        int[][] targets = {{2, 1}, {5, 5}};
        for (int[] target : targets) {
            int moves = BFS.minKnightMoves(target[0], target[1]);
            System.out.printf("To (%d, %d): %d moves%n", target[0], target[1], moves);
        }

        // Test 3: Rotting oranges
        System.out.println("\n--- Test 3: Rotting Oranges ---");
        int[][] grid = {
            {2, 1, 1},
            {1, 1, 0},
            {0, 1, 1}
        };

        System.out.println("Grid:");
        for (int[] row : grid) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("Minutes to rot all: " + BFS.orangesRotting(grid));
    }
}
```

---

### Pattern 3: Topological Sort

**Concept:** Linear ordering of vertices in a Directed Acyclic Graph (DAG).

**Use case:** Course schedule, task dependencies, build systems.

```java
import java.util.*;

public class TopologicalSort {

    /**
     * Problem: Course schedule (can finish all courses)
     * Time: O(V + E), Space: O(V + E)
     *
     * TODO: Implement using Kahn's algorithm (BFS)
     */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // Build adjacency list and in-degree array
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        // TODO: Initialize graph
        //   for (int i = 0; i < numCourses; i++)
        //     graph.add(new ArrayList<>());

        // TODO: Build graph from prerequisites
        //   For each [course, prereq]:
        //     graph.get(prereq).add(course);
        //     inDegree[course]++;

        // TODO: Add all courses with inDegree 0 to queue
        //   Queue<Integer> queue = new LinkedList<>();
        //   for (int i = 0; i < numCourses; i++)
        //     if (inDegree[i] == 0) queue.offer(i);

        int completed = 0;

        // TODO: Process courses in topological order
        //   While queue not empty:
        //     int course = queue.poll();
        //     completed++;
        //     For each dependent course:
        //       Decrement inDegree
        //       If inDegree becomes 0, add to queue

        // TODO: Return true if completed == numCourses

        return false; // Replace
    }

    /**
     * Problem: Course schedule II (return order)
     * Time: O(V + E), Space: O(V + E)
     *
     * TODO: Implement with DFS
     */
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        int[] visited = new int[numCourses]; // 0: unvisited, 1: visiting, 2: visited
        Stack<Integer> stack = new Stack<>();

        // TODO: Initialize graph
        //   for (int i = 0; i < numCourses; i++)
        //     graph.add(new ArrayList<>());

        // TODO: Build graph
        //   For each [course, prereq]:
        //     graph.get(prereq).add(course);

        // TODO: DFS from each unvisited node
        //   For each course:
        //     If dfs returns false (cycle detected):
        //       Return empty array

        // TODO: Build result from stack (reverse order)

        return new int[0]; // Replace
    }

    private static boolean dfsTopSort(List<List<Integer>> graph, int node,
                                     int[] visited, Stack<Integer> stack) {
        // TODO: If node is being visited (1), cycle detected, return false

        // TODO: If node already visited (2), return true

        // TODO: Mark as being visited
        //   visited[node] = 1;

        // TODO: Visit all neighbors
        //   For each neighbor:
        //     If dfs fails, return false

        // TODO: Mark as visited and push to stack
        //   visited[node] = 2;
        //   stack.push(node);

        return true; // Replace
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class TopologicalSortClient {

    public static void main(String[] args) {
        System.out.println("=== Topological Sort ===\n");

        // Test 1: Can finish courses
        System.out.println("--- Test 1: Can Finish Courses ---");
        int[][] prereq1 = {{1, 0}};
        int[][] prereq2 = {{1, 0}, {0, 1}};

        System.out.println("2 courses, [[1,0]]: " +
            TopologicalSort.canFinish(2, prereq1));
        System.out.println("2 courses, [[1,0],[0,1]]: " +
            TopologicalSort.canFinish(2, prereq2));

        // Test 2: Find course order
        System.out.println("\n--- Test 2: Course Order ---");
        int[][] prereq3 = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        int[] order = TopologicalSort.findOrder(4, prereq3);
        System.out.println("4 courses: " + Arrays.toString(order));
    }
}
```

---

### Pattern 4: Cycle Detection

**Concept:** Detect if graph contains a cycle.

**Use case:** Validate DAG, detect deadlocks, dependency resolution.

```java
import java.util.*;

public class CycleDetection {

    /**
     * Problem: Detect cycle in directed graph
     * Time: O(V + E), Space: O(V)
     *
     * TODO: Implement using DFS with states
     */
    public static boolean hasCycleDirected(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        int[] state = new int[n]; // 0: unvisited, 1: visiting, 2: visited

        // TODO: Build graph

        // TODO: DFS from each unvisited node
        //   For each node:
        //     If dfs detects cycle, return true

        return false; // Replace
    }

    private static boolean dfsCycleDirected(List<List<Integer>> graph, int node, int[] state) {
        // TODO: If state[node] == 1 (visiting), cycle found, return true

        // TODO: If state[node] == 2 (visited), no cycle from here, return false

        // TODO: Mark as visiting
        //   state[node] = 1;

        // TODO: Visit all neighbors
        //   For each neighbor:
        //     If dfs finds cycle, return true

        // TODO: Mark as visited
        //   state[node] = 2;

        return false; // Replace
    }

    /**
     * Problem: Detect cycle in undirected graph
     * Time: O(V + E), Space: O(V)
     *
     * TODO: Implement using DFS with parent tracking
     */
    public static boolean hasCycleUndirected(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        boolean[] visited = new boolean[n];

        // TODO: Build graph (both directions for undirected)

        // TODO: DFS from each unvisited component
        //   For each node:
        //     If not visited and dfs finds cycle, return true

        return false; // Replace
    }

    private static boolean dfsCycleUndirected(List<List<Integer>> graph, int node,
                                             int parent, boolean[] visited) {
        // TODO: Mark as visited

        // TODO: Visit all neighbors
        //   For each neighbor:
        //     If neighbor == parent, skip (came from here)
        //     If neighbor is visited, cycle found, return true
        //     If dfs from neighbor finds cycle, return true

        return false; // Replace
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class CycleDetectionClient {

    public static void main(String[] args) {
        System.out.println("=== Cycle Detection ===\n");

        // Test 1: Directed graph cycle
        System.out.println("--- Test 1: Directed Graph ---");
        int[][] edges1 = {{0, 1}, {1, 2}};
        int[][] edges2 = {{0, 1}, {1, 2}, {2, 0}};

        System.out.println("Edges [[0,1],[1,2]]: " +
            CycleDetection.hasCycleDirected(3, edges1));
        System.out.println("Edges [[0,1],[1,2],[2,0]]: " +
            CycleDetection.hasCycleDirected(3, edges2));

        // Test 2: Undirected graph cycle
        System.out.println("\n--- Test 2: Undirected Graph ---");
        int[][] edges3 = {{0, 1}, {1, 2}};
        int[][] edges4 = {{0, 1}, {1, 2}, {2, 0}};

        System.out.println("Edges [[0,1],[1,2]]: " +
            CycleDetection.hasCycleUndirected(3, edges3));
        System.out.println("Edges [[0,1],[1,2],[2,0]]: " +
            CycleDetection.hasCycleUndirected(3, edges4));
    }
}
```

---

### Pattern 5: Dijkstra's Algorithm (Single-Source Shortest Path)

**Concept:** Find shortest paths from a source node to all other nodes in a weighted graph with non-negative edge weights.

**Use case:** Network routing, GPS navigation, shortest path in weighted graphs.

```java
import java.util.*;

public class DijkstraAlgorithm {

    /**
     * Problem: Network delay time - time for all nodes to receive signal from source
     * Time: O((V + E) log V), Space: O(V)
     *
     * TODO: Implement Dijkstra's algorithm using PriorityQueue
     */
    public static int networkDelayTime(int[][] times, int n, int k) {
        // Build adjacency list: node -> List of [neighbor, weight]
        Map<Integer, List<int[]>> graph = new HashMap<>();

        // TODO: Build graph from times array
        //   for (int[] time : times) {
        //     int src = time[0], dst = time[1], weight = time[2];
        //     graph.computeIfAbsent(src, x -> new ArrayList<>()).add(new int[]{dst, weight});
        //   }

        // TODO: Initialize distances array
        //   int[] dist = new int[n + 1];
        //   Arrays.fill(dist, Integer.MAX_VALUE);
        //   dist[k] = 0;

        // TODO: Use PriorityQueue (min-heap) to track [distance, node]
        //   PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        //   pq.offer(new int[]{0, k});

        // TODO: Process nodes in order of distance
        //   While pq not empty:
        //     int[] curr = pq.poll();
        //     int d = curr[0], node = curr[1];
        //
        //     If d > dist[node], skip (already processed)
        //
        //     For each neighbor of node:
        //       int newDist = dist[node] + weight;
        //       If newDist < dist[neighbor]:
        //         dist[neighbor] = newDist;
        //         pq.offer(new int[]{newDist, neighbor});

        // TODO: Find maximum distance (time for all nodes to receive signal)
        //   int maxTime = 0;
        //   for (int i = 1; i <= n; i++) {
        //     if (dist[i] == Integer.MAX_VALUE) return -1;
        //     maxTime = Math.max(maxTime, dist[i]);
        //   }
        //   return maxTime;

        return -1; // Replace
    }

    /**
     * Problem: Shortest path in weighted graph
     * Time: O((V + E) log V), Space: O(V)
     *
     * TODO: Implement basic Dijkstra's algorithm
     */
    public static int[] dijkstra(Map<Integer, List<int[]>> graph, int start, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        // TODO: Use PriorityQueue for efficient minimum extraction
        //   PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        //   pq.offer(new int[]{0, start});

        // TODO: Process each node once
        //   Set<Integer> visited = new HashSet<>();
        //
        //   While pq not empty:
        //     int[] curr = pq.poll();
        //     int d = curr[0], node = curr[1];
        //
        //     If visited contains node, skip
        //     visited.add(node);
        //
        //     For each [neighbor, weight] in graph.get(node):
        //       int newDist = dist[node] + weight;
        //       If newDist < dist[neighbor]:
        //         dist[neighbor] = newDist;
        //         pq.offer(new int[]{newDist, neighbor});

        return dist; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class DijkstraClient {

    public static void main(String[] args) {
        System.out.println("=== Dijkstra's Algorithm ===\n");

        // Test 1: Network delay time
        System.out.println("--- Test 1: Network Delay Time ---");
        int[][] times1 = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        int n1 = 4, k1 = 2;
        System.out.println("Network: " + Arrays.deepToString(times1));
        System.out.println("Start node: " + k1);
        System.out.println("Time to reach all nodes: " +
            DijkstraAlgorithm.networkDelayTime(times1, n1, k1));

        // Test 2: Shortest paths
        System.out.println("\n--- Test 2: Shortest Paths ---");
        Map<Integer, List<int[]>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(new int[]{1, 4}, new int[]{2, 1}));
        graph.put(1, Arrays.asList(new int[]{3, 1}));
        graph.put(2, Arrays.asList(new int[]{1, 2}, new int[]{3, 5}));
        graph.put(3, Arrays.asList());

        int[] distances = DijkstraAlgorithm.dijkstra(graph, 0, 4);
        System.out.println("Graph: " + graph);
        System.out.println("Shortest distances from node 0: " + Arrays.toString(distances));
    }
}
```

---

### Pattern 6: Minimum Spanning Tree (MST)

**Concept:** Find the minimum cost subset of edges that connects all vertices in a weighted undirected graph.

**Use case:** Network design, clustering, approximation algorithms.

```java
import java.util.*;

public class MinimumSpanningTree {

    /**
     * Problem: Min cost to connect all points (Prim's algorithm)
     * Time: O((V + E) log V), Space: O(V + E)
     *
     * TODO: Implement Prim's algorithm using PriorityQueue
     */
    public static int minCostConnectPoints(int[][] points) {
        int n = points.length;

        // TODO: Build adjacency list with Manhattan distances
        //   Map<Integer, List<int[]>> graph = new HashMap<>();
        //   for (int i = 0; i < n; i++) {
        //     graph.put(i, new ArrayList<>());
        //     for (int j = 0; j < n; j++) {
        //       if (i != j) {
        //         int dist = Math.abs(points[i][0] - points[j][0]) +
        //                    Math.abs(points[i][1] - points[j][1]);
        //         graph.get(i).add(new int[]{j, dist});
        //       }
        //     }
        //   }

        // TODO: Use PriorityQueue to track [cost, node]
        //   PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        //   pq.offer(new int[]{0, 0}); // Start from node 0 with cost 0

        // TODO: Track visited nodes and total cost
        //   Set<Integer> visited = new HashSet<>();
        //   int totalCost = 0;

        // TODO: Greedily add minimum cost edges
        //   While visited.size() < n:
        //     int[] curr = pq.poll();
        //     int cost = curr[0], node = curr[1];
        //
        //     If visited contains node, skip
        //
        //     visited.add(node);
        //     totalCost += cost;
        //
        //     For each [neighbor, edgeCost] in graph.get(node):
        //       If neighbor not visited:
        //         pq.offer(new int[]{edgeCost, neighbor});

        return 0; // Replace
    }

    /**
     * Problem: MST using Prim's algorithm (generic version)
     * Time: O((V + E) log V), Space: O(V + E)
     *
     * TODO: Implement Prim's algorithm
     */
    public static int primMST(Map<Integer, List<int[]>> graph, int n) {
        // TODO: Use PriorityQueue for minimum edge selection
        //   PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        // TODO: Start from node 0
        //   Set<Integer> visited = new HashSet<>();
        //   int totalCost = 0;
        //
        //   Add all edges from node 0 to pq
        //   visited.add(0);

        // TODO: Process edges in order of weight
        //   While visited.size() < n and pq not empty:
        //     int[] edge = pq.poll();
        //     int cost = edge[0], node = edge[1];
        //
        //     If visited contains node, skip
        //
        //     visited.add(node);
        //     totalCost += cost;
        //
        //     Add all edges from node to pq (if neighbor not visited)

        return 0; // Replace
    }

    /**
     * Edge class for MST algorithms
     */
    static class Edge implements Comparable<Edge> {
        int src, dst, weight;

        Edge(int src, int dst, int weight) {
            this.src = src;
            this.dst = dst;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class MinimumSpanningTreeClient {

    public static void main(String[] args) {
        System.out.println("=== Minimum Spanning Tree ===\n");

        // Test 1: Min cost to connect points
        System.out.println("--- Test 1: Connect All Points ---");
        int[][] points1 = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        System.out.println("Points: " + Arrays.deepToString(points1));
        System.out.println("Min cost: " +
            MinimumSpanningTree.minCostConnectPoints(points1));

        // Test 2: Simple MST
        System.out.println("\n--- Test 2: MST (Prim's Algorithm) ---");
        int[][] points2 = {{0, 0}, {1, 1}, {1, 0}, {0, 1}};
        System.out.println("Points: " + Arrays.deepToString(points2));
        System.out.println("Min cost: " +
            MinimumSpanningTree.minCostConnectPoints(points2));

        // Test 3: Graph with weighted edges
        System.out.println("\n--- Test 3: Weighted Graph MST ---");
        Map<Integer, List<int[]>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(new int[]{1, 2}, new int[]{2, 3}));
        graph.put(1, Arrays.asList(new int[]{0, 2}, new int[]{2, 1}, new int[]{3, 5}));
        graph.put(2, Arrays.asList(new int[]{0, 3}, new int[]{1, 1}, new int[]{3, 4}));
        graph.put(3, Arrays.asList(new int[]{1, 5}, new int[]{2, 4}));

        System.out.println("Graph: " + graph);
        System.out.println("MST cost: " + MinimumSpanningTree.primMST(graph, 4));
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken graph implementations. This tests your understanding.

### Challenge 1: Broken DFS - Visited Array Bug

```java
/**
 * This code is supposed to count connected components using DFS.
 * It has 2 BUGS. Find them!
 */
public static int countComponents_Buggy(int n, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();

    // Build graph
    for (int i = 0; i < n; i++) {
        graph.put(i, new ArrayList<>());
    }
    for (int[] edge : edges) {
        graph.get(edge[0]).add(edge[1]);
        graph.get(edge[1]).add(edge[0]);
    }

    boolean[] visited = new boolean[n];
    int count = 0;

    for (int i = 0; i < n; i++) {
        if (!visited[i]) {
            dfs(graph, i, visited);  // BUG 1: What's missing here?
        }
    }

    return count;  // BUG 2: What's wrong with the count?
}

private static void dfs(Map<Integer, List<Integer>> graph, int node, boolean[] visited) {
    visited[node] = true;

    for (int neighbor : graph.get(node)) {
        if (!visited[neighbor]) {
            dfs(graph, neighbor, visited);
        }
    }
}
```

**Your debugging:**

- **Bug 1 location:** _[Which line?]_
- **Bug 1 explanation:** _[What's the problem?]_
- **Bug 1 fix:** _[What should it be?]_

- **Bug 2 location:** _[Which line?]_
- **Bug 2 explanation:** _[Why is count always 0?]_
- **Bug 2 fix:** _[How to fix?]_

**Test case:**

- Input: n = 5, edges = [[0,1], [1,2], [3,4]]
- Expected: 2 components
- Actual with buggy code: _[What do you get?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 18):** After calling `dfs()`, we never increment `count`! Should be:
```java
if (!visited[i]) {
    dfs(graph, i, visited);
    count++;  // Increment after exploring component
}
```

**Bug 2:** Actually the same as Bug 1. The `count` variable is initialized but never incremented, so it always returns 0.

**Correct fix:**
```java
for (int i = 0; i < n; i++) {
    if (!visited[i]) {
        dfs(graph, i, visited);
        count++;
    }
}
return count;
```
</details>

---

### Challenge 2: Broken BFS - Level Tracking Bug

```java
/**
 * Find shortest path length using BFS.
 * This has 1 CRITICAL BUG that causes wrong results.
 */
public static int shortestPath_Buggy(Map<Integer, List<Integer>> graph, int start, int end) {
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();
    int distance = 0;

    queue.offer(start);
    // BUG: Missing something crucial here!

    while (!queue.isEmpty()) {
        int node = queue.poll();  // BUG: Wrong level processing!

        if (node == end) return distance;

        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                queue.offer(neighbor);
                visited.add(neighbor);
            }
        }

        distance++;
    }

    return -1;
}
```

**Your debugging:**

- **Bug 1:** _[What's missing after queue.offer(start)?]_
- **Bug 2:** _[How should we process the queue by level?]_
- **Why it fails:** _[Trace through with start=0, end=2, graph: 0→1, 1→2]_

**Expected distance:** 2
**Actual with buggy code:** _[What do you get?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug 1:** Missing `visited.add(start)` after adding start to queue. Without this, we might revisit the start node.

**Bug 2:** Not processing nodes level-by-level. Should use:
```java
while (!queue.isEmpty()) {
    int size = queue.size();  // Process all nodes at current level

    for (int i = 0; i < size; i++) {
        int node = queue.poll();

        if (node == end) return distance;

        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                queue.offer(neighbor);
                visited.add(neighbor);
            }
        }
    }

    distance++;  // Increment after processing entire level
}
```

**Why:** Without level-by-level processing, `distance` increments for every node polled, not for each level.
</details>

---

### Challenge 3: Broken Topological Sort - Cycle Detection Miss

```java
/**
 * Topological sort using DFS.
 * This has 1 SUBTLE BUG that causes incorrect cycle detection.
 */
public static List<Integer> topSort_Buggy(int n, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    for (int i = 0; i < n; i++) graph.put(i, new ArrayList<>());
    for (int[] edge : edges) graph.get(edge[0]).add(edge[1]);

    int[] visited = new int[n];  // 0: unvisited, 1: visiting, 2: visited
    Stack<Integer> stack = new Stack<>();

    for (int i = 0; i < n; i++) {
        if (visited[i] == 0) {
            if (!dfsTopSort(graph, i, visited, stack)) {
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

private static boolean dfsTopSort(Map<Integer, List<Integer>> graph, int node,
                                 int[] visited, Stack<Integer> stack) {
    visited[node] = 1;  // Mark as visiting

    for (int neighbor : graph.get(node)) {
        if (visited[neighbor] == 1) {
            return false;  // Cycle detected
        }
        // BUG: What's missing for unvisited neighbors?
        if (visited[neighbor] == 0) {
            dfsTopSort(graph, neighbor, visited, stack);
        }
    }

    visited[node] = 2;  // Mark as visited
    stack.push(node);
    return true;
}
```

**Your debugging:**

- **Bug location:** _[Which line in dfsTopSort?]_
- **Bug explanation:** _[What happens if recursive call detects cycle?]_
- **Bug fix:** _[What code is missing?]_

**Test case with cycle:**

- Input: n = 3, edges = [[0,1], [1,2], [2,0]]
- Expected: Empty list (cycle exists)
- Actual with buggy code: _[Does it detect the cycle?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug (Line 38):** Missing return value check! Should be:
```java
if (visited[neighbor] == 0) {
    if (!dfsTopSort(graph, neighbor, visited, stack)) {
        return false;  // Propagate cycle detection
    }
}
```

**Why:** Even if a recursive call detects a cycle (returns false), we ignore it and continue. The cycle detection never propagates back up the call stack.

**Correct version:**
```java
for (int neighbor : graph.get(node)) {
    if (visited[neighbor] == 1) {
        return false;  // Cycle detected
    }
    if (visited[neighbor] == 0) {
        if (!dfsTopSort(graph, neighbor, visited, stack)) {
            return false;  // Propagate cycle detection
        }
    }
}
```
</details>

---

### Challenge 4: Broken Dijkstra - Priority Queue Bug

```java
/**
 * Dijkstra's algorithm for shortest path.
 * This has 1 PERFORMANCE BUG that causes incorrect results.
 */
public static int dijkstra_Buggy(Map<Integer, List<int[]>> graph, int start, int end, int n) {
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[start] = 0;

    pq.offer(new int[]{0, start});

    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int d = curr[0], node = curr[1];

        // BUG: Missing a critical optimization check here!

        if (node == end) return d;

        for (int[] neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            int next = neighbor[0], weight = neighbor[1];
            int newDist = d + weight;

            if (newDist < dist[next]) {
                dist[next] = newDist;
                pq.offer(new int[]{newDist, next});
            }
        }
    }

    return dist[end] == Integer.MAX_VALUE ? -1 : dist[end];
}
```

**Your debugging:**

- **Bug:** _[What check is missing after polling from pq?]_
- **Why it matters:** _[What happens without this check?]_
- **Performance impact:** _[How does this affect time complexity?]_

**Test case:**

- Graph: 0→1(weight=5), 0→1(weight=3), 1→2(weight=1)
- Without fix: _[How many times do we process node 1?]_
- With fix: _[How many times should we process node 1?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Missing distance check after polling. Should be:
```java
while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int d = curr[0], node = curr[1];

    // Skip if we've already found a better path to this node
    if (d > dist[node]) continue;

    if (node == end) return d;

    // ... rest of code
}
```

**Why:** We may add the same node to the priority queue multiple times with different distances. Without this check, we process outdated entries, doing unnecessary work and potentially getting wrong results.

**Example trace without fix:**
```
Step 1: Process (0, dist=0), add (1, dist=5)
Step 2: Process (1, dist=5), find shorter path, add (1, dist=3)
Step 3: Process (1, dist=3) ✓ (correct)
Step 4: Process (1, dist=5) ✗ (should skip - outdated)
```

With fix, step 4 would be skipped because d=5 > dist[1]=3.
</details>

---

### Challenge 5: Wrong Adjacency Representation

```java
/**
 * Build graph from edge list for undirected graph.
 * This has 1 LOGIC BUG.
 */
public static Map<Integer, List<Integer>> buildGraph_Buggy(int n, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();

    for (int i = 0; i < n; i++) {
        graph.put(i, new ArrayList<>());
    }

    for (int[] edge : edges) {
        int u = edge[0], v = edge[1];
        graph.get(u).add(v);  // BUG: Is this enough for undirected graph?
    }

    return graph;
}
```

**Your debugging:**

- **Bug:** _[What's missing for undirected graphs?]_
- **Test case:** edges = [[0,1], [1,2]], try to find path from 2 to 0
- **What happens:** _[Can you find the path with buggy graph?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** For undirected graphs, need to add edge in BOTH directions:
```java
for (int[] edge : edges) {
    int u = edge[0], v = edge[1];
    graph.get(u).add(v);
    graph.get(v).add(u);  // Add reverse edge!
}
```

**Why:** In an undirected graph, edge (u,v) means both u→v and v→u. Without the reverse edge, the graph is incorrectly treated as directed.
</details>

---

### Challenge 6: MST Prim's Algorithm Bug

```java
/**
 * Prim's algorithm for Minimum Spanning Tree.
 * This has 1 SUBTLE BUG that causes incorrect MST cost.
 */
public static int primMST_Buggy(Map<Integer, List<int[]>> graph, int n) {
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    Set<Integer> visited = new HashSet<>();
    int totalCost = 0;

    pq.offer(new int[]{0, 0});  // Start from node 0 with cost 0

    while (!visited.isEmpty() && visited.size() < n) {  // BUG 1: Wrong condition!
        int[] curr = pq.poll();
        int cost = curr[0], node = curr[1];

        // BUG 2: Missing visited check here!

        visited.add(node);
        totalCost += cost;

        for (int[] neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            int next = neighbor[0], edgeCost = neighbor[1];
            if (!visited.contains(next)) {
                pq.offer(new int[]{edgeCost, next});
            }
        }
    }

    return totalCost;
}
```

**Your debugging:**

- **Bug 1:** _[What's wrong with the while loop condition?]_
- **Bug 2:** _[What check is missing after polling?]_
- **Result:** _[What happens if we process same node twice?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug 1 (Line 7):** Condition `!visited.isEmpty()` is wrong. Should be `!pq.isEmpty()`:
```java
while (!pq.isEmpty() && visited.size() < n) {
```

**Bug 2 (Line 10):** Need to check if node was already visited before adding to MST:
```java
int[] curr = pq.poll();
int cost = curr[0], node = curr[1];

if (visited.contains(node)) continue;  // Skip if already in MST

visited.add(node);
totalCost += cost;
```

**Why:** Multiple edges can lead to the same node with different costs. Without the check, we might add a node to the MST multiple times, inflating the total cost.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 8+ bugs across 6 challenges
- [ ] Understood WHY each bug causes incorrect behavior
- [ ] Could explain the fix to someone else
- [ ] Learned common graph algorithm mistakes to avoid

**Common graph mistakes you discovered:**

1. _[Forgetting to mark start node as visited in BFS]_
2. _[Not propagating cycle detection in recursive calls]_
3. _[Missing distance check in Dijkstra after polling]_
4. _[Not adding both directions for undirected graphs]_
5. _[Processing queue/priority queue incorrectly]_
6. _[Fill in more patterns you noticed]_

---

## Decision Framework

**Your task:** Build decision trees for when to use each graph algorithm.

### Question 1: DFS vs BFS - Which to use?

Answer after solving problems:

**Use DFS when:**

- Need to explore all paths: _[Backtracking, cycle detection]_
- Memory is limited: _[DFS uses less space]_
- Finding any path (not shortest): _[Fill in]_

**Use BFS when:**

- Need shortest path: _[Unweighted graphs]_
- Level-order traversal: _[Process by distance from source]_
- Multi-source problems: _[Fill in examples]_

### Question 2: When to use Topological Sort?

Answer for different scenarios:

**Use topological sort when:**

- Have dependencies: _[Course prerequisites, build order]_
- Need ordering: _[Task scheduling]_
- Graph is DAG: _[Must be acyclic]_

### Question 3: Graph representation?

**Adjacency List:**

- Use when: _[Sparse graphs, need to iterate neighbors]_
- Space: _[O(V + E)]_

**Adjacency Matrix:**

- Use when: _[Dense graphs, need to check edge existence]_
- Space: _[O(V²)]_

### Question 4: When to use Dijkstra's Algorithm?

Answer for different scenarios:

**Use Dijkstra when:**

- Need shortest path in weighted graph: _[Non-negative edge weights]_
- Single-source shortest paths: _[From one node to all others]_
- Optimal path finding: _[GPS navigation, network routing]_

**Don't use Dijkstra when:**

- Negative edge weights exist: _[Use Bellman-Ford instead]_
- Unweighted graph: _[BFS is simpler and faster]_
- All-pairs shortest paths needed: _[Consider Floyd-Warshall]_

### Question 5: When to use Minimum Spanning Tree?

Answer for different scenarios:

**Use MST when:**

- Need to connect all nodes minimally: _[Network design, clustering]_
- Minimize total edge cost: _[Infrastructure optimization]_
- Graph is undirected and weighted: _[MST only for undirected graphs]_

**MST Algorithm Choice:**

- Prim's: _[Dense graphs, good with adjacency matrix]_
- Kruskal's: _[Sparse graphs, uses Union-Find]_

### Your Decision Tree

Build this after solving practice problems:

```
Graph Algorithm Selection
│
├─ Need shortest path?
│   ├─ Unweighted → BFS ✓
│   └─ Weighted (non-negative) → Dijkstra's Algorithm ✓
│
├─ Need to explore all paths?
│   └─ Use DFS ✓
│
├─ Need ordering with dependencies?
│   └─ Topological Sort ✓
│
├─ Need to connect all nodes minimally?
│   └─ Minimum Spanning Tree (Prim's/Kruskal's) ✓
│
├─ Detect cycle?
│   ├─ Directed → DFS with states ✓
│   └─ Undirected → DFS with parent OR Union-Find ✓
│
└─ Connected components?
    ├─ Static → DFS/BFS ✓
    └─ Dynamic → Union-Find ✓
```

### The "Kill Switch" - When NOT to use

**Don't use DFS when:**

1. _[Need shortest path - use BFS instead]_
2. _[Graph has cycles and you don't track visited - infinite loop]_

**Don't use BFS when:**

1. _[Memory is very limited - DFS more space efficient]_
2. _[Need to explore all paths - DFS better for backtracking]_

**Don't use Topological Sort when:**

1. _[Graph has cycles - no valid topological ordering]_
2. _[Graph is undirected - topological sort only for DAGs]_

**Don't use Dijkstra when:**

1. _[Negative edge weights exist - use Bellman-Ford]_
2. _[Unweighted graph - BFS is simpler and O(V+E)]_
3. _[Need all-pairs shortest paths - use Floyd-Warshall]_

**Don't use MST when:**

1. _[Graph is directed - MST only for undirected graphs]_
2. _[Don't need to connect all nodes - use shortest path instead]_
3. _[Graph is disconnected - MST won't exist]_

### The Rule of Three: Alternatives

**Option 1: DFS**

- Pros: _[Less memory, natural for recursion, backtracking]_
- Cons: _[Not shortest path, stack overflow risk]_
- Use when: _[Explore paths, detect cycles, connectivity]_

**Option 2: BFS**

- Pros: _[Shortest path, level-order, no stack overflow]_
- Cons: _[More memory (queue), not natural for backtracking]_
- Use when: _[Shortest path, minimum steps, multi-source]_

**Option 3: Union-Find**

- Pros: _[Fast connectivity queries, dynamic]_
- Cons: _[Only for undirected, no path info]_
- Use when: _[Dynamic connectivity, MST, cycle detection]_

---

## Practice

### LeetCode Problems

**Easy (Complete 2-3):**

- [ ] [997. Find the Town Judge](https://leetcode.com/problems/find-the-town-judge/)
    - Pattern: _[Graph properties]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

- [ ] [1971. Find if Path Exists in Graph](https://leetcode.com/problems/find-if-path-exists-in-graph/)
    - Pattern: _[DFS/BFS]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

**Medium (Complete 4-5):**

- [ ] [200. Number of Islands](https://leetcode.com/problems/number-of-islands/)
    - Pattern: _[DFS]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [133. Clone Graph](https://leetcode.com/problems/clone-graph/)
    - Pattern: _[DFS/BFS]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [207. Course Schedule](https://leetcode.com/problems/course-schedule/)
    - Pattern: _[Topological Sort]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/)
    - Pattern: _[Topological Sort]_
    - Comparison to 207: _[How similar?]_

- [ ] [994. Rotting Oranges](https://leetcode.com/problems/rotting-oranges/)
    - Pattern: _[Multi-source BFS]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [743. Network Delay Time](https://leetcode.com/problems/network-delay-time/)
    - Pattern: _[Dijkstra's Algorithm]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [1584. Min Cost to Connect All Points](https://leetcode.com/problems/min-cost-to-connect-all-points/)
    - Pattern: _[Minimum Spanning Tree (Prim's)]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

**Hard (Optional):**

- [ ] [127. Word Ladder](https://leetcode.com/problems/word-ladder/)
    - Pattern: _[BFS]_
    - Key insight: _[Fill in after solving]_

- [ ] [329. Longest Increasing Path in Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/)
    - Pattern: _[DFS + Memoization]_
    - Key insight: _[Fill in]_

- [ ] [787. Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)
    - Pattern: _[Dijkstra's Algorithm with constraints]_
    - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] DFS: islands, path finding all work
    - [ ] BFS: shortest path, knight moves, rotting oranges all work
    - [ ] Topological sort: both algorithms work
    - [ ] Cycle detection: directed and undirected work
    - [ ] Dijkstra's Algorithm: network delay time, shortest paths work
    - [ ] MST: Prim's algorithm for connecting points works
    - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
    - [ ] Can identify when to use DFS vs BFS
    - [ ] Understand topological sort applications
    - [ ] Know how to detect cycles
    - [ ] Understand when to use Dijkstra vs BFS
    - [ ] Recognize when MST is needed
    - [ ] Understand graph representation trade-offs

- [ ] **Problem Solving**
    - [ ] Solved 2-3 easy problems
    - [ ] Solved 6-7 medium problems (including Dijkstra and MST)
    - [ ] Analyzed time/space complexity
    - [ ] Handled edge cases (empty graph, disconnected)

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Built decision tree
    - [ ] Can explain DFS vs BFS trade-offs
    - [ ] Know when NOT to use each algorithm
    - [ ] Understand Dijkstra's priority queue optimization
    - [ ] Understand MST greedy approach

- [ ] **Mastery Check**
    - [ ] Could implement all patterns from memory
    - [ ] Could recognize pattern in new problem
    - [ ] Could explain to someone else
    - [ ] Understand why each algorithm works

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about graph algorithms.

**Your explanation (write it out):**

> "Graphs are used to represent relationships between entities. The main traversal algorithms are..."
>
> _[Fill in your explanation in plain English - 4-5 sentences max]_
>
> "Use DFS when..."
>
> _[Fill in]_
>
> "Use BFS when..."
>
> _[Fill in]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by a non-technical person? _[Yes/No]_
- Did you explain when to use each algorithm? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw BFS traversal for finding shortest path, without looking at code.

**Draw the graph and BFS progression:**

```
Graph:  0 → 1 → 3
        ↓   ↓
        2 → 4

Find shortest path from 0 to 4

Level 0: [Your drawing - initial queue state]
         Queue: ___
         Visited: ___

Level 1: [After processing level 0]
         Queue: ___
         Visited: ___

Level 2: [After processing level 1]
         Queue: ___
         Visited: ___
         Found: ___
```

**Verification:**

- [ ] Drew initial state correctly
- [ ] Showed queue state at each level
- [ ] Tracked visited set accurately
- [ ] Identified when target was found

---

### Gate 3: Algorithm Selection Test

**Without looking at your notes, select the correct algorithm:**

| Problem | Algorithm | Why? |
|---------|-----------|------|
| Find shortest path (unweighted) | _[Fill in]_ | _[Explain]_ |
| Detect cycle in directed graph | _[Fill in]_ | _[Explain]_ |
| Course prerequisite ordering | _[Fill in]_ | _[Explain]_ |
| GPS navigation (weighted roads) | _[Fill in]_ | _[Explain]_ |
| Connect cities with minimum cable | _[Fill in]_ | _[Explain]_ |
| Count connected components | _[Fill in]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the decision framework and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Algorithm | Time Complexity | Space Complexity | Why? |
|-----------|----------------|------------------|------|
| DFS (recursive) | O(?) | O(?) | _[Explain]_ |
| BFS | O(?) | O(?) | _[Explain]_ |
| Topological Sort | O(?) | O(?) | _[Explain]_ |
| Dijkstra (with PQ) | O(?) | O(?) | _[Explain]_ |
| Prim's MST | O(?) | O(?) | _[Explain]_ |

**Deep question:** Why is Dijkstra O((V+E) log V) while BFS is O(V+E)?

Your answer: _[Fill in - explain the fundamental difference]_

---

### Gate 5: Representation Trade-off Decision

**Scenario:** You need to store a social network graph with 1 million users and 10 million friendships.

**Option A:** Adjacency Matrix
- Space complexity: _[Fill in with calculation]_
- Add friendship: _[Time complexity]_
- Check friendship: _[Time complexity]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option B:** Adjacency List
- Space complexity: _[Fill in with calculation]_
- Add friendship: _[Time complexity]_
- Check friendship: _[Time complexity]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Your decision:** I would choose _[A/B]_ because...

_[Fill in your reasoning - consider time, space, and operations]_

**What if:** The graph was dense with 500 million friendships?

- Would your decision change? _[Yes/No - Why?]_

---

### Gate 6: DFS vs BFS Trade-off

**Scenario:** Find if path exists from A to B in a graph.

**When to use DFS:**

- Memory constraint: _[How much memory?]_
- Path requirements: _[Any path vs shortest?]_
- Graph structure: _[Deep vs wide?]_

**When to use BFS:**

- Need shortest path: _[Why does BFS guarantee this?]_
- Level-order processing: _[Give example]_
- Memory available: _[Queue size?]_

**Your analysis:** For finding ANY path in a deep, narrow graph with limited memory, which algorithm and why?

_[Fill in your reasoning]_

---

### Gate 7: Dijkstra Debugging

**Given this buggy Dijkstra implementation, identify the bug:**

```java
public static int[] dijkstra(Map<Integer, List<int[]>> graph, int start, int n) {
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[start] = 0;

    pq.offer(new int[]{0, start});

    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int d = curr[0], node = curr[1];

        for (int[] neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            int next = neighbor[0], weight = neighbor[1];
            int newDist = d + weight;

            if (newDist < dist[next]) {
                dist[next] = newDist;
                pq.offer(new int[]{newDist, next});
            }
        }
    }

    return dist;
}
```

**Your debugging:**

- Bug location: _[Which line or section?]_
- Bug description: _[What's missing?]_
- Fix: _[What code should be added?]_
- Why it matters: _[What happens without the fix?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Missing check after polling to skip outdated entries.

**Fix:** Add after line 10:
```java
if (d > dist[node]) continue;
```

**Why:** Without this, we process the same node multiple times with different distances, wasting time and potentially computing wrong distances.
</details>

---

### Gate 8: Code from Memory (Final Test)

**Set a 15-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: BFS to find shortest path length in unweighted graph
 * Return -1 if no path exists
 */
public static int shortestPath(Map<Integer, List<Integer>> graph, int start, int end) {
    // Your implementation here









    return -1; // Replace
}
```

**After implementing, test with:**

- Graph: 0→[1,2], 1→[3], 2→[3], 3→[]
- Start: 0, End: 3
- Expected: 2

**Verification:**

- [ ] Implemented correctly without looking
- [ ] Handles edge cases (start == end, no path)
- [ ] Time complexity is O(V + E)
- [ ] Space complexity is O(V)
- [ ] Processes nodes level-by-level

---

### Gate 9: Teaching Check - When NOT to Use

**The ultimate test of understanding is teaching.**

**Task:** Explain to an imaginary person when NOT to use each algorithm.

Your explanation:

> "You should NOT use DFS when..."
>
> _[Fill in - 2-3 scenarios]_

> "You should NOT use BFS when..."
>
> _[Fill in - 2-3 scenarios]_

> "You should NOT use Dijkstra when..."
>
> _[Fill in - 2-3 scenarios]_

**Examples of failures:**

1. _[Scenario where DFS fails/isn't optimal]_
2. _[Scenario where BFS fails/isn't optimal]_
3. _[Scenario where Dijkstra fails/isn't optimal]_

---

### Gate 10: Graph Representation Quiz

**Draw both representations for this graph:**

Edges: (0,1), (0,2), (1,3), (2,3)

**Adjacency List:**
```
Your drawing:
_[Show the map/list structure]_
```

**Adjacency Matrix:**
```
Your drawing:
  0 1 2 3
0 [ ][ ][ ][ ]
1 [ ][ ][ ][ ]
2 [ ][ ][ ][ ]
3 [ ][ ][ ][ ]
```

**Analysis:**

- Space used by list: _[Calculate]_
- Space used by matrix: _[Calculate]_
- Which is better for this graph? _[Why?]_

---

### Gate 11: Topological Sort Understanding

**Question:** Can you perform topological sort on these graphs?

1. Graph with cycle: 0→1→2→0
    - Can sort? _[Yes/No]_
    - Why? _[Explain]_

2. Undirected graph: 0—1—2
    - Can sort? _[Yes/No]_
    - Why? _[Explain]_

3. DAG: 0→1, 0→2, 1→3, 2→3
    - Can sort? _[Yes/No]_
    - Possible orderings: _[List at least 2]_

**Deep question:** What property makes topological sort possible?

Your answer: _[Fill in]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement DFS and BFS from memory
- [ ] Explain when to use each graph algorithm
- [ ] Identify the correct algorithm for new problems
- [ ] Analyze time and space complexity
- [ ] Choose appropriate graph representation
- [ ] Implement Dijkstra's algorithm correctly
- [ ] Understand when to use MST algorithms
- [ ] Debug common graph algorithm mistakes
- [ ] Teach these concepts to someone else

**Self-assessment score:** ___/10

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered graph algorithms. Proceed to the next topic.
