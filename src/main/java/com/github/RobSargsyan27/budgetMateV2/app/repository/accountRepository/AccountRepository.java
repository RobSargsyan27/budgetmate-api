package com.github.RobSargsyan27.budgetMateV2.app.repository.accountRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Optional<Account> getAccountById(UUID id);

    Optional<Account> getUserAccount(UUID ownerUserId, String accountName);

    void addUpAccountCurrentBalance(double amount, UUID id);

    void withdrawFromAccountCurrentBalance(double amount, UUID id);

    void deleteUserAccountAssociation(UUID userId, UUID accountId);

    int countUsersByAccountId(UUID accountId);

    void deleteAccountById(UUID accountId);

    Account save(Account account);
}
