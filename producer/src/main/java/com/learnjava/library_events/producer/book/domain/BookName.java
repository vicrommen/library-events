package com.learnjava.library_events.producer.book.domain;

import com.learnjava.library_events.shared.domain.InvalidValueException;

public record BookName(String value) {

    public BookName {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("name", value);
        }
        value = value.strip();
    }
}
