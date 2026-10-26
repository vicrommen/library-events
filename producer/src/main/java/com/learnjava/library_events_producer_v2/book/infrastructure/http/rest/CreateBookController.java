package com.learnjava.library_events_producer_v2.book.infrastructure.http.rest;

import com.learnjava.library_events_producer_v2.book.application.CreateBook;
import com.learnjava.library_events_producer_v2.shared.infrastructure.http.rest.ApiV1Controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CreateBookController implements ApiV1Controller {

    private static final Logger log = LoggerFactory.getLogger(CreateBookController.class);

    private final CreateBook createBook;

    public CreateBookController(CreateBook createBook) {
        this.createBook = createBook;
    }

    @PostMapping("/books")
    public ResponseEntity<CreateBookResponse> create(@Valid @RequestBody CreateBookRequest request) {
        log.info("Create book request received: name={}, author={}", request.name(), request.author());
        UUID id = createBook.execute(request.name(), request.author());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateBookResponse(id));
    }

    public record CreateBookRequest(@NotBlank String name, @NotBlank String author) {
    }

    public record CreateBookResponse(UUID id) {
    }
}
