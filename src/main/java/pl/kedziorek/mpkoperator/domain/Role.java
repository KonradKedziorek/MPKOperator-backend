package pl.kedziorek.mpkoperator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kedziorek.mpkoperator.config.validator.roleNameValidator.UniqueRoleName;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role", schema = "public")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role name is mandatory")
    @UniqueRoleName(message = "This role already exist!")
    private String name;
}
