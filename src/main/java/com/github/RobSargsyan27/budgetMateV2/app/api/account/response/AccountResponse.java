package com.github.RobSargsyan27.budgetMateV2.app.api.account.response;

import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.AccountType;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.Currency;
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
