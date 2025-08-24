package budgetMate.api.api.auth.service;

import budgetMate.api.api.auth.request.RegistrationRequest;
import budgetMate.api.api.auth.request.UserAuthenticationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    Void register(RegistrationRequest request) throws IllegalStateException;

    Void confirmRegister(String email, String token);

    Void login(UserAuthenticationRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
}
