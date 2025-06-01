package budgetMate.api.api.budget.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class BudgetsCurrentBalanceResponse {
    private UUID id;

    private Double currentBalance;

    public static BudgetsCurrentBalanceResponse from(UUID id, Double currentBalance){
        return BudgetsCurrentBalanceResponse.builder()
                .id(id)
                .currentBalance(currentBalance)
                .build();
    }

    public static List<BudgetsCurrentBalanceResponse> from(Map<UUID, Double> currentBalances){
        return currentBalances.isEmpty()
                ? List.of()
                : currentBalances.entrySet().stream()
                .map(entry -> BudgetsCurrentBalanceResponse.from(entry.getKey(), entry.getValue()))
                .toList();
    }
}
