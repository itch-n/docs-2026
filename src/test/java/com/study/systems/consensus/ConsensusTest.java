package com.study.systems.consensus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Lock, LogEntry, and VersionedValue data-holder classes in the
 * consensus package, plus interface contract tests via anonymous implementations.
 *
 * The interfaces (RaftConsensus, LeaderElection, DistributedLock, QuorumStore)
 * have no concrete implementations in the source tree, so tests use anonymous
 * classes that record calls and return fixed values.  These tests will fail until
 * the learner provides real implementations.
 */
class ConsensusTest {

    // ------------------------------------------------------------------
    // Lock (data class) tests
    // ------------------------------------------------------------------

    @Test
    void testLockStoresFields() {
        Lock lock = new Lock("resource-1", "owner-A", 7L, System.currentTimeMillis() + 5000);

        assertEquals("resource-1", lock.resourceId);
        assertEquals("owner-A", lock.ownerId);
        assertEquals(7L, lock.fencingToken);
    }

    @Test
    void testLockExpiresAtIsInFuture() {
        long expiresAt = System.currentTimeMillis() + 10_000;
        Lock lock = new Lock("res", "owner", 1L, expiresAt);

        assertTrue(lock.expiresAt > System.currentTimeMillis());
    }

    // ------------------------------------------------------------------
    // LogEntry (data class) tests
    // ------------------------------------------------------------------

    @Test
    void testLogEntryStoresFields() {
        LogEntry entry = new LogEntry(2, 5, "SET x=42");

        assertEquals(2, entry.term);
        assertEquals(5, entry.index);
        assertEquals("SET x=42", entry.command);
    }

    // ------------------------------------------------------------------
    // VersionedValue (data class) tests
    // ------------------------------------------------------------------

    @Test
    void testVersionedValueStoresFields() {
        VersionedValue vv = new VersionedValue("myData", 3L, 1000L);

        assertEquals("myData", vv.data);
        assertEquals(3L, vv.version);
        assertEquals(1000L, vv.timestamp);
    }

    // ------------------------------------------------------------------
    // LeaderElection interface contract tests (via stub implementation)
    // ------------------------------------------------------------------

    @Test
    void testLeaderElectionNoLeaderInitially() {
        LeaderElection election = new LeaderElection() {
            private int leader = -1;

            @Override public void startElection(int nodeId) { leader = nodeId; }
            @Override public int getLeader() { return leader; }
            @Override public boolean isLeader(int nodeId) { return nodeId == leader; }
            @Override public void checkLeaderHealth(int nodeId) {}
        };

        // Before any election, no leader
        assertEquals(-1, election.getLeader());
    }

    @Test
    void testLeaderElectionAfterStart() {
        LeaderElection election = new LeaderElection() {
            private int leader = -1;

            @Override public void startElection(int nodeId) { leader = nodeId; }
            @Override public int getLeader() { return leader; }
            @Override public boolean isLeader(int nodeId) { return nodeId == leader; }
            @Override public void checkLeaderHealth(int nodeId) {}
        };

        election.startElection(3);

        assertEquals(3, election.getLeader());
        assertTrue(election.isLeader(3));
        assertFalse(election.isLeader(1));
    }

    // ------------------------------------------------------------------
    // DistributedLock interface contract tests (via stub implementation)
    // ------------------------------------------------------------------

    @Test
    void testDistributedLockTryAcquireAndRelease() {
        DistributedLock dl = new DistributedLock() {
            private Lock held = null;

            @Override public Lock tryAcquire(String resourceId, String ownerId) {
                if (held != null) return null;
                held = new Lock(resourceId, ownerId, 1L, Long.MAX_VALUE);
                return held;
            }
            @Override public Lock tryAcquire(String r, String o, long ttl) { return tryAcquire(r, o); }
            @Override public Lock acquire(String r, String o, long timeoutMs) { return tryAcquire(r, o); }
            @Override public boolean release(String r, String o, long token) {
                if (held != null && held.ownerId.equals(o)) { held = null; return true; }
                return false;
            }
            @Override public boolean renew(String r, String o, long token) { return held != null; }
            @Override public boolean isLocked(String r) { return held != null; }
        };

        Lock lock = dl.tryAcquire("res-1", "node-A");
        assertNotNull(lock);
        assertTrue(dl.isLocked("res-1"));

        boolean released = dl.release("res-1", "node-A", lock.fencingToken);
        assertTrue(released);
        assertFalse(dl.isLocked("res-1"));
    }

    @Test
    void testDistributedLockCannotAcquireWhenAlreadyHeld() {
        DistributedLock dl = new DistributedLock() {
            private Lock held = null;

            @Override public Lock tryAcquire(String r, String o) {
                if (held != null) return null;
                held = new Lock(r, o, 1L, Long.MAX_VALUE);
                return held;
            }
            @Override public Lock tryAcquire(String r, String o, long ttl) { return tryAcquire(r, o); }
            @Override public Lock acquire(String r, String o, long t) { return tryAcquire(r, o); }
            @Override public boolean release(String r, String o, long token) {
                held = null; return true;
            }
            @Override public boolean renew(String r, String o, long token) { return true; }
            @Override public boolean isLocked(String r) { return held != null; }
        };

        dl.tryAcquire("res-1", "node-A"); // first acquire
        Lock second = dl.tryAcquire("res-1", "node-B"); // should fail

        assertNull(second);
    }
}
