package com.krgcorporate.postmanrunner.validator;

import com.krgcorporate.postmanrunner.domain.Gender;
import com.krgcorporate.postmanrunner.domain.Person;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.List;

@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor_ = @Autowired)
public class PersonsValidator {

    public void validate(@NonNull List<Person> persons, @NonNull Errors errors) {
        int i = 0;
        for (Person person : persons) {
            errors.pushNestedPath("persons[" + i + "]");

            validatePerson(person, errors);

            errors.popNestedPath();
            i++;
        }

        errors.pushNestedPath("persons");
        errors.popNestedPath();
    }

    private void validatePerson(@NonNull Person person, @NonNull Errors errors) {
        if (Gender.SOCIETY.equals(person.getGender()) && StringUtils.hasLength(person.getFirstname())) {
            errors.rejectValue("firstname", "must-be-blank", "Firstname must be blank on society.");
        }
    }
}
