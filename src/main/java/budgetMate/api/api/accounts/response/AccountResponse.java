package budgetMate.api.api.accounts.response;

import budgetMate.api.domain.Account;
import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record AccountResponse (
        UUID id, String name, Currency currency, Double currentBalance, AccountType type, String avatarColor
) {
    public static AccountResponse from(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .type(account.getType())
                .name(account.getName())
                .avatarColor(account.getAvatarColor())
                .currency(account.getCurrency())
                .currentBalance(account.getCurrentBalance())
                .build();
    }

    public static List<AccountResponse> from(List<Account> accounts){
        return accounts.isEmpty() ? List.of() : accounts.stream().map(AccountResponse::from).toList();
    }
}
