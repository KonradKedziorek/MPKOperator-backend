package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Role;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.domain.dto.request.CreateUserRequest;
import pl.kedziorek.mpkoperator.domain.dto.request.ResetPasswordRequest;
import pl.kedziorek.mpkoperator.domain.dto.request.UpdateUserDataRequest;
import pl.kedziorek.mpkoperator.domain.dto.request.UpdateUsersPasswordRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;

import java.util.Map;
import java.util.UUID;

public interface UserService<T> {
    User saveUser(CreateUserRequest user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    User resetPassword(ResetPasswordRequest resetPasswordRequest);
    User updateUsersPassword(UpdateUsersPasswordRequest passwordRequest, UUID uuid);
    User updateUsersData(UpdateUserDataRequest updateUserDataRequest, UUID uuid);
    DataResponse<T> getUsers(Map<String, String> params, int page, int size);
}
