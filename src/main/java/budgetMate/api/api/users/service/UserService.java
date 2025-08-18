package budgetMate.api.api.users.service;

import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.api.users.request.UpdateUserRequest;
import budgetMate.api.api.users.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    UserResponse getUser(HttpServletRequest request);

    UserResponse updateUser(HttpServletRequest request, UpdateUserRequest body);

    Void deleteUser(HttpServletRequest request);

    List<AccountAdditionResponse> getUserNotifications(HttpServletRequest request);
}
