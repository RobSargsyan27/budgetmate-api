package budgetMate.api.api.users;

import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.api.users.request.UpdateUserRequest;
import budgetMate.api.api.users.response.UserResponse;
import budgetMate.api.api.users.service.UserService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/users")
public class UserController {
    private final UserService userService;
    private final HttpUtil httpUtil;

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request){
        return httpUtil.handleGet(userService.getUser(request));
    }

    @PatchMapping("")
    public ResponseEntity<UserResponse> updateUser(
            HttpServletRequest request,
            @RequestBody @Valid UpdateUserRequest body)
    {
        return httpUtil.handleUpdate(userService.updateUser(request, body));
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request){
        return httpUtil.handleUpdate(userService.deleteUser(request));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountAdditionResponse>> getUserNotifications(HttpServletRequest request){
        return httpUtil.handleGet(userService.getUserNotifications(request));
    }
}
