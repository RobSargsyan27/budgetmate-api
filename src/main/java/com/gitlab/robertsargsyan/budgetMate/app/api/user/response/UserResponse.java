package com.gitlab.robertsargsyan.budgetMate.app.api.user.response;

import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;

    private String username;

    private String firstname;

    private String lastname;

    private String country;

    private String city;

    private String address;

    private String postalCode;

    private String avatarColor;

    private Role role;
}
