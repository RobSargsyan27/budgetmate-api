package budgetMate.api.api.accountRequests.service;

import budgetMate.api.api.accountRequests.request.*;
import budgetMate.api.api.accountRequests.response.AccountRequestResponse;
import budgetMate.api.domain.AccountAdditionRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface AccountRequestService {
    AccountRequestResponse addUserAccountRequest(HttpServletRequest request, AddUserAccountRequest body);

    AccountRequestResponse updateUserAccountRequest(HttpServletRequest request, UUID requestId, UpdateUserAccountRequest body);
}
