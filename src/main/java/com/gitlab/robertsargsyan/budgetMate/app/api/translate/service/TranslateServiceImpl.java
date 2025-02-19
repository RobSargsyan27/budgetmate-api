package com.gitlab.robertsargsyan.budgetMate.app.api.translate.service;

import com.gitlab.robertsargsyan.budgetMate.app.api.translate.request.TranslateRequest;
import com.gitlab.robertsargsyan.budgetMate.app.api.translate.response.TranslateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslateServiceImpl implements TranslateService {
    private final MessageSource messageSource;

    @Override
    public TranslateResponse getTextTranslation(String lang, TranslateRequest request) {
        Locale locale = new Locale(lang);

        final Map<String, String> translations = request.getOriginIds().stream().collect(
                Collectors.toMap(
                        id -> id,
                        id -> messageSource.getMessage(id, null, locale)
                ));

        return TranslateResponse.builder()
                .translations(translations)
                .build();
    }
}
