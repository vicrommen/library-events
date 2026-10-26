package com.learnjava.library_events.shared.domain;

import java.util.List;

public interface EventPublisher {

    void publish(List<? extends DomainEvent> events);
}
