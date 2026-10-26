package com.learnjava.library_events.contracts.utils.mother;

import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.book.LibraryEvent.BookInfo;
import com.learnjava.library_events.contracts.book.LibraryEvent.LibraryEventType;

import java.util.UUID;

public final class LibraryEventMother {

    private LibraryEventMother() {
    }

    public static LibraryEvent create(LibraryEventType type, UUID id, String name, String author) {
        return new LibraryEvent(type, new BookInfo(id, name, author));
    }

    public static LibraryEvent newBook(UUID id, String name, String author) {
        return create(LibraryEventType.NEW, id, name, author);
    }

    public static LibraryEvent newBook() {
        return newBook(UUID.randomUUID(), randomName(), randomAuthor());
    }

    public static LibraryEvent updateBook(UUID id, String name, String author) {
        return create(LibraryEventType.UPDATE, id, name, author);
    }

    public static LibraryEvent updateBook() {
        return updateBook(UUID.randomUUID(), randomName(), randomAuthor());
    }

    private static String randomName() {
        return "Book " + randomSuffix();
    }

    private static String randomAuthor() {
        return "Author " + randomSuffix();
    }

    private static String randomSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
