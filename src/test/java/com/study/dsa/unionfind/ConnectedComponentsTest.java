package com.study.dsa.unionfind;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class ConnectedComponentsTest {

    @Test
    void testNumIslands() {
        // 3 islands: top-left block, center cell, bottom-right pair
        char[][] grid = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
        };
        assertEquals(3, ConnectedComponents.numIslands(grid));
    }

    @Test
    void testNumIslandsSingleIsland() {
        char[][] grid = {
            {'1', '1'},
            {'1', '1'}
        };
        assertEquals(1, ConnectedComponents.numIslands(grid));
    }

    @Test
    void testNumIslandsAllWater() {
        char[][] grid = {
            {'0', '0'},
            {'0', '0'}
        };
        assertEquals(0, ConnectedComponents.numIslands(grid));
    }

    @Test
    void testFindCircleNum() {
        // [[1,1,0],[1,1,0],[0,0,1]]: people 0 and 1 are friends, 2 is alone -> 2 provinces
        int[][] isConnected = {
            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 1}
        };
        assertEquals(2, ConnectedComponents.findCircleNum(isConnected));
    }

    @Test
    void testFindCircleNumAllFriends() {
        int[][] isConnected = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        assertEquals(1, ConnectedComponents.findCircleNum(isConnected));
    }

    @Test
    void testFindCircleNumNoFriends() {
        int[][] isConnected = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
        assertEquals(3, ConnectedComponents.findCircleNum(isConnected));
    }

    @Test
    void testAccountsMerge() {
        // John has two accounts with overlapping email -> merged
        // John has separate account -> stays separate
        // Mary has own account
        List<List<String>> accounts = Arrays.asList(
            Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"),
            Arrays.asList("John", "johnnybravo@mail.com"),
            Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"),
            Arrays.asList("Mary", "mary@mail.com")
        );
        List<List<String>> merged = ConnectedComponents.accountsMerge(accounts);
        // Should produce 3 accounts:
        //   John with john00, john_newyork, johnsmith (merged accounts 0 and 2)
        //   John with johnnybravo (account 1 unchanged)
        //   Mary with mary@mail.com
        assertEquals(3, merged.size());
        // Find the merged John account by checking for johnsmith email
        boolean foundMergedJohn = false;
        for (List<String> account : merged) {
            if (account.contains("johnsmith@mail.com")) {
                foundMergedJohn = true;
                assertEquals("John", account.get(0));
                assertTrue(account.contains("john00@mail.com"));
                assertTrue(account.contains("john_newyork@mail.com"));
            }
        }
        assertTrue(foundMergedJohn, "Merged John account not found");
    }

    @Test
    void testSmallestStringWithSwaps() {
        // s = "dcab", pairs = [[0,3],[1,2]]
        // Group 1: indices {0,3} -> chars 'd','b' -> sorted: 'b','d'
        // Group 2: indices {1,2} -> chars 'c','a' -> sorted: 'a','c'
        // Result: "bacd"
        String s = "dcab";
        List<List<Integer>> pairs = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2)
        );
        assertEquals("bacd", ConnectedComponents.smallestStringWithSwaps(s, pairs));
    }

    @Test
    void testSmallestStringWithSwapsNoPairs() {
        String s = "dcab";
        List<List<Integer>> pairs = Arrays.asList();
        assertEquals("dcab", ConnectedComponents.smallestStringWithSwaps(s, pairs));
    }
}
