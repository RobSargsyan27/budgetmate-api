package budgetMate.api.lib;

import budgetMate.api.api.records.response.RecordReportResponse;
import budgetMate.api.domain.Record;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class RecordLib {
    public File generateRecordsReport(List<Record> records) {
        Gson gson = new Gson();
        File file = new File("records-report.json");

        List<RecordReportResponse> reportRecords = RecordReportResponse.from(records);
        String jsonReport = gson.toJson(reportRecords);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonReport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}
