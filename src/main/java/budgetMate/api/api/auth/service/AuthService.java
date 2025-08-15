package budgetMate.api.api.auth.service;

import budgetMate.api.api.auth.request.RegistrationRequest;
import budgetMate.api.api.auth.request.TokenAuthenticationRequest;
import budgetMate.api.api.auth.request.UserAuthenticationRequest;
import budgetMate.api.api.auth.response.UserAuthenticationResponse;

import java.util.Map;

public interface AuthService {
    Void register(RegistrationRequest request) throws IllegalStateException;

    void confirmRegister(String email, String token);

    UserAuthenticationResponse login(UserAuthenticationRequest request);

    Map<String, Boolean> validateToken(TokenAuthenticationRequest request);
}
