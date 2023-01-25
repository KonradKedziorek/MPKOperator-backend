package pl.kedziorek.mpkoperator.service;

import org.springframework.mail.SimpleMailMessage;
import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.domain.dto.request.*;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;

import java.util.Map;
import java.util.UUID;

public interface UserService<T> {
//    User saveUser(CreateUserRequest user, MultipartFile multipartFile) throws IOException;
    User saveUser(UserRequest user);
    Role saveRole(Role role);
    User findByUuid(UUID uuid);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    User resetPassword(ResetPasswordRequest resetPasswordRequest);
    User updateUsersPassword(UpdateUsersPasswordRequest passwordRequest, UUID uuid);
    User editUserDataByAdmin(UpdateUserDataByAdminRequest updateUserDataByAdminRequest, UUID uuid);
//    User updateUsersData(UpdateUserDataRequest updateUserDataRequest, UUID uuid);
    SimpleMailMessage sendMailFromUserDetails(EmailToUserRequest emailToUserRequest, UUID uuid);
    DataResponse<T> getUsers(Map<String, String> params, int page, int size);
}
