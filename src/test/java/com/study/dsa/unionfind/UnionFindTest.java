package com.study.dsa.unionfind;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnionFindTest {

    @Test
    void testInitialComponents() {
        UnionFind.DSU dsu = new UnionFind.DSU(10);
        assertEquals(10, dsu.getComponents());
    }

    @Test
    void testUnionReducesComponents() {
        UnionFind.DSU dsu = new UnionFind.DSU(10);
        assertTrue(dsu.union(0, 1));
        assertEquals(9, dsu.getComponents());
        assertTrue(dsu.union(1, 2));
        assertEquals(8, dsu.getComponents());
    }

    @Test
    void testUnionAlreadyConnectedReturnsFalse() {
        UnionFind.DSU dsu = new UnionFind.DSU(10);
        dsu.union(0, 1);
        dsu.union(1, 2);
        // 0 and 2 are now connected (through 1); unioning again should return false
        assertFalse(dsu.union(0, 2));
    }

    @Test
    void testConnectedAfterUnion() {
        UnionFind.DSU dsu = new UnionFind.DSU(10);
        dsu.union(0, 1);
        dsu.union(1, 2);
        assertTrue(dsu.connected(0, 2));
        assertTrue(dsu.connected(0, 1));
        assertTrue(dsu.connected(1, 2));
    }

    @Test
    void testNotConnected() {
        UnionFind.DSU dsu = new UnionFind.DSU(10);
        dsu.union(0, 1);
        dsu.union(3, 4);
        assertFalse(dsu.connected(0, 3));
        assertFalse(dsu.connected(5, 8));
    }

    @Test
    void testFinalComponentsAfterMultipleUnions() {
        UnionFind.DSU dsu = new UnionFind.DSU(10);
        // Connect {0,1,2}, {3,4}, {5,6,7} -> 10 - 5 = 5 components
        dsu.union(0, 1);
        dsu.union(1, 2);
        dsu.union(3, 4);
        dsu.union(5, 6);
        dsu.union(6, 7);
        assertEquals(5, dsu.getComponents());
    }

    @Test
    void testCountComponents() {
        // n=5, edges=[[0,1],[1,2],[3,4]] -> components: {0,1,2}, {3,4}, {none for node4... wait}
        // Actually with edges [0,1],[1,2],[3,4]: {0,1,2}, {3,4} = 2 components
        int n = 5;
        int[][] edges = {{0, 1}, {1, 2}, {3, 4}};
        assertEquals(2, UnionFind.countComponents(n, edges));
    }

    @Test
    void testCountComponentsNoEdges() {
        assertEquals(4, UnionFind.countComponents(4, new int[0][]));
    }

    @Test
    void testCountComponentsFullyConnected() {
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        assertEquals(1, UnionFind.countComponents(4, edges));
    }
}
