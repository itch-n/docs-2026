package com.study.systems.databasescaling;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MasterSlaveReplicationTest {

    @Test
    void testWriteAndReadReturnsValue() {
        MasterSlaveReplication db = new MasterSlaveReplication(2);

        db.write("key1", "value1");
        String result = db.read("key1");

        assertEquals("value1", result);
    }

    @Test
    void testMultipleWritesReadable() {
        MasterSlaveReplication db = new MasterSlaveReplication(2);

        db.write("key1", "value1");
        db.write("key2", "value2");
        db.write("key3", "value3");

        assertEquals("value1", db.read("key1"));
        assertEquals("value2", db.read("key2"));
        assertEquals("value3", db.read("key3"));
    }

    @Test
    void testReadMissingKeyReturnsNull() {
        MasterSlaveReplication db = new MasterSlaveReplication(2);

        assertNull(db.read("nonexistent"));
    }

    @Test
    void testDeleteRemovesEntry() {
        MasterSlaveReplication db = new MasterSlaveReplication(2);

        db.write("key1", "value1");
        db.delete("key1");

        assertNull(db.read("key1"));
    }
}
