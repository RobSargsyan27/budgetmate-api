package com.github.RobSargsyan27.budgetMateV2.app.api.auth.service;


import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.TokenAuthenticationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.UserAuthenticationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.RegistrationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.response.UserAuthenticationResponse;
import com.github.RobSargsyan27.budgetMateV2.app.domain.EmailAuthToken;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.Role;
import com.github.RobSargsyan27.budgetMateV2.app.producer.EmailProducer;
import com.github.RobSargsyan27.budgetMateV2.app.repository.emailAuthTokenRepository.EmailAuthTokenRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.userRepository.UserRepository;
import com.github.RobSargsyan27.budgetMateV2.app.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailAuthTokenRepository emailAuthTokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailProducer emailProducer;

    /**
     * <h2>Register.</h2>
     * @param request {RegistrationRequest}
     * @throws IllegalStateException - If user with given email is already registered.
     */
    @Transactional
    public void register(RegistrationRequest request) {
        User user = userRepository.findUserByUsername(request.getEmail()).orElse(null);
        if (user != null && user.isEnabled()) {
            throw new IllegalStateException("User is already registered!");
        }

        if (user == null) {
            user = User.builder()
                    .username(request.getEmail())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .receiveNewsLetters(request.isReceiveNewsLetters())
                    .avatarColor("#00008B")
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        final EmailAuthToken authToken = EmailAuthToken.builder()
                .token(UUID.randomUUID())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        emailAuthTokenRepository.save(authToken);

        emailProducer.sendTemplateEmail(user, authToken);
    }

    /**
     * <h2>Confirm registration.</h2>
     * @param email {String}
     * @param token {String}
     */
    @Transactional
    public void confirmRegister(String email, String token) {
        final UUID _token = UUID.fromString(token);

        EmailAuthToken authToken = emailAuthTokenRepository.getUserEmailAuthToken(_token, email)
                .orElseThrow(() -> new IllegalStateException("Token not found!"));

        if (authToken.getCreatedAt().plusMinutes(30).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("The token is expired! Try to register again!");
        }

        this.emailAuthTokenRepository.setEmailAuthTokenAsChecked(_token);
        this.userRepository.enableUser(email);
    }

    /**
     * <h2>Login.</h2>
     * @param request {AuthenticationRequest}
     * @return AuthenticationResponse
     */
    public UserAuthenticationResponse login(UserAuthenticationRequest request) {
        final String email = request.getEmail();
        final String password = request.getPassword();

        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        User user = userRepository.findUserByUsername(email)
                .orElseThrow(() -> new IllegalStateException("User not found!"));
        String jwt = jwtService.generateToken(user);

        return UserAuthenticationResponse.builder().token(jwt).build();
    }

    public Map<String, Boolean> validateToken(TokenAuthenticationRequest request){
        final String token = request.getToken();

        if(token == null){
            return Map.of("isTokenValid", false);
        }

        final String username = jwtService.extractUsername(token);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found!"));

        return Map.of("isTokenValid", jwtService.isTokenValid(token, user));
    }

}
