package com.study.systems.eventsourcing;

import java.util.List;

public interface DomainEvent {
    String aggregateId();
    long version();
    long occurredAt(); // epoch millis
}
