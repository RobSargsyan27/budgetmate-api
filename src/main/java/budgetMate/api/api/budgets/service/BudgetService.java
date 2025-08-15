package budgetMate.api.api.budgets.service;

import budgetMate.api.api.budgets.request.AddBudgetRequest;
import budgetMate.api.api.budgets.request.UpdateBudgetRequest;
import budgetMate.api.api.budgets.response.BudgetResponse;
import budgetMate.api.api.budgets.response.BudgetsCurrentBalanceResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface BudgetService {
    List<BudgetResponse> getUserBudgets(HttpServletRequest request);

    BudgetResponse addUserBudget(HttpServletRequest request, AddBudgetRequest body);

    List<BudgetsCurrentBalanceResponse> getUserBudgetsCurrentBalance(HttpServletRequest request);

    byte[] getUserBudgetsReport(HttpServletRequest request);

    BudgetResponse getUserBudget(HttpServletRequest request, UUID id);

    BudgetResponse updateUserBudget(HttpServletRequest request, UpdateBudgetRequest body, UUID id);

    Void deleteUserBudget(HttpServletRequest request, UUID id);
}
