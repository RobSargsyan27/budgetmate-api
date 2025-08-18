package budgetMate.api.api.accountRequests.request;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateUserAccountRequest {
    @NotNull
    private Boolean status;
}
