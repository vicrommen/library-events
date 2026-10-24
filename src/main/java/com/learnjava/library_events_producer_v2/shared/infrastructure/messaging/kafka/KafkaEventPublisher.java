package com.learnjava.library_events_producer_v2.shared.infrastructure.messaging.kafka;

import com.learnjava.library_events_producer_v2.shared.domain.DomainEvent;
import com.learnjava.library_events_producer_v2.shared.domain.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Primary
@Component
public class KafkaEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private final KafkaTemplate<UUID, DomainEvent> kafkaTemplate;
    private final TopicNamingStrategy topicNamingStrategy;

    public KafkaEventPublisher(KafkaTemplate<UUID, DomainEvent> kafkaTemplate, TopicNamingStrategy topicNamingStrategy) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicNamingStrategy = topicNamingStrategy;
    }

    @Override
    public void publish(List<? extends DomainEvent> events) {
        events.forEach(this::publish);
    }

    private void publish(DomainEvent event) {
        String topic = topicNamingStrategy.topicFor(event.getClass());
        UUID key = event.key().orElse(null);

        kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Event could not be sent: topic={}, key={}, event={}", topic, key, event, ex);
            } else {
                log.info("Event sent: topic={}, partition={}, offset={}, key={}",
                        topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset(), key);
            }
        });
    }
}
