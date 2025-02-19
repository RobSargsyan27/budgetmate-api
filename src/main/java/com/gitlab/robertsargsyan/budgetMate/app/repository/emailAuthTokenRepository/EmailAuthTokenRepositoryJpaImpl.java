package com.gitlab.robertsargsyan.budgetMate.app.repository.emailAuthTokenRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.EmailAuthToken;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Profile("prod")
@Repository
public interface EmailAuthTokenRepositoryJpaImpl extends JpaRepository<EmailAuthToken, UUID>, EmailAuthTokenRepository {

    @Query("SELECT t FROM EmailAuthToken t JOIN t.user u WHERE t.token = ?1 AND u.username = ?2 AND t.isUsed = FALSE")
    Optional<EmailAuthToken> getUserEmailAuthToken(UUID token, String username);

    @Modifying
    @Query("UPDATE EmailAuthToken t SET t.isUsed = TRUE WHERE t.token = :token")
    void setEmailAuthTokenAsChecked(UUID token);

}
