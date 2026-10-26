package com.learnjava.library_events.consumer;

import com.learnjava.library_events.consumer.utils.containers.PostgresContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@Import(PostgresContainerConfiguration.class)
@EmbeddedKafka(bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class LibraryEventsConsumerApplicationTests {

	@Test
	void contextLoads() {
	}

}
