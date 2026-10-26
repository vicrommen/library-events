package com.learnjava.library_events_producer_v2.book.domain;

import com.learnjava.library_events.shared.domain.Result;
import com.learnjava.library_events_producer_v2.utils.assertions.BookAssertions;
import com.learnjava.library_events_producer_v2.utils.mother.BookMother;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BookUnitTest {

    @Test
    void shouldCreateBookWithNewEvent() {
        BookName name = BookMother.randomName();
        BookAuthor author = BookMother.randomAuthor();

        Result<Book, LibraryEvent> result = Book.create(name, author);

        BookAssertions.assertCreation(name, author, result);
    }

    @Test
    void shouldUpdateBookWithUpdateEvent() {
        Book book = BookMother.random();
        BookName newName = BookMother.randomName();
        BookAuthor newAuthor = BookMother.randomAuthor();

        Result<Book, LibraryEvent> result = book.update(newName, newAuthor);

        BookAssertions.assertUpdate(book, newName, newAuthor, result);
    }

    @Test
    void shouldNotModifyOriginalBookOnUpdate() {
        BookName originalName = BookMother.randomName();
        BookAuthor originalAuthor = BookMother.randomAuthor();
        Book book = BookMother.create(BookId.generate(), originalName, originalAuthor);

        book.update(BookMother.randomName(), BookMother.randomAuthor());

        assertThat(book.name()).isEqualTo(originalName);
        assertThat(book.author()).isEqualTo(originalAuthor);
    }

    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        BookId id = BookId.generate();

        assertThat(BookMother.withId(id)).isEqualTo(BookMother.withId(id));
    }

    @Test
    void shouldNotBeEqualWhenIdsAreDifferent() {
        assertThat(BookMother.random()).isNotEqualTo(BookMother.random());
    }

    @Test
    void shouldRejectNullId() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookMother.create(null, BookMother.randomName(), BookMother.randomAuthor()))
                .withMessage("id must not be null");
    }

    @Test
    void shouldRejectNullName() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookMother.create(BookId.generate(), null, BookMother.randomAuthor()))
                .withMessage("name must not be null");
    }

    @Test
    void shouldRejectNullAuthor() {
        assertThatNullPointerException()
                .isThrownBy(() -> BookMother.create(BookId.generate(), BookMother.randomName(), null))
                .withMessage("author must not be null");
    }
}
