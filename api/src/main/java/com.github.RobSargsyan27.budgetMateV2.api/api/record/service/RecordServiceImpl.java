package com.github.RobSargsyan27.budgetMateV2.api.api.record.service;

import com.github.RobSargsyan27.budgetMateV2.app.api.record.request.*;
import com.github.RobSargsyan27.budgetMateV2.api.api.record.response.RecordResponse;
import com.github.RobSargsyan27.budgetMateV2.api.domain.Account;
import com.github.RobSargsyan27.budgetMateV2.api.domain.Record;
import com.github.RobSargsyan27.budgetMateV2.api.domain.RecordCategory;
import com.github.RobSargsyan27.budgetMateV2.api.domain.User;
import com.github.RobSargsyan27.budgetMateV2.api.domain.enums.RecordType;
import com.github.RobSargsyan27.budgetMateV2.api.lib.RecordLib;
import com.github.RobSargsyan27.budgetMateV2.api.lib.UserLib;
import com.github.RobSargsyan27.budgetMateV2.api.repository.AccountRepository;
import com.github.RobSargsyan27.budgetMateV2.api.repository.RecordCategoryRepository;
import com.github.RobSargsyan27.budgetMateV2.api.repository.specification.RecordFilterSpecification;
import com.github.RobSargsyan27.budgetMateV2.api.repository.RecordRepository;
import com.github.RobSargsyan27.budgetMateV2.api.repository.UserRepository;
import com.github.RobSargsyan27.budgetMateV2.api.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RecordRepository recordRepository;
    private final RecordFilterSpecification recordFilterSpecification;
    private final RecordCategoryRepository recordCategoryRepository;
    private final UserLib userLib;
    private final RecordLib recordLib;
    private final FileUtil fileUtil;

    @Override
    public RecordResponse getUserRecord(String id){
        final UUID recordId = UUID.fromString(id);

        final Record record = recordRepository.getRecordById(recordId).
                orElseThrow(() -> new IllegalStateException("Record is not found!"));

        return recordLib.buildRecordResponse(record);
    }


    @Override
    public List<RecordResponse> getUserRecords(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);
        final List<Record> records = recordRepository.getRecordsByUser(user);

        return records.stream().map(recordLib::buildRecordResponse).toList();
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

        return filteredRecords.stream().map(recordLib::buildRecordResponse).toList();
    }

    @Override
    @Transactional
    public RecordResponse addIncomeRecord(AddIncomeRecordRequest request) {
        final UUID userId = UUID.fromString(request.getUserId());
        final UUID receivingAccountId = UUID.fromString(request.getReceivingAccountId());
        final double amount = request.getAmount();
        final String recordCategoryName = request.getCategory();
        final String note = request.getNote();
        final LocalDateTime paymentTime = request.getPaymentTime() == null ? LocalDateTime.now() : request.getPaymentTime();

        final User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        final Account receivingAccount = accountRepository.getAccountById(receivingAccountId)
                .orElseThrow(() -> new IllegalStateException("Receiving account not found!"));
        final RecordCategory recordCategory = recordCategoryRepository.getRecordCategoryByName(recordCategoryName)
                .getFirst();

        final Record record = Record.builder()
                .amount(amount)
                .user(user)
                .paymentTime(paymentTime)
                .category(recordCategory)
                .type(RecordType.INCOME)
                .note(note)
                .currency(receivingAccount.getCurrency())
                .receivingAccount(receivingAccount)
                .build();

        recordRepository.save(record);
        accountRepository.addUpAccountCurrentBalance(amount, receivingAccountId);

        return RecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .paymentTime(record.getPaymentTime())
                .userId(record.getUser().getId())
                .category(record.getCategory())
                .type(record.getType())
                .note(record.getNote())
                .currency(record.getCurrency())
                .receivingAccountName(receivingAccount.getName())
                .receivingAccountId(receivingAccount.getId())
                .build();
    }

    @Override
    @Transactional
    public RecordResponse addExpenseRecord(AddExpenseRecordRequest request) {
        final UUID userId = UUID.fromString(request.getUserId());
        final double amount = request.getAmount();
        final String recordCategoryName = request.getCategory();
        final String note = request.getNote();
        final UUID withdrawalAccountId = UUID.fromString(request.getWithdrawalAccountId());
        final LocalDateTime paymentTime = request.getPaymentTime() == null ? LocalDateTime.now() : request.getPaymentTime();

        final User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        final Account withdrawalAccount = accountRepository.getAccountById(withdrawalAccountId)
                .orElseThrow(() -> new IllegalStateException("Withdrawal account not found!"));
        final RecordCategory recordCategory = recordCategoryRepository.getRecordCategoryByName(recordCategoryName)
                .getFirst();

        if(withdrawalAccount.getCurrentBalance() - amount < 0){
            throw new IllegalStateException("Insufficient balance on account");
        }

        final Record record = Record.builder()
                .amount(amount)
                .user(user)
                .paymentTime(paymentTime)
                .category(recordCategory)
                .type(RecordType.EXPENSE)
                .note(note)
                .currency(withdrawalAccount.getCurrency())
                .withdrawalAccount(withdrawalAccount)
                .build();

        recordRepository.save(record);
        accountRepository.withdrawFromAccountCurrentBalance(amount, withdrawalAccountId);

        return RecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .paymentTime(record.getPaymentTime())
                .userId(record.getUser().getId())
                .category(record.getCategory())
                .type(record.getType())
                .note(record.getNote())
                .currency(record.getCurrency())
                .withdrawalAccountName(withdrawalAccount.getName())
                .withdrawalAccountId(withdrawalAccount.getId())
                .build();
    }

    @Override
    @Transactional
    public RecordResponse addTransferRecord(AddTransferRecordRequest request) {
        final UUID userId = UUID.fromString(request.getUserId());
        final double amount = request.getAmount();
        final String note = request.getNote();
        final UUID withdrawalAccountId = UUID.fromString(request.getWithdrawalAccountId());
        final UUID receivingAccountId = UUID.fromString(request.getReceivingAccountId());
        final LocalDateTime paymentTime = request.getPaymentTime() == null ? LocalDateTime.now() : request.getPaymentTime();

        final User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        final Account withdrawalAccount = accountRepository.getAccountById(withdrawalAccountId)
                .orElseThrow(() -> new IllegalStateException("Withdrawal account not found!"));
        final Account receivingAccount = accountRepository.getAccountById(receivingAccountId)
                .orElseThrow(() -> new IllegalStateException("Receiving account not found!"));

        if(withdrawalAccount.getCurrentBalance() - amount < 0){
            throw new IllegalStateException("Insufficient balance on account");
        }

        if(!withdrawalAccount.getCurrency().equals(receivingAccount.getCurrency())){
            throw new IllegalStateException("Currencies of both account must be the same");
        }

        final Record record = Record.builder()
                .amount(amount)
                .user(user)
                .paymentTime(paymentTime)
                .type(RecordType.TRANSFER)
                .note(note)
                .currency(withdrawalAccount.getCurrency())
                .withdrawalAccount(withdrawalAccount)
                .receivingAccount(receivingAccount)
                .build();

        recordRepository.save(record);
        accountRepository.withdrawFromAccountCurrentBalance(amount, withdrawalAccountId);
        accountRepository.addUpAccountCurrentBalance(amount, receivingAccountId);

        return RecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .paymentTime(record.getPaymentTime())
                .userId(record.getUser().getId())
                .type(record.getType())
                .note(record.getNote())
                .currency(record.getCurrency())
                .withdrawalAccountName(withdrawalAccount.getName())
                .withdrawalAccountId(withdrawalAccount.getId())
                .receivingAccountName(receivingAccount.getName())
                .receivingAccountId(receivingAccount.getId())
                .build();
    }

    @Transactional
    public RecordResponse updateRecord(UpdateRecordRequest request, String id){
        final UUID recordId = UUID.fromString(id);

        final Record record = recordRepository.getRecordById(recordId)
                .orElseThrow(() -> new IllegalStateException("Record is not found!"));
        record.setNote(request.getNote());
        record.setPaymentTime(request.getPaymentTime());

        if(request.getCategory() != null){
            final RecordCategory recordCategory = recordCategoryRepository
                    .getRecordCategoryByName(request.getCategory()).getFirst();
            record.setCategory(recordCategory);
        }

        final Record updatedRecord = recordRepository.save(record);

        return recordLib.buildRecordResponse(updatedRecord);
    }

    @Override
    @Transactional
    public Integer deleteRecord(String id) {
        final UUID recordId = UUID.fromString(id);

        final Record record = recordRepository.getRecordById(recordId)
                .orElseThrow(() -> new IllegalStateException("Record is not found!"));

        switch (record.getType()){
            case RecordType.INCOME:
                accountRepository.withdrawFromAccountCurrentBalance(record.getAmount(), record.getReceivingAccount().getId());
                break;
            case RecordType.EXPENSE:
                accountRepository.addUpAccountCurrentBalance(record.getAmount(), record.getWithdrawalAccount().getId());
                break;
            case RecordType.TRANSFER:
                accountRepository.withdrawFromAccountCurrentBalance(record.getAmount(), record.getReceivingAccount().getId());
                accountRepository.addUpAccountCurrentBalance(record.getAmount(), record.getWithdrawalAccount().getId());
        }

        return recordRepository.deleteRecordById(recordId);
    }

    @Override
    public List<RecordCategory> getRecordCategories(){
    return recordCategoryRepository.getAllRecordCategories();
    }
}
