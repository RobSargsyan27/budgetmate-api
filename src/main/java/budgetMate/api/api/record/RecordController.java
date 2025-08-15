package budgetMate.api.api.record;

import budgetMate.api.api.record.request.*;
import budgetMate.api.api.record.response.RecordResponse;
import budgetMate.api.api.record.service.RecordService;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/record")
public class RecordController {
    private final RecordService recordService;
    private final HttpUtil httpUtil;

    @GetMapping("")
    public ResponseEntity<List<RecordResponse>> getRecords(HttpServletRequest request){
        return httpUtil.handleGet(recordService.getUserRecords(request));
    }

    @PostMapping("/count")
    public ResponseEntity<Long> getRecordsCount(
            HttpServletRequest request,
            @RequestBody GetAccountFilteredRecordsRequest requestBody)
    {
        return httpUtil.handleGet(recordService.getUserRecordsCount(request, requestBody));
    }

    @PostMapping("/{limit}/{offset}")
    public ResponseEntity<List<RecordResponse>> getPaginatedRecords(
            HttpServletRequest request,
            @RequestBody @Valid GetAccountFilteredRecordsRequest requestBody,
            @PathVariable int limit,
            @PathVariable int offset)
    {
        return httpUtil.handleGet(recordService.getUserPaginatedRecords(request, requestBody, limit, offset));
    }

    @PostMapping("/report")
    public ResponseEntity<byte[]> getRecordsReport(
            HttpServletRequest request,
            @RequestBody @Valid GetAccountFilteredRecordsRequest requestBody)
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=records-report.json");

        return httpUtil.handleGet(recordService.getRecordsReport(request, requestBody), headers);
    }

    @PostMapping("/income")
    public ResponseEntity<RecordResponse> addInputRecord(
            HttpServletRequest request,
            @RequestBody @Valid AddIncomeRecordRequest body)
    {
        return httpUtil.handleAdd(recordService.addIncomeRecord(request, body));
    }

    @PostMapping("/expense")
    public ResponseEntity<RecordResponse> addExpenseRecord(
            HttpServletRequest request,
            @RequestBody @Valid AddExpenseRecordRequest body)
    {
        return httpUtil.handleAdd(recordService.addExpenseRecord(request, body));
    }

    @PostMapping("/transfer")
    public ResponseEntity<RecordResponse> addTransferRecord(
            HttpServletRequest request,
            @RequestBody @Valid AddTransferRecordRequest body)
    {
        return httpUtil.handleAdd(recordService.addTransferRecord(request, body));
    }

    @GetMapping("/record-categories")
    public ResponseEntity<List<RecordCategory>> getRecordCategories(){
        return httpUtil.handleGet(recordService.getRecordCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordResponse> getRecord(
            HttpServletRequest request,
            @PathVariable UUID id)
    {
        return httpUtil.handleGet(recordService.getUserRecord(request, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecordResponse> updateRecord(
            HttpServletRequest request,
            @RequestBody @Valid UpdateRecordRequest body,
            @PathVariable UUID id){
        return httpUtil.handleUpdate(recordService.updateRecord(request, body, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            HttpServletRequest request,
            @PathVariable UUID id)
    {
        return httpUtil.handleDelete(recordService.deleteRecord(request, id));
    }
}
