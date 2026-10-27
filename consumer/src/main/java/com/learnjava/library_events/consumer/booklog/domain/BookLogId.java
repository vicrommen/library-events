package com.learnjava.library_events.consumer.booklog.domain;

import com.learnjava.library_events.shared.domain.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

public record BookLogId(UUID value) {

    public BookLogId {
        Objects.requireNonNull(value, "BookLogId value must not be null");
    }

    public static BookLogId generate() {
        return new BookLogId(UuidGenerator.generate());
    }

    public static BookLogId of(String value) {
        return new BookLogId(UUID.fromString(value));
    }
}
