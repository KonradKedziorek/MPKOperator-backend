package pl.kedziorek.mpkoperator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.text.RandomStringGenerator;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kedziorek.mpkoperator.config.validator.emailValidator.UniqueEmail;
import pl.kedziorek.mpkoperator.config.validator.peselValidator.UniquePesel;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.UniquePhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.phoneNumberValidator.ValidPhoneNumber;
import pl.kedziorek.mpkoperator.config.validator.usernameValidator.UniqueUsername;
import pl.kedziorek.mpkoperator.domain.dto.request.UserRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static pl.kedziorek.mpkoperator.domain.Bus.mapBusToBusResponse;

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

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_id")
    @JsonIgnore
    private Bus bus;

    // TODO operacje na userach musza byc na aktywnych userach!!!
    private Boolean isActive;

//    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")    to dodać jakby się coś zapętlało
    @OneToOne(cascade = CascadeType.ALL) //To do usuwania kaskadowego więc nie wiadomo czy się przyda
    @JoinColumn(name = "user_image_id", referencedColumnName = "id")
    private UserImage userImage;

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

    public static User map(UserRequest userRequest, Set<Role> roles, Bus bus) {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange(48, 125).build();
        return User.builder()
                .uuid(Objects.equals(userRequest.getUuid(), "") ? UUID.randomUUID() : UUID.fromString(userRequest.getUuid()))
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .password(randomStringGenerator.generate(9))
                .pesel(userRequest.getPesel())
                .phoneNumber(userRequest.getPhoneNumber())
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .createdAt(LocalDateTime.now())
                .roles(roles)
                .bus(bus)
                .isActive(true)
                .build();
    }

    public static UserResponse responseMap(User user) {
        return UserResponse.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .build();
    }

    public static UserBusResponse mapToUserBusResponse(User user) {
        return UserBusResponse.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static UserDetailsResponse mapToUserDetailsResponse(User user){
        return UserDetailsResponse.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .pesel(user.getPesel())
                .createdBy(user.getCreatedBy())
                .createdAt(user.getCreatedAt())
                .modifiedBy(user.getModifiedBy())
                .modifiedAt(user.getModifiedAt())
                .roles(user.getRoles())
                .city(user.getAddress().getCity())
                .postcode(user.getAddress().getPostcode())
                .street(user.getAddress().getStreet())
                .localNumber(user.getAddress().getLocalNumber())
                .houseNumber(user.getAddress().getHouseNumber())
                .build();
    }
}