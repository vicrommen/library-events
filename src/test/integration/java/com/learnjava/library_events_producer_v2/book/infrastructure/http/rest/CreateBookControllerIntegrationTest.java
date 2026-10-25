package com.learnjava.library_events_producer_v2.book.infrastructure.http.rest;

import com.learnjava.library_events_producer_v2.book.domain.Book;
import com.learnjava.library_events_producer_v2.book.domain.BookId;
import com.learnjava.library_events_producer_v2.book.domain.BookQueryRepository;
import com.learnjava.library_events_producer_v2.book.infrastructure.http.rest.CreateBookController.CreateBookRequest;
import com.learnjava.library_events_producer_v2.book.infrastructure.http.rest.CreateBookController.CreateBookResponse;
import com.learnjava.library_events_producer_v2.shared.ApiIntegrationTest;
import com.learnjava.library_events_producer_v2.utils.mother.CreateBookRequestMother;
import com.learnjava.library_events_producer_v2.utils.mother.HttpEntityMother;
import com.learnjava.library_events_producer_v2.utils.mother.LibraryEventMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.learnjava.library_events_producer_v2.utils.assertions.BookAssertions.assertCreatedBy;
import static org.assertj.core.api.Assertions.assertThat;

class CreateBookControllerIntegrationTest extends ApiIntegrationTest {

    private static final String BOOKS_URL = "/api/v1/books";

    @Autowired
    private BookQueryRepository bookQueryRepository;

    @Test
    void shouldCreateBook() {
        CreateBookRequest body = CreateBookRequestMother.random();
        HttpEntity<CreateBookRequest> request = HttpEntityMother.createJsonEntity(body);

        ResponseEntity<CreateBookResponse> response = restTemplate.exchange(BOOKS_URL, HttpMethod.POST, request, CreateBookResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Book created = bookQueryRepository.findById(new BookId(response.getBody().id())).orElseThrow();
        assertCreatedBy(created, body);

        assertEventPublished(LibraryEventMother.newBook(created));
    }

    @Test
    void shouldRejectBlankName() {
        HttpEntity<CreateBookRequest> request = HttpEntityMother.createJsonEntity(CreateBookRequestMother.withName(" "));

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(BOOKS_URL, HttpMethod.POST, request, ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getProperties().get("errors"))
                .isEqualTo(List.of("The value ' ' for field 'name' is not valid"));
    }

    @Test
    void shouldRejectBlankAuthor() {
        HttpEntity<CreateBookRequest> request = HttpEntityMother.createJsonEntity(CreateBookRequestMother.withAuthor(" "));

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(BOOKS_URL, HttpMethod.POST, request, ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getProperties().get("errors"))
                .isEqualTo(List.of("The value ' ' for field 'author' is not valid"));
    }
}
