package com.krgcorporate.postmanrunner.validator;

import com.krgcorporate.postmanrunner.domain.Contract;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor_ = @Autowired)
public class ContractsValidator implements Validator {

    private final PersonsValidator personsValidator;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Contract.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Contract contract = (Contract) target;

        // persons
        personsValidator.validate(contract.getPersons(), errors);
    }

}
