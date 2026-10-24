package com.learnjava.library_events_producer_v2.shared.infrastructure.messaging.mock;

import com.learnjava.library_events_producer_v2.shared.domain.DomainEvent;
import com.learnjava.library_events_producer_v2.shared.domain.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MockEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MockEventPublisher.class);

    private final List<DomainEvent> publishedEvents = new CopyOnWriteArrayList<>();

    @Override
    public void publish(List<? extends DomainEvent> events) {
        events.forEach(event -> log.info("Event published: {}", event));
        publishedEvents.addAll(events);
    }
}
