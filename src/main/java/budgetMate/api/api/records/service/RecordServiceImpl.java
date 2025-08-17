package budgetMate.api.api.records.service;

import budgetMate.api.api.records.request.SearchRecordsRequest;
import budgetMate.api.api.records.request.AddRecordRequest;
import budgetMate.api.api.records.request.UpdateRecordRequest;
import budgetMate.api.api.records.response.RecordResponse;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.RecordType;
import budgetMate.api.lib.FetchLib;
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
    private final FetchLib fetchLib;

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

    /**
     * <h2>Add user record.</h2>
     * @param request {HttpServletRequest}
     * @param body {AddRecordRequest}
     * @return {RecordResponse}
     */
    @Override
    @Transactional
    public RecordResponse addUserRecord(HttpServletRequest request, AddRecordRequest body) {
        final User user = userLib.fetchRequestUser(request);

        final Record record = recordLib.buildRecord(user, body);

        switch (record.getType()){
            case INCOME:
                accountRepository.updateAccountCurrentBalance(body.getReceivingAccountId(), body.getAmount());
                break;
            case EXPENSE:
                accountRepository.updateAccountCurrentBalance(body.getWithdrawalAccountId(), -body.getAmount());
                break;
            case TRANSFER:
                accountRepository.updateAccountCurrentBalance(body.getWithdrawalAccountId(), -body.getAmount());
                accountRepository.updateAccountCurrentBalance(body.getReceivingAccountId(), body.getAmount());
        }

        recordRepository.save(record);

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

        final Record record = fetchLib.fetchResource(recordRepository.getUserRecordById(user, id), "Record");

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

        final Record record = fetchLib.fetchResource(recordRepository.getUserRecordById(user, id), "Record");

        record.setNote(body.getNote());
        record.setPaymentTime(body.getPaymentTime());
        if(body.getCategory() != null){
            final RecordCategory recordCategory = fetchLib.fetchResource(recordCategoryRepository.getRecordCategoryByName(body.getCategory()), "Record Category");
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

        final Record record = fetchLib.fetchResource(recordRepository.getUserRecordById(user, id), "Record");

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
