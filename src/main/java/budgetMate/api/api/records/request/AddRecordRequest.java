package budgetMate.api.api.records.request;

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

    private LocalDateTime paymentTime;

    @NotNull
    private String category;

    private String note;

    @NotNull
    private UUID receivingAccountId;
}
