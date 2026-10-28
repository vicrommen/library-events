package com.learnjava.library_events.consumer.utils.annotations;

import com.learnjava.library_events.consumer.utils.containers.PostgresContainerConfiguration;
import com.learnjava.library_events.contracts.utils.kafka.KafkaTopics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@Import(PostgresContainerConfiguration.class)
@EmbeddedKafka(topics = KafkaTopics.LIBRARY_EVENT, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public @interface IntegrationTest {
}
