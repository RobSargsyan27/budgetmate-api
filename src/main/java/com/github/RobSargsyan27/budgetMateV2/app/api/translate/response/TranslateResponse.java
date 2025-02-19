package com.github.RobSargsyan27.budgetMateV2.app.api.translate.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class TranslateResponse {
    Map<String, String> translations;
}
