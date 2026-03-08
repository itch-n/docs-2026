package com.study.systems.eventsourcing;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for EventSourcing abstractions.
 *
 * Because EventSourcing contains only interfaces and an abstract class,
 * these tests use minimal concrete implementations to drive the public API.
 * All methods throw UnsupportedOperationException or behave incorrectly until
 * the learner implements the TODO stubs.
 */
class EventSourcingTest {

    // ------------------------------------------------------------------
    // Minimal concrete event type for testing
    // ------------------------------------------------------------------

    record TestEvent(String aggregateId, long version, long occurredAt, String payload)
            implements EventSourcing.DomainEvent {}

    record TestCommand(String aggregateId, String instruction)
            implements EventSourcing.Command {}

    // ------------------------------------------------------------------
    // Minimal concrete Aggregate for testing replayAll / raiseEvent
    // ------------------------------------------------------------------

    static class CounterAggregate extends EventSourcing.Aggregate {

        int count = 0;

        @Override
        protected void apply(EventSourcing.DomainEvent event) {
            if (event instanceof TestEvent te) {
                if ("increment".equals(te.payload())) count++;
            }
            // version tracking delegated to base class — learner must implement
        }

        @Override
        public List<EventSourcing.DomainEvent> handleCommand(EventSourcing.Command command) {
            if (command instanceof TestCommand tc && "increment".equals(tc.instruction())) {
                TestEvent e = new TestEvent(tc.aggregateId(), getVersion() + 1,
                        System.currentTimeMillis(), "increment");
                raiseEvent(e);
                return getPendingEvents();
            }
            return List.of();
        }
    }

    // ------------------------------------------------------------------
    // Minimal in-memory EventStore implementation
    // ------------------------------------------------------------------

    static class InMemoryEventStore implements EventSourcing.EventStore {
        private final List<EventSourcing.DomainEvent> events = new ArrayList<>();
        private long currentVersion = 0;

        @Override
        public void append(String aggregateId, List<EventSourcing.DomainEvent> newEvents,
                           long expectedVersion) {
            // TODO: learner must implement optimistic concurrency check
            if (currentVersion != expectedVersion) {
                throw new RuntimeException("Optimistic concurrency conflict");
            }
            events.addAll(newEvents);
            currentVersion += newEvents.size();
        }

        @Override
        public List<EventSourcing.DomainEvent> load(String aggregateId) {
            // TODO: learner must implement filtering by aggregateId
            return new ArrayList<>(events);
        }

        @Override
        public List<EventSourcing.DomainEvent> loadFrom(String aggregateId, long fromVersion) {
            // TODO: learner must implement version-filtered load
            return events.stream()
                    .filter(e -> e.version() >= fromVersion)
                    .toList();
        }
    }

    // ------------------------------------------------------------------
    // Aggregate.replayAll tests
    // ------------------------------------------------------------------

    @Test
    void testReplayAllAppliesEventsToAggregate() {
        CounterAggregate agg = new CounterAggregate();
        List<EventSourcing.DomainEvent> history = List.of(
            new TestEvent("agg-1", 1, 1000L, "increment"),
            new TestEvent("agg-1", 2, 2000L, "increment"),
            new TestEvent("agg-1", 3, 3000L, "increment")
        );

        agg.replayAll(history);

        // After replay the counter should reflect all three increments
        assertEquals(3, agg.count);
    }

    @Test
    void testReplayAllWithEmptyListLeavesAggregateUnchanged() {
        CounterAggregate agg = new CounterAggregate();

        agg.replayAll(List.of());

        assertEquals(0, agg.count);
    }

    // ------------------------------------------------------------------
    // Aggregate.raiseEvent / getPendingEvents tests
    // ------------------------------------------------------------------

    @Test
    void testHandleCommandProducesEvent() {
        CounterAggregate agg = new CounterAggregate();
        TestCommand cmd = new TestCommand("agg-1", "increment");

        List<EventSourcing.DomainEvent> produced = agg.handleCommand(cmd);

        assertNotNull(produced);
        assertFalse(produced.isEmpty());
    }

    @Test
    void testPendingEventsAreAccessibleAfterCommand() {
        CounterAggregate agg = new CounterAggregate();
        agg.handleCommand(new TestCommand("agg-1", "increment"));

        List<EventSourcing.DomainEvent> pending = agg.getPendingEvents();

        assertNotNull(pending);
        assertFalse(pending.isEmpty());
    }

    // ------------------------------------------------------------------
    // EventStore contract tests
    // ------------------------------------------------------------------

    @Test
    void testLoadReturnsEmptyListForUnknownAggregate() {
        InMemoryEventStore store = new InMemoryEventStore();

        List<EventSourcing.DomainEvent> events = store.load("unknown-agg");

        assertNotNull(events);
        assertTrue(events.isEmpty());
    }

    @Test
    void testAppendAndLoadRoundTrip() {
        InMemoryEventStore store = new InMemoryEventStore();
        List<EventSourcing.DomainEvent> toAppend = List.of(
            new TestEvent("agg-1", 1, 1000L, "increment")
        );

        store.append("agg-1", toAppend, 0L);
        List<EventSourcing.DomainEvent> loaded = store.load("agg-1");

        assertEquals(1, loaded.size());
        assertEquals("agg-1", loaded.get(0).aggregateId());
    }

    @Test
    void testOptimisticConcurrencyConflictThrows() {
        InMemoryEventStore store = new InMemoryEventStore();
        // Append one event to advance version to 1
        store.append("agg-1", List.of(new TestEvent("agg-1", 1, 1000L, "increment")), 0L);

        // Now try to append again with stale expectedVersion 0
        assertThrows(RuntimeException.class, () ->
            store.append("agg-1",
                List.of(new TestEvent("agg-1", 2, 2000L, "increment")),
                0L  // stale — actual version is now 1
            )
        );
    }

    // ------------------------------------------------------------------
    // SnapshotStore.Snapshot record test
    // ------------------------------------------------------------------

    @Test
    void testSnapshotRecordStoresFields() {
        byte[] state = "serialised-state".getBytes();
        EventSourcing.SnapshotStore.Snapshot snap =
                new EventSourcing.SnapshotStore.Snapshot("agg-1", 5L, state);

        assertEquals("agg-1", snap.aggregateId());
        assertEquals(5L, snap.version());
        assertArrayEquals(state, snap.state());
    }
}
