package com.gitlab.robertsargsyan.budgetMate.app.lib;

import com.gitlab.robertsargsyan.budgetMate.app.api.budget.response.BudgetResponse;
import com.gitlab.robertsargsyan.budgetMate.app.domain.Budget;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class BudgetLib {

    public List<BudgetResponse> buildBudgetsResponse(List<Budget> budgets){
        return budgets.stream().map((budget) ->
                BudgetResponse.builder()
                        .id(budget.getId())
                        .name(budget.getName())
                        .amount(budget.getAmount())
                        .userId(budget.getUser().getId())
                        .recordCategories(budget.getRecordCategories())
                        .build()
                ).toList();
    }

    public File generateBudgetsReport(List<Budget> budgetsList){
        final Gson gson = new Gson();
        final File file = new File("budgets-report.json");
        final List<BudgetResponse> budgets = this.buildBudgetsResponse(budgetsList);

        String jsonReport = gson.toJson(budgets);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonReport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}
