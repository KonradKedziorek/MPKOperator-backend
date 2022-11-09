package pl.kedziorek.mpkoperator.config.validator.usernameValidator;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kedziorek.mpkoperator.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername,String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        try{
            boolean exists = userRepository.existsUserByUsername(username);
            return !exists;
        }catch (NullPointerException nullPointerException){
            return true;
        }
    }
}
