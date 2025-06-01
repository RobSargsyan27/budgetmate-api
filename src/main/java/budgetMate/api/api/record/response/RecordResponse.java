package budgetMate.api.api.record.response;

import budgetMate.api.domain.Record;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class RecordResponse {
    private UUID id;

    private double amount;

    private UUID userId;

    private RecordCategory category;

    private RecordType type;

    private String note;

    private Currency currency;

    private LocalDateTime paymentTime;

    private String withdrawalAccountName;

    private UUID withdrawalAccountId;

    private String receivingAccountName;

    private UUID receivingAccountId;

    public static RecordResponse from(Record record) {
        final String receivingAccountName = record.getReceivingAccount() == null ? null : record.getReceivingAccount().getName();
        final String withdrawalAccountName = record.getWithdrawalAccount() == null ? null : record.getWithdrawalAccount().getName();

        final UUID receivingAccountId = record.getReceivingAccount() == null ? null : record.getReceivingAccount().getId();
        final UUID withdrawalAccountId = record.getWithdrawalAccount() == null ? null : record.getWithdrawalAccount().getId();

        return RecordResponse.builder()
                .id(record.getId())
                .userId(record.getUser().getId())
                .amount(record.getAmount())
                .category(record.getCategory())
                .currency(record.getCurrency())
                .type(record.getType())
                .note(record.getNote())
                .paymentTime(record.getPaymentTime())
                .withdrawalAccountName(withdrawalAccountName)
                .withdrawalAccountId(withdrawalAccountId)
                .receivingAccountName(receivingAccountName)
                .receivingAccountId(receivingAccountId)
                .build();
    }

    public static List<RecordResponse> from(List<Record> records){
        return records.isEmpty() ? List.of() : records.stream().map(RecordResponse::from).toList();
    }
}
