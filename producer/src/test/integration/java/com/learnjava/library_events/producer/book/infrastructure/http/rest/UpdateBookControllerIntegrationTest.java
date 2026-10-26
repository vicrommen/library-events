package com.learnjava.library_events.producer.book.infrastructure.http.rest;

import com.learnjava.library_events.producer.book.domain.Book;
import com.learnjava.library_events.producer.book.domain.BookQueryRepository;
import com.learnjava.library_events.producer.book.domain.BookRepository;
import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.producer.book.infrastructure.http.rest.UpdateBookController.UpdateBookRequest;
import com.learnjava.library_events.contracts.utils.kafka.KafkaTopics;
import com.learnjava.library_events.producer.utils.mother.BookMother;
import com.learnjava.library_events.producer.utils.mother.HttpEntityMother;
import com.learnjava.library_events.producer.utils.mother.KafkaConsumerMother;
import com.learnjava.library_events.producer.utils.mother.BookEventMother;
import com.learnjava.library_events.producer.utils.mother.UpdateBookRequestMother;
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

import static com.learnjava.library_events.producer.utils.assertions.BookAssertions.assertUpdatedBy;
import static com.learnjava.library_events.producer.utils.assertions.KafkaAssertions.assertEventPublished;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@EmbeddedKafka(topics = KafkaTopics.LIBRARY_EVENT, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class UpdateBookControllerIntegrationTest {

    private static final String BOOK_URL = "/api/v1/books/{id}";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookQueryRepository bookQueryRepository;

    @Test
    void shouldUpdateBook() {
        try (Consumer<UUID, LibraryEvent> consumer = KafkaConsumerMother.createConsumer(broker, KafkaTopics.LIBRARY_EVENT, LibraryEvent.class)) {
            Book book = BookMother.random();
            bookRepository.save(book);
            UpdateBookRequest body = UpdateBookRequestMother.random();
            HttpEntity<UpdateBookRequest> request = HttpEntityMother.createJsonEntity(body);

            ResponseEntity<Void> response = restTemplate.exchange(BOOK_URL, HttpMethod.PUT, request, Void.class, book.id().value());

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Book updated = bookQueryRepository.findById(book.id()).orElseThrow();
            assertUpdatedBy(updated, body);

            assertEventPublished(consumer, KafkaTopics.LIBRARY_EVENT, BookEventMother.updateBook(updated));
        }
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist() {
        UUID id = UUID.randomUUID();
        HttpEntity<UpdateBookRequest> request = HttpEntityMother.createJsonEntity(UpdateBookRequestMother.random());

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(BOOK_URL, HttpMethod.PUT, request, ProblemDetail.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getDetail())
                .isEqualTo("The resource with the id '%s' could not be found".formatted(id));
    }

    @Test
    void shouldRejectInvalidId() {
        HttpEntity<UpdateBookRequest> request = HttpEntityMother.createJsonEntity(UpdateBookRequestMother.random());

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(BOOK_URL, HttpMethod.PUT, request, ProblemDetail.class, "not-a-uuid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getProperties().get("errors"))
                .isEqualTo(List.of("The value 'not-a-uuid' for field 'id' is not valid"));
    }

    @Test
    void shouldRejectBlankName() {
        HttpEntity<UpdateBookRequest> request = HttpEntityMother.createJsonEntity(UpdateBookRequestMother.withName(" "));

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(BOOK_URL, HttpMethod.PUT, request, ProblemDetail.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getProperties().get("errors"))
                .isEqualTo(List.of("The value ' ' for field 'name' is not valid"));
    }

    @Test
    void shouldRejectBlankAuthor() {
        HttpEntity<UpdateBookRequest> request = HttpEntityMother.createJsonEntity(UpdateBookRequestMother.withAuthor(" "));

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(BOOK_URL, HttpMethod.PUT, request, ProblemDetail.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getProperties().get("errors"))
                .isEqualTo(List.of("The value ' ' for field 'author' is not valid"));
    }
}
