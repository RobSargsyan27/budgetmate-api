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
import java.util.UUID;

public interface RecordService {
    RecordResponse getUserRecord(HttpServletRequest request, UUID id);

    List<RecordResponse> getUserRecords(HttpServletRequest request);

    Long getUserRecordsCount(HttpServletRequest request, GetAccountFilteredRecordsRequest body);

    byte[] getRecordsReport(HttpServletRequest request, GetAccountFilteredRecordsRequest body);

    List<RecordResponse> getUserPaginatedRecords(HttpServletRequest request, GetAccountFilteredRecordsRequest body, int limit, int offset);

    RecordResponse addIncomeRecord(HttpServletRequest request, AddIncomeRecordRequest body);

    RecordResponse addExpenseRecord(HttpServletRequest request, AddExpenseRecordRequest body);

    RecordResponse addTransferRecord(HttpServletRequest request, AddTransferRecordRequest body);

    RecordResponse updateRecord(HttpServletRequest request, UpdateRecordRequest body, UUID id);

    Integer deleteRecord(HttpServletRequest request, UUID id);

    List<RecordCategory> getRecordCategories();
}
