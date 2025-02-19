package com.github.RobSargsyan27.budgetMateV2.app.repository.accountAdditionRequestRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.AccountAdditionRequest;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("prod")
@Repository
public interface AccountAdditionRequestRepositoryJpaImpl extends JpaRepository<AccountAdditionRequest, UUID>, AccountAdditionRequestRepository{
    Optional<AccountAdditionRequest> getAccountAdditionRequestById(UUID id);

    List<AccountAdditionRequest> findAllByOwnerUser(User ownerUser);

    @Query("SELECT r FROM AccountAdditionRequest r WHERE r.ownerUser = :ownerUser AND r.isRequestChecked = FALSE")
    List<AccountAdditionRequest> findUnapprovedRequestsByOwnerUser(User ownerUser);

    @Modifying
    @Query("UPDATE AccountAdditionRequest r SET r.isRequestApproved = :status, r.isRequestChecked = TRUE WHERE r.id = :id")
    void updateAccountAdditionRequest(UUID id, boolean status);
}
