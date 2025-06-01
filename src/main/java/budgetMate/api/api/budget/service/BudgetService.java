package budgetMate.api.api.budget.service;

import budgetMate.api.api.budget.request.AddBudgetRequest;
import budgetMate.api.api.budget.request.UpdateBudgetRequest;
import budgetMate.api.api.budget.response.BudgetResponse;
import budgetMate.api.api.budget.response.BudgetsCurrentBalanceResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface BudgetService {
    List<BudgetResponse> getUserBudgets(HttpServletRequest request);

    List<BudgetsCurrentBalanceResponse> getUserBudgetsCurrentBalance(HttpServletRequest request);

    byte[] getBudgetsReport(HttpServletRequest request);

    BudgetResponse getBudget(HttpServletRequest request, UUID id);

    BudgetResponse addBudget(HttpServletRequest request, AddBudgetRequest body);

    BudgetResponse updateBudget(HttpServletRequest request, UpdateBudgetRequest body, UUID id);

    Integer deleteBudget(HttpServletRequest request, UUID id);
}
