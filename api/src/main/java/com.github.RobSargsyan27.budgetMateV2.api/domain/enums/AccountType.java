package com.github.RobSargsyan27.budgetMateV2.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AccountType {
    GENERAL, CASH, CREDIT_CARD, SAVINGS_ACCOUNT, BONUS;

    @JsonCreator
    public static AccountType fromString(String value) {
        return AccountType.valueOf(value.toUpperCase());
    }
}
