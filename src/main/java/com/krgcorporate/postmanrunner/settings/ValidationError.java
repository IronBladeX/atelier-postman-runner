package com.krgcorporate.postmanrunner.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class ValidationError {

    private @NonNull List<ApiError> globalErrors;

    private @NonNull Map<String, List<ApiError>> fieldErrors;

    @Builder
    @JsonCreator
    @PersistenceConstructor
    ValidationError(
            @Nullable List<ApiError> globalErrors,
            @Nullable Map<String, List<ApiError>> fieldErrors
    ) {
        this.globalErrors = Optional.ofNullable(globalErrors)
                .orElseGet(ArrayList::new);
        this.fieldErrors = Optional.ofNullable(fieldErrors)
                .orElseGet(HashMap::new);
    }

    public static ValidationError fromBindingResult(BindingResult bindingResult) {
        return new ValidationError(
                bindingResult.getGlobalErrors().stream()
                        .map(e -> ApiError.builder()
                                .code(e.getCode())
                                .message(e.getDefaultMessage())
                                .build())
                        .collect(Collectors.toList()),
                bindingResult.getFieldErrors().stream()
                        .collect(Collectors.groupingBy(FieldError::getField,
                                Collectors.mapping(e -> ApiError.builder()
                                        .code(e.getCode())
                                        .message(e.getDefaultMessage())
                                        .build(), Collectors.toList())))
        );
    }

    @Data
    @Builder
    public static class ApiError {

        private String code;

        private String message;
    }
}
