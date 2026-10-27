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

    public static BookLog bookCreated(BookId bookId, String name, String author, Instant occurredAt) {
        return create(bookId, BookLogType.BOOK_CREATED, name, author, occurredAt);
    }

    public static BookLog bookUpdated(BookId bookId, String name, String author, Instant occurredAt) {
        return create(bookId, BookLogType.BOOK_UPDATED, name, author, occurredAt);
    }

    private static BookLog create(BookId bookId, BookLogType type, String name, String author, Instant occurredAt) {
        return new BookLog(BookLogId.generate(), bookId, type, BookLogMessage.describing(type, name, author), occurredAt);
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
