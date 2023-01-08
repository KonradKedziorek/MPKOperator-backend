package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.Address;
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
    private String phoneNumber;
    private Set<Role> roles;
    private Address address;
    private Boolean isActive;
}
