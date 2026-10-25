package com.learnjava.library_events_producer_v2.book.infrastructure.http.rest;

import com.learnjava.library_events_producer_v2.book.infrastructure.http.rest.CreateBookController.CreateBookRequest;
import com.learnjava.library_events_producer_v2.utils.mother.CreateBookRequestMother;
import com.learnjava.library_events_producer_v2.utils.mother.HttpEntityMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@EmbeddedKafka(topics = "library.library-event", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class CreateBookControllerIntegrationTest {

    private static final String BOOKS_URL = "/api/v1/books";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateBook() {
        HttpEntity<CreateBookRequest> request = HttpEntityMother.createJsonEntity(CreateBookRequestMother.random());

        ResponseEntity<Void> response = restTemplate.exchange(BOOKS_URL, HttpMethod.POST, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
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
