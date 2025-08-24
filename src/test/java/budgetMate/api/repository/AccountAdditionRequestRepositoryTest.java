package budgetMate.api.repository;

import budgetMate.api.domain.Account;
import budgetMate.api.domain.AccountAdditionRequest;
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
class AccountAdditionRequestRepositoryTest {

    private final AccountAdditionRequestRepository accountAdditionRequestRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final EntityManager entityManager;

    private Account account;
    private User owner;
    private User requested;
    private AccountAdditionRequest request;

    @BeforeEach
    void setUp() {
        owner = User.builder().username("owner").password("pass").role(Role.USER).build();
        requested = User.builder().username("req").password("pass").role(Role.USER).build();
        userRepository.saveAll(List.of(owner, requested));

        request = AccountAdditionRequest.builder()
                .ownerUser(owner)
                .requestedUser(requested)
                .accountName("Test Account")
                .build();
        accountAdditionRequestRepository.save(request);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void getAccountAdditionRequestById() {
        assertThat(accountAdditionRequestRepository.getAccountAdditionRequestById(request.getId()))
                .isPresent()
                .hasValueSatisfying(r -> assertThat(r.getAccountName()).isEqualTo("Test Account"));
    }

    @Test
    void findUnapprovedRequestsByOwnerUser() {
        List<AccountAdditionRequest> list = accountAdditionRequestRepository.findUnapprovedRequestsByOwnerUser(owner);
        assertThat(list).hasSize(1);
    }

    @Test
    void updateAccountAdditionRequest() {
        accountAdditionRequestRepository.updateAccountAdditionRequest(owner, request.getId(), true);
        entityManager.flush();
        entityManager.clear();
        AccountAdditionRequest refreshed = accountAdditionRequestRepository.findById(request.getId()).orElseThrow();
        assertThat(refreshed.isRequestApproved()).isTrue();
        assertThat(refreshed.isRequestChecked()).isTrue();
    }
}
