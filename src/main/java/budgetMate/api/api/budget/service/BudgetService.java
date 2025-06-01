package budgetMate.api.api.budget.service;

import budgetMate.api.api.budget.request.AddBudgetRequest;
import budgetMate.api.api.budget.request.UpdateBudgetRequest;
import budgetMate.api.api.budget.response.BudgetResponse;
import budgetMate.api.api.budget.response.BudgetsCurrentBalanceResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BudgetService {
    List<BudgetResponse> getUserBudgets(HttpServletRequest request);

    List<BudgetsCurrentBalanceResponse> getUserBudgetsCurrentBalance(HttpServletRequest request);

    byte[] getBudgetsReport(HttpServletRequest request);

    BudgetResponse getBudget(String id);

    BudgetResponse addBudget(AddBudgetRequest request);

    BudgetResponse updateBudget(UpdateBudgetRequest request, String id);

    Integer deleteBudget(String id);
}
