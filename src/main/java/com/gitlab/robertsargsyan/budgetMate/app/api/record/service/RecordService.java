package com.gitlab.robertsargsyan.budgetMate.app.api.record.service;

import com.gitlab.robertsargsyan.budgetMate.app.api.record.request.*;
import com.gitlab.robertsargsyan.budgetMate.app.api.record.response.RecordResponse;
import com.gitlab.robertsargsyan.budgetMate.app.domain.RecordCategory;
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
