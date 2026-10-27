package com.learnjava.library_events.producer.book.domain;

import com.learnjava.library_events.shared.domain.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

public record BookId(UUID value) {

    public BookId {
        Objects.requireNonNull(value, "BookId value must not be null");
    }

    public static BookId generate() {
        return new BookId(UuidGenerator.generate());
    }

    public static BookId of(String value) {
        return new BookId(UUID.fromString(value));
    }
}
