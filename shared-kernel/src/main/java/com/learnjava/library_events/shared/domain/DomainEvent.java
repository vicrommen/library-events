package com.learnjava.library_events.shared.domain;

import java.util.Optional;
import java.util.UUID;

public interface DomainEvent {

    Optional<UUID> key();
}
