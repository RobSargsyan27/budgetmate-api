package budgetMate.api.lib;

import budgetMate.api.api.records.request.AddRecordRequest;
import budgetMate.api.api.records.response.RecordReportResponse;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.Record;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.RecordType;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.RecordCategoryRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecordLib {
    private final FetchLib fetchLib;
    private final RecordCategoryRepository recordCategoryRepository;
    private final AccountRepository accountRepository;

    /**
     * <h2>Generate records report.</h2>
     * @param records {List<Record>}
     * @return {File}
     */
    public File generateRecordsReport(List<Record> records) {
        Gson gson = new Gson();
        File file = new File("records-report.json");

        List<RecordReportResponse> reportRecords = RecordReportResponse.from(records);
        String jsonReport = gson.toJson(reportRecords);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonReport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    /**
     * <h2>Build record.</h2>
     * @param user {User}
     * @param body {AddRecordRequest}
     * @return {Record}
     */
    public Record buildRecord(User user, AddRecordRequest body) {
        final RecordType recordType = body.getType();

        return switch (recordType) {
            case INCOME -> this.buildIncomeRecord(user, body);
            case EXPENSE -> this.buildExpenseRecord(user, body);
            case TRANSFER -> this.buildTransferRecord(user, body);
        };
    }

    /**
     * <h2>Build income record.</h2>
     * @param user {User}
     * @param body {AddRecordRequest}
     * @return {Record}
     */
    private Record buildIncomeRecord(User user, AddRecordRequest body){
        final LocalDateTime paymentTime = body.getPaymentTime() == null ? LocalDateTime.now() : body.getPaymentTime();
        final RecordCategory recordCategory = fetchLib.fetchResource(
                recordCategoryRepository.getRecordCategoryByName(body.getCategory()),
                "Record Category");
        final Account receivingAccount = fetchLib.fetchResource(
                accountRepository.getUserAccountById(user, body.getReceivingAccountId()),
                "Receiving account not found!");

        return Record.builder()
                .amount(body.getAmount())
                .user(user)
                .paymentTime(paymentTime)
                .category(recordCategory)
                .type(RecordType.INCOME)
                .note(body.getNote())
                .currency(receivingAccount.getCurrency())
                .receivingAccount(receivingAccount)
                .build();
    }

    /**
     * <h2>Build expense record.</h2>
     * @param user {User}
     * @param body {AddRecordRequest}
     * @return {Record}
     */
    private Record buildExpenseRecord(User user, AddRecordRequest body){
        final LocalDateTime paymentTime = body.getPaymentTime() == null ? LocalDateTime.now() : body.getPaymentTime();
        final RecordCategory recordCategory = fetchLib.fetchResource(
                recordCategoryRepository.getRecordCategoryByName(body.getCategory()),
                "Record Category");
        final Account withdrawalAccount = fetchLib.fetchResource(
                accountRepository.getUserAccountById(user, body.getWithdrawalAccountId()),
                "Withdrawal account");

        if(withdrawalAccount.getCurrentBalance() - body.getAmount() < 0){
            throw new IllegalStateException("Insufficient balance on withdrawal account");
        }

        return Record.builder()
                .amount(body.getAmount())
                .user(user)
                .paymentTime(paymentTime)
                .category(recordCategory)
                .type(RecordType.EXPENSE)
                .note(body.getNote())
                .currency(withdrawalAccount.getCurrency())
                .withdrawalAccount(withdrawalAccount)
                .build();
    }

    /**
     * <h2>Build transfer record.</h2>
     * @param user {User}
     * @param body {AddRecordRequest}
     * @return {Record}
     */
    private Record buildTransferRecord(User user, AddRecordRequest body){
        final LocalDateTime paymentTime = body.getPaymentTime() == null ? LocalDateTime.now() : body.getPaymentTime();
        final Account withdrawalAccount = fetchLib.fetchResource(
                accountRepository.getUserAccountById(user, body.getWithdrawalAccountId()),
                "Withdrawal account not found!");
        final Account receivingAccount = fetchLib.fetchResource(
                accountRepository.getUserAccountById(user, body.getReceivingAccountId()),
                "Receiving account not found!");

        if(withdrawalAccount.getCurrentBalance() - body.getAmount() < 0){
            throw new IllegalStateException("Insufficient balance on withdrawal account");
        }

        return Record.builder()
                .amount(body.getAmount())
                .user(user)
                .paymentTime(paymentTime)
                .type(RecordType.TRANSFER)
                .note(body.getNote())
                .currency(withdrawalAccount.getCurrency())
                .withdrawalAccount(withdrawalAccount)
                .receivingAccount(receivingAccount)
                .build();
    }
}
