package budgetMate.api.repository;

import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.Role;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    private final UserRepository userRepository;
    private final EntityManager em;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .username("robert")
                .password("pw")
                .role(Role.USER)
                .isEnabled(false)
                .isLocked(false)
                .receiveNewsLetters(false)
                .build();

        user2 = User.builder()
                .username("lilit")
                .password("pw")
                .role(Role.USER)
                .isEnabled(false)
                .isLocked(true)
                .receiveNewsLetters(true)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        em.flush();
        em.clear();
    }

    @Test
    void findUserByUsername_found_and_notFound() {
        Optional<User> found = userRepository.findUserByUsername("robert");
        Optional<User> notFound = userRepository.findUserByUsername("does-not-exist");

        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getUsername()).isEqualTo("robert");
        assertThat(notFound).isEmpty();
    }

    @Test
    void enableUser_setsIsEnabledTrue() {
        User before = userRepository.findUserByUsername("robert").orElseThrow();
        assertThat(before.isEnabled()).isFalse();

        userRepository.enableUser("robert");
        em.flush();
        em.clear();

        User after = userRepository.findUserByUsername("robert").orElseThrow();
        assertThat(after.isEnabled()).isTrue();

        User lilitReloaded = userRepository.findUserByUsername("lilit").orElseThrow();
        assertThat(lilitReloaded.isEnabled()).isFalse();
    }

    @Test
    void countUsers_returnsTotalRowCount() {
        Integer count = userRepository.countUsers();
        assertThat(count).isEqualTo(4);
    }
}
