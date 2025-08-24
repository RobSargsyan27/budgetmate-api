package budgetMate.api.api.analytics;

import budgetMate.api.api.analytics.response.AnalyticsResponse;
import budgetMate.api.api.analytics.response.ChartResponse;
import budgetMate.api.api.analytics.service.AnalyticsService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final HttpUtil httpUtil;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsResponse> getUserDashboardAnalytics(HttpServletRequest request){
        return httpUtil.handleGet(analyticsService.getUserDashboardAnalytics(request));
    }

    @GetMapping("/dashboard/categories-pie")
    public ResponseEntity<ChartResponse> getUserDashboardCategoriesPieChart(HttpServletRequest request){
        return httpUtil.handleGet(analyticsService.getUserDashboardCategoriesPieChart(request));
    }

    @GetMapping("/dashboard/expenses-line-chart")
    public ResponseEntity<ChartResponse> getUserDashboardExpensesLineChart(HttpServletRequest request){
        return httpUtil.handleGet(analyticsService.getUserDashboardExpensesLineChart(request));
    }

    @GetMapping("/overview")
    public ResponseEntity<AnalyticsResponse> getUserRecordsOverview(
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletRequest request
    ){
        return httpUtil.handleGet(analyticsService.getUserRecordsOverview(request, startDate, endDate));
    }

    @GetMapping("/overview-line")
    public ResponseEntity<ChartResponse> getUserRecordsOverviewLineChart(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String recordType,
            HttpServletRequest request
    ){
        return httpUtil.handleGet(analyticsService.getUserRecordsOverviewLineChart(request, startDate, endDate, recordType));
    }
}
