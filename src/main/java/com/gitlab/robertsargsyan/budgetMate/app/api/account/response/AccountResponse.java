package com.gitlab.robertsargsyan.budgetMate.app.api.account.response;

import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.AccountType;
import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class AccountResponse {
    private UUID id;

    private String name;

    private Currency currency;

    private double currentBalance;

    private AccountType type;

    private String avatarColor;
}
