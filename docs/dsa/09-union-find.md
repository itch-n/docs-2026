# 15. Union-Find (Disjoint Set Union)

> Efficiently track and merge disjoint sets with near-constant time operations

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is union-find in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **What do "union" and "find" operations do?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
   - Example: "Union-Find is like organizing people into groups where you can quickly check if two people are in the same group..."
   - Your analogy: _[Fill in]_

4. **When does this pattern work?**
   - Your answer: _[Fill in after solving problems]_

5. **What makes union-find fast?**
   - Your answer: _[Fill in after learning optimizations]_

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

## Decision Framework

**Your task:** Build decision trees for union-find problems.

### Question 1: What do you need to track?

Answer after solving problems:
- **Connected components?** _[Basic union-find]_
- **Cycles in graph?** _[Union-find with cycle detection]_
- **Dynamic connectivity?** _[Union-find with online queries]_
- **Weighted relationships?** _[Weighted union-find]_

### Question 2: What optimizations do you need?

**Always use:**
- Path compression: _[Makes find nearly O(1)]_
- Union by rank/size: _[Keeps tree balanced]_

**Additional data:**
- Component size: _[Track in size array]_
- Component count: _[Decrement on union]_
- Weights/ratios: _[For transitive relationships]_

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

### The "Kill Switch" - When NOT to use Union-Find

**Don't use union-find when:**
1. _[Need to split components - UF only merges]_
2. _[Need path information - use DFS/BFS]_
3. _[Directed graph cycles - use DFS]_
4. _[Need intermediate nodes - use graph traversal]_

### The Rule of Three: Alternatives

**Option 1: Union-Find**
- Pros: _[Near O(1) operations, simple for connectivity]_
- Cons: _[Can't split, no path info]_
- Use when: _[Dynamic connectivity, no splits]_

**Option 2: DFS/BFS**
- Pros: _[More flexible, path info]_
- Cons: _[O(V+E) per query]_
- Use when: _[Static graph, need paths]_

**Option 3: Adjacency List**
- Pros: _[Most flexible]_
- Cons: _[Slower connectivity checks]_
- Use when: _[Need full graph operations]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 2):**
- [ ] [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/)
  - Pattern: _[Connected components]_
  - Your solution time: ___
  - Key insight: _[Fill in after solving]_

- [ ] [990. Satisfiability of Equality Equations](https://leetcode.com/problems/satisfiability-of-equality-equations/)
  - Pattern: _[Constraint checking]_
  - Your solution time: ___
  - Key insight: _[Fill in]_

**Medium (Complete 3-4):**
- [ ] [200. Number of Islands](https://leetcode.com/problems/number-of-islands/)
  - Pattern: _[Connected components in grid]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [684. Redundant Connection](https://leetcode.com/problems/redundant-connection/)
  - Pattern: _[Cycle detection]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [721. Accounts Merge](https://leetcode.com/problems/accounts-merge/)
  - Pattern: _[Grouping with union-find]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

- [ ] [1202. Smallest String With Swaps](https://leetcode.com/problems/smallest-string-with-swaps/)
  - Pattern: _[Components with optimization]_
  - Difficulty: _[Rate 1-10]_
  - Key insight: _[Fill in]_

**Hard (Optional):**
- [ ] [685. Redundant Connection II](https://leetcode.com/problems/redundant-connection-ii/)
  - Pattern: _[Directed graph cycle]_
  - Key insight: _[Fill in after solving]_

- [ ] [399. Evaluate Division](https://leetcode.com/problems/evaluate-division/)
  - Pattern: _[Weighted union-find]_
  - Key insight: _[Fill in after solving]_

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

**Next Topic:** [10. Graphs →](10-graphs.md)

**Back to:** [08. Binary Search ←](08-binary-search.md)
