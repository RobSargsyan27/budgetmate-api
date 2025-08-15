package budgetMate.api.api.account.service;

import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import budgetMate.api.api.account.response.AccountResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountResponse> getUserAccounts(HttpServletRequest request);

    AccountResponse getUserAccount(HttpServletRequest request, UUID id);

    AccountResponse addUserAccount(HttpServletRequest request, AddAccountRequest body);

    AccountResponse updateUserAccount(HttpServletRequest request,  UUID id, UpdateAccountRequest body);

    Void deleteUserAccount(HttpServletRequest request, UUID id);
}
