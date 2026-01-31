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

**Next Topic:** [11. Heaps →](11-heaps.md)

**Back to:** [09. Union-Find ←](09-union-find.md)
