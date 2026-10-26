package com.learnjava.library_events.producer.utils.mother;

import com.learnjava.library_events.producer.book.infrastructure.http.rest.UpdateBookController.UpdateBookRequest;

public final class UpdateBookRequestMother {

    private UpdateBookRequestMother() {
    }

    public static UpdateBookRequest create(String name, String author) {
        return new UpdateBookRequest(name, author);
    }

    public static UpdateBookRequest random() {
        return create(BookMother.randomName().value(), BookMother.randomAuthor().value());
    }

    public static UpdateBookRequest withName(String name) {
        return create(name, BookMother.randomAuthor().value());
    }

    public static UpdateBookRequest withAuthor(String author) {
        return create(BookMother.randomName().value(), author);
    }
}
