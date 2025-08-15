package budgetMate.api.api.accountRequest;

import budgetMate.api.api.accountRequest.request.AddExistingAccountRequest;
import budgetMate.api.api.accountRequest.request.UpdateExistingAccountRequest;
import budgetMate.api.api.accountRequest.service.AccountRequestService;
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
    public ResponseEntity<Void> addAccountRequest(
            HttpServletRequest request,
            @RequestBody @Valid AddExistingAccountRequest body){
        return httpUtil.handleAdd(accountRequestService.addExistingAccountRequest(request, body));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateAccountRequest(
            HttpServletRequest request,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateExistingAccountRequest body)
    {
        return httpUtil.handleUpdate(accountRequestService.updateExistingAccountRequest(request, id, body));
    }
}
