package budgetMate.api.api.analytics;

import budgetMate.api.api.analytics.response.AnalyticsResponse;
import budgetMate.api.api.analytics.response.ChartResponse;
import budgetMate.api.api.analytics.service.AnalyticsService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final HttpUtil httpUtil;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsResponse> getDashboardAnalytics(HttpServletRequest request){
        return httpUtil.handleGet(analyticsService.getDashboardAnalytics(request));
    }

    @GetMapping("/overview/{startDate}/{endDate}")
    public ResponseEntity<AnalyticsResponse> getUserRecordsOverview(
            @PathVariable String startDate,
            @PathVariable String endDate,
            HttpServletRequest request
    ){
        return httpUtil.handleGet(analyticsService.getUserRecordsOverview(startDate, endDate, request));
    }

    @GetMapping("overview-line/{startDate}/{endDate}/{recordType}")
    public ResponseEntity<ChartResponse> getUserRecordsOverviewLineChart(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @PathVariable String recordType,
            HttpServletRequest request
    ){
        return httpUtil.handleGet(analyticsService.getUserRecordsOverviewLineChart(startDate, endDate, recordType, request));
    }

    @GetMapping("/dashboard/categories-pie")
    public ResponseEntity<ChartResponse> getDashboardCategoriesPieChart(HttpServletRequest request){
        return httpUtil.handleGet(analyticsService.getDashboardCategoriesPieChart(request));
    }

    @GetMapping("/dashboard/expenses-line-chart")
    public ResponseEntity<ChartResponse> getDashboardExpensesLineChart(HttpServletRequest request){
        return httpUtil.handleGet(analyticsService.getDashboardExpensesLineChart(request));
    }
}
