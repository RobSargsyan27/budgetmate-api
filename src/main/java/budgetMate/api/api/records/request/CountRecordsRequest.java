package budgetMate.api.api.records.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class CountRecordsRequest {
    private String recordType;

    private Double amountGreaterThan;

    private Double amountLessThan;

    private String paymentTimeGreaterThan;

    private String paymentTimeLessThan;
}
