package com.learnjava.library_events.consumer.book.infrastructure.messaging.kafka;

import com.learnjava.library_events.consumer.utils.containers.PostgresContainerConfiguration;
import com.learnjava.library_events.contracts.book.LibraryEvent;
import com.learnjava.library_events.contracts.utils.kafka.KafkaTopics;
import com.learnjava.library_events.contracts.utils.mother.LibraryEventMother;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.UUID;
import java.util.stream.Stream;

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

    static Stream<Arguments> events() {
        return Stream.of(
                Arguments.of(LibraryEventMother.newBook()),
                Arguments.of(LibraryEventMother.updateBook()));
    }

    @ParameterizedTest
    @MethodSource("events")
    void consumesLibraryEvent(LibraryEvent event) {
        UUID key = event.book().id();

        kafkaTemplate.send(KafkaTopics.LIBRARY_EVENT, key, event);

        verify(listener, timeout(10_000)).on(event, key);
    }
}
