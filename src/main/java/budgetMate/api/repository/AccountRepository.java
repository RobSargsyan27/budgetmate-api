package budgetMate.api.repository;

import budgetMate.api.domain.Account;
import budgetMate.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>{

    @Query("SELECT ac FROM Account ac WHERE ac.id = :id AND ac.createdBy = :user")
    Optional<Account> getUserAccountById(User user, UUID id);

    @Query("SELECT ac FROM Account ac WHERE ac.createdBy.id = :ownerUserId AND ac.name = :accountName")
    Optional<Account> getUserAccount(UUID ownerUserId, String accountName);

    @Modifying
    @Query("UPDATE Account ac SET ac.currentBalance = ac.currentBalance + :amount WHERE ac.id = :id")
    void updateAccountCurrentBalance(UUID id, double amount);

    @Modifying
    @Query(value = "DELETE FROM user_accounts WHERE user_id = :userId AND account_id = :accountId", nativeQuery = true)
    void deleteUserAccountAssociation(UUID userId, UUID accountId);

    @Query(value = "SELECT COUNT(*) FROM user_accounts WHERE account_id = :id", nativeQuery = true)
    int countUsersByAccountId(UUID id);

    @Modifying
    @Query(value = "DELETE FROM Account a WHERE a.id = :id")
    void deleteAccountById(UUID id);

}
