package com.gitlab.robertsargsyan.budgetMate.app.api.budget.response;

import com.gitlab.robertsargsyan.budgetMate.app.domain.RecordCategory;
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
