package com.learnjava.library_events_producer_v2.shared.infrastructure.messaging.kafka;

import com.learnjava.library_events_producer_v2.shared.domain.DomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class DefaultEventHeadersStrategy implements EventHeadersStrategy {

    private final String source;

    public DefaultEventHeadersStrategy(@Value("${spring.application.name}") String source) {
        this.source = source;
    }

    @Override
    public Map<String, String> headersFor(DomainEvent event) {
        return Map.of(
                "event-id", UUID.randomUUID().toString(),
                "event-type", event.getClass().getSimpleName(),
                "occurred-at", Instant.now().toString(),
                "source", source);
    }
}
