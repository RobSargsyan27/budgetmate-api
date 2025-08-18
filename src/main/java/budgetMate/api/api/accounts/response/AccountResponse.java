package budgetMate.api.api.accounts.response;

import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountResponse (
        UUID id, String name, Currency currency, Double currentBalance, AccountType type, String avatarColor
) {}
