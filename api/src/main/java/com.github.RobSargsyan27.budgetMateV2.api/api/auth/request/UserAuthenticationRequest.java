package com.github.RobSargsyan27.budgetMateV2.api.api.auth.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserAuthenticationRequest {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
