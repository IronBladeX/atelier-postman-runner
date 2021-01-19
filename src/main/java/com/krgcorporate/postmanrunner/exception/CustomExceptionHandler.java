package com.krgcorporate.postmanrunner.exception;

import com.krgcorporate.postmanrunner.settings.Response;
import com.krgcorporate.postmanrunner.settings.ValidationError;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        Response<ValidationError> response = Response.error(HttpStatus.BAD_REQUEST, "validation_failed", "Resource has validation errors.");
        response.setData(ValidationError.fromBindingResult(ex.getBindingResult()));

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NotFoundException.class})
    protected @NonNull ResponseEntity<Object> handleNotFound(
            @NonNull NotFoundException ex,
            @NonNull WebRequest request
    ) {
        Response<Object> response = Response.error(HttpStatus.NOT_FOUND, "not_found", ex.getMessage());

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({AlreadyExistsException.class})
    protected @NonNull ResponseEntity<Object> handleAlreadyExists(
            @NonNull AlreadyExistsException ex,
            @NonNull WebRequest request
    ) {
        Response<Object> response = Response.error(HttpStatus.CONFLICT, "already_exists", ex.getMessage());

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
