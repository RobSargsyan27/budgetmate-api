package budgetMate.api.api.accounts.request;

import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
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
    @Size(min = 3, max = 50, message = "Account should have a name.")
    private String name;

    private Currency currency;

    private Double currentBalance;

    @NotNull
    private AccountType type;

    private String avatarColor;
}
