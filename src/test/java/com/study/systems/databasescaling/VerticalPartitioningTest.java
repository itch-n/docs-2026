package com.study.systems.databasescaling;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VerticalPartitioningTest {

    @Test
    void testInsertAndGetHotData() {
        VerticalPartitioning db = new VerticalPartitioning();

        db.insert("user1", "Alice", "alice@example.com", "Long bio...", new byte[100]);

        VerticalPartitioning.HotData hot = db.getHotData("user1");
        assertNotNull(hot);
        assertEquals("Alice", hot.name);
        assertEquals("alice@example.com", hot.email);
    }

    @Test
    void testInsertAndGetFullRecord() {
        VerticalPartitioning db = new VerticalPartitioning();

        db.insert("user1", "Alice", "alice@example.com", "My bio", new byte[50]);

        VerticalPartitioning.FullRecord full = db.getFullRecord("user1");
        assertNotNull(full);
        assertEquals("Alice", full.name);
        assertEquals("alice@example.com", full.email);
        assertEquals("My bio", full.bio);
        assertEquals(50, full.largeData.length);
    }

    @Test
    void testGetHotDataForMissingUserReturnsNull() {
        VerticalPartitioning db = new VerticalPartitioning();

        assertNull(db.getHotData("nonexistent"));
    }

    @Test
    void testGetFullRecordForMissingUserReturnsNull() {
        VerticalPartitioning db = new VerticalPartitioning();

        assertNull(db.getFullRecord("nonexistent"));
    }

    @Test
    void testStatsReflectInsertedRecords() {
        VerticalPartitioning db = new VerticalPartitioning();

        db.insert("user1", "Alice", "a@example.com", "Bio1", new byte[10]);
        db.insert("user2", "Bob", "b@example.com", "Bio2", new byte[10]);

        VerticalPartitioning.PartitionStats stats = db.getStats();
        assertEquals(2, stats.hotRecords);
        assertEquals(2, stats.coldRecords);
    }
}
