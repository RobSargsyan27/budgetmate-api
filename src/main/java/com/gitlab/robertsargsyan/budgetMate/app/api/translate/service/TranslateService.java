package com.gitlab.robertsargsyan.budgetMate.app.api.translate.service;

import com.gitlab.robertsargsyan.budgetMate.app.api.translate.request.TranslateRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.translate.response.TranslateResponse;

public interface TranslateService {
    TranslateResponse getTextTranslation(String lang, TranslateRequest request);
}
