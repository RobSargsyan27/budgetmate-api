package budgetMate.api.api.account;


import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.AddExistingAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import budgetMate.api.api.account.response.AccountResponse;
import budgetMate.api.api.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("")
    public ResponseEntity<List<AccountResponse>> getAccounts(HttpServletRequest request){
        return ResponseEntity.ok(accountService.getAccounts(request));
    }

    @PostMapping("")
    public ResponseEntity<AccountResponse> addAccount(
            HttpServletRequest request,
            @RequestBody @Valid AddAccountRequest body)
    {
        return ResponseEntity.ok(accountService.addAccount(request, body));
    }

    @PostMapping("/existing")
    public HttpStatus sendAddExistingAccountRequest(
            HttpServletRequest request,
            @RequestBody @Valid AddExistingAccountRequest body)
    {
        accountService.sendAddExistingAccountRequest(request, body);
        return HttpStatus.ACCEPTED;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id, HttpServletRequest request){
        return ResponseEntity.ok(accountService.getAccount(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            HttpServletRequest request,
            @RequestBody @Valid UpdateAccountRequest body,
            @PathVariable UUID id) {
        return ResponseEntity.ok(accountService.updateAccount(request, body, id));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteAccount(HttpServletRequest request, @PathVariable UUID id){
        accountService.deleteAccount(request, id);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/{id}/{status}")
    public HttpStatus updateAccountRequestStatus(
            HttpServletRequest request,
            @PathVariable UUID id,
            @PathVariable Boolean status){
        accountService.updateAccountRequestStatus(request, id, status);
        return HttpStatus.ACCEPTED;
    }
}
