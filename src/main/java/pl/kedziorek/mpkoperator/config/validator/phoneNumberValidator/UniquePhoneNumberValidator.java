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
            Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
            if (user.isEmpty()) {
                return true;
            } else {
                User newUser = user.get();
                Boolean exist = userRepository.existsUserByPhoneNumberAndUuidIsNot(phoneNumber, newUser.getUuid());
                return !exist;
            }
        } catch (NullPointerException nullPointerException){
            return true;
        }
    }
}
