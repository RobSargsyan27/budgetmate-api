package budgetMate.api.api.accountAdditionRequests.mapper;

import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.domain.AccountAdditionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountAdditionResponseMapper {

    @Mapping(source = "ownerUser.username", target = "ownerUsername")
    @Mapping(source = "requestedUser.username", target = "requestedUsername")
    AccountAdditionResponse toDto(AccountAdditionRequest request);

    List<AccountAdditionResponse> toDtoList(List<AccountAdditionRequest> requests);
}
