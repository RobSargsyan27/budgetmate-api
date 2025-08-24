package budgetMate.api.api;

import budgetMate.api.api.analytics.AnalyticsController;
import budgetMate.api.api.analytics.service.AnalyticsService;
import budgetMate.api.domain.enums.Role;
import budgetMate.api.security.CustomUserDetails;
import budgetMate.api.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnalyticsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AnalyticsControllerTest {
    private final MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @MockBean
    private HttpUtil httpUtil;

    @TestConfiguration
    @EnableMethodSecurity
    static class TestMethodSecurityConfig { }

    @Test
    void getUsersCount_asAdmin_succeeds() throws Exception {
        CustomUserDetails admin = CustomUserDetails.builder()
                .id(UUID.randomUUID())
                .role(Role.ADMIN)
                .username("admin")
                .password("pass")
                .build();

        when(analyticsService.getUsersCount()).thenReturn(5);
        when(httpUtil.handleGet(5)).thenReturn(ResponseEntity.ok(5));

        mockMvc.perform(get("/api/v3/analytics/overview/users").with(user(admin)))
                .andExpect(status().isOk());
    }

    @Test
    void getUsersCount_asUser_forbidden() throws Exception {
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .id(UUID.randomUUID())
                .role(Role.USER)
                .username("user")
                .password("pass")
                .build();

        mockMvc.perform(get("/api/v3/analytics/overview/users").with(user(userDetails)))
                .andExpect(status().isForbidden());
    }
}