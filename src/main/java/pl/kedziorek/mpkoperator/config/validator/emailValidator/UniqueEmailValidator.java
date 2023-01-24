package pl.kedziorek.mpkoperator.config.validator.emailValidator;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        try{
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return true;
            } else {
                User newUser = user.get();
                Boolean exist = userRepository.existsUserByEmailAndUuidIsNot(email, newUser.getUuid());
                return !exist;
            }
        } catch (NullPointerException nullPointerException){
            return true;
        }
    }
}
