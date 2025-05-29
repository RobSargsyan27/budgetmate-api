package com.github.RobSargsyan27.budgetMateV2.api.api.account.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class AddExistingAccountRequest {
    @NotEmpty
    private String ownerUsername;

    @NotEmpty
    @Size(min = 3, max = 50, message = "Account should have a name.")
    private String accountName;

    @NotEmpty
    private String userId;
}
