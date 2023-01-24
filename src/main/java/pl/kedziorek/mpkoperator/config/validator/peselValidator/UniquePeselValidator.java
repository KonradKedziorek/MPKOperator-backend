package pl.kedziorek.mpkoperator.config.validator.peselValidator;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniquePeselValidator implements ConstraintValidator<UniquePesel,String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniquePesel unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext context) {
        try{
            Optional<User> user = userRepository.findByPesel(pesel);
            if (user.isEmpty()) {
                return true;
            } else {
                User newUser = user.get();
                Boolean exist = userRepository.existsUserByPeselAndUuidIsNot(pesel, newUser.getUuid());
                return !exist;
            }
        } catch (NullPointerException nullPointerException){
            return true;
        }
    }
}
