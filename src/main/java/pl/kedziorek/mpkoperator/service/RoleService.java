package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.domain.dto.request.ChangeUserRolesRequest;

import java.util.Set;
import java.util.UUID;

public interface RoleService {
    Set<Role> getRolesByNames(Set<String> names);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User changeUserRoles(ChangeUserRolesRequest changeUserRolesRequest, UUID uuid);
}
