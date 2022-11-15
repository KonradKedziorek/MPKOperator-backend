package pl.kedziorek.mpkoperator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kedziorek.mpkoperator.config.validator.emailValidator.UniqueEmail;
import pl.kedziorek.mpkoperator.config.validator.passwordValidator.StrongPassword;
import pl.kedziorek.mpkoperator.config.validator.peselValidator.UniquePesel;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.UniquePhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.ValidPhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.usernameValidator.UniqueUsername;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank
//    private UUID uuid;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @NotBlank(message = "Username is mandatory")
    @UniqueUsername(message = "User with this username already exist!")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @UniqueEmail(message = "User with this email address already exist!")
    @Email
    private String email;

    @NotBlank(message = "Password is mandatory")
//    @StrongPassword(message = "Invalid password!")
    @Column(length = 256)
    private String password;

    @NotBlank(message = "Pesel is mandatory")
    @UniquePesel(message = "User with this pesel already exist!")
    private String pesel;

    @NotBlank(message = "Phone number is mandatory")
    @ValidPhoneNumber(message = "Invalid phone number!")
    @UniquePhoneNumber(message = "User with this phone number already exist!")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}
