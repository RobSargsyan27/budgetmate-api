package com.gitlab.robertsargsyan.budgetMate.app.api.budget.service;

import com.gitlab.robertsargsyan.budgetMate.app.api.budget.request.AddBudgetRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.budget.request.UpdateBudgetRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.budget.response.BudgetResponse;
import com.gitlab.robertsargsyan.budgetMate.app.api.budget.response.BudgetsCurrentBalanceResponse;
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
