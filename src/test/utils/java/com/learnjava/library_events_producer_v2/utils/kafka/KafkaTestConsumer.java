package com.learnjava.library_events_producer_v2.utils.kafka;

import com.learnjava.library_events_producer_v2.shared.domain.DomainEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public final class KafkaTestConsumer<K, V> implements AutoCloseable {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final Duration POLL_INTERVAL = Duration.ofMillis(100);

    private final Consumer<K, V> consumer;
    private final String topic;

    private KafkaTestConsumer(EmbeddedKafkaBroker broker, String topic,
                              Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        Map<String, Object> props = KafkaTestUtils.consumerProps(broker, "test-" + UUID.randomUUID(), false);
        this.consumer = new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer).createConsumer();
        this.topic = topic;
        broker.consumeFromAnEmbeddedTopic(consumer, topic);
    }

    public static <V extends DomainEvent> KafkaTestConsumer<UUID, V> createConsumerForTopic(
            EmbeddedKafkaBroker broker, String topic, Class<V> eventType) {
        return new KafkaTestConsumer<>(broker, topic, new UUIDDeserializer(),
                new JacksonJsonDeserializer<>(eventType).ignoreTypeHeaders());
    }

    public ConsumerRecord<K, V> awaitRecordWithKey(K key) {
        long deadline = System.nanoTime() + TIMEOUT.toNanos();
        while (System.nanoTime() < deadline) {
            for (ConsumerRecord<K, V> record : consumer.poll(POLL_INTERVAL)) {
                if (key.equals(record.key())) {
                    return record;
                }
            }
        }
        throw new AssertionError("No record with key '%s' in topic '%s' after %s".formatted(key, topic, TIMEOUT));
    }

    @Override
    public void close() {
        consumer.close();
    }
}
