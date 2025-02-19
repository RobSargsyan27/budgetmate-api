package com.gitlab.robertsargsyan.budgetMate.app.repository.emailAuthTokenRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.EmailAuthToken;

import java.util.Optional;
import java.util.UUID;


public interface EmailAuthTokenRepository {

    Optional<EmailAuthToken> getUserEmailAuthToken(UUID token, String username);

    void setEmailAuthTokenAsChecked(UUID token);

    EmailAuthToken save(EmailAuthToken token);
}
