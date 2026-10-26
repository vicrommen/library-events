package com.learnjava.library_events.producer.utils.mother;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.json.JsonMapper;

public final class MockRequestMother {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

    private MockRequestMother() {
    }

    public static MockHttpServletRequestBuilder post(String url, Object body) {
        return MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_MAPPER.writeValueAsString(body));
    }
}
