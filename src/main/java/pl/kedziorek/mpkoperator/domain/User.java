package pl.kedziorek.mpkoperator.domain;

import lombok.*;
import org.hibernate.Hibernate;
import pl.kedziorek.mpkoperator.config.validator.emailValidator.UniqueEmail;
import pl.kedziorek.mpkoperator.config.validator.peselValidator.UniquePesel;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.UniquePhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.ValidPhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.usernameValidator.UniqueUsername;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

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
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
