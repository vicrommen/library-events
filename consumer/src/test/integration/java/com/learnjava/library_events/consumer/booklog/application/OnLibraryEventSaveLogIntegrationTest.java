package com.learnjava.library_events.consumer.booklog.application;

import com.learnjava.library_events.consumer.booklog.domain.BookId;
import com.learnjava.library_events.consumer.booklog.domain.BookLog;
import com.learnjava.library_events.consumer.booklog.domain.BookLogQueryRepository;
import com.learnjava.library_events.consumer.utils.annotations.IntegrationTest;
import com.learnjava.library_events.consumer.utils.mother.BookLogMother;
import com.learnjava.library_events.consumer.utils.mother.KafkaProducerMother;
import com.learnjava.library_events.consumer.utils.mother.LibraryEventRecordMother;
import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.utils.mother.LibraryEventMother;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.learnjava.library_events.consumer.utils.assertions.BookLogAssertions.assertBookLog;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@IntegrationTest
class OnLibraryEventSaveLogIntegrationTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private BookLogQueryRepository bookLogQueryRepository;

    @Test
    void shouldSaveBookCreatedLog() {
        LibraryEvent event = LibraryEventMother.newBook();
        Instant occurredAt = Instant.now();

        send(event, occurredAt);

        BookLog expected = BookLogMother.bookCreated(event, occurredAt);
        assertBookLog(findSavedLog(expected.bookId()), expected);
    }

    @Test
    void shouldSaveBookUpdatedLog() {
        LibraryEvent event = LibraryEventMother.updateBook();
        Instant occurredAt = Instant.now();

        send(event, occurredAt);

        BookLog expected = BookLogMother.bookUpdated(event, occurredAt);
        assertBookLog(findSavedLog(expected.bookId()), expected);
    }

    private void send(LibraryEvent event, Instant occurredAt) {
        try (Producer<UUID, LibraryEvent> producer = KafkaProducerMother.createProducer(broker)) {
            producer.send(LibraryEventRecordMother.create(event, occurredAt));
        }
    }

    private BookLog findSavedLog(BookId bookId) {
        List<BookLog> logs = await().atMost(TIMEOUT)
                .until(() -> bookLogQueryRepository.findByBookId(bookId), found -> !found.isEmpty());
        assertThat(logs).hasSize(1);
        return logs.getFirst();
    }
}
