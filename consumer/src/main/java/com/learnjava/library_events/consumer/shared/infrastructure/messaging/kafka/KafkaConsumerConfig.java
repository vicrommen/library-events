package com.learnjava.library_events.consumer.shared.infrastructure.messaging.kafka;

import com.learnjava.library_events.shared.domain.InvalidValueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.kafka.autoconfigure.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.time.Duration;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> consumerFactory,
            DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    DefaultErrorHandler errorHandler(@Value("${app.kafka.consumer.retry.interval}") Duration interval,
                                     @Value("${app.kafka.consumer.retry.max-attempts}") long maxAttempts) {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(interval.toMillis(), maxAttempts));
        errorHandler.addNotRetryableExceptions(InvalidValueException.class);
        return errorHandler;
    }
}
