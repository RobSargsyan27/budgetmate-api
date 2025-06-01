package budgetMate.api.api.user;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import budgetMate.api.api.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request){
        return ResponseEntity.ok(userService.getUser(request));
    }

    @PatchMapping("")
    public ResponseEntity<UserResponse> updateUser(
            HttpServletRequest request,
            @RequestBody @Valid UpdateUserRequest body)
    {
        return ResponseEntity.ok(userService.updateUser(request, body));
    }

    @DeleteMapping("")
    public HttpStatus deleteUser(HttpServletRequest request){
        userService.deleteUser(request);
        return HttpStatus.ACCEPTED;
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountAdditionResponse>> getNotifications(HttpServletRequest request){
        return ResponseEntity.ok(userService.getNotifications(request));
    }
}
