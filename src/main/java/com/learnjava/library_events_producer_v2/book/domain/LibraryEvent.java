package com.learnjava.library_events_producer_v2.book.domain;

import java.util.Objects;
import java.util.UUID;

public record LibraryEvent(LibraryEventType libraryEventType, BookInfo book) {

    public LibraryEvent {
        Objects.requireNonNull(libraryEventType, "libraryEventType must not be null");
        Objects.requireNonNull(book, "book must not be null");
    }

    public enum LibraryEventType {
        NEW,
        UPDATE
    }

    public record BookInfo(UUID id, String name, String author) {
    }
}
