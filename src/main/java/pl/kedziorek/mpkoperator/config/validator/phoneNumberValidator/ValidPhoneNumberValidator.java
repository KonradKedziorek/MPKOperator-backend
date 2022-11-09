package pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public void initialize(ValidPhoneNumber unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber != null && phoneNumber.matches("[0-9]+")
                && (phoneNumber.length() > 8) && (phoneNumber.length() < 14);
    }
}
