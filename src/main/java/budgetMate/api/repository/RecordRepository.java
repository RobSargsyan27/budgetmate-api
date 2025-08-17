package budgetMate.api.repository;

import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.User;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID>, JpaSpecificationExecutor<Record> {

    @Override
    @EntityGraph(attributePaths = {"user", "category", "withdrawalAccount", "receivingAccount"})
    Page<Record> findAll(Specification<Record> specification, Pageable pageable);

    @Query("SELECT r FROM Record r " +
            "LEFT JOIN FETCH r.user u " +
            "LEFT JOIN FETCH r.category rc " +
            "LEFT JOIN FETCH r.receivingAccount ra " +
            "LEFT JOIN FETCH r.withdrawalAccount wa " +
            "WHERE r.id = :id AND r.user = :user ")
    Optional<Record> getUserRecordById(User user, UUID id);

    @Query("SELECT r FROM Record r " +
            "LEFT JOIN FETCH r.user u " +
            "LEFT JOIN FETCH r.category rc " +
            "LEFT JOIN FETCH r.receivingAccount ra " +
            "LEFT JOIN FETCH r.withdrawalAccount wa " +
            "WHERE r.user = :user")
    List<Record> getUserRecords(User user);

    @Query("SELECT SUM(r.amount) FROM Record r WHERE r.category IN :categories AND r.user = :user")
    BigDecimal getUserRecordsSumByCategories(User user, List<RecordCategory> categories);

    @Query("SELECT SUM(r.amount) FROM Record r " +
            "WHERE r.type = :type AND " +
            "r.paymentTime >= :startOfInterval AND " +
            "r.paymentTime < :endOfInterval AND " +
            "r.user = :user")
    BigDecimal getUserRecordsIntervalSum(User user, RecordType type, LocalDateTime startOfInterval, LocalDateTime endOfInterval);

    @Query("SELECT r.category, SUM(r.amount) AS totalAmount " +
            "FROM Record r " +
            "WHERE r.user = :user AND r.type = :type AND r.paymentTime >= :startOfMonth AND r.paymentTime < :startOfNextMonth " +
            "GROUP BY r.category " +
            "ORDER BY totalAmount DESC")
    List<Object[]> getUserCurrentMonthTopCategoriesByType(
            User user, RecordType type, LocalDateTime startOfMonth, LocalDateTime startOfNextMonth, Pageable pageable
    );

    @Query("SELECT r.paymentTime, SUM(r.amount) " +
            "FROM Record r WHERE r.user = :user " +
            "AND r.type = :type " +
            "AND r.paymentTime BETWEEN :startOfMonth AND :startOfNextMonth " +
            "GROUP BY r.paymentTime ORDER BY r.paymentTime DESC")
    List<Object[]> getUserExpenseRecordsIntervalSum(
            User user, RecordType type, LocalDateTime startOfMonth, LocalDateTime startOfNextMonth
    );

    @Modifying
    @Query("DELETE FROM Record r WHERE r.id = :id AND r.user = :user")
    void deleteUserRecordById(User user, UUID id);

    @Modifying
    @Query("DELETE FROM Record r WHERE r.receivingAccount.id = :accountId OR r.withdrawalAccount.id = :accountId")
    void deleteAccountRecords(UUID accountId);
}
