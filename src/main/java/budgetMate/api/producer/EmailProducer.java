package budgetMate.api.producer;

import budgetMate.api.api.contact.request.SendContactRequest;
import budgetMate.api.domain.EmailAuthToken;
import budgetMate.api.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailProducer {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${spring.application.url}")
    private String baseUrl;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    /**
     * <h2>Send confirmation email.</h2>
     * @param user {User}
     * @param token {EmailAuthToken}
     */
    public void sendConfirmationEmail(User user, EmailAuthToken token) {
        final String url = String.format("%s/api/v2/auth/register/confirm?email=%s&token=%s",
                baseUrl, user.getUsername(), token.getToken());
        final String fullName = String.format("%s %s", user.getFirstname(), user.getLastname());
        final Map<String, Object> params = Map.of("username", fullName, "url", url);
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> body = Map.of(
                "sender", Map.of("email", "budgetmate27@gmail.com", "name", "BudgetMate"),
                "to", new Map[]{Map.of("email", user.getUsername(), "name", fullName)},
                "templateId", 1,
                "params", params
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(BREVO_API_URL, request, String.class);
    }

    /**
     * <h2>Send contact email.</h2>
     * @param contactRequest {SendContactRequest}
     */
    public void sendContactEmail(SendContactRequest contactRequest){
        final Map<String, Object> params = Map.of(
                "firstName", contactRequest.getFirstname(),
                "lastName", contactRequest.getLastname(),
                "email", contactRequest.getEmail(),
                "message", contactRequest.getMessage());
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> body = Map.of(
                "sender", Map.of("email", "budgetmate27@gmail.com", "name", "BudgetMate"),
                "to", new Map[]{Map.of("email", "robertsargsyan6@gmail.com", "name", "Robert Sargsyan")},
                "templateId", 2,
                "params", params
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(BREVO_API_URL, request, String.class);
    }
}
