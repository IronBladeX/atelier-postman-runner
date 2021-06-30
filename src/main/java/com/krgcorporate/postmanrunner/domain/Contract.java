package com.krgcorporate.postmanrunner.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document
@Data
@Builder
public class Contract {

    private @Nullable String id;

    @Indexed(unique = true)
    private @NonNull String ref;

    @Indexed
    private @NonNull String agency;

    @Indexed
    private @NonNull String vendor;

    private @NonNull String status;

    private @NonNull List<Person> persons;

    @JsonCreator
    @PersistenceConstructor
    public Contract(
            @Nullable String id,
            @NonNull String ref,
            @NonNull String agency,
            @NonNull String vendor,
            @NonNull String status,
            @Nullable List<Person> persons) {
        this.id = id;
        this.ref = ref;
        this.agency = agency;
        this.vendor = vendor;
        this.status = status;
        this.persons = Optional.ofNullable(persons)
                .orElse(new ArrayList<>());
    }
}
