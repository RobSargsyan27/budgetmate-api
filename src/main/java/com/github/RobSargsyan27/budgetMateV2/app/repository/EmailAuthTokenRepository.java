package com.github.RobSargsyan27.budgetMateV2.app.repository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.EmailAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailAuthTokenRepository extends JpaRepository<EmailAuthToken, UUID> {

    @Query("SELECT t FROM EmailAuthToken t JOIN t.user u WHERE t.token = ?1 AND u.username = ?2 AND t.isUsed = FALSE")
    Optional<EmailAuthToken> getUserEmailAuthToken(UUID token, String username);

    @Modifying
    @Query("UPDATE EmailAuthToken t SET t.isUsed = TRUE WHERE t.token = :token")
    void setEmailAuthTokenAsChecked(UUID token);

}
