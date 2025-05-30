package budgetMate.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecordType {
    EXPENSE, INCOME, TRANSFER;

    @JsonCreator
    public static RecordType fromString(String value) {
        return value == null ? null : RecordType.valueOf(value.toUpperCase());
    }
}
