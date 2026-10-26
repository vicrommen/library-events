package com.learnjava.library_events.producer.book.domain;

import com.learnjava.library_events.shared.domain.NotFoundException;

public class BookNotFoundException extends NotFoundException {

    public BookNotFoundException(BookId id) {
        super(id.value().toString());
    }
}
