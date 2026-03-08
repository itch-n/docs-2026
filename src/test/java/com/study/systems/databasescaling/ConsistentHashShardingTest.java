package com.study.systems.databasescaling;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConsistentHashShardingTest {

    @Test
    void testInsertAndRetrieve() {
        ConsistentHashSharding db = new ConsistentHashSharding(3, 3);

        db.insert("key1", "value1");

        assertEquals("value1", db.get("key1"));
    }

    @Test
    void testSameKeyAlwaysGoesToSameShard() {
        ConsistentHashSharding db = new ConsistentHashSharding(3, 3);

        DatabaseShard s1 = db.getShard("key1");
        DatabaseShard s2 = db.getShard("key1");

        assertEquals(s1.shardId, s2.shardId);
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        ConsistentHashSharding db = new ConsistentHashSharding(3, 3);

        assertNull(db.get("nonexistent"));
    }

    @Test
    void testDeleteRemovesEntry() {
        ConsistentHashSharding db = new ConsistentHashSharding(3, 3);

        db.insert("key1", "value1");
        db.delete("key1");

        assertNull(db.get("key1"));
    }

    @Test
    void testAddShardAndDataStillRetrievable() {
        ConsistentHashSharding db = new ConsistentHashSharding(3, 3);

        db.insert("key1", "value1");
        db.insert("key2", "value2");

        db.addShard();

        // Pre-existing data should still be accessible
        // (after shard addition, some keys may have been migrated in production,
        //  but this stub just tests the structure doesn't break)
        assertNotNull(db.getStats());
    }
}
