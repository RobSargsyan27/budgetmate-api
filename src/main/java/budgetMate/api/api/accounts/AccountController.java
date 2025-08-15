package budgetMate.api.api.accounts;

import budgetMate.api.api.accounts.request.AddAccountRequest;
import budgetMate.api.api.accounts.request.UpdateAccountRequest;
import budgetMate.api.api.accounts.response.AccountResponse;
import budgetMate.api.api.accounts.service.AccountService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/accounts")
public class AccountController {
    private final AccountService accountService;
    private final HttpUtil httpUtil;

    @GetMapping("")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(HttpServletRequest request){
        return httpUtil.handleGet(accountService.getUserAccounts(request));
    }

    @PostMapping("")
    public ResponseEntity<AccountResponse> addUserAccount(
            HttpServletRequest request,
            @RequestBody @Valid AddAccountRequest body)
    {
        return httpUtil.handleAdd(accountService.addUserAccount(request, body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getUserAccount(@PathVariable UUID id, HttpServletRequest request){
        return httpUtil.handleGet(accountService.getUserAccount(request, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> updateUserAccount(
            HttpServletRequest request,
            @RequestBody @Valid UpdateAccountRequest body,
            @PathVariable UUID id) {
        return httpUtil.handleUpdate(accountService.updateUserAccount(request, id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccount(HttpServletRequest request, @PathVariable UUID id){
        return httpUtil.handleUpdate(accountService.deleteUserAccount(request, id));
    }
}
