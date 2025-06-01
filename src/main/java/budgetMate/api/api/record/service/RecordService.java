package budgetMate.api.api.record.service;

import budgetMate.api.api.record.request.GetAccountFilteredRecordsRequest;
import budgetMate.api.api.record.request.AddIncomeRecordRequest;
import budgetMate.api.api.record.request.AddExpenseRecordRequest;
import budgetMate.api.api.record.request.AddTransferRecordRequest;
import budgetMate.api.api.record.request.UpdateRecordRequest;
import budgetMate.api.api.record.response.RecordResponse;
import budgetMate.api.domain.RecordCategory;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface RecordService {
    RecordResponse getUserRecord(String id);

    List<RecordResponse> getUserRecords(HttpServletRequest request);

    Long getUserRecordsCount(HttpServletRequest request, GetAccountFilteredRecordsRequest requestBody);

    byte[] getRecordsReport(HttpServletRequest request, GetAccountFilteredRecordsRequest requestBody);

    List<RecordResponse> getUserPaginatedRecords(HttpServletRequest request, GetAccountFilteredRecordsRequest requestBody, int limit, int offset);

    RecordResponse addIncomeRecord(AddIncomeRecordRequest request);

    RecordResponse addExpenseRecord(AddExpenseRecordRequest request);

    RecordResponse addTransferRecord(AddTransferRecordRequest request);

    RecordResponse updateRecord(UpdateRecordRequest request, String id);

    Integer deleteRecord(String id);

    List<RecordCategory> getRecordCategories();
}
