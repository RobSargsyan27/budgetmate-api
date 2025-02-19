package com.github.RobSargsyan27.budgetMateV2.app.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecordType {
    EXPENSE, INCOME, TRANSFER;

    @JsonCreator
    public static RecordType fromString(String value) {
        return value == null ? null : RecordType.valueOf(value.toUpperCase());
    }
}
