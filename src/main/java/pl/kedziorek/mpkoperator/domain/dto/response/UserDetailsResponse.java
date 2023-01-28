package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserDetailsResponse {
    private UUID uuid;

    private String name;

    private String surname;

    private String username;

    private String email;

    private String phoneNumber;

    private String pesel;

    private String createdBy;

    private LocalDateTime createdAt;

    private String modifiedBy;

    private LocalDateTime modifiedAt;

    private Set<Role> roles;

    private String city;

    private String postcode;

    private String street;

    private String localNumber;

    private String houseNumber;
}
