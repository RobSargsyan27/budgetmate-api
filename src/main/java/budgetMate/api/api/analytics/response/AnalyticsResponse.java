package budgetMate.api.api.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {
    private Double monthlyEarnings;

    private Double annualEarnings;

    private Double monthlyExpenses;

    public void setAnnualEarnings(BigDecimal annualEarnings) {
        this.annualEarnings = annualEarnings != null ? annualEarnings.doubleValue() : 0;
    }

    public void setMonthlyEarnings(BigDecimal monthlyEarnings) {
        this.monthlyEarnings = monthlyEarnings != null ? monthlyEarnings.doubleValue() : 0;
    }

    public void setMonthlyExpenses(BigDecimal monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses != null ? monthlyExpenses.doubleValue() : 0;
    }

    /**
     * <h2>Parse to AnalyticsResponse.</h2>
     * @param annualEarnings {BigDecimal}
     * @param monthlyEarnings {BigDecimal}
     * @param monthlyExpenses {BigDecimal}
     * @return {AnalyticsResponse}
     */
    public static AnalyticsResponse from(BigDecimal annualEarnings, BigDecimal monthlyEarnings, BigDecimal monthlyExpenses){
        final AnalyticsResponse response = new AnalyticsResponse();
        response.setAnnualEarnings(annualEarnings);
        response.setMonthlyEarnings(monthlyEarnings);
        response.setMonthlyExpenses(monthlyExpenses);

        return response;
    }

    /**
     * <h2>Parse to AnalyticsResponse.</h2>
     * @param monthlyEarnings {BigDecimal}
     * @param monthlyExpenses {BigDecimal}
     * @return {AnalyticsResponse}
     */
    public static AnalyticsResponse from(BigDecimal monthlyEarnings, BigDecimal monthlyExpenses){
        final AnalyticsResponse response = new AnalyticsResponse();
        response.setMonthlyEarnings(monthlyEarnings);
        response.setMonthlyExpenses(monthlyExpenses);

        return response;
    }
}
