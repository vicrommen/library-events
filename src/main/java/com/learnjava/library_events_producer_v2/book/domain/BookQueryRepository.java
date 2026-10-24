package com.learnjava.library_events_producer_v2.book.domain;

import java.util.Optional;

public interface BookQueryRepository {

    Optional<Book> findById(BookId id);
}
