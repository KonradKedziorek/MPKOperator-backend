package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.kedziorek.mpkoperator.config.exception.BadRequestException;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.*;
import pl.kedziorek.mpkoperator.domain.dto.request.*;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.repository.RoleRepository;
import pl.kedziorek.mpkoperator.repository.UserImageRepository;
import pl.kedziorek.mpkoperator.repository.UserRepository;
import pl.kedziorek.mpkoperator.service.AddressService;
import pl.kedziorek.mpkoperator.service.EmailService;
import pl.kedziorek.mpkoperator.service.RoleService;
import pl.kedziorek.mpkoperator.service.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService<User> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleService roleService;
    private final AddressService addressService;
    private final UserImageRepository userImageRepository;

//    @Override
//    public User saveOrUpdateUser(UserRequest userRequest) {
//        log.info("Saving new user to the database");
//        //if uuid is null should create new object
//        if (Objects.equals(userRequest.getUuid(), "")) {
//            return saveUser(userRequest);
//        }//else update existing object
//        return editUser(userRequest);
//    }

    @Override
    @Valid
    public User saveUser(UserRequest userRequest) {
        log.info("Saving new user to the database");
        Set<Role> roles = roleService.getRolesByNames(userRequest.getRoles());

        Address address = addressService.findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
                userRequest.getCity(),
                userRequest.getPostcode(),
                userRequest.getStreet(),
                userRequest.getLocalNumber(),
                userRequest.getHouseNumber()
        );
        User user = User.map(userRequest, roles);

        String notEncodedPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAddress(address);

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange(48, 125).build();
        user.setUsername(randomStringGenerator.generate(10));

        userRepository.save(user);
        user.setUsername((userRequest.getName() + "_" + userRequest.getSurname() + "_" + user.getId()).toLowerCase(Locale.ROOT));

        emailService.sendMail(emailService.prepareInfoMailAboutCreatedAccount(
                user.getId(),
                userRequest.getEmail(),
                userRequest.getName(),
                notEncodedPassword,
                user.getCreatedBy()));

        return user;
    }

//    @Override
//    @Valid
//    public User saveUser(CreateUserRequest createUserRequest, MultipartFile multipartFile) throws IOException {
//        log.info("Saving new user to the database");
//        Set<Role> roles = roleService.getRolesByNames(createUserRequest.getRoles());
//
//        Address address = addressService.findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
//                createUserRequest.getCity(),
//                createUserRequest.getPostcode(),
//                createUserRequest.getStreet(),
//                createUserRequest.getLocalNumber(),
//                createUserRequest.getHouseNumber()
//        );
//        User user = User.map(createUserRequest, roles);
//
//        String notEncodedPassword = user.getPassword();
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setAddress(address);
//
//        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange(48, 125).build();
//        user.setUsername(randomStringGenerator.generate(10));
//
//        uploadUserImage(user, multipartFile);
//
//        userRepository.save(user);
//        user.setUsername((createUserRequest.getName() + "_" + createUserRequest.getSurname() + "_" + user.getId()).toLowerCase(Locale.ROOT));
//
//        emailService.sendMail(emailService.prepareInfoMailAboutCreatedAccount(
//                user.getId(),
//                createUserRequest.getEmail(),
//                createUserRequest.getName(),
//                notEncodedPassword,
//                user.getCreatedBy()));
//
//        return user;
//    }

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
    public User updateUsersPassword(UpdateUsersPasswordRequest passwordRequest, UUID uuid) {
        User updatedUser = userRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("User not found in the database"));

        log.info("Updating user's password with uuid: {} to the database", updatedUser.getUuid());
        updatedUser.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        updatedUser.setModifiedAt(LocalDateTime.now());

        if (passwordEncoder.matches(passwordRequest.getOldPassword(), updatedUser.getPassword())) {
            if (passwordRequest.getNewPassword().equals(passwordRequest.getRepeatNewPassword())) {
                updatedUser.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            } else {
                throw new BadRequestException("New password and repeated new password not matches!");
            }
        } else {
            throw new BadRequestException("Wrong password!");
        }
        return userRepository.save(updatedUser);
    }

    @Override
    public User updateUsersData(UpdateUserDataRequest updateUserDataRequest, UUID uuid) {
        User updatedUser = userRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("User not found in the database"));

        Set<Role> roles = roleService.getRolesByNames(updateUserDataRequest.getRoles());

        Address address = addressService.findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
                updateUserDataRequest.getCity(),
                updateUserDataRequest.getPostcode(),
                updateUserDataRequest.getStreet(),
                updateUserDataRequest.getLocalNumber(),
                updateUserDataRequest.getHouseNumber()
        );

        log.info("Updating user's password with uuid: {} to the database", updatedUser.getUuid());
        updatedUser.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        updatedUser.setModifiedAt(LocalDateTime.now());
        updatedUser.setName(updateUserDataRequest.getName());
        updatedUser.setSurname(updateUserDataRequest.getSurname());
        updatedUser.setEmail(updateUserDataRequest.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(updateUserDataRequest.getPassword()));
        updatedUser.setPesel(updateUserDataRequest.getPesel());
        updatedUser.setPhoneNumber(updateUserDataRequest.getPhoneNumber());
        updatedUser.setAddress(address);
        updatedUser.setIsActive(updateUserDataRequest.getIsActive());
        updatedUser.setRoles(roles);

        return userRepository.save(updatedUser);
    }

    //TODO Change role method

    //TODO przenieść to do role service
    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public DataResponse<User> getUsers(Map<String, String> params, int page, int size) {
        Page<User> pageUser = userRepository.findAllParams(
                params.get("name") == null ? "" : params.get("name"),
                params.get("surname") == null ? "" : params.get("surname"),
                params.get("username") == null ? "" : params.get("username"),
                params.get("email") == null ? "" : params.get("email"),
                params.get("pesel") == null ? "" : params.get("pesel"),
                params.get("phoneNumber") == null ? "" : params.get("phoneNumber"),
                PageRequest.of(page, size)
        );

        return DataResponse.<User>builder()
                .data(pageUser.get().collect(Collectors.toList()))
                .page(pageUser.getTotalPages())
                .size(pageUser.getTotalElements())
                .build();
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



//    public void uploadUserImage(User user, MultipartFile multipartFile) throws IOException {
//        if (multipartFile.getOriginalFilename() != null
//                && !StringUtils.cleanPath(multipartFile.getOriginalFilename()).contains("..")) {
//            UserImage userImage = new UserImage();
//            userImage.setBytes(multipartFile.getBytes());
//            userImage.setName(multipartFile.getOriginalFilename());
//            userImage.setUuid(UUID.randomUUID());
//
//            user.setUserImage(userImage);
//            userRepository.save(user);
//            userImageRepository.save(userImage);
//        }
//    }

    @Override
    public User findByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("User not found in database"));
    }

//    private User editUser(UserRequest userRequest) {
//        User user = findByUuid(UUID.fromString(userRequest.getUuid()));
//        var userRef = changePropertiesValue(userRequest, user);
//        return userRepository.save(userRef);
//    }

//    private User changePropertiesValue(UserRequest userRequest, User user) {
//        Set<Role> roles = roleService.getRolesByNames(userRequest.getRoles());
//
//        Address address = addressService.findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
//                userRequest.getCity(),
//                userRequest.getPostcode(),
//                userRequest.getStreet(),
//                userRequest.getLocalNumber(),
//                userRequest.getHouseNumber()
//        );
//
//        user.setName(userRequest.getName());
//        user.setSurname(userRequest.getSurname());
//        user.setEmail(userRequest.getEmail());
//        user.setPesel(userRequest.getPesel());
//        user.setPhoneNumber(userRequest.getPhoneNumber());
//        user.setAddress(address);
//        user.setRoles(roles);
//        user.setIsActive(userRequest.getIsActive());
//        user.setModifiedAt(LocalDateTime.now());
//        user.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
//        return user;
//    }
}
