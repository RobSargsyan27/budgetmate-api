package budgetMate.api.api.budget;

import budgetMate.api.api.budget.request.AddBudgetRequest;
import budgetMate.api.api.budget.request.UpdateBudgetRequest;
import budgetMate.api.api.budget.response.BudgetResponse;
import budgetMate.api.api.budget.response.BudgetsCurrentBalanceResponse;
import budgetMate.api.api.budget.service.BudgetService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/budget")
public class BudgetController {
    private final BudgetService budgetService;
    private final HttpUtil httpUtil;

    @GetMapping("")
    public ResponseEntity<List<BudgetResponse>> getBudgets(HttpServletRequest request){
        return httpUtil.handleGet(budgetService.getUserBudgets(request));
    }

    @PostMapping("")
    public ResponseEntity<BudgetResponse> addBudget(
            HttpServletRequest request,
            @RequestBody @Valid AddBudgetRequest body)
    {
        return httpUtil.handleAdd(budgetService.addBudget(request, body));
    }

    @GetMapping("/current-balance")
    public ResponseEntity<List<BudgetsCurrentBalanceResponse>> getBudgetsCurrentBalance(HttpServletRequest request){
        return httpUtil.handleGet(budgetService.getUserBudgetsCurrentBalance(request));
    }

    @PostMapping("/report")
    public ResponseEntity<byte[]> getBudgetsReport(HttpServletRequest request){
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=budgets-report.json");

        return httpUtil.handleGet(budgetService.getBudgetsReport(request), headers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudget(HttpServletRequest request, @PathVariable UUID id){
        return httpUtil.handleGet(budgetService.getBudget(request, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            HttpServletRequest request,
            @RequestBody @Valid UpdateBudgetRequest body,
            @PathVariable UUID id)
    {
        return httpUtil.handleUpdate(budgetService.updateBudget(request, body, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(HttpServletRequest request, @PathVariable UUID id){
        return httpUtil.handleDelete(budgetService.deleteBudget(request, id));
    }
}
