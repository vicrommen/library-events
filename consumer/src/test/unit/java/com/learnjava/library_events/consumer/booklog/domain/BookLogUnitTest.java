package com.learnjava.library_events.consumer.booklog.domain;

import com.learnjava.library_events.consumer.utils.mother.BookLogMother;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BookLogUnitTest {

    @Test
    void shouldCreateBookLog() {
        BookId bookId = BookLogMother.randomBookId();
        BookLogMessage message = BookLogMother.randomMessage();
        Instant occurredAt = Instant.parse("2026-10-27T10:00:00.123456Z");

        BookLog bookLog = BookLog.create(bookId, BookLogType.BOOK_UPDATED, message, occurredAt);

        assertThat(bookLog.id()).isNotNull();
        assertThat(bookLog.bookId()).isEqualTo(bookId);
        assertThat(bookLog.type()).isEqualTo(BookLogType.BOOK_UPDATED);
        assertThat(bookLog.message()).isEqualTo(message);
        assertThat(bookLog.occurredAt()).isEqualTo(occurredAt);
    }

    @Test
    void shouldGenerateDifferentIdsOnCreate() {
        BookId bookId = BookLogMother.randomBookId();
        BookLogMessage message = BookLogMother.randomMessage();
        Instant occurredAt = Instant.now();

        BookLog first = BookLog.create(bookId, BookLogType.BOOK_CREATED, message, occurredAt);
        BookLog second = BookLog.create(bookId, BookLogType.BOOK_CREATED, message, occurredAt);

        assertThat(first.id()).isNotEqualTo(second.id());
    }

    @Test
    void shouldTruncateOccurredAtToMicros() {
        Instant occurredAt = Instant.parse("2026-10-27T10:00:00.123456789Z");

        BookLog bookLog = BookLog.create(BookLogMother.randomBookId(), BookLogType.BOOK_CREATED,
                BookLogMother.randomMessage(), occurredAt);

        assertThat(bookLog.occurredAt()).isEqualTo(occurredAt.truncatedTo(ChronoUnit.MICROS));
    }

    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        BookLogId id = BookLogId.generate();

        assertThat(BookLogMother.withId(id)).isEqualTo(BookLogMother.withId(id));
    }

    @Test
    void shouldNotBeEqualWhenIdsAreDifferent() {
        assertThat(BookLogMother.random()).isNotEqualTo(BookLogMother.random());
    }

    @Test
    void shouldRejectNullId() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookLogMother.create(null, BookLogMother.randomBookId(), BookLogType.BOOK_CREATED,
                        BookLogMother.randomMessage(), Instant.now()))
                .withMessage("id must not be null");
    }

    @Test
    void shouldRejectNullBookId() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookLogMother.create(BookLogId.generate(), null, BookLogType.BOOK_CREATED,
                        BookLogMother.randomMessage(), Instant.now()))
                .withMessage("bookId must not be null");
    }

    @Test
    void shouldRejectNullType() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookLogMother.create(BookLogId.generate(), BookLogMother.randomBookId(), null,
                        BookLogMother.randomMessage(), Instant.now()))
                .withMessage("type must not be null");
    }

    @Test
    void shouldRejectNullMessage() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookLogMother.create(BookLogId.generate(), BookLogMother.randomBookId(),
                        BookLogType.BOOK_CREATED, null, Instant.now()))
                .withMessage("message must not be null");
    }

    @Test
    void shouldRejectNullOccurredAt() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookLogMother.create(BookLogId.generate(), BookLogMother.randomBookId(),
                        BookLogType.BOOK_CREATED, BookLogMother.randomMessage(), null))
                .withMessage("occurredAt must not be null");
    }
}
