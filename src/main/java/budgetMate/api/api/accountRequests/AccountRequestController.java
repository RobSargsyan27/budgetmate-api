package budgetMate.api.api.accountRequests;

import budgetMate.api.api.accountRequests.request.AddUserAccountRequest;
import budgetMate.api.api.accountRequests.request.UpdateUserAccountRequest;
import budgetMate.api.api.accountRequests.response.AccountRequestResponse;
import budgetMate.api.api.accountRequests.service.AccountRequestService;
import budgetMate.api.domain.AccountAdditionRequest;
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
public class AccountRequestController {
    private final AccountRequestService accountRequestService;
    private final HttpUtil httpUtil;

    @PostMapping("")
    public ResponseEntity<AccountRequestResponse> addUserAccountRequest(
            HttpServletRequest request,
            @RequestBody @Valid AddUserAccountRequest body)
    {
        return httpUtil.handleAdd(accountRequestService.addUserAccountRequest(request, body));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountRequestResponse> updateUserAccountRequest(
            HttpServletRequest request,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserAccountRequest body)
    {
        return httpUtil.handleUpdate(accountRequestService.updateUserAccountRequest(request, id, body));
    }
}
