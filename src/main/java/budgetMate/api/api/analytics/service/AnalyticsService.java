package budgetMate.api.api.analytics.service;

import budgetMate.api.api.analytics.response.AnalyticsResponse;
import budgetMate.api.api.analytics.response.ChartResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AnalyticsService {
    AnalyticsResponse getUserDashboardAnalytics(HttpServletRequest request);

    ChartResponse getUserDashboardCategoriesPieChart(HttpServletRequest request);

    ChartResponse getUserDashboardExpensesLineChart(HttpServletRequest request);

    AnalyticsResponse getUserRecordsOverview(HttpServletRequest request, String startDate, String endDate);

    ChartResponse getUserRecordsOverviewLineChart(
            HttpServletRequest request, String startDate, String endDate, String recordType
    );
}
