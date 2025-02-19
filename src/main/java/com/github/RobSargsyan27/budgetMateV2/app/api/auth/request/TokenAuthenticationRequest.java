package com.github.RobSargsyan27.budgetMateV2.app.api.auth.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenAuthenticationRequest {
    private String token;
}
