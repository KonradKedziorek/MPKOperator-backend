package pl.kedziorek.mpkoperator.config.validator.passwordValidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.ValidPhoneNumberValidator;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StrongPasswordValidatorTest {

    @ParameterizedTest
    @DisplayName(value = "Should return expected result by given password")
    @MethodSource("phoneNumbers")
    void shouldReturnCorrectValidPassword(String password, boolean expectedResult) {
        StrongPasswordValidator validator = new StrongPasswordValidator();
        assertEquals(expectedResult, validator.isValid(password, null));
    }

    private static Stream<Arguments> phoneNumbers() {
        return Stream.of(
                Arguments.of("HaSl012#3", true),
                Arguments.of("654343has", false),
                Arguments.of("test 48", false),
                Arguments.of("haslomaslo", false),
                Arguments.of("latwehaslo", false),
                Arguments.of("HaSl012#3", false),
                Arguments.of("HaSl012#", false),
                Arguments.of("HaSl0123", false),
                Arguments.of("HaSl012343", false),
                Arguments.of("HaSloo012#3", false),
                Arguments.of("HaSloo 012#3", false)
        );
    }
}