package com.learnjava.library_events.shared.domain;

public class InvalidValueException extends RuntimeException {

    private final String field;
    private final Object value;

    public InvalidValueException(String field, Object value) {
        super("Invalid value '" + value + "' for field '" + field + "'");
        this.field = field;
        this.value = value;
    }

    public String field() {
        return field;
    }

    public Object value() {
        return value;
    }
}
