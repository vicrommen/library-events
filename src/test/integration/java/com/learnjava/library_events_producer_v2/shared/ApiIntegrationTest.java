package com.learnjava.library_events_producer_v2.shared;

import com.learnjava.library_events_producer_v2.shared.domain.DomainEvent;
import com.learnjava.library_events_producer_v2.shared.infrastructure.messaging.kafka.TopicNamingStrategy;
import com.learnjava.library_events_producer_v2.utils.kafka.KafkaTestConsumer;
import com.learnjava.library_events_producer_v2.utils.kafka.KafkaTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static com.learnjava.library_events_producer_v2.utils.assertions.EventAssertions.assertPublished;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@EmbeddedKafka(topics = KafkaTopics.LIBRARY_EVENT, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public abstract class ApiIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private TopicNamingStrategy topicNamingStrategy;

    protected <V extends DomainEvent> void assertEventPublished(V expected) {
        assertEventPublished(topicNamingStrategy.topicFor(expected.getClass()), expected);
    }

    @SuppressWarnings("unchecked")
    protected <V extends DomainEvent> void assertEventPublished(String topic, V expected) {
        try (var events = KafkaTestConsumer.createConsumerForTopic(broker, topic, (Class<V>) expected.getClass())) {
            assertPublished(events.awaitRecordWithKey(expected.key().orElseThrow()), expected);
        }
    }
}
