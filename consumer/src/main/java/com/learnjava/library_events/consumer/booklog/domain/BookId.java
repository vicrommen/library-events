package com.learnjava.library_events.consumer.booklog.domain;

import java.util.Objects;
import java.util.UUID;

public record BookId(UUID value) {

    public BookId {
        Objects.requireNonNull(value, "BookId value must not be null");
    }

    public static BookId of(String value) {
        return new BookId(UUID.fromString(value));
    }
}
