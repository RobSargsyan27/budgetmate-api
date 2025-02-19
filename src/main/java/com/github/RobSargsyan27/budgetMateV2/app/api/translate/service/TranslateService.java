package com.github.RobSargsyan27.budgetMateV2.app.api.translate.service;

import com.github.RobSargsyan27.budgetMateV2.app.api.translate.request.TranslateRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.translate.response.TranslateResponse;

public interface TranslateService {
    TranslateResponse getTextTranslation(String lang, TranslateRequest request);
}
