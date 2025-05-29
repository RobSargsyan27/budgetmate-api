package com.github.RobSargsyan27.budgetMateV2.api.api.account.request;

import com.github.RobSargsyan27.budgetMateV2.api.domain.enums.AccountType;
import com.github.RobSargsyan27.budgetMateV2.api.domain.enums.Currency;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class AddAccountRequest {
    @NotEmpty
    private String userId;

    @NotEmpty
    @Size(min = 3, max = 50, message = "Account should have a name.")
    private String name;

    private Currency currency;

    private double currentBalance;

    @NotNull
    private AccountType type;

    private String avatarColor;
}
