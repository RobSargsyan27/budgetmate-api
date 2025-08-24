package budgetMate.api.repository;

import budgetMate.api.domain.EmailAuthToken;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.Role;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EmailAuthTokenRepositoryTest {

    private final EmailAuthTokenRepository emailAuthTokenRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    private User user1;
    private User user2;
    private EmailAuthToken tokenActiveUser1;
    private EmailAuthToken tokenUsedUser1;
    private EmailAuthToken tokenActiveUser2;

    @BeforeEach
    void setUp() {
        user1 = User.builder().username("john").password("pass").role(Role.USER).build();
        user2 = User.builder().username("fred").password("pass").role(Role.USER).build();
        userRepository.saveAll(List.of(user1, user2));

        tokenActiveUser1 = EmailAuthToken.builder()
                .token(UUID.randomUUID())
                .user(user1)
                .createdAt(LocalDateTime.now())
                .isUsed(false)
                .build();

        tokenUsedUser1 = EmailAuthToken.builder()
                .token(UUID.randomUUID())
                .user(user1)
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .isUsed(true)
                .build();

        tokenActiveUser2 = EmailAuthToken.builder()
                .token(UUID.randomUUID())
                .user(user2)
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .isUsed(false)
                .build();

        emailAuthTokenRepository.saveAll(List.of(tokenActiveUser1, tokenUsedUser1, tokenActiveUser2));
        em.flush();
        em.clear();
    }

    @Test
    void getUserEmailAuthToken_returnsMatchWhenUsernameAndTokenAndNotUsed() {
        EmailAuthToken token = emailAuthTokenRepository
                .getUserEmailAuthToken(tokenActiveUser1.getToken(), "john")
                .orElseThrow( () -> new IllegalStateException("Error"));

        assertThat(token).isNotNull();
        assertThat(token.isUsed()).isFalse();
        assertThat(token.getUser().getId()).isEqualTo(user1.getId());
    }

    @Test
    void getUserEmailAuthToken_returnsEmptyForWrongUsernameOrUsedToken() {
        assertThat(emailAuthTokenRepository.getUserEmailAuthToken(tokenActiveUser1.getToken(), "fred"))
                .isNotPresent();

        assertThat(emailAuthTokenRepository.getUserEmailAuthToken(tokenUsedUser1.getToken(), "john"))
                .isNotPresent();

        assertThat(emailAuthTokenRepository.getUserEmailAuthToken(UUID.randomUUID(), "john"))
                .isNotPresent();
    }

    @Test
    void setEmailAuthTokenAsChecked_marksTokenUsed() {
        emailAuthTokenRepository.setEmailAuthTokenAsChecked(tokenActiveUser1.getToken());
        em.flush();
        em.clear();

        EmailAuthToken usedToken = emailAuthTokenRepository.findById(tokenActiveUser1.getToken()).orElseThrow();
        assertThat(usedToken.isUsed()).isTrue();

        assertThat(emailAuthTokenRepository.getUserEmailAuthToken(tokenActiveUser1.getToken(), "john"))
                .isNotPresent();
    }
}
