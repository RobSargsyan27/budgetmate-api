package budgetMate.api.api.records.response;

import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecordResponse(
        UUID id, Double amount, UUID userId, RecordCategory category, RecordType type, String note, Currency currency,
        LocalDateTime paymentTime, String withdrawalAccountName, UUID withdrawalAccountId, String receivingAccountName,
        UUID receivingAccountId) { }
