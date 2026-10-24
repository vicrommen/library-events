package com.learnjava.library_events_producer_v2.shared.infrastructure.http.rest;

import com.learnjava.library_events_producer_v2.shared.domain.InvalidValueException;
import com.learnjava.library_events_producer_v2.shared.domain.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice(assignableTypes = ApiV1Controller.class)
public class ApiV1ExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiV1ExceptionHandler.class);

    private static final String NOT_FOUND = "The resource with the id '%s' could not be found";
    private static final String INVALID_VALUE = "The value '%s' for field '%s' is not valid";
    private static final String INVALID_REQUEST = "The request contains invalid values";
    private static final String UNEXPECTED = "An unexpected error occurred";

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, NOT_FOUND.formatted(ex.id()));
    }

    @ExceptionHandler(InvalidValueException.class)
    public ProblemDetail handleInvalidValue(InvalidValueException ex) {
        return invalidRequest(List.of(INVALID_VALUE.formatted(ex.value(), ex.field())));
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> INVALID_VALUE.formatted(error.getRejectedValue(), error.getField()))
                .distinct()
                .toList();
        return ResponseEntity.badRequest().body(invalidRequest(errors));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String field = ex instanceof MethodArgumentTypeMismatchException mismatch ? mismatch.getName() : ex.getPropertyName();
        return ResponseEntity.badRequest().body(invalidRequest(List.of(INVALID_VALUE.formatted(ex.getValue(), field))));
    }

    private ProblemDetail invalidRequest(List<String> errors) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, INVALID_REQUEST);
        problem.setProperty("errors", errors);
        return problem;
    }
}
