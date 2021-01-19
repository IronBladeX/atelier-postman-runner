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

import java.util.List;

@Document
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor_ = {@JsonCreator, @PersistenceConstructor})
public class Contract {

    private @Nullable String id;

    @Indexed
    private @NonNull String ref;

    @Indexed
    private @NonNull String agency;

    @Indexed
    private @NonNull String vendor;

    private @NonNull String status;

    private @NonNull List<Person> persons;
}
