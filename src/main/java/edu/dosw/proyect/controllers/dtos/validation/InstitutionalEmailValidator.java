package edu.dosw.proyect.controllers.dtos.validation;

import edu.dosw.proyect.core.utils.InstitutionalMailStrategy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class
InstitutionalEmailValidator implements ConstraintValidator<InstitutionalEmail, String> {

    private final InstitutionalMailStrategy strategy = new InstitutionalMailStrategy();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return strategy.validate(value);
    }
}
