package budgetMate.api.api.user;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import budgetMate.api.api.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request){
        return ResponseEntity.ok(userService.getUser(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserRequest request)
    {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUser(@PathVariable String id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountAdditionResponse>> getNotifications(HttpServletRequest request){
        return ResponseEntity.ok(userService.getNotifications(request));
    }
}
