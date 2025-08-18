package budgetMate.api.api.accountAdditionRequests;

import budgetMate.api.api.accountAdditionRequests.request.AddAccountAdditionRequest;
import budgetMate.api.api.accountAdditionRequests.request.UpdateAccountAdditionRequest;
import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.api.accountAdditionRequests.service.AccountAdditionRequestService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account-requests")
public class AccountAdditionRequestController {
    private final AccountAdditionRequestService accountAdditionRequestService;
    private final HttpUtil httpUtil;

    @PostMapping("")
    public ResponseEntity<AccountAdditionResponse> addUserAccountRequest(
            HttpServletRequest request,
            @RequestBody @Valid AddAccountAdditionRequest body)
    {
        return httpUtil.handleAdd(accountAdditionRequestService.addUserAccountRequest(request, body));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountAdditionResponse> updateUserAccountRequest(
            HttpServletRequest request,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateAccountAdditionRequest body)
    {
        return httpUtil.handleUpdate(accountAdditionRequestService.updateUserAccountRequest(request, id, body));
    }
}
