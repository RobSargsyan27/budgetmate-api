package budgetMate.api.api.record;

import budgetMate.api.api.record.request.*;
import budgetMate.api.api.record.response.RecordResponse;
import budgetMate.api.api.record.service.RecordService;
import budgetMate.api.domain.RecordCategory;
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

    @GetMapping("")
    public ResponseEntity<List<RecordResponse>> getRecords(HttpServletRequest request){
        return ResponseEntity.ok(recordService.getUserRecords(request));
    }

    @PostMapping("/count")
    public ResponseEntity<Long> getRecordsCount(
            HttpServletRequest request,
            @RequestBody GetAccountFilteredRecordsRequest requestBody){
        return ResponseEntity.ok(recordService.getUserRecordsCount(request, requestBody));
    }

    @PostMapping("/{limit}/{offset}")
    public ResponseEntity<List<RecordResponse>> getPaginatedRecords(
            HttpServletRequest request,
            @RequestBody @Valid GetAccountFilteredRecordsRequest requestBody,
            @PathVariable int limit,
            @PathVariable int offset){
        return ResponseEntity.ok(recordService.getUserPaginatedRecords(request, requestBody, limit, offset));
    }

    @PostMapping("/report")
    public ResponseEntity<byte[]> getRecordsReport(
            HttpServletRequest request,
            @RequestBody @Valid GetAccountFilteredRecordsRequest requestBody){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=records-report.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(recordService.getRecordsReport(request, requestBody));
    }

    @PostMapping("/income")
    public ResponseEntity<RecordResponse> addInputRecord(
            HttpServletRequest request,
            @RequestBody @Valid AddIncomeRecordRequest body)
    {
        return ResponseEntity.ok(recordService.addIncomeRecord(request, body));
    }

    @PostMapping("/expense")
    public ResponseEntity<RecordResponse> addExpenseRecord(
            HttpServletRequest request,
            @RequestBody @Valid AddExpenseRecordRequest body)
    {
        return ResponseEntity.ok(recordService.addExpenseRecord(request, body));
    }

    @PostMapping("/transfer")
    public ResponseEntity<RecordResponse> addTransferRecord(
            HttpServletRequest request,
            @RequestBody @Valid AddTransferRecordRequest body)
    {
        return ResponseEntity.ok(recordService.addTransferRecord(request, body));
    }

    @GetMapping("/record-categories")
    public ResponseEntity<List<RecordCategory>> getRecordCategories(){
        return ResponseEntity.ok(recordService.getRecordCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordResponse> getRecord(
            HttpServletRequest request,
            @PathVariable UUID id)
    {
        return ResponseEntity.ok(recordService.getUserRecord(request, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecordResponse> updateRecord(
            HttpServletRequest request,
            @RequestBody @Valid UpdateRecordRequest body,
            @PathVariable UUID id){
        return ResponseEntity.ok(recordService.updateRecord(request, body, id));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteRecord(
            HttpServletRequest request,
            @PathVariable UUID id)
    {
        recordService.deleteRecord(request, id);
        return HttpStatus.ACCEPTED;
    }
}
