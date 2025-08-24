package budgetMate.api.api.accounts.service;

import budgetMate.api.domain.Account;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;
import budgetMate.api.domain.enums.Role;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.RecordRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Account account;

    @BeforeEach
    void init() {
        recordRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();

        user1 = User.builder().username("user1@test.com").password("pass").role(Role.USER).build();
        user2 = User.builder().username("user2@test.com").password("pass").role(Role.USER).build();
        userRepository.save(user1);
        userRepository.save(user2);

        account = Account.builder()
                .name("Wallet")
                .currency(Currency.USD)
                .currentBalance(50)
                .type(AccountType.CASH)
                .createdBy(user1)
                .build();
        accountRepository.save(account);
        accountRepository.addUserAccountAssociation(user1.getId(), account.getId());
    }

    private HttpServletRequest requestFor(User user) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> user.getUsername());
        return request;
    }

    @Test
    void deleteAccount_whenNoOtherAssociations_removesAccountAndRecords() {
        Record record = Record.builder()
                .amount(10)
                .currency(Currency.USD)
                .type(RecordType.EXPENSE)
                .user(user1)
                .withdrawalAccount(account)
                .build();
        recordRepository.save(record);

        accountService.deleteUserAccount(requestFor(user1), account.getId());

        assertThat(accountRepository.findById(account.getId())).isEmpty();
        assertThat(recordRepository.findAll()).isEmpty();
    }

    @Test
    void deleteAccount_whenStillShared_onlyRemovesAssociationAndRecords() {
        accountRepository.addUserAccountAssociation(user2.getId(), account.getId());
        Record record = Record.builder()
                .amount(20)
                .currency(Currency.USD)
                .type(RecordType.EXPENSE)
                .user(user1)
                .withdrawalAccount(account)
                .build();
        recordRepository.save(record);

        accountService.deleteUserAccount(requestFor(user1), account.getId());

        assertThat(accountRepository.findById(account.getId())).isPresent();
        assertThat(accountRepository.countUserAccountAssociations(account.getId())).isEqualTo(1);
        assertThat(recordRepository.findAll()).isEmpty();
    }
}
