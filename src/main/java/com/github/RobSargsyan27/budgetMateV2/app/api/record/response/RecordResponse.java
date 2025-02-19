package com.github.RobSargsyan27.budgetMateV2.app.api.record.response;

import com.github.RobSargsyan27.budgetMateV2.app.domain.RecordCategory;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.Currency;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
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
}
