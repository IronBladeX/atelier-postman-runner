package com.krgcorporate.postmanrunner.repository;

import com.krgcorporate.postmanrunner.domain.Contract;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ContractRepository extends MongoRepository<Contract, String> {

    @Query(value = "{ \"ref\": { $regex: ?#{ '^' + [0] + '$' }, $options: 'i' } }")
    @NonNull
    Optional<Contract> findByRef(@NonNull String ref);
}
