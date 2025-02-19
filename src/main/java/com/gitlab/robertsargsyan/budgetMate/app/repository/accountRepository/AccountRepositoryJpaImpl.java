package com.gitlab.robertsargsyan.budgetMate.app.repository.accountRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.Account;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Profile("prod")
@Repository
public interface AccountRepositoryJpaImpl extends JpaRepository<Account, UUID>, AccountRepository{

    Optional<Account> getAccountById(UUID id);

    @Query("SELECT ac FROM Account ac WHERE ac.createdBy.id = :ownerUserId AND ac.name = :accountName")
    Optional<Account> getUserAccount(UUID ownerUserId, String accountName);

    @Modifying
    @Query("UPDATE Account ac SET ac.currentBalance = ac.currentBalance + :amount WHERE ac.id = :id")
    void addUpAccountCurrentBalance(double amount, UUID id);

    @Modifying
    @Query("UPDATE Account ac SET ac.currentBalance = ac.currentBalance - :amount WHERE ac.id = :id")
    void withdrawFromAccountCurrentBalance(double amount, UUID id);

    @Modifying
    @Query(value = "DELETE FROM user_accounts WHERE user_id = :userId AND account_id = :accountId", nativeQuery = true)
    void deleteUserAccountAssociation(UUID userId, UUID accountId);

    @Query(value = "SELECT COUNT(*) FROM user_accounts WHERE account_id = :accountId", nativeQuery = true)
    int countUsersByAccountId(UUID accountId);

    @Modifying
    @Query(value = "DELETE FROM accounts WHERE id = :accountId", nativeQuery = true)
    void deleteAccountById(UUID accountId);

}
