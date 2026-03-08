package com.study.systems.multiregion;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class VectorClockTest {

    @Test
    void testNewClockHasNoEntries() {
        VectorClock vc = new VectorClock();

        assertNotNull(vc.entries());
        assertTrue(vc.entries().isEmpty());
    }

    @Test
    void testIncrementAddsEntry() {
        VectorClock vc = new VectorClock();

        vc.increment("us-east-1");

        assertEquals(1, vc.entries().get("us-east-1"));
    }

    @Test
    void testIncrementAccumulatesForSameNode() {
        VectorClock vc = new VectorClock();

        vc.increment("us-east-1");
        vc.increment("us-east-1");
        vc.increment("us-east-1");

        assertEquals(3, vc.entries().get("us-east-1"));
    }

    @Test
    void testHappensBeforeStrictPrecedence() {
        VectorClock v1 = new VectorClock();
        v1.increment("A"); // A=1

        VectorClock v2 = new VectorClock();
        v2.increment("A"); // A=1
        v2.increment("A"); // A=2

        assertTrue(v1.happensBefore(v2));
        assertFalse(v2.happensBefore(v1));
    }

    @Test
    void testEqualClocksDoNotHappenBeforeEachOther() {
        VectorClock v1 = new VectorClock();
        v1.increment("A");

        VectorClock v2 = new VectorClock();
        v2.increment("A");

        // Identical clocks — neither happens-before the other
        assertFalse(v1.happensBefore(v2));
        assertFalse(v2.happensBefore(v1));
    }

    @Test
    void testConcurrentClocksNeitherHappensBefore() {
        VectorClock v1 = new VectorClock();
        v1.increment("A"); // A=1, B=0

        VectorClock v2 = new VectorClock();
        v2.increment("B"); // A=0, B=1

        // Concurrent — neither causally precedes the other
        assertFalse(v1.happensBefore(v2));
        assertFalse(v2.happensBefore(v1));
    }

    @Test
    void testMergeProducesElementWiseMaximum() {
        VectorClock v1 = new VectorClock();
        v1.increment("A"); // A=1
        v1.increment("B"); // B=1

        VectorClock v2 = new VectorClock();
        v2.increment("B"); // B=1
        v2.increment("B"); // B=2
        v2.increment("C"); // C=1

        VectorClock merged = v1.merge(v2);

        assertNotNull(merged);
        assertEquals(1, merged.entries().get("A")); // max(1, 0)
        assertEquals(2, merged.entries().get("B")); // max(1, 2)
        assertEquals(1, merged.entries().get("C")); // max(0, 1)
    }

    @Test
    void testCopyConstructorProducesIndependentClock() {
        VectorClock original = new VectorClock();
        original.increment("A");

        VectorClock copy = new VectorClock(original);
        copy.increment("A"); // modifying copy should not affect original

        assertEquals(1, original.entries().get("A"));
        assertEquals(2, copy.entries().get("A"));
    }
}

class VersionedValueTest {

    @Test
    void testVersionedValueStoresFields() {
        VectorClock clock = new VectorClock();
        VersionedValue vv = new VersionedValue("hello", clock, 12345L);

        assertEquals("hello", vv.value);
        assertEquals(clock, vv.clock);
        assertEquals(12345L, vv.timestampMs);
    }
}

class ReplicationManagerTest {

    /** Last-write-wins resolver: always keeps the value with the later timestamp. */
    static ConflictResolver lww() {
        return (v1, v2) -> v1.timestampMs >= v2.timestampMs ? v1 : v2;
    }

    @Test
    void testWriteAndReadRoundTrip() {
        ReplicationManager rm = new ReplicationManager(
                "us-east-1", List.of("eu-west-1"), lww());

        rm.write("key1", "value1", "us-east-1");

        Optional<VersionedValue> result = rm.read("key1");
        assertTrue(result.isPresent());
        assertEquals("value1", result.get().value);
    }

    @Test
    void testReadUnknownKeyReturnsEmpty() {
        ReplicationManager rm = new ReplicationManager(
                "us-east-1", List.of(), lww());

        Optional<VersionedValue> result = rm.read("does-not-exist");

        assertTrue(result.isEmpty());
    }

    @Test
    void testReceiveRemoteWriteStoresNewKey() {
        ReplicationManager rm = new ReplicationManager(
                "us-east-1", List.of("eu-west-1"), lww());

        VectorClock clock = new VectorClock();
        clock.increment("eu-west-1");
        VersionedValue incoming = new VersionedValue("remote-value", clock, 1000L);

        rm.receiveRemoteWrite("key-remote", incoming);

        Optional<VersionedValue> local = rm.read("key-remote");
        assertTrue(local.isPresent());
        assertEquals("remote-value", local.get().value);
    }

    @Test
    void testReceiveRemoteWriteDoesNotOverwriteNewerLocalValue() {
        ReplicationManager rm = new ReplicationManager(
                "us-east-1", List.of("eu-west-1"), lww());

        // Local write first with a later clock
        VectorClock localClock = new VectorClock();
        localClock.increment("us-east-1");
        localClock.increment("us-east-1"); // local is ahead
        VersionedValue local = new VersionedValue("local-value", localClock, 2000L);
        rm.receiveRemoteWrite("shared-key", local);

        // Incoming remote write has an older clock
        VectorClock remoteClock = new VectorClock();
        remoteClock.increment("eu-west-1");
        VersionedValue remote = new VersionedValue("stale-remote", remoteClock, 500L);
        rm.receiveRemoteWrite("shared-key", remote);

        // The older remote should not overwrite the newer local
        Optional<VersionedValue> result = rm.read("shared-key");
        assertTrue(result.isPresent());
        assertEquals("local-value", result.get().value);
    }
}
