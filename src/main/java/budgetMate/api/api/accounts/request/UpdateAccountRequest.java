package budgetMate.api.api.accounts.request;

import budgetMate.api.domain.enums.AccountType;
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
