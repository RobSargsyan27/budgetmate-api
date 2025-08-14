package budgetMate.api.api.record.service;

import budgetMate.api.api.record.request.GetAccountFilteredRecordsRequest;
import budgetMate.api.api.record.request.AddIncomeRecordRequest;
import budgetMate.api.api.record.request.AddExpenseRecordRequest;
import budgetMate.api.api.record.request.AddTransferRecordRequest;
import budgetMate.api.api.record.request.UpdateRecordRequest;
import budgetMate.api.api.record.response.RecordResponse;
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

    @Override
    public RecordResponse getUserRecord(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Record record = recordRepository.getUserRecordById(user, id).
                orElseThrow(() -> new IllegalStateException("Record is not found!"));

        return RecordResponse.from(record);
    }


    @Override
    public List<RecordResponse> getUserRecords(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Record> records = recordRepository.getRecordsByUser(user);

        return RecordResponse.from(records);
    }

    @Override
    public Long getUserRecordsCount(HttpServletRequest request, GetAccountFilteredRecordsRequest requestBody){
        final User user = userLib.fetchRequestUser(request);

        final RecordType recordType = RecordType.fromString(requestBody.getRecordType());
        final String paymentTimeGreaterThan = requestBody.getPaymentTimeGreaterThan();
        final String paymentTimeLessThan = requestBody.getPaymentTimeLessThan();
        final Double amountGreaterThan = requestBody.getAmountGreaterThan();
        final Double amountLessThan = requestBody.getAmountLessThan();

        final LocalDateTime _paymentTimeGreaterThan = paymentTimeGreaterThan == null
                ? null : LocalDateTime.parse(paymentTimeGreaterThan.substring(0, paymentTimeGreaterThan.length() - 1));
        final LocalDateTime _paymentTimeLessThan = paymentTimeLessThan == null
                ? null : LocalDateTime.parse(paymentTimeLessThan.substring(0, paymentTimeLessThan.length() - 1));

        Specification<Record> specification = recordFilterSpecification.buildRecordSpecification(
                user, recordType, _paymentTimeGreaterThan, _paymentTimeLessThan, amountGreaterThan, amountLessThan
        );

        return recordRepository.count(specification);
    }

    @Override
    public byte[] getRecordsReport(HttpServletRequest request, GetAccountFilteredRecordsRequest requestBody) {
        final User user = userLib.fetchRequestUser(request);

        final RecordType recordType = RecordType.fromString(requestBody.getRecordType());
        final String paymentTimeGreaterThan = requestBody.getPaymentTimeGreaterThan();
        final String paymentTimeLessThan = requestBody.getPaymentTimeLessThan();
        final Double amountGreaterThan = requestBody.getAmountGreaterThan();
        final Double amountLessThan = requestBody.getAmountLessThan();

        final LocalDateTime _paymentTimeGreaterThan = paymentTimeGreaterThan == null
                ? null : LocalDateTime.parse(paymentTimeGreaterThan.substring(0, paymentTimeGreaterThan.length() - 1));
        final LocalDateTime _paymentTimeLessThan = paymentTimeLessThan == null
                ? null : LocalDateTime.parse(paymentTimeLessThan.substring(0, paymentTimeLessThan.length() - 1));

        Specification<Record> specification = recordFilterSpecification.buildRecordSpecification(
                user, recordType, _paymentTimeGreaterThan, _paymentTimeLessThan, amountGreaterThan, amountLessThan
        );

        final List<Record> records = recordRepository.findAll(specification);
        final File file = recordLib.generateRecordsReport(records);

        try{
            byte[] result =  Files.readAllBytes(file.toPath());
            fileUtil.fileCleanUp(file);
            return result;
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<RecordResponse> getUserPaginatedRecords(HttpServletRequest request, GetAccountFilteredRecordsRequest requestBody, int limit, int offset){
        final User user = userLib.fetchRequestUser(request);

        final RecordType recordType = RecordType.fromString(requestBody.getRecordType());
        final String paymentTimeGreaterThan = requestBody.getPaymentTimeGreaterThan();
        final String paymentTimeLessThan = requestBody.getPaymentTimeLessThan();
        final Double amountGreaterThan = requestBody.getAmountGreaterThan();
        final Double amountLessThan = requestBody.getAmountLessThan();

        final LocalDateTime _paymentTimeGreaterThan = paymentTimeGreaterThan == null
                ? null : LocalDateTime.parse(paymentTimeGreaterThan.substring(0, paymentTimeGreaterThan.length() - 1));
        final LocalDateTime _paymentTimeLessThan = paymentTimeLessThan == null
                ? null : LocalDateTime.parse(paymentTimeLessThan.substring(0, paymentTimeLessThan.length() - 1));

        Specification<Record> specification = recordFilterSpecification.buildRecordSpecification(
                user, recordType, _paymentTimeGreaterThan, _paymentTimeLessThan, amountGreaterThan, amountLessThan
        );
        Pageable pageable = PageRequest.of((offset / limit), limit, Sort.by("paymentTime").descending());

        final List<Record> filteredRecords = recordRepository.findAll(specification, pageable).stream().toList();

        return RecordResponse.from(filteredRecords);
    }

    @Override
    @Transactional
    public RecordResponse addIncomeRecord(HttpServletRequest request, AddIncomeRecordRequest body) {
        final User user = userLib.fetchRequestUser(request);

        final LocalDateTime paymentTime = body.getPaymentTime() == null ? LocalDateTime.now() : body.getPaymentTime();
        final Account receivingAccount = accountRepository.getUserAccountById(user, body.getReceivingAccountId())
                .orElseThrow(() -> new IllegalStateException("Receiving account not found!"));
        final RecordCategory recordCategory = recordCategoryRepository.getRecordCategoryByName(body.getCategory())
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

    @Override
    @Transactional
    public RecordResponse addExpenseRecord(HttpServletRequest request, AddExpenseRecordRequest body) {
        final User user = userLib.fetchRequestUser(request);

        final LocalDateTime paymentTime = body.getPaymentTime() == null ? LocalDateTime.now() : body.getPaymentTime();
        final Account withdrawalAccount = accountRepository.getUserAccountById(user, body.getWithdrawalAccountId())
                .orElseThrow(() -> new IllegalStateException("Withdrawal account not found!"));
        final RecordCategory recordCategory = recordCategoryRepository.getRecordCategoryByName(body.getCategory())
                .getFirst();

        if(withdrawalAccount.getCurrentBalance() - body.getAmount() < 0){
            throw new IllegalStateException("Insufficient balance on account");
        }

        final Record record = Record.builder()
                .amount(body.getAmount())
                .user(user)
                .paymentTime(paymentTime)
                .category(recordCategory)
                .type(RecordType.EXPENSE)
                .note(body.getNote())
                .currency(withdrawalAccount.getCurrency())
                .withdrawalAccount(withdrawalAccount)
                .build();
        recordRepository.save(record);
        accountRepository.updateAccountCurrentBalance(body.getWithdrawalAccountId(), -body.getAmount());

        return RecordResponse.from(record);
    }

    @Override
    @Transactional
    public RecordResponse addTransferRecord(HttpServletRequest request, AddTransferRecordRequest body) {
        final User user = userLib.fetchRequestUser(request);

        final LocalDateTime paymentTime = body.getPaymentTime() == null ? LocalDateTime.now() : body.getPaymentTime();
        final Account withdrawalAccount = accountRepository.getUserAccountById(user, body.getWithdrawalAccountId())
                .orElseThrow(() -> new IllegalStateException("Withdrawal account not found!"));
        final Account receivingAccount = accountRepository.getUserAccountById(user, body.getReceivingAccountId())
                .orElseThrow(() -> new IllegalStateException("Receiving account not found!"));

        if(withdrawalAccount.getCurrentBalance() - body.getAmount() < 0){
            throw new IllegalStateException("Insufficient balance on account");
        }

        if(!withdrawalAccount.getCurrency().equals(receivingAccount.getCurrency())){
            throw new IllegalStateException("Currencies of both account must be the same");
        }

        final Record record = Record.builder()
                .amount(body.getAmount())
                .user(user)
                .paymentTime(paymentTime)
                .type(RecordType.TRANSFER)
                .note(body.getNote())
                .currency(withdrawalAccount.getCurrency())
                .withdrawalAccount(withdrawalAccount)
                .receivingAccount(receivingAccount)
                .build();
        recordRepository.save(record);
        accountRepository.updateAccountCurrentBalance(body.getWithdrawalAccountId(), -body.getAmount());
        accountRepository.updateAccountCurrentBalance(body.getReceivingAccountId(), body.getAmount());

        return RecordResponse.from(record);
    }

    @Transactional
    public RecordResponse updateRecord(HttpServletRequest request, UpdateRecordRequest body, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Record record = recordRepository.getUserRecordById(user, id)
                .orElseThrow(() -> new IllegalStateException("Record is not found!"));
        record.setNote(body.getNote());
        record.setPaymentTime(body.getPaymentTime());

        if(body.getCategory() != null){
            final RecordCategory recordCategory = recordCategoryRepository
                    .getRecordCategoryByName(body.getCategory()).getFirst();
            record.setCategory(recordCategory);
        }

        final Record updatedRecord = recordRepository.save(record);

        return RecordResponse.from(updatedRecord);
    }

    @Override
    @Transactional
    public Integer deleteRecord(HttpServletRequest request, UUID id) {
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

        return recordRepository.deleteRecordById(id);
    }

    @Override
    public List<RecordCategory> getRecordCategories(){
    return recordCategoryRepository.getAllRecordCategories();
    }
}
