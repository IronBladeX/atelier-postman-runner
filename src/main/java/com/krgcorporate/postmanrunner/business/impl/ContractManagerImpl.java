package com.krgcorporate.postmanrunner.business.impl;

import com.krgcorporate.postmanrunner.business.ContractManager;
import com.krgcorporate.postmanrunner.domain.Contract;
import com.krgcorporate.postmanrunner.repository.ContractRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ContractManagerImpl implements ContractManager {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractManagerImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public @NonNull Page<Contract> findAll(@NonNull Pageable pageable) {
        return contractRepository.findAll(pageable);
    }

    @Override
    public @NonNull Optional<Contract> findByRef(@NonNull String ref) {
        return contractRepository.findByRef(ref);
    }

    @Override
    public @NonNull Contract save(@NonNull Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public void delete(@NonNull Contract contract) {
        contractRepository.delete(contract);
    }

    @Override
    public void reset() {
        contractRepository.deleteAll();
    }

    @Override
    public @NonNull Contract applyModification(@NonNull Contract original, @NonNull Contract modified) {

        modified.getPersons()
                .stream()
                .filter(person -> original.getPersons().stream().noneMatch(person1 -> person.getCode().equals(person1.getCode())))
                .forEach(person -> {
                    person.setCode(UUID.randomUUID().toString());
                });

        original.setPersons(modified.getPersons());
        original.setStatus(modified.getStatus());

        return original;
    }
}
