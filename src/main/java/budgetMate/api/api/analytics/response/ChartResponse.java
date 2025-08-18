package budgetMate.api.api.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ChartResponse {
    private List<String> labels;

    private List<Double> data;

    /**
     * <h2>Parse to ChartResponse.</h2>
     * @param chartData {List<Object[]>}
     * @return {ChartResponse}
     */
    public static ChartResponse from(List<Object[]> chartData){
        final Map<LocalDateTime, Double> groupedData = new HashMap<>();

        for (Object[] item : chartData) {
            LocalDateTime paymentTime = (LocalDateTime) item[0];
            Double amount = (Double) item[1];

            LocalDateTime intervalStart = paymentTime
                    .minusDays(paymentTime.getDayOfMonth() % 5)
                    .truncatedTo(ChronoUnit.DAYS);

            groupedData.merge(intervalStart, amount, Double::sum);
        }

        final List<Map.Entry<LocalDateTime, Double>> entries = new ArrayList<>(groupedData.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        final List<String> labels = entries.stream()
                .map(entry ->
                        String.format("%s/%s", entry.getKey().getDayOfMonth(), entry.getKey().getMonthValue())
                ).toList();
        final List<Double> data = entries.stream()
                .map(Map.Entry::getValue)
                .toList();

        return ChartResponse.builder().labels(labels).data(data).build();
    }

    /**
     * <h2>Parse to ChartResponse.</h2>
     * @param labels {List<String>}
     * @param data {List<Double>}
     * @return {ChartResponse}
     */
    public static ChartResponse from(List<String> labels, List<Double> data){
        return ChartResponse.builder()
                .labels(labels)
                .data(data)
                .build();
    }
}
