package budgetMate.api.api.user.service;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse getUser(HttpServletRequest request);

    UserResponse updateUser(UUID id, UpdateUserRequest request);

    Integer deleteUser(String id);

    List<AccountAdditionResponse> getNotifications(HttpServletRequest request);
}
