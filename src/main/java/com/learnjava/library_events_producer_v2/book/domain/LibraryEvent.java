package com.learnjava.library_events_producer_v2.book.domain;

import com.learnjava.library_events_producer_v2.shared.domain.DomainEvent;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record LibraryEvent(LibraryEventType libraryEventType, BookInfo book) implements DomainEvent {

    public LibraryEvent {
        Objects.requireNonNull(libraryEventType, "libraryEventType must not be null");
        Objects.requireNonNull(book, "book must not be null");
    }

    @Override
    public Optional<UUID> key() {
        return Optional.of(book.id());
    }

    public enum LibraryEventType {
        NEW,
        UPDATE
    }

    public record BookInfo(UUID id, String name, String author) {
    }
}
