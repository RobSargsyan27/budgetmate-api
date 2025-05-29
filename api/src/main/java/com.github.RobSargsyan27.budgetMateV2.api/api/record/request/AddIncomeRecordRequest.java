package com.github.RobSargsyan27.budgetMateV2.api.api.record.request;

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
public class AddIncomeRecordRequest {
    @NotEmpty(message = "User should be specified!")
    private String userId;

    @Positive
    private double amount;

    private LocalDateTime paymentTime;

    @NotNull
    private String category;

    private String note;

    @NotNull
    private String receivingAccountId;
}
