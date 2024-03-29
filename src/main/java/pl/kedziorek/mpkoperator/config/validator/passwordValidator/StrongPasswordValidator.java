package pl.kedziorek.mpkoperator.config.validator.passwordValidator;

import lombok.SneakyThrows;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword,String> {
    @Override
    public void initialize(StrongPassword arg0) {
        arg0.message();
    }


    //TODO POPRAWIC DZIALANIE STRONG PASSWORD - WYSKAKUJE NULL I NIE PRZECHODZI ZADNE HASLO
    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Properties props = new Properties();
        InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream("passay.properties");
        props.load(inputStream);
        MessageResolver resolver = new PropertiesMessageResolver(props);
        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(

        new LengthRule(8, 16),
        new CharacterRule(EnglishCharacterData.UpperCase, 1),
        new CharacterRule(EnglishCharacterData.LowerCase, 1),
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterRule(EnglishCharacterData.Special, 1),
        new WhitespaceRule(),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
        new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)
        ));
        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }
}
