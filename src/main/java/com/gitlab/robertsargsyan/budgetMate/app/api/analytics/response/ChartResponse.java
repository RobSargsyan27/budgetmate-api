package com.gitlab.robertsargsyan.budgetMate.app.api.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class ChartResponse {
    private List<String> labels;

    private List<Double> data;
}
