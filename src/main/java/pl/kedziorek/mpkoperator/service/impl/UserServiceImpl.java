package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Address;
import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.domain.dto.request.CreateUserRequest;
import pl.kedziorek.mpkoperator.domain.dto.request.ResetPasswordRequest;
import pl.kedziorek.mpkoperator.repository.RoleRepository;
import pl.kedziorek.mpkoperator.repository.UserRepository;
import pl.kedziorek.mpkoperator.service.AddressService;
import pl.kedziorek.mpkoperator.service.EmailService;
import pl.kedziorek.mpkoperator.service.RoleService;
import pl.kedziorek.mpkoperator.service.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleService roleService;
    private final AddressService addressService;

    @Override
    @Valid
    public User saveUser(CreateUserRequest createUserRequest) {
        log.info("Saving new user to the database");
        Set<Role> roles = roleService.getRolesByNames(createUserRequest.getRoles());

        Address address = addressService.findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
                createUserRequest.getCity(),
                createUserRequest.getPostcode(),
                createUserRequest.getStreet(),
                createUserRequest.getLocalNumber(),
                createUserRequest.getHouseNumber()
        );
        User user = User.map(createUserRequest, roles);

        String notEncodedPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAddress(address);

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange(48, 125).build();
        user.setUsername(randomStringGenerator.generate(10));

        userRepository.save(user);
        user.setUsername((createUserRequest.getName() + "_" + createUserRequest.getSurname() + "_" + user.getId()).toLowerCase(Locale.ROOT));

        emailService.sendMail(emailService.prepareInfoMailAboutCreatedAccount(
                user.getId(),
                createUserRequest.getEmail(),
                createUserRequest.getName(),
                notEncodedPassword,
                user.getCreatedBy()));

        return user;
    }

    @Override
    public User resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange(48, 125).build();

        String password = randomStringGenerator.generate(9);
        user.setPassword(passwordEncoder.encode(password));
        emailService.sendMail(emailService.prepareMailToResetPassword(resetPasswordRequest.getEmail(), user.getName(), password));
        return user;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Role %s not found in the database", roleName)));
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));
    }
}
