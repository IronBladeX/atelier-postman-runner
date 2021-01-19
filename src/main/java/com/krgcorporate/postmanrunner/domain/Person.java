package com.krgcorporate.postmanrunner.domain;

import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
public class Person {

    private @NonNull String code;

    private @NonNull Gender gender;

    private @Nullable String firstname;

    private @NonNull String lastname;

    private @Nullable String email;

    private @NonNull String role;
}
