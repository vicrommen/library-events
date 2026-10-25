package com.learnjava.library_events_producer_v2.utils.mother;

import com.learnjava.library_events_producer_v2.book.infrastructure.http.rest.CreateBookController.CreateBookRequest;

public final class CreateBookRequestMother {

    private CreateBookRequestMother() {
    }

    public static CreateBookRequest create(String name, String author) {
        return new CreateBookRequest(name, author);
    }

    public static CreateBookRequest random() {
        return create(BookMother.randomName().value(), BookMother.randomAuthor().value());
    }

    public static CreateBookRequest withName(String name) {
        return create(name, BookMother.randomAuthor().value());
    }

    public static CreateBookRequest withAuthor(String author) {
        return create(BookMother.randomName().value(), author);
    }
}
