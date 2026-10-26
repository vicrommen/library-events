package com.learnjava.library_events.producer.book.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BookIdUnitTest {

    @Test
    void shouldCreateFromUuid() {
        UUID value = UUID.randomUUID();

        assertThat(new BookId(value).value()).isEqualTo(value);
    }

    @Test
    void shouldRejectNullValue() {
        assertThatNullPointerException()
                .isThrownBy(() -> new BookId(null))
                .withMessage("BookId value must not be null");
    }

    @Test
    void shouldGenerateDifferentIds() {
        assertThat(BookId.generate()).isNotEqualTo(BookId.generate());
    }

    @Test
    void shouldCreateFromString() {
        UUID value = UUID.randomUUID();

        assertThat(BookId.of(value.toString())).isEqualTo(new BookId(value));
    }

    @Test
    void shouldRejectInvalidString() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BookId.of("not-a-uuid"));
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        UUID value = UUID.randomUUID();

        assertThat(new BookId(value)).isEqualTo(new BookId(value));
    }
}
