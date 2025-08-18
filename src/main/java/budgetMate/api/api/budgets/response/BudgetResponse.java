package budgetMate.api.api.budgets.response;

import budgetMate.api.domain.RecordCategory;

import java.util.List;
import java.util.UUID;

public record BudgetResponse (UUID id, String name, Double amount, UUID userId, List<RecordCategory> recordCategories){ }
