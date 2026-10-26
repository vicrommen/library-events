package com.learnjava.library_events.contracts.book;

import com.learnjava.library_events.shared.domain.DomainEvent;
import com.learnjava.library_events.shared.domain.EventNaming;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@EventNaming(LibraryEventTopics.LIBRARY_EVENT)
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
