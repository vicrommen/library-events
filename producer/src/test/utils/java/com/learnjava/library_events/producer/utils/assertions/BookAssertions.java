package com.learnjava.library_events.producer.utils.assertions;

import com.learnjava.library_events.producer.book.domain.Book;
import com.learnjava.library_events.producer.book.domain.BookAuthor;
import com.learnjava.library_events.producer.book.domain.BookName;
import com.learnjava.library_events.producer.book.domain.LibraryEvent;
import com.learnjava.library_events.producer.book.infrastructure.http.rest.CreateBookController.CreateBookRequest;
import com.learnjava.library_events.producer.book.infrastructure.http.rest.UpdateBookController.UpdateBookRequest;
import com.learnjava.library_events.shared.domain.Result;
import com.learnjava.library_events.producer.utils.mother.LibraryEventMother;

import static org.assertj.core.api.Assertions.assertThat;

public final class BookAssertions {

    private BookAssertions() {
    }

    public static void assertCreation(BookName name, BookAuthor author, Result<Book, LibraryEvent> result) {
        Book book = result.aggregate();
        assertThat(book.id()).isNotNull();
        assertThat(book.name()).isEqualTo(name);
        assertThat(book.author()).isEqualTo(author);
        assertThat(result.events()).containsExactly(LibraryEventMother.newBook(book));
    }

    public static void assertUpdate(Book original, BookName name, BookAuthor author, Result<Book, LibraryEvent> result) {
        Book book = result.aggregate();
        assertThat(book.id()).isEqualTo(original.id());
        assertThat(book.name()).isEqualTo(name);
        assertThat(book.author()).isEqualTo(author);
        assertThat(result.events()).containsExactly(LibraryEventMother.updateBook(book));
    }

    public static void assertCreatedBy(Book book, CreateBookRequest request) {
        assertThat(book.name()).isEqualTo(new BookName(request.name()));
        assertThat(book.author()).isEqualTo(new BookAuthor(request.author()));
    }

    public static void assertUpdatedBy(Book book, UpdateBookRequest request) {
        assertThat(book.name()).isEqualTo(new BookName(request.name()));
        assertThat(book.author()).isEqualTo(new BookAuthor(request.author()));
    }
}
