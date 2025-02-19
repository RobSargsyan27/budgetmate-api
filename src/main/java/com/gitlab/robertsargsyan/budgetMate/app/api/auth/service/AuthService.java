package com.gitlab.robertsargsyan.budgetMate.app.api.auth.service;

import com.gitlab.robertsargsyan.budgetMate.app.api.auth.request.TokenAuthenticationRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.auth.request.UserAuthenticationRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.auth.request.RegistrationRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.auth.response.UserAuthenticationResponse;

import java.util.Map;

public interface AuthService {
    void register(RegistrationRequest request) throws IllegalStateException;

    void confirmRegister(String email, String token);

    UserAuthenticationResponse login(UserAuthenticationRequest request);

    Map<String, Boolean> validateToken(TokenAuthenticationRequest request);
}
