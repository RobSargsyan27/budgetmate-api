package budgetMate.api.repository;

import budgetMate.api.domain.Account;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.Role;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AccountRepositoryTest {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    private User user1;
    private User user2;
    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        user1 = User.builder().username("John").password("pass").role(Role.USER).build();
        user2 = User.builder().username("Fred").password("pass").role(Role.USER).build();
        UUID user1Id = userRepository.save(user1).getId();
        userRepository.save(user2);

        account1 = Account.builder()
                .name("Investments")
                .currency(Currency.USD)
                .currentBalance(100.0)
                .type(AccountType.CASH)
                .avatarColor("#123456")
                .createdBy(user1)
                .build();

        account2 = Account.builder()
                .name("Savings")
                .currency(Currency.USD)
                .currentBalance(250.0)
                .type(AccountType.BONUS)
                .avatarColor("#654321")
                .createdBy(user1)
                .build();

        UUID account1Id = accountRepository.save(account1).getId();
        UUID account2Id = accountRepository.save(account2).getId();
        accountRepository.addUserAccountAssociation(user1Id, account1Id);
        accountRepository.addUserAccountAssociation(user1Id, account2Id);

        em.flush();
        em.clear();
    }

    @Test
    void getUserAccounts_returnsAccountsFromJoinTable() {
        List<Account> accounts = accountRepository.getUserAccounts(user1.getId());
        List<String> names = accounts.stream().map(Account::getName).toList();

        assertThat(names).containsExactlyInAnyOrder("Investments", "Savings");
    }

    @Test
    void getUserAccountById_returnsOnlyWhenCreatedByMatches() {
        assertThat(accountRepository.getUserAccountById(user1, account1.getId()))
                .isPresent()
                .get()
                .extracting(Account::getName)
                .isEqualTo("Investments");

        assertThat(accountRepository.getUserAccountById(user2, account1.getId()))
                .isNotPresent();
    }

    @Test
    void getUserAccount_byOwnerIdAndName() {
        assertThat(accountRepository.getUserAccount(user1.getId(), "Investments"))
                .isPresent()
                .get()
                .extracting(Account::getId)
                .isEqualTo(account1.getId());

        assertThat(accountRepository.getUserAccount(user1.getId(), "DoesNotExist"))
                .isNotPresent();
    }

    @Test
    void updateAccountCurrentBalance_incrementsBalanceAtomically() {
        UUID id = account1.getId();

        accountRepository.updateAccountCurrentBalance(id, 50.0);
        em.flush();
        em.clear();

        double updated = accountRepository.findById(id).orElseThrow().getCurrentBalance();
        assertThat(updated).isEqualTo(150.0);
    }

    @Test
    void addUserAccountAssociation_insertsIntoJoinTable() {
        assertThat(accountRepository.getUserAccounts(user2.getId())).isEmpty();

        accountRepository.addUserAccountAssociation(user2.getId(), account1.getId());
        em.flush();
        em.clear();

        List<Account> teammateAccounts = accountRepository.getUserAccounts(user2.getId());
        assertThat(teammateAccounts)
                .extracting(Account::getId)
                .containsExactly(account1.getId());

        int count = accountRepository.countUserAccountAssociations(account1.getId());
        assertThat(count).isEqualTo(2);
    }

    @Test
    void deleteUserAccountAssociation_removesFromJoinTable() {
        accountRepository.addUserAccountAssociation(user2.getId(), account1.getId());
        em.flush();
        em.clear();

        List<Account> teammateAccounts = accountRepository.getUserAccounts(user2.getId());
        assertThat(teammateAccounts)
                .extracting(Account::getId)
                .containsExactly(account1.getId());

        accountRepository.deleteUserAccountAssociation(user2.getId(), account1.getId());
        em.flush();
        em.clear();

        assertThat(accountRepository.getUserAccounts(user2.getId())).isEmpty();

        int count = accountRepository.countUserAccountAssociations(account1.getId());
        assertThat(count).isEqualTo(1);
    }

    @Test
    void countUserAccountAssociations_countsRowsForGivenAccount() {
        int initial = accountRepository.countUserAccountAssociations(account1.getId());
        assertThat(initial).isEqualTo(1);

        accountRepository.addUserAccountAssociation(user2.getId(), account1.getId());
        em.flush();
        em.clear();

        int after = accountRepository.countUserAccountAssociations(account1.getId());
        assertThat(after).isEqualTo(2);
    }

    @Test
    void deleteAccountById_deletesAccountRow() {
        accountRepository.deleteUserAccountAssociation(user1.getId(), account2.getId());
        accountRepository.deleteAccountById(account2.getId());
        em.flush();
        em.clear();

        assertThat(accountRepository.findById(account2.getId())).isEmpty();
    }
}
