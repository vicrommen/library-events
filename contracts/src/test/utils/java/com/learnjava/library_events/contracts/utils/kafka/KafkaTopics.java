package com.learnjava.library_events.contracts.utils.kafka;

import com.learnjava.library_events.contracts.book.LibraryEventTopics;

public final class KafkaTopics {

    public static final String PREFIX = "library.";
    public static final String LIBRARY_EVENT = PREFIX + LibraryEventTopics.LIBRARY_EVENT;

    private KafkaTopics() {
    }
}
