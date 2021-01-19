package com.krgcorporate.postmanrunner.exception;

import lombok.NonNull;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(@NonNull String message) {
        super(message);
    }

    public NotFoundException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(@NonNull Throwable cause) {
        super(cause);
    }
}
