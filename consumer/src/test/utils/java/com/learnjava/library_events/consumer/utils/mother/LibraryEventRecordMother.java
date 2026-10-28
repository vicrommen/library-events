package com.learnjava.library_events.consumer.utils.mother;

import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.utils.kafka.KafkaTopics;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

public final class LibraryEventRecordMother {

    private LibraryEventRecordMother() {
    }

    public static ProducerRecord<UUID, LibraryEvent> create(LibraryEvent event, Instant occurredAt) {
        ProducerRecord<UUID, LibraryEvent> record = new ProducerRecord<>(KafkaTopics.LIBRARY_EVENT, event.book().id(), event);
        record.headers().add("occurred-at", occurredAt.toString().getBytes(StandardCharsets.UTF_8));
        return record;
    }
}
