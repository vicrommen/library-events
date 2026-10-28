package com.learnjava.library_events.consumer.utils.assertions;

import com.learnjava.library_events.consumer.booklog.domain.BookLog;

import static org.assertj.core.api.Assertions.assertThat;

public final class BookLogAssertions {

    private BookLogAssertions() {
    }

    public static void assertBookLog(BookLog actual, BookLog expected) {
        assertThat(actual.id()).isNotNull();
        assertThat(actual.bookId()).isEqualTo(expected.bookId());
        assertThat(actual.type()).isEqualTo(expected.type());
        assertThat(actual.message()).isEqualTo(expected.message());
        assertThat(actual.occurredAt()).isEqualTo(expected.occurredAt());
    }
}
