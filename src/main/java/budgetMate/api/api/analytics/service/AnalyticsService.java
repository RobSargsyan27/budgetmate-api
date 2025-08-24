package budgetMate.api.api.analytics.service;

import budgetMate.api.api.analytics.response.AnalyticsResponse;
import budgetMate.api.api.analytics.response.ChartResponse;
import budgetMate.api.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;

public interface AnalyticsService {
    AnalyticsResponse getUserDashboardAnalytics(CustomUserDetails user);

    ChartResponse getUserDashboardCategoriesPieChart(CustomUserDetails user);

    ChartResponse getUserDashboardExpensesLineChart(CustomUserDetails user);

    AnalyticsResponse getUserRecordsOverview(CustomUserDetails user, String startDate, String endDate);

    ChartResponse getUserRecordsOverviewLineChart(
            CustomUserDetails user, String startDate, String endDate, String recordType
    );

    Integer getUsersCount();
}
