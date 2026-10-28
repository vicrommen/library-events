package com.learnjava.library_events.consumer.booklog.infrastructure.persistence.jpa;

import com.learnjava.library_events.consumer.booklog.domain.BookId;
import com.learnjava.library_events.consumer.booklog.domain.BookLog;
import com.learnjava.library_events.consumer.booklog.domain.BookLogQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class HibernateBookLogQueryRepository implements BookLogQueryRepository {

    private final EntityManager entityManager;

    public HibernateBookLogQueryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLog> findByBookId(BookId bookId) {
        return entityManager.createQuery(
                        "SELECT b FROM BookLogEntity b WHERE b.bookId = :bookId ORDER BY b.occurredAt", BookLogEntity.class)
                .setParameter("bookId", bookId.value())
                .getResultStream()
                .map(BookLogMapper::toDomain)
                .toList();
    }
}
