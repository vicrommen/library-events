package com.learnjava.library_events.consumer.book.infrastructure.messaging.kafka;

import com.learnjava.library_events.consumer.booklog.application.OnLibraryEventSaveLog;
import com.learnjava.library_events.consumer.utils.containers.PostgresContainerConfiguration;
import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.utils.kafka.KafkaTopics;
import com.learnjava.library_events.contracts.utils.mother.LibraryEventMother;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.UUIDSerializer",
        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JacksonJsonSerializer"
})
@Import(PostgresContainerConfiguration.class)
@EmbeddedKafka(topics = KafkaTopics.LIBRARY_EVENT, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class LibraryEventListenerIntegrationTest {

    @Autowired
    private KafkaTemplate<UUID, Object> kafkaTemplate;

    @MockitoSpyBean
    private LibraryEventListener listener;

    @MockitoSpyBean
    private OnLibraryEventSaveLog onLibraryEventSaveLog;

    static Stream<Arguments> events() {
        return Stream.of(
                Arguments.of(LibraryEventMother.newBook()),
                Arguments.of(LibraryEventMother.updateBook()));
    }

    @ParameterizedTest
    @MethodSource("events")
    void consumesLibraryEvent(LibraryEvent event) {
        UUID key = event.book().id();

        ProducerRecord<UUID, Object> record = new ProducerRecord<>(KafkaTopics.LIBRARY_EVENT, key, event);
        Instant occurredAt = Instant.now();
        record.headers().add("occurred-at", occurredAt.toString().getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);

        verify(listener, timeout(10_000)).on(eq(event), eq(key), any(byte[].class));
        verify(onLibraryEventSaveLog, timeout(10_000)).execute(event, occurredAt);
    }
}
