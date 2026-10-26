package com.learnjava.library_events_producer_v2.book.infrastructure.persistence.mock;

import com.learnjava.library_events_producer_v2.book.domain.Book;
import com.learnjava.library_events_producer_v2.book.domain.BookId;
import com.learnjava.library_events_producer_v2.book.domain.BookQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MockBookQueryRepository implements BookQueryRepository {

    private final MockBookStorage storage;

    public MockBookQueryRepository(MockBookStorage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return storage.get(id);
    }
}
