package com.gitlab.robertsargsyan.budgetMate.app.repository.accountAdditionRequestRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.AccountAdditionRequest;
import com.gitlab.robertsargsyan.budgetMate.app.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountAdditionRequestRepository {
    Optional<AccountAdditionRequest> getAccountAdditionRequestById(UUID id);

    List<AccountAdditionRequest> findAllByOwnerUser(User ownerUser);

    List<AccountAdditionRequest> findUnapprovedRequestsByOwnerUser(User ownerUser);

    void updateAccountAdditionRequest(UUID id, boolean status);

    AccountAdditionRequest save(AccountAdditionRequest request);
}
