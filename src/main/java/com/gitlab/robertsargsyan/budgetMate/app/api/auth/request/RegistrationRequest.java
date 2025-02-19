package com.gitlab.robertsargsyan.budgetMate.app.api.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class RegistrationRequest {

    @NotEmpty
    @Size(min = 2, max = 255, message = "Firstname must be specified!")
    private String firstname;

    @NotEmpty
    @Size(min = 2, max = 255, message = "Lastname must be specified!")
    private String lastname;

    @Email
    @NotEmpty(message = "Email must be specified and well-formatted!")
    private String email;

    @NotEmpty(message = "Password must be specified!")
    private String password;

    private boolean receiveNewsLetters;

    public RegistrationRequest(String firstname, String lastname, String email,String password, String receiveNewsLetters){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.receiveNewsLetters = Boolean.parseBoolean(receiveNewsLetters);
    }
}
