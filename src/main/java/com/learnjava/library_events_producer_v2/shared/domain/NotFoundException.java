package com.learnjava.library_events_producer_v2.shared.domain;

public abstract class NotFoundException extends RuntimeException {

    private final String id;

    protected NotFoundException(String id) {
        super("Resource not found: " + id);
        this.id = id;
    }

    public String id() {
        return id;
    }
}
