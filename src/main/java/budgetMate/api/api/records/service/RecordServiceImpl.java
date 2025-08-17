package budgetMate.api.api.records.service;

import budgetMate.api.api.records.request.SearchRecordsRequest;
import budgetMate.api.api.records.request.AddRecordRequest;
import budgetMate.api.api.records.request.UpdateRecordRequest;
import budgetMate.api.api.records.response.RecordResponse;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.RecordType;
import budgetMate.api.lib.RecordLib;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.RecordCategoryRepository;
import budgetMate.api.repository.specification.RecordFilterSpecification;
import budgetMate.api.repository.RecordRepository;
import budgetMate.api.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final AccountRepository accountRepository;
    private final RecordRepository recordRepository;
    private final RecordFilterSpecification recordFilterSpecification;
    private final RecordCategoryRepository recordCategoryRepository;
    private final UserLib userLib;
    private final RecordLib recordLib;
    private final FileUtil fileUtil;

    /**
     * <h2>Search user records.</h2>
     * @param request {HttpServletRequest}
     * @param body {SearchRecordsRequest}
     * @return {List<RecordResponse>}
     */
    @Override
    @Transactional
    public List<RecordResponse> searchUserRecords(HttpServletRequest request, SearchRecordsRequest body){
        final User user = userLib.fetchRequestUser(request);
        final Integer limit = body.getLimit();
        final Integer offset = body.getOffset();

        Specification<Record> specification = recordFilterSpecification.buildRecordSpecification(user, body);
        Pageable pageable = PageRequest.of((offset / limit), limit, Sort.by("paymentTime").descending());

        final List<Record> filteredRecords = recordRepository.findAll(specification, pageable).stream().toList();

        return RecordResponse.from(filteredRecords);
    }

    /**
     * <h2>Count user records.</h2>
     * @param request {HttpServletRequest}
     * @param body {SearchRecordsRequest}
     * @return {Long}
     */
    @Override
    @Transactional
    public Long countUserRecords(HttpServletRequest request, SearchRecordsRequest body){
        final User user = userLib.fetchRequestUser(request);

        Specification<Record> specification = recordFilterSpecification.buildRecordSpecification(user, body);

        return recordRepository.count(specification);
    }

    /**
     * <h2>Get user records.</h2>
     * @param request {HttpServletRequest}
     * @return {List<RecordResponse>}
     */
    @Override
    @Transactional
    public List<RecordResponse> getUserRecords(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Record> records = recordRepository.getUserRecords(user);

        return RecordResponse.from(records);
    }

    @Override
    @Transactional
    public RecordResponse addUserRecord(HttpServletRequest request, AddRecordRequest body) {
        final User user = userLib.fetchRequestUser(request);

        final LocalDateTime paymentTime = body.getPaymentTime() == null ? LocalDateTime.now() : body.getPaymentTime();
        final Account receivingAccount = accountRepository.getUserAccountById(user, body.getReceivingAccountId())
                .orElseThrow(() -> new IllegalStateException("Receiving account not found!"));
        final RecordCategory recordCategory = recordCategoryRepository.getRecordCategoryByNames(List.of(body.getCategory()))
                .getFirst();

        final Record record = Record.builder()
                .amount(body.getAmount())
                .user(user)
                .paymentTime(paymentTime)
                .category(recordCategory)
                .type(RecordType.INCOME)
                .note(body.getNote())
                .currency(receivingAccount.getCurrency())
                .receivingAccount(receivingAccount)
                .build();
        recordRepository.save(record);
        accountRepository.updateAccountCurrentBalance(body.getReceivingAccountId(), body.getAmount());

        return RecordResponse.from(record);
    }

    /**
     * <h2>Get user records report.</h2>
     * @param request {HttpServletRequest}
     * @param body {SearchRecordsRequest}
     * @return {byte[]}
     */
    @Override
    @Transactional
    public byte[] getUserRecordsReport(HttpServletRequest request, SearchRecordsRequest body) {
        final User user = userLib.fetchRequestUser(request);

        Specification<Record> specification = recordFilterSpecification.buildRecordSpecification(user, body);
        final List<Record> records = recordRepository.findAll(specification);

        final File file = recordLib.generateRecordsReport(records);

        try {
            byte[] result = Files.readAllBytes(file.toPath());
            fileUtil.fileCleanUp(file);
            return result;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * <h2>Get user record.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @return {RecordResponse}
     */
    @Override
    @Transactional
    public RecordResponse getUserRecord(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Record record = recordRepository.getUserRecordById(user, id).
                orElseThrow(() -> new IllegalStateException("Record is not found!"));

        return RecordResponse.from(record);
    }

    /**
     * <h2>Update user record.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @param body {UpdateRecordRequest}
     * @return {RecordResponse}
     */
    @Override
    @Transactional
    public RecordResponse updateUserRecord(HttpServletRequest request, UUID id, UpdateRecordRequest body){
        final User user = userLib.fetchRequestUser(request);

        final Record record = recordRepository.getUserRecordById(user, id)
                .orElseThrow(() -> new IllegalStateException("Record is not found!"));

        record.setNote(body.getNote());
        record.setPaymentTime(body.getPaymentTime());
        if(body.getCategory() != null){
            final RecordCategory recordCategory = recordCategoryRepository.getRecordCategoryByName(body.getCategory())
                    .orElseThrow(() -> new IllegalStateException("Record category is not found!"));
            record.setCategory(recordCategory);
        }

        final Record updatedRecord = recordRepository.save(record);

        return RecordResponse.from(updatedRecord);
    }

    /**
     * <h2>Delete user record.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @return {Void}
     */
    @Override
    @Transactional
    public Void deleteUserRecord(HttpServletRequest request, UUID id) {
        final User user = userLib.fetchRequestUser(request);

        final Record record = recordRepository.getUserRecordById(user, id)
                .orElseThrow(() -> new IllegalStateException("Record is not found!"));

        switch (record.getType()){
            case RecordType.INCOME:
                accountRepository.updateAccountCurrentBalance(record.getReceivingAccount().getId(), -record.getAmount());
                break;
            case RecordType.EXPENSE:
                accountRepository.updateAccountCurrentBalance(record.getWithdrawalAccount().getId(), record.getAmount());
                break;
            case RecordType.TRANSFER:
                accountRepository.updateAccountCurrentBalance(record.getReceivingAccount().getId(), -record.getAmount());
                accountRepository.updateAccountCurrentBalance(record.getWithdrawalAccount().getId(), record.getAmount());
        }

        recordRepository.deleteUserRecordById(user, id);
        return null;
    }
}
