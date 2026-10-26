package com.learnjava.library_events.producer.book.infrastructure.http.rest;

import com.learnjava.library_events.producer.book.application.UpdateBook;
import com.learnjava.library_events.producer.shared.infrastructure.http.rest.ApiV1Controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UpdateBookController implements ApiV1Controller {

    private static final Logger log = LoggerFactory.getLogger(UpdateBookController.class);

    private final UpdateBook updateBook;

    public UpdateBookController(UpdateBook updateBook) {
        this.updateBook = updateBook;
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequest request) {
        log.info("Update book request received: id={}, name={}, author={}", id, request.name(), request.author());
        updateBook.execute(id, request.name(), request.author());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public record UpdateBookRequest(@NotBlank String name, @NotBlank String author) {
    }
}
