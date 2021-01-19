package com.krgcorporate.postmanrunner.ws;

import com.krgcorporate.postmanrunner.business.ContractManager;
import com.krgcorporate.postmanrunner.domain.Contract;
import com.krgcorporate.postmanrunner.exception.AlreadyExistsException;
import com.krgcorporate.postmanrunner.exception.NotFoundException;
import com.krgcorporate.postmanrunner.validator.ContractsValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/contracts")
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor_ = @Autowired)
public class ContractController {

    private final @NonNull ContractManager contractManager;

    private final @NonNull ContractsValidator contractsValidator;

    @InitBinder("contract")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(contractsValidator);
    }

    @GetMapping()
    public Page<Contract> list(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "200") int size) {

        return contractManager.findAll(PageRequest.of(page, size));
    }

    @GetMapping("{ref}")
    public Contract findOne(@PathVariable("ref") final String ref) {
        return contractManager.findByRef(ref)
                .orElseThrow(() -> new NotFoundException(String.format("No Contract with ref `%s`", ref)));
    }

    @PostMapping()
    public Contract post(@RequestBody @Validated final Contract contract) {
        if (Objects.nonNull(contract.getId())) {
            throw new AlreadyExistsException("Contract `" + contract.getId() + "` already exists in database.");
        }

        contractManager.findByRef(contract.getRef()).ifPresent(result -> {
            throw new AlreadyExistsException(String.format("Contract with ref `%s` already exists.", contract.getRef()));
        });

        contract.getPersons().forEach(person -> person.setCode(UUID.randomUUID().toString()));

        return contractManager.save(contract);
    }

    @PutMapping("{ref}")
    public Contract put(
            @PathVariable("ref") final String ref,
            @RequestBody @Validated final Contract contract) {

        Contract contractFromDb = contractManager.findByRef(ref)
                .orElseThrow(() -> new NotFoundException(String.format("No Contract with ref `%s`", ref)));

        contractFromDb = contractManager.applyModification(contractFromDb, contract);

        return contractManager.save(contractFromDb);
    }

    @DeleteMapping("{ref}")
    public void delete(@PathVariable("ref") final String ref) {

        Contract contractFromDb = contractManager.findByRef(ref)
                .orElseThrow(() -> new NotFoundException(String.format("No Contract with ref `%s`", ref)));

        contractManager.delete(contractFromDb);
    }

    @PostMapping("reset")
    public void reset() {
        contractManager.reset();
    }
}
