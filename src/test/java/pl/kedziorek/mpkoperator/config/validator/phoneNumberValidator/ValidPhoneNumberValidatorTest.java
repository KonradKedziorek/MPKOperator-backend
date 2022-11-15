package pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidPhoneNumberValidatorTest {

    @ParameterizedTest
    @DisplayName(value = "Should return expected result by given phone number")
    @MethodSource("phoneNumbers")
    void shouldReturnCorrectValidPhoneNumberResult(String phoneNumber, boolean expectedResult) {
        ValidPhoneNumberValidator validator = new ValidPhoneNumberValidator();
        assertEquals(expectedResult, validator.isValid(phoneNumber, null));
    }

    private static Stream<Arguments> phoneNumbers() {
        return Stream.of(
                Arguments.of("667589439", true),
                Arguments.of("782989900", true),
                Arguments.of("123321422", true),
                Arguments.of("12 332132", false),
                Arguments.of("", false),
                Arguments.of(" ", false),
                Arguments.of(null, false),
                Arguments.of("243", false),
                Arguments.of("127865342567356", false)
        );
    }
}