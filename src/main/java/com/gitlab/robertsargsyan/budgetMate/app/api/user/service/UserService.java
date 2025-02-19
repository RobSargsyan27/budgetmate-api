package com.gitlab.robertsargsyan.budgetMate.app.api.user.service;

import com.gitlab.robertsargsyan.budgetMate.app.api.account.response.AccountAdditionResponse;
import com.gitlab.robertsargsyan.budgetMate.app.api.user.request.UpdateUserRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.user.response.UserResponse;
import com.gitlab.robertsargsyan.budgetMate.app.domain.AccountAdditionRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    UserResponse getUser(HttpServletRequest request);

    UserResponse updateUser(UpdateUserRequest request);

    Integer deleteUser(String id);

    List<AccountAdditionResponse> getNotifications(HttpServletRequest request);
}
