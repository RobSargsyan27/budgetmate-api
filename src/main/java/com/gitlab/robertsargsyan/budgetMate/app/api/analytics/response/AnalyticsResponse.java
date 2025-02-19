package com.gitlab.robertsargsyan.budgetMate.app.api.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {
    private double monthlyEarnings;

    private double annualEarnings;

    private double monthlyExpenses;

    public void setAnnualEarnings(BigDecimal annualEarnings) {
        this.annualEarnings = annualEarnings != null ? annualEarnings.doubleValue() : 0;
    }

    public void setMonthlyEarnings(BigDecimal monthlyEarnings) {
        this.monthlyEarnings = monthlyEarnings != null ? monthlyEarnings.doubleValue() : 0;
    }

    public void setMonthlyExpenses(BigDecimal monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses != null ? monthlyExpenses.doubleValue() : 0;
    }
}
