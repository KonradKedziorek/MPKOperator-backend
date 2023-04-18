package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.Role;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID uuid;

    private String name;

    private String surname;

    private String username;

    private String email;

    private String password;

    private String pesel;

    private String phoneNumber;

    private String city;

    private String postcode;

    private String street;

    private String localNumber;

    private String houseNumber;

    private Integer busNumber;

    private Set<Role> roles;

    private Boolean isActive;
}
