package budgetMate.api.api.account.service;


import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.AddExistingAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import budgetMate.api.api.account.response.AccountResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AccountService {
    List<AccountResponse> getAccounts(HttpServletRequest request);

    AccountResponse getAccount(String id);

    AccountResponse addAccount(AddAccountRequest request);

    AccountResponse updateAccount(UpdateAccountRequest request, String id);

    void deleteAccount(HttpServletRequest request, String id);

    void sendAddExistingAccountRequest(AddExistingAccountRequest request);

    void updateAccountRequestStatus(String requestId, String status);
}
