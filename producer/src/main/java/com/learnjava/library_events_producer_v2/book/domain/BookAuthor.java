package com.learnjava.library_events_producer_v2.book.domain;

import com.learnjava.library_events.shared.domain.InvalidValueException;

public record BookAuthor(String value) {

    public BookAuthor {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("author", value);
        }
        value = value.strip();
    }
}
