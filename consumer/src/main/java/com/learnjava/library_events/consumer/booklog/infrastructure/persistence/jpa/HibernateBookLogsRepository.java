package com.learnjava.library_events.consumer.booklog.infrastructure.persistence.jpa;

import com.learnjava.library_events.consumer.booklog.domain.BookLog;
import com.learnjava.library_events.consumer.booklog.domain.BookLogsRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateBookLogsRepository implements BookLogsRepository {

    private final EntityManager entityManager;

    public HibernateBookLogsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(BookLog bookLog) {
        entityManager.persist(BookLogMapper.toEntity(bookLog));
    }
}
