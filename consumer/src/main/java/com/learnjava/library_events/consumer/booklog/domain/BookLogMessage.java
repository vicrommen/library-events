package com.learnjava.library_events.consumer.booklog.domain;

import com.learnjava.library_events.shared.domain.InvalidValueException;

public record BookLogMessage(String value) {

    public BookLogMessage {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("message", value);
        }
        value = value.strip();
    }
}
