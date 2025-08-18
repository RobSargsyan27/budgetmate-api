package budgetMate.api.api.records.response;

import budgetMate.api.api.accounts.response.AccountResponse;
import budgetMate.api.api.users.response.UserResponse;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;

import java.util.UUID;

public record RecordReportResponse(
        UUID id, Double amount, UserResponse user, RecordCategory category, RecordType type, String note,
        Currency currency, String paymentTime, AccountResponse withdrawalAccount, AccountResponse receivingAccount){
}
