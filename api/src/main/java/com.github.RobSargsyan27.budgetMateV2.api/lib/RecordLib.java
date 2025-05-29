package com.github.RobSargsyan27.budgetMateV2.api.lib;

import com.github.RobSargsyan27.budgetMateV2.app.api.account.response.AccountResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.record.response.RecordReportResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.record.response.RecordResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.response.UserResponse;
import com.github.RobSargsyan27.budgetMateV2.api.domain.Record;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class RecordLib {
    public RecordResponse buildRecordResponse(Record record) {
        final String receivingAccountName = record.getReceivingAccount() == null ? null : record.getReceivingAccount().getName();
        final String withdrawalAccountName = record.getWithdrawalAccount() == null ? null : record.getWithdrawalAccount().getName();

        final UUID receivingAccountId = record.getReceivingAccount() == null ? null : record.getReceivingAccount().getId();
        final UUID withdrawalAccountId = record.getWithdrawalAccount() == null ? null : record.getWithdrawalAccount().getId();

        return RecordResponse.builder()
                .id(record.getId())
                .userId(record.getUser().getId())
                .amount(record.getAmount())
                .category(record.getCategory())
                .currency(record.getCurrency())
                .type(record.getType())
                .note(record.getNote())
                .paymentTime(record.getPaymentTime())
                .withdrawalAccountName(withdrawalAccountName)
                .withdrawalAccountId(withdrawalAccountId)
                .receivingAccountName(receivingAccountName)
                .receivingAccountId(receivingAccountId)
                .build();
    }

    public File generateRecordsReport(List<Record> recordList) {
        Gson gson = new Gson();
        File file = new File("records-report.json");
        List<RecordReportResponse> records = this.buildRecordReportResponse(recordList);

        String jsonReport = gson.toJson(records);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonReport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    public List<RecordReportResponse> buildRecordReportResponse(List<Record> recordList) {
        return recordList.stream().map(record -> {
            final UserResponse userResponse = UserResponse.builder()
                    .id(record.getUser().getId().toString())
                    .username(record.getUser().getUsername())
                    .firstname(record.getUser().getFirstname())
                    .lastname(record.getUser().getLastname())
                    .country(record.getUser().getCountry())
                    .city(record.getUser().getCity())
                    .address(record.getUser().getAddress())
                    .postalCode(record.getUser().getPostalCode())
                    .avatarColor(record.getUser().getAvatarColor())
                    .role(record.getUser().getRole())
                    .build();

            final AccountResponse receivingAccount = record.getReceivingAccount() != null ?
                    AccountResponse.builder()
                            .id(record.getReceivingAccount().getId())
                            .name(record.getReceivingAccount().getName())
                            .currentBalance(record.getReceivingAccount().getCurrentBalance())
                            .currency(record.getReceivingAccount().getCurrency())
                            .type(record.getReceivingAccount().getType())
                            .avatarColor(record.getReceivingAccount().getAvatarColor())
                            .build()
                    : null;

            final AccountResponse withdrawalAccount = record.getWithdrawalAccount() != null ?
                    AccountResponse.builder()
                            .id(record.getWithdrawalAccount().getId())
                            .name(record.getWithdrawalAccount().getName())
                            .currentBalance(record.getWithdrawalAccount().getCurrentBalance())
                            .currency(record.getWithdrawalAccount().getCurrency())
                            .type(record.getWithdrawalAccount().getType())
                            .avatarColor(record.getWithdrawalAccount().getAvatarColor())
                            .build()
                    : null;

            return RecordReportResponse.builder()
                    .id(record.getId())
                    .amount(record.getAmount())
                    .user(userResponse)
                    .category(record.getCategory())
                    .type(record.getType())
                    .note(record.getNote())
                    .currency(record.getCurrency())
                    .paymentTime(record.getPaymentTime().toString())
                    .receivingAccount(receivingAccount)
                    .withdrawalAccount(withdrawalAccount)
                    .build();
        }).toList();
    }
}
