package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.repository.RoleRepository;
import pl.kedziorek.mpkoperator.service.RoleService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Set<Role> getRolesByNames(Set<String> names) {
        Set<Role> roles = new HashSet<>();
        for (String name : names) {
            if (roleRepository.findByName(name).isPresent()) {
                roles.add(roleRepository.findByName(name)
                        .orElseThrow(()-> new ResourceNotFoundException(
                        String.format("Role %s not found in the database", name))));
            }
        }
        return roles;
    }
}
