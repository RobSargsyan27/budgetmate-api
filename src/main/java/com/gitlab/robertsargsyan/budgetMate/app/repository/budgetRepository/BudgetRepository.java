package com.gitlab.robertsargsyan.budgetMate.app.repository.budgetRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.Budget;
import com.gitlab.robertsargsyan.budgetMate.app.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository {
    List<Budget> getBudgetsByUser(User user);

    Optional<Budget> getBudgetById(UUID id);

    int deleteBudgetById(UUID id);

    Budget save(Budget budget);
}
