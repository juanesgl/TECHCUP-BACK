package edu.dosw.proyect.controllers.dtos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = InstitutionalEmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InstitutionalEmail {

    String message() default "Debe ser un correo institucional válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
