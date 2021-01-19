package com.krgcorporate.postmanrunner.business;

import com.krgcorporate.postmanrunner.domain.Contract;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContractManager {

    @NonNull
    Optional<Contract> findByRef(@NonNull String ref);

    @NonNull
    Page<Contract> findAll(@NonNull Pageable pageable);

    @NonNull
    Contract save(@NonNull Contract contract);

    void delete(@NonNull Contract contract);

    void reset();

    @NonNull
    Contract applyModification(@NonNull Contract original, @NonNull Contract modified);
}
