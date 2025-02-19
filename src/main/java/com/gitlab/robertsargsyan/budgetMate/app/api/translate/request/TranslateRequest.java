package com.gitlab.robertsargsyan.budgetMate.app.api.translate.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class TranslateRequest {
    private List<String> originIds;
}
