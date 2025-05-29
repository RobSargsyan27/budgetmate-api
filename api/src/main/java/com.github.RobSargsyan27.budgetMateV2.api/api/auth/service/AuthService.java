package com.github.RobSargsyan27.budgetMateV2.api.api.auth.service;

import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.TokenAuthenticationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.UserAuthenticationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.RegistrationRequest;
import com.github.RobSargsyan27.budgetMateV2.api.api.auth.response.UserAuthenticationResponse;

import java.util.Map;

public interface AuthService {
    void register(RegistrationRequest request) throws IllegalStateException;

    void confirmRegister(String email, String token);

    UserAuthenticationResponse login(UserAuthenticationRequest request);

    Map<String, Boolean> validateToken(TokenAuthenticationRequest request);
}
