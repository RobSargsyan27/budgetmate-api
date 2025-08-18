package budgetMate.api.api.accountAdditionRequests.service;

import budgetMate.api.api.accountAdditionRequests.request.*;
import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface AccountAdditionRequestService {
    AccountAdditionResponse addUserAccountRequest(HttpServletRequest request, AddAccountAdditionRequest body);

    AccountAdditionResponse updateUserAccountRequest(HttpServletRequest request, UUID requestId, UpdateAccountAdditionRequest body);
}
