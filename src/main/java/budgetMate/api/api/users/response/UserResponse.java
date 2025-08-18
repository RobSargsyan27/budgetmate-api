package budgetMate.api.api.users.response;

import budgetMate.api.domain.enums.Role;

import java.util.UUID;

public record UserResponse (
        UUID id, String username, String firstname, String lastname, String country, String city, String address,
        String postalCode, String avatarColor, Role role
) { }
