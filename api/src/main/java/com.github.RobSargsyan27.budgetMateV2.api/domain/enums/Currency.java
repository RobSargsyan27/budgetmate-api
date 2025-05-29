package com.github.RobSargsyan27.budgetMateV2.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Currency {
    USD;

    @JsonCreator
    public static Currency fromString(String value) {
        return Currency.valueOf(value.toUpperCase());
    }
}
