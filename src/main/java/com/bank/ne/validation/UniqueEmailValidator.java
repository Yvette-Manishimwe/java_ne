package com.bank.ne.validation;

import com.bank.ne.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final CustomerRepository customerRepository;

    @Autowired
    public UniqueEmailValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Default constructor required by Spring for bean instantiation


    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !customerRepository.existsByEmail(email);
    }
}


