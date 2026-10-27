package com.learnjava.library_events.consumer.booklog.domain;

import com.learnjava.library_events.shared.domain.InvalidValueException;

public record BookLogMessage(String value) {

    public BookLogMessage {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("message", value);
        }
        value = value.strip();
    }

    public static BookLogMessage describing(BookLogType type, String name, String author) {
        String action = switch (type) {
            case BOOK_CREATED -> "created";
            case BOOK_UPDATED -> "updated";
        };
        return new BookLogMessage("Book '%s' by %s was %s".formatted(name, author, action));
    }
}
