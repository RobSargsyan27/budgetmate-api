package budgetMate.api.api.accountRequests.response;

import budgetMate.api.domain.AccountAdditionRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class AddExistingAccountResponse {
    private UUID id;

    private String ownerUsername;

    private String requestedUsername;

    private String accountName;

    public static AddExistingAccountResponse from(AccountAdditionRequest request){
        return AddExistingAccountResponse.builder()
                .id(request.getId())
                .ownerUsername(request.getOwnerUser().getUsername())
                .requestedUsername(request.getRequestedUser().getUsername())
                .accountName(request.getAccountName())
                .build();
    }

    public static List<AddExistingAccountResponse> from(List<AccountAdditionRequest> requests){
        return requests.isEmpty() ? List.of() : requests.stream().map(AddExistingAccountResponse::from).toList();
    }
}
