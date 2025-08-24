package budgetMate.api.api.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
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

    private Boolean receiveNewsLetters;
}
