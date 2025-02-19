package com.github.RobSargsyan27.budgetMateV2.app.api.budget.response;

import com.github.RobSargsyan27.budgetMateV2.app.domain.RecordCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class BudgetResponse {
    private UUID id;

    private String name;

    private double amount;

    private UUID userId;

    private List<RecordCategory> recordCategories;
}
