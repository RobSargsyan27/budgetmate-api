package budgetMate.api.api.user.service;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    UserResponse getUser(HttpServletRequest request);

    UserResponse updateUser(HttpServletRequest request, UpdateUserRequest body);

    void deleteUser(HttpServletRequest request);

    List<AccountAdditionResponse> getNotifications(HttpServletRequest request);
}
