package budgetMate.api.repository.specification;

import budgetMate.api.api.records.request.SearchRecordsRequest;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.RecordType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecordFilterSpecification {
    /**
     * <h2>Build record specification.</h2>
     * @param user {User}
     * @param body {SearchRecordsRequest}
     * @return {Specification<Record>}
     */
    public Specification<Record> buildRecordSpecification(User user, SearchRecordsRequest body) {
        final RecordType recordType = RecordType.fromString(body.getRecordType());
        final Double amountGreaterThan = body.getAmountGreaterThan();
        final Double amountLessThan = body.getAmountLessThan();
        final LocalDateTime paymentTimeGreaterThan = body.getPaymentTimeGreaterThan() == null
                ? null : LocalDateTime.parse(body.getPaymentTimeGreaterThan().substring(0, body.getPaymentTimeGreaterThan().length() - 1));
        final LocalDateTime paymentTimeLessThan = body.getPaymentTimeLessThan() == null
                ? null : LocalDateTime.parse(body.getPaymentTimeLessThan().substring(0, body.getPaymentTimeLessThan().length() - 1));

        return Specification
                .where(RecordFilterSpecification.hasUser(user))
                .and(RecordFilterSpecification.hasRecordType(recordType))
                .and(RecordFilterSpecification.hasPaymentTimeGreaterThan(paymentTimeGreaterThan))
                .and(RecordFilterSpecification.hasPaymentTimeLessThan(paymentTimeLessThan))
                .and(RecordFilterSpecification.hasAmountGreaterThan(amountGreaterThan))
                .and(RecordFilterSpecification.hasAmountLessThan(amountLessThan));
    }

    /**
     * <h2>Has user.</h2>
     * @param user {User}
     * @return {Specification<Record>}
     */
    private static Specification<Record> hasUser(User user){
        return ((root, query, criteriaBuilder) ->
                user == null ? null : criteriaBuilder.equal(root.get("user"), user)
        );
    }

    /**
     * <h2>Has record type.</h2>
     * @param recordType {RecordType}
     * @return {Specification<Record>}
     */
    private static Specification<Record> hasRecordType(RecordType recordType) {
        return ((root, query, criteriaBuilder) ->
                recordType == null ? null : criteriaBuilder.equal(root.get("type"), recordType)
        );
    }

    /**
     * <h2>Has amount greater than.</h2>
     * @param amount {Double}
     * @return {Specification<Record>}
     */
    private static Specification<Record> hasAmountGreaterThan(Double amount) {
        return ((root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.greaterThan(root.get("amount"), amount)
        );
    }

    /**
     * <h2>Has amount less than.</h2>
     * @param amount {Double}
     * @return {Specification<Record>}
     */
    private static Specification<Record> hasAmountLessThan(Double amount) {
        return ((root, query, criteriaBuilder) ->
                amount == null ? null : criteriaBuilder.lessThan(root.get("amount"), amount)
        );
    }

    /**
     * <h2>Has payment time greater than.</h2>
     * @param paymentTime {LocalDateTime}
     * @return {Specification<Record>}
     */
    private static Specification<Record> hasPaymentTimeGreaterThan(LocalDateTime paymentTime) {
        return ((root, query, criteriaBuilder) ->
                paymentTime == null ? null : criteriaBuilder.greaterThan(root.get("paymentTime"), paymentTime)
        );
    }

    /**
     * <h2>Has payment time less than.</h2>
     * @param paymentTime {LocalDateTime}
     * @return {Specification<Record>}
     */
    private static Specification<Record> hasPaymentTimeLessThan(LocalDateTime paymentTime) {
        return ((root, query, criteriaBuilder) ->
                paymentTime == null ? null : criteriaBuilder.lessThan(root.get("paymentTime"), paymentTime)
        );
    }
}
