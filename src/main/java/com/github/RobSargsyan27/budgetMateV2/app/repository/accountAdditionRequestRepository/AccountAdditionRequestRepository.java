package com.github.RobSargsyan27.budgetMateV2.app.repository.accountAdditionRequestRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.AccountAdditionRequest;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;

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
