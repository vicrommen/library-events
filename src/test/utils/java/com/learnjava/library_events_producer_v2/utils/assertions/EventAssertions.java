package com.learnjava.library_events_producer_v2.utils.assertions;

import com.learnjava.library_events_producer_v2.shared.domain.DomainEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public final class EventAssertions {

    private EventAssertions() {
    }

    public static <K, V extends DomainEvent> void assertPublished(ConsumerRecord<K, V> record, V expected) {
        assertThat(record.key()).isEqualTo(expected.key().orElse(null));
        assertThat(record.value()).isEqualTo(expected);
        assertThatNoException().isThrownBy(() -> UUID.fromString(header(record, "event-id")));
        assertThat(header(record, "event-type")).isEqualTo(expected.getClass().getSimpleName());
        assertThatNoException().isThrownBy(() -> Instant.parse(header(record, "occurred-at")));
        assertThat(header(record, "source")).isNotBlank();
    }

    private static String header(ConsumerRecord<?, ?> record, String name) {
        Header header = record.headers().lastHeader(name);
        assertThat(header).as("header '%s'", name).isNotNull();
        return new String(header.value(), StandardCharsets.UTF_8);
    }
}
