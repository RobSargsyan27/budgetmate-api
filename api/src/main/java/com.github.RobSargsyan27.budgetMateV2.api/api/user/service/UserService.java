package com.github.RobSargsyan27.budgetMateV2.api.api.user.service;

import com.github.RobSargsyan27.budgetMateV2.api.api.account.response.AccountAdditionResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.request.UpdateUserRequest;
import com.github.RobSargsyan27.budgetMateV2.api.api.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    UserResponse getUser(HttpServletRequest request);

    UserResponse updateUser(Integer id, UpdateUserRequest request);

    Integer deleteUser(String id);

    List<AccountAdditionResponse> getNotifications(HttpServletRequest request);
}
