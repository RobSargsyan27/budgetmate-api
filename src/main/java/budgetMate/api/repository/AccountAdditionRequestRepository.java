package budgetMate.api.repository;

import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountAdditionRequestRepository extends JpaRepository<AccountAdditionRequest, UUID> {
    @Query("SELECT ar FROM AccountAdditionRequest ar " +
            "LEFT JOIN FETCH ar.ownerUser u " +
            "LEFT JOIN FETCH ar.requestedUser ru " +
            "WHERE ar.id = :id")
    Optional<AccountAdditionRequest> getAccountAdditionRequestById(UUID id);

    @Query("SELECT r FROM AccountAdditionRequest r WHERE r.ownerUser = :ownerUser AND r.isRequestChecked = FALSE")
    List<AccountAdditionRequest> findUnapprovedRequestsByOwnerUser(User ownerUser);

    @Modifying
    @Query("UPDATE AccountAdditionRequest r SET r.isRequestApproved = :status, r.isRequestChecked = TRUE WHERE r.id = :id AND r.ownerUser = :user")
    void updateAccountAdditionRequest(User user, UUID id, boolean status);
}
