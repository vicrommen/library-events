package com.learnjava.library_events_producer_v2.book.infrastructure.persistence.mock;

import com.learnjava.library_events_producer_v2.book.domain.Book;
import com.learnjava.library_events_producer_v2.book.domain.BookId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MockBookStorage {

    private final Map<BookId, Book> books = new ConcurrentHashMap<>();

    public void put(Book book) {
        books.put(book.id(), book);
    }

    public Optional<Book> get(BookId id) {
        return Optional.ofNullable(books.get(id));
    }
}
