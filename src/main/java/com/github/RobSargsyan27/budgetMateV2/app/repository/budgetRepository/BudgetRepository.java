package com.github.RobSargsyan27.budgetMateV2.app.repository.budgetRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.Budget;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository {
    List<Budget> getBudgetsByUser(User user);

    Optional<Budget> getBudgetById(UUID id);

    int deleteBudgetById(UUID id);

    Budget save(Budget budget);
}
