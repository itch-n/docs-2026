package com.study.systems.storage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LSMTreeTest {

    @Test
    void testPutAndGet() {
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        lsm.put(1, "Value1");

        assertEquals("Value1", lsm.get(1));
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        assertNull(lsm.get(100));
    }

    @Test
    void testMultipleWritesReadable() {
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        for (int i = 1; i <= 5; i++) {
            lsm.put(i, "Value" + i);
        }

        assertEquals("Value1", lsm.get(1));
        assertEquals("Value5", lsm.get(5));
    }

    @Test
    void testWritesBeyondMemTableSizeAreReadable() {
        // memTableSize=5: after 5 writes a flush should happen
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        for (int i = 1; i <= 12; i++) {
            lsm.put(i, "Value" + i);
        }

        // Items flushed to SSTable should still be readable
        assertEquals("Value5", lsm.get(5));   // In SSTable
        assertEquals("Value11", lsm.get(11)); // In MemTable
        assertNull(lsm.get(100));
    }

    @Test
    void testUpdateReturnsNewValue() {
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        lsm.put(5, "OriginalValue5");
        lsm.put(5, "UpdatedValue5");

        assertEquals("UpdatedValue5", lsm.get(5));
    }

    @Test
    void testUpdateAcrossFlushBoundary() {
        // Insert 5 items (flush), then update one
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        for (int i = 1; i <= 5; i++) {
            lsm.put(i, "Value" + i);
        }
        // At this point value 5 is flushed to SSTable
        lsm.put(5, "UpdatedValue5");

        // MemTable update should shadow the SSTable entry
        assertEquals("UpdatedValue5", lsm.get(5));
    }

    @Test
    void testCompactionPreservesData() {
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        // Trigger multiple flushes
        for (int i = 1; i <= 20; i++) {
            lsm.put(i, "Value" + i);
        }

        lsm.compact();

        // Data should still be accessible after compaction
        assertEquals("Value5", lsm.get(5));
        assertEquals("Value15", lsm.get(15));
    }

    @Test
    void testCompactionPreservesUpdatedValues() {
        LSMTree<Integer, String> lsm = new LSMTree<>(5);

        for (int i = 1; i <= 12; i++) {
            lsm.put(i, "Value" + i);
        }
        lsm.put(5, "UpdatedValue5");

        lsm.compact();

        assertEquals("UpdatedValue5", lsm.get(5));
    }
}
