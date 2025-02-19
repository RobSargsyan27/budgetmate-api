package com.github.RobSargsyan27.budgetMateV2.app.repository.emailAuthTokenRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.EmailAuthToken;

import java.util.Optional;
import java.util.UUID;


public interface EmailAuthTokenRepository {

    Optional<EmailAuthToken> getUserEmailAuthToken(UUID token, String username);

    void setEmailAuthTokenAsChecked(UUID token);

    EmailAuthToken save(EmailAuthToken token);
}
