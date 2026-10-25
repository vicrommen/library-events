package com.learnjava.library_events_producer_v2.utils.mother;

import com.learnjava.library_events_producer_v2.book.domain.Book;
import com.learnjava.library_events_producer_v2.book.domain.LibraryEvent;
import com.learnjava.library_events_producer_v2.book.domain.LibraryEvent.BookInfo;
import com.learnjava.library_events_producer_v2.book.domain.LibraryEvent.LibraryEventType;

public final class LibraryEventMother {

    private LibraryEventMother() {
    }

    public static LibraryEvent create(LibraryEventType type, Book book) {
        return new LibraryEvent(type, new BookInfo(book.id().value(), book.name().value(), book.author().value()));
    }

    public static LibraryEvent newBook(Book book) {
        return create(LibraryEventType.NEW, book);
    }

    public static LibraryEvent newBook() {
        return newBook(BookMother.random());
    }

    public static LibraryEvent updateBook(Book book) {
        return create(LibraryEventType.UPDATE, book);
    }

    public static LibraryEvent updateBook() {
        return updateBook(BookMother.random());
    }
}
