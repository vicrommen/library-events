package com.learnjava.library_events_producer_v2.book.domain;

import com.learnjava.library_events_producer_v2.shared.domain.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BookAuthorUnitTest {

    @Test
    void shouldCreateWithValidValue() {
        assertThat(new BookAuthor("Robert C. Martin").value()).isEqualTo("Robert C. Martin");
    }

    @Test
    void shouldStripValue() {
        assertThat(new BookAuthor("  Robert C. Martin  ").value()).isEqualTo("Robert C. Martin");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void shouldRejectBlankValue(String value) {
        assertThatExceptionOfType(InvalidValueException.class)
                .isThrownBy(() -> new BookAuthor(value))
                .satisfies(ex -> {
                    assertThat(ex.field()).isEqualTo("author");
                    assertThat(ex.value()).isEqualTo(value);
                });
    }

    @Test
    void shouldBeEqualWhenStrippedValuesAreEqual() {
        assertThat(new BookAuthor(" Robert C. Martin ")).isEqualTo(new BookAuthor("Robert C. Martin"));
    }
}
