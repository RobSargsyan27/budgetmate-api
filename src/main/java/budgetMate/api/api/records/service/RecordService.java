package budgetMate.api.api.records.service;

import budgetMate.api.api.records.request.SearchRecordsRequest;
import budgetMate.api.api.records.request.AddRecordRequest;
import budgetMate.api.api.records.request.UpdateRecordRequest;
import budgetMate.api.api.records.response.RecordResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface RecordService {
    List<RecordResponse> searchUserRecords(HttpServletRequest request, SearchRecordsRequest body);

    Long countUserRecords(HttpServletRequest request, SearchRecordsRequest body);

    List<RecordResponse> getUserRecords(HttpServletRequest request);

    RecordResponse addUserRecord(HttpServletRequest request, AddRecordRequest body);

    byte[] getUserRecordsReport(HttpServletRequest request, SearchRecordsRequest body);

    RecordResponse getUserRecord(HttpServletRequest request, UUID id);

    RecordResponse updateUserRecord(HttpServletRequest request, UUID id, UpdateRecordRequest body);

    Void deleteUserRecord(HttpServletRequest request, UUID id);
}
