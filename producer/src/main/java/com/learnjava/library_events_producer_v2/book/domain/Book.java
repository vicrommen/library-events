package com.learnjava.library_events_producer_v2.book.domain;

import com.learnjava.library_events_producer_v2.book.domain.LibraryEvent.BookInfo;
import com.learnjava.library_events_producer_v2.book.domain.LibraryEvent.LibraryEventType;
import com.learnjava.library_events.shared.domain.Result;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Objects;

@RecordBuilder
public record Book(BookId id, BookName name, BookAuthor author) implements BookBuilder.With {

    public Book {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(author, "author must not be null");
    }

    public static Result<Book, LibraryEvent> create(BookName name, BookAuthor author) {
        Book book = new Book(BookId.generate(), name, author);
        return Result.of(book, book.toEvent(LibraryEventType.NEW));
    }

    public Result<Book, LibraryEvent> update(BookName newName, BookAuthor newAuthor) {
        Book updated = with(b -> b.name(newName).author(newAuthor));
        return Result.of(updated, updated.toEvent(LibraryEventType.UPDATE));
    }

    private LibraryEvent toEvent(LibraryEventType type) {
        return new LibraryEvent(type, new BookInfo(id.value(), name.value(), author.value()));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Book other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
