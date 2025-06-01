package budgetMate.api.repository;

import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.User;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.enums.RecordType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID>, JpaSpecificationExecutor<Record> {
    Optional<Record> getRecordById(UUID id);

    List<Record> getRecordsByUser(User user);

    @Query("SELECT SUM(r.amount) FROM Record r WHERE r.category IN :categories AND r.user = :user")
    BigDecimal findSumOfUserRecordsAmountsByCategories(User user, List<RecordCategory> categories);

    int deleteRecordById(UUID id);

    @Modifying
    @Query("DELETE FROM Record r WHERE r.receivingAccount.id = :accountId OR r.withdrawalAccount.id = :accountId")
    void deleteAccountRecords(UUID accountId);

    @Query("SELECT SUM(r.amount) FROM Record r " +
            "WHERE r.type = :type AND " +
            "r.paymentTime >= :startOfInterval AND " +
            "r.paymentTime < :endOfInterval AND " +
            "r.user = :user")
    BigDecimal userIntervalAnalytics(User user, RecordType type, LocalDateTime startOfInterval, LocalDateTime endOfInterval);

    @Query("SELECT r.category, SUM(r.amount) AS totalAmount " +
            "FROM Record r " +
            "WHERE r.user = :user AND r.type = :type AND r.paymentTime >= :startOfMonth AND r.paymentTime < :startOfNextMonth " +
            "GROUP BY r.category " +
            "ORDER BY totalAmount DESC")
    List<Object[]> findCurrentMonthTopCategoriesByType(
            User user, RecordType type, LocalDateTime startOfMonth, LocalDateTime startOfNextMonth, Pageable pageable
    );

    @Query("SELECT r.paymentTime, SUM(r.amount) " +
            "FROM Record r WHERE r.user = :user " +
            "AND r.type = :type " +
            "AND r.paymentTime BETWEEN :startOfMonth AND :startOfNextMonth " +
            "GROUP BY r.paymentTime ORDER BY r.paymentTime DESC")
    List<Object[]> findExpenseGroupedByDate(
            User user, RecordType type, LocalDateTime startOfMonth, LocalDateTime startOfNextMonth
    );
}
