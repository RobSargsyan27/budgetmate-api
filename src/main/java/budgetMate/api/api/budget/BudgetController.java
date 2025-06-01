package budgetMate.api.api.budget;

import budgetMate.api.api.budget.request.AddBudgetRequest;
import budgetMate.api.api.budget.request.UpdateBudgetRequest;
import budgetMate.api.api.budget.response.BudgetResponse;
import budgetMate.api.api.budget.response.BudgetsCurrentBalanceResponse;
import budgetMate.api.api.budget.service.BudgetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/budget")
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping("")
    public ResponseEntity<List<BudgetResponse>> getBudgets(HttpServletRequest request){
        return ResponseEntity.ok(budgetService.getUserBudgets(request));
    }

    @PostMapping("")
    public ResponseEntity<BudgetResponse> addBudget(
            HttpServletRequest request,
            @RequestBody @Valid AddBudgetRequest body)
    {
        return ResponseEntity.ok(budgetService.addBudget(request, body));
    }

    @GetMapping("/current-balance")
    public ResponseEntity<List<BudgetsCurrentBalanceResponse>> getBudgetsCurrentBalance(HttpServletRequest request){
        return ResponseEntity.ok(budgetService.getUserBudgetsCurrentBalance(request));
    }

    @PostMapping("/report")
    public ResponseEntity<byte[]> getBudgetsReport(HttpServletRequest request){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=budgets-report.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(budgetService.getBudgetsReport(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudget(HttpServletRequest request, @PathVariable UUID id){
        return ResponseEntity.ok(budgetService.getBudget(request, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            HttpServletRequest request,
            @RequestBody @Valid UpdateBudgetRequest body,
            @PathVariable UUID id)
    {
        return ResponseEntity.ok(budgetService.updateBudget(request, body, id));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteBudget(HttpServletRequest request, @PathVariable UUID id){
        budgetService.deleteBudget(request, id);
        return HttpStatus.ACCEPTED;
    }
}
