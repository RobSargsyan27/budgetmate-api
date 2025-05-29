package com.github.RobSargsyan27.budgetMateV2.api.api.analytics.response;

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
