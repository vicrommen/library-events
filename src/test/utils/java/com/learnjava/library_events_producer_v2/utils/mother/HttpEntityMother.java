package com.learnjava.library_events_producer_v2.utils.mother;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public final class HttpEntityMother {

    private HttpEntityMother() {
    }

    public static <T> HttpEntity<T> createJsonEntity(T content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(content, headers);
    }
}
