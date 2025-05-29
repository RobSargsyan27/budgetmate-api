package com.github.RobSargsyan27.budgetMateV2.api.repository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.Budget;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> getBudgetsByUser(User user);

    Optional<Budget> getBudgetById(UUID id);

    @Modifying
    int deleteBudgetById(UUID id);
}
