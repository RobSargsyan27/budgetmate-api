package budgetMate.api.api.records.request;

import budgetMate.api.domain.enums.RecordType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class AddRecordRequest {
    @Positive
    private double amount;

    @NotNull
    private String category;

    @NotNull
    private RecordType type;

    private LocalDateTime paymentTime;

    private String note;

    private UUID receivingAccountId;

    private UUID withdrawalAccountId;
}
