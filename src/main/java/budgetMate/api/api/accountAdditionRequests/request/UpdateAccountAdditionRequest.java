package budgetMate.api.api.accountAdditionRequests.request;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateAccountAdditionRequest {
    @NotNull
    private Boolean status;
}
