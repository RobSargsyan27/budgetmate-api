package com.github.RobSargsyan27.budgetMateV2.api.api.user;

import com.github.RobSargsyan27.budgetMateV2.api.api.account.response.AccountAdditionResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.request.UpdateUserRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.response.UserResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.service.UserService;
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
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request){
        return ResponseEntity.ok(userService.getUser(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateUserRequest request)
    {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

//    @PostMapping("")
//    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserRequest request){
//        return ResponseEntity.ok(userService.updateUser(request));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUser(@PathVariable String id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountAdditionResponse>> getNotifications(HttpServletRequest request){
        return ResponseEntity.ok(userService.getNotifications(request));
    }
}
