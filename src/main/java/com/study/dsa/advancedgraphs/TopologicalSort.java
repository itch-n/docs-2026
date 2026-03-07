package com.study.dsa.advancedgraphs;

import java.util.*;
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
