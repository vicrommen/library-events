package com.learnjava.library_events.producer.book.infrastructure.persistence.mock;

import com.learnjava.library_events.producer.book.domain.Book;
import com.learnjava.library_events.producer.book.domain.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MockBookRepository implements BookRepository {

    private static final Logger log = LoggerFactory.getLogger(MockBookRepository.class);

    private final MockBookStorage storage;

    public MockBookRepository(MockBookStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Book book) {
        storage.put(book);
        log.info("Book saved: {}", book);
    }
}
