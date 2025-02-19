package com.github.RobSargsyan27.budgetMateV2.app.api.record;

import com.github.RobSargsyan27.budgetMateV2.app.api.record.request.*;
import com.gitlab.robertsargsyan.budgetMate.app.api.record.request.*;
import com.github.RobSargsyan27.budgetMateV2.app.api.record.response.RecordResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.record.service.RecordService;
import com.github.RobSargsyan27.budgetMateV2.app.domain.RecordCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/record")
public class RecordController {
    private final RecordService recordService;

    @GetMapping("/{id}")
    public ResponseEntity<RecordResponse> getRecord(@PathVariable String id){
        return ResponseEntity.ok(recordService.getUserRecord(id));
    }

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
    public ResponseEntity<RecordResponse> addInputRecord(@RequestBody @Valid AddIncomeRecordRequest request){
        return ResponseEntity.ok(recordService.addIncomeRecord(request));
    }

    @PostMapping("/expense")
    public ResponseEntity<RecordResponse> addExpenseRecord(@RequestBody @Valid AddExpenseRecordRequest request){
        return ResponseEntity.ok(recordService.addExpenseRecord(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<RecordResponse> addTransferRecord(@RequestBody @Valid AddTransferRecordRequest request){
        return ResponseEntity.ok(recordService.addTransferRecord(request));
    }

    @PostMapping("/{id}")
    public ResponseEntity<RecordResponse> updateRecord(
            @RequestBody @Valid UpdateRecordRequest request,
            @PathVariable String id){
        return ResponseEntity.ok(recordService.updateRecord(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteRecord(@PathVariable String id){
        return ResponseEntity.ok(recordService.deleteRecord(id));
    }

    @GetMapping("/record-categories")
    public ResponseEntity<List<RecordCategory>> getRecordCategories(){
        return ResponseEntity.ok(recordService.getRecordCategories());
    }
}
