package com.gitlab.robertsargsyan.budgetMate.app.repository.budgetRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.Budget;
import com.gitlab.robertsargsyan.budgetMate.app.domain.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("prod")
@Repository
public interface BudgetRepositoryJpaImpl extends JpaRepository<Budget, UUID>, BudgetRepository {
    List<Budget> getBudgetsByUser(User user);

    Optional<Budget> getBudgetById(UUID id);

    @Modifying
    int deleteBudgetById(UUID id);
}
