package com.github.RobSargsyan27.budgetMateV2.app.api.budget.service;

import com.github.RobSargsyan27.budgetMateV2.app.api.budget.request.AddBudgetRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.budget.request.UpdateBudgetRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.budget.response.BudgetResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.budget.response.BudgetsCurrentBalanceResponse;
import com.github.RobSargsyan27.budgetMateV2.app.domain.Budget;
import com.github.RobSargsyan27.budgetMateV2.app.domain.RecordCategory;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import com.github.RobSargsyan27.budgetMateV2.app.lib.BudgetLib;
import com.github.RobSargsyan27.budgetMateV2.app.lib.UserLib;
import com.github.RobSargsyan27.budgetMateV2.app.repository.budgetRepository.BudgetRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.recordCategoryRepository.RecordCategoryRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.recordRepository.RecordRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.userRepository.UserRepository;
import com.github.RobSargsyan27.budgetMateV2.app.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final RecordCategoryRepository recordCategoryRepository;
    private final RecordRepository recordRepository;
    private final UserLib userLib;
    private final BudgetLib budgetLib;
    private final FileUtil fileUtil;

    public BudgetResponse getBudget(String id){
        final Budget budget = budgetRepository.getBudgetById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Budget not found!"));

        return BudgetResponse.builder()
                .id(budget.getId())
                .recordCategories(budget.getRecordCategories())
                .amount(budget.getAmount())
                .userId(budget.getUser().getId())
                .name(budget.getName())
                .build();
    }

    public List<BudgetResponse> getUserBudgets(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Budget> budgets = budgetRepository.getBudgetsByUser(user);

        return budgets.stream().map((budget ->
                BudgetResponse.builder()
                        .id(budget.getId())
                        .recordCategories(budget.getRecordCategories())
                        .amount(budget.getAmount())
                        .name(budget.getName())
                        .userId(budget.getUser().getId())
                        .build()))
                .toList();
    }

    public List<BudgetsCurrentBalanceResponse> getUserBudgetsCurrentBalance(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Budget> budgets = budgetRepository.getBudgetsByUser(user);

        return budgets.stream().map(((budget) -> {
             final BigDecimal spendOverTime = recordRepository
                     .findSumOfUserRecordsAmountsByCategories(user, budget.getRecordCategories());
             final double currentBalance = spendOverTime == null
                     ? budget.getAmount()
                     : budget.getAmount() - spendOverTime.doubleValue();

             return BudgetsCurrentBalanceResponse.builder()
                     .currentBalance(currentBalance)
                     .id(budget.getId())
                     .build();
        })).toList();

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

    public BudgetResponse addBudget(AddBudgetRequest request){
        if(request.getBudgetCategories() == null || request.getBudgetCategories().isEmpty()){
            throw new IllegalArgumentException("Budget should be associated with at least one record category.");
        }

        final User user = userRepository.findUserById(UUID.fromString(request.getUserId()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        final List<RecordCategory> recordCategories = request.getBudgetCategories()
                .stream()
                .map((recordCategoryName) ->
                        recordCategoryRepository.getRecordCategoryByName(recordCategoryName).getLast())
                .toList();

        final Budget budget = new Budget();
        budget.setUser(user);
        budget.setAmount(request.getAmount());
        budget.setName(request.getName());
        budget.setRecordCategories(recordCategories);

        budgetRepository.save(budget);

        return BudgetResponse.builder()
                .id(budget.getId())
                .recordCategories(budget.getRecordCategories())
                .amount(budget.getAmount())
                .userId(budget.getUser().getId())
                .name(budget.getName())
                .build();
    }

    @Transactional
    public BudgetResponse updateBudget(UpdateBudgetRequest request, String id){
        final UUID budgetId = UUID.fromString(id);

        final Budget budget = budgetRepository.getBudgetById(budgetId)
                .orElseThrow(() -> new IllegalStateException("Budget is not found."));

        budget.setName(request.getName());
        budget.setAmount(request.getAmount());

        if(request.getRecordCategories() != null && !request.getRecordCategories().isEmpty()){
            List<RecordCategory> recordCategories = request.getRecordCategories()
                    .stream()
                    .map((recordCategoryName) ->
                            recordCategoryRepository.getRecordCategoryByName(recordCategoryName).getLast())
                    .toList();

            budget.getRecordCategories().clear();
            budget.getRecordCategories().addAll(recordCategories);
        }

        final Budget updatedBudget = budgetRepository.save(budget);

        return BudgetResponse.builder()
                .id(updatedBudget.getId())
                .recordCategories(updatedBudget.getRecordCategories())
                .amount(updatedBudget.getAmount())
                .userId(updatedBudget.getUser().getId())
                .name(updatedBudget.getName())
                .build();
    }

    @Transactional
    public Integer deleteBudget(String id){
        return budgetRepository.deleteBudgetById(UUID.fromString(id));
    }

}
