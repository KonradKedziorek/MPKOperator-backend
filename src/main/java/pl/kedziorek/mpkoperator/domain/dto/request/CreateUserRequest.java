package pl.kedziorek.mpkoperator.domain.dto.request;

import lombok.*;
import pl.kedziorek.mpkoperator.config.validator.emailValidator.UniqueEmail;
import pl.kedziorek.mpkoperator.config.validator.peselValidator.UniquePesel;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.UniquePhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.ValidPhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.usernameValidator.UniqueUsername;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @NotBlank(message = "Email is mandatory")
    @UniqueEmail(message = "User with this email address already exist!")
    @Email
    private String email;

    @NotBlank(message = "Pesel is mandatory")
    @UniquePesel(message = "User with this pesel already exist!")
    private String pesel;

    @NotBlank(message = "Phone number is mandatory")
    @ValidPhoneNumber(message = "Invalid phone number!")
    @UniquePhoneNumber(message = "User with this phone number already exist!")
    private String phoneNumber;

    private String city;

    private String postcode;

    private String street;

    private String localNumber;

    private String houseNumber;

    private Set<String> roles = new HashSet<>();
}
