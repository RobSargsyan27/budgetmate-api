package budgetMate.api.repository;

import budgetMate.api.domain.Budget;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.Role;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BudgetRepositoryTest {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    private User user1;
    private User user2;
    private Budget b1_user1;
    private Budget b2_user1;
    private Budget b1_user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder().username("john").password("pass").role(Role.USER).build();
        user2 = User.builder().username("fred").password("pass").role(Role.USER).build();
        userRepository.saveAll(List.of(user1, user2));

        b1_user1 = Budget.builder().name("Groceries").amount(300).user(user1).build();
        b2_user1 = Budget.builder().name("Entertainment").amount(150).user(user1).build();
        b1_user2 = Budget.builder().name("Travel").amount(1).user(user2).build();
        budgetRepository.saveAll(List.of(b1_user1, b2_user1, b1_user2));

        em.flush();
        em.clear();
    }

    @Test
    void getUserBudgets_returnsOnlyBudgetsForThatUser() {
        List<Budget> u1Budgets = budgetRepository.getUserBudgets(user1);
        List<String> u1BudgetsNames = u1Budgets.stream().map(Budget::getName).toList();
        List<Budget> u2Budgets = budgetRepository.getUserBudgets(user2);
        List<String> u2BudgetsNames = u2Budgets.stream().map(Budget::getName).toList();

        assertThat(u1BudgetsNames).containsExactlyInAnyOrder("Groceries", "Entertainment");
        assertThat(u2BudgetsNames).containsExactlyInAnyOrder("Travel");
    }

    @Test
    void getUserBudgetById_scopesByOwner() {
        assertThat(budgetRepository.getUserBudgetById(user1, b1_user1.getId()))
                .isPresent()
                .get()
                .extracting(Budget::getName)
                .isEqualTo("Groceries");

        assertThat(budgetRepository.getUserBudgetById(user2, b1_user1.getId()))
                .isNotPresent();
    }

    @Test
    void deleteUserBudgetById_deletesOnlyWhenOwnerMatches() {
        budgetRepository.deleteUserBudgetById(user1, b2_user1.getId());
        em.flush();
        em.clear();

        assertThat(budgetRepository.findById(b2_user1.getId())).isEmpty();

        budgetRepository.deleteUserBudgetById(user1, b1_user2.getId());
        em.flush();
        em.clear();

        assertThat(budgetRepository.findById(b1_user2.getId())).isPresent();
    }

    @Test
    void setAmount_isClampedToMinimumOfOne() {
        Budget renovation = Budget.builder().name("Renovation").user(user1).build();
        renovation.setAmount(0);
        renovation.setName(null);
        renovation = budgetRepository.save(renovation);
        em.flush();
        em.clear();

        double amount = budgetRepository.findById(renovation.getId()).orElseThrow().getAmount();
        assertThat(amount).isEqualTo(1.0);
    }
}