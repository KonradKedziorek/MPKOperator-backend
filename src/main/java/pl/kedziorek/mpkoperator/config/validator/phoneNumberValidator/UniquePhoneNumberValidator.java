package pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber,String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniquePhoneNumber unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        try{
            boolean exists = userRepository.existsUserByPhoneNumber(phoneNumber);
            return !exists;
        } catch (NullPointerException nullPointerException){
            return true;
        }
    }
}
