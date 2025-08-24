package budgetMate.api.api.accountAdditionRequests.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AddAccountAdditionRequest {
    @NotEmpty
    private String ownerUsername;

    @NotEmpty
    @Size(min = 3, max = 50, message = "Account should have a name.")
    private String accountName;
}
