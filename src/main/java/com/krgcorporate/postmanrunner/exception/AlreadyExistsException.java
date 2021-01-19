package com.krgcorporate.postmanrunner.exception;

import lombok.NonNull;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException() {
        super();
    }

    public AlreadyExistsException(@NonNull String message) {
        super(message);
    }

    public AlreadyExistsException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistsException(@NonNull Throwable cause) {
        super(cause);
    }
}
