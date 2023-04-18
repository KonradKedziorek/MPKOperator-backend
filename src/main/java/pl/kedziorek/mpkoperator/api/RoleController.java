package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.dto.RoleToUserDTO;
import pl.kedziorek.mpkoperator.domain.dto.request.ChangeUserRolesRequest;
import pl.kedziorek.mpkoperator.service.RoleService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@Validated @RequestBody Role role) {
        return ResponseEntity.ok().body(roleService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserDTO form) {
        roleService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/uuid={uuid}/role/changeUserRoles")
    public ResponseEntity<?> changeUserRoles(
            @RequestBody ChangeUserRolesRequest changeUserRolesRequest,
            @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(roleService.changeUserRoles(changeUserRolesRequest, uuid));
    }
}
