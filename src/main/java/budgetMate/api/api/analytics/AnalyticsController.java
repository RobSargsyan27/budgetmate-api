package budgetMate.api.api.analytics;

import budgetMate.api.api.analytics.response.AnalyticsResponse;
import budgetMate.api.api.analytics.response.ChartResponse;
import budgetMate.api.api.analytics.service.AnalyticsService;
import budgetMate.api.security.CustomUserDetails;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final HttpUtil httpUtil;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsResponse> getUserDashboardAnalytics(@AuthenticationPrincipal CustomUserDetails user){
        return httpUtil.handleGet(analyticsService.getUserDashboardAnalytics(user));
    }

    @GetMapping("/dashboard/categories-pie")
    public ResponseEntity<ChartResponse> getUserDashboardCategoriesPieChart(@AuthenticationPrincipal CustomUserDetails user){
        return httpUtil.handleGet(analyticsService.getUserDashboardCategoriesPieChart(user));
    }

    @GetMapping("/dashboard/expenses-line-chart")
    public ResponseEntity<ChartResponse> getUserDashboardExpensesLineChart(@AuthenticationPrincipal CustomUserDetails user){
        return httpUtil.handleGet(analyticsService.getUserDashboardExpensesLineChart(user));
    }

    @GetMapping("/overview")
    public ResponseEntity<AnalyticsResponse> getUserRecordsOverview(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @AuthenticationPrincipal CustomUserDetails user
    ){
        return httpUtil.handleGet(analyticsService.getUserRecordsOverview(user, startDate, endDate));
    }

    @GetMapping("/overview-line")
    public ResponseEntity<ChartResponse> getUserRecordsOverviewLineChart(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String recordType,
            @AuthenticationPrincipal CustomUserDetails user
    ){
        return httpUtil.handleGet(analyticsService.getUserRecordsOverviewLineChart(user, startDate, endDate, recordType));
    }

    @GetMapping("/overview/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Integer> getUsersCount(){
        return httpUtil.handleGet(analyticsService.getUsersCount());
    }
}
