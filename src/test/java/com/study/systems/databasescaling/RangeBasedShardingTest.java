package com.study.systems.databasescaling;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RangeBasedShardingTest {

    @Test
    void testInsertAndRetrieve() {
        List<String> ranges = Arrays.asList("M", "Z");
        RangeBasedSharding db = new RangeBasedSharding(ranges);

        db.insert("Alice", "alice_data");

        assertEquals("alice_data", db.get("Alice"));
    }

    @Test
    void testNamesBeforeMGoToFirstShard() {
        List<String> ranges = Arrays.asList("M", "Z");
        RangeBasedSharding db = new RangeBasedSharding(ranges);

        // Alice, Bob, Charlie all < "M"
        DatabaseShard sAlice = db.getShard("Alice");
        DatabaseShard sBob = db.getShard("Bob");

        assertEquals(sAlice.shardId, sBob.shardId);
    }

    @Test
    void testNamesBetweenMAndZGoToSecondShard() {
        List<String> ranges = Arrays.asList("M", "Z");
        RangeBasedSharding db = new RangeBasedSharding(ranges);

        DatabaseShard sMike = db.getShard("Mike");
        DatabaseShard sNancy = db.getShard("Nancy");

        assertEquals(sMike.shardId, sNancy.shardId);
    }

    @Test
    void testNamesAfterZGoToLastShard() {
        List<String> ranges = Arrays.asList("M", "Z");
        RangeBasedSharding db = new RangeBasedSharding(ranges);

        DatabaseShard sZoe = db.getShard("Zoe");
        DatabaseShard sZachary = db.getShard("Zachary");

        assertEquals(sZoe.shardId, sZachary.shardId);
    }

    @Test
    void testDifferentRangesGoToDifferentShards() {
        List<String> ranges = Arrays.asList("M", "Z");
        RangeBasedSharding db = new RangeBasedSharding(ranges);

        DatabaseShard earlyRange = db.getShard("Alice");   // < M
        DatabaseShard midRange = db.getShard("Mike");      // >= M, < Z
        DatabaseShard lateRange = db.getShard("Zoe");      // >= Z

        assertNotEquals(earlyRange.shardId, midRange.shardId);
        assertNotEquals(midRange.shardId, lateRange.shardId);
    }

    @Test
    void testRangeQueryReturnsResults() {
        List<String> ranges = Arrays.asList("M", "Z");
        RangeBasedSharding db = new RangeBasedSharding(ranges);

        db.insert("Mike", "mike_data");
        db.insert("Nancy", "nancy_data");
        db.insert("Oscar", "oscar_data");

        List<String> results = db.rangeQuery("M", "P");

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
}
