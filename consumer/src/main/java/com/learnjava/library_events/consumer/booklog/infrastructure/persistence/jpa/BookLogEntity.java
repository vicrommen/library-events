package com.learnjava.library_events.consumer.booklog.infrastructure.persistence.jpa;

import com.learnjava.library_events.consumer.booklog.domain.BookLogType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "book_log")
public class BookLogEntity {

    @Id
    private UUID id;

    @Column(name = "book_id", nullable = false)
    private UUID bookId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BookLogType type;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    protected BookLogEntity() {
    }

    BookLogEntity(UUID id, UUID bookId, BookLogType type, String message, Instant occurredAt) {
        this.id = id;
        this.bookId = bookId;
        this.type = type;
        this.message = message;
        this.occurredAt = occurredAt;
    }

    UUID id() {
        return id;
    }

    UUID bookId() {
        return bookId;
    }

    BookLogType type() {
        return type;
    }

    String message() {
        return message;
    }

    Instant occurredAt() {
        return occurredAt;
    }
}
