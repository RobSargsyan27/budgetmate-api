package budgetMate.api.api.auth.service;

import budgetMate.api.api.auth.request.RegistrationRequest;
import budgetMate.api.api.auth.request.UserAuthenticationRequest;

public interface AuthService {
    Void register(RegistrationRequest request) throws IllegalStateException;

    Void confirmRegister(String email, String token);

    Void login(UserAuthenticationRequest request);
}
