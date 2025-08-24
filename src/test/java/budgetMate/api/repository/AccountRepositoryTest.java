package budgetMate.api.repository;

import budgetMate.api.domain.Account;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("repo@test.com")
                .password("pass")
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    @Test
    void deletingUser_shouldAlsoDeleteAccounts() {
        Account account = Account.builder()
                .name("Main")
                .currency(Currency.USD)
                .currentBalance(10)
                .type(AccountType.CASH)
                .createdBy(user)
                .build();
        accountRepository.save(account);

        assertThat(accountRepository.findAll()).hasSize(1);

        userRepository.delete(user);

        assertThat(accountRepository.findAll()).isEmpty();
    }

    @Test
    void savingAccountWithoutRequiredName_shouldFail() {
        Account account = Account.builder()
                .currency(Currency.USD)
                .currentBalance(5)
                .type(AccountType.CASH)
                .createdBy(user)
                .build();

        assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.saveAndFlush(account));
    }
}
