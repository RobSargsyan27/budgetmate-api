package budgetMate.api.api.auth.service;

import budgetMate.api.api.auth.request.RegistrationRequest;
import budgetMate.api.domain.EmailAuthToken;
import budgetMate.api.domain.User;
import budgetMate.api.producer.EmailProducer;
import budgetMate.api.repository.EmailAuthTokenRepository;
import budgetMate.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailAuthTokenRepository emailAuthTokenRepository;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailProducer emailProducer;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest("John", "Doe", "john@test.com", "pwd", true);
    }

    @Test
    void register_existingEnabledUser_throwsException() {
        User existing = User.builder().username("john@test.com").isEnabled(true).build();
        when(userRepository.findUserByUsername("john@test.com")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(IllegalStateException.class);

        verify(userRepository, never()).save(any());
        verify(emailAuthTokenRepository, never()).save(any());
    }

    @Test
    void register_newUser_savesUserAndToken() {
        when(userRepository.findUserByUsername("john@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pwd")).thenReturn("enc");

        authService.register(registrationRequest);

        verify(userRepository).save(any(User.class));
        verify(emailAuthTokenRepository).save(any(EmailAuthToken.class));
        verify(emailProducer).sendConfirmationEmail(any(), any());
    }

    @Test
    void confirmRegister_tokenNotFound_throwsException() {
        UUID token = UUID.randomUUID();
        when(emailAuthTokenRepository.getUserEmailAuthToken(token, "john@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.confirmRegister("john@test.com", token.toString()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void confirmRegister_tokenExpired_throwsException() {
        UUID token = UUID.randomUUID();
        EmailAuthToken authToken = EmailAuthToken.builder()
                .token(token)
                .user(new User())
                .createdAt(LocalDateTime.now().minusMinutes(31))
                .build();
        when(emailAuthTokenRepository.getUserEmailAuthToken(token, "john@test.com"))
                .thenReturn(Optional.of(authToken));

        assertThatThrownBy(() -> authService.confirmRegister("john@test.com", token.toString()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void confirmRegister_validToken_marksCheckedAndEnablesUser() {
        UUID token = UUID.randomUUID();
        EmailAuthToken authToken = EmailAuthToken.builder()
                .token(token)
                .user(new User())
                .createdAt(LocalDateTime.now())
                .build();
        when(emailAuthTokenRepository.getUserEmailAuthToken(token, "john@test.com"))
                .thenReturn(Optional.of(authToken));

        authService.confirmRegister("john@test.com", token.toString());

        verify(emailAuthTokenRepository).setEmailAuthTokenAsChecked(token);
        verify(userRepository).enableUser("john@test.com");
    }
}
