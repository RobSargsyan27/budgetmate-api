package budgetMate.api.api.record.response;

import budgetMate.api.api.account.response.AccountResponse;
import budgetMate.api.api.user.response.UserResponse;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class RecordReportResponse {
    private UUID id;

    private double amount;

    private UserResponse user;

    private RecordCategory category;

    private RecordType type;

    private String note;

    private Currency currency;

    private String paymentTime;

    private AccountResponse withdrawalAccount;

    private AccountResponse receivingAccount;
}
