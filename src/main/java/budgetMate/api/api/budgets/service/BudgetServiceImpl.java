package budgetMate.api.api.budgets.service;

import budgetMate.api.api.budgets.request.AddBudgetRequest;
import budgetMate.api.api.budgets.request.UpdateBudgetRequest;
import budgetMate.api.api.budgets.response.BudgetResponse;
import budgetMate.api.api.budgets.response.BudgetsCurrentBalanceResponse;
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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final RecordCategoryRepository recordCategoryRepository;
    private final RecordRepository recordRepository;
    private final UserLib userLib;
    private final BudgetLib budgetLib;
    private final FileUtil fileUtil;

    /**
     * <h2>Get user budgets.</h2>
     * @param request {HttpServletRequest}
     * @return {List<BudgetResponse>}
     */
    @Override
    @Transactional
    public List<BudgetResponse> getUserBudgets(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Budget> budgets = budgetRepository.getUserBudgets(user);

        return BudgetResponse.from(budgets);
    }

    /**
     * <h2>Add user budget.</h2>
     * @param request {HttpServletRequest}
     * @param body {AddBudgetRequest}
     * @return {BudgetResponse}
     */
    @Override
    @Transactional
    public BudgetResponse addUserBudget(HttpServletRequest request, AddBudgetRequest body){
        final User user = userLib.fetchRequestUser(request);

        final List<RecordCategory> recordCategories =
                recordCategoryRepository.getRecordCategoryByNames(body.getRecordCategories());

        final Budget budget = Budget.builder()
                .user(user)
                .amount(body.getAmount())
                .name(body.getName())
                .recordCategories(recordCategories)
                .build();
        budgetRepository.save(budget);

        return BudgetResponse.from(budget);
    }

    /**
     * <h2>Get budget.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @return {BudgetResponse}
     */
    @Override
    @Transactional
    public BudgetResponse getUserBudget(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Budget budget = budgetRepository.getUserBudgetById(user, id)
                .orElseThrow(() -> new IllegalStateException("Budget not found!"));

        return BudgetResponse.from(budget);
    }

    /**
     * <h2>Get user budget current balance.</h2>
     * @param request {HttpServletRequest}
     * @return {List<BudgetsCurrentBalanceResponse>}
     */
    @Override
    @Transactional
    public List<BudgetsCurrentBalanceResponse> getUserBudgetsCurrentBalance(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Budget> budgets = budgetRepository.getUserBudgets(user);

        final Map<UUID, Double> currentBalances = budgets.stream()
                .collect(Collectors.toMap(Budget::getId,
                        budget -> {
                            BigDecimal spendOverTime = recordRepository
                                    .getUserRecordsSumByCategories(user, budget.getRecordCategories());
                            return spendOverTime == null
                                    ? budget.getAmount()
                                    : budget.getAmount() - spendOverTime.doubleValue();
                        }
                ));

        return BudgetsCurrentBalanceResponse.from(currentBalances);
    }

    /**
     * <h2>Get user budgets report.</h2>
     * @param request {HttpServletRequest}
     * @return {byte[]}
     */
    @Override
    @Transactional
    public byte[] getUserBudgetsReport(HttpServletRequest request) {
        final User user = userLib.fetchRequestUser(request);
        final List<Budget> budgets = budgetRepository.getUserBudgets(user);

        final File file = budgetLib.generateBudgetsReport(budgets);

        try {
            byte[] result = Files.readAllBytes(file.toPath());
            fileUtil.fileCleanUp(file);
            return result;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * <h2>Update user budget.</h2>
     * @param request {HttpServletRequest}
     * @param body {UpdateBudgetRequest}
     * @param id {UUID}
     * @return {BudgetResponse}
     */
    @Override
    @Transactional
    public BudgetResponse updateUserBudget(HttpServletRequest request, UpdateBudgetRequest body, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Budget budget = budgetRepository.getUserBudgetById(user, id)
                .orElseThrow(() -> new IllegalStateException("Budget is not found."));

        budget.setName(body.getName());
        budget.setAmount(body.getAmount());
        if(body.getRecordCategories() != null && !body.getRecordCategories().isEmpty()){
            List<RecordCategory> recordCategories =
                    recordCategoryRepository.getRecordCategoryByNames(body.getRecordCategories());
            budget.getRecordCategories().clear();
            budget.getRecordCategories().addAll(recordCategories);
        }

        final Budget updatedBudget = budgetRepository.save(budget);

        return BudgetResponse.from(updatedBudget);
    }

    /**
     * <h2>Delete user budget.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @return {Void}
     */
    @Override
    @Transactional
    public Void deleteUserBudget(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        budgetRepository.deleteUserBudgetById(user, id);
        return null;
    }
}
