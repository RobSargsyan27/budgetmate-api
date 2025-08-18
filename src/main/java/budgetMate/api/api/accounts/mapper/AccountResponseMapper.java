package budgetMate.api.api.accounts.mapper;

import budgetMate.api.api.accounts.response.AccountResponse;
import budgetMate.api.domain.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountResponseMapper {

    AccountResponse toDto(Account account);

    List<AccountResponse> toDtoList(List<Account> accounts);
}
