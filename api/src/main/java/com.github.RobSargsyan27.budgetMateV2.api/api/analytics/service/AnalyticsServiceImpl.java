package com.github.RobSargsyan27.budgetMateV2.api.api.analytics.service;

import com.github.RobSargsyan27.budgetMateV2.api.api.analytics.response.AnalyticsResponse;
import com.github.RobSargsyan27.budgetMateV2.api.api.analytics.response.ChartResponse;
import com.github.RobSargsyan27.budgetMateV2.api.domain.RecordCategory;
import com.github.RobSargsyan27.budgetMateV2.api.domain.User;
import com.github.RobSargsyan27.budgetMateV2.api.domain.enums.RecordType;
import com.github.RobSargsyan27.budgetMateV2.api.lib.AnalyticsLib;
import com.github.RobSargsyan27.budgetMateV2.api.lib.UserLib;
import com.github.RobSargsyan27.budgetMateV2.api.repository.RecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final RecordRepository recordRepository;
    private final UserLib userLib;
    private final AnalyticsLib analyticsLib;

    @Override
    public AnalyticsResponse getDashboardAnalytics(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        Year currentYear = Year.now();
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime startOfNextMonth = currentMonth.plusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime startOfYear = currentYear.atDay(1).atStartOfDay();
        LocalDateTime startOfNextYear = currentYear.plusYears(1).atDay(1).atStartOfDay();

        final BigDecimal monthlyEarnings = recordRepository
                .userIntervalAnalytics(user, RecordType.INCOME, startOfMonth, startOfNextMonth);
        final BigDecimal monthlyExpenses = recordRepository
                .userIntervalAnalytics(user, RecordType.EXPENSE, startOfMonth, startOfNextMonth);
        final BigDecimal annualEarnings = recordRepository
                .userIntervalAnalytics(user, RecordType.INCOME, startOfYear, startOfNextYear);

        final AnalyticsResponse analyticsResponse = new AnalyticsResponse();
        analyticsResponse.setAnnualEarnings(annualEarnings);
        analyticsResponse.setMonthlyEarnings(monthlyEarnings);
        analyticsResponse.setMonthlyExpenses(monthlyExpenses);

        return analyticsResponse;
    }

    @Override
    public ChartResponse getDashboardCategoriesPieChart(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime startOfNextMonth = currentMonth.plusMonths(1).atDay(1).atStartOfDay();
        Pageable pageable = PageRequest.of(0, 5);

        final List<Object[]> categoriesPieChartData = recordRepository
                .findCurrentMonthTopCategoriesByType(user, RecordType.EXPENSE, startOfMonth, startOfNextMonth, pageable);

        final List<String> categories = categoriesPieChartData.stream()
                .map((item) -> ((RecordCategory) item[0]).getName())
                .toList();

        final List<Double> totalAmounts =  categoriesPieChartData.stream()
                .map((item) -> (Double) item[1])
                .toList();

        return ChartResponse.builder()
                .labels(categories)
                .data(totalAmounts)
                .build();
    }

    @Override
    public ChartResponse getDashboardExpensesLineChart(HttpServletRequest request) {
        final User user = userLib.fetchRequestUser(request);

        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();

        final List<Object[]> expensesLineChartData = recordRepository
                .findExpenseGroupedByDate(user, RecordType.EXPENSE, startDate, endDate);

        return analyticsLib.prepareLineChartResponse(expensesLineChartData);
    }

    @Override
    public AnalyticsResponse getUserRecordsOverview(String startDate, String endDate, HttpServletRequest request){
        LocalDateTime _startDate = LocalDateTime.parse(startDate.substring(0, startDate.length() - 1));
        LocalDateTime _endDate = LocalDateTime.parse(endDate.substring(0, startDate.length() - 1));
        User user = userLib.fetchRequestUser(request);

        final BigDecimal monthlyEarnings = recordRepository
                .userIntervalAnalytics(user, RecordType.INCOME, _startDate, _endDate);
        final BigDecimal monthlyExpenses = recordRepository
                .userIntervalAnalytics(user, RecordType.EXPENSE, _startDate, _endDate);

        AnalyticsResponse analyticsResponse = new AnalyticsResponse();
        analyticsResponse.setMonthlyExpenses(monthlyExpenses);
        analyticsResponse.setMonthlyEarnings(monthlyEarnings);

        return analyticsResponse;
    }

    @Override
    public ChartResponse getUserRecordsOverviewLineChart(
            String startDate, String endDate, String recordType, HttpServletRequest request
    ){
        LocalDateTime _startDate = LocalDateTime.parse(startDate.substring(0, startDate.length() - 1));
        LocalDateTime _endDate = LocalDateTime.parse(endDate.substring(0, startDate.length() - 1));
        RecordType _recordType = RecordType.fromString(recordType.toUpperCase());
        User user = userLib.fetchRequestUser(request);

        final List<Object[]> expensesLineChartData = recordRepository
                .findExpenseGroupedByDate(user, _recordType, _startDate, _endDate);

        return analyticsLib.prepareLineChartResponse(expensesLineChartData);
    }


}
