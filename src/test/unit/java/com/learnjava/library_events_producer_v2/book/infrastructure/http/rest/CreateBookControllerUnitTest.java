package com.learnjava.library_events_producer_v2.book.infrastructure.http.rest;

import com.learnjava.library_events_producer_v2.book.application.CreateBook;
import com.learnjava.library_events_producer_v2.book.infrastructure.http.rest.CreateBookController.CreateBookRequest;
import com.learnjava.library_events_producer_v2.utils.mother.CreateBookRequestMother;
import com.learnjava.library_events_producer_v2.utils.mother.MockRequestMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateBookController.class)
class CreateBookControllerUnitTest {

    private static final String BOOKS_URL = "/api/v1/books";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateBook createBook;

    @Test
    void shouldCreateBook() throws Exception {
        CreateBookRequest request = CreateBookRequestMother.random();
        UUID id = UUID.randomUUID();
        when(createBook.execute(request.name(), request.author())).thenReturn(id);

        mockMvc.perform(MockRequestMother.post(BOOKS_URL, request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(createBook).execute(request.name(), request.author());
    }

    @Test
    void shouldRejectBlankName() throws Exception {
        CreateBookRequest request = CreateBookRequestMother.withName(" ");

        mockMvc.perform(MockRequestMother.post(BOOKS_URL, request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("The value ' ' for field 'name' is not valid"));

        verifyNoInteractions(createBook);
    }

    @Test
    void shouldRejectBlankAuthor() throws Exception {
        CreateBookRequest request = CreateBookRequestMother.withAuthor(" ");

        mockMvc.perform(MockRequestMother.post(BOOKS_URL, request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("The value ' ' for field 'author' is not valid"));

        verifyNoInteractions(createBook);
    }
}
