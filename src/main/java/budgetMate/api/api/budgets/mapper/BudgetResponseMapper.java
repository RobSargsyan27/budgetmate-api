package budgetMate.api.api.budgets.mapper;

import budgetMate.api.api.budgets.response.BudgetResponse;
import budgetMate.api.domain.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetResponseMapper {

    @Mapping(source = "user.id", target = "userId")
    BudgetResponse toDto(Budget budget);

    List<BudgetResponse> toDtoList(List<Budget> budgets);
}
