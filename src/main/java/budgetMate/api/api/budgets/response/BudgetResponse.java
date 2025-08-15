package budgetMate.api.api.budgets.response;

import budgetMate.api.domain.Budget;
import budgetMate.api.domain.RecordCategory;
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

    public static BudgetResponse from (Budget budget){
        return BudgetResponse.builder()
                .id(budget.getId())
                .name(budget.getName())
                .amount(budget.getAmount())
                .recordCategories(budget.getRecordCategories())
                .userId(budget.getUser().getId())
                .build();
    }

    public static List<BudgetResponse> from(List<Budget> budgets){
        return budgets.isEmpty() ? List.of() : budgets.stream().map(BudgetResponse::from).toList();
    }
}
