package budgetMate.api.api.budgets.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateBudgetRequest {

    @Size(message = "Budget name should be between 3 and 50 characters.")
    private String name;

    @Positive(message = "Budget amount should be specified!")
    private double amount;

    private List<String> recordCategories;
}
