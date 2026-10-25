package com.learnjava.library_events_producer_v2.utils.mother;

import com.learnjava.library_events_producer_v2.book.domain.Book;
import com.learnjava.library_events_producer_v2.book.domain.BookAuthor;
import com.learnjava.library_events_producer_v2.book.domain.BookId;
import com.learnjava.library_events_producer_v2.book.domain.BookName;

import java.util.UUID;

public final class BookMother {

    private BookMother() {
    }

    public static Book create(BookId id, BookName name, BookAuthor author) {
        return new Book(id, name, author);
    }

    public static Book random() {
        return create(BookId.generate(), randomName(), randomAuthor());
    }

    public static Book withId(BookId id) {
        return create(id, randomName(), randomAuthor());
    }

    public static BookName randomName() {
        return new BookName("Book " + randomSuffix());
    }

    public static BookAuthor randomAuthor() {
        return new BookAuthor("Author " + randomSuffix());
    }

    private static String randomSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
