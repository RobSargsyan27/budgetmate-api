package com.github.RobSargsyan27.budgetMateV2.api.api.account.request;

import com.github.RobSargsyan27.budgetMateV2.api.domain.enums.AccountType;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateAccountRequest {

    @Size(min = 3, max = 50, message = "Account should have a name.")
    private String name;

    private double currentBalance;

    private AccountType type;

    private String avatarColor;
}
