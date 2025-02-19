package com.gitlab.robertsargsyan.budgetMate.app.api.record.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class AddTransferRecordRequest {

    @NotEmpty(message = "User should be specified!")
    private String userId;

    @Positive
    private double amount;

    private LocalDateTime paymentTime;

    private String note;

    @NotNull
    private String withdrawalAccountId;

    @NotNull
    private String receivingAccountId;
}
