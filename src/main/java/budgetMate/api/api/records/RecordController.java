package budgetMate.api.api.records;

import budgetMate.api.api.records.request.*;
import budgetMate.api.api.records.response.RecordResponse;
import budgetMate.api.api.records.service.RecordService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/records")
public class RecordController {
    private final RecordService recordService;
    private final HttpUtil httpUtil;

    @PostMapping("/search")
    public ResponseEntity<List<RecordResponse>> searchUserRecords(
            HttpServletRequest request,
            @RequestBody @Valid SearchRecordsRequest body)
    {
        return httpUtil.handleGet(recordService.searchUserRecords(request, body));
    }

    @PostMapping("/count")
    public ResponseEntity<Long> countUserRecords(
            HttpServletRequest request,
            @RequestBody SearchRecordsRequest body)
    {
        return httpUtil.handleGet(recordService.countUserRecords(request, body));
    }

    @GetMapping("")
    public ResponseEntity<List<RecordResponse>> getUserRecords(HttpServletRequest request){
        return httpUtil.handleGet(recordService.getUserRecords(request));
    }

    @PostMapping("")
    public ResponseEntity<RecordResponse> addUserRecord(
            HttpServletRequest request,
            @RequestBody @Valid AddRecordRequest body)
    {
        return httpUtil.handleAdd(recordService.addUserRecord(request, body));
    }

    @PostMapping("/report")
    public ResponseEntity<byte[]> getUserRecordsReport(
            HttpServletRequest request,
            @RequestBody @Valid SearchRecordsRequest body)
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=records-report.json");

        return httpUtil.handleGet(recordService.getUserRecordsReport(request, body), headers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordResponse> getUserRecord(
            HttpServletRequest request,
            @PathVariable UUID id)
    {
        return httpUtil.handleGet(recordService.getUserRecord(request, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecordResponse> updateUserRecord(
            HttpServletRequest request,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateRecordRequest body){
        return httpUtil.handleUpdate(recordService.updateUserRecord(request, id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRecord(
            HttpServletRequest request,
            @PathVariable UUID id)
    {
        return httpUtil.handleUpdate(recordService.deleteUserRecord(request, id));
    }
}
