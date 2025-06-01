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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budget")
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping("")
    public ResponseEntity<List<BudgetResponse>> getBudgets(HttpServletRequest request){
        return ResponseEntity.ok(budgetService.getUserBudgets(request));
    }

    @PostMapping("")
    public ResponseEntity<BudgetResponse> addBudget(@RequestBody @Valid AddBudgetRequest request){
        return ResponseEntity.ok(budgetService.addBudget(request));
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
    public ResponseEntity<BudgetResponse> getBudget(@PathVariable String id){
        return ResponseEntity.ok(budgetService.getBudget(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @RequestBody @Valid UpdateBudgetRequest request,
            @PathVariable String id){
        return ResponseEntity.ok(budgetService.updateBudget(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteBudget(@PathVariable String id){
        return ResponseEntity.ok(budgetService.deleteBudget(id));
    }
}
