package com.github.RobSargsyan27.budgetMateV2.app.api.analytics;

import com.github.RobSargsyan27.budgetMateV2.app.api.analytics.response.AnalyticsResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.analytics.response.ChartResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.analytics.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsResponse> getDashboardAnalytics(HttpServletRequest request){
        return ResponseEntity.ok(analyticsService.getDashboardAnalytics(request));
    }

    @GetMapping("/overview/{startDate}/{endDate}")
    public ResponseEntity<AnalyticsResponse> getUserRecordsOverview(
            @PathVariable String startDate,
            @PathVariable String endDate,
            HttpServletRequest request
    ){
        return ResponseEntity.ok(analyticsService.getUserRecordsOverview(startDate, endDate, request));
    }

    @GetMapping("overview-line/{startDate}/{endDate}/{recordType}")
    public ResponseEntity<ChartResponse> getUserRecordsOverviewLineChart(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @PathVariable String recordType,
            HttpServletRequest request
    ){
        return ResponseEntity.ok(analyticsService.getUserRecordsOverviewLineChart(startDate, endDate, recordType, request));
    }

    @GetMapping("/dashboard/categories-pie")
    public ResponseEntity<ChartResponse> getDashboardCategoriesPieChart(HttpServletRequest request){
        return ResponseEntity.ok(analyticsService.getDashboardCategoriesPieChart(request));
    }

    @GetMapping("/dashboard/expenses-line-chart")
    public ResponseEntity<ChartResponse> getDashboardExpensesLineChart(HttpServletRequest request){
        return ResponseEntity.ok(analyticsService.getDashboardExpensesLineChart(request));
    }
}
