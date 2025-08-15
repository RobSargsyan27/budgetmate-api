package budgetMate.api.api.user;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import budgetMate.api.api.user.service.UserService;
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
@RequestMapping("/api/v2/user")
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
        return httpUtil.handleDelete(userService.deleteUser(request));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountAdditionResponse>> getNotifications(HttpServletRequest request){
        return httpUtil.handleGet(userService.getNotifications(request));
    }
}
