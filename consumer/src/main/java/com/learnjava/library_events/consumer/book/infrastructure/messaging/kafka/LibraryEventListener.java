package com.learnjava.library_events.consumer.book.infrastructure.messaging.kafka;

import com.learnjava.library_events.consumer.booklog.application.OnLibraryEventSaveLog;
import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.book.LibraryEventTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Component
public class LibraryEventListener {

    private static final Logger log = LoggerFactory.getLogger(LibraryEventListener.class);

    private final OnLibraryEventSaveLog onLibraryEventSaveLog;

    public LibraryEventListener(OnLibraryEventSaveLog onLibraryEventSaveLog) {
        this.onLibraryEventSaveLog = onLibraryEventSaveLog;
    }

    @KafkaListener(
            topics = "${app.kafka.topic-prefix}" + LibraryEventTopics.LIBRARY_EVENT,
            properties = "spring.json.value.default.type=com.learnjava.library_events.contracts.book.LibraryEvent")
    public void on(@Payload LibraryEvent event,
                   @Header(KafkaHeaders.RECEIVED_KEY) UUID key,
                   @Header("occurred-at") byte[] occurredAt) {
        log.info("Library event received: key={}, type={}, book={}", key, event.libraryEventType(), event.book());
        onLibraryEventSaveLog.execute(event, Instant.parse(new String(occurredAt, StandardCharsets.UTF_8)));
    }
}
