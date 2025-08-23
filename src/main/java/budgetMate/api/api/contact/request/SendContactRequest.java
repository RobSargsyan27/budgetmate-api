package budgetMate.api.api.contact.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SendContactRequest {
    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String message;
}
