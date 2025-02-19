package com.github.RobSargsyan27.budgetMateV2.app.api.user.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateUserRequest {
    @NotEmpty
    private String id;

    @Size(min = 3, max = 255, message = "User's firstname should be between 3 and 255 characters.")
    private String firstname;

    @Size(min = 3, max = 255, message = "User's lastname should be between 3 and 255 characters.")
    private String lastname;

    @Size(min = 3, max = 255, message = "User's country should be between 3 and 255 characters.")
    private String country;

    @Size(min = 3, max = 255, message = "User's city should be between 3 and 255 characters.")
    private String city;

    @Size(min = 3, max = 255, message = "User's address should be between 3 and 255 characters.")
    private String address;

    @Size(min = 3, max = 255, message = "User's postal code should be between 3 and 255 characters.")
    private String postalCode;

    @Size(min = 3, max = 255, message = "User's avatar color should be between 3 and 255 characters.")
    private String avatarColor;
}
