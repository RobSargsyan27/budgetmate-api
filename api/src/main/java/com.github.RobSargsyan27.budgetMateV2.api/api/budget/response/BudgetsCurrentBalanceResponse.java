package com.github.RobSargsyan27.budgetMateV2.api.api.budget.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class BudgetsCurrentBalanceResponse {
    private UUID id;

    private double currentBalance;
}
