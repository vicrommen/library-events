package com.learnjava.library_events_producer_v2.book.infrastructure.http.rest;

import com.learnjava.library_events_producer_v2.book.domain.Book;
import com.learnjava.library_events_producer_v2.book.domain.BookId;
import com.learnjava.library_events_producer_v2.book.domain.BookQueryRepository;
import com.learnjava.library_events_producer_v2.book.domain.LibraryEvent;
import com.learnjava.library_events_producer_v2.book.infrastructure.http.rest.CreateBookController.CreateBookRequest;
import com.learnjava.library_events_producer_v2.book.infrastructure.http.rest.CreateBookController.CreateBookResponse;
import com.learnjava.library_events_producer_v2.utils.kafka.KafkaTopics;
import com.learnjava.library_events_producer_v2.utils.mother.CreateBookRequestMother;
import com.learnjava.library_events_producer_v2.utils.mother.HttpEntityMother;
import com.learnjava.library_events_producer_v2.utils.mother.KafkaConsumerMother;
import com.learnjava.library_events_producer_v2.utils.mother.LibraryEventMother;
import org.apache.kafka.clients.consumer.Consumer;
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
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;
import java.util.UUID;

import static com.learnjava.library_events_producer_v2.utils.assertions.BookAssertions.assertCreatedBy;
import static com.learnjava.library_events_producer_v2.utils.assertions.KafkaAssertions.assertEventPublished;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@EmbeddedKafka(topics = KafkaTopics.LIBRARY_EVENT, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class CreateBookControllerIntegrationTest {

    private static final String BOOKS_URL = "/api/v1/books";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private BookQueryRepository bookQueryRepository;

    @Test
    void shouldCreateBook() {
        try (Consumer<UUID, LibraryEvent> consumer = KafkaConsumerMother.createConsumer(broker, KafkaTopics.LIBRARY_EVENT, LibraryEvent.class)) {
            CreateBookRequest body = CreateBookRequestMother.random();
            HttpEntity<CreateBookRequest> request = HttpEntityMother.createJsonEntity(body);

            ResponseEntity<CreateBookResponse> response = restTemplate.exchange(BOOKS_URL, HttpMethod.POST, request, CreateBookResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            Book created = bookQueryRepository.findById(new BookId(response.getBody().id())).orElseThrow();
            assertCreatedBy(created, body);

            assertEventPublished(consumer, KafkaTopics.LIBRARY_EVENT, LibraryEventMother.newBook(created));
        }
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
