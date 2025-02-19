package com.gitlab.robertsargsyan.budgetMate.app.api.record.response;

import com.gitlab.robertsargsyan.budgetMate.app.api.account.response.AccountResponse;
import com.gitlab.robertsargsyan.budgetMate.app.api.user.response.UserResponse;
import com.gitlab.robertsargsyan.budgetMate.app.domain.RecordCategory;
import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.Currency;
import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.RecordType;
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
