package com.learnjava.library_events_producer_v2.utils.mother;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.UUID;

public final class KafkaConsumerMother {

    private KafkaConsumerMother() {
    }

    public static <V> Consumer<UUID, V> createConsumer(EmbeddedKafkaBroker broker, String topic, Class<V> eventType) {
        Map<String, Object> props = KafkaTestUtils.consumerProps(broker, "test-" + UUID.randomUUID(), false);
        Consumer<UUID, V> consumer = new DefaultKafkaConsumerFactory<>(props, new UUIDDeserializer(),
                new JacksonJsonDeserializer<>(eventType).ignoreTypeHeaders()).createConsumer();
        broker.consumeFromAnEmbeddedTopic(consumer, true, topic);
        return consumer;
    }
}
