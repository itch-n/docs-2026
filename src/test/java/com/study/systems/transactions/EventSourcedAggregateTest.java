package com.study.systems.transactions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventSourcedAggregateTest {

    @Test
    void testInitialVersionIsZero() {
        EventSourcedAggregate agg = new EventSourcedAggregate("account123");

        assertEquals(0, agg.getVersion());
    }

    @Test
    void testApplyEventIncrementsVersion() {
        EventSourcedAggregate agg = new EventSourcedAggregate("account123");

        EventSourcedAggregate.DomainEvent event =
            new EventSourcedAggregate.DomainEvent("account123", "AccountCreated");
        agg.applyEvent(event);

        assertEquals(1, agg.getVersion());
    }

    @Test
    void testMultipleEventsIncrementVersionCorrectly() {
        EventSourcedAggregate agg = new EventSourcedAggregate("account123");

        EventSourcedAggregate.DomainEvent created =
            new EventSourcedAggregate.DomainEvent("account123", "AccountCreated");
        created.put("initialBalance", 1000);
        agg.applyEvent(created);

        EventSourcedAggregate.DomainEvent deposited =
            new EventSourcedAggregate.DomainEvent("account123", "MoneyDeposited");
        deposited.put("amount", 500);
        agg.applyEvent(deposited);

        EventSourcedAggregate.DomainEvent withdrawn =
            new EventSourcedAggregate.DomainEvent("account123", "MoneyWithdrawn");
        withdrawn.put("amount", 200);
        agg.applyEvent(withdrawn);

        assertEquals(3, agg.getVersion());
    }

    @Test
    void testGetAllEventsReturnsAppliedEvents() {
        EventSourcedAggregate agg = new EventSourcedAggregate("account123");

        EventSourcedAggregate.DomainEvent e1 =
            new EventSourcedAggregate.DomainEvent("account123", "AccountCreated");
        EventSourcedAggregate.DomainEvent e2 =
            new EventSourcedAggregate.DomainEvent("account123", "MoneyDeposited");

        agg.applyEvent(e1);
        agg.applyEvent(e2);

        assertEquals(2, agg.getAllEvents().size());
    }

    @Test
    void testReplayEventsReconstructsVersion() {
        EventSourcedAggregate original = new EventSourcedAggregate("account123");

        EventSourcedAggregate.DomainEvent created =
            new EventSourcedAggregate.DomainEvent("account123", "AccountCreated");
        EventSourcedAggregate.DomainEvent deposited =
            new EventSourcedAggregate.DomainEvent("account123", "MoneyDeposited");
        EventSourcedAggregate.DomainEvent withdrawn =
            new EventSourcedAggregate.DomainEvent("account123", "MoneyWithdrawn");

        original.applyEvent(created);
        original.applyEvent(deposited);
        original.applyEvent(withdrawn);

        // Reconstruct from event log
        EventSourcedAggregate replayed = new EventSourcedAggregate("account123");
        replayed.replayEvents(original.getAllEvents());

        assertEquals(original.getVersion(), replayed.getVersion());
    }
}
