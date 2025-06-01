package budgetMate.api.api.auth.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
public class UserAuthenticationResponse {
    private final String token;
}
