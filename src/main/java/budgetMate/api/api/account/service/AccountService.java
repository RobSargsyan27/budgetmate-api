package budgetMate.api.api.account.service;


import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.AddExistingAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import budgetMate.api.api.account.response.AccountResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountResponse> getAccounts(HttpServletRequest request);

    AccountResponse getAccount(UUID id, HttpServletRequest request);

    AccountResponse addAccount(HttpServletRequest request, AddAccountRequest body);

    AccountResponse updateAccount(HttpServletRequest request, UpdateAccountRequest body, UUID id);

    Void deleteAccount(HttpServletRequest request, UUID id);

    Void sendAddExistingAccountRequest(HttpServletRequest request, AddExistingAccountRequest body);

    Void updateAccountRequestStatus(HttpServletRequest request, UUID requestId, Boolean status);
}
