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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("")
    public ResponseEntity<List<AccountResponse>> getAccounts(HttpServletRequest request){
        return ResponseEntity.ok(accountService.getAccounts(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String id){
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PostMapping("")
    public ResponseEntity<AccountResponse> addAccount(@RequestBody @Valid AddAccountRequest request){
        return ResponseEntity.ok(accountService.addAccount(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestBody @Valid UpdateAccountRequest request,
            @PathVariable String id) {
        return ResponseEntity.ok(accountService.updateAccount(request, id));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteAccount(
            HttpServletRequest request,
            @PathVariable String id){
        accountService.deleteAccount(request, id);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/existing")
    public HttpStatus sendAddExistingAccount(@RequestBody @Valid AddExistingAccountRequest request){
        accountService.sendAddExistingAccountRequest(request);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/{id}/{status}")
    public HttpStatus updateAccountRequestStatus(@PathVariable String id, @PathVariable String status){
        accountService.updateAccountRequestStatus(id, status);
        return HttpStatus.ACCEPTED;
    }
}
