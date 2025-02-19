package com.github.RobSargsyan27.budgetMateV2.app.api.record.response;

import com.github.RobSargsyan27.budgetMateV2.app.api.account.response.AccountResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.response.UserResponse;
import com.github.RobSargsyan27.budgetMateV2.app.domain.RecordCategory;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.Currency;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.RecordType;
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
