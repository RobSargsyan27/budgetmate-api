package com.github.RobSargsyan27.budgetMateV2.app.repository.recordRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.Record;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.RecordType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecordFilterSpecification {
    public Specification<Record> buildRecordSpecification(
            User user, RecordType recordType, LocalDateTime paymentTimeGreaterThan, LocalDateTime paymentTimeLessThan,
            Double amountGreaterThan, Double amountLessThan) {
        return Specification
                .where(RecordFilterSpecification.hasUser(user))
                .and(RecordFilterSpecification.hasRecordType(recordType))
                .and(RecordFilterSpecification.hasPaymentTimeGreaterThan(paymentTimeGreaterThan))
                .and(RecordFilterSpecification.hasPaymentTimeLessThan(paymentTimeLessThan))
                .and(RecordFilterSpecification.hasAmountGreaterThan(amountGreaterThan))
                .and(RecordFilterSpecification.hasAmountLessThan(amountLessThan));
    }

    private static Specification<Record> hasUser(User user){
        return ((root, query, criteriaBuilder) ->
                user == null ? null : criteriaBuilder.equal(root.get("user"), user)
        );
    }

    private static Specification<Record> hasRecordType(RecordType recordType) {
        return ((root, query, criteriaBuilder) ->
                recordType == null ? null : criteriaBuilder.equal(root.get("type"), recordType)
        );
    }

    private static Specification<Record> hasAmountGreaterThan(Double amount) {
        return ((root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.greaterThan(root.get("amount"), amount)
        );
    }

    private static Specification<Record> hasAmountLessThan(Double amount) {
        return ((root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.lessThan(root.get("amount"), amount)
        );
    }

    private static Specification<Record> hasPaymentTimeGreaterThan(LocalDateTime paymentTime) {
        return ((root, query, criteriaBuilder) ->
                paymentTime == null ? null : criteriaBuilder.greaterThan(root.get("paymentTime"), paymentTime)
        );
    }

    private static Specification<Record> hasPaymentTimeLessThan(LocalDateTime paymentTime) {
        return ((root, query, criteriaBuilder) ->
                paymentTime == null ? null : criteriaBuilder.lessThan(root.get("paymentTime"), paymentTime)
        );
    }
}
