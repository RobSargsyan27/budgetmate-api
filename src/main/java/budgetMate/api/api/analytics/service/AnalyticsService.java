package budgetMate.api.api.analytics.service;

import budgetMate.api.api.analytics.response.AnalyticsResponse;
import budgetMate.api.api.analytics.response.ChartResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AnalyticsService {
    AnalyticsResponse getDashboardAnalytics(HttpServletRequest request);

    ChartResponse getDashboardCategoriesPieChart(HttpServletRequest request);

    ChartResponse getDashboardExpensesLineChart(HttpServletRequest request);

    AnalyticsResponse getUserRecordsOverview(String startDate, String endDate, HttpServletRequest request);

    ChartResponse getUserRecordsOverviewLineChart(
            String startDate, String endDate, String recordType, HttpServletRequest request
    );
}
