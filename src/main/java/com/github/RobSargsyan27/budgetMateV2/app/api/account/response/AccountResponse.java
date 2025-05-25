package com.github.RobSargsyan27.budgetMateV2.app.api.account.response;

import com.github.RobSargsyan27.budgetMateV2.app.domain.Account;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.AccountType;
import com.github.RobSargsyan27.budgetMateV2.app.domain.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
        return accounts.stream().map(AccountResponse::from).toList();
    }
}
