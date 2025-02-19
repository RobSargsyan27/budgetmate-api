package com.gitlab.robertsargsyan.budgetMate.app.api.auth.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenAuthenticationRequest {
    private String token;
}
