package budgetMate.api.api.analytics.service;

import budgetMate.api.api.analytics.response.AnalyticsResponse;
import budgetMate.api.api.analytics.response.ChartResponse;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.RecordType;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.RecordRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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
    private final UserRepository userRepository;

    /**
     * <h2>Get user dashboard analytics.</h2>
     * @param request {HttpServletRequest}
     * @return {AnalyticsResponse}
     */
    @Override
    @Transactional
    public AnalyticsResponse getUserDashboardAnalytics(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        Year currentYear = Year.now();
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime startOfNextMonth = currentMonth.plusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime startOfYear = currentYear.atDay(1).atStartOfDay();
        LocalDateTime startOfNextYear = currentYear.plusYears(1).atDay(1).atStartOfDay();

        final BigDecimal annualEarnings = recordRepository
                .getUserRecordsIntervalSum(user, RecordType.INCOME, startOfYear, startOfNextYear);
        final BigDecimal monthlyEarnings = recordRepository
                .getUserRecordsIntervalSum(user, RecordType.INCOME, startOfMonth, startOfNextMonth);
        final BigDecimal monthlyExpenses = recordRepository
                .getUserRecordsIntervalSum(user, RecordType.EXPENSE, startOfMonth, startOfNextMonth);

        return AnalyticsResponse.from(annualEarnings, monthlyEarnings, monthlyExpenses);
    }

    /**
     * <h2>Get user dashboard categories pie chart.</h2>
     * @param request {HttpServletRequest}
     * @return {ChartResponse}
     */
    @Override
    @Transactional
    public ChartResponse getUserDashboardCategoriesPieChart(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDateTime startOfMonth = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime startOfNextMonth = lastMonth.plusMonths(1).atDay(1).atStartOfDay();
        Pageable pageable = PageRequest.of(0, 5);

        final List<Object[]> categoriesPieChartData = recordRepository
                .getUserCurrentMonthTopCategoriesByType(user, RecordType.EXPENSE, startOfMonth, startOfNextMonth, pageable);

        final List<String> categories = categoriesPieChartData.stream()
                .map((item) -> ((RecordCategory) item[0]).getName())
                .toList();

        final List<Double> totalAmounts =  categoriesPieChartData.stream()
                .map((item) -> (Double) item[1])
                .toList();

        return ChartResponse.from(categories, totalAmounts);
    }

    /**
     * <h2>Get user dashboard expenses line chart.</h2>
     * @param request {HttpServletRequest}
     * @return {ChartResponse}
     */
    @Override
    @Transactional
    public ChartResponse getUserDashboardExpensesLineChart(HttpServletRequest request) {
        final User user = userLib.fetchRequestUser(request);

        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();

        final List<Object[]> lineChartData = recordRepository
                .getUserExpenseRecordsIntervalSum(user, RecordType.EXPENSE, startDate, endDate);

        return ChartResponse.from(lineChartData);
    }

    /**
     * <h2>Get user records overview.</h2>
     * @param request {HttpServletRequest}
     * @param startDate {String}
     * @param endDate {String}
     * @return {AnalyticsResponse}
     */
    @Override
    @Transactional
    public AnalyticsResponse getUserRecordsOverview(HttpServletRequest request, String startDate, String endDate){
        LocalDateTime _startDate = LocalDateTime.parse(startDate.substring(0, startDate.length() - 1));
        LocalDateTime _endDate = LocalDateTime.parse(endDate.substring(0, startDate.length() - 1));
        User user = userLib.fetchRequestUser(request);

        final BigDecimal monthlyEarnings = recordRepository
                .getUserRecordsIntervalSum(user, RecordType.INCOME, _startDate, _endDate);
        final BigDecimal monthlyExpenses = recordRepository
                .getUserRecordsIntervalSum(user, RecordType.EXPENSE, _startDate, _endDate);

        return AnalyticsResponse.from(monthlyEarnings, monthlyExpenses);
    }

    /**
     * <h2>Get user records overview line chart.</h2>
     * @param request {HttpServletRequest}
     * @param startDate {String}
     * @param endDate {String}
     * @param recordType {String}
     * @return {ChartResponse}
     */
    @Override
    @Transactional
    public ChartResponse getUserRecordsOverviewLineChart(
            HttpServletRequest request, String startDate, String endDate, String recordType
    ){
        LocalDateTime _startDate = LocalDateTime.parse(startDate.substring(0, startDate.length() - 1));
        LocalDateTime _endDate = LocalDateTime.parse(endDate.substring(0, startDate.length() - 1));
        RecordType _recordType = RecordType.fromString(recordType.toUpperCase());
        User user = userLib.fetchRequestUser(request);

        final List<Object[]> lineChartData = recordRepository
                .getUserExpenseRecordsIntervalSum(user, _recordType, _startDate, _endDate);

        return ChartResponse.from(lineChartData);
    }

    /**
     * <h2>Get users count.</h2>
     * @return {Integer}
     */
    @Override
    @Transactional
    public Integer getUsersCount(){
        return userRepository.countUsers();
    }
}
