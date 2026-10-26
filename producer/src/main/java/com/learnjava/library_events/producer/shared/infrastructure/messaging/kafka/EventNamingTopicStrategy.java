package com.learnjava.library_events.producer.shared.infrastructure.messaging.kafka;

import com.learnjava.library_events.shared.domain.DomainEvent;
import com.learnjava.library_events.shared.domain.EventNaming;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventNamingTopicStrategy implements TopicNamingStrategy {

    private final String defaultPrefix;
    private final Map<Class<?>, String> topics = new ConcurrentHashMap<>();

    public EventNamingTopicStrategy(@Value("${app.kafka.topic-prefix}") String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    @Override
    public String topicFor(Class<? extends DomainEvent> eventType) {
        return topics.computeIfAbsent(eventType, this::resolve);
    }

    private String resolve(Class<?> eventType) {
        EventNaming naming = eventType.getAnnotation(EventNaming.class);
        if (naming == null) {
            return defaultPrefix + toKebabCase(eventType.getSimpleName());
        }
        String prefix = naming.prefix().isEmpty() ? defaultPrefix : naming.prefix();
        return prefix + naming.value();
    }

    private static String toKebabCase(String name) {
        return name
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1-$2")
                .replaceAll("([a-z0-9])([A-Z])", "$1-$2")
                .toLowerCase();
    }
}
