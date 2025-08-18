package budgetMate.api.api.users.mapper;

import budgetMate.api.api.users.response.UserResponse;
import budgetMate.api.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    UserResponse toDto(User user);
}
