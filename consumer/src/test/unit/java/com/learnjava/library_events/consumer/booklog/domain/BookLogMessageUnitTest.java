package com.learnjava.library_events.consumer.booklog.domain;

import com.learnjava.library_events.shared.domain.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BookLogMessageUnitTest {

    @Test
    void shouldCreateWithValidValue() {
        assertThat(new BookLogMessage("Book created").value()).isEqualTo("Book created");
    }

    @Test
    void shouldStripValue() {
        assertThat(new BookLogMessage("  Book created  ").value()).isEqualTo("Book created");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void shouldRejectBlankValue(String value) {
        assertThatExceptionOfType(InvalidValueException.class)
                .isThrownBy(() -> new BookLogMessage(value))
                .satisfies(ex -> {
                    assertThat(ex.field()).isEqualTo("message");
                    assertThat(ex.value()).isEqualTo(value);
                });
    }

    @Test
    void shouldDescribeBookCreated() {
        assertThat(BookLogMessage.describing(BookLogType.BOOK_CREATED, "Clean Code", "Robert C. Martin").value())
                .isEqualTo("Book 'Clean Code' by Robert C. Martin was created");
    }

    @Test
    void shouldDescribeBookUpdated() {
        assertThat(BookLogMessage.describing(BookLogType.BOOK_UPDATED, "Clean Code", "Robert C. Martin").value())
                .isEqualTo("Book 'Clean Code' by Robert C. Martin was updated");
    }

    @Test
    void shouldBeEqualWhenStrippedValuesAreEqual() {
        assertThat(new BookLogMessage(" Book created ")).isEqualTo(new BookLogMessage("Book created"));
    }
}
