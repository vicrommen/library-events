package com.learnjava.library_events.consumer.utils.mother;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.UUID;

public final class KafkaProducerMother {

    private KafkaProducerMother() {
    }

    public static <V> Producer<UUID, V> createProducer(EmbeddedKafkaBroker broker) {
        Map<String, Object> props = KafkaTestUtils.producerProps(broker);
        return new DefaultKafkaProducerFactory<UUID, V>(props, new UUIDSerializer(),
                new JacksonJsonSerializer<V>().noTypeInfo()).createProducer();
    }
}
