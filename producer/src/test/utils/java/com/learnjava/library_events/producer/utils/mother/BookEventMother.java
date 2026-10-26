package com.learnjava.library_events.producer.utils.mother;

import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.utils.mother.LibraryEventMother;
import com.learnjava.library_events.producer.book.domain.Book;

public final class BookEventMother {

    private BookEventMother() {
    }

    public static LibraryEvent newBook(Book book) {
        return LibraryEventMother.newBook(book.id().value(), book.name().value(), book.author().value());
    }

    public static LibraryEvent updateBook(Book book) {
        return LibraryEventMother.updateBook(book.id().value(), book.name().value(), book.author().value());
    }
}
