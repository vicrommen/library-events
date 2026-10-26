package com.learnjava.library_events.producer.book.application;

import com.learnjava.library_events.producer.book.domain.Book;
import com.learnjava.library_events.producer.book.domain.BookAuthor;
import com.learnjava.library_events.producer.book.domain.BookName;
import com.learnjava.library_events.producer.book.domain.BookRepository;
import com.learnjava.library_events.producer.book.domain.LibraryEvent;
import com.learnjava.library_events.shared.domain.EventPublisher;
import com.learnjava.library_events.shared.domain.Result;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateBook {

    private final BookRepository repository;
    private final EventPublisher publisher;

    public CreateBook(BookRepository repository, EventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public UUID execute(String name, String author) {
        Result<Book, LibraryEvent> result = Book.create(new BookName(name), new BookAuthor(author));
        repository.save(result.aggregate());
        publisher.publish(result.events());
        return result.aggregate().id().value();
    }
}
