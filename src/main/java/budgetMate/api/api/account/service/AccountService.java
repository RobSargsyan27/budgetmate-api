package budgetMate.api.api.account.service;

import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import budgetMate.api.api.account.response.AccountResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountResponse> getAccounts(HttpServletRequest request);

    AccountResponse getAccount(HttpServletRequest request, UUID id);

    AccountResponse addAccount(HttpServletRequest request, AddAccountRequest body);

    AccountResponse updateAccount(HttpServletRequest request,  UUID id, UpdateAccountRequest body);

    Void deleteAccount(HttpServletRequest request, UUID id);
}
