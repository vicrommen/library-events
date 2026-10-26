package com.learnjava.library_events_producer_v2.shared.infrastructure.messaging.kafka;

import com.learnjava.library_events.shared.domain.DomainEvent;

public interface TopicNamingStrategy {

    String topicFor(Class<? extends DomainEvent> eventType);
}
