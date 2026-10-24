package com.learnjava.library_events_producer_v2.shared.domain;

import java.util.List;
import java.util.Objects;

public record Result<A, E>(A aggregate, List<E> events) {

    public Result {
        Objects.requireNonNull(aggregate, "aggregate must not be null");
        events = List.copyOf(events);
    }

    public static <A, E> Result<A, E> of(A aggregate, E event) {
        return new Result<>(aggregate, List.of(event));
    }

    public static <A, E> Result<A, E> withoutEvents(A aggregate) {
        return new Result<>(aggregate, List.of());
    }
}
