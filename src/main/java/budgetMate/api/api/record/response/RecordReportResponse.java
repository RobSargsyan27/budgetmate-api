package budgetMate.api.api.record.response;

import budgetMate.api.api.account.response.AccountResponse;
import budgetMate.api.api.user.response.UserResponse;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;
import budgetMate.api.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
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

    public static RecordReportResponse from(Record record) {
        final UserResponse userResponse = UserResponse.from(record.getUser());
        final AccountResponse receivingAccount = record.getReceivingAccount() == null
                ? null
                : AccountResponse.from(record.getReceivingAccount());
        final AccountResponse withdrawalAccount = record.getWithdrawalAccount() == null
                ? null
                : AccountResponse.from(record.getWithdrawalAccount());

        return RecordReportResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .user(userResponse)
                .category(record.getCategory())
                .type(record.getType())
                .note(record.getNote())
                .currency(record.getCurrency())
                .paymentTime(record.getPaymentTime().toString())
                .receivingAccount(receivingAccount)
                .withdrawalAccount(withdrawalAccount)
                .build();
    }

    public static List<RecordReportResponse> from(List<Record> records){
        return records.isEmpty() ? List.of() : records.stream().map(RecordReportResponse::from).toList();
    }
}
