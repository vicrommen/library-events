package com.learnjava.library_events.producer.book.domain;

import java.util.Optional;

public interface BookQueryRepository {

    Optional<Book> findById(BookId id);
}
