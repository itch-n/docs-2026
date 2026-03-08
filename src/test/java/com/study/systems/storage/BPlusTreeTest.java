package com.study.systems.storage;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BPlusTreeTest {

    @Test
    void testInsertAndSearch() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);

        tree.insert(10, "Value10");

        assertEquals("Value10", tree.search(10));
    }

    @Test
    void testSearchMissingKeyReturnsNull() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);

        assertNull(tree.search(100));
    }

    @Test
    void testMultipleInsertsAndSearches() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);

        for (int i = 1; i <= 20; i++) {
            tree.insert(i, "Value" + i);
        }

        assertEquals("Value10", tree.search(10));
        assertEquals("Value15", tree.search(15));
        assertNull(tree.search(100));
    }

    @Test
    void testRangeQueryReturnsCorrectValues() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);

        for (int i = 1; i <= 20; i++) {
            tree.insert(i, "Value" + i);
        }

        List<String> range = tree.rangeQuery(5, 10);

        assertNotNull(range);
        assertEquals(6, range.size()); // 5, 6, 7, 8, 9, 10
        assertTrue(range.contains("Value5"));
        assertTrue(range.contains("Value10"));
    }

    @Test
    void testRangeQueryReturnsValuesInOrder() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);

        for (int i = 1; i <= 10; i++) {
            tree.insert(i, "Value" + i);
        }

        List<String> range = tree.rangeQuery(1, 5);

        assertNotNull(range);
        assertEquals(5, range.size());
        // Values should be in sorted order
        assertEquals("Value1", range.get(0));
        assertEquals("Value5", range.get(4));
    }

    @Test
    void testRandomInsertsAreSearchable() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);
        int[] keys = {45, 12, 67, 23, 89, 34, 56, 78, 90, 1};

        for (int key : keys) {
            tree.insert(key, "Val" + key);
        }

        for (int key : keys) {
            assertEquals("Val" + key, tree.search(key));
        }
    }

    @Test
    void testRangeQueryForAllValuesAfterRandomInserts() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);
        int[] keys = {45, 12, 67, 23, 89, 34, 56, 78, 90, 1};

        for (int key : keys) {
            tree.insert(key, "Val" + key);
        }

        List<String> allValues = tree.rangeQuery(0, 100);

        assertNotNull(allValues);
        assertEquals(10, allValues.size());
    }

    @Test
    void testUpdateExistingKey() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);

        tree.insert(5, "original");
        tree.insert(5, "updated");

        assertEquals("updated", tree.search(5));
    }
}
