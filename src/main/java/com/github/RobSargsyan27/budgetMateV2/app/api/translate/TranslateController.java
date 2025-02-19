package com.github.RobSargsyan27.budgetMateV2.app.api.translate;

import com.github.RobSargsyan27.budgetMateV2.app.api.translate.request.TranslateRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.translate.response.TranslateResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.translate.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/translate")
public class TranslateController {

    private final TranslateService translateService;

    @PostMapping()
    public ResponseEntity<TranslateResponse> translate(
            @RequestParam(defaultValue = "en") String lang,
            @RequestBody TranslateRequest request) {
        return ResponseEntity.ok(translateService.getTextTranslation(lang, request));
    }
}
