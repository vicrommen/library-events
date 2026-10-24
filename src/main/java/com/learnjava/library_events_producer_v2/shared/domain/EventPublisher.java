package com.learnjava.library_events_producer_v2.shared.domain;

import java.util.List;

public interface EventPublisher {

    void publish(List<? extends DomainEvent> events);
}
