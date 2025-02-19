package com.github.RobSargsyan27.budgetMateV2.app.api.analytics.service;

import com.github.RobSargsyan27.budgetMateV2.app.api.analytics.response.AnalyticsResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.analytics.response.ChartResponse;
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
