package com.learnjava.library_events.consumer.booklog.domain;

import java.util.List;

public interface BookLogQueryRepository {

    List<BookLog> findByBookId(BookId bookId);
}
