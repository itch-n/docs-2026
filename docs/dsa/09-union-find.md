# 15. Union-Find (Disjoint Set Union)

> Efficiently track and merge disjoint sets with near-constant time operations

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is union-find in one sentence?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

2. **What do "union" and "find" operations do?**
    - Your answer: <span class="fill-in">[Fill in after implementation]</span>

3. **Real-world analogy:**
    - Example: "Union-Find is like organizing people into groups where you can quickly check if two people are in the
      same group..."
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **When does this pattern work?**
    - Your answer: <span class="fill-in">[Fill in after solving problems]</span>

5. **What makes union-find fast?**
    - Your answer: <span class="fill-in">[Fill in after learning optimizations]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

<div class="learner-section" markdown>

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **Naive connectivity check using DFS/BFS:**
    - Time complexity per query: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified after learning: <span class="fill-in">[Actual: O(?)]</span>

2. **Union-Find with optimizations (path compression + union by rank):**
    - Time complexity per operation: <span class="fill-in">[Your guess: O(?)]</span>
    - Space complexity: <span class="fill-in">[Your guess: O(?)]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **Speedup calculation:**
    - If n = 10,000 nodes with 1,000 connectivity queries
    - DFS approach: <span class="fill-in">_____</span> operations
    - Union-Find: <span class="fill-in">_____</span> operations
    - Speedup factor: <span class="fill-in">_____</span> times faster

### Scenario Predictions

**Scenario 1:** You have nodes {0, 1, 2, 3, 4}. Perform: union(0,1), union(2,3), union(1,2)

- **After these operations, which nodes are connected?** <span class="fill-in">[Fill in]</span>
- **How many disjoint components remain?** <span class="fill-in">[Your guess]</span>
- **Are nodes 0 and 3 connected?** <span class="fill-in">[Yes/No - Why?]</span>
- **What happens if we call union(0,3) now?** <span class="fill-in">[Fill in]</span>

**Scenario 2:** Graph edges: [(0,1), (1,2), (2,3), (3,0)]

- **Can you detect a cycle using union-find?** <span class="fill-in">[Yes/No - How?]</span>
- **Which edge creates the cycle?** <span class="fill-in">[Fill in your reasoning]</span>
- **What does find(x) return after path compression?** <span class="fill-in">[Fill in]</span>

**Scenario 3:** Why path compression?

- **Without path compression:** Finding root of deeply nested node costs <span class="fill-in">[O(?)]</span>
- **With path compression:** Amortized cost becomes <span class="fill-in">[O(?)]</span>
- **Draw a tree before and after path compression:** <span class="fill-in">[Sketch after implementation]</span>

### Trade-off Quiz

**Question:** When would DFS/BFS be BETTER than union-find for connectivity?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question:** What's the MAIN benefit of union by rank?

- [ ] Saves memory
- [ ] Reduces number of nodes
- [ ] Keeps tree height balanced
- [ ] Makes find operation faster initially

Verify after implementation: <span class="fill-in">[Which one(s)?]</span>

**Question:** Can union-find split a component into smaller components?

- Your answer: <span class="fill-in">[Yes/No - Why or why not?]</span>
- Implication: <span class="fill-in">[When does this limitation matter?]</span>

</div>

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand the impact.

### Example: Connectivity Queries

**Problem:** Check if two nodes are connected in a dynamic graph with union operations.

#### Approach 1: DFS/BFS for Each Query

```java
// Naive approach - Traverse graph for every connectivity check
public class NaiveConnectivity {
    private List<Integer>[] graph;

    public NaiveConnectivity(int n) {
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    public void union(int x, int y) {
        graph[x].add(y);
        graph[y].add(x);
    }

    public boolean connected(int x, int y) {
        // DFS to check connectivity
        boolean[] visited = new boolean[graph.length];
        return dfs(x, y, visited);
    }

    private boolean dfs(int node, int target, boolean[] visited) {
        if (node == target) return true;
        visited[node] = true;

        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                if (dfs(neighbor, target, visited)) return true;
            }
        }
        return false;
    }
}
```

**Analysis:**

- Time per union: O(1) - Just add edges
- Time per connected query: O(V + E) - Full DFS/BFS traversal
- Space: O(V + E) - Store all edges
- For 1,000 queries on 10,000 nodes: ~10,000,000+ operations per query

#### Approach 2: Union-Find (Optimized)

```java
// Optimized approach - Union-Find with path compression and union by rank
public class OptimizedConnectivity {
    private int[] parent;
    private int[] rank;

    public OptimizedConnectivity(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        // Path compression: point directly to root
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return;

        // Union by rank: attach smaller tree under larger
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }
}
```

**Analysis:**

- Time per operation: O(α(n)) ≈ O(1) - Inverse Ackermann (practically constant)
- Space: O(n) - Only parent and rank arrays
- For 1,000 queries: ~1,000 operations total (vs millions)

#### Performance Comparison

| Operations     | DFS/BFS (O(V+E)) | Union-Find (O(α(n))) | Speedup |
|----------------|------------------|----------------------|---------|
| 100 queries    | ~100,000 ops     | ~100 ops             | 1,000x  |
| 1,000 queries  | ~1,000,000 ops   | ~1,000 ops           | 1,000x  |
| 10,000 queries | ~10,000,000 ops  | ~10,000 ops          | 1,000x  |

**Your calculation:** For 5,000 connectivity queries on a graph with 5,000 nodes, the speedup is approximately _____
times faster.

#### Why Does Union-Find Work?

**Key insight to understand:**

Starting with nodes: {0, 1, 2, 3, 4} (all separate)

```
Step 1: union(0, 1)
   Component structure:  0    2  3  4
                        /
                       1

Step 2: union(2, 3)
   Component structure:  0    2    4
                        /    /
                       1    3

Step 3: union(1, 2)
   Component structure:  0      4
                        / \
                       1   2
                          /
                         3

Now find(1) and find(3) both return 0 (same root) → connected!
```

**Path compression in action:**

```
Before find(3): 3 → 2 → 0  (must traverse 2 links)
After find(3):  3 → 0      (directly points to root)
                2 → 0      (also flattened)
```

**Why can we skip intermediate nodes?**

- We only care if two nodes share the same root (same component)
- The path structure doesn't matter, only connectivity
- Path compression flattens trees without changing connectivity
- Future operations become faster!

**After implementing, explain in your own words:**

<div class="learner-section" markdown>

- <span class="fill-in">[Why does union by rank keep trees balanced?]</span>
- <span class="fill-in">[How does path compression improve future finds?]</span>
- <span class="fill-in">[What's the inverse Ackermann function and why does it matter?]</span>

</div>

---

## Core Implementation

### Pattern 1: Basic Union-Find with Optimizations

**Concept:** Track connected components with path compression and union by rank.

**Use case:** Dynamic connectivity, detecting cycles, network connections.

```java
public class UnionFind {

    /**
     * Union-Find Data Structure
     * Time: O(α(n)) ≈ O(1) per operation with optimizations
     * Space: O(n)
     *
     * TODO: Implement with path compression and union by rank
     */
    static class DSU {
        private int[] parent;
        private int[] rank; // or size, depending on optimization
        private int components; // Track number of disjoint sets

        public DSU(int n) {
            // TODO: Initialize parent array: parent[i] = i
            // TODO: Initialize rank array: rank[i] = 0
            // TODO: Initialize components = n
        }

        /**
         * Find with path compression
         * Time: O(α(n)) amortized
         *
         * TODO: Implement find with path compression
         */
        public int find(int x) {
            // TODO: If parent[x] != x, recursively find root
            // TODO: Path compression: parent[x] = find(parent[x])
            // TODO: Return parent[x]
            return 0; // Replace with implementation
        }

        /**
         * Union by rank
         * Time: O(α(n)) amortized
         *
         * TODO: Implement union by rank
         */
        public boolean union(int x, int y) {
            // TODO: Find roots of x and y
            // TODO: If same root, already connected, return false
            // TODO: Attach smaller rank tree under larger rank tree
            // TODO: If same rank, increment rank of new root
            // TODO: Decrement components count
            // TODO: Return true (successful union)
            return false; // Replace with implementation
        }

        /**
         * Check if connected
         * Time: O(α(n))
         */
        public boolean connected(int x, int y) {
            // TODO: Return find(x) == find(y)
            return false; // Replace with implementation
        }

        /**
         * Get number of disjoint components
         * Time: O(1)
         */
        public int getComponents() {
            // TODO: Return components count
            return 0; // Replace with implementation
        }

        /**
         * Get size of component containing x
         * Time: O(α(n))
         */
        public int getSize(int x) {
            // TODO: If using size array, return size[find(x)]
            // TODO: Otherwise, count elements with same root
            return 0; // Replace with implementation
        }
    }

    /**
     * Problem: Number of connected components in undirected graph
     * Time: O(E * α(V)), Space: O(V)
     *
     * TODO: Implement using union-find
     */
    public static int countComponents(int n, int[][] edges) {
        // TODO: Initialize DSU with n nodes
        // TODO: For each edge, union the two nodes
        // TODO: Return number of components

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
public class UnionFindClient {

    public static void main(String[] args) {
        System.out.println("=== Union-Find ===\n");

        // Test 1: Basic operations
        System.out.println("--- Test 1: Basic Operations ---");
        UnionFind.DSU dsu = new UnionFind.DSU(10);

        System.out.println("Initial components: " + dsu.getComponents());

        // Connect some nodes
        int[][] connections = {{0, 1}, {1, 2}, {3, 4}, {5, 6}, {6, 7}};
        System.out.println("\nConnecting nodes:");
        for (int[] conn : connections) {
            boolean success = dsu.union(conn[0], conn[1]);
            System.out.printf("  union(%d, %d): %s%n", conn[0], conn[1],
                success ? "SUCCESS" : "ALREADY CONNECTED");
        }

        System.out.println("\nFinal components: " + dsu.getComponents());

        // Test connectivity
        System.out.println("\nConnectivity tests:");
        int[][] tests = {{0, 2}, {0, 3}, {3, 4}, {5, 8}};
        for (int[] test : tests) {
            boolean connected = dsu.connected(test[0], test[1]);
            System.out.printf("  connected(%d, %d): %s%n", test[0], test[1],
                connected ? "YES" : "NO");
        }

        // Test 2: Count components
        System.out.println("\n--- Test 2: Count Components ---");
        int n = 5;
        int[][] edges = {{0, 1}, {1, 2}, {3, 4}};

        System.out.println("Nodes: " + n);
        System.out.println("Edges: " + java.util.Arrays.deepToString(edges));

        int components = UnionFind.countComponents(n, edges);
        System.out.println("Components: " + components);
    }
}
```

---

### Pattern 2: Cycle Detection

**Concept:** Detect cycles in undirected graphs using union-find.

**Use case:** Redundant connection, graph valid tree.

```java
import java.util.*;

public class CycleDetection {

    /**
     * Problem: Detect if undirected graph has a cycle
     * Time: O(E * α(V)), Space: O(V)
     *
     * TODO: Implement cycle detection
     */
    public static boolean hasCycle(int n, int[][] edges) {
        // TODO: Initialize union-find
        // TODO: For each edge (u, v):
        //   If find(u) == find(v), cycle detected
        //   Otherwise, union(u, v)
        // TODO: Return false if no cycle

        return false; // Replace with implementation
    }

    /**
     * Problem: Find redundant connection (edge that creates cycle)
     * Time: O(E * α(V)), Space: O(V)
     *
     * TODO: Implement redundant connection
     */
    public static int[] findRedundantConnection(int[][] edges) {
        // TODO: Initialize union-find
        // TODO: For each edge (u, v):
        //   If find(u) == find(v), this edge creates cycle
        //   Return this edge
        //   Otherwise, union(u, v)

        return new int[]{-1, -1}; // Replace with implementation
    }

    /**
     * Problem: Check if graph is a valid tree
     * Time: O(E * α(V)), Space: O(V)
     *
     * TODO: Implement tree validation
     */
    public static boolean validTree(int n, int[][] edges) {
        // TODO: Tree must have exactly n-1 edges
        // TODO: Must have no cycles
        // TODO: Must be fully connected (1 component)

        return false; // Replace with implementation
    }

    /**
     * Problem: Find redundant directed connection
     * Time: O(E * α(V)), Space: O(V)
     *
     * TODO: Implement for directed graph
     */
    public static int[] findRedundantDirectedConnection(int[][] edges) {
        // TODO: More complex - need to handle:
        //   Case 1: Node with two parents
        //   Case 2: Cycle
        // TODO: Try removing each candidate edge

        return new int[]{-1, -1}; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class CycleDetectionClient {

    public static void main(String[] args) {
        System.out.println("=== Cycle Detection ===\n");

        // Test 1: Has cycle
        System.out.println("--- Test 1: Has Cycle ---");
        int[][] testGraphs = {
            {{0, 1}, {1, 2}},           // No cycle
            {{0, 1}, {1, 2}, {2, 0}},   // Cycle
            {{0, 1}, {0, 2}, {1, 2}}    // Cycle
        };

        for (int i = 0; i < testGraphs.length; i++) {
            int n = 3;
            boolean cycle = CycleDetection.hasCycle(n, testGraphs[i]);
            System.out.printf("Graph %d: %s -> %s%n", i + 1,
                Arrays.deepToString(testGraphs[i]),
                cycle ? "HAS CYCLE" : "NO CYCLE");
        }

        // Test 2: Redundant connection
        System.out.println("\n--- Test 2: Redundant Connection ---");
        int[][] edgeSets = {
            {{1, 2}, {1, 3}, {2, 3}},
            {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}
        };

        for (int[][] edges : edgeSets) {
            int[] redundant = CycleDetection.findRedundantConnection(edges);
            System.out.printf("Edges: %s%n", Arrays.deepToString(edges));
            System.out.printf("Redundant: %s%n%n", Arrays.toString(redundant));
        }

        // Test 3: Valid tree
        System.out.println("--- Test 3: Valid Tree ---");
        int[][] treeTests = {
            {{0, 1}, {0, 2}, {0, 3}, {1, 4}},           // Valid tree (5 nodes)
            {{0, 1}, {1, 2}, {2, 3}, {1, 3}, {1, 4}}    // Not tree (cycle)
        };

        for (int i = 0; i < treeTests.length; i++) {
            int n = 5;
            boolean isTree = CycleDetection.validTree(n, treeTests[i]);
            System.out.printf("Test %d: %s -> %s%n", i + 1,
                Arrays.deepToString(treeTests[i]),
                isTree ? "VALID TREE" : "NOT TREE");
        }
    }
}
```

---

### Pattern 3: Connected Components Problems

**Concept:** Group elements into connected components.

**Use case:** Number of islands, accounts merge, provinces.

```java
import java.util.*;

public class ConnectedComponents {

    /**
     * Problem: Number of islands (using union-find)
     * Time: O(m*n * α(m*n)), Space: O(m*n)
     *
     * TODO: Implement using union-find
     */
    public static int numIslands(char[][] grid) {
        // TODO: Initialize union-find for all cells
        // TODO: For each land cell ('1'):
        //   Union with adjacent land cells
        // TODO: Count unique components of land cells

        return 0; // Replace with implementation
    }

    /**
     * Problem: Number of provinces (friend circles)
     * Time: O(n^2 * α(n)), Space: O(n)
     *
     * TODO: Implement using union-find
     */
    public static int findCircleNum(int[][] isConnected) {
        // TODO: Initialize union-find with n people
        // TODO: For each connection isConnected[i][j] == 1:
        //   Union i and j
        // TODO: Return number of components

        return 0; // Replace with implementation
    }

    /**
     * Problem: Accounts merge (emails belonging to same person)
     * Time: O(n*k * α(n*k)), Space: O(n*k)
     *
     * TODO: Implement accounts merge
     */
    public static List<List<String>> accountsMerge(List<List<String>> accounts) {
        // TODO: Map email to account index
        // TODO: Union accounts that share emails
        // TODO: Group emails by component
        // TODO: Sort emails in each group

        return new ArrayList<>(); // Replace with implementation
    }

    /**
     * Problem: Smallest string with swaps
     * Time: O(n log n + E * α(n)), Space: O(n)
     *
     * TODO: Implement using union-find
     */
    public static String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        // TODO: Union indices that can be swapped
        // TODO: Group characters by component
        // TODO: Sort characters in each component
        // TODO: Reconstruct string

        return ""; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class ConnectedComponentsClient {

    public static void main(String[] args) {
        System.out.println("=== Connected Components ===\n");

        // Test 1: Number of islands
        System.out.println("--- Test 1: Number of Islands ---");
        char[][] grid = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };

        System.out.println("Grid:");
        for (char[] row : grid) {
            System.out.println("  " + Arrays.toString(row));
        }

        int islands = ConnectedComponents.numIslands(grid);
        System.out.println("Number of islands: " + islands);

        // Test 2: Number of provinces
        System.out.println("\n--- Test 2: Number of Provinces ---");
        int[][] isConnected = {
            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 1}
        };

        System.out.println("Connections:");
        for (int[] row : isConnected) {
            System.out.println("  " + Arrays.toString(row));
        }

        int provinces = ConnectedComponents.findCircleNum(isConnected);
        System.out.println("Number of provinces: " + provinces);

        // Test 3: Accounts merge
        System.out.println("\n--- Test 3: Accounts Merge ---");
        List<List<String>> accounts = Arrays.asList(
            Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"),
            Arrays.asList("John", "johnnybravo@mail.com"),
            Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"),
            Arrays.asList("Mary", "mary@mail.com")
        );

        System.out.println("Accounts:");
        for (List<String> account : accounts) {
            System.out.println("  " + account);
        }

        List<List<String>> merged = ConnectedComponents.accountsMerge(accounts);
        System.out.println("\nMerged accounts:");
        for (List<String> account : merged) {
            System.out.println("  " + account);
        }

        // Test 4: Smallest string with swaps
        System.out.println("\n--- Test 4: Smallest String with Swaps ---");
        String s = "dcab";
        List<List<Integer>> pairs = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2)
        );

        System.out.println("String: " + s);
        System.out.println("Swappable pairs: " + pairs);

        String result = ConnectedComponents.smallestStringWithSwaps(s, pairs);
        System.out.println("Smallest string: " + result);
    }
}
```

---

### Pattern 4: Advanced Union-Find Applications

**Concept:** Use union-find with additional constraints or weights.

**Use case:** Satisfiability, equations, sentence similarity.

```java
import java.util.*;

public class AdvancedUnionFind {

    /**
     * Problem: Satisfiability of equality equations
     * Time: O(n * α(26)), Space: O(26)
     *
     * TODO: Implement equation satisfaction check
     */
    public static boolean equationsPossible(String[] equations) {
        // TODO: Initialize union-find for 26 letters
        // TODO: First pass: union all equal variables (==)
        // TODO: Second pass: check all inequalities (!=)
        //   If find(a) == find(b) but a != b, contradiction
        // TODO: Return true if no contradictions

        return false; // Replace with implementation
    }

    /**
     * Problem: Evaluate division (transitive division)
     * Time: O(E * α(V) + Q * V), Space: O(V)
     *
     * TODO: Implement with weighted union-find
     */
    public static double[] calcEquation(List<List<String>> equations,
                                       double[] values,
                                       List<List<String>> queries) {
        // TODO: Build graph with division relationships
        // TODO: For each query, find path and multiply values
        // TODO: Or use weighted union-find with ratios

        return new double[0]; // Replace with implementation
    }

    /**
     * Problem: Sentence similarity II (transitive similarity)
     * Time: O(P * α(W)), Space: O(W)
     *
     * TODO: Implement similarity check
     */
    public static boolean areSentencesSimilar(String[] words1, String[] words2,
                                             List<List<String>> pairs) {
        // TODO: If different lengths, not similar
        // TODO: Union similar word pairs
        // TODO: Check if words1[i] and words2[i] in same component

        return false; // Replace with implementation
    }

    /**
     * Problem: Minimize malware spread
     * Time: O(n^2 * α(n)), Space: O(n)
     *
     * TODO: Implement using union-find
     */
    public static int minMalwareSpread(int[][] graph, int[] initial) {
        // TODO: Union all connected nodes
        // TODO: For each initially infected node:
        //   Count how many nodes would be saved if removed
        // TODO: Return node whose removal saves most nodes

        return 0; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class AdvancedUnionFindClient {

    public static void main(String[] args) {
        System.out.println("=== Advanced Union-Find ===\n");

        // Test 1: Equations possible
        System.out.println("--- Test 1: Equations Possible ---");
        String[][] equationSets = {
            {"a==b", "b!=a"},
            {"b==a", "a==b"},
            {"a==b", "b==c", "a==c"}
        };

        for (String[] equations : equationSets) {
            boolean possible = AdvancedUnionFind.equationsPossible(equations);
            System.out.printf("Equations: %s%n", Arrays.toString(equations));
            System.out.printf("Possible: %s%n%n", possible ? "YES" : "NO");
        }

        // Test 2: Evaluate division
        System.out.println("--- Test 2: Evaluate Division ---");
        List<List<String>> equations = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("b", "c")
        );
        double[] values = {2.0, 3.0};
        List<List<String>> queries = Arrays.asList(
            Arrays.asList("a", "c"),
            Arrays.asList("b", "a"),
            Arrays.asList("a", "e"),
            Arrays.asList("a", "a"),
            Arrays.asList("x", "x")
        );

        System.out.println("Equations: " + equations);
        System.out.println("Values: " + Arrays.toString(values));
        System.out.println("Queries: " + queries);

        double[] results = AdvancedUnionFind.calcEquation(equations, values, queries);
        System.out.println("Results: " + Arrays.toString(results));

        // Test 3: Sentence similarity
        System.out.println("\n--- Test 3: Sentence Similarity II ---");
        String[] words1 = {"great", "acting", "skills"};
        String[] words2 = {"fine", "drama", "talent"};
        List<List<String>> pairs = Arrays.asList(
            Arrays.asList("great", "good"),
            Arrays.asList("fine", "good"),
            Arrays.asList("acting", "drama"),
            Arrays.asList("skills", "talent")
        );

        System.out.println("Sentence 1: " + Arrays.toString(words1));
        System.out.println("Sentence 2: " + Arrays.toString(words2));
        System.out.println("Similar pairs: " + pairs);

        boolean similar = AdvancedUnionFind.areSentencesSimilar(words1, words2, pairs);
        System.out.println("Similar: " + (similar ? "YES" : "NO"));
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken implementations. This tests your understanding.

### Challenge 1: Broken Find (Missing Path Compression)

```java
/**
 * This find implementation is supposed to use path compression.
 * It has 1 CRITICAL BUG. Find it!
 */
public int find_Buggy(int x) {
    if (parent[x] != x) {
        return find_Buggy(parent[x]);    }
    return parent[x];
}
```

**Your debugging:**

- **Bug location:** <span class="fill-in">[Which line?]</span>
- **Bug explanation:** <span class="fill-in">[What optimization is missing?]</span>
- **Bug fix:** <span class="fill-in">[What should the code be?]</span>

**Test case to measure impact:**

- Chain: 0 → 1 → 2 → 3 → 4 → 5
- Call find(5) multiple times
- Without fix: Each call traverses ___ links
- With fix: Second call traverses ___ links

<details markdown>
<summary>Click to verify your answers</summary>

**Bug:** Missing path compression! Should assign `parent[x] = find_Buggy(parent[x])` to flatten the tree.

**Correct:**

```java
public int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);  // Path compression!
    }
    return parent[x];
}
```

**Impact:** Without path compression, find() is O(n) in worst case (long chain). With it, amortized O(α(n)) ≈ O(1).
</details>

---

### Challenge 2: Broken Union (Wrong Parent Update)

```java
/**
 * Union by rank implementation.
 * This has 1 SUBTLE BUG that breaks the rank optimization.
 */
public boolean union_Buggy(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) return false;

    if (rank[rootX] < rank[rootY]) {
        parent[x] = rootY;    } else if (rank[rootX] > rank[rootY]) {
        parent[y] = rootX;    } else {
        parent[rootY] = rootX;
        rank[rootX]++;
    }

    return true;
}
```

**Your debugging:**

- **Bug 1:** _[What's wrong with `parent[x] = rootY`?]_
- **Bug 2:** _[What's wrong with `parent[y] = rootX`?]_
- **Why this breaks union by rank:** <span class="fill-in">[Explain the impact]</span>
- **Correct code:** <span class="fill-in">[What should it be?]</span>

**Trace through example:**

- Nodes: {0, 1, 2, 3}, all separate
- union(0, 1) → Works fine
- union(2, 3) → Works fine
- union(0, 2) with buggy code
- Expected: Attach one root under the other
- Actual: <span class="fill-in">[What happens?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Should attach ROOTS, not the original nodes!

**Correct:**

```java
if (rank[rootX] < rank[rootY]) {
    parent[rootX] = rootY;  // Attach rootX under rootY
} else if (rank[rootX] > rank[rootY]) {
    parent[rootY] = rootX;  // Attach rootY under rootX
}
```

**Why:** If you attach `x` instead of `rootX`, you're not attaching the entire tree's root, just one node. This breaks
the tree structure and defeats the purpose of union by rank.
</details>

---

### Challenge 3: Broken Cycle Detection

```java
/**
 * Detect cycle in undirected graph.
 * This has 1 LOGIC ERROR.
 */
public boolean hasCycle_Buggy(int n, int[][] edges) {
    UnionFind uf = new UnionFind(n);

    for (int[] edge : edges) {
        int u = edge[0];
        int v = edge[1];

        if (uf.find(u) == uf.find(v)) {
            uf.union(u, v);            return true;
        }

        uf.union(u, v);
    }

    return false;
}
```

**Your debugging:**

- **Bug:** <span class="fill-in">[What's the logic error?]</span>
- **Bug explanation:** <span class="fill-in">[Why is this incorrect?]</span>
- **Correct approach:** <span class="fill-in">[What should happen when find(u) == find(v)?]</span>

**Test case:**

- Graph edges: [(0,1), (1,2), (2,0)]
- Expected: Detect cycle at edge (2,0)
- Actual with buggy code: <span class="fill-in">[What happens?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Should NOT call `union(u, v)` when they're already connected! If `find(u) == find(v)`, they're in the same
component, which means adding this edge creates a cycle. Just return true immediately.

**Correct:**

```java
if (uf.find(u) == uf.find(v)) {
    return true;  // Cycle detected! Don't union.
}
uf.union(u, v);
```

**Why:** Calling union when they're already connected is pointless and wastes an operation.
</details>

---

### Challenge 4: Missing Component Count Update

```java
/**
 * Union-Find with component counting.
 * This has 1 MISSING OPERATION.
 */
public class UnionFind_Buggy {
    private int[] parent;
    private int[] rank;
    private int components;

    public UnionFind_Buggy(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return false;

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

    public int getComponents() {
        return components;
    }
}
```

**Your debugging:**

- **Bug:** <span class="fill-in">[What's missing in union()?]</span>
- **Impact:** <span class="fill-in">[How does this affect getComponents()?]</span>
- **Fix:** <span class="fill-in">[What line should be added?]</span>

**Test case:**

- Initialize with 5 nodes (components = 5)
- union(0, 1) → Expected components: 4, Actual: <span class="fill-in">___</span>
- union(2, 3) → Expected components: 3, Actual: <span class="fill-in">___</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Missing `components--;` in the union method!

**Correct:**

```java
if (rootX == rootY) return false;

// ... union logic ...

components--;  // Decrement when we merge two components!
return true;
```

**Why:** Every successful union merges two disjoint components into one, reducing the total count by 1.
</details>

---

### Challenge 5: Rank Update Error

```java
/**
 * Union by rank implementation.
 * This has 1 SUBTLE BUG in rank update.
 */
public boolean union_Buggy(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) return false;

    if (rank[rootX] < rank[rootY]) {
        parent[rootX] = rootY;
        rank[rootY]++;    } else if (rank[rootX] > rank[rootY]) {
        parent[rootY] = rootX;
        rank[rootX]++;    } else {
        parent[rootY] = rootX;
        rank[rootX]++;  // This one is correct
    }

    return true;
}
```

**Your debugging:**

- **Bug 1:** <span class="fill-in">[Should we increment rank when rootY is taller?]</span>
- **Bug 2:** <span class="fill-in">[Should we increment rank when rootX is taller?]</span>
- **When should rank be incremented?** <span class="fill-in">[Fill in the rule]</span>
- **Why rank matters:** <span class="fill-in">[Explain the purpose of rank]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Only increment rank when ranks are EQUAL!

**Correct:**

```java
if (rank[rootX] < rank[rootY]) {
    parent[rootX] = rootY;
    // Don't increment - rootY's height doesn't change
} else if (rank[rootX] > rank[rootY]) {
    parent[rootY] = rootX;
    // Don't increment - rootX's height doesn't change
} else {
    parent[rootY] = rootX;
    rank[rootX]++;  // Only increment when equal!
}
```

**Why:** Rank represents tree height (upper bound). When attaching a shorter tree under a taller one, the height doesn't
change. Only when equal-height trees merge does the new root's height increase by 1.
</details>

---

### Challenge 6: Iterative Find Bug

```java
/**
 * Iterative find with path compression attempt.
 * This has 1 BUG that prevents path compression from working.
 */
public int find_Buggy(int x) {
    // Find root
    int root = x;
    while (parent[root] != root) {
        root = parent[root];
    }

    // Path compression
    while (parent[x] != x) {
        int next = parent[x];
        parent[x] = root;
        x = next;    }

    return root;
}

// Now the BUGGY version - what if we write it like this?
public int find_ActualBuggy(int x) {
    int root = x;
    while (parent[root] != root) {
        root = parent[root];
    }

    // Attempted path compression
    while (parent[x] != root) {        parent[x] = root;
        x = parent[x];
    }

    return root;
}
```

**Your debugging:**

- **Bug:** <span class="fill-in">[What's wrong with the compression loop condition?]</span>
- **What happens:** <span class="fill-in">[Trace through with chain: 0 → 1 → 2]</span>
- **Correct version:** <span class="fill-in">[How to fix it?]</span>

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** In the compression loop, after we set `parent[x] = root`, we then do `x = parent[x]`, which now equals `root`!
This causes the loop to terminate immediately, compressing only the first node.

**Correct:**

```java
while (parent[x] != x) {
    int next = parent[x];  // Save next before modifying
    parent[x] = root;      // Point to root
    x = next;              // Move to saved next
}
```

Or use the condition `parent[x] != root` but save the next pointer first.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 6+ bugs across 6 challenges
- [ ] Understood WHY each bug causes incorrect behavior
- [ ] Could explain the fix to someone else
- [ ] Learned common union-find mistakes to avoid

**Common mistakes you discovered:**

1. <span class="fill-in">[Forgetting path compression in find]</span>
2. <span class="fill-in">[Attaching node instead of root in union]</span>
3. <span class="fill-in">[Incorrectly updating rank]</span>
4. <span class="fill-in">[Missing component count decrement]</span>
5. <span class="fill-in">[Calling union when cycle detected]</span>
6. <span class="fill-in">[Path compression iteration bugs]</span>

---

## Decision Framework

**Your task:** Build decision trees for union-find problems.

### Question 1: What do you need to track?

Answer after solving problems:

- **Connected components?** <span class="fill-in">[Basic union-find]</span>
- **Cycles in graph?** <span class="fill-in">[Union-find with cycle detection]</span>
- **Dynamic connectivity?** <span class="fill-in">[Union-find with online queries]</span>
- **Weighted relationships?** <span class="fill-in">[Weighted union-find]</span>

### Question 2: What optimizations do you need?

**Always use:**

- Path compression: <span class="fill-in">[Makes find nearly O(1)]</span>
- Union by rank/size: <span class="fill-in">[Keeps tree balanced]</span>

**Additional data:**

- Component size: <span class="fill-in">[Track in size array]</span>
- Component count: <span class="fill-in">[Decrement on union]</span>
- Weights/ratios: <span class="fill-in">[For transitive relationships]</span>

### Your Decision Tree

```
Union-Find Pattern Selection
│
├─ Basic connectivity?
│   └─ Use: Standard union-find ✓
│
├─ Cycle detection?
│   ├─ Undirected → Union-find ✓
│   └─ Directed → DFS (not union-find)
│
├─ Dynamic components?
│   ├─ Number of islands → Union-find ✓
│   ├─ Provinces/groups → Union-find ✓
│   └─ Merging accounts → Union-find ✓
│
└─ Weighted relationships?
    ├─ Division equations → Weighted UF ✓
    ├─ Distance/ratio → Weighted UF ✓
    └─ Equality constraints → Basic UF ✓
```


---

## Practice

### LeetCode Problems

**Easy (Complete all 2):**

- [ ] [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/)
    - Pattern: <span class="fill-in">[Connected components]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [990. Satisfiability of Equality Equations](https://leetcode.com/problems/satisfiability-of-equality-equations/)
    - Pattern: <span class="fill-in">[Constraint checking]</span>
    - Your solution time: <span class="fill-in">___</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Medium (Complete 3-4):**

- [ ] [200. Number of Islands](https://leetcode.com/problems/number-of-islands/)
    - Pattern: <span class="fill-in">[Connected components in grid]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [684. Redundant Connection](https://leetcode.com/problems/redundant-connection/)
    - Pattern: <span class="fill-in">[Cycle detection]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [721. Accounts Merge](https://leetcode.com/problems/accounts-merge/)
    - Pattern: <span class="fill-in">[Grouping with union-find]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

- [ ] [1202. Smallest String With Swaps](https://leetcode.com/problems/smallest-string-with-swaps/)
    - Pattern: <span class="fill-in">[Components with optimization]</span>
    - Difficulty: <span class="fill-in">[Rate 1-10]</span>
    - Key insight: <span class="fill-in">[Fill in]</span>

**Hard (Optional):**

- [ ] [685. Redundant Connection II](https://leetcode.com/problems/redundant-connection-ii/)
    - Pattern: <span class="fill-in">[Directed graph cycle]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

- [ ] [399. Evaluate Division](https://leetcode.com/problems/evaluate-division/)
    - Pattern: <span class="fill-in">[Weighted union-find]</span>
    - Key insight: <span class="fill-in">[Fill in after solving]</span>

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] Basic union-find with path compression and union by rank works
    - [ ] Cycle detection: redundant connection, valid tree work
    - [ ] Connected components: islands, provinces, merge accounts work
    - [ ] Advanced: equations, weighted relationships work
    - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
    - [ ] Can identify when union-find is appropriate
    - [ ] Understand path compression and union by rank
    - [ ] Know when to track additional data (size, count)
    - [ ] Recognize weighted union-find problems

- [ ] **Problem Solving**
    - [ ] Solved 2 easy problems
    - [ ] Solved 3-4 medium problems
    - [ ] Analyzed time/space complexity
    - [ ] Understood amortized analysis

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Built decision tree
    - [ ] Identified when NOT to use union-find
    - [ ] Can explain why optimizations work

- [ ] **Mastery Check**
    - [ ] Could implement all patterns from memory
    - [ ] Could recognize pattern in new problem
    - [ ] Could explain to someone else
    - [ ] Understand inverse Ackermann function complexity

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement union-find with both optimizations from memory
- [ ] Explain path compression and union by rank clearly
- [ ] Identify when union-find is the right tool
- [ ] Analyze time complexity including inverse Ackermann
- [ ] Compare trade-offs with DFS/BFS approaches
- [ ] Debug common union-find mistakes
- [ ] Teach this concept to someone else
- [ ] Solve new union-find problems independently

**Self-assessment score:** ___/10

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered union-find. Proceed to the next topic.

**Reflection:**

- What was the hardest part to understand? <span class="fill-in">[Fill in]</span>
- What clicked for you? <span class="fill-in">[Fill in]</span>
- How would you explain this to your past self before learning it? <span class="fill-in">[Fill in]</span>
