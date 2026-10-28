package com.learnjava.library_events.consumer.utils.mother;

import com.learnjava.library_events.consumer.booklog.domain.BookId;
import com.learnjava.library_events.consumer.booklog.domain.BookLog;
import com.learnjava.library_events.consumer.booklog.domain.BookLogId;
import com.learnjava.library_events.consumer.booklog.domain.BookLogMessage;
import com.learnjava.library_events.consumer.booklog.domain.BookLogType;
import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.book.LibraryEvent.BookInfo;

import java.time.Instant;
import java.util.UUID;

public final class BookLogMother {

    private BookLogMother() {
    }

    public static BookLog create(BookLogId id, BookId bookId, BookLogType type, BookLogMessage message, Instant occurredAt) {
        return new BookLog(id, bookId, type, message, occurredAt);
    }

    public static BookLog random() {
        return withId(BookLogId.generate());
    }

    public static BookLog withId(BookLogId id) {
        return create(id, randomBookId(), BookLogType.BOOK_CREATED, randomMessage(), Instant.now());
    }

    public static BookLog bookCreated(LibraryEvent event, Instant occurredAt) {
        BookInfo book = event.book();
        return BookLog.bookCreated(new BookId(book.id()), book.name(), book.author(), occurredAt);
    }

    public static BookLog bookUpdated(LibraryEvent event, Instant occurredAt) {
        BookInfo book = event.book();
        return BookLog.bookUpdated(new BookId(book.id()), book.name(), book.author(), occurredAt);
    }

    public static BookId randomBookId() {
        return new BookId(UUID.randomUUID());
    }

    public static BookLogMessage randomMessage() {
        return new BookLogMessage("Message " + randomSuffix());
    }

    private static String randomSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
