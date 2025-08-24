package budgetMate.api.api.auth;

import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.Role;
import budgetMate.api.producer.EmailProducer;
import budgetMate.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private EmailProducer emailProducer;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void registerNewUser_returnsCreated() throws Exception {
        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\":\"John\",\"lastname\":\"Doe\",\"email\":\"john@example.com\",\"password\":\"pwd\",\"receiveNewsLetters\":true}"))
                .andExpect(status().isCreated());

        assertThat(userRepository.findUserByUsername("john@example.com")).isPresent();
    }

    @Test
    void registerExistingUser_returnsNotFound() throws Exception {
        User existing = User.builder().username("jane@example.com").password("pwd").role(Role.USER).isEnabled(true).build();
        userRepository.save(existing);

        mockMvc.perform(post("/api/v2/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\":\"Jane\",\"lastname\":\"Doe\",\"email\":\"jane@example.com\",\"password\":\"pwd\",\"receiveNewsLetters\":false}"))
                .andExpect(status().isNotFound());
    }
}
