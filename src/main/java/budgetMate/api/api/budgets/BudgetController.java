package budgetMate.api.api.budgets;

import budgetMate.api.api.budgets.request.AddBudgetRequest;
import budgetMate.api.api.budgets.request.UpdateBudgetRequest;
import budgetMate.api.api.budgets.response.BudgetResponse;
import budgetMate.api.api.budgets.response.BudgetsCurrentBalanceResponse;
import budgetMate.api.api.budgets.service.BudgetService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final HttpUtil httpUtil;

    @GetMapping("")
    public ResponseEntity<List<BudgetResponse>> getUserBudgets(HttpServletRequest request){
        return httpUtil.handleGet(budgetService.getUserBudgets(request));
    }

    @PostMapping("")
    public ResponseEntity<BudgetResponse> addUserBudget(
            HttpServletRequest request,
            @RequestBody @Valid AddBudgetRequest body)
    {
        return httpUtil.handleAdd(budgetService.addUserBudget(request, body));
    }

    @GetMapping("/current-balance")
    public ResponseEntity<List<BudgetsCurrentBalanceResponse>> getUserBudgetsCurrentBalance(HttpServletRequest request){
        return httpUtil.handleGet(budgetService.getUserBudgetsCurrentBalance(request));
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> getUserBudgetsReport(HttpServletRequest request){
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=budgets-report.json");

        return httpUtil.handleGet(budgetService.getUserBudgetsReport(request), headers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getUserBudget(HttpServletRequest request, @PathVariable UUID id){
        return httpUtil.handleGet(budgetService.getUserBudget(request, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateUserBudget(
            HttpServletRequest request,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateBudgetRequest body)
    {
        return httpUtil.handleUpdate(budgetService.updateUserBudget(request, body, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserBudget(HttpServletRequest request, @PathVariable UUID id){
        return httpUtil.handleUpdate(budgetService.deleteUserBudget(request, id));
    }
}
