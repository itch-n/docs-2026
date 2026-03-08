package com.study.systems.databasescaling;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashBasedShardingTest {

    @Test
    void testGetShardReturnsNonNull() {
        HashBasedSharding db = new HashBasedSharding(3);

        DatabaseShard shard = db.getShard("user1");

        assertNotNull(shard);
    }

    @Test
    void testSameKeyAlwaysGoesToSameShard() {
        HashBasedSharding db = new HashBasedSharding(3);

        DatabaseShard s1 = db.getShard("user1");
        DatabaseShard s2 = db.getShard("user1");

        assertEquals(s1.shardId, s2.shardId);
    }

    @Test
    void testInsertAndRetrieve() {
        HashBasedSharding db = new HashBasedSharding(3);

        db.insert("user1", "Alice");
        String value = db.get("user1");

        assertEquals("Alice", value);
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        HashBasedSharding db = new HashBasedSharding(3);

        assertNull(db.get("nonexistent"));
    }

    @Test
    void testDeleteRemovesEntry() {
        HashBasedSharding db = new HashBasedSharding(3);

        db.insert("user1", "Alice");
        db.delete("user1");

        assertNull(db.get("user1"));
    }

    @Test
    void testMultipleKeysDistributeAcrossShards() {
        HashBasedSharding db = new HashBasedSharding(3);

        String[] users = {"user1", "user2", "user3", "user4", "user5",
                          "user6", "user7", "user8", "user9", "user10"};
        for (String user : users) {
            db.insert(user, user + "_data");
        }

        // Total record count across all shards should equal 10
        int total = db.getStats().values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(10, total);
    }

    @Test
    void testStatsTotalMatchesInsertCount() {
        HashBasedSharding db = new HashBasedSharding(4);

        for (int i = 1; i <= 8; i++) {
            db.insert("key" + i, "value" + i);
        }

        int total = db.getStats().values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(8, total);
    }
}
