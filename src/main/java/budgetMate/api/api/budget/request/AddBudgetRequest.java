package budgetMate.api.api.budget.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class AddBudgetRequest {
    @NotEmpty(message = "User should be specified!")
    private String userId;

    @NotEmpty
    @Size(message = "Budget name should be between 3 and 50 characters.")
    private String name;


    @Positive(message = "Budget amount should be specified!")
    private double amount;

    @NotNull
    private List<String> budgetCategories;
}
