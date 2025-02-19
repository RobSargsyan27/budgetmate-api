package com.gitlab.robertsargsyan.budgetMate.app.api.account.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class AccountAdditionResponse {
    private UUID id;

    private String ownerUsername;

    private String requestedUsername;

    private String accountName;
}
