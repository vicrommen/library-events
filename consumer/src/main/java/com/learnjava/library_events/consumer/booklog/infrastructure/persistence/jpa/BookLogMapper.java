package com.learnjava.library_events.consumer.booklog.infrastructure.persistence.jpa;

import com.learnjava.library_events.consumer.booklog.domain.BookId;
import com.learnjava.library_events.consumer.booklog.domain.BookLog;
import com.learnjava.library_events.consumer.booklog.domain.BookLogId;
import com.learnjava.library_events.consumer.booklog.domain.BookLogMessage;

final class BookLogMapper {

    private BookLogMapper() {
    }

    static BookLogEntity toEntity(BookLog bookLog) {
        return new BookLogEntity(
                bookLog.id().value(),
                bookLog.bookId().value(),
                bookLog.type(),
                bookLog.message().value(),
                bookLog.occurredAt()
        );
    }

    static BookLog toDomain(BookLogEntity entity) {
        return new BookLog(
                new BookLogId(entity.id()),
                new BookId(entity.bookId()),
                entity.type(),
                new BookLogMessage(entity.message()),
                entity.occurredAt()
        );
    }
}
