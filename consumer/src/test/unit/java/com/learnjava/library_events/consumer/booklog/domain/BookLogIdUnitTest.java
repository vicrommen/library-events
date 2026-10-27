package com.learnjava.library_events.consumer.booklog.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BookLogIdUnitTest {

    @Test
    void shouldCreateFromUuid() {
        UUID value = UUID.randomUUID();

        assertThat(new BookLogId(value).value()).isEqualTo(value);
    }

    @Test
    void shouldRejectNullValue() {
        assertThatNullPointerException()
                .isThrownBy(() -> new BookLogId(null))
                .withMessage("BookLogId value must not be null");
    }

    @Test
    void shouldGenerateDifferentIds() {
        assertThat(BookLogId.generate()).isNotEqualTo(BookLogId.generate());
    }

    @Test
    void shouldCreateFromString() {
        UUID value = UUID.randomUUID();

        assertThat(BookLogId.of(value.toString())).isEqualTo(new BookLogId(value));
    }

    @Test
    void shouldRejectInvalidString() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BookLogId.of("not-a-uuid"));
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        UUID value = UUID.randomUUID();

        assertThat(new BookLogId(value)).isEqualTo(new BookLogId(value));
    }
}
