package com.github.RobSargsyan27.budgetMateV2.api.api.account.service;

import com.github.RobSargsyan27.budgetMateV2.app.api.account.request.AddAccountRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.account.request.AddExistingAccountRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.account.request.UpdateAccountRequest;
import com.github.RobSargsyan27.budgetMateV2.api.api.account.response.AccountResponse;
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
