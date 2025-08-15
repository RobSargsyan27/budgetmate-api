package budgetMate.api.api.account;

import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.AddExistingAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import budgetMate.api.api.account.response.AccountResponse;
import budgetMate.api.api.account.service.AccountService;
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
@RequestMapping("/api/v2/account")
public class AccountController {
    private final AccountService accountService;
    private final HttpUtil httpUtil;

    @GetMapping("")
    public ResponseEntity<List<AccountResponse>> getAccounts(HttpServletRequest request){
        return httpUtil.handleGet(accountService.getAccounts(request));
    }

    @PostMapping("")
    public ResponseEntity<AccountResponse> addAccount(
            HttpServletRequest request,
            @RequestBody @Valid AddAccountRequest body)
    {
        return httpUtil.handleAdd(accountService.addAccount(request, body));
    }

    @PostMapping("/existing")
    public ResponseEntity<Void> addExistingAccountRequest(
            HttpServletRequest request,
            @RequestBody @Valid AddExistingAccountRequest body)
    {

        return httpUtil.handleAdd(accountService.sendAddExistingAccountRequest(request, body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id, HttpServletRequest request){
        return httpUtil.handleGet(accountService.getAccount(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            HttpServletRequest request,
            @RequestBody @Valid UpdateAccountRequest body,
            @PathVariable UUID id) {
        return httpUtil.handleUpdate(accountService.updateAccount(request, body, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(HttpServletRequest request, @PathVariable UUID id){
        return httpUtil.handleDelete(accountService.deleteAccount(request, id));
    }

    @PostMapping("/{id}/{status}")
    public ResponseEntity<Void> updateAccountRequestStatus(
            HttpServletRequest request,
            @PathVariable UUID id,
            @PathVariable Boolean status){
        return httpUtil.handleUpdate(accountService.updateAccountRequestStatus(request, id, status));
    }
}
