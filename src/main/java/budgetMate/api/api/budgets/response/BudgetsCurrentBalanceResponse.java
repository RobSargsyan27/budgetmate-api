package budgetMate.api.api.budgets.response;

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

    /**
     * <h2>Parse to BudgetsCurrentBalanceResponse.</h2>
     * @param id {UUID}
     * @param currentBalance {Double}
     * @return {BudgetsCurrentBalanceResponse}
     */
    public static BudgetsCurrentBalanceResponse from(UUID id, Double currentBalance){
        return BudgetsCurrentBalanceResponse.builder()
                .id(id)
                .currentBalance(currentBalance)
                .build();
    }

    /**
     * <h2>Parse to BudgetsCurrentBalanceResponse list.</h2>
     * @param currentBalances {Map<UUID, Double>}
     * @return {List<BudgetsCurrentBalanceResponse>}
     */
    public static List<BudgetsCurrentBalanceResponse> from(Map<UUID, Double> currentBalances){
        return currentBalances.isEmpty()
                ? List.of()
                : currentBalances.entrySet().stream()
                .map(entry -> BudgetsCurrentBalanceResponse.from(entry.getKey(), entry.getValue()))
                .toList();
    }
}
