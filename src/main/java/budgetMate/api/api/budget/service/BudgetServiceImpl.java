package budgetMate.api.api.budget.service;

import budgetMate.api.api.budget.request.AddBudgetRequest;
import budgetMate.api.api.budget.request.UpdateBudgetRequest;
import budgetMate.api.api.budget.response.BudgetResponse;
import budgetMate.api.api.budget.response.BudgetsCurrentBalanceResponse;
import budgetMate.api.domain.Budget;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.User;
import budgetMate.api.lib.BudgetLib;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.BudgetRepository;
import budgetMate.api.repository.RecordCategoryRepository;
import budgetMate.api.repository.RecordRepository;
import budgetMate.api.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final RecordCategoryRepository recordCategoryRepository;
    private final RecordRepository recordRepository;
    private final UserLib userLib;
    private final BudgetLib budgetLib;
    private final FileUtil fileUtil;

    public BudgetResponse getBudget(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Budget budget = budgetRepository.getUserBudgetById(user, id)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found!"));

        return BudgetResponse.from(budget);
    }

    public List<BudgetResponse> getUserBudgets(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Budget> budgets = budgetRepository.getBudgetsByUser(user);

        return BudgetResponse.from(budgets);
    }

    public List<BudgetsCurrentBalanceResponse> getUserBudgetsCurrentBalance(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Budget> budgets = budgetRepository.getBudgetsByUser(user);

        final Map<UUID, Double> currentBalances = budgets.stream()
                .collect(Collectors.toMap(Budget::getId,
                        budget -> {
                            BigDecimal spendOverTime = recordRepository
                                    .findSumOfUserRecordsAmountsByCategories(user, budget.getRecordCategories());
                            return (spendOverTime == null)
                                    ? budget.getAmount()
                                    : budget.getAmount() - spendOverTime.doubleValue();
                        }
                ));

        return BudgetsCurrentBalanceResponse.from(currentBalances);
    }

    @Override
    public byte[] getBudgetsReport(HttpServletRequest request) {
        final User user = userLib.fetchRequestUser(request);
        final List<Budget> budgets = budgetRepository.getBudgetsByUser(user);

        final File file = budgetLib.generateBudgetsReport(budgets);

        try{
            byte[] result =  Files.readAllBytes(file.toPath());
            fileUtil.fileCleanUp(file);
            return result;
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    public BudgetResponse addBudget(HttpServletRequest request, AddBudgetRequest body){
        final User user = userLib.fetchRequestUser(request);

        final List<RecordCategory> recordCategories = body.getBudgetCategories().stream()
                .map((recordCategoryName) ->
                        recordCategoryRepository.getRecordCategoryByName(recordCategoryName).getLast())
                .toList();

        final Budget budget = Budget.builder()
                .user(user)
                .amount(body.getAmount())
                .name(body.getName())
                .recordCategories(recordCategories)
                .build();
        budgetRepository.save(budget);

        return BudgetResponse.from(budget);
    }

    @Transactional
    public BudgetResponse updateBudget(HttpServletRequest request, UpdateBudgetRequest body, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Budget budget = budgetRepository.getUserBudgetById(user, id)
                .orElseThrow(() -> new IllegalStateException("Budget is not found."));

        budget.setName(body.getName());
        budget.setAmount(body.getAmount());

        if(body.getRecordCategories() != null && !body.getRecordCategories().isEmpty()){
            List<RecordCategory> recordCategories = body.getRecordCategories()
                    .stream()
                    .map((recordCategoryName) ->
                            recordCategoryRepository.getRecordCategoryByName(recordCategoryName).getLast())
                    .toList();

            budget.getRecordCategories().clear();
            budget.getRecordCategories().addAll(recordCategories);
        }

        final Budget updatedBudget = budgetRepository.save(budget);

        return BudgetResponse.from(updatedBudget);
    }

    @Transactional
    public Integer deleteBudget(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        return budgetRepository.deleteUserBudgetById(user, id);
    }
}
