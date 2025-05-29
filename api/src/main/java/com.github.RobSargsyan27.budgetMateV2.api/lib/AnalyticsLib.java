package com.github.RobSargsyan27.budgetMateV2.api.lib;

import com.github.RobSargsyan27.budgetMateV2.app.api.analytics.response.ChartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AnalyticsLib {

    public ChartResponse prepareLineChartResponse(List<Object[]> chartData){
        Map<LocalDateTime, Double> groupedData = new HashMap<>();

        for (Object[] item : chartData) {
            LocalDateTime paymentTime = (LocalDateTime) item[0];
            Double amount = (Double) item[1];

            LocalDateTime intervalStart = paymentTime
                    .minusDays(paymentTime.getDayOfMonth() % 5)
                    .truncatedTo(ChronoUnit.DAYS);

            groupedData.merge(intervalStart, amount, Double::sum);
        }

        List<Map.Entry<LocalDateTime, Double>> entries = new ArrayList<>(groupedData.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        List<String> labels = entries.stream()
                .map(entry ->
                        String.format("%s/%s", entry.getKey().getDayOfMonth(), entry.getKey().getMonthValue())
                ).toList();
        List<Double> data = entries.stream()
                .map(Map.Entry::getValue)
                .toList();

        return ChartResponse.builder().labels(labels).data(data).build();
    }
}
