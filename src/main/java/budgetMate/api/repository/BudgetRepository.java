package budgetMate.api.repository;

import budgetMate.api.domain.Budget;
import budgetMate.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    @Query("SELECT bd FROM Budget bd WHERE bd.user = :user")
    List<Budget> getUserBudgets(User user);

    @Query("SELECT bd FROM Budget bd WHERE bd.id = :id AND bd.user = :user")
    Optional<Budget> getUserBudgetById(User user, UUID id);

    @Modifying
    @Query("DELETE Budget bd WHERE bd.user = :user AND bd.id = :id")
    void deleteUserBudgetById(User user, UUID id);
}
