package com.gitlab.robertsargsyan.budgetMate.app.api.record.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateRecordRequest {

    private LocalDateTime paymentTime;

    private String category;

    private String note;
}
