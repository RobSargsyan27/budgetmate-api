package budgetMate.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    USER, ADMIN;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    public String getName(){
        return String.format("ROLE_%s", this.name());
    }
}
