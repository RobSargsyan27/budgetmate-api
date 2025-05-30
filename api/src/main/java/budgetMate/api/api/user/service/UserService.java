package budgetMate.api.api.user.service;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    UserResponse getUser(HttpServletRequest request);

    UserResponse updateUser(Integer id, UpdateUserRequest request);

    Integer deleteUser(String id);

    List<AccountAdditionResponse> getNotifications(HttpServletRequest request);
}
