package budgetMate.api.api.accountRequest.request;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateExistingAccountRequest {
    @NotNull
    private Boolean status;
}
