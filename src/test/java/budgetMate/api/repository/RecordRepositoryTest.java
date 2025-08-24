package budgetMate.api.repository;

import budgetMate.api.domain.*;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;
import budgetMate.api.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RecordRepositoryTest {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final EntityManager em;

    private User user1;
    private User user2;

    private Account user1_account1;
    private Account user1_account2;
    private Account user2_account1;

    private RecordCategory foodCategory;
    private RecordCategory salaryCategory;

    private LocalDateTime now;
    private LocalDateTime startOfMonth;
    private LocalDateTime startOfNextMonth;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        startOfNextMonth = startOfMonth.plusMonths(1);

        user1 = User.builder()
                .username("john")
                .password("pw")
                .role(Role.USER)
                .isEnabled(true)
                .isLocked(false)
                .receiveNewsLetters(false)
                .build();
        user1 = userRepository.save(user1);

        user2 = User.builder()
                .username("fred")
                .password("pw")
                .role(Role.USER)
                .isEnabled(true)
                .isLocked(false)
                .receiveNewsLetters(false)
                .build();
        user2 = userRepository.save(user2);

        user1_account1 = Account.builder()
                .name("A-Checking")
                .currency(Currency.USD)
                .currentBalance(1000.0)
                .type(AccountType.BONUS)
                .avatarColor("#123456")
                .createdBy(user1)
                .build();
        user1_account1 = accountRepository.save(user1_account1);

        user1_account2 = Account.builder()
                .name("A-Savings")
                .currency(Currency.USD)
                .currentBalance(5000.0)
                .type(AccountType.CASH)
                .avatarColor("#654321")
                .createdBy(user1)
                .build();
        user1_account2 = accountRepository.save(user1_account2);

        user2_account1 = Account.builder()
                .name("B-Wallet")
                .currency(Currency.USD)
                .currentBalance(300.0)
                .type(AccountType.GENERAL)
                .avatarColor("#abcdef")
                .createdBy(user2)
                .build();
        user2_account1 = accountRepository.save(user2_account1);

        accountRepository.addUserAccountAssociation(user1.getId(), user1_account1.getId());
        accountRepository.addUserAccountAssociation(user1.getId(), user1_account2.getId());
        accountRepository.addUserAccountAssociation(user2.getId(), user2_account1.getId());

        foodCategory = RecordCategory.builder().name("Food").description("Food & Groceries").build();
        salaryCategory = RecordCategory.builder().name("Salary").description("Income").build();
        em.persist(foodCategory);
        em.persist(salaryCategory);


        LocalDateTime tSame = startOfMonth.plusDays(2).withHour(12);
        persistRecord(Record.builder()
                .amount(25.00)
                .user(user1)
                .type(RecordType.EXPENSE)
                .currency(Currency.USD)
                .category(foodCategory)
                .paymentTime(startOfMonth.plusDays(1))
                .withdrawalAccount(user1_account1));
        persistRecord(Record.builder()
                .amount(15.00)
                .user(user1)
                .type(RecordType.EXPENSE)
                .currency(Currency.USD)
                .category(foodCategory)
                .paymentTime(tSame)
                .withdrawalAccount(user1_account1));
        persistRecord(Record.builder()
                .amount(10.00)
                .user(user1)
                .type(RecordType.EXPENSE)
                .currency(Currency.USD)
                .category(foodCategory)
                .paymentTime(tSame)
                .withdrawalAccount(user1_account1));

        persistRecord(Record.builder()
                .amount(2000.00)
                .user(user1)
                .type(RecordType.INCOME)
                .currency(Currency.USD)
                .category(salaryCategory)
                .paymentTime(startOfMonth.plusDays(1))
                .receivingAccount(user1_account1));
        persistRecord(Record.builder()
                .amount(500.00)
                .user(user1)
                .type(RecordType.INCOME)
                .currency(Currency.USD)
                .category(salaryCategory)
                .paymentTime(startOfMonth.plusDays(10))
                .receivingAccount(user1_account1));

        persistRecord(Record.builder()
                .amount(150.00)
                .user(user1)
                .type(RecordType.TRANSFER)
                .currency(Currency.USD)
                .paymentTime(startOfMonth.plusDays(3))
                .withdrawalAccount(user1_account1)
                .receivingAccount(user1_account2));
        persistRecord(Record.builder()
                .amount(999.00)
                .user(user2)
                .type(RecordType.EXPENSE)
                .currency(Currency.USD)
                .category(foodCategory)
                .paymentTime(startOfMonth.plusDays(5))
                .withdrawalAccount(user2_account1));

        em.flush();
        em.clear();
    }

    private void persistRecord(Record.RecordBuilder builder) {
        em.persist(builder.note("Note").build());
    }

    @Test
    void usersHaveAccountsSanity() {
        List<Account> user1Accounts = accountRepository.getUserAccounts(user1.getId());
        List<Account> user2Accounts = accountRepository.getUserAccounts(user2.getId());

        List<String> user1AccountsName = user1Accounts.stream().map(Account::getName).toList();
        List<String> user2AccountsName = user2Accounts.stream().map(Account::getName).toList();

        assertThat(user1AccountsName).containsExactlyInAnyOrder("A-Checking", "A-Savings");
        assertThat(user2AccountsName).containsExactlyInAnyOrder("B-Wallet");
    }

    @Test
    void getUserRecords_returnsOnlyUsersRecords() {
        List<Record> aRecords = recordRepository.getUserRecords(user1);
        List<Record> bRecords = recordRepository.getUserRecords(user2);

        List<UUID> aRecordsUserIds = aRecords.stream().map(item -> item.getUser().getId()).toList();
        List<UUID> bRecordsUserIds = bRecords.stream().map(item -> item.getUser().getId()).toList();

        assertThat(aRecordsUserIds).containsOnly(user1.getId());
        assertThat(bRecordsUserIds).containsOnly(user2.getId());
        assertThat(aRecordsUserIds.size()).isGreaterThan(bRecordsUserIds.size());
    }

    @Test
    void getUserRecordById_enforcesOwnership() {
        Record record = recordRepository.getUserRecords(user1).getFirst();

        Optional<Record> foundForOwner = recordRepository.getUserRecordById(user1, record.getId());
        Optional<Record> foundForOther = recordRepository.getUserRecordById(user2, record.getId());

        assertThat(foundForOwner).isPresent();
        assertThat(foundForOther).isEmpty();
    }

    @Test
    void accountsAreWiredPerRecordType() {
        List<Record> aRecords = recordRepository.getUserRecords(user1);

        Record expense = aRecords.stream().filter(r -> r.getType() == RecordType.EXPENSE).findFirst().orElseThrow();
        Record income = aRecords.stream().filter(r -> r.getType() == RecordType.INCOME).findFirst().orElseThrow();
        Record transfer = aRecords.stream().filter(r -> r.getType() == RecordType.TRANSFER).findFirst().orElseThrow();

        assertThat(expense.getWithdrawalAccount()).isNotNull();
        assertThat(expense.getWithdrawalAccount().getId()).isNotNull();
        assertThat(expense.getReceivingAccount()).isNull();

        assertThat(income.getReceivingAccount()).isNotNull();
        assertThat(income.getReceivingAccount().getId()).isNotNull();
        assertThat(income.getWithdrawalAccount()).isNull();

        assertThat(transfer.getWithdrawalAccount()).isNotNull();
        assertThat(transfer.getReceivingAccount()).isNotNull();
        assertThat(transfer.getWithdrawalAccount().getId()).isEqualTo(user1_account1.getId());
        assertThat(transfer.getReceivingAccount().getId()).isEqualTo(user1_account2.getId());
    }


    @Test
    void getUserRecordsSumByCategories_works() {
        BigDecimal sumFood = recordRepository.getUserRecordsSumByCategories(user1, List.of(foodCategory));
        BigDecimal sumFoodAndSalary = recordRepository.getUserRecordsSumByCategories(user1, List.of(foodCategory, salaryCategory));
        BigDecimal sumForUserB = recordRepository.getUserRecordsSumByCategories(user2, List.of(foodCategory, salaryCategory));

        assertThat(sumFood).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(sumFoodAndSalary).isEqualByComparingTo(BigDecimal.valueOf(2550.00));
        assertThat(sumForUserB).isEqualByComparingTo(BigDecimal.valueOf(999.00));
    }

    @Test
    void getUserRecordsIntervalSum_intervalAndTypeWork() {
        BigDecimal expenseSum = recordRepository.getUserRecordsIntervalSum(
                user1, RecordType.EXPENSE, startOfMonth, startOfNextMonth);

        BigDecimal incomeSum = recordRepository.getUserRecordsIntervalSum(
                user1, RecordType.INCOME, startOfMonth, startOfNextMonth);

        assertThat(expenseSum).isNotNull();
        assertThat(incomeSum).isNotNull();
        assertThat(expenseSum).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(incomeSum).isEqualByComparingTo(BigDecimal.valueOf(2500.00));
    }

    @Test
    void getUserCurrentMonthTopCategoriesByType_ordersAndPages() {
        Pageable limit1 = PageRequest.of(0, 1);

        List<Object[]> topExpenseCats = recordRepository.getUserCurrentMonthTopCategoriesByType(
                user1, RecordType.EXPENSE, startOfMonth, startOfNextMonth, limit1);

        assertThat(topExpenseCats).hasSize(1);
        Object[] row = topExpenseCats.getFirst();
        RecordCategory category = (RecordCategory) row[0];
        Double total = (Double) row[1];

        assertThat(category.getName()).isEqualTo("Food");
        assertThat(total).isEqualByComparingTo(Double.valueOf(50.00));
    }

    @Test
    void getUserExpenseRecordsIntervalSum_groupsByPaymentTime() {
        List<Object[]> rows = recordRepository.getUserExpenseRecordsIntervalSum(
                user1, RecordType.EXPENSE, startOfMonth, startOfNextMonth);

        assertThat(rows).hasSize(2);

        LocalDateTime time0 = (LocalDateTime) rows.get(0)[0];
        Double sum0 = (Double) rows.get(0)[1];

        LocalDateTime time1 = (LocalDateTime) rows.get(1)[0];
        Double sum1 = (Double) rows.get(1)[1];

        assertThat(sum0).isEqualByComparingTo(Double.valueOf(25.00));
        assertThat(sum1).isEqualByComparingTo(Double.valueOf(25.00));
        assertThat(time0).isAfter(time1);
    }

    @Test
    void deleteUserRecordById_respectsOwnership() {
        Record target = recordRepository.getUserRecords(user1).stream()
                .filter(r -> r.getType() == RecordType.EXPENSE)
                .findFirst().orElseThrow();

        UUID id = target.getId();

        recordRepository.deleteUserRecordById(user2, id);
        em.flush();
        assertThat(recordRepository.getUserRecordById(user1, id)).isPresent();

        recordRepository.deleteUserRecordById(user1, id);
        em.flush();
        assertThat(recordRepository.getUserRecordById(user1, id)).isEmpty();
    }

    @Test
    void deleteAccountRecords_removesAllAccountLinkedRecords() {
        long before = recordRepository.getUserRecords(user1).size();

        recordRepository.deleteAccountRecords(user1_account1.getId());
        em.flush();

        List<Record> remainingA = recordRepository.getUserRecords(user1);
        long after = remainingA.size();

        assertThat(after).isLessThan(before);
        assertThat(remainingA).noneMatch(r ->
                (r.getWithdrawalAccount() != null && r.getWithdrawalAccount().getId().equals(user1_account1.getId())) ||
                        (r.getReceivingAccount() != null && r.getReceivingAccount().getId().equals(user1_account1.getId()))
        );

        assertThat(recordRepository.getUserRecords(user2)).isNotEmpty();
    }
}
