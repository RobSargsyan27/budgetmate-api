package budgetMate.api.repository.specification;

import budgetMate.api.domain.Record;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.RecordType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecordFilterSpecification {
    public Specification<Record> buildRecordSpecification(
            User user, RecordType recordType, String paymentTimeGreaterThan, String paymentTimeLessThan,
            Double amountGreaterThan, Double amountLessThan)
    {
        final LocalDateTime _paymentTimeGreaterThan = paymentTimeGreaterThan == null
                ? null : LocalDateTime.parse(paymentTimeGreaterThan.substring(0, paymentTimeGreaterThan.length() - 1));
        final LocalDateTime _paymentTimeLessThan = paymentTimeLessThan == null
                ? null : LocalDateTime.parse(paymentTimeLessThan.substring(0, paymentTimeLessThan.length() - 1));

        return Specification
                .where(RecordFilterSpecification.hasUser(user))
                .and(RecordFilterSpecification.hasRecordType(recordType))
                .and(RecordFilterSpecification.hasPaymentTimeGreaterThan(_paymentTimeGreaterThan))
                .and(RecordFilterSpecification.hasPaymentTimeLessThan(_paymentTimeLessThan))
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
