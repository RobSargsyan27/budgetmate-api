package budgetMate.api.lib;

import budgetMate.api.api.budgets.mapper.BudgetResponseMapper;
import budgetMate.api.api.budgets.response.BudgetResponse;
import budgetMate.api.domain.Budget;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BudgetLib {
    private final BudgetResponseMapper budgetResponseMapper;

    /**
     * <h2>Generate budgets report.</h2>
     * @param budgetsList {List<Budget>}
     * @return {File}
     */
    public File generateBudgetsReport(List<Budget> budgetsList){
        final Gson gson = new Gson();
        final File file = new File("budgets-report.json");
        final List<BudgetResponse> budgets = budgetResponseMapper.toDtoList(budgetsList);

        String jsonReport = gson.toJson(budgets);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonReport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}
