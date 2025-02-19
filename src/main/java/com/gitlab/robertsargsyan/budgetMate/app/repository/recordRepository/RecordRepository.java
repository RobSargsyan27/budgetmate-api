package com.gitlab.robertsargsyan.budgetMate.app.repository.recordRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.Record;
import com.gitlab.robertsargsyan.budgetMate.app.domain.RecordCategory;
import com.gitlab.robertsargsyan.budgetMate.app.domain.User;
import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface RecordRepository {
    Optional<Record> getRecordById(UUID id);

    List<Record> getRecordsByUser(User user);

    BigDecimal findSumOfUserRecordsAmountsByCategories(User user, List<RecordCategory> categories);

    int deleteRecordById(UUID id);

    void deleteAccountRecords(UUID accountId);

    BigDecimal userIntervalAnalytics(User user, RecordType type, LocalDateTime startOfInterval, LocalDateTime endOfInterval);

    List<Object[]> findCurrentMonthTopCategoriesByType(
            User user, RecordType type, LocalDateTime startOfMonth, LocalDateTime startOfNextMonth, Pageable pageable
    );

    List<Object[]> findExpenseGroupedByDate(
            User user, RecordType type, LocalDateTime startOfMonth, LocalDateTime startOfNextMonth
    );

    Record save(Record record);

    long count(Specification<Record> spec);

    List<Record> findAll(Specification<Record> spec);

    Page<Record> findAll(@Nullable Specification<Record> spec, Pageable pageable);
}
