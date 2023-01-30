package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getRolesByNames(Set<String> names);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
}
