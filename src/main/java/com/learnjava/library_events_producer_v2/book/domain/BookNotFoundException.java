package com.learnjava.library_events_producer_v2.book.domain;

import com.learnjava.library_events_producer_v2.shared.domain.NotFoundException;

public class BookNotFoundException extends NotFoundException {

    public BookNotFoundException(BookId id) {
        super(id.value().toString());
    }
}
