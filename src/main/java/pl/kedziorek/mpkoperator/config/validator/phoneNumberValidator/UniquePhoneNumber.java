package pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Retention(RUNTIME)
public @interface UniquePhoneNumber {
    String message();
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
