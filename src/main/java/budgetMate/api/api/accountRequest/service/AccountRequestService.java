package budgetMate.api.api.accountRequest.service;

import budgetMate.api.api.accountRequest.request.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface AccountRequestService {
    Void addExistingAccountRequest(HttpServletRequest request, AddExistingAccountRequest body);

    Void updateExistingAccountRequest(HttpServletRequest request, UUID requestId, UpdateExistingAccountRequest body);
}
