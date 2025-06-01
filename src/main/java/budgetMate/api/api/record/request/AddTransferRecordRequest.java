package budgetMate.api.api.record.request;

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
public class AddTransferRecordRequest {
    @Positive
    private double amount;

    private LocalDateTime paymentTime;

    private String note;

    @NotNull
    private UUID withdrawalAccountId;

    @NotNull
    private UUID receivingAccountId;
}
