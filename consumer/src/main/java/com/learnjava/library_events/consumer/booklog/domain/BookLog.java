package com.learnjava.library_events.consumer.booklog.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record BookLog(BookLogId id, BookId bookId, BookLogType type, BookLogMessage message, Instant occurredAt) {

    public BookLog {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(bookId, "bookId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(message, "message must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
        occurredAt = occurredAt.truncatedTo(ChronoUnit.MICROS);
    }

    public static BookLog create(BookId bookId, BookLogType type, BookLogMessage message, Instant occurredAt) {
        return new BookLog(BookLogId.generate(), bookId, type, message, occurredAt);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BookLog other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
