package budgetMate.api.api.record.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class GetAccountFilteredRecordsRequest {
    private String recordType;

    private Double amountGreaterThan;

    private Double amountLessThan;

    private String paymentTimeGreaterThan;

    private String paymentTimeLessThan;
}
