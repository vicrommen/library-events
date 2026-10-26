package com.learnjava.library_events_producer_v2.book.application;

import com.learnjava.library_events_producer_v2.book.domain.Book;
import com.learnjava.library_events_producer_v2.book.domain.BookAuthor;
import com.learnjava.library_events_producer_v2.book.domain.BookId;
import com.learnjava.library_events_producer_v2.book.domain.BookName;
import com.learnjava.library_events_producer_v2.book.domain.BookNotFoundException;
import com.learnjava.library_events_producer_v2.book.domain.BookQueryRepository;
import com.learnjava.library_events_producer_v2.book.domain.BookRepository;
import com.learnjava.library_events_producer_v2.book.domain.LibraryEvent;
import com.learnjava.library_events.shared.domain.EventPublisher;
import com.learnjava.library_events.shared.domain.Result;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateBook {

    private final BookQueryRepository queryRepository;
    private final BookRepository repository;
    private final EventPublisher publisher;

    public UpdateBook(BookQueryRepository queryRepository, BookRepository repository, EventPublisher publisher) {
        this.queryRepository = queryRepository;
        this.repository = repository;
        this.publisher = publisher;
    }

    public void execute(UUID id, String name, String author) {
        BookId bookId = new BookId(id);
        Book book = queryRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Result<Book, LibraryEvent> result = book.update(new BookName(name), new BookAuthor(author));
        repository.save(result.aggregate());
        publisher.publish(result.events());
    }
}
