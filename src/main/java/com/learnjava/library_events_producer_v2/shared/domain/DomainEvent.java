package com.learnjava.library_events_producer_v2.shared.domain;

import java.util.Optional;
import java.util.UUID;

public interface DomainEvent {

    Optional<UUID> key();
}
