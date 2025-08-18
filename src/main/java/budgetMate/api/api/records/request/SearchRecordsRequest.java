package budgetMate.api.api.records.request;

import budgetMate.api.domain.enums.RecordType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class SearchRecordsRequest {
    private RecordType recordType;

    private Double amountGreaterThan;

    private Double amountLessThan;

    private LocalDateTime paymentTimeGreaterThan;

    private LocalDateTime paymentTimeLessThan;

    private Integer limit;

    private Integer offset;
}
