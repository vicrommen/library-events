package com.learnjava.library_events.producer.shared.infrastructure.messaging.kafka;

import com.learnjava.library_events.shared.domain.DomainEvent;

import java.util.Map;

public interface EventHeadersStrategy {

    Map<String, String> headersFor(DomainEvent event);
}
