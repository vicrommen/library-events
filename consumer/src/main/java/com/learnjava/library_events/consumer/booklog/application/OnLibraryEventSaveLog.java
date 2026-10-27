package com.learnjava.library_events.consumer.booklog.application;

import com.learnjava.library_events.consumer.booklog.domain.BookId;
import com.learnjava.library_events.consumer.booklog.domain.BookLog;
import com.learnjava.library_events.consumer.booklog.domain.BookLogsRepository;
import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.book.LibraryEvent.BookInfo;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OnLibraryEventSaveLog {

    private final BookLogsRepository repository;

    public OnLibraryEventSaveLog(BookLogsRepository repository) {
        this.repository = repository;
    }

    public void execute(LibraryEvent event, Instant occurredAt) {
        BookInfo book = event.book();
        BookId bookId = new BookId(book.id());
        BookLog bookLog = switch (event.libraryEventType()) {
            case NEW -> BookLog.bookCreated(bookId, book.name(), book.author(), occurredAt);
            case UPDATE -> BookLog.bookUpdated(bookId, book.name(), book.author(), occurredAt);
        };
        repository.save(bookLog);
    }
}
