package com.learnjava.library_events_producer_v2.book.domain;

import com.learnjava.library_events_producer_v2.shared.domain.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BookNameUnitTest {

    @Test
    void shouldCreateWithValidValue() {
        assertThat(new BookName("Clean Code").value()).isEqualTo("Clean Code");
    }

    @Test
    void shouldStripValue() {
        assertThat(new BookName("  Clean Code  ").value()).isEqualTo("Clean Code");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void shouldRejectBlankValue(String value) {
        assertThatExceptionOfType(InvalidValueException.class)
                .isThrownBy(() -> new BookName(value))
                .satisfies(ex -> {
                    assertThat(ex.field()).isEqualTo("name");
                    assertThat(ex.value()).isEqualTo(value);
                });
    }

    @Test
    void shouldBeEqualWhenStrippedValuesAreEqual() {
        assertThat(new BookName(" Clean Code ")).isEqualTo(new BookName("Clean Code"));
    }
}
